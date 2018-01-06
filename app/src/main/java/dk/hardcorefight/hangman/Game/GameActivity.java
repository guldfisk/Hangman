package dk.hardcorefight.hangman.Game;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import dk.hardcorefight.hangman.R;

public class GameActivity extends AppCompatActivity implements CardPickedListener {

    private HangmanGame game;
    private HashMap<Integer, Drawable> gallowStateMap;

    private TextView guessedWordView;
    private TextView gameStatusView;
    private ImageView gallowImage;
    private EditText guessInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        PickCardFragment pickCardFragment = PickCardFragment.newInstance();

        pickCardFragment.addCardPickedListener(this);

        this.getFragmentManager().beginTransaction()
            .replace(
                R.id.screenView,
                pickCardFragment
            )
            .commit();

//        this.letters = new ArrayList<>();
//        for (char chr : GameActivity.alphabet.toCharArray()) {
//            this.letters.add(chr);
//        }
//
//        ArrayList<String> possibelWords = new ArrayList<>();
//        possibelWords.add("dinosaur");
//
//        this.game = new HangmanGame();
//        this.game.setPossibelWords(possibelWords);
//        this.game.initialize();
//
//        this.gallowImage = (ImageView) findViewById(R.id.HangmanStatusImage);
//        this.guessedWordView = (TextView) findViewById(R.id.GuessedWordView);
//        this.gameStatusView = (TextView) findViewById(R.id.GameStatusTextView);
//
//        LinearLayout layout = (LinearLayout) findViewById(R.id.ButtonLayout);
//        ArrayList<String> letters = new ArrayList<>();
//        for (Character letter : this.letters) {
//            letters.add(letter.toString());
//        }
//        this.buttons = this.constructButtonGrid(layout, letters, 6);
//
//        for (Button button : buttons.values()) {
//            button.setOnClickListener(this);
//        }
//        this.guessedWordView.setText(game.getVisibleWord());
    }

    @Override
    public void cardPicked(Printing printing, Bitmap image) {
        this.getFragmentManager().beginTransaction()
            .replace(
                R.id.screenView,
                PlayGameFragment.newInstance(printing, image)
            )
            .commit();
    }

//    private void endGame() {
//        if (this.game.getGameIsWon()) {
//            Intent intent = new Intent(this, PostGame.class);
//            intent.putExtra(
//                "INFO",
//                String.format(
//                    getString(R.string.YouWon),
//                    this.game.getAmountWrongGuesses()
//                )
//            );
//            startActivity(intent);
//            Scorelist scorelist = new Scorelist(this);
//            try {
//                scorelist.addScore(
//                        this.game.getAmountWrongGuesses()
//                );
//                scorelist.save();
//            } catch (IOException e) {
//
//            }
//        } else {
//            Intent intent = new Intent(this, PostGame.class);
//            intent.putExtra(
//                    "INFO",
//                    String.format(
//                            getString(R.string.YouLost),
//                            this.game.getSecretWord()
//                    )
//            );
//            startActivity(intent);
//        }
//    }
//
//    private void performGuess(char guess) {
//        this.game.guessLetter(guess);
//        this.guessedWordView.setText(game.getVisibleWord());
//        this.gallowImage.setImageDrawable(
//                this.gallowStateMap.get(
//                        this.game.getAmountWrongGuesses()
//                )
//        );
//        for (String name : this.buttons.keySet()) {
//            this.buttons.get(name).setEnabled(!this.game.getUsedLetters().contains(name.charAt(0)));
//        }
//        if (this.game.gameIsFinished()) {
//            this.endGame();
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        this.performGuess(
//            ((Button)v).getText().charAt(0)
//        );
//
//    }
}
