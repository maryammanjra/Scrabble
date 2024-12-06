public class Move implements Comparable<Move> {
    private int row;
    private int col;
    private char c;

    public Move(int row, int col, char c){
        this.row = row;
        this.col = col;
        this.c = c;
    }

    public char getC(){
        return c;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int compareTo(Move m) {
        if(this.row == m.row){
            return this.col - m.col;
        }
        else{
            return this.row - m.row;
        }
    }

    public boolean equals(Move m){
        if(this.row == m.row && this.col == m.col){
            if(c == m.c){
                return true;
            }
        }
        return false;
    }

    public String toString(){
        return this.row + "," + this.col + "," + this.c;
    }

}
