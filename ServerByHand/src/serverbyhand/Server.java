package serverbyhand;

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
public class Server extends JFrame {

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;
    private ServerSocket server;

    public Server() {
        super("Server");
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

    public void startRunning() {
        try {
            server = new ServerSocket(6789, 100);//int port and backlog
            //backlog noof people to wait only  100 here
            while (true) {
                try {
                    //connect and have conversation 
                    waitForConnection();  //wait for someone to connect
                    setupStreams();  //out and in streams
                    whileChatting();  //chatting in and out
                } catch (EOFException eo) {
                    showMessage("Server ended connection");
                } finally {
                    closeStream(); //closing the stream of chat
                }
            }
        } catch (IOException io) {
            io.printStackTrace();

        }

    }

    //wait for connection display connecion info
    private void waitForConnection() throws IOException {
        //message
        showMessage("Waiting someone to connect");
        connection = server.accept();  //until connect to someone

        showMessage("Now Connected to" + connection.getInetAddress().getHostAddress());
    }

    //get stream to send and receive data
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());  //creating path to connect another computer
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("Your streams are set up");
    }

    //during the chatting 
    private void whileChatting() throws IOException {
        String message = "You are connected";
        showMessage(message);
        ableToType(true);  //allow user to type
        do {
            try {
                message = (String) input.readObject(); //stroe in coming message in astring
                showMessage("\n" + message);

            } catch (ClassNotFoundException cle) {
                System.out.println(cle);
            }
            //have a conversation
        } while (!message.equals("CLIENT - END"));
    }

    //closing the streams and sockets
    private void closeStream() {
        showMessage("Closing the connection....");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

//send message to client
    private void sendMessage(String message) {
        try {
            output.writeObject("SERVER - " + message);
            output.flush();
            showMessage("SERVER - " + message);
        } catch (IOException ex) {
            chatWindow.append("I Cannot send");
        }
    }

    //update chatWindow = txtchatwindow
    private void showMessage(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                chatWindow.append(text);  //add message to the end and update
            }
        });

    }

    //let the user type syuff in the chat box
    private void ableToType(final boolean tof) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                userText.setEditable(tof);  //editable
            }
        });

    }

}
