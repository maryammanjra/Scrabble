import com.sun.tools.javac.Main;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Rack implements Serializable {

    private ArrayList<Tile> rack;
    int playerID;

    public Rack(int playerID) {
        this.playerID = playerID;
        rack = new ArrayList<>();
    }

    public void initializeRack(Bag bag){
        for(int i = 0; i < 7; i++){
            rack.add(bag.getRandomTile());
        }
    }

    public void swapTile(Tile tile, Bag bag){
        rack.remove(tile);
        rack.add(bag.swapTile(tile));
    }

    public Tile playTile(char c){
        Tile removeTile = null;
        for(Tile tile: rack){
            if(tile.getCharacter() == c){
                removeTile = tile;
            }
        }
        rack.remove(removeTile);
        return removeTile;
    }

    public void addTile(Bag bag){
        rack.add(bag.getRandomTile());
    }

    public boolean rackEmpty(){
        if(rack.isEmpty()){
            return true;
        }
        return false;
    }

    public ArrayList<Tile> getRack(){
        return rack;
    }

    public void printRack(){
        for(Tile t: rack){
            System.out.println("Character:" + t.getCharacter() + " Score:" + t.getScore());
        }
    }

    public void returnToRack(Tile tile){
        rack.add(tile);
    }
}
