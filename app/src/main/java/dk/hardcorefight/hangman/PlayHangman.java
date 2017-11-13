package dk.hardcorefight.hangman;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PlayHangman extends AppCompatActivity implements View.OnClickListener{

    private HangmanGame game;
    private HashMap<Integer, Drawable> gallowStateMap;

    private TextView guessedWordView;
    private TextView gameStatusView;
    private ImageView gallowImage;
    private EditText guessInput;
    private  HashMap<String, Button> buttons;

    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    private ArrayList<Character> letters;

    private HashMap<String, Button> constructButtonGrid(LinearLayout layout, ArrayList<String> names, int rowLength) {
        HashMap<String, Button> buttons = new HashMap<>();
        Iterator<String> remainingNames = names.iterator();
        while (remainingNames.hasNext()) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            for (int i = 0; i < rowLength; i++) {
                if (!remainingNames.hasNext()) {
                    break;
                }
                String name = remainingNames.next();
                Button button = new Button(this);
                button.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                button.setText(name);
                row.addView(button);
                buttons.put(name, button);
            }
            layout.addView(row);
        }
        return buttons;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_hangman);

        this.letters = new ArrayList<>();
        for (char chr : PlayHangman.alphabet.toCharArray()) {
            this.letters.add(chr);
        }

        this.gallowStateMap = new HashMap<>();
        int enumerator = 0;
        for (Integer id : new int[]{
                R.drawable.gallow_0,
                R.drawable.gallow_1,
                R.drawable.gallow_2,
                R.drawable.gallow_3,
                R.drawable.gallow_4,
                R.drawable.gallow_5,
                R.drawable.gallow_6,
                R.drawable.gallow_7,
        }) {
            this.gallowStateMap.put(
                    enumerator++,
                    ContextCompat.getDrawable(this, id)
            );
        }

        ArrayList<String> possibelWords = new ArrayList<>();
        possibelWords.add("dinosaur");

        this.game = new HangmanGame();
        this.game.setPossibelWords(possibelWords);
        this.game.initialize();

        this.gallowImage = (ImageView) findViewById(R.id.HangmanStatusImage);
        this.guessedWordView = (TextView) findViewById(R.id.GuessedWordView);
        this.gameStatusView = (TextView) findViewById(R.id.GameStatusTextView);

        LinearLayout layout = (LinearLayout) findViewById(R.id.ButtonLayout);
        ArrayList<String> letters = new ArrayList<>();
        for (Character letter : this.letters) {
            letters.add(letter.toString());
        }
        this.buttons = this.constructButtonGrid(layout, letters, 6);

        for (Button button : buttons.values()) {
            button.setOnClickListener(this);
        }
        this.guessedWordView.setText(game.getVisibleWord());
    }

    private void endGame() {
        if (this.game.getGameIsWon()) {
            Intent intent = new Intent(this, PostGame.class);
            intent.putExtra(
                "INFO",
                String.format(
                    getString(R.string.YouWon),
                    this.game.getAmountWrongGuesses()
                )
            );
            startActivity(intent);
            Scorelist scorelist = new Scorelist(this);
            try {
                scorelist.addScore(
                        this.game.getAmountWrongGuesses()
                );
                scorelist.save();
            } catch (IOException e) {

            }
        } else {
            Intent intent = new Intent(this, PostGame.class);
            intent.putExtra(
                    "INFO",
                    String.format(
                            getString(R.string.YouLost),
                            this.game.getSecretWord()
                    )
            );
            startActivity(intent);
        }
    }

    private void performGuess(char guess) {
        this.game.guessLetter(guess);
        this.guessedWordView.setText(game.getVisibleWord());
        this.gallowImage.setImageDrawable(
                this.gallowStateMap.get(
                        this.game.getAmountWrongGuesses()
                )
        );
        for (String name : this.buttons.keySet()) {
            this.buttons.get(name).setEnabled(!this.game.getUsedLetters().contains(name.charAt(0)));
        }
        if (this.game.gameIsFinished()) {
            this.endGame();
        }
    }

    @Override
    public void onClick(View v) {
        this.performGuess(
            ((Button)v).getText().charAt(0)
        );

    }
}
