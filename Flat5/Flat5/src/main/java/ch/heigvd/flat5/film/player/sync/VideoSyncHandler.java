package ch.heigvd.flat5.film.player.sync;


import ch.heigvd.flat5.AppConfig;
import ch.heigvd.flat5.film.player.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class VideoSyncHandler {

    private Socket communication;

    private Player player;

    public VideoSyncHandler(Player player) {
        this.player = player;
    }

    public void connect(String ip) {
        try {
            communication = new Socket();
            communication.connect(new InetSocketAddress(ip, AppConfig.DEFAULT_PORT), 10000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPlay(String fileName) {
        try {
            communication.getOutputStream().write(("PLAY " + fileName + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPause() {
        try {
            communication.getOutputStream().write(("PAUSE\n".getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendSetTime(long time) {
        try {
            communication.getOutputStream().write(("SETTIME " + time +"\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitForConnection() {
        try {
            ServerSocket s = new ServerSocket(AppConfig.DEFAULT_PORT);
            s.setSoTimeout(30000);
            communication = s.accept();

            startMessageThread();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startMessageThread() {
        new Thread(()->{
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(communication.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(true) {
                try {
                    String message = reader.readLine();

                    if (message.startsWith("SETTIME ")) {
                        player.setTime(Long.parseLong(message.replace("SETTIME ", "").trim()));
                    }
                    if (message.startsWith("PAUSE")) {
                        player.pause();
                    }
                    if (message.startsWith(("PLAY "))) {
                        player.start(message.replace("PLAY ", "").trim());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
