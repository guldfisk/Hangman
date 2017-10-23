package dk.hardcorefight.hangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class HangmanGame {
    private ArrayList<String> possibelWords = new ArrayList<>();
    private String secretWord;
    private ArrayList<Character> usedLetters = new ArrayList<>();
    private String visibleWord;
    private int amountWrongGuesses;
    private boolean lastGuessWasWrong;
    private boolean gameIsWon;
    private boolean gameIsLost;


    public ArrayList<Character> getUsedLetters() {
        return this.usedLetters;
    }

    public String getVisibleWord() {
        return this.visibleWord;
    }

    public String getSecretWord() {
        return this.secretWord;
    }

    public int getAmountWrongGuesses() {
        return this.amountWrongGuesses;
    }

    public boolean getLastGuessWasWrong() {
        return this.lastGuessWasWrong;
    }

    public boolean getGameIsWon() {
        return this.gameIsWon;
    }

    public boolean getGameIsLost() {
        return this.gameIsLost;
    }

    public boolean gameIsFinished() {
        return this.gameIsLost || this.gameIsWon;
    }

    public HangmanGame() {
        this.usedLetters.clear();
        this.amountWrongGuesses = 0;
        this.gameIsWon = false;
        this.gameIsLost = false;
    }

    public void initialize() {
        this.secretWord = possibelWords.get(
            new Random().nextInt(
                possibelWords.size()
            )
        );
        this.updateVisibleWord();
    }

    public void setPossibelWords(ArrayList<String> words) {
        this.possibelWords = words;
    }

    private void updateVisibleWord() {
        this.visibleWord = "";
        this.gameIsWon = true;

        for (Character letter : this.secretWord.toCharArray()) {
            if (this.usedLetters.contains(letter)){
                this.visibleWord += letter;
            } else {
                this.visibleWord += "*";
                this.gameIsWon = false;
            }
        }
    }

    public void guessLetter(Character letter) {
        System.out.println("Der gættes på bogstavet: " + letter);
        if (this.usedLetters.contains(letter) || this.gameIsFinished())
            return;

        this.usedLetters.add(letter);

        if (this.secretWord.contains(letter.toString())) {
            this.lastGuessWasWrong = true;
            System.out.println("Bogstavet var korrekt: " + letter);
        } else {
            // Vi gættede på et bogstav der ikke var i secretWord.
            this.lastGuessWasWrong = false;
            System.out.println("Bogstavet var IKKE korrekt: " + letter);
            if (++this.amountWrongGuesses > 6) {
                this.gameIsLost = true;
            }
        }
        updateVisibleWord();
    }

    public void logStatus() {
        System.out.println("---------- ");
        System.out.println("- secretWord (skult) = " + secretWord);
        System.out.println("- visibleWord = " + visibleWord);
        System.out.println("- forkerteBogstaver = " + amountWrongGuesses);
        System.out.println("- brugeBogstaver = " + usedLetters);
        if (gameIsLost) System.out.println("- SPILLET ER TABT");
        if (gameIsWon) System.out.println("- SPILLET ER VUNDET");
        System.out.println("---------- ");
    }


    private static String hentUrl(String url) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        StringBuilder sb = new StringBuilder();
        String linje = br.readLine();
        while (linje != null) {
            sb.append(linje + "\n");
            linje = br.readLine();
        }
        return sb.toString();
    }


    public void hentOrdFraDr() throws Exception {
        String data = hentUrl("http://dr.dk");
        //System.out.println("data = " + data);

        data = data.substring(data.indexOf("<body")). // fjern headere
                replaceAll("<.+?>", " ").toLowerCase(). // fjern tags
                replaceAll("[^a-zæøå]", " "). // fjern tegn der ikke er bogstaver
                replaceAll(" [a-zæøå] ", " "). // fjern 1-bogstavsord
                replaceAll(" [a-zæøå][a-zæøå] ", " "); // fjern 2-bogstavsord

        System.out.println("data = " + data);
        possibelWords.clear();
        possibelWords.addAll(new HashSet<String>(Arrays.asList(data.split(" "))));

        System.out.println("possibelWords = " + possibelWords);
        initialize();
    }
}
