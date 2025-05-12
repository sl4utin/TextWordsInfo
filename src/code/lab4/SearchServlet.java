package code.lab4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    private static final String DB_PATH = "src/data/words.db";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String query = request.getParameter("word");
        if (query == null || query.isEmpty()) {
            request.setAttribute("results", List.of());
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        List<Result> results = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            String sql = "SELECT file_name, count FROM word_stats WHERE word = ? ORDER BY count DESC";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, query.toLowerCase());
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String filename = rs.getString("file_name");
                        int count = rs.getInt("count");
                        results.add(new Result(filename, count));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("results", results);
        request.setAttribute("query", query);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    public static class Result {
        public String filename;
        public int count;

        public Result(String filename, int count) {
            this.filename = filename;
            this.count = count;
        }
    }
}
