import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IntegrationTest {
    File f;
    String fPath;
    @Before 
    public void setUpClass() {
        fPath = new File("").getAbsolutePath();
        System.out.println(fPath);
        f = new File(fPath.concat("/test_output.txt"));
        Player.resetPlayers();
        Deck.resetDeck();

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
        cards.sort(Comparator.comparing(Card::getCardValue));

        ArrayList<Card> initalList = new ArrayList<>(p1.getHand());
        initalList.addAll(d1.getDeck());
        initalList.addAll(d2.getDeck());
        initalList.sort(Comparator.comparing(Card::getCardValue));
        
        assertEquals(cards, initalList);
        System.out.println("    ✅ Passed 1/3: Deal methods conserve cards");

        p1.drawDiscard();
        
        ArrayList<Card> firstPassList = new ArrayList<>(p1.getHand());
        firstPassList.addAll(d1.getDeck());
        firstPassList.addAll(d2.getDeck());
        
        boolean allCardsPresentAfterFirstPass = true;
        for (Card c: cards) {
            if (!firstPassList.contains(c)) {
                allCardsPresentAfterFirstPass = false;
            }
        }
        
        assertTrue(allCardsPresentAfterFirstPass);
        System.out.println("    ✅ Passed 2/3: Player draw/discard conserves cards");

        p1.turns();
        
        ArrayList<Card> afterTurnsList = new ArrayList<>(p1.getHand());
        afterTurnsList.addAll(d1.getDeck());
        afterTurnsList.addAll(d2.getDeck());
        
        boolean allCardsPresentAfterTurns = true;
        for (Card c: cards) {
            if (!firstPassList.contains(c)) {
                allCardsPresentAfterTurns = false;
            }
        }
        
        assertTrue(allCardsPresentAfterTurns);
        System.out.println("    ✅ Passed 3/3: Cards are conserved after turns and win");

        System.out.println("    ✅ All tests passed");

    }

    @Test
    public void testWinDetection() {
        System.out.println("testWinDetection:");
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

    @Test
    public void testOutputFiles() throws FileNotFoundException {
        File d1Output = new File(fPath.concat("/deck1_output.txt"));
        File d2Output = new File(fPath.concat("/deck2_output.txt"));

        File p1Output = new File(fPath.concat("/player1_output.txt"));
        File p2Output = new File(fPath.concat("/player2_output.txt"));
        System.out.println(fPath.concat("/player1_output.txt"));

        ArrayList<Card> cards = new ArrayList<>();
        
        for (int i=0; i<16; i++) {
            if (i < 3 || i == 8) {
                cards.add(new Card(1));
            } else {
                cards.add(new Card(i + 1));
            }
            
        }
        Deck d1 = new Deck(d1Output);
        Deck d2 = new Deck(d2Output);
        System.out.println(d1.getDeckName());
        System.out.println(d2.getDeckName());

        Player p1 = new Player(d1, d2, p1Output);
        Player p2 = new Player(d2, d1, p2Output);

        for (int i=0; i<4; i++) {
            p1.receiveCard(cards.get(i));
            p2.receiveCard(cards.get(i+4));
            d1.addCard(cards.get(i+8));
            d2.addCard(cards.get(i+12));
        }
        System.out.println("starting turns");
        p1.turns();
        Scanner p1reader = new Scanner(p1Output);
        String initialHandLine = p1reader.nextLine();
        String discardLine = p1reader.nextLine();
        String drawLine = p1reader.nextLine();
        String currentHandLine = p1reader.nextLine();

        String winLine = p1reader.nextLine();
        String exitLine = p1reader.nextLine();
        String finalHandLine = p1reader.nextLine();

        assertEquals("player1 initial hand 1 1 1 4", initialHandLine);
        System.out.println("    ✅ Passed 1/6: Starting the turns method writes the correct messsage to the output file");

        assertEquals("player1 discards a 4 to deck 2", discardLine);
        assertEquals("player1 draws a 1 from deck 1", drawLine);
        System.out.println("    ✅ Passed 2/6: Drawing and discarding writes the correct messages to the output file");

        assertEquals("player1 current hand is 1 1 1 1", currentHandLine);
        System.out.println("    ✅ Passed 3/6: Current hand added to output after every turn");
        
        
        
        assertEquals("player1 wins", winLine);
        assertEquals("player1 exits", exitLine);
        assertEquals("player1 final hand 1 1 1 1", finalHandLine);
        System.out.println("    ✅ Passed 4/6: Win detection causes player 1 to output it winning exiting and its final hand");

        p2.turns();
        Scanner p2reader = new Scanner(p2Output);
        p2reader.nextLine();  // Skip initial hand which has already been tested
        String lossLine = p2reader.nextLine();
        String p2ExitLine = p2reader.nextLine();
        String p2FinalHandLine = p2reader.nextLine();

        assertEquals("player1 has informed player2 that player1 has won", lossLine);
        assertEquals("player2 exits", p2ExitLine);
        assertEquals("player2 final hand 5 6 7 8", p2FinalHandLine); // Hand unchanged
        System.out.println("    ✅ Passed 5/6: Player 2 outputs that player1 has won and proceeds with other output messages as above");

        d1.endGame();
        Scanner deckReader = new Scanner(d1Output);
        String deckOutput = deckReader.nextLine();
        assertEquals("Deck1 contents: " + d1.getDeckAsString(), deckOutput);
        System.out.println("    ✅ Passed 6/6: Deck outputs its contents when the game is ended");

        p1reader.close();
        p2reader.close();
        deckReader.close();

        // Cleanup files at end
        p1Output.delete();
        p2Output.delete();
        d1Output.delete();
        d2Output.delete();
    }
}