package dk.hardcorefight.hangman.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import dk.hardcorefight.hangman.Game.GameActivity;
import dk.hardcorefight.hangman.R;
import dk.hardcorefight.hangman.Scoreboard.ScoreboardActivity;
import dk.hardcorefight.hangman.Sound.SoundPlayer;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        SoundPlayer.init(this.getApplicationContext());

        findViewById(R.id.NewGameButton).setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(MenuActivity.this, GameActivity.class);
                    startActivity(intent);
                }
            }
        );


        findViewById(R.id.ScoreboardButton).setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(MenuActivity.this, ScoreboardActivity.class);
                    startActivity(intent);
                }
            }
        );
    }

}
