import java.io.Serializable;
import java.util.Random;
import java.util.HashMap;

public class Bag implements Serializable {
    private HashMap<Tile, Integer> tileCounts;
    private Random randomTile;

    public Bag() {
        tileCounts = new HashMap<>();
        randomTile = new Random();
        this.initializeBag();
    }

    private void initializeBag(){

        tileCounts.put(new Tile('E', 1), 12);
        tileCounts.put(new Tile('A', 1), 9);
        tileCounts.put(new Tile('I', 1), 9);
        tileCounts.put(new Tile('O', 1), 8);
        tileCounts.put(new Tile('N', 1), 6);
        tileCounts.put(new Tile('R', 1), 6);
        tileCounts.put(new Tile('T', 1), 6);
        tileCounts.put(new Tile('L', 1), 4);
        tileCounts.put(new Tile('S', 1), 4);
        tileCounts.put(new Tile('U', 1), 4);

        tileCounts.put(new Tile('D', 2), 4);
        tileCounts.put(new Tile('G', 2), 3);

        tileCounts.put(new Tile('B', 3), 2);
        tileCounts.put(new Tile('C', 3), 2);
        tileCounts.put(new Tile('M', 3), 2);
        tileCounts.put(new Tile('P', 3), 2);

        tileCounts.put(new Tile('F', 4), 2);
        tileCounts.put(new Tile('H', 4), 2);
        tileCounts.put(new Tile('V', 4), 2);
        tileCounts.put(new Tile('W', 4), 2);
        tileCounts.put(new Tile('Y', 4), 2);

        tileCounts.put(new Tile('K', 5), 1);

        tileCounts.put(new Tile('J', 8), 1);
        tileCounts.put(new Tile('X', 8), 1);

        tileCounts.put(new Tile('Q', 10), 1);
        tileCounts.put(new Tile('Z', 10), 1);
    }

    public Tile getRandomTile(){
        int totalTiles = 0;
        int randomIndex;
        int runningSum = 0;

        for(int numEach: tileCounts.values()){
            totalTiles += numEach;
        }

        if(totalTiles == 0){
            return null;
        }
        else{
            randomIndex = randomTile.nextInt(totalTiles);
            for(HashMap.Entry<Tile, Integer> entry: tileCounts.entrySet()){
                runningSum += entry.getValue();
                if(randomIndex < runningSum){
                    Tile returningTile = entry.getKey();
                    tileCounts.put(returningTile, entry.getValue() - 1);
                    return returningTile;
                }
            }
        }
        return null;
    }

    public void returnTile(Tile tile){
        tileCounts.put(tile, tileCounts.get(tile) + 1);
    }

    public Tile swapTile(Tile tile){
        tileCounts.put(tile, tileCounts.get(tile) + 1);
        return this.getRandomTile();
    }

    public int getTileScore(char c){
        for(HashMap.Entry<Tile, Integer> entry: tileCounts.entrySet()){
            if(entry.getKey().getCharacter() == c){
                return entry.getKey().getScore();
            }
        }
        return 0;
    }

    public boolean bagEmpty(){
        int sum = 0;
        for(HashMap.Entry<Tile, Integer> entry: tileCounts.entrySet()){
            sum += entry.getValue();
        }
        if(sum == 0){
            return true;
        }
        return false;
    }

}
