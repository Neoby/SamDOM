/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Enrico
 */
public class Server implements Runnable {

    ArrayList<Server_OnDemandThread> d;
    private final ServerMatrix srvInfo;
    private ServerThreadController_INT sControl;
    private volatile boolean run = true;
    ServerThreadController_INT srv_Ctrl;
    ServerSocket server;

    /**
     * Start the server
     *
     * @param srvMatrix
     */
    public Server(ServerMatrix srvMatrix, ServerThreadController_INT srv_Ctrl) throws IllegalArgumentException {
        this.srvInfo = srvMatrix;
        this.srv_Ctrl = srv_Ctrl;
        this.d = new ArrayList<>();

        try {
            server = new ServerSocket(srvInfo.port);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {

            //THREAD BODY
            while (run) {
                Socket p = server.accept();

                new Thread(new Server_OnDemandThread(p)).start();
            }
            srv_Ctrl.onServerThreadStop();
            //END OF THREAD BODY
        } catch (IOException ex) {
            srv_Ctrl.onServerIOException(ex);
        }

        System.out.println("Uscito dalla thread " + srvInfo.port);

    }

    class Server_OnDemandThread implements Runnable {

        Socket clientSocket = null;

        public Server_OnDemandThread(Socket c) {
            this.clientSocket = c;
        }

        public void run() {
            try {
                System.out.println("Connection successful");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;

                clientSocket.setSoTimeout(10);
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                }
                System.out.println("Connessione chiusa");
                clientSocket.close();
            } catch (IOException e) {
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }

                //System.err.println(e.getMessage());
            }
        }
    }

    public void stop() {
        //Fermo il loop della thread
        run = false;
        //Chiudo il socket così che esco dal metodo bloccante server.accept()

        try {
            server.close();
        } catch (IOException ex) {
            System.out.println("Library exception contact ____");
            ex.printStackTrace();
        }

    }

}
