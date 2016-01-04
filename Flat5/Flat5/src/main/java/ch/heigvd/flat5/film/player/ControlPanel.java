package ch.heigvd.flat5.film.player;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

public class ControlPanel extends JPanel {

    private JPanel buttonPanel = new JPanel();
    private JPanel sliderPanel = new JPanel();
    private JSlider progressBar = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
    private JLabel currentTime = new JLabel();
    private JLabel totalTime = new JLabel();

    public ControlPanel() {
        buttonPanel.setBackground(new Color(162, 212, 247, 255));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));

        JLabel previous = new JLabel(new ImageIcon("src/main/resources/img/VideoPlayer/previous.png"));
        previous.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                Player.getInstance().previous();
            }
        });

        JLabel play = new JLabel(new ImageIcon("src/main/resources/img/VideoPlayer/pause.png"));
        play.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (Player.getInstance().isPlaying()) {
                    play.setIcon(new ImageIcon("src/main/resources/img/VideoPlayer/play.png"));
                } else {
                    play.setIcon(new ImageIcon("src/main/resources/img/VideoPlayer/pause.png"));
                }
                
                Player.getInstance().pause(true);
            }
        });

        JLabel next = new JLabel(new ImageIcon("src/main/resources/img/VideoPlayer/next.png"));
        next.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                Player.getInstance().next();
            }
        });

        progressBar.setPreferredSize(new Dimension(350, 25));
        progressBar.setBackground(new Color(162, 212, 247, 255));

        final boolean[] dontUpdate = {false};
        progressBar.addChangeListener((ChangeEvent e) -> {
            JSlider slider = (JSlider) e.getSource();
            if (!slider.getValueIsAdjusting() && !dontUpdate[0]) {
                Player.getInstance().setPosition((float) (slider.getValue() / 100.0));
            }
        });

        buttonPanel.add(previous);
        buttonPanel.add(play);
        buttonPanel.add(next);

        sliderPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        sliderPanel.setBackground(new Color(162, 212, 247, 255));

        sliderPanel.add(currentTime);
        sliderPanel.add(progressBar);
        sliderPanel.add(totalTime);

        this.setLayout(new BorderLayout());
        this.setBackground(new Color(0, 0, 0, 0));

        this.add(buttonPanel, BorderLayout.CENTER);
        this.add(sliderPanel, BorderLayout.SOUTH);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if (!progressBar.getValueIsAdjusting()) {
                    dontUpdate[0] = true;
                    setCurrentTime(Player.getInstance().getCurrentTime());
                    progressBar.setValue((int) (Player.getInstance().getPosition() * 100));
                    dontUpdate[0] = false;
                    setLength(Player.getInstance().getLength());
                }
            }
        }, 0, 1000);
    }

    public void setCurrentTime(long time) {
        currentTime.setText(String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
                TimeUnit.MILLISECONDS.toSeconds(time)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))));
    }

    public void setLength(long length) {
        totalTime.setText(String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(length),
                TimeUnit.MILLISECONDS.toMinutes(length)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(length)),
                TimeUnit.MILLISECONDS.toSeconds(length)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(length))));
    }
}
