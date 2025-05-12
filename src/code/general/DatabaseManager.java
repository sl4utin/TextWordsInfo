package code.general;

import java.sql.*;
import java.util.Map;

public class DatabaseManager {

    private final Connection conn;

    public DatabaseManager(String dbPath) throws SQLException {
        // Подключаемся к базе данных SQLite
        this.conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

    // Метод для создания таблицы, если она не существует
    public void createTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS word_stats (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "file_name TEXT, " +
                "word TEXT, " +
                "count INTEGER)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    // Метод для сохранения частот слов в базе данных
    public void saveFrequencies(String fileName, Map<String, Integer> frequencies) throws SQLException {
        String insertSQL = "INSERT INTO word_stats(file_name, word, count) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
                pstmt.setString(1, fileName);
                pstmt.setString(2, entry.getKey());
                pstmt.setInt(3, entry.getValue());
                pstmt.addBatch();
            }
            pstmt.executeBatch();  // Выполнение всех операций добавления за один раз
        }
    }

    // Метод для индексации по слову для ускорения поиска
    public void createWordIndex() throws SQLException {
        String createIndexSQL = "CREATE INDEX IF NOT EXISTS idx_word ON word_stats(word)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createIndexSQL);
        }
    }

    // Закрытие соединения с базой данных
    public void close() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
}
