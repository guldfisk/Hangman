package dk.hardcorefight.hangman.Game;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import dk.hardcorefight.hangman.Game.HangmanGame.GameState;
import dk.hardcorefight.hangman.Game.HangmanGame.HangmanGame;
import dk.hardcorefight.hangman.R;

public class GameActivity
extends
    AppCompatActivity
implements
    CardPickedListener,
    GameFinishedListener
{

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
    }

    @Override
    public void cardPicked(Printing printing, Bitmap image) {
        PlayGameFragment playGameFragment = PlayGameFragment.newInstance(printing, image);
        playGameFragment.addGameFinishedListener(this);
        this.getFragmentManager().beginTransaction()
            .replace(
                R.id.screenView,
                playGameFragment
            )
            .commit();
    }

    @Override
    public void onGameFinished(HangmanGame game) {
        this.getFragmentManager().beginTransaction()
            .replace(
                R.id.screenView,
                PostGameFragment.newInstance(
                    game.getGameState() == GameState.WON,
                    game.getAmountWrongGuesses(),
                    game.getSecretWord().secretValue()
                )
            )
            .commit();
    }

}
