package dk.hardcorefight.hangman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

public class Scoreboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        Scorelist scorelist = new Scorelist(this);

        StringBuilder stringBuilder = new StringBuilder();

        for (Integer score : scorelist.sortedScores()) {
            stringBuilder.append(score);
            stringBuilder.append("\n");
        }

        ((TextView) findViewById(R.id.ScoresView)).setText(stringBuilder.toString());

    }
}
