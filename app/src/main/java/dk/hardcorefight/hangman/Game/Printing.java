package dk.hardcorefight.hangman.Game;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Printing implements Serializable{

    public Printing(String name, String expansionName, String expansionCode, String imageCode) {
        this.name = name;
        this.expansionName = expansionName;
        this.expansionCode = expansionCode;
        this.imageCode = imageCode;
    }

    private String name;
    private String expansionName;
    private String expansionCode;
    private String imageCode;

    @Override
    public String toString() {
        return "Printing{" +
            "name='" + name + '\'' +
            ", expansionName='" + expansionName + '\'' +
            ", expansionCode='" + expansionCode + '\'' +
            ", imageCode='" + imageCode + '\'' +
            '}';
    }
}
