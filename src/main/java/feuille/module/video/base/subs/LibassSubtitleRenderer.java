package feuille.module.video.base.subs;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Renders an in-memory ASS/SSA track to transparent ARGB images.
 * Instances are safe to call from worker threads; rendering is serialized per instance.
 */
public final class LibassSubtitleRenderer implements AutoCloseable {
	private final int width;
	private final int height;
	private final int bufferSize;
	private final Object lock = new Object();
	private Pointer nativeRenderer;
	private Memory nativeBuffer;

	public LibassSubtitleRenderer(int width, int height) {
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException("width and height must be positive");
		}
		if ((long) width > Integer.MAX_VALUE / 4L / height) {
			throw new IllegalArgumentException("image buffer is too large");
		}
		this.width = width;
		this.height = height;
		this.bufferSize = width * height * 4;
		this.nativeRenderer = LibassNative.INSTANCE.la_create(width, height);
		if (nativeRenderer == null || Pointer.nativeValue(nativeRenderer) == 0) {
			throw new IllegalStateException("Unable to create the libass renderer");
		}
		this.nativeBuffer = new Memory(bufferSize);
	}

	/** Replaces the complete ASS/SSA track with UTF-8 data held in memory. */
	public void loadTrack(byte[] assUtf8) {
		Objects.requireNonNull(assUtf8, "assUtf8");
		synchronized (lock) {
			ensureOpen();
			int result = LibassNative.INSTANCE.la_load_track(nativeRenderer, assUtf8, assUtf8.length);
			if (result != 0) {
				throw new IllegalArgumentException("libass rejected the ASS/SSA track (code " + result + ")");
			}
		}
	}

	public void loadTrack(String assText) {
		loadTrack(Objects.requireNonNull(assText, "assText").getBytes(StandardCharsets.UTF_8));
	}

	/** Renders at a presentation timestamp expressed in milliseconds. */
	public BufferedImage render(long timestampMilliseconds) {
		synchronized (lock) {
			ensureOpen();
			int result = LibassNative.INSTANCE.la_render_rgba(
					nativeRenderer, timestampMilliseconds, nativeBuffer, bufferSize);
			if (result != 0) {
				throw new IllegalStateException("libass rendering failed (code " + result + ")");
			}

			byte[] rgba = nativeBuffer.getByteArray(0, bufferSize);
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			int[] argb = ((java.awt.image.DataBufferInt) image.getRaster().getDataBuffer()).getData();
			for (int i = 0, pixel = 0; i < rgba.length; i += 4, pixel++) {
				int r = rgba[i] & 0xff;
				int g = rgba[i + 1] & 0xff;
				int b = rgba[i + 2] & 0xff;
				int a = rgba[i + 3] & 0xff;
				argb[pixel] = (a << 24) | (r << 16) | (g << 8) | b;
			}
			return image;
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public void close() {
		synchronized (lock) {
			if (nativeRenderer != null) {
				LibassNative.INSTANCE.la_destroy(nativeRenderer);
				nativeRenderer = null;
				nativeBuffer = null;
			}
		}
	}

	private void ensureOpen() {
		if (nativeRenderer == null) {
			throw new IllegalStateException("renderer is closed");
		}
	}
}
