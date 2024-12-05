import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Verifier {

    ArrayList<Move> checkMoves;

    public Verifier(ArrayList<Move> checkMoves) {
        this.checkMoves = checkMoves;
    }

    public void sortList(){
        Collections.sort(checkMoves);
        System.out.println("Sorted moves:" + checkMoves);
    }

    public boolean verify(Dictionary dictionary) {
        this.sortList();
        StringBuilder sOne = new StringBuilder();

        for(Move move : checkMoves) {
            sOne.append(move.getC());
        }

        System.out.println(sOne.toString());

        if(dictionary.inDictionary(sOne.toString().toLowerCase())) {
            System.out.println("In dictionary:");
            return true;
        }
        return false;
    }
}
