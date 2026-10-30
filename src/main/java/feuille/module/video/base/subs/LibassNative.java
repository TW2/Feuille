package feuille.module.video.base.subs;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;

import java.io.File;

/** JNA mapping for the small C ABI in native/libass_jna.c. */
interface LibassNative extends Library {
	LibassNative INSTANCE = Loader.loadLibass();

	Pointer la_create(int width, int height);

	void la_destroy(Pointer renderer);

	int la_load_track(Pointer renderer, byte[] assData, int length);

	int la_render_rgba(Pointer renderer, long timestampMilliseconds, Pointer destination, int capacity);

	class Loader {
		// The raw library
		private final LibassNative libass;

		private Loader() {
			// OS choices
			String osName = System.getProperty("os.name");
			int platform = Platform.getOSType();
			switch(platform) {
				case Platform.LINUX -> osName = "linux";
				case Platform.WINDOWS -> osName = "windows";
				case Platform.MAC -> osName = "mac";
			}

			// External path
			File app = new File(new File("").getAbsolutePath());
			File folder = new File(app.getPath() +
					File.separator + "settings" +
					File.separator + "dependencies"
			);

			// The path where the shared library is
			String libPath = new File(folder, osName).getPath();

			// The linkage
			System.setProperty("jna.library.path", libPath);

			// The instance
			libass = Native.load("ass_jna", LibassNative.class);
		}

		static LibassNative loadLibass() {
			return new Loader().libass;
		}
	}

}
