import javax.swing.*;

/**
 * By Toby Thomas
 */

public class Cards {

    //Initialize global ints
    int suit, rank;

    //Default constructor
    public Cards () {
        this.suit = 0;
        this.rank = 0;
    }

    //Constructor with suit and rank parameters
    public Cards (int suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }

    //This print method prints a card
    public static void printCard (Cards c) {
        String[] suits = { "Clubs", "Diamonds", "Hearts", "Spades" };
        String[] ranks = { "narf", "Ace", "2", "3", "4", "5", "6",
                "7", "8", "9", "10", "Jack", "Queen", "King" };
        System.out.println (ranks[c.rank] + " of " + suits[c.suit]);
    }

    //This toString method creates a String of a card
    public static String cardToString (Cards c) {
        String cardString;
        String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
        String[] ranks = {"narf", "Ace", "2", "3", "4", "5", "6",
                "7", "8", "9", "10", "Jack", "Queen", "King"};
        return cardString = ranks[c.rank] + " of " + suits[c.suit];
    }

    //This static method compares two cards
    public static boolean sameCard (Cards c1, Cards c2) {
        return (c1.suit == c2.suit && c1.rank == c2.rank);
    }

    //This method compares two cards deeply
    public int compareCards (Cards c1, Cards c2){

        if (c1.suit > c2.suit) return 1;
        if (c1.suit < c2.suit) return -1;

        if (c1.rank > c2.rank) return 1;
        if (c1.rank < c2.rank) return -1;

        return 0;

    }

    //The main tests these methods
    public static void main (String [] args){
        Cards card1 = new Cards (1, 11);
        Cards card2 = new Cards (1, 12);
        if (sameCard (card1, card2)) {
            System.out.println ("card1 and card2 are the same card.");
        }
    }

}
