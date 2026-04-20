package service;

import model.Student;
import java.util.*;

public class ReportService {
    private final StudentService studentService = new StudentService();

    public String overallSummary() {
        List<Student> all = studentService.getAllStudents();
        int low = 0, pending = 0, top = 0;
        for (Student s : all) {
            if (s.getAttendancePercentage() < 75) low++;
            if (s.getPendingFee() > 0) pending++;
            if (s.getMarks() >= 85) top++;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Overall Summary\n");
        sb.append("--------------------------------------------------\n");
        sb.append("Total Students: ").append(all.size()).append("\n");
        sb.append("Low Attendance: ").append(low).append("\n");
        sb.append("Pending Fee: ").append(pending).append("\n");
        sb.append("Top Performers: ").append(top).append("\n");
        return sb.toString();
    }

    public String lowAttendanceReport(double threshold) {
        StringBuilder sb = new StringBuilder();
        sb.append("Low Attendance Report (Below ").append(threshold).append("%)\n");
        sb.append("--------------------------------------------------\n");
        int c = 0;
        for (Student s : studentService.getAllStudents()) {
            if (s.getAttendancePercentage() < threshold) {
                c++;
                sb.append(s.getId()).append(" | ").append(s.getName()).append(" | ")
                  .append(String.format("%.2f%%", s.getAttendancePercentage())).append("\n");
            }
        }
        if (c == 0) sb.append("No students found.\n");
        return sb.toString();
    }

    public String pendingFeeReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pending Fee Report\n");
        sb.append("--------------------------------------------------\n");
        int c = 0;
        for (Student s : studentService.getAllStudents()) {
            if (s.getPendingFee() > 0) {
                c++;
                sb.append(s.getId()).append(" | ").append(s.getName()).append(" | Pending ₹")
                  .append(String.format("%.2f", s.getPendingFee())).append("\n");
            }
        }
        if (c == 0) sb.append("No pending fees.\n");
        return sb.toString();
    }

    public String topPerformersReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Top Performers Report\n");
        sb.append("--------------------------------------------------\n");
        int c = 0;
        for (Student s : studentService.getAllStudents()) {
            if (s.getMarks() >= 85) {
                c++;
                sb.append(s.getId()).append(" | ").append(s.getName()).append(" | Grade ")
                  .append(s.getGrade()).append(" | GPA ").append(String.format("%.1f", s.getGPA())).append("\n");
            }
        }
        if (c == 0) sb.append("No top performers.\n");
        return sb.toString();
    }

    public String riskReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Risk Prediction Report\n");
        sb.append("--------------------------------------------------\n");
        int c = 0;
        for (Student s : studentService.getAllStudents()) {
            String risk = s.getRiskLevel();
            if (!"LOW".equals(risk)) {
                c++;
                sb.append(s.getId()).append(" | ").append(s.getName()).append(" | ")
                  .append(risk).append(" | ").append(s.getRiskReason()).append("\n");
            }
        }
        if (c == 0) sb.append("No risky students detected.\n");
        return sb.toString();
    }

    public String suggestionReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Auto Suggestions\n");
        sb.append("--------------------------------------------------\n");
        for (Student s : studentService.getAllStudents()) {
            sb.append(s.getId()).append(" | ").append(s.getName()).append(" -> ")
              .append(s.getSuggestion()).append("\n");
        }
        return sb.toString();
    }
}
