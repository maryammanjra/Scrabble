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
    int addToScore, adjacencyScores;
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
        addToScore = 0;
        adjacencyScores = 0;
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
        if(!currentPlayersMoves.isEmpty()) {
            this.checkWords();
        }

        indexOfCurrentPlayer = (indexOfCurrentPlayer + 1)  % players.size();
        currentPlayer = players.get(indexOfCurrentPlayer);
        addToScore = 0;
        adjacencyScores = 0;

        for (View view : views) {
            view.newPlayer(currentPlayer.getRack(), currentPlayer.getScore());
        }
    }

    public void checkWords(){
        boolean isHorizontal = currentPlayersMoves.get(0).getRow() ==
                currentPlayersMoves.get(currentPlayersMoves.size() - 1).getRow();
        boolean isVertical = !isHorizontal;
        boolean adjoiningInvalid = false;

        if(currentPlayersMoves.size() > 1){
            ArrayList<Move> fullWord;
            if(isHorizontal){
                fullWord = board.buildWordHorizontally(currentPlayersMoves);
                for(Move move : currentPlayersMoves){
                    if(!checkNeighboursVertical(move)){
                        adjoiningInvalid = true;
                    }
                }
            }
            else{
                fullWord = board.buildWordVertically(currentPlayersMoves);
                for(Move move : currentPlayersMoves){
                    if(!checkNeighboursHorizontal(move)){
                        adjoiningInvalid = true;
                    }
                }
            }

            verifier = new Verifier(fullWord);

            if(!verifier.verify(dictionary) || adjoiningInvalid){
                for(Move move: currentPlayersMoves){
                    currentPlayer.getRack().returnToRack(board.removeTile(move.getRow(),move.getCol()));
                    for(View view: views){
                        view.removeTile(move.getRow(),move.getCol());
                    }
                }
            }

            else{
                for(int i = 0; i < currentPlayersMoves.size(); i++){
                    if(!bag.bagEmpty()){
                        currentPlayer.getRack().addTile(bag);
                    }
                }
                int addToScore = verifier.computeScore() + adjacencyScores;
                currentPlayer.addToScore(addToScore);
                views.get(0).scoreUpdated(currentPlayer.getScore());
            }
        }

        else if(currentPlayersMoves.size() == 1){
            this.processOneLetterPlacement();
            currentPlayer.addToScore(adjacencyScores);
            views.get(0).scoreUpdated(currentPlayer.getScore());
        }

        currentPlayersMoves.clear();
    }

    public void processOneLetterPlacement() {
        boolean invalidOneLetter = false;
        ArrayList<Move> horizontalWord = null;
        ArrayList<Move> verticalWord = null;
        int row = currentPlayersMoves.get(0).getRow();
        int column = currentPlayersMoves.get(0).getCol();
        Verifier verifierH;
        Verifier verifierV;

        if(board.checkAdjacentVertical(row, column) && board.checkAdjacentHorizontal(row, column)){
            horizontalWord = board.buildWordHorizontally(currentPlayersMoves);
            verticalWord = board.buildWordVertically(currentPlayersMoves);
        }
        else if(board.checkAdjacentHorizontal(row, column)){
            horizontalWord = board.buildWordHorizontally(currentPlayersMoves);
        }
        else if(board.checkAdjacentVertical(row, column)){
            verticalWord = board.buildWordVertically(currentPlayersMoves);
        }

        if(horizontalWord != null && verticalWord != null){
            verifierH = new Verifier(horizontalWord);
            verifierV = new Verifier(verticalWord);
            if(!verifierH.verify(dictionary) || !verifierV.verify(dictionary)){
                invalidOneLetter = true;
            }
            else{
                adjacencyScores += verifierH.computeScore() + verifierV.computeScore();
            }
        }
        else if(horizontalWord != null){
            verifierH = new Verifier(horizontalWord);
            if(!verifierH.verify(dictionary)){
                invalidOneLetter = true;
            }
            else{
                adjacencyScores += verifierH.computeScore();
            }
        }
        else if(verticalWord != null){
            verifierV = new Verifier(verticalWord);
            if(!verifierV.verify(dictionary)){
                invalidOneLetter = true;
            }
            else{
                adjacencyScores += verifierV.computeScore();
            }
        }

        if(invalidOneLetter){
            currentPlayer.getRack().returnToRack(board.removeTile(row,column));
            views.get(0).removeTile(row,column);
        }
        else{
            currentPlayer.getRack().addTile(bag);
        }
    }

    public boolean checkNeighboursVertical(Move move){
       int row = move.getRow();
       int col = move.getCol();

       if(board.checkAdjacentVertical(row, col)){
           ArrayList<Move> verticalWord = new ArrayList<>();
           ArrayList<Move> builtWord;
           verticalWord.add(move);
           builtWord = board.buildWordVertically(verticalWord);
           Verifier verifierV = new Verifier(builtWord);
           if(!verifierV.verify(dictionary)){
               return false;
           }
           else{
               adjacencyScores += verifierV.computeScore();
               return true;
           }
       }

       else{
           return true;
       }
    }

    public boolean checkNeighboursHorizontal(Move move){
        int row = move.getRow();
        int col = move.getCol();
        if(board.checkAdjacentHorizontal(row, col)){
            ArrayList<Move> horizontalWord = new ArrayList<>();
            ArrayList<Move> builtWord;
            horizontalWord.add(move);
            builtWord = board.buildWordHorizontally(horizontalWord);
            Verifier verifierH = new Verifier(builtWord);
            if(!verifierH.verify(dictionary)){
                return false;
            }
            else{
                adjacencyScores += verifierH.computeScore();
                return true;
            }
        }
        else{
            return true;
        }
    }
}

