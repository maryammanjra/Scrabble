import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.Serializable;

public class ScrabbleView extends JFrame implements View, Serializable {
    JButton[][] buttons;
    Game game;
    Controller controller;
    JButton[] rackButtons;
    Color boardColour = new Color(192,64,43);


    public ScrabbleView() throws IOException {
        super("Scrabble");

        this.game = new Game();
        game.addView(this);
        this.controller = new Controller(game, this);

        buttons = new JButton[15][15];
        rackButtons = new JButton[14];
        Container contentPane = this.getContentPane();
        contentPane.setBackground(boardColour);
        this.setSize(715, 515);
        this.setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel();
        JPanel rackPanel = new JPanel();
        JOptionPane startGame = new JOptionPane();
        startGame.setBackground(boardColour);
        JOptionPane.showMessageDialog(null, "Click OK to Begin Playing", "Welcome to Scrabble", JOptionPane.PLAIN_MESSAGE);
        JOptionPane playerNumbers = new JOptionPane();

        String numPlayers = JOptionPane.showInputDialog(null, "Please enter the number of players", "Players", JOptionPane.PLAIN_MESSAGE);
        int intPlayers = Integer.parseInt(numPlayers);

        for(int i = 0; i < intPlayers; i++){
            JOptionPane playerName = new JOptionPane();
            String name = JOptionPane.showInputDialog(null, "Please enter the name of the player", "Player " + (i + 1), JOptionPane.PLAIN_MESSAGE);
            game.initializePlayer((i+1), name);
        }

        rackPanel.setBackground(Color.WHITE);
        JButton rackButton = new JButton(" ");
        rackButton.setForeground(boardColour);
        rackButton.setLayout(new GridLayout(11, 1));

        for (int i = 0; i < 8; i++) {
            JButton button = new JButton(" ");
            if(i == 7){
                button.setText("Done");
                button.setActionCommand("Done");
            }
            rackButtons[i] = button;
            rackButton.add(button);
        }

        rackPanel.setLayout(new BorderLayout());
        rackPanel.add(rackButton, BorderLayout.CENTER);
        boardPanel.setBackground(boardColour);
        boardPanel.setPreferredSize(new Dimension(510, 500));

        GridLayout grid = new GridLayout(15, 15);
        grid.setHgap(1);
        grid.setVgap(1);
        boardPanel.setLayout(grid);

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                JButton button = new JButton(" ");
                button.setBackground(boardColour);
                this.buttons[i][j] = button;
                button.setActionCommand(i + "." + j);
                boardPanel.add(button);
            }
        }

        contentPane.add(boardPanel, BorderLayout.WEST);
        contentPane.add(rackPanel, BorderLayout.EAST);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                buttons[i][j].addActionListener(controller);
                if(i == 7 && j== 7){
                    buttons[i][j].setText("S");
                }
            }
        }


        rackButtons[8] = new JButton("Score: ");
        rackButton.add(rackButtons[8]);

        rackButtons[9] = new JButton("Pass");
        rackButton.add(rackButtons[9]);
        rackButtons[9].setActionCommand("Pass");

        rackButtons[10] = new JButton("Undo");
        rackButton.add(rackButtons[10]);
        rackButtons[10].setActionCommand("Undo");

        rackButtons[11] = new JButton("Redo");
        rackButton.add(rackButtons[11]);
        rackButtons[11].setActionCommand("Redo");

        rackButtons[12] = new JButton("Save");
        rackButton.add(rackButtons[12]);
        rackButtons[12].setActionCommand("Save");

        rackButtons[13] = new JButton("Load");
        rackButton.add(rackButtons[13]);
        rackButtons[13].setActionCommand("Load");

        for(int i = 0; i < 14; i++){
            rackButtons[i].addActionListener(controller);
        }

        game.gameStarted();
        int customBoard = JOptionPane.showConfirmDialog(null, "Load a custom board?", "Custom configuration", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null);
        if(customBoard == 0){
            game.customBoard();
        }
        JOptionPane.showMessageDialog(rootPane, "Place first letter in the middle of the board", "Scrabble", JOptionPane.PLAIN_MESSAGE);
    }

    public void newPlayer(Rack rack, int score){
        int i = 0;
        for(Tile t: rack.getRack()){
            rackButtons[i].setText(t.toString());
            rackButtons[i].setActionCommand(t.toString());
            i++;
        }
        rackButtons[8].setText("Score: " + score);
    }

    @Override
    public void letterPlaced(char c) {
        for(JButton b: rackButtons){
            if(b.getText().contains(""+c)){
                b.setText(" ");
                b.setActionCommand(" ");
                break;
            }
        }
    }

    public void tilePlaced(int row, int column, char c) {
        buttons[row][column].setText(c + "");
        buttons[row][column].setActionCommand(" ");
    }

    public void scoreUpdated(int score){
        rackButtons[8].setText("Score: " + score);
    }

    public void removeTile(int row, int column){
        buttons[row][column].setText(" ");
        buttons[row][column].setActionCommand(row + "." + column);
    }

    public void updateRack(Rack rack){
        int i = 0;
        for(Tile t: rack.getRack()){
            rackButtons[i].setText(t.toString());
            rackButtons[i].setActionCommand(t.toString());
            i++;
        }
        if(i < 7){
            rackButtons[i].setText(" ");
            rackButtons[i].setActionCommand(" ");
        }
    }

    public String getFileName(){
        String fileName = JOptionPane.showInputDialog(null,
                "Please enter the file name: ", "Save game", JOptionPane.PLAIN_MESSAGE);
        return fileName;
    }

    public void setDoubleAndTriples(VersionedBoard board){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(board.returnScore(i, j) == 2){
                    buttons[i][j].setText("DL");
                }
                else if(board.returnScore(i, j) == 3){
                    buttons[i][j].setText("TL");
                }
                else if(board.doubleWord(i, j)){
                    buttons[i][j].setText("DW");
                }
                else if(board.tripleWord(i, j)){
                    buttons[i][j].setText("TW");
                }
                else if(board.returnScore(i,j) == 1 && board.getTile(i,j) == null){
                    buttons[i][j].setText(" ");
                }
            }
        }
    }

    public void updateView(Game game){
        VersionedBoard newBoard = game.returnBoard();
        this.game = game;
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(newBoard.getTile(i,j) == null){
                    this.buttons[i][j].setText(" ");
                    this.buttons[i][j].setActionCommand(i + "." + j);
                    this.buttons[i][j].addActionListener(controller);
                }
                else{
                    this.buttons[i][j].setText(""+newBoard.getTile(i,j).getCharacter());
                    this.buttons[i][j].setActionCommand(" ");
                }
            }
        }
        this.updateRack(game.getCurrentPlayer().getRack());
        this.scoreUpdated(game.getCurrentPlayer().getScore());
        this.setDoubleAndTriples(game.returnBoard());
        game.addView(this);
    }

    public static void main(String[] args) throws IOException {
        new ScrabbleView();
    }

}
