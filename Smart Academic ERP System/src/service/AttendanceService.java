package service;

import model.Student;

public class AttendanceService {
    public double calculate(Student s) { return s.getAttendancePercentage(); }

    public String advice(Student s) {
        double p = calculate(s);
        if (p < 75) return "Attend extra classes.";
        if (p < 85) return "Keep attendance stable.";
        return "Excellent attendance.";
    }
}
