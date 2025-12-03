public class WordleGame {
    private String hiddenWord;
    private int attempts;
    public static final int MAX_ATTEMPTS = 6;

    public WordleGame(String hiddenWord) {
        this.hiddenWord = hiddenWord;
        this.attempts = 0;
    }

    public boolean isGameOver() {
        return attempts >= MAX_ATTEMPTS;
    }

    public boolean isCorrect(String guess) {
        return hiddenWord.equalsIgnoreCase(guess);
    }

    public String[] checkGuess(String guess) {
        guess = guess.toUpperCase();
        String[] result = new String[5];
        boolean[] hiddenUsed = new boolean[5];

        // Green letters
        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == hiddenWord.charAt(i)) {
                result[i] = "GREEN";
                hiddenUsed[i] = true;
            }
        }

        // Yellow / Gray letters
        for (int i = 0; i < 5; i++) {
            if (result[i] == null) {
                boolean found = false;
                for (int j = 0; j < 5; j++) {
                    if (!hiddenUsed[j] && guess.charAt(i) == hiddenWord.charAt(j)) {
                        found = true;
                        hiddenUsed[j] = true;
                        break;
                    }
                }
                result[i] = found ? "YELLOW" : "GRAY";
            }
        }

        attempts++;
        return result;
    }
}

