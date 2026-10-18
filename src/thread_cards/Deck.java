import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Deck {
    
    
    private static int deckCount = 0;

    private ArrayList<Card> deck;
    private int deckNumber;

    private File logFile;

    public Deck(File logFile) {
        this.deck = new ArrayList<>();
        this.deckNumber = ++deckCount;

        this.logFile = logFile;
    }

    public synchronized void addCard(Card c) {
        this.deck.add(c);
    }

    public synchronized Card removeCard() {
        Card card = this.deck.remove(0);
        return card;
    }

    public int getDeckNumber() {
        return this.deckNumber;
    }
    public String getDeckName() {
        return "Deck" + this.deckNumber;
    }

    public ArrayList<Card> getDeck() {
        return this.deck;
    }

    public String getDeckAsString() {
        String s = "";
        for (Card c: this.deck) {
            s += c.getCardValue() + " ";
        }
        if (s.length() > 0) {
            s = s.substring(0, s.length()-1);
        }
        return s;
    }

    public boolean isReady() {
        return !deck.isEmpty();
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

    public void endGame() {
        String m = this.getDeckName() + " contents: " + this.getDeckAsString();
        this.logMessage(m);
    }

    // public Card swapCard(Card c) {
    //     synchronized (deck){
    //         deck.add(c);
    //     }
    // }
}
