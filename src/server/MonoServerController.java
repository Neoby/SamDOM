/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import toolbox.Timer;

/**
 *
 * @author Enrico
 */
public class MonoServerController implements ServerThreadController_INT {

    //Variabili dettate dall'utente
    ServerMatrix srvMatrix;

    //Variabili della classe per creare un nuvo server
    private Thread server_Com;
    private Server server;

    //Variabili riguardati lo stato del server
    private int serverStatus;
    private static final int STATUS_NOT_INITIALIZED = 0;
    private static final int STATUS_INITIALIZED = 1;
    private static final int STATUS_RUNNING = 2;
    private static final int STATUS_STOPPED = 3;

    //CRITICAL_CICLES_FOR_THREAD_CLOSE
    public int CCFTC = 1000;
    public int CICLE_DURATION = 100;

    public MonoServerController() {
        serverStatus = STATUS_NOT_INITIALIZED;
    }

    /**
     * This metod will create a new Server based on server Matrix informations.
     * The previous server will be overwritten and threads will be stopped
     */
    public void initNewServer(ServerMatrix srvMatrix) {
        //fermo (se è in funzione) la thread precedente
        this.stopServer();

        //salvo la matrice del server
        this.srvMatrix = srvMatrix;

        //setto come non inizializzata la thread
        try {

            //creo un nuovo server
            server = new Server(srvMatrix, this);
            serverStatus = STATUS_INITIALIZED;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            /*
             Se avviene un errore nel costruttore il server non viene segnato come 
             inizializzato
             */
        }

    }

    public void startServer() {
        //faccio partire/ripartire la thread
        if ((serverStatus == STATUS_INITIALIZED) || (serverStatus == STATUS_STOPPED)) {
            server_Com = (new Thread(server));
            serverStatus = STATUS_RUNNING;
            server_Com.start();
        }
    }

    /**
     * Metodo bloccante che ferma la thread del server
     */
    public void stopServer() {
        //prima controllo se il server è in funzione
        if (serverStatus == STATUS_RUNNING)//poi se caso fermo la thread
        {
            //server_Com.interrupt();
            
            server.stop();
            //Attenzione se la thread era già ferma e tento di fermarla cado in un ciclo infinito!
            /*La classe Timer ha problemi, il sistema anti-loop è per il momento sospeso
            Timer timer = new Timer();
            int i = 0;
            while (serverStatus != STATUS_NOT_INITIALIZED && serverStatus != STATUS_STOPPED) {
                timer.wait(CICLE_DURATION);
                if (i++ > CCFTC) {
                    System.err.print("Crytical time reached, trying to break (deprecated)"
                            + "previous thread... ");
                    //Uccido la thread nel caso non riesca a chiuderla normalmente
                    server_Com.stop();
                    //Cosidero la thread distrutta
                    serverStatus = STATUS_NOT_INITIALIZED;
                }

            }*/
        }
    }

    @Override
    public void onServerThreadStop() {
        serverStatus = STATUS_STOPPED;
        //report event to user's controller
    }

    @Override
    public void onServerThreadInterruptedException(InterruptedException e) {
        serverStatus = STATUS_STOPPED;
        //report event to user's controller
    }

    @Override
    public void onServerIOException(IOException e) {
 
    }

}
