import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * By: Toby Thomas
 * Date: 04/14/15
 * Description: This is an interactive and live action game of Crazy 8's against a computer AI. While the user interface simulates an actual card table, users can
 * scroll through their cards and the opponent's, see the discard pile, and pick up cards from the Pick Up pile, when needed. Additionally, the game offers a reset
 * and help button, as well as a updating tag line for every move. This class extends JFrame, implements ActionListener and incorporates Cards and Deck classes.
 */

public class Game extends JFrame implements ActionListener{


    //Initialize global deck objects
    Deck deck;
    Deck playPile;
    Deck pickUpPile;

    //Initialize global arrays of deck and visual deck objects
    ArrayList<Deck> hands = new ArrayList<Deck>();
    ArrayList<ArrayList<JLabel>> handsCards = new ArrayList<ArrayList<JLabel>>();

    //Initialize global containers
    GridBagLayout gb = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    JScrollPane comPane;
    JScrollPane userPane;
    JPanel comPanel;
    JPanel playPanel;
    JPanel userPanel;

    //Initialize global components
    JLabel playLabel;
    JButton pickUpButton;
    JLabel updates = new JLabel();

    //Initialize global dynamic constants
    Cards QoS = new Cards(3, 12);
    int currentSuit, currentRank;
    int turn = 2;
    int numOfPickUp = 0;

