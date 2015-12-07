package ch.heigvd.flat5.music.view;

import ch.heigvd.flat5.AppConfig;
import ch.heigvd.flat5.sync.SyncHandler;
import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * Created by Anthony on 07.12.2015.
 */
public class MusicSyncHandler implements SyncHandler {
    private MusicController controller;
    private MediaPlayer player;

    public MusicSyncHandler(MusicController controller) {
        this.controller = controller;
        player = controller.getPlayer();
    }

    @Override
    public void begin(String mediaName) {
        controller.playMusic(AppConfig.MUSIC_DIRECTORY + "/" + mediaName, false);
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public void play() {
        player.play();
    }

    @Override
    public void setAt(int second) {
        player.setTime(second * 1000);
    }

    @Override
    public void bye() {
        controller.exit();
    }
}
