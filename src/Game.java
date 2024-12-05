import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.IOException;

public class Game {
    ArrayList<Player> players;
    Bag bag;
    Dictionary dictionary;
    Board board;
    Scanner scanner;
    Verifier verifier;


    int scorelessTurns;
    ArrayList<View> views;
    ArrayList<Move> currentPlayersMoves;

    ArrayList<Tile> playerPlacedTiles;
    Tile tileToPlace;
    Player currentPlayer;
    int indexOfCurrentPlayer;

    public Game(View v) throws IOException {
        players = new ArrayList<>();
        bag = new Bag();
        board = new Board();
        scanner = new Scanner(System.in);
        dictionary = new Dictionary();
        scorelessTurns = 0;
        views = new ArrayList<>();
        views.add(v);
        tileToPlace = null;
        currentPlayersMoves = new ArrayList<>();
    }

    public void initializePlayer(int i, String playerName) {
        Player player = new Player(i, playerName);
        player.getRack().initializeRack(bag);
        players.add(player);
    }

    public void gameStarted() {
        currentPlayer = players.get(0);
        indexOfCurrentPlayer = 0;
        for (View view : views) {
            view.newPlayer(currentPlayer.getRack(), currentPlayer.getScore());
        }
    }

    public void tileSelected(char c) {
        tileToPlace = currentPlayer.getRack().playTile(c);
        for (View view : views) {
            view.letterPlaced(c);
        }
    }

    //Not the nicest part of this, I'll change it later
    public void firstTurn() {
        board.firstTurn(tileToPlace, 7, 7);

        for (View view : views) {
            view.tilePlaced(7, 7, tileToPlace.getCharacter());
        }
        currentPlayersMoves.add(new Move(7,7,tileToPlace.getCharacter()));
    }

    public void anotherTurn(int row, int column) {
        if (board.addTile(tileToPlace, row, column)) {
            System.out.println(tileToPlace.getCharacter());
            currentPlayersMoves.add(new Move(row, column, tileToPlace.getCharacter()));

            for (View view : views) {
                view.tilePlaced(row, column, tileToPlace.getCharacter());
            }
        }
    }

    public void playerSwitched() {
        if(!currentPlayersMoves.isEmpty()){
            Collections.sort(currentPlayersMoves);
            ArrayList<Move> copyOfMoves = new ArrayList<>(currentPlayersMoves);

            board.addMoves(currentPlayersMoves);
            verifier = new Verifier(currentPlayersMoves);

            if(!verifier.verify(dictionary)){
                for(Move move: copyOfMoves){
                    currentPlayer.getRack().returnToRack(board.removeTile(move.getRow(),move.getCol()));
                    for(View view: views){
                        view.removeTile(move.getRow(),move.getCol());
                    }
                }
            }

            else{
                for(int i = 0; i < copyOfMoves.size(); i++){
                    if(!bag.bagEmpty()){
                        currentPlayer.getRack().addTile(bag);
                    }
                }
            }
            currentPlayersMoves.clear();
            copyOfMoves.clear();
        }

        indexOfCurrentPlayer = indexOfCurrentPlayer + 1;


        if (indexOfCurrentPlayer == players.size()) {
            indexOfCurrentPlayer = 0;
        }

        currentPlayer = players.get(indexOfCurrentPlayer);

        for (View view : views) {
            view.newPlayer(currentPlayer.getRack(), currentPlayer.getScore());
        }

    }


}

