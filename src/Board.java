import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Board implements Serializable {
    private Tile[][] tiles;


    public Board () {
        tiles = new Tile[15][15];
    }

    public boolean addTile(Tile tile, int row, int column){
        int i = row;
        int j = column;

        if(i < 14 && j < 14){
            if((tiles[i][j] == null && tiles[i-1][j] != null) || (tiles[i][j] == null && tiles[i+1][j] != null) ||
                    (tiles[i][j] == null && tiles[i][j-1] != null) || (tiles[i][j] == null && tiles[i][j+1] != null)) {
                tiles[i][j] = tile;
                return true;
            }
        }
        else if(i == 14 && j < 14){
            if((tiles[i][j] == null && tiles[i-1][j] != null) || (tiles[i][j] == null && tiles[i][j-1] != null) || (tiles[i][j] == null && tiles[i][j+1] != null)){
                tiles[i][j] = tile;
                return true;
            }
        }
        else if(i < 14 && j == 14){
            if((tiles[i][j] == null && tiles[i-1][j] != null) || (tiles[i][j] == null && tiles[i+1][j] != null) ||
                    (tiles[i][j] == null && tiles[i][j-1] != null)){
                tiles[i][j] = tile;
                return true;
            }
        }
        else if(i == 14 && j == 14){
            if((tiles[i][j] == null && tiles[i-1][j] != null) ||  (tiles[i][j] == null && tiles[i][j-1] != null)){
                tiles[i][j] = tile;
                return true;
            }
        }
        return false;
    }

    public void firstTurn(Tile tile, int i, int j){
        if(i == 7 && j == 7){
            tiles[7][7] = tile;
        }
    }

    public ArrayList<Move> buildWordHorizontally(ArrayList<Move> moves){
        Collections.sort(moves);
        System.out.println("Moves passed in");
        for(Move move: moves){
            System.out.println(move);
        }
        int currentRow = moves.get(0).getRow();
        int startColumn = moves.get(0).getCol();
        int endColumn = moves.get(moves.size()-1).getCol();
        ArrayList<Move> newMoves = new ArrayList<>(moves);

        while(startColumn != endColumn){
            boolean contains = false;
            Move inBetween = new Move(currentRow, startColumn + 1, tiles[currentRow][startColumn + 1].getCharacter());
            for(Move move: moves){
                if(move.equals(inBetween)){
                    contains = true;
                }
            }
            if(!contains){
                newMoves.add(inBetween);
                System.out.println("Move added " + inBetween.toString() +"\n" );
            }
            startColumn++;
        }

        startColumn = moves.get(0).getCol();

        while ((startColumn - 1 != -1) && tiles[currentRow][startColumn - 1] != null) {
            newMoves.add(new Move(currentRow, startColumn - 1, tiles[currentRow][startColumn - 1].getCharacter()));
            startColumn--;
        }
        while ((endColumn + 1 != 15) && tiles[currentRow][endColumn + 1] != null) {
            newMoves.add(new Move(currentRow, endColumn + 1, tiles[currentRow][endColumn + 1].getCharacter()));
            endColumn++;
        }

        Collections.sort(newMoves);
        System.out.println("New moves passed in");
        for(Move move: newMoves){
            System.out.println(move);
        }
        return newMoves;
    }

    public ArrayList<Move> buildWordVertically(ArrayList<Move> moves){
        Collections.sort(moves);
        int currentColumn = moves.get(0).getCol();
        int startRow = moves.get(0).getRow();
        int endRow = moves.get(moves.size()-1).getRow();
        ArrayList<Move> newMoves = new ArrayList<>(moves);
        System.out.println("Moves passed in");
        for(Move move: moves){
            System.out.println(move);
        }

        while(startRow != endRow){
            boolean contains = false;
            Move inBetween = new Move(startRow + 1, currentColumn, tiles[startRow+1][currentColumn].getCharacter());
            for(Move move: moves){
                if(move.equals(inBetween)){
                    contains = true;
                }
            }
            if(!contains){
                newMoves.add(inBetween);
            }

            startRow++;
        }

        startRow = moves.get(0).getRow();

        while((startRow - 1 != -1) && tiles[startRow-1][currentColumn] != null) {
            newMoves.add(new Move(startRow - 1, currentColumn, tiles[startRow-1][currentColumn].getCharacter()));
            startRow--;
        }
        while((endRow + 1 != 15) && tiles[endRow+1][currentColumn] != null) {
            newMoves.add(new Move(endRow + 1, currentColumn, tiles[endRow+1][currentColumn].getCharacter()));
            endRow++;
        }
        Collections.sort(newMoves);
        System.out.println("New moves passed in");
        for(Move move: newMoves){
            System.out.println(move);
        }
        return newMoves;
    }

    public Tile removeTile(int row, int column){
        Tile tile = tiles[row][column];
        tiles[row][column] = null;
        return tile;
    }

    public boolean checkAdjacentVertical(int row, int col){
        return (tiles[row - 1][col] != null || tiles[row + 1][col] != null);
    }

    public boolean checkAdjacentHorizontal(int row, int col){
        return (tiles[row][col - 1] != null || tiles[row][col+1] != null);
    }

    public Tile getTile(int row, int col){
        return tiles[row][col];
    }

    public void printBoard(){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(tiles[i][j] != null){
                    System.out.print(tiles[i][j].getCharacter() + "|");
                }
                else{
                    System.out.print(" |");
                }
            }
            System.out.println();
        }
    }

}
