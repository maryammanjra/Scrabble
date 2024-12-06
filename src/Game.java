import java.io.*;
import java.util.ArrayList;

public class Game implements Serializable {
    ArrayList<Player> players;
    Bag bag;
    Dictionary dictionary;
    versionedBoard board;
    Verifier verifier;


    int scorelessTurns;
    int addToScore, adjacencyScores;
    ArrayList<View> views;
    ArrayList<Move> currentPlayersMoves;
    ArrayList<Move> undoneMoves;

    ArrayList<Tile> playerPlacedTiles;
    Tile tileToPlace;
    Player currentPlayer;
    int indexOfCurrentPlayer;

    public Game() throws IOException {
        players = new ArrayList<>();
        bag = new Bag();
        board = new versionedBoard(true);
        dictionary = new Dictionary();
        scorelessTurns = 0;
        views = new ArrayList<>();
        tileToPlace = null;
        currentPlayersMoves = new ArrayList<>();
        addToScore = 0;
        adjacencyScores = 0;
    }

    public void customBoard(){
        try{
            customBoardParser customBoard = new customBoardParser("./resources/customBoard.xml");
            customBoard.importFromXML();
            board = customBoard.getBoard();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Print board in game class:");
        board.printBoard();
        views.get(0).setDoubleAndTriples(board);
    }

    public void addView(View v) {
        views.add(v);
    }

    public void initializePlayer(int i, String playerName) {
        Player player = new Player(i, playerName);
        player.getRack().initializeRack(bag);
        players.add(player);
    }

    public void gameStarted() {
        views.get(0).setDoubleAndTriples(board);
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
                int addToScore = verifier.computeScoreVersionedBoard(board) + adjacencyScores;
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
                adjacencyScores += verifierH.computeScoreVersionedBoard(board) +
                        verifierV.computeScoreVersionedBoard(board);
            }
        }
        else if(horizontalWord != null){
            verifierH = new Verifier(horizontalWord);
            if(!verifierH.verify(dictionary)){
                invalidOneLetter = true;
            }
            else{
                adjacencyScores += verifierH.computeScoreVersionedBoard(board);
            }
        }
        else if(verticalWord != null){
            verifierV = new Verifier(verticalWord);
            if(!verifierV.verify(dictionary)){
                invalidOneLetter = true;
            }
            else{
                adjacencyScores += verifierV.computeScoreVersionedBoard(board);
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
               adjacencyScores += verifierV.computeScoreVersionedBoard(board);
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
                adjacencyScores += verifierH.computeScoreVersionedBoard(board);
                return true;
            }
        }
        else{
            return true;
        }
    }

    public void undo(){
        undoneMoves = new ArrayList<>();
        if(!currentPlayersMoves.isEmpty()){
           Move moveToUndo = currentPlayersMoves.get(currentPlayersMoves.size()-1);

           views.get(0).removeTile(moveToUndo.getRow(),moveToUndo.getCol());
           currentPlayer.getRack().returnToRack(board.removeTile(moveToUndo.getRow(),moveToUndo.getCol()));
           views.get(0).updateRack(currentPlayer.getRack());

           undoneMoves.add(moveToUndo);
           currentPlayersMoves.remove(moveToUndo);
        }
    }

    public void redo(){
        Verifier verifier = new Verifier();
        if(!undoneMoves.isEmpty()){
            Move moveToRedo = undoneMoves.get(undoneMoves.size()-1);
            views.get(0).tilePlaced(moveToRedo.getRow(), moveToRedo.getCol(), moveToRedo.getC());

            currentPlayer.getRack().playTile(moveToRedo.getC());
            views.get(0).updateRack(currentPlayer.getRack());
            board.addTile(new Tile(moveToRedo.getC(), verifier.getTileScore(moveToRedo.getC())), moveToRedo.getRow(), moveToRedo.getCol());

            undoneMoves.remove(moveToRedo);
            currentPlayersMoves.add(moveToRedo);
        }
    }

    public void serializeGame(String fileName){
        try{
            FileOutputStream fileOutputStream = new FileOutputStream("./resources/" + fileName + ".txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static Game deserializeGame(String fileName){
        try{
            FileInputStream fileInputStream = new FileInputStream("./resources/" + fileName + ".txt");
            ObjectInputStream objectInput = new ObjectInputStream(fileInputStream);
            try{
                Game reloadedGame = (Game)(objectInput.readObject());
                objectInput.close();
                return reloadedGame;
            }
            catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        catch(FileNotFoundException e){} catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public versionedBoard returnBoard(){
        return board;
    }

}

