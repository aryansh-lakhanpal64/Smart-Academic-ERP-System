package service;

import model.Student;

public class ResultService {
    public String grade(Student s) { return s.getGrade(); }
    public double gpa(Student s) { return s.getGPA(); }
    public String advice(Student s) {
        if (s.getMarks() < 50) return "Revise core topics.";
        if (s.getMarks() < 75) return "Solve more practice papers.";
        return "Great performance.";
    }
}
