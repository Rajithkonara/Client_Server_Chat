/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverbyhand;

import javax.swing.JFrame;

/**
 *
 * @author Rajith Konara
 */
public class ServerByHand {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       Server s = new Server();
       s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       s.startRunning();
    
}
}