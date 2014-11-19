/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.io.IOException;

/**
 *
 * @author Enrico
 */
interface ServerThreadController_INT {
    void onServerThreadStop();
    void onServerThreadInterruptedException(InterruptedException e);
    void onServerIOException(IOException e);
}
