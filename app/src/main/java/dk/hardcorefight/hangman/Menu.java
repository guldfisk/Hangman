package dk.hardcorefight.hangman;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final Context _this = this;

        Button newGameButton = (Button) findViewById(R.id.NewGameButton);

        newGameButton.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(_this, PlayHangman.class);
                    startActivity(intent);
                }
        }
        );

        Button scoreboardButton = (Button) findViewById(R.id.ScoreboardButton);

        scoreboardButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(_this, Scoreboard.class);
                        startActivity(intent);
                    }
                }
        );
    }

}
