import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CardGame {

    public static void main(String[] args) {
        int playerCount;
        ArrayList<Card> pack;
        
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Deck> decks = new ArrayList<>();
        ArrayList<Thread> playerThreads = new ArrayList<>();

        // Read user input
        Scanner scn = new Scanner(System.in);

        //Read and validate player count
        System.out.println("Enter the number of players: ");
        playerCount = getPlayerCount(scn.nextLine());

        //Read and validate pack file
        System.out.println("Enter the pack file path: ");
        pack = getPack(scn.nextLine(), playerCount);

        scn.close();

        // Make requried supporting directories
        try {
            // Clean directories
            String fPath = new File("").getAbsolutePath();
            boolean _ = new File(fPath.concat("/log_files")).mkdirs();
        } catch (Exception e) {
            System.out.println("Unable to make required directories: check permissions");
            System.exit(0);
        }
        
        
        // Create Decks
        for (int i=0; i < playerCount; i++) {
            String fPath = new File("").getAbsolutePath();
            File f = new File(fPath.concat("/log_files/deck" + (i+1) + "_output.txt"));
            decks.add(new Deck(f));            
        }

        // Create players with deck references
        for (int i=0; i < playerCount; i++) {
            Deck drawDeck = decks.get(i);
            Deck discardDeck = decks.get((i+1) % (playerCount)); // Allows ring topology wrapping

            // Logging file setup
            String fPath = new File("").getAbsolutePath();
            File f = new File(fPath.concat("/log_files/player" + (i+1) + "_output.txt"));
            players.add(new Player(drawDeck, discardDeck, f));            
        }

        // Deal cards to players
        for (int i=0; i<playerCount; i++) {
            for (Player p : players) {
                Card cardToGive = pack.remove(0);
                p.receiveCard(cardToGive);
            }
        }
        
        // Deal cards to decks
        for (int i=0; i<playerCount; i++) {
            for (Deck d : decks) {
                Card cardToGive = pack.remove(0);
                d.addCard(cardToGive);
            }
        }
        // Start Player Threads
        for (Player p: players) {
            Thread thrd = new Thread(p);
            playerThreads.add(thrd);
        }

        for (Thread t: playerThreads) {
            t.start();
        }

        try {
            for (Thread t: playerThreads) {
                t.join();
            }
        } catch (InterruptedException e) { 
            e.printStackTrace();
        }

        // Game end logic
        for (Deck d: decks) {
            d.endGame();
        }
    }


    public static int getPlayerCount(String p) {
        int playerCount = 0;

        try {
            playerCount = Integer.parseInt(p);
        } catch (NumberFormatException e) {
            System.out.println("Must be a number");
            System.exit(-1);
        }

        // Can we have a 1 player game?
        if (playerCount <= 1) {
            throw new IllegalArgumentException("Must be a positive integer");
        }
        return playerCount;
    }

    public static ArrayList<Card> getPack(String fpath, int playerCount) {
        File fileObj = new File(fpath);
        ArrayList<Card> pack = new ArrayList<>(); 
        int cardValue = 0;

        try (Scanner fscn = new Scanner(fileObj)) {
            while (fscn.hasNextLine()) {
                try {
                    cardValue = Integer.parseInt(fscn.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid line in file");
                }
                

                if (cardValue <= 0) {
                    throw new IllegalArgumentException("Invalid line in file");
                }

                pack.add(new Card(cardValue));
            }
            fscn.close();

            if (pack.size() != (8*playerCount)) {
                throw new IllegalArgumentException("Invalid pack size");
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.out.println("Please check that the corect file path was supplied");
            System.exit(0);
        }

        return pack;
    }
}
