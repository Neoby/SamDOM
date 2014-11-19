/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toolbox;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Enrico
 */
public class Timer implements Runnable {

    int msToWait;
    boolean msToWait_Passed=true;

    @Override
    public void run() {
        try {
            Thread.sleep(msToWait);
            msToWait_Passed=false;
        } catch (InterruptedException ex) {
            Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean wait(int msToWait) {
        this.msToWait = msToWait;
        this.msToWait_Passed = false;
        (new Thread(new Timer())).start();
        while(msToWait_Passed){}
        return true;
        
    }

}
