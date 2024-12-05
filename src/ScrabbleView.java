import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ScrabbleView extends JFrame implements View{
    JButton[][] buttons;
    Game game;
    Controller controller;
    JButton[] rackButtons;
    Color boardColour = new Color(192,64,43);


    public ScrabbleView() throws IOException {
        super("Scrabble");

        this.game = new Game(this);
        this.controller = new Controller(game, this);

        buttons = new JButton[15][15];
        rackButtons = new JButton[11];
        Container contentPane = this.getContentPane();
        contentPane.setBackground(boardColour);
        this.setSize(615, 500);
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

        for(int i = 0; i < 8; i++){
            rackButtons[i].addActionListener(controller);
        }

        rackButtons[8] = new JButton("Score: ");
        rackButton.add(rackButtons[8]);
        rackButtons[9] = new JButton("Pass");
        rackButton.add(rackButtons[9]);
        rackButtons[9].setActionCommand("Pass");
        rackButtons[9].addActionListener(controller);
        rackButtons[10] = new JButton("Undo");
        rackButton.add(rackButtons[10]);
        rackButtons[10].setActionCommand("Undo");

        game.gameStarted();
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

    public static void main(String[] args) throws IOException {
        new ScrabbleView();
    }

}
