import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IntegrationTest {
    @Before 
    public void setUpClass() {
        
    }

    @After 
    public void tearDownClass() {

    }

    @Test 
    public void testTest() {
        System.out.println("IntegrationTest working");
    }

    @Test
    public void testCardConservation() {
        System.out.println("testCardConservation:");
        //
        ArrayList<Card> cards = new ArrayList<>();

        for (int i=1; i<=12; i++) {

            // Create a list of cards [1..50]
            if (i > 3 &&  i< 8) {
                cards.add(new Card(1));
            } else {
                cards.add(new Card(i));
            }
        }

        File f = new File("_.txt");

        Deck d1 = new Deck(f);
        Deck d2 = new Deck(f);

        Player p1 = new Player(d1, d2, f);

        // Deal initial cards
        for (int i=0; i<4; i++) {
            p1.receiveCard(cards.get(i));
     
            d1.addCard(cards.get(i+4));
            d2.addCard(cards.get(i+8));
        }
        // Check initial condions
        ArrayList<Card> initalList = new ArrayList<>(p1.getHand());
        initalList.addAll(d1.getDeck());
        initalList.addAll(d2.getDeck());
        initalList.sort(Comparator.comparing(Card::getCardValue));
        cards.sort(Comparator.comparing(Card::getCardValue));

        
        assertEquals(cards, initalList);
        System.out.println("    ✅ Passed 1/3: Deal methods conserve cards");

        p1.drawDiscard();
        
        ArrayList<Card> firstPassList = new ArrayList<>(p1.getHand());
        firstPassList.addAll(d1.getDeck());
        firstPassList.addAll(d2.getDeck());
        firstPassList.sort(Comparator.comparing(Card::getCardValue));
        
        assertEquals(cards, firstPassList);
        System.out.println("    ✅ Passed 2/3: Player draw/discard conserves cards");

        p1.turns();
        
        ArrayList<Card> afterTurnsList = new ArrayList<>(p1.getHand());
        afterTurnsList.addAll(d1.getDeck());
        afterTurnsList.addAll(d2.getDeck());
        afterTurnsList.sort(Comparator.comparing(Card::getCardValue));
        
        assertEquals(cards, afterTurnsList);
        System.out.println("    ✅ Passed 3/3: Cards are conserved after turns and win");

        System.out.println("    ✅ All tests passed");
    }

    @Test
    public void testWinDetection() {
        File f = new File("_.txt");
        ArrayList<Card> cards = new ArrayList<>();
        
        for (int i=0; i<16; i++) {
            if (i > 12) {
                cards.add(new Card(1));
            } else {
                cards.add(new Card(i + 1));
            }
            
        }

        Deck d1 = new Deck(f);
        Deck d2 = new Deck(f);

        Player p1 = new Player(d1, d2, f);
        Player p2 = new Player(d2, d1, f);

        // Deal players and decks
        for (int i=0; i<4; i++) {
            p1.receiveCard(cards.get(i));
            p2.receiveCard(cards.get(i+4));
            d1.addCard(cards.get(i+8));
            d2.addCard(cards.get(i+12));
        }


        Thread p1T = new Thread(p1);
        Thread p2T = new Thread(p2);

        p1T.start();
        p2T.start();
        try {
            p1T.join();
        } catch (InterruptedException e) {}
        

        String p1HandBefore;
        String p2HandBefore;
        String d1DeckBefore;
        String d2DeckBefore;

        // Check game state
        synchronized (d1) { // Stop player 2 from performing actions
            p1HandBefore = p1.getHandAsString();
            p2HandBefore = p2.getHandAsString();
            d1DeckBefore = d1.getDeckAsString();
            d2DeckBefore = d2.getDeckAsString();
            
        }
        try {
            p2T.join();
        } catch (InterruptedException e) {}

        // Check game state again
        String p1HandAfter = p1.getHandAsString();
        String p2HandAfter = p2.getHandAsString();
        String d1DeckAfter = d1.getDeckAsString();
        String d2DeckAfter = d2.getDeckAsString();
        
        assertEquals(p1HandBefore, p1HandAfter);
        assertEquals(p2HandBefore, p2HandAfter);
        assertEquals(d1DeckBefore, d1DeckAfter);
        assertEquals(d2DeckBefore, d2DeckAfter);
        System.out.println("    ✅ Passed 1/1: Player 2 immediately exits and does not perform any more actions");

        System.out.println("    ✅ All tests passed");

    }
}