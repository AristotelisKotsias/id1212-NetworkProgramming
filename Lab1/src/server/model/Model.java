/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/**
 *
 * @author aristotelis 
 * @author koszio
 */
public class Model {
    
     public String guessingWord() throws FileNotFoundException, IOException {
        String path = "/home/aristotelis/NetBeansProjects/HangmanGame/src/server/model/words.txt";
        BufferedReader br = new BufferedReader(new FileReader(path));
        ArrayList<String> words = new ArrayList();

        while (br.readLine() != null) {
            words.add(br.readLine().toString());
        }

        br.close();
        String word = getRandomWord(words);
        return word;
    }

    private String getRandomWord(ArrayList<String> list) {
        return list.get((int) (Math.random() * list.size()));
    }
}
