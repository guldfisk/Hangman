package dk.hardcorefight.hangman.Game;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import dk.hardcorefight.hangman.R;


public class PlayGameFragment
extends
    Fragment
{

    private static final String cardParameter = "CARD";
    private static final String cardImageParameter = "IMAGE";

    private Printing card;
    private Bitmap cardImage;

    private HashMap<String, Button> buttons;

    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    private ArrayList<Character> letters;

    private HashMap<String, Button> constructButtonGrid(LinearLayout layout, ArrayList<String> names, int rowLength) {
        HashMap<String, Button> buttons = new HashMap<>();
        Iterator<String> remainingNames = names.iterator();
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
                String name = remainingNames.next();
                Button button = new Button(this.getActivity());
                button.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                button.setText(name);
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

    public static PlayGameFragment newInstance(Printing card, Bitmap cardImage) {
        PlayGameFragment fragment = new PlayGameFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PlayGameFragment.cardParameter, card);
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
            this.card = (Printing) this.getArguments().getSerializable(
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
    }

    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_play_game, container, false);

        this.letters = new ArrayList<>();
        for (char chr : PlayGameFragment.alphabet.toCharArray()) {
            this.letters.add(chr);
        }

        ArrayList<String> letters = new ArrayList<>();
        for (Character letter : this.letters) {
            letters.add(letter.toString());
        }
        this.buttons = this.constructButtonGrid(
            (LinearLayout) view.findViewById(R.id.ButtonLayout),
            letters,
            6
        );
        ((ImageView) view.findViewById(R.id.GamePrintingArtImage)).setImageBitmap(this.cardImage);
        return view;
    }

}
