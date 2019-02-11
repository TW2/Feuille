/*
 * Copyright (C) 2013 Alex Andres
 *
 * This file is part of JavaAV.
 *
 * JavaAV is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version (subject to the "Classpath"
 * exception as provided in the LICENSE file that accompanied
 * this code).
 *
 * JavaAV is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JavaAV. If not, see <http://www.gnu.org/licenses/>.
 */
package feuille.util;

import java.awt.image.BufferedImage;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * From JavaAV (just a few)
 * @author util2
 */
public class Image {
    
    public static BufferedImage createImage(ByteBuffer data, int width, int height, int type) {
        BufferedImage image = new BufferedImage(width, height, type);

        SampleModel model = image.getSampleModel();
        Raster raster = image.getRaster();
        DataBuffer outBuffer = raster.getDataBuffer();

        int x = -raster.getSampleModelTranslateX();
        int y = -raster.getSampleModelTranslateY();
        int step = model.getWidth() * model.getNumBands();
        int channels = model.getNumBands();

        data.position(0).limit(height * width * channels);

        if (model instanceof ComponentSampleModel) {
            ComponentSampleModel compModel = (ComponentSampleModel) model;
            step = compModel.getScanlineStride();
            channels = compModel.getPixelStride();
        }
        else if (model instanceof SinglePixelPackedSampleModel) {
            SinglePixelPackedSampleModel singleModel = (SinglePixelPackedSampleModel) model;
            step = singleModel.getScanlineStride();
            channels = 1;
        }
        else if (model instanceof MultiPixelPackedSampleModel) {
            MultiPixelPackedSampleModel multiModel = (MultiPixelPackedSampleModel) model;
            step = multiModel.getScanlineStride();
            channels = ((MultiPixelPackedSampleModel) model).getPixelBitStride() / 8;
        }

        int start = y * step + x * channels;

        if (outBuffer instanceof DataBufferByte) {
            byte[] a = ((DataBufferByte) outBuffer).getData();
            copy(data, step, ByteBuffer.wrap(a, start, a.length - start), step, false);
        }
        else if (outBuffer instanceof DataBufferShort) {
            short[] a = ((DataBufferShort) outBuffer).getData();
            copy(data.asShortBuffer(), step / 2, ShortBuffer.wrap(a, start, a.length - start), step, true);
        }
        else if (outBuffer instanceof DataBufferUShort) {
            short[] a = ((DataBufferUShort) outBuffer).getData();
            copy(data.asShortBuffer(), step / 2, ShortBuffer.wrap(a, start, a.length - start), step, false);
        }
        else if (outBuffer instanceof DataBufferInt) {
            int[] a = ((DataBufferInt) outBuffer).getData();
            copy(data.asIntBuffer(), step / 4, IntBuffer.wrap(a, start, a.length - start), step);
        }
        else if (outBuffer instanceof DataBufferFloat) {
            float[] a = ((DataBufferFloat) outBuffer).getData();
            copy(data.asFloatBuffer(), step / 4, FloatBuffer.wrap(a, start, a.length - start), step);
        }
        else if (outBuffer instanceof DataBufferDouble) {
            double[] a = ((DataBufferDouble) outBuffer).getData();
            copy(data.asDoubleBuffer(), step / 8, DoubleBuffer.wrap(a, start, a.length - start), step);
        }
        
        return image;
    }

    public static void copy(ByteBuffer srcBuf, int srcStep, ByteBuffer dstBuf, int dstStep, boolean signed) {
        int w = Math.min(srcStep, dstStep);
        int srcLine = srcBuf.position();
        int dstLine = dstBuf.position();

        while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
            srcBuf.position(srcLine);
            dstBuf.position(dstLine);

            w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());

            for (int x = 0; x < w; x++) {
                int in = signed ? srcBuf.get() : srcBuf.get() & 0xFF;
                byte out = (byte) in;
                dstBuf.put(out);
            }

            srcLine += srcStep;
            dstLine += dstStep;
        }
    }

    public static void copy(ShortBuffer srcBuf, int srcStep, ShortBuffer dstBuf, int dstStep, boolean signed) {
        int w = Math.min(srcStep, dstStep);
        int srcLine = srcBuf.position();
        int dstLine = dstBuf.position();

        while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
            srcBuf.position(srcLine);
            dstBuf.position(dstLine);

            w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());

            for (int x = 0; x < w; x++) {
                int in = signed ? srcBuf.get() : srcBuf.get() & 0xFFFF;
                short out = (short) in;
                dstBuf.put(out);
            }

            srcLine += srcStep;
            dstLine += dstStep;
        }
    }

    public static void copy(IntBuffer srcBuf, int srcStep, IntBuffer dstBuf, int dstStep) {
        int w = Math.min(srcStep, dstStep);
        int srcLine = srcBuf.position();
        int dstLine = dstBuf.position();

        while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
            srcBuf.position(srcLine);
            dstBuf.position(dstLine);

            w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());

            for (int x = 0; x < w; x++) {
                int in = srcBuf.get();
                int out = in;
                dstBuf.put(out);
            }

            srcLine += srcStep;
            dstLine += dstStep;
        }
    }

    public static void copy(FloatBuffer srcBuf, int srcStep, FloatBuffer dstBuf, int dstStep) {
        int w = Math.min(srcStep, dstStep);
        int srcLine = srcBuf.position();
        int dstLine = dstBuf.position();

        while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
            srcBuf.position(srcLine);
            dstBuf.position(dstLine);

            w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());

            for (int x = 0; x < w; x++) {
                float in = srcBuf.get();
                float out = in;

                dstBuf.put(out);
            }

            srcLine += srcStep;
            dstLine += dstStep;
        }
    }

    public static void copy(DoubleBuffer srcBuf, int srcStep, DoubleBuffer dstBuf, int dstStep) {
        int w = Math.min(srcStep, dstStep);
        int srcLine = srcBuf.position();
        int dstLine = dstBuf.position();

        while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
            srcBuf.position(srcLine);
            dstBuf.position(dstLine);

            w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());

            for (int x = 0; x < w; x++) {
                double in = srcBuf.get();
                double out = in;

                dstBuf.put(out);
            }

            srcLine += srcStep;
            dstLine += dstStep;
        }
    }
    
}
