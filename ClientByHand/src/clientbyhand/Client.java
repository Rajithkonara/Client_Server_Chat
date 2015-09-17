/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientbyhand;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Rajith Konara
 */
public class Client extends JFrame {

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;  //client ip address
    private Socket connection;

    Client(String host) {
        super("client");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener() {

                    @Override

                    public void actionPerformed(ActionEvent e) {
                        sendMessage(e.getActionCommand());
                        userText.setText("");
                    }
                }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(300, 150);
        setVisible(true);

    }

    //connect to server
    public void startRunning() {
        try {
            connectToServer();
            setupStreams();
            whileChatting();

        } catch (EOFException ex) {
            System.out.println("Clien terminated" + ex);
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            closeStream();
        }
    }

    //connect toserver
    private void connectToServer() throws IOException {
        showMessage("Attempting connection .. \n");
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("Connected To " + connection.getInetAddress().getHostName());
    }

    //set up streams send and receive stream 
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n You are good to go");

    }

    //while chatting method
    private void whileChatting() throws IOException {
        ableToType(true);
        do {
            try {
                message = (String) input.readObject();  //read as String
                showMessage("\n " + message);
            } catch (ClassNotFoundException es) {
                System.out.println(es);
            }

        } while (!message.equals("SERVER - END"));

    }

    //close Server
    private void closeStream() {
        showMessage("\n closing the chat down");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    //Send Messages to the Server
    private void sendMessage(String message) {
        try {
            output.writeObject("ClIENT - " + message);
            output.flush();
            showMessage("\nCLIENT" + message);  //displaying the message
        } catch (IOException ex) {
            chatWindow.append("\n Something went wrong");
        }
    }

    //show message  change /update chat window
    private void showMessage(final String m) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                chatWindow.append(m);
            }
        });
    }

//gives usr permission to type
    private void ableToType(final boolean tof) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                userText.setEditable(tof);
            }
        });

    }
}
