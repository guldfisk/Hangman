package dk.hardcorefight.hangman.Game.HangmanGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import lombok.Getter;

public class HangmanGame {
    @Getter private SecretWord secretWord;
    @Getter private Set<Character> usedLetters = new HashSet<>();
    @Getter private int amountWrongGuesses;
    @Getter private boolean lastGuessWasWrong;
    @Getter private GameState gameState;

    public boolean isFinished() {
        return this.gameState != GameState.PENDING;
    }

    public HangmanGame() {
        this.amountWrongGuesses = 0;
        this.gameState = GameState.PENDING;
        this.lastGuessWasWrong = false;
    }

    public void initialize(SecretWord secretWord) {
        this.secretWord = secretWord;
    }

    public void initialize(ArrayList<SecretWord> possibleWords) {
        this.secretWord = possibleWords.get(
            new Random().nextInt(
                possibleWords.size()
            )
        );
    }
    public void guessLetter(Character letter) {
        if (this.usedLetters.contains(letter) || this.isFinished())
            return;

        this.usedLetters.add(letter);

        if (!this.secretWord.guessChar(letter)) {
            this.lastGuessWasWrong = true;
            this.amountWrongGuesses += 1;
            if (this.amountWrongGuesses >= 5) {
                this.gameState = GameState.LOST;
            }
        } else {
            this.lastGuessWasWrong = false;
            if (this.secretWord.guessed()) {
                this.gameState = GameState.WON;
            }
        }
    }



}
