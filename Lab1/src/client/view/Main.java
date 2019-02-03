/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import client.net.ClientConnection;
import java.io.IOException;
/**
 *
 * @author aristotelis 
 * @author koszio
 */
public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ClientConnection cc = new ClientConnection();
        cc.connect("127.0.0.1", 8888);
        cc.playGame();   
    }
    
}
