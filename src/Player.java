import java.io.Serializable;

public class Player implements Serializable {
    private int playerID;
    private String playerName;
    private int score;
    private Rack rack;

    public Player(int playerID, String playerName) {
        this.playerID = playerID;
        this.playerName = playerName;
        this.rack = new Rack(playerID);
        this.score = 0;
    }

    public int getScore(){
        return this.score;
    }

    public void addToScore(int score){
        this.score += score;
    }

    public void setRack(Rack rack){
        this.rack = rack;
    }

    public int getPlayerID(){
        return this.playerID;
    }

    public String getPlayerName(){
        return this.playerName;
    }

    public Rack getRack(){
        return this.rack;
    }
}
