/*
 * Copyright (C) 2019 util2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package feuille.video;

import feuille.MainFrame;
import feuille.exception.AudioVideoErrorException;
import feuille.io.Event;
import feuille.listener.FrameEvent;
import feuille.listener.FrameListener;
import feuille.util.Time;
import feuille.util.VideoBag;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.event.EventListenerList;
import org.bytedeco.javacpp.*;
import static org.bytedeco.javacpp.avcodec.*;
import static org.bytedeco.javacpp.avformat.*;
import static org.bytedeco.javacpp.avutil.*;
import static org.bytedeco.javacpp.swscale.*;

/**
 *
 * @author util2
 */
public class Reader implements Runnable {
    
    public enum PlayerOption {
        Audio("Audio only"),
        Video("Video only"),
        Both("Audio and video");
        
        String state;
        
        PlayerOption(String state){
            this.state = state;
        }

        public String getState() {
            return state;
        }

        @Override
        public String toString() {
            return state;
        }        
    }
    
    //==========================================================================
    // Java variables
    //==========================================================================
    
    private File fileToRead = null;
    private Thread mainThread = null;
    private volatile boolean running = false;
    private PlayerOption playerOption = PlayerOption.Both;
    
    // There is a bug for thread interrupt in the code
    // So adjust it with some booleans that leads this logic
    // of start, pause, stop
    private volatile boolean videoAlive = false;
    
    // Add values for the control of frames to read
    private Time fromStartTime = Time.create(0L);
    private Time toEndTime = Time.create(0L);
    private int frameStart = 0;
    private int frameStop = 0;
    private long current_pts = 0L;
    
    //==========================================================================
    // FFMpeg 4.0 - Video and Audio
    //==========================================================================
    
    private AVFormatContext         pFormatCtx = new AVFormatContext(null);
    private AVDictionary            OPTIONS_DICT = null;
    private AVPacket                pPacket = new AVPacket();
    
    //==========================================================================
    // FFMpeg 4.0 - Video
    //==========================================================================
    
    private int                     videoStream;
    private AVCodecContext          pVideoCodecCtx = null;
    private AVCodec                 pVideoCodec = null;
    private AVFrame                 pVideoFrame = null;
    private AVFrame                 pVideoFrameRGB = null;
    private BytePointer             video_buffer = null;
    private int                     video_numBytes;    
    private SwsContext              video_sws_ctx = null;
    
    //==========================================================================
    // FFMpeg 4.0 - Audio
    //==========================================================================
    
    private AVCodec                 pAudioCodec;
    private AVCodecContext          pAudioCodecCtx;
    private int                     audioStream = -1;
    private int                     audio_data_size;
    private BytePointer             audio_data = new BytePointer(0);
    private int                     audio_ret;
    private AVFrame                 pAudioDecodedFrame = null;
    private AVCodecParserContext    pAudioParser;
    
    //==========================================================================
    // FFFMpeg 4.0 - Subtitles
    //==========================================================================
    
    private AVCodec                         pSubtitleCodec = null;
    private AVCodecContext                  pSubtitleCodecCtx = null;
    private AVSubtitle                      pSubtitleEventIn = null;
    private AVSubtitle                      pSubtitleEventOut = null;
    private AVPacket                        pSubtitlePacket = null;
    private AVDictionary                    optionsSubtitle = null;
    
    //==========================================================================
    // Main stuffs
    //==========================================================================
    
    public Reader(){
        
    }
    
