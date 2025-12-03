import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class GameWindow extends JFrame {
    private WordleGame game;
    private WordManager manager;
    private JTextField[][] board;
    private int currentRow = 0;
    private int currentCol = 0;

    public GameWindow() {
        setTitle("Wordle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLayout(new BorderLayout());

        try {
            manager = new WordManager("words.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load word list.");
            System.exit(1);
        }

        game = new WordleGame(manager.getHiddenWord());

        board = new JTextField[WordleGame.MAX_ATTEMPTS][5];
        JPanel boardPanel = new JPanel(new GridLayout(WordleGame.MAX_ATTEMPTS, 5, 5, 5));

        for (int i = 0; i < WordleGame.MAX_ATTEMPTS; i++) {
            for (int j = 0; j < 5; j++) {
                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setFont(new Font("Arial", Font.BOLD, 24));
                tf.setEditable(false);
                tf.setFocusable(false);
                board[i][j] = tf;
                boardPanel.add(tf);
            }
        }

        enableCurrentRow(); 

        add(boardPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void enableCurrentRow() {
        currentCol = 0;
        for (int i = 0; i < 5; i++) {
            JTextField tf = board[currentRow][i];
            tf.setText("");
            tf.setEditable(true);
            tf.setFocusable(true);
            int col = i;
            tf.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!Character.isLetter(c)) {
                        e.consume();
                        return;
                    }
                    tf.setText(String.valueOf(Character.toUpperCase(c)));
                    e.consume();
                    // move to next column
                    if (col + 1 < 5) {
                        board[currentRow][col + 1].requestFocus();
                    }
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                        if (tf.getText().isEmpty() && col > 0) {
                            board[currentRow][col - 1].setText("");
                            board[currentRow][col - 1].requestFocus();
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        processRow();
                    }
                }
            });
        }
        board[currentRow][0].requestFocus();
    }

    private void processRow() {
        StringBuilder guessBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            String letter = board[currentRow][i].getText();
            if (letter.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all 5 letters.");
                return;
            }
            guessBuilder.append(letter);
        }
        String guess = guessBuilder.toString().toUpperCase();

        String[] feedback = game.checkGuess(guess);

        for (int i = 0; i < 5; i++) {
            board[currentRow][i].setBackground(switch (feedback[i]) {
                case "GREEN" -> Color.GREEN;
                case "YELLOW" -> Color.YELLOW;
                default -> Color.LIGHT_GRAY;
            });
            board[currentRow][i].setEditable(false);
            board[currentRow][i].setFocusable(false);
        }

        if (game.isCorrect(guess)) {
            JOptionPane.showMessageDialog(this, "Congratulations! You guessed the word!");
            resetGame();
        } else if (game.isGameOver()) {
            JOptionPane.showMessageDialog(this, "Game Over! The word was: " + manager.getHiddenWord());
            resetGame();
        } else {
            currentRow++;
            if (currentRow < WordleGame.MAX_ATTEMPTS) {
                enableCurrentRow();
            }
        }
    }

    private void resetGame() {
        manager.pickRandomWord();
        game = new WordleGame(manager.getHiddenWord());
        currentRow = 0;
        for (int i = 0; i < WordleGame.MAX_ATTEMPTS; i++) {
            for (int j = 0; j < 5; j++) {
                board[i][j].setText("");
                board[i][j].setBackground(Color.WHITE);
                board[i][j].setEditable(false);
                board[i][j].setFocusable(false);
            }
        }
        enableCurrentRow();
    }
}


