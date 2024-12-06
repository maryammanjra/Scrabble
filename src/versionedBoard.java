import java.security.KeyStore;
import java.util.HashMap;

public class versionedBoard extends Board {
    public int[][] scoreMultipliers;
    HashMap<Coordinate, Integer> wordMultipliers;

    public versionedBoard() {
        super();
        scoreMultipliers = new int[15][15];
        wordMultipliers = new HashMap<>();
        for(int i = 0; i < scoreMultipliers.length; i++) {
            for (int j = 0; j < scoreMultipliers[i].length; j++) {
                scoreMultipliers[i][j] = 1;
            }
        }
        this.initializeTripleWordMultipliers();
        this.initializeDoubleWordMultipliers();
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

    public void printBoard(){
        for(int i = 0; i < scoreMultipliers.length; i++){
            for(int j = 0; j < scoreMultipliers[i].length; j++){
                System.out.print(scoreMultipliers[i][j] + "|");
            }
            System.out.println();
        }
    }

    public void printMultipliers(){
        for(HashMap.Entry<Coordinate, Integer> entry: wordMultipliers.entrySet()){
            System.out.println(entry.getKey() + "|" + entry.getValue());
        }
    }

    public static void main(String[] args) {
        versionedBoard board = new versionedBoard();
        board.setDoubles();
        board.setTriples();
        board.printBoard();
        board.printMultipliers();
    }
}
