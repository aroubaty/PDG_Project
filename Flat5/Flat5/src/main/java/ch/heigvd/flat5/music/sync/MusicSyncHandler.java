package ch.heigvd.flat5.music.sync;

import ch.heigvd.flat5.AppConfig;
import ch.heigvd.flat5.music.view.MusicController;
import ch.heigvd.flat5.sync.SyncHandler;
import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * Created by Anthony on 07.12.2015.
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