    //This constructor initializes the game
    public Game() {
        super("Crazy 8's!");

        deal(2);

        setLayout(gb);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 200;
        gbc.ipady = 225;
        gbc.insets = new Insets(5,5,5,5);

        updates.setText("Your turn.");

        createUserPanel();
        playPanel = createPlayingField();
        createComPanel();

        comPane = new JScrollPane(comPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        userPane = new JScrollPane(userPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        gbc.gridx=0;
        gbc.gridy=0;
        super.add(comPane, gbc);

        gbc.ipadx = 200;
        gbc.ipady = 0;

        gbc.gridx=0;
        gbc.gridy=1;
        super.add(playPanel, gbc);

        gbc.ipadx = 200;
        gbc.ipady = 225;

        gbc.gridx=0;
        gbc.gridy=2;
        super.add(userPane, gbc);

        super.setBackground(Color.decode("#590000"));
        super.pack();
        super.setSize(660,765);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setVisible(true);

    }

    //This method deals the cards at the beginning of the game
    public void deal(int numOfPlayers) {
        deck = new Deck();
        Deck.shuffle(deck);

        int low = 0;
        int high = 7;

        for (int i = 0; i < numOfPlayers; i++) {
            hands.add(Deck.subdeck(deck, low, high));
            handsCards.add(subdeckCards(i, low, high));
            low+=8;
            high+=8;
        }

        playPile = Deck.subdeck(deck, low, low);
        pickUpPile = Deck.subdeck(deck, low + 1, 51);

        currentRank = playPile.cards.get(playPile.cards.size() - 1).rank;
        currentSuit = playPile.cards.get(playPile.cards.size() - 1).suit;
    }

    //This method divides a deck object into an array of card labels
    public ArrayList<JLabel> subdeckCards (int player, int low, int high) {
        ArrayList<JLabel> sub = new ArrayList<JLabel>();

        if (player == 0) {
            for (int i = 0; i < (high - low + 1); i++) {
                sub.add(cardToLabel(hands.get(player).cards.get(low + i)));
            }
        } else if (player == 1) {
            for (int i = 0; i < (high - low + 1); i++) {
                sub.add(cardToLabel(new Cards()));
            }
        }

        return sub;
    }

    //This method reshuffles the pick up and discard pile, when the pick up pile has no more cards
    public void reshuffle() {
        int numOfPickUp = 0;
        for (int i = 1; i <= 5; i++) {
            if (i == 1) {
                if (playPile.cards.get(playPile.cards.size() - i).rank == 2) numOfPickUp += 1;
                else if (playPile.cards.get(playPile.cards.size() - i).rank == 12) numOfPickUp += 1;
            } else if (i == 2){
                if (playPile.cards.get(playPile.cards.size() - i).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 1).rank == 2) numOfPickUp += 1;
            } else if (i == 3) {
                if (playPile.cards.get(playPile.cards.size() - i).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 1).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 2).rank == 2) numOfPickUp += 1;
            } else if (i == 4) {
                if (playPile.cards.get(playPile.cards.size() - i).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 1).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 2).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 3).rank == 2) numOfPickUp += 1;
            } else if (i == 5) {
                if (playPile.cards.get(playPile.cards.size() - i).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 1).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 2).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 3).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 4).rank == 2) numOfPickUp += 1;
            }
        }
        for (int i = 0; i < playPile.cards.size() - (1 + numOfPickUp); i++) {
            pickUpPile.cards.add(playPile.cards.get(i));
            playPile.cards.remove(i);
        }
        Deck.shuffle(pickUpPile);
    }

    //This method determines whose turn it is next
    public int whoseTurnsNext() {
        if (turn % 2 == 0) return 0;
        else return 1;
    }

    //This method is the main pivot between moves, allowing a player to place down a card, as well as initiate the next player's move
    public void playCard(int player, int indexOfCard) {
        currentSuit = hands.get(player).cards.get(indexOfCard).suit;
        currentRank = hands.get(player).cards.get(indexOfCard).rank;

        playPile.cards.add(hands.get(player).cards.get(indexOfCard));

        playPanel.remove(playLabel);
        playLabel = cardToLabel(playPile.cards.get(playPile.cards.size() - 1));
        playPanel.add(playLabel);
        playPanel.validate();
        playPanel.repaint();

        hands.get(player).cards.remove(indexOfCard);
        hands.get(player).cards.trimToSize();
        handsCards.get(player).trimToSize();

        playSpecialCard(playPile.cards.get(playPile.cards.size()-1));

        turn += 1;

        checkWin();

        if (whoseTurnsNext() == 1) {
            updateComPanel();
        } else if (whoseTurnsNext() == 0) {
            updateUserPanel();
        }
    }

    //This method reacts to special cards in an appropriate manner
    public void playSpecialCard(Cards specialCard) {
        if (specialCard.rank == 2 || Cards.sameCard(specialCard, QoS)) {
            if (specialCard.rank == 2) numOfPickUp += 2;
            else numOfPickUp += 5;

            if (whoseTurnsNext() == 0) {
                updates.setText("Computer picked up " + numOfPickUp + " cards.");
                for (int i = 0; i < numOfPickUp; i++) {
                    pickUp(1);
                }
            } else if (whoseTurnsNext() == 1) {
                JOptionPane.showMessageDialog(null, "Computer made you pick up " + numOfPickUp + " cards!");
                updates.setText("You picked up " + numOfPickUp + " cards.");
                for (int i = 0; i < numOfPickUp; i++) {
                    pickUp(0);
                }
            }

            return;

        } else if (specialCard.rank == 8) {
            if (whoseTurnsNext() == 0) {
                final Object[] inputs = new Object[]{"Clubs", "Diamonds", "Hearts", "Spades"};
                currentSuit = JOptionPane.showOptionDialog(super.createRootPane(), "Which suit do you want?", "Choose a suit", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, inputs, inputs[3]);
                if (currentSuit == 0) {
                    updates.setText("Current Suit is Clubs");
                } else if (currentSuit == 1) {
                    updates.setText("Current Suit is Diamonds");
                } else if (currentSuit == 2) {
                    updates.setText("Current Suit is Hearts");
                } else if (currentSuit == 3) {
                    updates.setText("Current Suit is Spades");
                }
            } else if (whoseTurnsNext() == 1) {
                currentSuit = (int)(Math.random()*4);
                if (currentSuit == 0) {
                    JOptionPane.showMessageDialog(null, "Computer changed it to Clubs!");
                    updates.setText("Current Suit is Clubs");
                } else if (currentSuit == 1) {
                    JOptionPane.showMessageDialog(null, "Computer changed it to Diamonds!");
                    updates.setText("Current Suit is Diamonds");
                } else if (currentSuit == 2) {
                    JOptionPane.showMessageDialog(null, "Computer changed it to Hearts!");
                    updates.setText("Current Suit is Hearts");
                } else if (currentSuit == 3) {
                    JOptionPane.showMessageDialog(null, "Computer changed it to Spades!");
                    updates.setText("Current Suit is Spades");
                }
            }
        } else if (specialCard.rank == 11) {
            if (whoseTurnsNext() == 1) {
                JOptionPane.showMessageDialog(null, "Computer skipped your turn!");
                turn += 1;
            }
            else if (whoseTurnsNext() == 0) {
                updates.setText("Computer skips a turn!");
                turn += 1;
            }
        }

        numOfPickUp = 0;
    }

    //This check method checks to see if there is a winner
    public void checkWin() {
        if (ifWon(hands.get(0))) {
            winner(0);
        } else if (ifWon(hands.get(1))) {
            winner(1);
        }
    }

    //This method returns true if a player's hand is empty
    public Boolean ifWon(Deck hand) {
        if (hand.cards.size() == 0) return true;
        return false;
    }

    //This method announces victory
    public void winner(int player) {
        if (player == 0) {
            JOptionPane.showMessageDialog(null, "You won!");
            dispose();
            new Game();
        } else if (player == 1) {
            JOptionPane.showMessageDialog(null, "You lost!");
            dispose();
            new Game();
        }

    }

    //This method checks if a move is valid
    public Boolean isValidMove(Cards card) {
        if (card.suit == currentSuit || card.rank == currentRank || card.rank == 8) return true;
        return false;
    }

    //This method returns true if a player can play any of its cards
    public Boolean canPlay(Deck hand) {
        for (int i = 0; i < hand.cards.size(); i++) {
            if (hand.cards.get(i).suit == currentSuit || hand.cards.get(i).rank == currentRank || hand.cards.get(i).rank == 8) return true;
        } return false;
    }

    //This methods picks up a card for a player
    public void pickUp(int player) {
        if (pickUpPile.cards.size() == 0) {
            reshuffle();
        }
        hands.get(player).cards.add(pickUpPile.cards.get(pickUpPile.cards.size() - 1));
        if (player == 0) {
            handsCards.get(player).add(cardToLabel(pickUpPile.cards.get(pickUpPile.cards.size() - 1)));
            userPanel.add(handsCards.get(0).get(handsCards.get(0).size()-1));
            userPanel.validate();
            userPanel.repaint();
        } else if (player == 1) {
            handsCards.get(player).add(cardToLabel(new Cards()));
            comPanel.removeAll();
            for (int i = 0; i < handsCards.get(1).size(); i++) {
                comPanel.add(handsCards.get(1).get(i));
            }
            comPanel.validate();
            comPanel.repaint();
        }

        pickUpPile.cards.remove(pickUpPile.cards.size() - 1);
        pickUpPile.cards.trimToSize();
    }

    //This method creates the playing field
    public JPanel createPlayingField() {
        GridBagConstraints gbcd = new GridBagConstraints();
        JPanel panel = new JPanel(gb);
        pickUpButton = new JButton();
        pickUpButton.setIcon(cardToLabel(new Cards()).getIcon());
        playLabel = cardToLabel(playPile.cards.get(playPile.cards.size() - 1));
        pickUpButton.setBorderPainted(false);
        JPanel menu = createMenu();

        gbcd.fill = GridBagConstraints.HORIZONTAL;
        gbcd.insets = new Insets(10,10,10,10);

        gbcd.gridy = 0;
        gbcd.gridx = 0;
        panel.add(pickUpButton, gbcd);
        gbcd.gridx = 1;
        panel.add(menu, gbcd);
        gbcd.gridx = 2;
        panel.add(playLabel, gbcd);

        pickUpButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!canPlay(hands.get(0))) {
                    pickUp(0);
                    System.out.println("(User Pick up) Current turn: " + turn);
                    turn += 1;
                    System.out.println("(User Pick up) Next turn: " + turn);
                    updateComPanel();
                } else JOptionPane.showMessageDialog(null, "You don't need a card!");
            }
        });

        return panel;
    }

    //This method creates the menu within the playing field
    public JPanel createMenu() {
        GridBagConstraints gbcd = new GridBagConstraints();
        JPanel panel = new JPanel(gb);
        JButton newGame = new JButton("New Game");
        JButton help = new JButton("Help");

        updates.setHorizontalAlignment(JLabel.CENTER);
        updates.setForeground(Color.WHITE);

        gbcd.fill = GridBagConstraints.HORIZONTAL;
        gbcd.ipadx = 10;
        gbcd.ipady = 10;
        gbcd.insets = new Insets(10,10,10,10);

        gbcd.gridx = 0;
        gbcd.gridy = 0;
        panel.add(newGame, gbcd);
        gbcd.gridx = 0;
        gbcd.gridy = 1;
        panel.add(updates, gbcd);
        gbcd.gridx = 0;
        gbcd.gridy = 2;
        panel.add(help, gbcd);

        newGame.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
                new Game();
            }
        });

        help.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null, "Thanks for playing Crazy 8's!\n\n" + "Here are some tips and tricks to play the game smoothly:\n\n" +
                "1. If you don't have any cards to play, click on the Pick Up\npile or press spacebar to pick up a card.\n\n" + "2. If you played or picked" +
                " up a card, but your deck didn't\nseem to change, try scrolling through your cards or resizing\nthe game's frame to kick the GUI back into action." +
                        "\n(The same goes for the computer's deck)\n\n" +
                "3. Please email at toby.james.thomas@gmail.com for any\nother concerns!\n\n" + "Toby Thomas\nMs. Shaw\nICS4U\n04/14/15");
            }
        });

        return panel;
    }

    //This method formulates the valid moves for the computer
    public Deck comValidMoves(Deck hand) {
        Deck validMoves = new Deck(0);

        for (int i = 0; i < hand.cards.size(); i++) {
            if (hand.cards.get(i).suit == currentSuit || hand.cards.get(i).rank == currentRank || hand.cards.get(i).rank == 8) {
                validMoves.cards.add(hand.cards.get(i));
            }
        }

        validMoves.cards.trimToSize();
        return validMoves;
    }

    //This method creates the computer's deck panel
    public void createComPanel() {
        comPanel = new JPanel();
        for (int i = 0; i < handsCards.get(1).size(); i++) {
            comPanel.add(handsCards.get(1).get(i));
        }
    }

    //This method updates the computer's deck panel, as well as initiates its move
    public void updateComPanel() {
        comPanel.removeAll();
        for (int i = 0; i < handsCards.get(1).size(); i++) {
            comPanel.add(handsCards.get(1).get(i));
        }
        if (canPlay(hands.get(1))) {
            Deck compValidMoves = comValidMoves(hands.get(1));
            int indexOfCard = hands.get(1).cards.indexOf(compValidMoves.cards.get((int)(Math.random()*compValidMoves.cards.size())));
            comPanel.remove(handsCards.get(1).get(handsCards.get(1).size() - 1));
            comPanel.validate();
            comPanel.repaint();
            handsCards.get(1).remove(indexOfCard);
            updates.setText("Your turn.");
            playCard(1, hands.get(1).cards.indexOf(hands.get(1).cards.get(indexOfCard)));
        } else {
            updates.setText("Computer 1 picked up a card.");
            pickUp(1);
            System.out.println("(Com Pick up) Current turn: " + turn);
            turn += 1;
            System.out.println("(Com Pick up) Next turn: " + turn);
            updateUserPanel();
        }
    }

    //This method create the user's deck panel, as well as initiates its first move
    public void createUserPanel() {
        userPanel = new JPanel();
        for (int i = 0; i < handsCards.get(0).size(); i++) {
            if (isValidMove(hands.get(0).cards.get(i))) {
                final Border border = BorderFactory.createLineBorder(Color.GREEN, 3);
                final int finalI = i;
                userPanel.add(handsCards.get(0).get(i));
                handsCards.get(0).get(i).addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        userPanel.remove(handsCards.get(0).get(finalI));
                        userPanel.validate();
                        userPanel.repaint();
                        handsCards.get(0).remove(finalI);
                        playCard(0, finalI);
                    }

                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent mouseEvent) {
                        handsCards.get(0).get(finalI).setBorder(border);
                    }

                    @Override
                    public void mouseExited(MouseEvent mouseEvent) {
                        handsCards.get(0).get(finalI).setBorder(null);
                    }
                });
            } else {
                userPanel.add(handsCards.get(0).get(i));
            }
        }
    }

    //This method updates the user's panel, as well as initiates its moves
    public void updateUserPanel() {
        for (int i = 0; i < handsCards.get(0).size(); i++) {
            for (int a = 0; a < handsCards.get(0).get(i).getMouseListeners().length; a++) {
                handsCards.get(0).get(i).removeMouseListener(handsCards.get(0).get(i).getMouseListeners()[a]);
            }
        }
        userPanel.removeAll();
        for (int i = 0; i < handsCards.get(0).size(); i++) {
            if (isValidMove(hands.get(0).cards.get(i))) {
                final Border border = BorderFactory.createLineBorder(Color.GREEN, 3);
                final int finalI = i;
                userPanel.add(handsCards.get(0).get(i));
                handsCards.get(0).get(i).addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        userPanel.remove(handsCards.get(0).get(finalI));
                        userPanel.validate();
                        userPanel.repaint();
                        handsCards.get(0).remove(finalI);
                        playCard(0, finalI);
                    }

                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent mouseEvent) {
                        handsCards.get(0).get(finalI).setBorder(border);
                    }

                    @Override
                    public void mouseExited(MouseEvent mouseEvent) {
                        handsCards.get(0).get(finalI).setBorder(null);
                    }
                });
            } else {
                userPanel.add(handsCards.get(0).get(i));
            }
        }
    }

    //This helper method assigns a card to a label
    public static JLabel cardToLabel(Cards c) {
        JLabel cardLabel = createCard(c);
        return cardLabel;
    }

    //This helper method creates a label from a card
    public static JLabel createCard(Cards card) {
        ImageIcon cardIMG = createImageIcon(card, Cards.cardToString(card));
        JLabel cardL = new JLabel(cardIMG);
        return cardL;
    }

    //Helper method, used to import and create images of cards
    protected static ImageIcon createImageIcon(Cards card,
                                               String description) {
        String path = "images/" + card.rank + "-" + card.suit + ".png";
        java.net.URL imgURL = Game.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    //This print method prints all of game's players' hands
    public void printHands() {
        for (int i = 0; i < hands.size(); i++) {
            System.out.print("Player " + (i+1) + ":\n");
            Deck.printDeck(hands.get(i));
            System.out.print("\n");
        }
    }

    //This print method prints a single player's hand
    public void printPlayer(int player) {
        System.out.print("Player " + (player + 1) + ":\n");
        Deck.printDeck(hands.get(player));
    }

    //This print method prints the hands and play pile of the game
    public void printGame() {
        printHands();
        System.out.println("PlayPile: " + Cards.cardToString(playPile.cards.get(playPile.cards.size()-1)));
        System.out.print("\n");
    }

    //Action listener method
    public interface ActionListener {
        public void actionPerformed (ActionEvent e);
    }

    //Action performed method
    public void actionPerformed (ActionEvent e) {
    }

    //The main initiates and runs the game
    public static void main(String[] args) {
        new Game();
    }

}