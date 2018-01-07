package dk.hardcorefight.hangman.Game.HangmanGame;

import lombok.Getter;
import lombok.Setter;

public class SecretCharacter {

    private static final char placeHolder = '*';

    private char value;
    @Setter@Getter private boolean guessed;

    public SecretCharacter(char value, boolean guessed) {
        this.value = value;
        this.guessed = guessed;
    }

    public SecretCharacter(char value) {
        this(value, false);
    }

    public char getValue() {
        return this.guessed ? this.value : SecretCharacter.placeHolder;
    }

    public char getSecretValue() {
        return this.value;
    }

    public boolean guess(char chr) {
        if (this.guessed) {
            return false;
        }
        if (chr == Character.toLowerCase(this.value)) {
            this.guessed = true;
            return true;
        }
        return false;
    }
}
