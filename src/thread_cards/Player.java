package src.thread_cards;

import java.util.ArrayList;
import java.util.Random;

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

    public void receiveCard(Card c) {
        // TODO: Invariants - Make sure there is space in hand
        hand.add(c);
    }
    
    public Card disCard() {
        // TODO: Algorithm to choose card
        return hand.remove(0);
    }

    @Override 
    public void run() {
        try {
            this.turns();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }

    public void turns() throws InterruptedException {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Running while loop for: " + this.getPlayerNumber());
            if (this.isWinningHand()) {
                // TODO: Notify all other threads
                // Initialise shutdown for all threads (logging file dumps)
                Thread.currentThread().interrupt();
            }
            // TODO: Choose a card to discard (non-preferred value must GO!)

            // TODO: Draw from deck
            // TODO: Discard to deck
        }
        System.out.println("WE WIN!!!!! THREAD " + this.getPlayerName());
    }

    private boolean isWinningHand() {
        boolean winningHand = true;
        int v = this.hand.get(0).getCardValue();
        for (Card c: this.hand) {
            if (c.getCardValue() != v) {
                winningHand = false;
            }
        }
        return winningHand;
    }

    // TODO: TEST THIS!!!
    private int getIndexToDiscard() {
        int discardIndex = -1;
        ArrayList<Integer> discardable = new ArrayList<>();

        Random randomNumberGen = new Random();

        for (int i=0;i<4;i++) {
            if (this.hand.get(i).getCardValue() != this.playerNumber) {
            discardable.add(i);
            }
        }  

        // Get a random discardable index from list of non-preferred card indexes.
        // Return the index of the card (in hand) to be discarded 
        discardIndex = discardable.get(randomNumberGen.nextInt(discardable.size())); 
        return discardIndex;
    }

}
