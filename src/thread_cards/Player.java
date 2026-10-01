public class Player {
    
    private static int playerCount;
    private int playerNumber;
    private Card[] hand = new Card[4];
    private String playerName;

    Player () {
        this.playerNumber = ++playerCount;
    }

    public int getPlayerNumber() {
        return this.playerNumber;
    }

    public String getPlayerName() {
        return "player" + this.playerNumber;
    }

}
