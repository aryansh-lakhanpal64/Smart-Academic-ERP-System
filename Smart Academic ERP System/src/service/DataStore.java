package service;

import model.Notification;
import model.Student;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class DataStore {
    private static final Path DATA_DIR = Paths.get(System.getProperty("user.dir"), "erp_data");
    private static final Path STUDENTS_FILE = DATA_DIR.resolve("students.tsv");
    private static final Path NOTIFICATIONS_FILE = DATA_DIR.resolve("notifications.tsv");

    private static void ensureDir() {
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create data directory", e);
        }
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\t", "\\t").replace("\n", "\\n").replace("\r", "");
    }

    private static String unesc(String s) {
        StringBuilder out = new StringBuilder();
        boolean slash = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (slash) {
                if (c == 't') out.append('\t');
                else if (c == 'n') out.append('\n');
                else out.append(c);
                slash = false;
            } else if (c == '\\') {
                slash = true;
            } else {
                out.append(c);
            }
        }
        if (slash) out.append('\\');
        return out.toString();
    }

    public static List<Student> loadStudents() {
        ensureDir();
        List<Student> list = new ArrayList<Student>();
        if (!Files.exists(STUDENTS_FILE)) return list;

        try (BufferedReader br = Files.newBufferedReader(STUDENTS_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\t", -1);
                if (p.length < 9) continue;
                list.add(new Student(
                        Integer.parseInt(p[0]),
                        unesc(p[1]),
                        unesc(p[2]),
                        unesc(p[3]),
                        Integer.parseInt(p[4]),
                        Integer.parseInt(p[5]),
                        Double.parseDouble(p[6]),
                        Double.parseDouble(p[7]),
                        Integer.parseInt(p[8])
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed loading students", e);
        }
        return list;
    }

    public static void saveStudents(Collection<Student> students) {
        ensureDir();
        try (BufferedWriter bw = Files.newBufferedWriter(STUDENTS_FILE, StandardCharsets.UTF_8)) {
            for (Student s : students) {
                bw.write(s.getId() + "\t" + esc(s.getName()) + "\t" + esc(s.getCourse()) + "\t" + esc(s.getPassword()) + "\t"
                        + s.getTotalClasses() + "\t" + s.getAttendedClasses() + "\t" + s.getFeeTotal() + "\t"
                        + s.getFeePaid() + "\t" + s.getMarks());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed saving students", e);
        }
    }

    public static List<Notification> loadNotifications() {
        ensureDir();
        List<Notification> list = new ArrayList<Notification>();
        if (!Files.exists(NOTIFICATIONS_FILE)) return list;

        try (BufferedReader br = Files.newBufferedReader(NOTIFICATIONS_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\t", 2);
                if (p.length < 2) continue;
                list.add(new Notification(Integer.parseInt(p[0]), unesc(p[1])));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed loading notifications", e);
        }
        return list;
    }

    public static void saveNotifications(Collection<Notification> list) {
        ensureDir();
        try (BufferedWriter bw = Files.newBufferedWriter(NOTIFICATIONS_FILE, StandardCharsets.UTF_8)) {
            for (Notification n : list) {
                bw.write(n.getTargetId() + "\t" + esc(n.getMessage()));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed saving notifications", e);
        }
    }
}
