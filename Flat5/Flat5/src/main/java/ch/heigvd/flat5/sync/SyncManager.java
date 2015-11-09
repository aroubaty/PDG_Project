package ch.heigvd.flat5.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Anthony on 09.11.2015.
 */
public class SyncManager {
    private final Logger log = Logger.getLogger(SyncManager.class.getName());
    private int port;
    private SyncHandler handler;
    private ServerSocket serverSocket;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Worker worker;

    public SyncManager(int p, SyncHandler handler){
        this.port = p;

        try {
            serverSocket = new ServerSocket(port);
            Socket clientSocket = null;
            log.log(Level.INFO, "[TCP][Server] Server start on " + port + " port");
            log.log(Level.INFO, "[TCP][Server] Waiting for connection.....");

            socket = serverSocket.accept();

            log.log(Level.INFO, "[TCP][Server] Client come " + socket.getInetAddress().toString());
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            worker = new Worker(socket, in);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stop(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////// Client part /////////////////////////////////
    public void connect(String ip, int port){
        if(isConnected()){
            log.log(Level.SEVERE, "[TCP][Client] ALREADY CONNECT");
            return ;
        }

        try {
            socket = new Socket(ip, port);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            log.log(Level.INFO, "[TCP][Client] Connection successful to " + socket.getInetAddress().toString());

            worker = new Worker(socket, in);


        } catch (IOException e) {
            log.log(Level.SEVERE, "[TCP][Client] Can't connect to " + socket.getInetAddress().toString());
            e.printStackTrace();
        }
    }

    public boolean isConnected(){
        return (socket != null);
    }

    public void begin(String mediaName){
        if(!isConnected()){
            log.log(Level.SEVERE, "[TCP][Client] NOT CONNECTED");
            return ;
        }

        log.log(Level.INFO, "[TCP][Client] begin : " + mediaName);
        out.println("begin-" + mediaName);
    }

    public void pause(){
        if(!isConnected()){
            log.log(Level.SEVERE, "[TCP][Client] NOT CONNECTED");
            return ;
        }

        log.log(Level.INFO, "[TCP][Client] pause");
        out.println("pause");
    }

    public void playAt(int second){
        if(!isConnected()){
            log.log(Level.SEVERE, "[TCP][Client] NOT CONNECTED");
            return ;
        }

        log.log(Level.INFO, "[TCP][Client] playAt : " + second);
        out.println("playAt-" + second);
    }

    public void setAt(int second){
        if(!isConnected()){
            log.log(Level.SEVERE, "[TCP][Client] NOT CONNECTED");
            return ;
        }

        log.log(Level.INFO, "[TCP][Client] setAt : " + second);
        out.println("setAt-" + second);
    }

    public void bye(){
        if(!isConnected()){
            log.log(Level.SEVERE, "[TCP][Client] NOT CONNECTED");
            return ;
        }

        log.log(Level.INFO, "[TCP][Client] bye");
        out.println("bye");
        worker.t.stop();
        worker = null;
        out.close();

        try {
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    ///////////////////////////////////////////////////////////////////////////

    //Thread who handle incomming request
    private class Worker implements Runnable {
        private Socket socket;
        private BufferedReader in;
        Thread t;

        public Worker(Socket socket, BufferedReader in){
            this.socket = socket;
            this.in = in;
            t = new Thread(this);
            t.start();
        }

        @Override
        public void run() {
            try {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    log.log(Level.INFO, "[TCP][Server] Receives : " + inputLine);

                    String[] command = inputLine.split("-");

                    if (command[0].equals("begin")) {
                        log.log(Level.INFO, "[TCP][Server] begin : " + command[1]);
                        handler.begin(command[1]);
                    } else if (command[0].equals("pause")) {
                        log.log(Level.INFO, "[TCP][Server] pause");
                        handler.pause();
                    } else if (command[0].equals("playAt")) {
                        log.log(Level.INFO, "[TCP][Server] playAt : " + command[1]);
                        handler.playAt(Integer.parseInt(command[1]));
                    } else if (command[0].equals("setAt")) {
                        log.log(Level.INFO, "[TCP][Server] setAt : " + command[1]);
                        handler.setAt(Integer.parseInt(command[1]));
                    } else if (command[0].equals("bye")) {
                        log.log(Level.INFO, "[TCP][Server] bye");
                        out.close();
                        in.close();
                        socket.close();
                        break;
                    } else
                        log.log(Level.SEVERE, "[TCP][Server] Don't know message : " + inputLine);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
