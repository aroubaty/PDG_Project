package ch.heigvd.flat5.film.player;

import com.sun.jna.NativeLibrary;
import javax.swing.SwingUtilities;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class Program {

    private static final String LIBVLC_PATH = "C:/Program Files/VideoLAN/VLC";

    public static void main(String[] args) {
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), LIBVLC_PATH);
        System.out.println(LibVlc.INSTANCE.libvlc_get_version());

        SwingUtilities.invokeLater(() -> {
            //Player.getInstance().start("file:///C:/Users/léonard/Downloads/[Ms-FR]TeddyLoid feat. Debra Zeer (Panty & Stocking with Garterbelt) 1080p[1FEA7F86].mp4");
        });
    }
}
