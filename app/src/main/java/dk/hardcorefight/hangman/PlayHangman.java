package dk.hardcorefight.hangman;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayHangman extends AppCompatActivity implements View.OnClickListener{

    private HangmanGame game;
    private HashMap<Integer, Drawable> gallowStateMap;

    private TextView guessedWordView;
    private TextView gameStatusView;
    private ImageView gallowImage;
    private EditText guessInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_hangman);

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
        this.guessInput = (EditText) findViewById(R.id.GuessTextEdit);

        findViewById(R.id.SubmitGuessButton).setOnClickListener(this);
    }

    private void endGame() {
        if (this.game.getGameIsWon()) {
            this.gameStatusView.setText("You won! :DDD");
            Scorelist scorelist = new Scorelist(this);
            try {
                scorelist.addScore(
                        this.game.getAmountWrongGuesses()
                );
                scorelist.save();
            } catch (IOException e) {

            }
        } else {
            this.gameStatusView.setText("You Lost xD");
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
        if (this.game.gameIsFinished()) {
            this.endGame();
        }
    }

    @Override
    public void onClick(View v) {
        if (!this.guessInput.getText().toString().isEmpty() && !this.game.gameIsFinished()) {
            this.performGuess(
                    this.guessInput.getText().charAt(0)
            );
        }
        this.guessInput.setText("");
    }
}
