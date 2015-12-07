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
 * Classe prennant en charge tout la partie TCP de la synchronisation
 *
 * @author Anthony
 */
public class SyncManager {
    private static SyncManager instance;

    /**
     * Création de l'instance du singleton
     * @param p
     *          Port sur lequel le seveur va écouter
     * @param handler
     *          Interface qui va gère le commande sortante
     */
    public static void createInstance(int p, SyncHandler handler){
        instance = new SyncManager(p, handler);
    }

    /**
     * Retourne l'instance du singleton
     * @return
     *          instance du singleton
     */
    public static SyncManager getInstance(){
        return instance;
    }

    private final Logger log = Logger.getLogger(SyncManager.class.getName());
    private int port;
    private SyncHandler handler;
    private ServerSocket serverSocket;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Worker worker;

    /**
     * Constructeur qui va lancer un serveur TCP (non bloquant)
     * @param p
     *          Port sur lequel le seveur va écouter
     * @param handler
     *          Interface qui va gère le commande sortante
     */
    private SyncManager(int p, SyncHandler handler){
        this.port = p;
        this.handler = handler;

        try {
            serverSocket = new ServerSocket(port);
            Socket clientSocket = null;
            log.log(Level.INFO, "[TCP][Server] Server start on " + port + " port");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Permet de changer le handler en fonction du lecteur utilisé
     * @param handler
     *          Nouveau handler
     */
    public void setHandler(SyncHandler handler){
        this.handler = handler;
    }

    /**
     * Permet de recevoir un client (bloquant)
     */
    public void accept(){
        try {
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

    /**
     * Termine le processus serveur
     */
    public void stop(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////// Client part /////////////////////////////////

    /**
     * Connection à un serveur
     * @param ip
     *          IP du serveur
     * @param port
     *          Port du serveur
     */
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

    /**
     * Contrôle si on a une connection ouverte
     * @return
     *      Si il y a une connection ouverte
     */
    public boolean isConnected(){
        return (socket != null);
    }

    /**
     * Envoie au client le nom du média que l'on va lire
     * @param mediaName
     *          Nom du média
     */
    public void begin(String mediaName){
        if(!isConnected()){
            log.log(Level.SEVERE, "[TCP][Client] NOT CONNECTED");
            return ;
        }

        log.log(Level.INFO, "[TCP][Client] begin : " + mediaName);
        out.println("begin-" + mediaName);
    }

    /**
     * Indique au client que l'on a cliqué sur pause
     */
    public void pause(){
        if(!isConnected()){
            log.log(Level.SEVERE, "[TCP][Client] NOT CONNECTED");
            return ;
        }

        log.log(Level.INFO, "[TCP][Client] pause");
        out.println("pause");
    }

    /**
     * Indique au client que l'on a cliqué sur play
     */
    public void play(){
        if(!isConnected()){
            log.log(Level.SEVERE, "[TCP][Client] NOT CONNECTED");
            return ;
        }

        log.log(Level.INFO, "[TCP][Client] play");
        out.println("play");
    }

    /**
     * Indique au client que l'on met le média à N secondes
     * @param second
     *          Nombres de seconde
     */
    public void setAt(int second){
        if(!isConnected()){
            log.log(Level.SEVERE, "[TCP][Client] NOT CONNECTED");
            return ;
        }

        log.log(Level.INFO, "[TCP][Client] setAt : " + second);
        out.println("setAt-" + second);
    }

    /**
     * Termine la connection
     */
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

    /**
     * Thread qui va traiter les commandes entrantes
     */
    private class Worker implements Runnable {
        private Socket socket;
        private BufferedReader in;
        Thread t;

        /**
         * Constructeur de la classe
         * @param socket
         *          Socket du client
         * @param in
         *          Flux entrant (pour lire les commandes)
         */
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
                    } else if (command[0].equals("play")) {
                        log.log(Level.INFO, "[TCP][Server] play");
                        handler.play();
                    } else if (command[0].equals("setAt")) {
                        log.log(Level.INFO, "[TCP][Server] setAt : " + command[1]);
                        handler.setAt(Integer.parseInt(command[1]));
                    } else if (command[0].equals("bye")) {
                        log.log(Level.INFO, "[TCP][Server] bye");
                        handler.bye();
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
