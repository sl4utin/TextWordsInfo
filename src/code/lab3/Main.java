package code.lab3;

import code.general.DatabaseManager;
import code.general.WordFrequencyAnalyzer;

import java.nio.file.Path;
import java.sql.SQLException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Использование: java code.lab3.Main <файл1> <файл2> ... <слово>");
            return;
        }

        String targetWord = args[args.length - 1].toLowerCase();  // Последний аргумент — слово
        Path blacklistPath = Path.of("src/data/blacklist.txt");

        // Подключаемся к базе данных
        String dbPath = "src/data/words.db";
        try {
            DatabaseManager db = new DatabaseManager(dbPath);
            db.createTable();  // Создаем таблицу в базе данных
            db.createWordIndex();  // Создаем индекс по слову для ускорения поиска

            for (int i = 0; i < args.length - 1; i++) {
                String rawFilename = args[i];
                String finalFilename = rawFilename.endsWith(".txt") ? rawFilename : rawFilename + ".txt";
                Path filePath = Path.of("src/data/files", finalFilename);

                WordFrequencyAnalyzer analyzer = new WordFrequencyAnalyzer(blacklistPath);
                analyzer.analyze(filePath);
                analyzer.saveFrequenciesToDatabase(finalFilename, db);  // Сохраняем данные в базу

                // Выводим информацию о слове, если оно задано
                if (targetWord != null && !targetWord.isEmpty()) {
                    analyzer.queryWord(targetWord, filePath.toString());
                } else {
                    analyzer.printAllFrequencies();
                }
            }

            db.close();  // Закрываем соединение с базой данных

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}

// java -cp "out;lib/sqlite-jdbc-3.49.1.0.jar" code.lab3.Main text1 text2 дерево