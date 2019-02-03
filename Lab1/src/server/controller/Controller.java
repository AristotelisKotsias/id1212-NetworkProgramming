/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import java.io.IOException;
import server.model.Model;
import server.model.Status;
/**
 *
 * @author aristotelis
 */
public class Controller {
    
    Model model = new Model();
    Status status;
    
    public Controller(int remainingMisses, String word) {
        this.status = new Status(remainingMisses,word);
    }
    
    public String guessingWord() throws IOException {
        return model.guessingWord();
    }
    
    public int getMisses() {
        return status.getMisses();
    }

    public String getWord() {
        return status.getWord();
    }
    
}
