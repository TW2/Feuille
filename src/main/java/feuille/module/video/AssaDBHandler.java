package feuille.module.video;

import javax.imageio.ImageIO;
import javax.swing.event.EventListenerList;
import java.io.File;
import java.io.IOException;

// Try to fill DB with image at time t
// Must be delayed in playback
public class AssaDBHandler implements Runnable {

    private Thread thread;

    private final AssaDB assaDB;

    private double seconds;
    private int width;
    private int height;
    private final AssaRenderer assaRenderer;
    private final String temporaryAssFilename;
    private final String temporaryImgFilename;
    private volatile boolean running = false;

    public AssaDBHandler(String temporaryAssFilename,
                   String temporaryImgFilename) {
        seconds = -1d;
        width = 1280;
        height = 720;
        this.temporaryAssFilename = temporaryAssFilename;
        this.temporaryImgFilename = temporaryImgFilename;
        this.assaRenderer = new AssaRenderer();

        //============================================================
        //== IMAGE DATABASE FOR RGBA PNG SUBTITLES                  ==
        //============================================================
        // Get the settings folder
        File app = new File(new File("").getAbsolutePath());
        File folder = new File(app + "/settings/assa");
        // Initialize the DB
        assaDB = new AssaDB(
                (new File(folder, "assa.db").toPath()),
                "assa"
        );
        // Remove old artifacts
        assaDB.clear();
        // Create a new DB for the video playback
        assaDB.create();

        // Callback to add images to db (blob)
        addAssaListener(new AssaListener() {
            @Override
            public void getAssaEvent(AssaEvent assaEvent) {
                assaDB.add(
                        (long) (assaEvent.getSeconds() * 1000L),
                        assaEvent.getImage()
                );
            }
        });
        //============================================================
    }

    public void stopGrabbing() {
        running = false;
        if(thread != null) {
            if(thread.isAlive() || !thread.isInterrupted()) {
                thread.interrupt();
                thread = null;
            }
        }
    }

    public void startGrabbing(double seconds, int width, int height) {
        this.seconds = seconds;
        this.width = width;
        this.height = height;
        if(thread != null) {
            stopGrabbing();
        }
        thread = new Thread(this);
        running = true;
        thread.start();
    }

    public void restartGrabbing() {
        if(thread != null) {
            stopGrabbing();
        }
        thread = new Thread(this);
        running = true;
        thread.start();
    }

    @Override
    public void run() {
        while (running) {
            if(seconds != -1d){
                try {
                    fillDatabase();
                } catch (IOException _) {
                    stopGrabbing();
                }
            }
        }
    }

    private void fillDatabase() throws IOException {
        boolean result = assaRenderer.getASSA().set_and_get(
                temporaryImgFilename,
                temporaryAssFilename,
                seconds,
                width,
                height
        );

        if(result){
            boolean res = false;
            File imageFile = new File(temporaryImgFilename);
            if(imageFile.exists()){
                res = imageFile.delete();
            }
            if(res){
                AssaEvent ev = new AssaEvent(seconds, ImageIO.read(new File(temporaryImgFilename)));
                fireAssaImage(ev);
                seconds += 0.002d; // TODO: search with fps
            }
        }
    }

    private final EventListenerList listeners = new EventListenerList();

    public void addAssaListener(AssaInterface listener){
        listeners.add(AssaListener.class, (AssaListener)listener);
    }

    public void removeAssaListener(AssaInterface listener){
        listeners.remove(AssaListener.class, (AssaListener)listener);
    }

    public Object[] getListeners(){
        return listeners.getListenerList();
    }

    protected void fireAssaImage(AssaEvent event){
        for(Object o : getListeners()){
            if(o instanceof AssaListener listen){
                listen.getAssaEvent(event);
                break;
            }
        }
    }
}
