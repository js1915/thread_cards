import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class DeckTest {
    
    private static File f;
    private static Deck d;

    @Before 
    public void setUpClass() {
        f = new File("_.txt");
        d = new Deck(f);
    }

    @After
    public void tearDownClass() {
        Deck.resetDeck();
        d = new Deck(f);       
    }

    @Test 
    public void testAddCard() {
        System.out.println("testAddCard:");

        ArrayList<Card> expectedDeck = new ArrayList<>();
        
        Card c1 = new Card(1);
        d.addCard(c1);
        expectedDeck.add(c1);

        assertEquals(expectedDeck, d.getDeck());
        System.out.println("    ✅ Passed 1/2: Deck recieves one card correctly");

        for (int i=1;i<10;i++) {
            Card c = new Card(i);
            d.addCard(c);
            expectedDeck.add(c);
        }

        assertEquals(expectedDeck, d.getDeck());
        System.out.println("    ✅ Passed 2/2: Deck recieves multiple cards correctly and maintains given order");

        System.out.println("    ✅ All tests passed");
    }

    @Test 
    public void testRemoveCard() {
        System.out.println("testRemoveCard:");

        ArrayList<Card> expectedDeck = new ArrayList<>();

        // Add cards to deck
        for (int i=1;i<10;i++) {
            Card c = new Card(i);
            d.addCard(c);
            expectedDeck.add(c);
        }

        // Remove one card from deck
        Card firstDrawnCard = d.removeCard();
        
        assertEquals(firstDrawnCard, expectedDeck.get(0));
        System.out.println("    ✅ Passed 1/3: Deck selects the correct card to be drawn");

        expectedDeck.remove(0);
        assertEquals(expectedDeck, d.getDeck());
        System.out.println("    ✅ Passed 2/3: Card is removed from the deck correctly");

        for (int i=1;i<9;i++) {
            Card drawnCard = d.removeCard();
            expectedDeck.remove(0);
        }
        assertEquals(0, d.getDeck().size());
        System.out.println("    ✅ Passed 2/3: All cards removed from the deck correctly");

        System.out.println("    ✅ All tests passed");

    }

    @Test
    public void testFirstInFirstOutBehaviour() {
        System.out.println("testFirstInFirstOutBehaviour:");

        ArrayList<Card> beforeCards = new ArrayList<>();
        ArrayList<Card> afterCards = new ArrayList<>();

        // Add cards to deck
        for (int i=1;i<10;i++) {
            Card c = new Card(i);
            d.addCard(c);
            beforeCards.add(c);
        }

        boolean correctCardsFlag = true;
        for (int i=0;i<9;i++) {
            Card drawnCard = d.removeCard();
            if (drawnCard != beforeCards.get(i)) {
                correctCardsFlag = false;
            }
            afterCards.add(drawnCard);
        }
        assertTrue(correctCardsFlag);

        System.out.println("    ✅ Passed 1/2: All cards are drawn from the deck correctly");

        assertEquals(beforeCards, afterCards);
        System.out.println("    ✅ Passed 2/2: Card removal follows FIFO behaviour correctly (card order is preserved)");

        System.out.println("    ✅ All tests passed");
        

    }
}
