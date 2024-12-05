import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Board {
    private Tile[][] tiles;


    public Board () throws IOException {
        tiles = new Tile[15][15];
    }

    public boolean addTile(Tile tile, int row, int column){
        int i = row;
        int j = column;

        //Replace the multiple if statements later, but you can only add beside a tile already on the board unless the game just
        //started
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

    //Rn this adds the letters on the board before this players turn, this needs to be split into two functions
    //one for vertical and one for horizontal because of the case where you put down a word horizontally but it
    //touches multiple words vertically, or vice versa
    public void addMoves(ArrayList<Move> moves) {
        //Save the coordinates of the first tile the player placed, and the last tile the player placed
        int startRow = moves.get(0).getRow();
        int endRow = moves.get(moves.size() - 1).getRow();
        int startColumn = moves.get(0).getCol();
        int endColumn = moves.get(moves.size() - 1).getCol();

        //If rows of first and last tile are equal the player placed a word horizontally, otherwise vertically
        boolean isHorizontal = moves.get(0).getRow() == moves.get(moves.size() - 1).getRow();
        boolean isVertical = !isHorizontal;

        //If only one tile was placed, can't determine whether horizontal or vertical yet, so check both, this won't work atm ik why
        if(moves.size() == 1){
            isHorizontal = true;
            isVertical = true;
        }

        //First if there are letters in between the players placed word add those letters to the word, you can get rid of this and
        // just add it into the while loop that follows after temporary fix
        if(isVertical){
            while(startRow != endRow){
                Move inBetween = new Move(startRow + 1, startColumn, tiles[startRow + 1][startColumn].getCharacter());
                if(!moves.contains(inBetween)){
                    moves.add(inBetween);
                }
                startRow++;
            }
        }

        //Reset variable
        startRow = moves.get(0).getRow();

        //If there are letters in between the players placed word add those letters to the word
        if(isHorizontal){
            while(startColumn != endColumn){
                System.out.println("Checking start column" + startColumn);
                Move inBetween = new Move(startRow, startColumn + 1, tiles[startRow][startColumn + 1].getCharacter());
                if(!moves.contains(inBetween)){
                    moves.add(inBetween);
                }
                startColumn++;
            }
        }

        //Reset variable
        startColumn = moves.get(0).getCol();

        //If the word was horizontal, and there are tiles to the right or to the left then add these tiles to
        //the word and check the full word
        if(isHorizontal){
            while ((startColumn - 1 != -1) && tiles[startRow][startColumn - 1] != null) {
                moves.add(new Move(startRow, startColumn - 1, tiles[startRow][startColumn - 1].getCharacter()));
                startColumn--;
            }
            while ((endColumn + 1 != 15) && tiles[startRow][endColumn + 1] != null) {
                moves.add(new Move(startRow, endColumn + 1, tiles[startRow][endColumn + 1].getCharacter()));
                endColumn++;
            }
        }

        //This needs to be reset for the one tile case, atm blegh
        startColumn = moves.get(0).getCol();

        //If the word was vertical, and there are tiles above or below, add these tiles to the word and check the full
        //thing
        if(isVertical) {
            while((startRow - 1 != -1) && tiles[startRow-1][startColumn] != null) {
                moves.add(new Move(startRow - 1, startColumn, tiles[startRow-1][startColumn].getCharacter()));
                startRow--;
            }
            while((endRow + 1 != 15) && tiles[endRow+1][startColumn] != null) {
                moves.add(new Move(endRow + 1, startColumn, tiles[endRow+1][startColumn].getCharacter()));
                endRow++;
            }
        }
        //Sort these tiles so that you can correctly verify the word in the order it appeared on the board
        Collections.sort(moves);
    }

    public Tile removeTile(int row, int column){
        Tile tile = tiles[row][column];
        tiles[row][column] = null;
        return tile;
    }

}
