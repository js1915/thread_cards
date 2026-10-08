package src.thread_cards;

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

        // Read user input
        Scanner scn = new Scanner(System.in);

        //Read and validate player count
        System.out.println("Enter the number of players: ");
        playerCount = getPlayerCount(scn.nextLine());

        //Read and validate pack file
        System.out.println("Enter the pack file path: ");
        pack = getPack(scn.nextLine(), playerCount);

        scn.close();

        //Debug print
        for (int i=0; i<pack.size(); i++) {
            System.out.println(pack.get(i).getCardValue());
        }


        for (int i=0; i < playerCount; i++) {
            // Create Players and decks
            players.add(new Player());
            decks.add(new Deck());
        }

        for (int i=0; i<playerCount; i++) {
            for (Player p : players) {
                Card cardToGive = pack.remove(0);
                p.receiveCard(cardToGive);
            }
        }

        //Debug print
        for (Player p : players) {
            System.out.println(p.getPlayerName());
            for (Card c : p.hand) {
                System.out.println("    " + c.getCardValue());
            }
        }
        
        
        for (int i=0; i<playerCount; i++) {
            for (Deck d : decks) {
                Card cardToGive = pack.remove(0);
                d.addCard(cardToGive);
            }
        }
        int i = 0;
        for (Deck d : decks) {

            System.out.println("Deck " + ++i);
            for (Card c : d.deck) {
                System.out.println("    " + c.getCardValue());
            }
        }
        for (Card c : pack) {
            System.out.println(c.getCardValue());
        }



        // TODO: Deal decks (n each)
        // TODO: Start Player Threads
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
        
        try (Scanner fscn = new Scanner(fileObj)) {
            while (fscn.hasNextLine()) {
                int cardValue = Integer.parseInt(fscn.nextLine());

                if (cardValue <= 0) {
                    throw new IllegalArgumentException("Invalid line in file");
                }

                pack.add(new Card(cardValue));
            }
            fscn.close();

            if (pack.size() != (8*playerCount)) {
                throw new IllegalArgumentException("Invalid pack size"); // TODO: Write custom exceptions
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }

        return pack;
    }
}
