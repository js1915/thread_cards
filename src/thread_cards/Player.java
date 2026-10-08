package src.thread_cards;

import java.util.ArrayList;

public class Player {
    
    private static int playerCount;
    private int playerNumber;
    public ArrayList<Card> hand;

    public Player () {
        this.playerNumber = ++playerCount;
        this.hand = new ArrayList<>();
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

    public synchronized void receiveCard(Card c) {
        // TODO: Invariants - Make sure there is space in hand
        hand.add(c);
    }
    
    public synchronized Card disCard() {
        // TODO: Algorithm to choose card
        return hand.remove(0);
    }

     

}
