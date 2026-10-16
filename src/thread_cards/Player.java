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
    private static volatile boolean playerWon = false;

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
        this.turns();
    }

    public void turns() {
        this.logMessage(this.getPlayerName() + " initial hand " + this.getHandAsString());
        while (!playerWon) {
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

    private void setupFile() {
        try {
            if (!this.logFile.exists()) {
                this.logFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        
        
    }

    private void logMessage(String m) {
        try {
            FileWriter w = new FileWriter(this.logFile);
            w.write(m + "\n");
            w.close();
        } catch (IOException e) {
            System.out.println("File writing failed!");
            e.printStackTrace();
            System.exit(0);
            return;
        }
        System.out.println("Wrote '" + m + "' to file");
    }

}
