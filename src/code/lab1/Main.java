package code.lab1;

import code.general.WordFrequencyAnalyzer;

import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        // Проверка аргументов
        if (args.length < 1) {
            System.out.println("Использование: java code.lab1.Main <имя_файла> [слово]");
            return;
        }

        String filename = args[0].trim();
        if (!filename.endsWith(".txt")) {
            filename += ".txt";
        }

        String word = args.length >= 2 ? args[1].trim() : null;

        runWithArguments(filename, word);
    }

    public static void runWithArguments(String filename, String word) {
        try {
            Path blacklistPath = Path.of("src/data/blacklist.txt");
            Path textPath = Path.of("src/data/files", filename);

            if (!textPath.toFile().exists()) {
                System.out.println("Файл не найден: " + textPath.toAbsolutePath());
                return;
            }

            WordFrequencyAnalyzer analyzer = new WordFrequencyAnalyzer(blacklistPath);
            analyzer.analyze(textPath);

            if (analyzer.getTotalWordCount() == 0) {
                System.out.println("Нет слов для анализа (возможно всё в blacklist).");
                return;
            }

            if (word != null && !word.isEmpty()) {
                analyzer.queryWord(word,null);
            } else {
                analyzer.printAllFrequencies();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


// java -cp out code.lab1.Main text2
// java -cp out code.lab1.Main text2 день

