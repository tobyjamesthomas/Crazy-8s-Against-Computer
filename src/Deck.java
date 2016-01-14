import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * By Toby Thomas
 */

public class Deck {

    //Initialize global array of cards
    ArrayList<Cards> cards;

    //This constructor creates a deck with a certain size
    public Deck (int n) {
        cards = new ArrayList<Cards>(n);
    }

    //Default constructor – creates a deck of 52 cards
    public Deck () {
        cards = new ArrayList<Cards>(52);
        int index = 0;
        for (int suit = 0; suit <= 3; suit++) {
            for (int rank = 1; rank <= 13; rank++) {
                cards.add(new Cards(suit, rank));
                index++;
            }
        }
    }

    //This print method prints a deck
    public static void printDeck (Deck deck) {
        for (int i = 0; i < deck.cards.size(); i++) {
            Cards.printCard(deck.cards.get(i));
        }
    }

    //This toString method creates a String of a deck
    public static String deckToString (Deck deck) {
        String deckString = "";
        for (int i = 0; i < deck.cards.size(); i++) {
            deckString += Cards.cardToString(deck.cards.get(i)) + "\n";
        }
        return deckString;
    }

    //This method switches two cards within a deck
    public static void switchCards (Deck deck, int card1, int card2){
        Cards cardA = new Cards(deck.cards.get(card1).suit, deck.cards.get(card1).rank);
        Cards cardB = new Cards(deck.cards.get(card2).suit, deck.cards.get(card2).rank);

        deck.cards.get(card1).suit = cardB.suit;
        deck.cards.get(card1).rank = cardB.rank;

        deck.cards.get(card2).suit = cardA.suit;
        deck.cards.get(card2).rank = cardA.rank;

    }

    //Shuffle switches random cards in a deck
    public static void shuffle (Deck deck) {
        for (int i = 0; i < deck.cards.size(); i++) {
            int random = (int)(Math.random()*deck.cards.size());
            switchCards(deck, i, random);
        }
    }

    //This method creates a subdeck of a deck
    public static Deck subdeck (Deck deck, int low, int high) {
        Deck sub = new Deck(high - low + 1);

        for (int i = 0; i < (high - low + 1); i++) {
            sub.cards.add(deck.cards.get(low + i));
        }

        return sub;
    }

    //The main method tests these methods
    public static void main (String [] args) {
        Deck deck = new Deck();
        shuffle(deck);
        Deck hand1 = subdeck(deck, 0, 7);
        printDeck(hand1);
    }



}
