import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PlayerTest {

    private static Player p;
    private static Deck d1;
    private static Deck d2;
    private static File f1;
    private static File f2;

    @Before
    public void setUpClass() throws Exception {

        f1 = new File("deck1Output.txt");
        f2 = new File("deck2Output.txt");

        d1 = new Deck(f1);
        d2 = new Deck(f2);

        File f3 = new File("playerOutput.txt");
        p = new Player(d1, d2, f3);
    }

    @After
    public void tearDownClass() {
        Player.resetPlayers(0);
    }


    @Test
    public void testGetPlayerCount() {
        System.out.println("testGetPlayerCount");

        assertEquals(1, Player.getPlayerCount());
        System.out.println("    ✅ Passed 1/2: Player count is 1");

        Player newP = new Player(d1,d2, f1);
        // Now there are two players
        assertEquals(2, Player.getPlayerCount());
        System.out.println("    ✅ Passed 2/2: Player count becomes 2 after a player is instantiated");

        System.out.println("    ✅ All tests passed");
    }

    @Test
    public void testGetPlayerNumber() {
        System.out.println("testGetPlayerNumber:");

        assertEquals(1, p.getPlayerNumber());
        System.out.println("    ✅ Passed 1/2: First player has player number 1");

        Player newP = new Player(d1, d2, f1);
        assertEquals(2, newP.getPlayerNumber());
        System.out.println("    ✅ Passed 2/2: Second player has player number 2");
        
        System.out.println("    ✅ All tests passed");

    }

    @Test
    public void testGetPlayerName() {
        System.out.println("testGetPlayerName:");

        assertEquals("player1", p.getPlayerName());
        System.out.println("    ✅ Passed 1/2: First player has name 'player1'");

        Player newP = new Player(d1, d2, f1);
        assertEquals("player2", newP.getPlayerName());
        System.out.println("    ✅ Passed 2/2: Second player has name 'player2'");

        System.out.println("    ✅ All tests passed");
    }

    @Test 
    public void testGetHandAsString() {
        System.out.println("testGetHandAsString");

        assertEquals(0, p.getHandAsString().length());
        System.out.println("    ✅ Passed 1/3: Empty hand is shown");

        p.receiveCard(new Card(1));
        assertEquals("1", p.getHandAsString());
        System.out.println("    ✅ Passed 2/3: Hand is shown with one card");

        p.receiveCard(new Card(2));
        p.receiveCard(new Card(3));
        p.receiveCard(new Card(4));
        assertEquals("1 2 3 4", p.getHandAsString());
        System.out.println("    ✅ Passed 3/3: Hand is shown with multiple cards in the correct order");

        System.out.println("    ✅ All tests passed");
    }

    @Test
    public void testRecieveCard() {
        System.out.println("testRecieveCard:");

        assertEquals(new ArrayList<Card>(), p.getHand());
        System.out.println("    ✅ Passed 1/4: Hand is initially empty");

        Card c1 = new Card(1);
        Card c2 = new Card(2);
        Card c3 = new Card(3);
        Card c4 = new Card(4);

        ArrayList<Card> expectedHand = new ArrayList<>();

        p.receiveCard(c1);
        expectedHand.add(c1);
        assertEquals(expectedHand, p.getHand());
        System.out.println("    ✅ Passed 2/4: Hand correctly recieves a card");

        p.receiveCard(c2);
        p.receiveCard(c3);
        p.receiveCard(c4);

        expectedHand.add(c2);
        expectedHand.add(c3);
        expectedHand.add(c4);
        assertEquals(expectedHand, p.getHand());
        System.out.println("    ✅ Passed 3/4: Hand correctly recieves all cards and maintains correct order");
        
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {p.receiveCard(new Card(100));});
        System.out.println("    ✅ Passed 4/4: Hand cannot recieve more than 4 cards");

        System.out.println("    ✅ All tests passed");
    }

    @Test
    public void testDrawDiscard() {
        System.out.println("testDrawDiscard:");

        boolean handCountFlag = false;

        Card preferred1 = new Card(1); // This will be the preferred card so should stay
        Card c2 = new Card(2);
        Card c3 = new Card(3);
        Card c4 = new Card(4);

        Card preferred2 = new Card(1);
        Card preferred3 = new Card(1);
        Card preferred4 = new Card(1);

        d1.addCard(preferred2);
        d1.addCard(preferred3);
        d1.addCard(preferred4);

        p.receiveCard(preferred1);
        p.receiveCard(c2);
        p.receiveCard(c3);
        p.receiveCard(c4);
        
        p.drawDiscard();
        if (p.getHand().size() != 4) {
            handCountFlag = true;
        }
        Card discardedCard = d2.getDeck().getLast();
        assertNotEquals(discardedCard, preferred1);
        System.out.println("    ✅ Passed 1/4: Preferred card not discarded");

        // Draw discard 3 more times
        for (int i=0; i<2; i++) {
            p.drawDiscard();
            if (p.getHand().size() != 4) {
                handCountFlag = true;
            }
        }

        // Check that the discard deck does not contain a preferred card that should be held
        boolean flag = false;
        for (Card c: d2.getDeck()) {
            if ((c == preferred1) || (c == preferred2) || (c == preferred3) || (c == preferred4)) {
                flag = true;
            }
        }
        assertFalse(flag);
        System.out.println("    ✅ Passed 2/4: Discard deck does not contain a preferred card");

        ArrayList<Card> expectedFinalHand = new ArrayList<>();
        expectedFinalHand.add(preferred1);
        expectedFinalHand.add(preferred2);
        expectedFinalHand.add(preferred3);
        expectedFinalHand.add(preferred4);

        assertEquals(expectedFinalHand, p.getHand());
        System.out.println("    ✅ Passed 3/4: Final hand consists of all preferred cards");

        assertFalse(handCountFlag);
        System.out.println("    ✅ Passed 4/4: Hand always contains 4 cards");

        System.out.println("    ✅ All tests passed");
    }

    @Test
    public void testWinDetection() {
        System.out.println("testWinDetection");

        Card c1 = new Card(1);
        Card c2 = new Card(1);
        Card c3 = new Card(1);
        Card c4 = new Card(1);

        Card c5 = new Card(2);

        p.receiveCard(c1);
        p.receiveCard(c2);
        p.receiveCard(c3);
        p.receiveCard(c5);

        d1.addCard(c4);

        assertFalse(p.isWinningHand());
        System.out.println("    ✅ Passed 1/2: Non-winning hand does not trigger win detection");

        p.drawDiscard();
        assertTrue(p.isWinningHand());
        System.out.println("    ✅ Passed 2/2: Winning hand triggers win detection");

        System.out.println("    ✅ All tests passed");
    }

    @Test 
    public void testTurnsActions() {
        System.out.println("testTurnsActions:");
        // This will not simulate an actual game being played
        // It will only test that the player holds on to and discards the correct cards
        // and that cards move correctly through the player
        
        // Test scenario in which the pack contains a potential win
        ArrayList<Card> testCards = new ArrayList<>(); // 1,2,3,4,1,5,6,7,8,9,1,10,11,12,13,14,1,15,16,17,18,19,1,20
        ArrayList<Card> expectedDrawDeck = new ArrayList<>();
        ArrayList<Card> expectedDiscardDeck = new ArrayList<>();
        
        // Generate a testing pack with numbers 1-20 and 5 preferred cards
        for (int i=1;i<21;i++) {
            // Add the preferred card throughout the test deck
            if (i%5==0) {
                testCards.add(new Card(1));
            }
            testCards.add(new Card(i));
        }

        for (Card c: testCards) {
            d1.addCard(c);
        }
        
        // Sert up the expected draw deck after the player has finished its turns
        for (int i=17;i<24;i++) {
            expectedDrawDeck.add(testCards.get(i));
        }
        

        // Set up the expected discard deck after the player has finished its turns
        for (int i=0;i<17;i++) {
            if (testCards.get(i).getCardValue() != 1) {
                expectedDiscardDeck.add(testCards.get(i));
            }
        }

        // Initial hand of all non-preferred cards
        Card s1 = new Card(50);
        Card s2 = new Card(51);
        Card s3 = new Card(52);
        Card s4 = new Card(53);

        p.receiveCard(s1);
        p.receiveCard(s2);
        p.receiveCard(s3);
        p.receiveCard(s4);

        p.turns();

        ArrayList<Card> actualDrawDeck = d1.getDeck();

        
        expectedDiscardDeck.add(s1);
        expectedDiscardDeck.add(s2);
        expectedDiscardDeck.add(s3);
        expectedDiscardDeck.add(s4);
        expectedDiscardDeck.sort(Comparator.comparing(Card::getCardValue));

        ArrayList<Card> actualDiscardDeck = d2.getDeck();

        // Required as cards may enter the discard deck in a random order
        actualDiscardDeck.sort(Comparator.comparing(Card::getCardValue));

        assertEquals("1 1 1 1", p.getHandAsString());
        System.out.println("    ✅ Passed 1/___: Final hand is a winning hand");

        assertEquals(expectedDrawDeck, d1.getDeck());
        System.out.println("    ✅ Passed 2/___: Draw deck contains exactly the expected cards (correctly ordered)");

        assertEquals(expectedDiscardDeck, actualDiscardDeck);
        System.out.println("    ✅ Passed 3/___: Discard deck contains exactly the expected cards");


    }
}
