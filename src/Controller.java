import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class Controller implements ActionListener, Serializable {
    Game game;
    View view;

    public Controller(Game game, View v) {
        this.game = game;
        this.view = v;
    }

    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().contains(",")){
            game.tileSelected(e.getActionCommand().toCharArray()[0]);
        }
        else if(e.getActionCommand().contains(".")){
            String[] position = e.getActionCommand().split("\\.");
            if(e.getActionCommand().toCharArray()[0] == '7'){
                if(e.getActionCommand().toCharArray()[2] == '7'){
                    game.firstTurn();
                }
                else{
                    game.anotherTurn(Integer.parseInt(position[0]), Integer.parseInt(position[1]));
                }
            }
            else{
                game.anotherTurn(Integer.parseInt(position[0]), Integer.parseInt(position[1]));
            }
        }
        else if(e.getActionCommand() == "Done"){
            game.playerSwitched();
        }
        else if(e.getActionCommand() == "Pass"){
            game.playerSwitched();
        }
        else if(e.getActionCommand() == "Undo"){
            game.undo();
        }
        else if(e.getActionCommand() == "Redo"){
            game.redo();
        }
        else if(e.getActionCommand() == "Save"){
            String fileName = view.getFileName();
            game.serializeGame(fileName);
        }
        else if(e.getActionCommand() == "Load"){
            String fileName = view.getFileName();
            Game gameOne = Game.deserializeGame(fileName);
            this.game = gameOne;
            view.updateView(gameOne);
        }
    }
}
