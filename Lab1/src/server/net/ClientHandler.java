/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.Controller;
/**
 *
 * @author aristotelis 
 * @author koszio
 */
public class ClientHandler implements Runnable {

    private Socket clientSocket;
    Controller ctrl = new Controller(0,"");
    private PrintWriter toClient;
    private BufferedReader fromClient;
    public int score = 0;
    private boolean connected;
    
    public ClientHandler(Socket clientSocket,boolean connected) {
        this.clientSocket = clientSocket;
        this.connected = true;
    }

    @Override
    public void run() {
        try {
            toClient = new PrintWriter(clientSocket.getOutputStream(), true);
            fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                try {
                    while (connected) {
                        startGame();    
                    }
                } catch (IOException ioe) {
                    System.out.println("Connection Lost.");
                    clientSocket.close();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    private void startGame() throws IOException, ClassNotFoundException, InterruptedException, NullPointerException{
        String word = ctrl.guessingWord().toLowerCase();
        System.out.println("Word chosen: " + word); //Shows which word has been picked
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            sb.append('-');
        }
        
        Controller playerStatus = new Controller(sb.toString().length(), sb.toString());
        int remainingMisses = playerStatus.getMisses() + 1;
        while (true) {
            String updatedWord = fromClient.readLine();
            try{
                if (updatedWord.length() == 1) {
                    if (word.contains(updatedWord)) {
                        char[] arrayWord = playerStatus.getWord().toLowerCase().toCharArray();
                        for (int i = 0; i < word.length(); i++) {
                            if (word.charAt(i) == updatedWord.charAt(0)) {
                                arrayWord[i] = updatedWord.charAt(0);
                            }
                        }
                        updatedWord = new String(arrayWord);
                        if (updatedWord.equals(word)) {
                            score++;
                            playerStatus = new Controller(remainingMisses,word);
                            break;
                        }
                        playerStatus = new Controller(remainingMisses, updatedWord);
                    } else {
                        remainingMisses--;
                        updatedWord = playerStatus.getWord();
                        playerStatus = new Controller(remainingMisses, updatedWord);
                    }
                } else if (word.equals(updatedWord)) {
                    updatedWord = word;
                    score++;
                    playerStatus = new Controller(remainingMisses,word);
                    break;
                } else if (!word.equals(updatedWord)) {
                    remainingMisses--;
                    playerStatus = new Controller(remainingMisses,playerStatus.getWord());
                }

                if (remainingMisses == 0) {
                    score--;
                    break;
                }
                //TimeUnit.SECONDS.sleep(5); //Used to test that the client is multi-threaded
                toClient.printf("%60s\t%26d\t%10d  \n", playerStatus.getWord(), playerStatus.getMisses(), score);
            }catch(NullPointerException e){
                connected = false;
                System.out.println("User disconnected");
                clientSocket.close();
                break;
            }
        }
        toClient.printf("%60s\t%26s\t%10d  \n", playerStatus.getWord(), "no value", score);
    }
}