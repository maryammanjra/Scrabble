import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;

public class VersionedBoard extends Board implements Serializable {
    public int[][] scoreMultipliers;
    HashMap<Coordinate, Integer> wordMultipliers;

    public VersionedBoard(boolean originalBoard) {
        super();
        scoreMultipliers = new int[15][15];
        wordMultipliers = new HashMap<>();
        for(int i = 0; i < scoreMultipliers.length; i++) {
            for (int j = 0; j < scoreMultipliers[i].length; j++) {
                scoreMultipliers[i][j] = 1;
            }
        }
        if(originalBoard) {
            this.setDoubles();
            this.setTriples();
            this.initializeTripleWordMultipliers();
            this.initializeDoubleWordMultipliers();
        }
    }

    public void setDoubles(){
        scoreMultipliers[0][3] = 2;
        scoreMultipliers[0][11] = 2;

        scoreMultipliers[2][6] = 2;
        scoreMultipliers[2][8] = 2;

        scoreMultipliers[3][0] = 2;
        scoreMultipliers[3][7] = 2;
        scoreMultipliers[3][14] = 2;

        scoreMultipliers[6][2] = 2;
        scoreMultipliers[6][6] = 2;
        scoreMultipliers[6][8] = 2;
        scoreMultipliers[6][12] = 2;

        scoreMultipliers[7][3] = 2;
        scoreMultipliers[7][11] = 2;

        scoreMultipliers[8][2] = 2;
        scoreMultipliers[8][6] = 2;
        scoreMultipliers[8][8] = 2;
        scoreMultipliers[8][12] = 2;

        scoreMultipliers[11][0] = 2;
        scoreMultipliers[11][7] = 2;
        scoreMultipliers[11][14] = 2;

        scoreMultipliers[12][6] = 2;
        scoreMultipliers[12][8] = 2;

        scoreMultipliers[14][3] = 2;
        scoreMultipliers[14][11] = 2;
    }

    public void setTriples(){
        scoreMultipliers[1][5] = 3;
        scoreMultipliers[1][9] = 3;

        scoreMultipliers[5][1] = 3;
        scoreMultipliers[5][5] = 3;
        scoreMultipliers[5][9] = 3;
        scoreMultipliers[5][13] = 3;

        scoreMultipliers[9][1] = 3;
        scoreMultipliers[9][5] = 3;
        scoreMultipliers[9][9] = 3;
        scoreMultipliers[9][13] = 3;

        scoreMultipliers[13][5] = 3;
        scoreMultipliers[13][9] = 3;
    }

    public void initializeTripleWordMultipliers() {
        wordMultipliers.put(new Coordinate(0, 0), 3);
        wordMultipliers.put(new Coordinate(0, 7), 3);
        wordMultipliers.put(new Coordinate(0, 14), 3);

        wordMultipliers.put(new Coordinate(7, 0), 3);
        wordMultipliers.put(new Coordinate(7, 14), 3);

        wordMultipliers.put(new Coordinate(14, 0), 3);
        wordMultipliers.put(new Coordinate(14, 7), 3);
        wordMultipliers.put(new Coordinate(14, 14), 3);

    }

    public void initializeDoubleWordMultipliers() {
        int rowCount = 1;
        int rightCol = 1;
        int leftCol = 13;

        while(rowCount < 5){
            wordMultipliers.put(new Coordinate(rowCount, rightCol), 2);
            wordMultipliers.put(new Coordinate(rowCount, leftCol), 2);
            rightCol++;
            leftCol--;
            rowCount++;
        }

        rowCount = 10;
        rightCol--;
        leftCol++;

        while(rowCount < 14){
            wordMultipliers.put(new Coordinate(rowCount, rightCol), 2);
            wordMultipliers.put(new Coordinate(rowCount, leftCol), 2);
            rightCol--;
            leftCol++;
            rowCount++;
         }
    }

    public int returnScore(int row, int col) {
        return scoreMultipliers[row][col];
    }

    public boolean tripleWord(int row, int col) {
        Coordinate coordinate = new Coordinate(row, col);
        for(Coordinate c: wordMultipliers.keySet()){
            if(c.equals(coordinate) && wordMultipliers.get(c).equals(3)){
                return true;
            }
        }
        return false;
    }

    public boolean doubleWord(int row, int col) {
        Coordinate coordinate = new Coordinate(row, col);
        for(Coordinate c: wordMultipliers.keySet()){
            if(c.equals(coordinate) && wordMultipliers.get(c).equals(2)){
                return true;
            }
        }
        return false;
    }

    public void initializeDoublesFromArray(ArrayList<Coordinate> coordinates){
        for(Coordinate c: coordinates){
            scoreMultipliers[c.row][c.col] = 2;
        }
    }

    public void initializeTriplesFromArray(ArrayList<Coordinate> coordinates){
        for(Coordinate c: coordinates){
            scoreMultipliers[c.row][c.col] = 2;
        }
    }

    public void initializeDoubleWordsFromArray(ArrayList<Coordinate> coordinates){
        for (Coordinate c: coordinates){
            wordMultipliers.put(new Coordinate(c.row, c.col), 2);
        }
    }

    public void initializeTripleWordsFromArray(ArrayList<Coordinate> coordinates){
        for (Coordinate c: coordinates){
            wordMultipliers.put(new Coordinate(c.row, c.col), 3);
        }
    }

    public void printBoard(){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                System.out.print(scoreMultipliers[i][j] + "|");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        VersionedBoard board = new VersionedBoard(true);
        System.out.println(board.tripleWord(0,0));
        board.printBoard();
    }
}
