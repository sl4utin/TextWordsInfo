package code.lab2;

import code.general.WordFrequencyAnalyzer;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Использование: java code.lab2.Main <файл1> <файл2> ... <слово>");
            return;
        }

        String targetWord = args[args.length - 1].toLowerCase();  // Последний аргумент — слово
        Path blacklistPath = Path.of("src/data/blacklist.txt");

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < args.length - 1; i++) {
            String rawFilename = args[i];
            String finalFilename = rawFilename.endsWith(".txt") ? rawFilename : rawFilename + ".txt";
            Path filePath = Path.of("src/data/files", finalFilename);

            executor.submit(() -> {
                try {
                    WordFrequencyAnalyzer analyzer = new WordFrequencyAnalyzer(blacklistPath);
                    analyzer.analyze(filePath);
                    analyzer.queryWord(targetWord, filePath.toString());

                } catch (IOException e) {
                    System.err.println("Ошибка при обработке файла " + finalFilename + ": " + e.getMessage());
                }
            });
        }

        executor.shutdown();
    }
}


// java -cp out code.lab2.Main text1 text2 дерево