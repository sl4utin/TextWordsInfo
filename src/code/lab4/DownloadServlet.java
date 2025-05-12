package code.lab4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.*;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {

    private static final String FILE_DIR = "src/data/files/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String filename = request.getParameter("file");
        if (filename == null || filename.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Файл не указан.");
            return;
        }

        Path path = Path.of(FILE_DIR, filename);
        if (!Files.exists(path)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Файл не найден.");
            return;
        }

        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);

        try (OutputStream out = response.getOutputStream()) {
            Files.copy(path, out);
        }
    }
}
