import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;


public class PlayerTest {

    private static Player p;
    private static Deck d1;
    private static Deck d2;
    private static File f1;

    @Before
    public void setUpClass() throws Exception {

        f1 = new File("_.txt");
        File f2 = new File("playerOutput.txt");
        d1 = new Deck(f1);
        d2 = new Deck(f1);
        p = new Player(d1, d2, f2);
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

}
