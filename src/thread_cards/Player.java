package src.thread_cards;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Player implements Runnable{
    
    private static int playerCount;
    private int playerNumber;
    public ArrayList<Card> hand;

    private Deck drawDeck;
    private Deck discardDeck;

    private File logFile;

    // TODO: Make an integer flag that has the winning player's number so that all threads KNOW
    private static volatile int playerWon = -1;

    public Player (Deck drawDeck, Deck discardDeck, File logFile) {
        this.playerNumber = ++playerCount;
        this.hand = new ArrayList<>();

        this.drawDeck = drawDeck;
        this.discardDeck = discardDeck;

        this.logFile = logFile;
        this.setupFile();
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

    public String getHandAsString() {
        String s = "";
        for (Card c: this.hand) {
            s += c.getCardValue() + " ";
        }
        s = s.substring(0,s.length() - 1); // Remove trailing space
        return s;
    }

    public void receiveCard(Card c) {
        hand.add(c);
    }

    public void drawCard() {
        // TODO: Invariants - Make sure there is space in hand
        Card cardReceived = this.drawDeck.removeCard();
        this.receiveCard(cardReceived);
        String m = this.getPlayerName() + " draws a " + cardReceived.getCardValue() + " from deck " + this.drawDeck.getDeckNumber();
        this.logMessage(m);     
    }
    
    public void disCard(int id) {
        // TODO: Algorithm to choose card
        Card cardToDiscard = hand.remove(id);
        discardDeck.addCard(cardToDiscard);
        String m = this.getPlayerName() + " discards a " + cardToDiscard.getCardValue() + " to deck " + this.discardDeck.getDeckNumber();
        this.logMessage(m);
    }

    @Override
    public void run() {
        this.turns();
    }

    public void turns() {
        // Log initial hand
        this.logMessage(this.getPlayerName() + " initial hand " + this.getHandAsString());

        while (playerWon==-1) {
            if (this.isWinningHand()) {
                // TODO: Notify all other threads
                playerWon = this.getPlayerNumber();
            } else if (drawDeck.isReady()) {
                    int indexToDiscard = getIndexToDiscard();
                    disCard(indexToDiscard);
                    drawCard();
                    this.logMessage(this.getPlayerName() + " current hand is " + this.getHandAsString());
                }
            }
        this.shutdownThread();
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

    private void setupFile() {
        try {
            if (this.logFile.exists()) {
                this.logFile.delete(); 
            }
            this.logFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void logMessage(String m) {
        try {
            FileWriter w = new FileWriter(this.logFile, true);
            w.write(m + "\n");
            w.close();
        } catch (IOException e) {
            System.out.println("File writing failed!");
            e.printStackTrace();
            System.exit(0);
            return;
        }
    }

    private void shutdownThread() {
        String m = "";
        if (playerWon == this.getPlayerNumber()) {
            m += this.getPlayerName() + " wins";
        } else {
            m += "player" + playerWon + " has informed " + this.getPlayerName() + " that player" + playerWon + " has won";
        }
        this.logMessage(m);
        this.logMessage(this.getPlayerName() + " exits");
        this.logMessage(this.getPlayerName() + " final hand " + this.getHandAsString());
    }

}
