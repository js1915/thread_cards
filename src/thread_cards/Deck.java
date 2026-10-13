package src.thread_cards;

import java.util.ArrayList;

public class Deck {
    
    
    private static int deckCount = 0;

    public ArrayList<Card> deck;
    private int deckNumber;

    public Deck() {
        this.deck = new ArrayList<>();
        this.deckNumber = ++deckCount;
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

    // public Card swapCard(Card c) {
    //     synchronized (deck){
    //         deck.add(c);
    //     }
    // }
}
