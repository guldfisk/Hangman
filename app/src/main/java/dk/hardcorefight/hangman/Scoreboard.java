package dk.hardcorefight.hangman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Scoreboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        Scorelist scorelist = new Scorelist(this);

        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item_layout, scorelist.sortedScores());

        ListView listView = (ListView) this.findViewById(R.id.ScoreboardList);

        listView.setAdapter(adapter);

    }
}
