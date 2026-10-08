package src.thread_cards;

import java.util.ArrayList;

public class Deck {
    
    public ArrayList<Card> deck;

    public Deck() {
        this.deck = new ArrayList<>();
    }

    public synchronized void addCard(Card c) {
        this.deck.add(c);
    }

    public synchronized Card removeCard() {
        Card card = this.deck.remove(0);
        return card;
    }

    // public Card swapCard(Card c) {
    //     synchronized (deck){
    //         deck.add(c);
    //     }
    // }
}
