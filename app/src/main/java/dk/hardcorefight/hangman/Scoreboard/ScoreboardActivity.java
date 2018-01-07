package dk.hardcorefight.hangman.Scoreboard;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

import dk.hardcorefight.hangman.R;

public class ScoreboardActivity extends AppCompatActivity implements View.OnClickListener{

    private Scorelist scorelist;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        this.scorelist = new Scorelist(this);

        this.adapter = new ArrayAdapter<>(this, R.layout.list_item, scorelist.sortedScores());

        ListView listView = this.findViewById(R.id.ScoreboardList);

        listView.setAdapter(this.adapter);

        this.findViewById(R.id.ResetScoresButton).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.ResetHighscoreConfirmTitle)
            .setMessage(R.string.ResetHighscoreConfirmBody)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(
                android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            ScoreboardActivity.this.scorelist.clear().save();
                            ScoreboardActivity.this.adapter.clear();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            )
            .setNegativeButton(android.R.string.no, null)
            .show();

    }
}
