package src.thread_cards;

import java.util.ArrayList;

public class Player implements Runnable{
    
    private static int playerCount;
    private int playerNumber;
    public ArrayList<Card> hand;

    public Deck drawDeck;
    public Deck discardDeck;

    public Player (Deck drawDeck, Deck discardDeck) {
        this.playerNumber = ++playerCount;
        this.hand = new ArrayList<>();

        this.drawDeck = drawDeck;
        this.discardDeck = discardDeck;
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

    @Override 
    public void run() {
        try {
            this.turn();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }

    public void turn() throws InterruptedException {
        System.out.println("This is " + this.getPlayerName());
        System.out.println("    Draw deck: " + this.drawDeck.getDeckName());
        System.out.println("    Discard deck: " + this.discardDeck.getDeckName());
    }

}
