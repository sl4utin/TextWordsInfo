package code.general;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

public class WordFrequencyAnalyzer {
    private final Set<String> blacklist;
    private final Map<String, Integer> wordFrequencies = new HashMap<>();
    private int totalWords = 0;

    public WordFrequencyAnalyzer(Path blacklistPath) throws IOException {
        this.blacklist = new HashSet<>();
        List<String> lines = Files.readAllLines(blacklistPath, StandardCharsets.UTF_8);
        for (String line : lines) {
            Collections.addAll(blacklist, line.toLowerCase().split("\\s+"));
        }
    }

    public void analyze(Path textFilePath) throws IOException {
        wordFrequencies.clear();
        totalWords = 0;

        List<String> lines = Files.readAllLines(textFilePath, StandardCharsets.UTF_8);
        for (String line : lines) {
            String[] words = line.toLowerCase().replaceAll("[^a-zA-Zа-яА-ЯёЁ]", " ").split("\\s+");
            for (String word : words) {
                if (!word.isEmpty() && !blacklist.contains(word)) {
                    wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
                    totalWords++;
                }
            }
        }
    }

    // Метод для сохранения результатов в базу данных
    public void saveFrequenciesToDatabase(String fileName, DatabaseManager db) throws SQLException {
        db.saveFrequencies(fileName, wordFrequencies);
    }

    public void printAllFrequencies() {
        System.out.println("Общее количество слов (без учёта blacklist): " + totalWords);
        wordFrequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> {
                    String word = entry.getKey();
                    int count = entry.getValue();
                    double percent = totalWords == 0 ? 0.0 : (count * 100.0) / totalWords;
                    if (count > 2) {
                        System.out.printf("%s — %d (%.2f%%)\n", word, count, percent);
                    }
                });
    }

    public void queryWord(String word, String filePath) {
        int count = wordFrequencies.getOrDefault(word.toLowerCase(), 0);
        double percent = totalWords == 0 ? 0.0 : (count * 100.0) / totalWords;

        StringBuilder output = new StringBuilder();
        if (filePath != null && !filePath.isEmpty()) {
            output.append(String.format("Файл: %s\n", filePath));
        }

        if (count > 0) {
            output.append(String.format("Слово \"%s\" встречается %d раз (%.2f%% от общего).",
                    word, count, percent));
        } else {
            output.append(String.format("Слово \"%s\" не было найдено.\n", word));
        }

        System.out.println(output);
    }

    public int getTotalWordCount() {
        return totalWords;
    }
}
