package service;

import model.Student;
import java.util.*;

public class StudentService {
    private static final Map<Integer, Student> STUDENTS = new LinkedHashMap<Integer, Student>();
    private static boolean loaded = false;

    public StudentService() {
        loadIfNeeded();
        seedDemoDataIfEmpty();
    }

    private static synchronized void loadIfNeeded() {
        if (loaded) return;
        STUDENTS.clear();
        for (Student s : DataStore.loadStudents()) {
            STUDENTS.put(s.getId(), s);
        }
        loaded = true;
    }

    private static synchronized void persist() {
        DataStore.saveStudents(STUDENTS.values());
    }

    private static void seedDemoDataIfEmpty() {
        if (!STUDENTS.isEmpty()) return;
        STUDENTS.put(101, new Student(101, "Aryansh Lakhanpal", "BTech CSE", "1111", 120, 95, 50000, 28000, 84));
        STUDENTS.put(102, new Student(102, "Harshit Sharma", "BTech CSE", "2222", 120, 102, 50000, 50000, 91));
        STUDENTS.put(103, new Student(103, "Aayush Bahuguna", "BTech CSE", "3333", 120, 70, 50000, 15000, 67));
        persist();
    }

    public synchronized List<Student> getAllStudents() {
        return new ArrayList<Student>(STUDENTS.values());
    }

    public synchronized Student findById(int id) {
        Student s = STUDENTS.get(id);
        return s == null ? null : s.copy();
    }

    public synchronized void addStudent(Student s) {
        if (STUDENTS.containsKey(s.getId())) throw new IllegalArgumentException("Student ID already exists.");
        STUDENTS.put(s.getId(), s.copy());
        persist();
    }

    public synchronized void updateStudent(Student s) {
        if (!STUDENTS.containsKey(s.getId())) throw new IllegalArgumentException("Student ID not found.");
        STUDENTS.put(s.getId(), s.copy());
        persist();
    }

    public synchronized void deleteStudent(int id) {
        if (!STUDENTS.containsKey(id)) throw new IllegalArgumentException("Student ID not found.");
        STUDENTS.remove(id);
        new NotificationService().removeNotificationsForStudent(id);
        persist();
    }

    public synchronized List<Student> search(String text) {
        String q = text == null ? "" : text.trim().toLowerCase();
        List<Student> result = new ArrayList<Student>();
        for (Student s : STUDENTS.values()) {
            if (s.getName().toLowerCase().contains(q) || s.getCourse().toLowerCase().contains(q)) {
                result.add(s.copy());
            }
        }
        return result;
    }

    public synchronized void refreshFromDisk() {
        loaded = false;
        loadIfNeeded();
    }
}
