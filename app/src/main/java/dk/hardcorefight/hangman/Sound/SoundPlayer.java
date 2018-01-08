package dk.hardcorefight.hangman.Sound;


import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dk.hardcorefight.hangman.R;

public class SoundPlayer {

    private static Set<Integer> sounds = new HashSet<>();
    @SuppressLint("UseSparseArrays")
    private static Map<Integer, MediaPlayer> soundMap = new HashMap<>();

    public static void init(Context context) {

        SoundPlayer.sounds.add(R.raw.success);
        SoundPlayer.sounds.add(R.raw.error);

        for (Integer key : SoundPlayer.sounds) {
            SoundPlayer.soundMap.put(
                key,
                MediaPlayer.create(context, key)
            );
        }

    }

    public static void play(int id) {
        final MediaPlayer mediaPlayer = SoundPlayer.soundMap.get(id);
        mediaPlayer.start();
    }

}
