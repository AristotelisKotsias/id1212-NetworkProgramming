/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.PrintWriter;
import java.util.Scanner;
/**
 *
 * @author aristotelis 
 * @author koszio
 */
public class ClientConnection {

    private Socket socket;
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private static final int TIMEOUT_HALF_MINUTE = 30000;
    private PrintWriter toServer;

    public void connect(String host, int port) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), TIMEOUT_HALF_MINUTE);
        socket.setSoTimeout(TIMEOUT_HALF_HOUR);
        System.out.println("Connected to server");
        toServer = new PrintWriter(socket.getOutputStream(), true);
        CoolThread thread = new CoolThread(socket);
        thread.start();
    }

    public void playGame() throws IOException, ClassNotFoundException {
        
        
        System.out.println("\n                                                                   WELCOME TO HANGMAN GAME");
        System.out.println("\nRules: 1) Give one letter or the whole word");
        System.out.println("       2) Press 'exit game' to exit the game");
        System.out.println("Press ENTER to start the game...");
        Scanner sc = new Scanner(System.in);
        String a = "Word";
        String b = "Remaining Failed Attempts";
        String c = "Score";
        System.out.printf("\n%60s\t%26s\t%10s  \n", a,b, c );
        String message;
       
        for (;;) {
            message = sc.nextLine().toLowerCase();
            if(message.equals("exit game")) {
                disconnect();
                break;
            }
            try{
                toServer.println(message);   
            } catch(java.lang.NullPointerException e){
                System.out.println("You have exited the game");
            }   
        }
    }

    private void disconnect() throws IOException {
        socket.close();
    }
}
