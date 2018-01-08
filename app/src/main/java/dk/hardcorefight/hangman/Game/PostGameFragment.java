package dk.hardcorefight.hangman.Game;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.jinatonic.confetti.CommonConfetti;

import java.io.IOException;

import dk.hardcorefight.hangman.Menu.MenuActivity;
import dk.hardcorefight.hangman.R;
import dk.hardcorefight.hangman.Scoreboard.Scorelist;
import dk.hardcorefight.hangman.Sound.SoundPlayer;
import lombok.NonNull;


public class PostGameFragment extends Fragment implements View.OnClickListener {

    private static final String wonParameter = "WON";
    private static final String amountWrongGuessesParameter = "WRONGGUESSES";
    private static final String printingNameParamater = "PRINTINGNAME";

    private boolean won;
    private int amountWrongGuesses;
    private String printingName;

    private TextView postGameInfo;
    private TextView postGameHighScoreView;

    public PostGameFragment() {
        // Required empty public constructor
    }


    public static PostGameFragment newInstance(boolean won, int amountWrongGuesses, String printingName) {
        PostGameFragment fragment = new PostGameFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(PostGameFragment.wonParameter, won);
        bundle.putInt(PostGameFragment.amountWrongGuessesParameter, amountWrongGuesses);
        bundle.putString(PostGameFragment.printingNameParamater, printingName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
            this.won = this.getArguments().getBoolean(PostGameFragment.wonParameter);
            this.amountWrongGuesses = this.getArguments().getInt(PostGameFragment.amountWrongGuessesParameter);
            this.printingName = this.getArguments().getString(PostGameFragment.printingNameParamater);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_post_game, container, false);

        this.postGameInfo = view.findViewById(R.id.PostGameInfo);
        this.postGameHighScoreView = view.findViewById(R.id.PostGameHighScoreView);

        if (this.won) {
            this.postGameInfo.setText(R.string.YouWon);

            Scorelist scorelist = new Scorelist(this.getActivity());

            if (
                scorelist.sortedScores().isEmpty()
                || this.amountWrongGuesses < scorelist.sortedScores().get(0)
            ) {
                try {
                    scorelist.addScore(this.amountWrongGuesses).save();
                } catch (IOException e) {
                    Log.e("postgame", "cannot save highscore", e);
                }
                this.postGameHighScoreView.setVisibility(View.VISIBLE);
                this.postGameHighScoreView.setText(
                    this.getResources()
                        .getString(
                            R.string.NewHighscore,
                            String.valueOf(this.amountWrongGuesses)
                        )
                );
            }

        } else {
            this.postGameInfo.setText(
                this.getResources()
                    .getString(
                        R.string.YouLost,
                        this.printingName
                    )
            );
        }

        view.findViewById(R.id.PostGameOk).setOnClickListener(this);

        if (this.won) {
            SoundPlayer.play(R.raw.success);
            CommonConfetti.rainingConfetti(
                container,
                new int[] {Color.WHITE, Color.BLUE, Color.BLACK, Color.RED, Color.GREEN}
            ).infinite();
        } else {
            SoundPlayer.play(R.raw.error);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this.getActivity(), MenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }

}
