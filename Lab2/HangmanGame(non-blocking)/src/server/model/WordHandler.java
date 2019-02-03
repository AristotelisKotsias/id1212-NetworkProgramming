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

/**
 * This class holds the logic of reading the text file and choosing a random
 * word to start the game.
 *
 */
public class WordHandler {

    private final String path = "/home/aristotelis/NetBeansProjects/HangmanGame(non-blocking)/src/server/model/words.txt";

    public String guessingWord() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        ArrayList<String> words = new ArrayList();

        while (br.readLine() != null) {
            words.add(br.readLine());
        }

        br.close();
        String word = getRandomWord(words).toLowerCase();
        return word;
    }

    private String getRandomWord(ArrayList<String> list) {
        return list.get((int) (Math.random() * list.size()));
    }
}