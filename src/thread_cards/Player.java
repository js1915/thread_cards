package src.thread_cards;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Player implements Runnable{
    
    private static int playerCount;
    private int playerNumber;
    public ArrayList<Card> hand;

    private Deck drawDeck;
    private Deck discardDeck;
    
    // TODO: Make an integer flag that has the winning player's number so that all threads KNOW
    private static volatile boolean playerWon = false;

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
        hand.add(c);
    }

    public void drawCard() throws InterruptedException {
        // TODO: Invariants - Make sure there is space in hand
        System.out.println(this.getPlayerName() + " drew a card");
        System.out.println("-------Start Of Deck" + drawDeck.getDeckNumber());
        // for (Card c: drawDeck.deck) {
        //     System.out.println(c.getCardValue());
        // }
        System.out.println("-------End of Deck");
        
        Card cardReceived = drawDeck.removeCard();
        this.receiveCard(cardReceived);       
    }
    
    public void disCard(int id) {
        // TODO: Algorithm to choose card
        Card cardToDiscard = hand.remove(id);
        System.out.println(this.getPlayerName() + " discarded a card");
        System.out.println("Discarding card " + cardToDiscard.getCardValue());
        System.out.println("-------Start Of Deck" + discardDeck.getDeckNumber());
        // for (Card c: discardDeck.deck) {
        //     System.out.println(c.getCardValue());
        // }
        System.out.println("-------End of Deck");
        discardDeck.addCard(cardToDiscard);
        System.out.println("-------Start Of Deck" + discardDeck.getDeckNumber());
        // for (Card c: discardDeck.deck) {
        //     System.out.println(c.getCardValue());
        // }
        System.out.println("-------End of Deck");
    }

    @Override
    public void run() {
        try {
            this.turns();
        } catch (Exception e) {
            synchronized (System.out) {
                e.printStackTrace();
            }

            System.out.println("Exiting " + getPlayerName());
            playerWon = true;
            System.exit(0);

        }
        
    }

    public void turns() throws InterruptedException {
        while (!playerWon) {
            System.out.println("Running while loop for: " + this.getPlayerNumber());
            synchronized (System.out) {
                System.out.println(getPlayerName() + "'s hand");
                for (Card c: this.hand) {
                    System.out.println(c.getCardValue());
            }
            }   
            if (this.isWinningHand()) {
                // TODO: Notify all other threads
                playerWon = true;
                System.out.println("WE WIN!!!!! THREAD " + this.getPlayerName());
                // Initialise shutdown for all threads (logging file dumps)
                // Thread.currentThread().interrupt();
            } else if (drawDeck.isReady()) {
                    int indexToDiscard = getIndexToDiscard();
                    disCard(indexToDiscard);
                    drawCard();
                }
            }
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
