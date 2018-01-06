package dk.hardcorefight.hangman.Game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import dk.hardcorefight.hangman.Menu.MenuActivity;
import dk.hardcorefight.hangman.R;

public class PostGame extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        this.findViewById(R.id.PostGameOk).setOnClickListener(this);
        ((TextView)this.findViewById(R.id.PostGameInfo)).setText(
                this.getIntent().getStringExtra("INFO")
        );

    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(PostGame.this, MenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }
}
