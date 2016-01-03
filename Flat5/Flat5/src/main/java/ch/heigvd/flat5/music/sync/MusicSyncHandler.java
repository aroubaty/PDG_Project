package ch.heigvd.flat5.music.sync;

import ch.heigvd.flat5.AppConfig;
import ch.heigvd.flat5.music.view.MusicController;
import ch.heigvd.flat5.sync.SyncHandler;
import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * Gestionnaire de synchronisation pour la musique
 * C'est ici qu'on reçoit les actions que notre ami a effectué sur son lecteur
 * @author Jérôme
 */
public class MusicSyncHandler implements SyncHandler {
    private MusicController controller;

    public MusicSyncHandler(MusicController controller) {
        this.controller = controller;
    }

    @Override
    public void begin(String mediaName) {
        controller.playMusic(mediaName, false);
    }

    @Override
    public void pause() {
        controller.getPlayer().pause();
    }

    @Override
    public void play() {
        controller.getPlayer().play();
    }

    @Override
    public void setAt(int second) {
        controller.getPlayer().setTime(second * 1000);
    }

    @Override
    public void bye() {
        controller.unsyncThePlayer(false);
    }
}
