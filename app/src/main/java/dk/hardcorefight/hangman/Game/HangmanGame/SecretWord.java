package dk.hardcorefight.hangman.Game.HangmanGame;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SecretWord {

    private List<SecretCharacter> characters;

    public SecretWord(String word, Pattern pattern) {
        this.characters = new ArrayList<>();
        for (char chr : word.toCharArray()){
            this.characters.add(
                new SecretCharacter(
                    chr,
                    !pattern.matcher(String.valueOf(chr)).matches()
                )
            );
        }
    }

    public SecretWord(String word) {
        this(word, Pattern.compile("."));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (SecretCharacter secretCharacter : this.characters) {
            builder.append(secretCharacter.getValue());
        }
        return builder.toString();
    }

    public String secretValue() {
        StringBuilder builder = new StringBuilder();
        for (SecretCharacter secretCharacter : this.characters) {
            builder.append(secretCharacter.getSecretValue());
        }
        return builder.toString();
    }

    public boolean guessChar(char guess) {
        boolean guessed = false;
        for (SecretCharacter secretCharacter : this.characters) {
            if (secretCharacter.guess(guess)) {
                guessed = true;
            }
        }
        return guessed;
    }

    public boolean guessed() {
        for (SecretCharacter secretCharacter : this.characters) {
            if (!secretCharacter.isGuessed()) {
                return false;
            }
        }
        return true;
    }

}
