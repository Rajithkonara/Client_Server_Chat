/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientbyhand;

import javax.swing.JFrame;

/**
 *
 * @author Rajith Konara
 */
public class ClientByHand {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Client c= new Client("127.0.0.1");
        c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c.startRunning();
    }
    
}