    public void setFile(File fileToRead, PlayerOption playerOption){
        this.fileToRead = fileToRead;
        this.playerOption = playerOption;
        try {
            getInfos();
        } catch (AudioVideoErrorException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setPlayingArea(Time from, Time to){
        fromStartTime = from == null ? Time.create(0L) : from;
        toEndTime = to == null ? Time.create(0L) : to;
    }
    
    public void setPlayingArea(int from, int to){
        frameStart = from;
        frameStop = to;
    }
    
    @Override
    public void run() {
        while(true){
            if(running == true){
                try {
                    play_v4_0();
                } catch (AudioVideoErrorException ex) {
                    Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="Start, pause and stop">
    public void startMedia() throws AudioVideoErrorException{
        if(fileToRead != null && mainThread == null){
            // Thread controller
            running = true;
            videoAlive = true;           
            // Thread launcher 
            mainThread = new Thread(this);
            mainThread.start();
        }else if(fileToRead != null){
            // Thread controller
            running = true;
            videoAlive = true;
        }
    }
    
    public void pauseMedia(){
        if(fileToRead != null && mainThread != null && videoAlive == true){
            videoAlive = false;
        }else if(fileToRead != null && mainThread != null && videoAlive == false){
            videoAlive = true;
        }
    }
    
    public void stopMedia(){
        if(fileToRead != null && mainThread != null && videoAlive == true){
            running = false;
            videoAlive = false;
            setPlayingArea(Time.create(0L), Time.create(1L)); // Return to beginning
        }
    }
    // </editor-fold>
    
    //==========================================================================
    // FFMpeg 4.0 processing
    //==========================================================================
    
    // <editor-fold defaultstate="collapsed" desc="Play audio/video (FFMpeg4.0)">
    public void getInfos() throws AudioVideoErrorException {
        
        // Initialize packet and check for error
        pPacket = av_packet_alloc();
        if(pPacket == null){
            throw new AudioVideoErrorException("ALL: Couldn't allocate packet");
        }
        
        // Register all formats and codecs
        av_register_all();

        // Open video file
        if (avformat_open_input(pFormatCtx, fileToRead.getPath(), null, null) != 0) {
            throw new AudioVideoErrorException("ALL: Couldn't open file");
        }

        // Retrieve stream information
        if (avformat_find_stream_info(pFormatCtx, (PointerPointer)null) < 0) {
            throw new AudioVideoErrorException("ALL: Couldn't find stream information");
        }

        // Dump information about file onto standard error
        av_dump_format(pFormatCtx, 0, fileToRead.getPath(), 0);

        // Find the first audio/video stream
        audioStream = -1;
        videoStream = -1;
        for (int i = 0; i < pFormatCtx.nb_streams(); i++) {
            if (pFormatCtx.streams(i).codec().codec_type() == AVMEDIA_TYPE_AUDIO && audioStream == -1) {
                audioStream = i;
            } else if (pFormatCtx.streams(i).codec().codec_type() == AVMEDIA_TYPE_VIDEO && videoStream == -1) {
                videoStream = i;
            }
            if (videoStream != -1 && audioStream != -1) {
                break;
            }
        }
        switch(playerOption){
            case Audio:
                if(audioStream == -1){
                    throw new AudioVideoErrorException("Didn't find an audio stream");
                }
                break;
            case Video:
                if(videoStream == -1){
                    throw new AudioVideoErrorException("Didn't find a video stream");
                }
                break;
            case Both:
                if(audioStream == -1 && videoStream == -1){
                    String message;
                    if(audioStream == -1 && videoStream == -1){
                        message = "Didn't find an audio or a video stream";
                    }else if(audioStream == -1){
                        message = "Didn't find an audio stream";
                    }else if(videoStream == -1){
                        message = "Didn't find a video stream";
                    }else{
                        message = "Errors with streams";
                    }
                    throw new AudioVideoErrorException(message);
                }
                break;
        }

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // VIDEO
        //------------------------------------------------------------------

        // Get a pointer to the codec context for the video stream
        pVideoCodecCtx = pFormatCtx.streams(videoStream).codec();

        // Find the decoder for the video stream
        pVideoCodec = avcodec_find_decoder(pVideoCodecCtx.codec_id());
        if (pVideoCodec == null) {
            throw new AudioVideoErrorException("VIDEO: Unsupported codec or not found!");
        }
        // Open codec
        if (avcodec_open2(pVideoCodecCtx, pVideoCodec, OPTIONS_DICT) < 0) {
            throw new AudioVideoErrorException("VIDEO: Could not open codec");
        }

        // Allocate video frame
        pVideoFrame = av_frame_alloc();

        // Allocate an AVFrame structure
        pVideoFrameRGB = av_frame_alloc();
        if(pVideoFrameRGB == null) {
            throw new AudioVideoErrorException("VIDEO: Could not allocate frame");
        }

        // Determine required buffer size and allocate buffer
        video_numBytes = avpicture_get_size(AV_PIX_FMT_BGR24,
                pVideoCodecCtx.width(), pVideoCodecCtx.height());
        video_buffer = new BytePointer(av_malloc(video_numBytes));

        video_sws_ctx = sws_getContext(pVideoCodecCtx.width(), pVideoCodecCtx.height(),
                pVideoCodecCtx.pix_fmt(), pVideoCodecCtx.width(), pVideoCodecCtx.height(),
                AV_PIX_FMT_BGR24, SWS_BILINEAR, null, null, (DoublePointer)null);

        // Assign appropriate parts of buffer to image planes in pFrameRGB
        // Note that pFrameRGB is an AVFrame, but AVFrame is a superset
        // of AVPicture
        avpicture_fill(new AVPicture(pVideoFrameRGB), video_buffer, AV_PIX_FMT_BGR24,
                pVideoCodecCtx.width(), pVideoCodecCtx.height());

        // Video treatment end ---------------------------------------------
        //==================================================================

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // AUDIO
        //------------------------------------------------------------------

        // Get a pointer to the codec context for the video stream
        pAudioCodecCtx = pFormatCtx.streams(audioStream).codec();

        /* find audio decoder */    
        pAudioCodec = avcodec_find_decoder(pAudioCodecCtx.codec_id());
        if (pAudioCodec == null) {
            throw new AudioVideoErrorException("AUDIO: Codec not found");
        }

        pAudioCodecCtx = avcodec_alloc_context3(pAudioCodec);
        if (pAudioCodecCtx == null) {
            throw new AudioVideoErrorException("AUDIO: Could not allocate audio codec context");
        }

        /* open it */
        if (avcodec_open2(pAudioCodecCtx, pAudioCodec, OPTIONS_DICT) < 0) {
            throw new AudioVideoErrorException("AUDIO: Could not open codec");
        }
        
        pAudioDecodedFrame = av_frame_alloc();
        if (pAudioDecodedFrame == null){
            throw new AudioVideoErrorException("AUDIO: DecodedFrame allocation failed");
        }
        
        // Audio treatment end ---------------------------------------------
        //==================================================================
        
    }    
    
    public void play_v4_0() throws AudioVideoErrorException{
        
        av_init_packet(pPacket);
        
        if(frameStart == 0 & frameStop == 0){
            definePts(pPacket, fromStartTime.getMilliseconds(), toEndTime.getMilliseconds());
        }else{
            definePts(pPacket, frameStart, frameStop);
        }
        
        // Read frames
        while (videoAlive == true && av_read_frame(pFormatCtx, pPacket) >= 0) {            
            if (pPacket.stream_index() == videoStream && playerOption != PlayerOption.Audio) {
                // Is this a packet from the video stream?   
                decodeVideo(pVideoCodecCtx, pVideoFrame, pPacket);
                renewPacket();
            } else if (pPacket.stream_index() == audioStream && playerOption != PlayerOption.Video) {
                // Is this a packet from the audio stream?
                if(pPacket.size() > 0){
                    decodeAudio(pAudioCodecCtx, pAudioDecodedFrame, pPacket);
                    renewPacket();
                }                
            }
        }

//        freeAllResources();
//
//        // Fire EOF
//        fireEndOfFileReached();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Renewal">
    
    private void renewPacket(){
        // Free the packet that was allocated by av_read_frame
        av_packet_unref(pPacket);

        pPacket.data(null);
        pPacket.size(0);
        av_init_packet(pPacket);
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Free resources">
    public void freeVideo(){
        if(playerOption != PlayerOption.Audio){
            // Free the RGB image
            av_free(video_buffer);
            av_free(pVideoFrameRGB);
            // Free the YUV frame
            av_free(pVideoFrame);
            // Close the codec
            avcodec_close(pVideoCodecCtx);
        }
    }

    public void freeAudio() {
        if(playerOption != PlayerOption.Video){
            avcodec_free_context(pAudioCodecCtx);
            av_parser_close(pAudioParser);
            av_frame_free(pAudioDecodedFrame);
        }            
    }

    public void freeAudioVideo() {
        // Free the packet that was allocated by av_read_frame
        av_free_packet(pPacket);
        // Close the video file
        avformat_close_input(pFormatCtx);
    }

    public void freeAllResources(){
        if(fileToRead != null){
            freeVideo();
            freeAudio();
            freeAudioVideo();
        }        
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Decode video">
    /**
     * Decode the video for FFMPEG 4.0
     * @param pCodecCtx The AVCodecContext* of the video.
     * @param pFrame The AVFrame* that contains decoded image of the video.
     * @param pPacket The AVCodecContext* of the video.
     * @throws AudioVideoErrorException
     */
    private void decodeVideo(AVCodecContext pCodecCtx, AVFrame pFrame, AVPacket pPacket) throws AudioVideoErrorException{
        // Decoding step 1
        int ret = avcodec_send_packet(pCodecCtx, pPacket);

        // Define exception for packet error if ret is negative
        if (ret < 0) {
            throw new AudioVideoErrorException("Error sending a packet for decoding");
        }

        // While ret is positive then let's loop
        while (ret >= 0) {
            // Decoding step 2
            ret = avcodec_receive_frame(pCodecCtx, pFrame);

            // If there is no data anymore then returns otherwise if ret is negative triggers an exception
            if (ret == AVERROR_EAGAIN() || ret == AVERROR_EOF){
                return;
            } else if (ret < 0) {
                throw new AudioVideoErrorException("Error during decoding");
            }

            // Convert the image from its native format to RGB
            sws_scale(video_sws_ctx, pFrame.data(), pFrame.linesize(), 0, pCodecCtx.height(), pVideoFrameRGB.data(), pVideoFrameRGB.linesize()); 


            //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
            // Display the (lonely) frame
            //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

            // Create the bag
            VideoBag vb = new VideoBag();

            // Convert to system Java image from PixelFormat and AVFrame to BufferdImage
            BufferedImage image = GetImage(pVideoFrameRGB, pCodecCtx.width(), pCodecCtx.height());

            // Fill the bag
            vb.setImage(image);
            vb.setWidth(pCodecCtx.width());
            vb.setHeight(pCodecCtx.height());
            vb.setFps(calculateFPS(pPacket));
            vb.setPts(calculatePts(pPacket));
            vb.setDuration(pFormatCtx.duration());
            vb.setTime(vb.getPts() / 1000);
            vb.setFrameNumber(Time.getFrame(vb.getPts(), vb.getFps()));
            vb.setNbFrames(Math.round(vb.getDuration() / 1000 / 1000 * vb.getFps()));
            // Fire
            fireFrameChanged(vb);
            //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
            
            if(vb.getFrameNumber() >= vb.getNbFrames()){
                running = false;
                fireEndOfFileReached();
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Decode audio">
    private void decodeAudio(AVCodecContext pCodecCtx, AVFrame pDecodedFrame, AVPacket pPacket) throws AudioVideoErrorException{
        
        do {
            audio_ret = avcodec_send_packet(pCodecCtx, pPacket);
        } while(audio_ret == AVERROR_EAGAIN());
        System.out.println("packet sent return value: " + audio_ret);
                
        if(audio_ret == AVERROR_EOF || audio_ret == AVERROR_EINVAL()) {
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);
            formatter.format("AVERROR(EAGAIN): %d, AVERROR_EOF: %d, AVERROR(EINVAL): %d\n", AVERROR_EAGAIN(), AVERROR_EOF, AVERROR_EINVAL());
            formatter.format("Audio frame getting error (%d)!\n", audio_ret);
            throw new AudioVideoErrorException(sb.toString());
        }

        audio_ret = avcodec_receive_frame(pCodecCtx, pDecodedFrame);
        System.out.println("frame received return value: " + audio_ret);

        audio_data_size = av_get_bytes_per_sample(pCodecCtx.sample_fmt());

        if (audio_data_size < 0) {
            /* This should not occur, checking just for paranoia */
            throw new AudioVideoErrorException("Failed to calculate data size");
        }
        
        for (int ch = 0; ch < pCodecCtx.channels(); ch++){
            //fwrite(frame->data[ch] + data_size*i, 1, data_size, outfile);
            BytePointer bytePointer = pDecodedFrame.data(ch);

            byte[] bytes = bytePointer.asBuffer().array();

            if(bytes != null){
                AudioFormat af = new AudioFormat(pDecodedFrame.sample_rate(), audio_data_size * pDecodedFrame.nb_samples(), 1, true, false);
                try (SourceDataLine sdl = AudioSystem.getSourceDataLine(af)) {
                    sdl.open(af);                
                    sdl.start();
                    sdl.write(bytes,0,bytes.length);
                    sdl.drain();
                    sdl.stop();
                } catch (LineUnavailableException ex) {
                    Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BGR24 to ARGB">
    private BufferedImage GetImage(AVFrame pFrame, int width, int height){
        BufferedImage img_3BYTE_BGR;
        BufferedImage img_INT_ARGB = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Create output stream
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            // Write pixel data
            BytePointer data = pFrame.data(0);
            byte[] bytes = new byte[width * 3];
            int l = pFrame.linesize(0);
            for(int y = 0; y < height; y++) {
                data.position(y * l).get(bytes);                
                baos.write(bytes);
            }

            // Create image
            img_3BYTE_BGR = feuille.util.Image.createImage(ByteBuffer.wrap(baos.toByteArray()), width, height, BufferedImage.TYPE_3BYTE_BGR);

            // Convert to INT ARGB
            Graphics2D g2d = img_INT_ARGB.createGraphics();
            g2d.drawImage(img_3BYTE_BGR, 0, 0, null);
            g2d.dispose();
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return img_INT_ARGB;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Events">
    //==========================================================================
    // Events
    //==========================================================================

    private final EventListenerList listeners = new EventListenerList();

    public void addFrameListener(FrameListener listener) {
        listeners.add(FrameListener.class, (FrameListener)listener);
    }

    public void removeFrameListener(FrameListener listener) {
        listeners.remove(FrameListener.class, (FrameListener)listener);
    }

    public Object[] getFrameListeners() {
        return listeners.getListenerList();
    }

    protected void fireFrameChanged(VideoBag vb) {
        FrameEvent event = new FrameEvent(vb);
        for(Object o : getFrameListeners()){
            if(o instanceof FrameListener){
                FrameListener afl = (FrameListener)o;
                afl.FrameChanged(event);
                break;
            }
        }
    }    

    protected void fireEndOfFileReached() {
        for(Object o : getFrameListeners()){
            if(o instanceof FrameListener){
                FrameListener afl = (FrameListener)o;
                afl.EndOfFileReached();
                break;
            }
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Utilities">
    
    private double calculateFPS(AVPacket pkt){
        AVStream stream = pFormatCtx.streams(pkt.stream_index());
        double num = stream.avg_frame_rate().num();
        double den = stream.avg_frame_rate().den();
        return num / den;
    }
    
    private long calculatePts(AVPacket pkt){
        AVStream stream = pFormatCtx.streams(pkt.stream_index());
        
        if(pkt.dts() != AV_NOPTS_VALUE) {
            current_pts = av_rescale_q(pkt.pts(), stream.time_base(), av_get_time_base_q());
        }else{
            current_pts = 0;
        }        
        
        return current_pts;
    }
    
    private void definePts(AVPacket pkt, int frameStart, int frameStop){
        double fps = calculateFPS(pkt);
        long msStart = Math.round(frameStart / fps * 1000);
        long msStop = Math.round(frameStop / fps * 1000);
        if(msStart != msStop && msStart >=0 & msStart < msStop & msStop <= pFormatCtx.duration()){
            av_seek_frame(pFormatCtx, pkt.stream_index(), msStart * 1000, AVSEEK_FLAG_FRAME);
            fromStartTime = Time.create(0L);
            toEndTime = Time.create(0L);
        }
    }
    
    private void definePts(AVPacket pkt, long msStart, long msStop){
        if(msStart != msStop && msStart >=0 & msStart < msStop & msStop <= pFormatCtx.duration()){
            av_seek_frame(pFormatCtx, pkt.stream_index(), msStart * 1000, AVSEEK_FLAG_FRAME);
            fromStartTime = Time.create(0L);
            toEndTime = Time.create(0L);
        }
    }
    
    // </editor-fold>
    
    private String prepareAndExtractHeader(){
        // Variable to verify status of elements called
        int ret;

        // Find the decoder for ASS subtitles
        pSubtitleCodec = avcodec_find_decoder(SUBTITLE_ASS);

        // Initialize context
        pSubtitleCodecCtx = avcodec_alloc_context3(pSubtitleCodec);
        BytePointer bp = pSubtitleCodecCtx.subtitle_header();
        String header = bp.getString();

        // Open context
        ret = avcodec_open2(pSubtitleCodecCtx, pSubtitleCodec, optionsSubtitle);

        // Init packet
        pSubtitlePacket = av_packet_alloc();
        
        return header;
    }
    
    private List<Event> getASSFromEvents(VideoBag vb){
        List<Event> events = new ArrayList<>();
        int ret;
            
        // Set packet pts
        pSubtitlePacket.pts(vb.getPts());

        // Try to fill an AVSubtitle (in this case: pSubtitleEventOut)
        ret = avcodec_decode_subtitle2(pSubtitleCodecCtx, pSubtitleEventOut, new IntPointer(0), pSubtitlePacket);
        if(ret > 0){
            // We got a subtitle (ret is not negative - negative = error)
            for(int i=0; i<pSubtitleEventOut.num_rects(); i++){
                AVSubtitleRect pSubRect = pSubtitleEventOut.rects(i);
                Event evt = Event.createFromASS(pSubRect.ass().getString(), MainFrame.getScriptStyles());
                events.add(evt);
            }
        }
        return events;
    }
}

