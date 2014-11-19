/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samdom;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.MonoServerController;
import server.ServerMatrix;
import toolbox.Timer;

/**
 *
 * @author Enrico
 */
public class SamDOM {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MonoServerController ctr = new MonoServerController();
        
        stampaTest("Inizializzo il server 8282");
        ctr.initNewServer(new ServerMatrix(8282));
        stampaTest("Faccio partire il server 8282");
        ctr.startServer();
        //Notare che inizializzo senza chiudere il socket precedente.
        //Il controller lo chiude da se...
        stampaTest("Inizializzo il server 8281");
        ctr.initNewServer(new ServerMatrix(8181));
        stampaTest("Faccio partire il server 8281");
        ctr.startServer();
        stampaTest("Fermo il server 8281");
        
        BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
        try {
            String line=buffer.readLine();
        } catch (IOException ex) {
            Logger.getLogger(SamDOM.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        ctr.stopServer();
    }
    static void stampaTest(String s){
        System.out.println("[Test] "+s);
    }

}
