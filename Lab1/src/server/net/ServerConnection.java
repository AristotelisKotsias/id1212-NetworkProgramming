/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import server.net.ClientHandler;
import server.model.Model;
/**
 *
 * @author aristotelis 
 * @author koszio
 */
public class ServerConnection {

    private static final int LINGER_TIME = 5000;
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private int defaultPort = 8888;
    Model model = new Model(); 

    public static void main(String[] args) {
        ServerConnection sc = new ServerConnection();
        sc.serve();
    }

    public void serve() {
        try {
            System.out.println("Server is up");
            ServerSocket listeningSocket = new ServerSocket(defaultPort);
            while (true) {
                Socket clientSocket = listeningSocket.accept(); 
                System.out.println("New user has connected");                       
                startHandler(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Server Failure.");
        }
    }

    private void startHandler(Socket clientSocket) throws SocketException, IOException {
        clientSocket.setSoLinger(true, LINGER_TIME);
        clientSocket.setSoTimeout(TIMEOUT_HALF_HOUR);
        ClientHandler clientHandler = new ClientHandler(clientSocket,true);
        Thread handlerThread = new Thread(clientHandler);
        handlerThread.start();
    } 
}
