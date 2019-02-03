/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import client.net.ClientConnection;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("\n                                                                   WELCOME TO HANGMAN GAME");
        System.out.println("\nRules: 1) Give one letter or the whole word");
        System.out.println("       2) Press any key and 'Enter' to start the game");
        System.out.println("       3) Press 'exit game' to exit the game");
        ClientConnection cc = new ClientConnection();
        cc.connectToServer("127.0.0.1", 8888);
        Scanner sc = new Scanner(System.in);
        while(true){
           cc.sendClientInput(sc.nextLine().toLowerCase()); 
        }
           
    }
}
