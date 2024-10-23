import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SecretNumberGame extends JFrame implements ActionListener {
    private JButton[] buttons = new JButton[20];
    private String player1Name, player2Name;
    private int player1Secret, player2Secret;
    private boolean player1Turn = true; // Player 1 starts
    private int player1RoundsWon = 0, player2RoundsWon = 0;
    private int totalRounds, roundsPlayed = 0;

    public SecretNumberGame() {
        setTitle("Secret Number Game");
        setLayout(new GridLayout(5, 4)); // Grid of 5 rows x 4 columns for numbers 1 to 20

        // Initialize buttons for numbers 1 to 20
        for (int i = 0; i < 20; i++) {
            buttons[i] = new JButton(String.valueOf(i + 1));
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 20));
            buttons[i].setFocusPainted(false);
            buttons[i].addActionListener(this);
            add(buttons[i]);
        }

        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        // Get player names and the number of rounds to play (best of 5, 7, 10, etc.)
        player1Name = JOptionPane.showInputDialog("Enter Player 1's name:");
        player2Name = JOptionPane.showInputDialog("Enter Player 2's name:");
        totalRounds = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of rounds (e.g., 5, 7, 10):"));
        getPlayerSecrets();
        updateTurnDisplay(); // Update the display to show whose turn it is
    }

    private void getPlayerSecrets() {
        // Get secret numbers for both players at the start of each round
        player1Secret = Integer.parseInt(JOptionPane.showInputDialog(player1Name + ": Enter your secret number (1-20):"));
        player2Secret = Integer.parseInt(JOptionPane.showInputDialog(player2Name + ": Enter your secret number (1-20):"));
    }

    private void resetGame() {
        // Reset all buttons and prepare for the next round
        for (JButton button : buttons) {
            button.setEnabled(true);
        }
        player1Turn = true; // Player 1 starts each new round
        getPlayerSecrets(); // Get new secret numbers for the new round
        updateTurnDisplay(); // Show the new turn
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton buttonClicked = (JButton) e.getSource();
        int numberClicked = Integer.parseInt(buttonClicked.getText());

        // Disable the button once clicked
        buttonClicked.setEnabled(false);

        // Check whose turn it is and make sure they aren't clicking their own secret number
        if (player1Turn) {
            if (numberClicked == player1Secret) {
                // Prevent Player 1 from clicking their own secret number
                JOptionPane.showMessageDialog(this, player1Name + " cannot click their own secret number!");
                buttonClicked.setEnabled(true); // Re-enable the button and let them try again
            } else if (numberClicked == player2Secret && player1Secret != player2Secret) {
                // Player 2 wins because Player 1 clicked Player 2's number, but only if secrets are different
                player2RoundsWon++;
                JOptionPane.showMessageDialog(this, player1Name + " clicked " + player2Name + "'s secret number! " + player2Name + " wins this round.");
                endRound();
            } else {
                // Switch to Player 2's turn if no secret number was clicked
                player1Turn = false;
                updateTurnDisplay(); // Update turn display for Player 2
            }
        } else {
            if (numberClicked == player2Secret) {
                // Prevent Player 2 from clicking their own secret number
                JOptionPane.showMessageDialog(this, player2Name + " cannot click their own secret number!");
                buttonClicked.setEnabled(true); // Re-enable the button and let them try again
            } else if (numberClicked == player1Secret && player1Secret != player2Secret) {
                // Player 1 wins because Player 2 clicked Player 1's number, but only if secrets are different
                player1RoundsWon++;
                JOptionPane.showMessageDialog(this, player2Name + " clicked " + player1Name + "'s secret number! " + player1Name + " wins this round.");
                endRound();
            } else {
                // Switch to Player 1's turn if no secret number was clicked
                player1Turn = true;
                updateTurnDisplay(); // Update turn display for Player 1
            }
        }

        // Check if only one button remains active and both secret numbers are unclicked
        if (remainingButtons() == 1 && (numberClicked != player1Secret && numberClicked != player2Secret)) {
            JOptionPane.showMessageDialog(this, "It's a draw! Both players had the same secret number.");
            endRound(); // End the round as a draw
        }
    }

    private void updateTurnDisplay() {
        // Update the window title or display to show whose turn it is
        String turn = player1Turn ? player1Name + "'s Turn" : player2Name + "'s Turn";
        setTitle(player1Name + ": " + player1RoundsWon + " | " + player2Name + ": " + player2RoundsWon + " | " + turn);
    }

    private void endRound() {
        roundsPlayed++;
        // Check if we've played all rounds or if one player has won the majority of rounds
        if (roundsPlayed == totalRounds || (player1RoundsWon > totalRounds / 2 || player2RoundsWon > totalRounds / 2)) {
            String winner = (player1RoundsWon > player2RoundsWon) ? player1Name + " Wins the Game!" : player2Name + " Wins the Game!";
            if (player1RoundsWon == player2RoundsWon) winner = "It's a Tie!";
            JOptionPane.showMessageDialog(this, winner);
            System.exit(0); // End the game after the final round
        } else {
            resetGame(); // Start the next round
        }
    }

    private int remainingButtons() {
        int remaining = 0;
        for (JButton button : buttons) {
            if (button.isEnabled()) {
                remaining++;
            }
        }
        return remaining;
    }

    public static void main(String[] args) {
        new SecretNumberGame();
    }
}


