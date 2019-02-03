/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import java.io.IOException;

/**
 *
 * @author aristotelis
 */
public class HangmanGame {

    private int remainingMisses;
    private int score = 0;
    private String word;
    private String result;
    private StringBuilder sb;
    private WordHandler wordhandler;
    Status playerStatus;

    public HangmanGame() throws IOException {
        wordhandler = new WordHandler();
        score = 0;
        pickWord();
        hideWord();
    }

    private void pickWord() throws IOException {
        word = wordhandler.guessingWord();
        remainingMisses = word.length() + 1;
    }

    private void hideWord() {
        sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            sb.append('-');
        }
        playerStatus = new Status(remainingMisses, sb.toString());
    }

    private void pickNewWord() throws IOException {
        pickWord();
        hideWord();
    }

    public String playGame(String guessFromClient) throws IOException {
        System.out.println("Word chosen: " + word);
        //System.out.println("User typed: " + guessFromClient);

        if (playerStatus.getMisses() != 0) {
            if (guessFromClient.length() == 1) {
                if (word.contains(guessFromClient)) {
                    char[] arrayWord = playerStatus.getWord().toLowerCase().toCharArray();
                    for (int i = 0; i < word.length(); i++) {
                        if (word.charAt(i) == guessFromClient.charAt(0)) {
                            arrayWord[i] = guessFromClient.charAt(0);
                        }
                    }
                    guessFromClient = new String(arrayWord);
                    if (guessFromClient.equals(word)) {
                        score++;
                        playerStatus = new Status(remainingMisses, word);
                        result = "YOU WON. Word: " + playerStatus.getWord() + " | Remaining misses: " + "no value" + " | score: " + score;
                        pickNewWord();
                        return result;
                    }
                    playerStatus = new Status(remainingMisses, guessFromClient);
                    result = "| Word: " + playerStatus.getWord() + " | Remaining misses: " + playerStatus.getMisses() + " | score: " + score;
                } else {
                    remainingMisses--;
                    guessFromClient = playerStatus.getWord();
                    playerStatus = new Status(remainingMisses, guessFromClient);
                    result = "| Word: " + playerStatus.getWord() + " | Remaining misses: " + playerStatus.getMisses() + " | score: " + score;
                }
                return result;
            } else if (word.equals(guessFromClient)) {
                guessFromClient = word;
                score++;
                playerStatus = new Status(remainingMisses, word);
                result = "YOU WON. Word: " + playerStatus.getWord() + " | Remaining misses: " + "no value" + " | score: " + score;
                pickNewWord();
                return result;
            } else if (!word.equals(guessFromClient)) {
                remainingMisses--;
                playerStatus = new Status(remainingMisses, playerStatus.getWord());
                result = "| Word: " + playerStatus.getWord() + " | Remaining misses: " + playerStatus.getMisses() + " | score: " + score;
                return result;
            }
        }
        score--;
        playerStatus = new Status(remainingMisses, word);
        result = "YOU LOST. The word was: " + playerStatus.getWord() + " | Remaining misses: " + "no value" + " | score: " + score;
        pickNewWord();
        return result;
    }
}
