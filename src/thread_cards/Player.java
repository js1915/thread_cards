package src.thread_cards;
public class Player {
    
    private static int playerCount;
    private int playerNumber;
    private Card[] hand = new Card[4];

    public Player () {
        this.playerNumber = ++playerCount;
    }

    public static int getPlayerCount() {
        return playerCount;
    }

    public int getPlayerNumber() {
        return this.playerNumber;
    }

    public String getPlayerName() {
        return "player" + this.playerNumber;
    }

}
