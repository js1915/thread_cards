package src.thread_cards;

import java.util.Scanner;

public class CardGame {
    public static void main(String[] args) {
        Card[] pack;
        int playerCount = getPlayers();
        // TODO: Get pack file path (validate)
        //          8n rows, no negatives
        // TODO: Generate Cards and ...
        // TODO: Add to an array of cards
        // TODO: Deal player hands (4 each)
        // TODO: Deal decks (n each)
        // TODO: Start Player Threads
        
    }

    public static int getPlayers() {
        Scanner scn = new Scanner(System.in);
        System.out.println("Enter the number of players:");

        int playerCount = 0;
        boolean validNumber = false;

        while (!validNumber){
            try {
                playerCount = Integer.parseInt(scn.nextLine());
                
                if (playerCount <= 1) {
                    throw new IllegalArgumentException("Must be a positive integer");
                    
                }
                scn.close();
                validNumber = true;
            } catch (NumberFormatException _) {
                System.out.println("Not a valid integer. Try again:");
            }
            catch (IllegalArgumentException _) {
                System.out.println("Player count must be greater than 1. Try again:");
            }
        }
        return playerCount;
    }
}
