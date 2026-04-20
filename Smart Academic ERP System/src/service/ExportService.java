package service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class ExportService {
    public Path exportText(String fileName, String content) {
        try {
            Path dir = Paths.get(System.getProperty("user.dir"), "erp_exports");
            Files.createDirectories(dir);
            Path file = dir.resolve(fileName);
            Files.write(file, content.getBytes(StandardCharsets.UTF_8));
            return file;
        } catch (IOException e) {
            throw new RuntimeException("Export failed", e);
        }
    }
}
