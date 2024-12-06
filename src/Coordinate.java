import java.io.Serializable;

public class Coordinate implements Comparable<Coordinate>, Serializable {


    int row;
    int col;

    public Coordinate(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String toString() {
        return "(" + row + ", " + col + ")";
    }

    public boolean equals(Coordinate o) {
        return this.getRow() == o.getRow() && this.getCol() == o.getCol();
    }

    public int compareTo(Coordinate o) {
        return this.getCol() - o.getCol() + this.getRow() - o.getRow();
    }
}
