package dk.hardcorefight.hangman.Scoreboard;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Scorelist {

    private Context context;
    private static final String filename = "scorelist";
    private Set<Integer> scores;


    public Scorelist(Context context) {
        this.scores = new HashSet<>();
        this.context = context;

        try {
            BufferedReader fileReadStream = new BufferedReader(
                new InputStreamReader(
                    context.openFileInput(Scorelist.filename)
                )
            );
            String line;
            while ((line = fileReadStream.readLine()) != null) {
                this.scores.add(
                    Integer.parseInt(line)
                );
            }
        } catch (IOException e) {

        }

    }

    public Set<Integer> getScores() {
        return scores;
    }

    public Scorelist addScore(Integer score) {
        this.scores.add(score);
        return this;
    }

    public List<Integer> sortedScores(){
        Integer[] values = new Integer[this.scores.size()];
        this.scores.toArray(values);
        Arrays.sort(values);
        return new ArrayList<>(Arrays.asList(values));
    }

    public Scorelist save() throws IOException {
        FileOutputStream fileStream = this.context.openFileOutput(Scorelist.filename, Context.MODE_PRIVATE);
        for (Integer score : this.scores) {
            fileStream.write(
                (score.toString() + "\n").getBytes()
            );
        }
        fileStream.close();
        return this;
    }

    public Scorelist clear() {
        this.scores.clear();
        return this;
    }
}
