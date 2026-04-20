package service;

import model.Student;

public class AuthService {
    private final StudentService studentService = new StudentService();

    public boolean loginFaculty(String username, String password) {
        return "faculty".equalsIgnoreCase(username) && "admin123".equals(password);
    }

    public Student loginStudent(String idText, String password) {
        try {
            int id = Integer.parseInt(idText.trim());
            Student s = studentService.findById(id);
            if (s != null && s.getPassword().equals(password)) return s;
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
