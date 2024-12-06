import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Verifier implements Serializable {
    HashMap<Character, Integer> tileScores;
    ArrayList<Move> checkMoves;

    public Verifier(ArrayList<Move> checkMoves) {
        this.checkMoves = checkMoves;
        this.tileScores = new HashMap<>();
        this.initializeScores();
    }

    public Verifier(){
        this.tileScores = new HashMap<>();
        this.initializeScores();
    }

    public void sortList(){
        Collections.sort(checkMoves);
    }

    public void initializeScores(){
        tileScores.put('E', 1);
        tileScores.put('A', 1);
        tileScores.put('I', 1);
        tileScores.put('O', 1);
        tileScores.put('U', 1);
        tileScores.put('N', 1);
        tileScores.put('R', 1);
        tileScores.put('T', 1);
        tileScores.put('L', 1);
        tileScores.put('S', 1);

        tileScores.put('D', 2);
        tileScores.put('G', 2);

        tileScores.put('B', 2);
        tileScores.put('C', 2);
        tileScores.put('M', 2);
        tileScores.put('P', 2);

        tileScores.put('F', 4);
        tileScores.put('H', 4);
        tileScores.put('V', 4);
        tileScores.put('W', 4);
        tileScores.put('Y', 4);

        tileScores.put('K', 5);

        tileScores.put('J', 8);
        tileScores.put('X', 8);

        tileScores.put('Q', 10);
        tileScores.put('Z', 10);

    }

    public boolean verify(Dictionary dictionary) {
        this.sortList();
        StringBuilder sOne = new StringBuilder();

        for(Move move : checkMoves) {
            sOne.append(move.getC());
        }

        if(dictionary.inDictionary(sOne.toString().toLowerCase())) {
            System.out.println("In dictionary:");
            System.out.println(sOne);
            return true;
        }
        return false;
    }

    public int computeScore(){
        int score = 0;
        for(Move move: checkMoves) {
            score += tileScores.get(move.getC());
        }
        return score;
    }

    public int getTileScore(char c){
        return tileScores.get(c);
    }

}
