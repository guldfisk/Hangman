package dk.hardcorefight.hangman.Game;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.AbstractList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import dk.hardcorefight.hangman.Game.HangmanGame.HangmanGame;
import dk.hardcorefight.hangman.Game.HangmanGame.SecretWord;
import dk.hardcorefight.hangman.R;

public class PlayGameFragment
extends
    Fragment
implements
    View.OnClickListener
{

    private static final String cardParameter = "PRINTING";
    private static final String cardImageParameter = "IMAGE";

    private Printing printing;
    private Bitmap cardImage;

    private HangmanGame hangmanGame;

    private HashMap<Character, Button> buttons;

    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    private Set<GameFinishedListener> gameFinishedListeners = Collections.synchronizedSet(
        new HashSet<GameFinishedListener>()
    );

    private HashMap<Character, Button> constructButtonGrid(
        LinearLayout layout,
        List<Character> names,
        int rowLength
    ) {
        HashMap<Character, Button> buttons = new HashMap<>();
        Iterator<Character> remainingNames = names.iterator();
        while (remainingNames.hasNext()) {
            LinearLayout row = new LinearLayout(this.getActivity());
            row.setLayoutParams(
                new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            );
            for (int i = 0; i < rowLength; i++) {
                if (!remainingNames.hasNext()) {
                    break;
                }
                Character name = remainingNames.next();
                Button button = new Button(this.getActivity());
                button.setLayoutParams(
                    new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                    )
                );
                button.setText(String.valueOf(name));
                row.addView(button);
                buttons.put(name, button);
            }
            layout.addView(row);
        }
        return buttons;
    }

    public PlayGameFragment() {
        // Required empty public constructor
    }

    public static PlayGameFragment newInstance(Printing printing, Bitmap cardImage) {
        PlayGameFragment fragment = new PlayGameFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PlayGameFragment.cardParameter, printing);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        cardImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bundle.putByteArray(PlayGameFragment.cardImageParameter, byteArray);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.printing = (Printing) this.getArguments().getSerializable(
                PlayGameFragment.cardParameter
            );
            byte[] byteArray = this.getArguments().getByteArray(
                PlayGameFragment.cardImageParameter
            );
            this.cardImage = BitmapFactory.decodeByteArray(
                byteArray,
                0,
                byteArray.length
            );
        } else {
            throw new RuntimeException();
        }
        this.hangmanGame = new HangmanGame();
        this.hangmanGame.initialize(
            new SecretWord(this.printing.getName(), Pattern.compile("[a-zA-Z]"))
        );
    }

    public static List<Character> stringAsCharacterList(final String string) {
        return new AbstractList<Character>() {
            public int size() { return string.length(); }
            public Character get(int index) { return string.charAt(index); }
        };
    }

    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_play_game, container, false);

        this.buttons = this.constructButtonGrid(
            (LinearLayout) view.findViewById(R.id.ButtonLayout),
            PlayGameFragment.stringAsCharacterList(PlayGameFragment.alphabet),
            7
        );
        for (Button button : buttons.values()) {
            button.setOnClickListener(this);
        }
        ((ImageView) view.findViewById(R.id.GamePrintingArtImage)).setImageBitmap(this.cardImage);
        this.updateGameInfo(view);
        return view;
    }

    private void updateGameInfo(View view) {
        Log.e("playgame", this.hangmanGame.getSecretWord().toString());
        Log.e("playgame", String.valueOf(this.hangmanGame.getAmountWrongGuesses())+" wrong guesses");
        ((TextView) view.findViewById(R.id.GuessedWordView))
            .setText(this.hangmanGame.getSecretWord().toString());
        ((TextView) view.findViewById(R.id.GameStatusTextView))
            .setText(String.valueOf(this.hangmanGame.getAmountWrongGuesses())+" wrong guesses");
    }

    private void guessLetter(Character letter) {
        this.hangmanGame.guessLetter(letter);
        this.buttons.get(letter).setEnabled(false);
        this.updateGameInfo(this.getView());
        if (this.hangmanGame.isFinished()) {
            this.finishGame();
        }
    }

    private void finishGame() {
        for (GameFinishedListener gameFinishedListener : this.gameFinishedListeners) {
            gameFinishedListener.onGameFinished(this.hangmanGame);
        }
    }

    @Override
    public void onClick(View view) {
        this.guessLetter(
            ((Button)view).getText().charAt(0)
        );
    }

    public void addGameFinishedListener(GameFinishedListener gameFinishedListener) {
        this.gameFinishedListeners.add(gameFinishedListener);
    }

    public void removeGameFinishedListener(GameFinishedListener gameFinishedListener) {
        this.gameFinishedListeners.remove(gameFinishedListener);
    }
}
