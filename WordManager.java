import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class WordManager {
    private List<String> solutionWords;   // Hidden words
    private String hiddenWord;

    public WordManager(String solutionFile) throws IOException {
        solutionWords = Files.lines(Paths.get(solutionFile))
                .map(String::toUpperCase)
                .filter(w -> w.length() == 5)
                .collect(Collectors.toList());

        pickRandomWord();
    }

    public void pickRandomWord() {
        Random random = new Random();
        hiddenWord = solutionWords.get(random.nextInt(solutionWords.size()));
    }

    public String getHiddenWord() {
        return hiddenWord;
    }
}
