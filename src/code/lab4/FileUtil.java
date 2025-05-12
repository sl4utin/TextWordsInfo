package code.lab4;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileUtil {

    private static final String FILES_DIR = "src/data/files/";

    /**
     * Возвращает список всех текстовых файлов из папки.
     */
    public static List<String> listAllTextFiles() {
        try {
            return Files.list(Path.of(FILES_DIR))
                    .filter(p -> p.toString().endsWith(".txt"))
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Читает содержимое файла как строку.
     */
    public static String readFileContent(String filename) {
        Path path = Path.of(FILES_DIR, filename);
        try {
            return Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Проверяет, существует ли файл в папке.
     */
    public static boolean fileExists(String filename) {
        return Files.exists(Path.of(FILES_DIR, filename));
    }
}
