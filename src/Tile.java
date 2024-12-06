import java.io.Serializable;

public class Tile implements Serializable {
    private int score;
    private char character;

    public Tile(char character, int score) {
        this.score = score;
        this.character = character;
    }

    public int getScore() {
        return score;
    }

    public char getCharacter() {
        return character;
    }

    public String toString() {
        String s = character + "," + score;
        return s;
    }
}
