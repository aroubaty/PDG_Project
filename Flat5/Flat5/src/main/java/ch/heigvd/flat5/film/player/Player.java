package ch.heigvd.flat5.film.player;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ch.heigvd.flat5.film.player.sync.VideoSyncHandler;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.DefaultAdaptiveRuntimeFullScreenStrategy;

public class Player {

    private final JFrame frame;
    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private ControlPanel controls;

    private Player() {
        frame = new JFrame("Flat5");
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        contentPane.add(mediaPlayerComponent, BorderLayout.CENTER);
        mediaPlayerComponent.getMediaPlayer().setEnableMouseInputHandling(false);

        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mediaPlayerComponent.getMediaPlayer().setFullScreenStrategy(new DefaultAdaptiveRuntimeFullScreenStrategy(frame));
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
                System.exit(0);
            }
        });
        frame.setContentPane(contentPane);

        ScheduledThreadPoolExecutor timer = new ScheduledThreadPoolExecutor(1);
        ScheduledFuture<?>[] future = new ScheduledFuture<?>[1];
        mediaPlayerComponent.getVideoSurface().addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                controls.setVisible(true);

                if (future[0] != null) {
                    future[0].cancel(true);
                }
                future[0] = timer.schedule(() -> {
                    controls.setVisible(false);
                }, 2000, TimeUnit.MILLISECONDS);
            }
        });

        mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                mediaPlayerComponent.getMediaPlayer().toggleFullScreen();
                frame.setVisible(false);
            }
        });
    }

    private static class Holder {

        private static final Player instance = new Player();
    }

    public static Player getInstance() {
        return Holder.instance;
    }

    public void start(String file) {
        controls = new ControlPanel();
        controls.setVisible(false);
        frame.getContentPane().add(controls, BorderLayout.SOUTH);
        frame.setVisible(true);
        //mediaPlayerComponent.getMediaPlayer().setOverlay(new Overlay(frame));
        //mediaPlayerComponent.getMediaPlayer().enableOverlay(true);
        mediaPlayerComponent.getMediaPlayer().playMedia(file);
        mediaPlayerComponent.getMediaPlayer().toggleFullScreen();
        if (VideoSyncHandler.getInstance().isConnected()) {
            VideoSyncHandler.getInstance().sendPlay(Paths.get(file).getFileName().toString());
        }
    }

    public void next() {
        mediaPlayerComponent.getMediaPlayer().skip(2000);
        if (VideoSyncHandler.getInstance().isConnected()) {
            VideoSyncHandler.getInstance().sendSetTime(mediaPlayerComponent.getMediaPlayer().getTime());
        }
    }

    public void previous() {
        mediaPlayerComponent.getMediaPlayer().skip(-2000);
        if (VideoSyncHandler.getInstance().isConnected()) {
            VideoSyncHandler.getInstance().sendSetTime(mediaPlayerComponent.getMediaPlayer().getTime());
        }
    }

    public void pause() {
        if (VideoSyncHandler.getInstance().isConnected()) {
            VideoSyncHandler.getInstance().sendPause();
        }
        mediaPlayerComponent.getMediaPlayer().pause();
    }

    public long getLength() {
        return mediaPlayerComponent.getMediaPlayer().getLength();
    }

    public void setPosition(float percent) {
        mediaPlayerComponent.getMediaPlayer().setPosition(percent);
        if (VideoSyncHandler.getInstance().isConnected()) {
            VideoSyncHandler.getInstance().sendSetTime(mediaPlayerComponent.getMediaPlayer().getTime());
        }
    }

    public long getCurrentTime() {
        return mediaPlayerComponent.getMediaPlayer().getTime();
    }

    public float getPosition() {
        return mediaPlayerComponent.getMediaPlayer().getPosition();
    }
    
    public boolean isPlaying() {
        return mediaPlayerComponent.getMediaPlayer().isPlaying();
    }

    public void setTime(long time) {
        mediaPlayerComponent.getMediaPlayer().setTime(time);
    }
}
