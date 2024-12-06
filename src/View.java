public interface View {
    public void newPlayer(Rack rack, int score);
    public void letterPlaced(char c);
    public void tilePlaced(int row, int column, char c);
    public void scoreUpdated(int score);
    public void removeTile(int row, int column);
    public void updateRack(Rack rack);
    public String getFileName();
    public void updateView(Game game);
}
