/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
/**
 *
 * @author aristotelis 
 * @author koszio
 */
public class CoolThread extends Thread {

    private BufferedReader fromServer;

    public CoolThread(Socket socket) throws IOException {
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
    }
    
    @Override
    public void run() {
        for(;;){    
            try {
                System.out.println(fromServer.readLine());
            } catch (IOException ex) {
                System.out.println("Game ended");
                break;
            }
        }
    }
}