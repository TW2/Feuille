package feuille.module.video;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

import java.util.Objects;

public class AssaRenderer {

    public interface LibASS extends Library {
        LibASS INSTANCE = (LibASS)Native.
                load(
                        (Platform.isWindows() ? "" :        // Windows
                        Platform.isLinux() ? "libassa" :    // Linux
                                ""),                        // MacOS
                        LibASS.class);

        boolean set_and_get(String png_file, String sub_file, double seconds, int width, int height);
    }

    private final LibASS ASSA;

    public AssaRenderer(){
        String libPath = Objects.requireNonNull(getClass().getResource("/libass")).getPath();
        System.setProperty("jna.library.path", libPath);
        ASSA = LibASS.INSTANCE;
    }

    public LibASS getASSA() {
        return ASSA;
    }

}
