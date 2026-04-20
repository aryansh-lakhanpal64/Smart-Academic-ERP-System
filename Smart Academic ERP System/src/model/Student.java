package model;

public class Student {
    private int id;
    private String name;
    private String course;
    private String password;
    private int totalClasses;
    private int attendedClasses;
    private double feeTotal;
    private double feePaid;
    private int marks;

    public Student(int id, String name, String course, String password,
                   int totalClasses, int attendedClasses,
                   double feeTotal, double feePaid, int marks) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.password = password;
        this.totalClasses = totalClasses;
        this.attendedClasses = attendedClasses;
        this.feeTotal = feeTotal;
        this.feePaid = feePaid;
        this.marks = marks;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getTotalClasses() { return totalClasses; }
    public void setTotalClasses(int totalClasses) { this.totalClasses = totalClasses; }

    public int getAttendedClasses() { return attendedClasses; }
    public void setAttendedClasses(int attendedClasses) { this.attendedClasses = attendedClasses; }

    public double getFeeTotal() { return feeTotal; }
    public void setFeeTotal(double feeTotal) { this.feeTotal = feeTotal; }

    public double getFeePaid() { return feePaid; }
    public void setFeePaid(double feePaid) { this.feePaid = feePaid; }

    public int getMarks() { return marks; }
    public void setMarks(int marks) { this.marks = marks; }

    public double getAttendancePercentage() {
        if (totalClasses <= 0) return 0.0;
        return (attendedClasses * 100.0) / totalClasses;
    }

    public double getPendingFee() {
        return feeTotal - feePaid;
    }

    public String getGrade() {
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 70) return "B";
        if (marks >= 60) return "C";
        if (marks >= 40) return "D";
        return "Fail";
    }

    public double getGPA() {
        if (marks >= 90) return 10.0;
        if (marks >= 80) return 9.0;
        if (marks >= 70) return 8.0;
        if (marks >= 60) return 7.0;
        if (marks >= 50) return 6.0;
        if (marks >= 40) return 5.0;
        return 0.0;
    }

    public String getRiskLevel() {
        int score = 0;
        if (getAttendancePercentage() < 75.0) score += 2;
        if (marks < 50) score += 2;
        if (getPendingFee() > 0) score += 1;

        if (score >= 4) return "HIGH";
        if (score >= 2) return "MEDIUM";
        return "LOW";
    }

    public String getRiskReason() {
        StringBuilder sb = new StringBuilder();
        if (getAttendancePercentage() < 75.0) sb.append("Low attendance; ");
        if (marks < 50) sb.append("Low marks; ");
        if (getPendingFee() > 0) sb.append("Fee pending; ");
        if (sb.length() == 0) sb.append("No major risk.");
        return sb.toString();
    }

    public String getSuggestion() {
        if (getAttendancePercentage() < 75.0 && marks < 50) return "Attend extra classes and revise core topics.";
        if (getAttendancePercentage() < 75.0) return "Improve attendance immediately.";
        if (marks < 50) return "Focus on revision and practice tests.";
        if (getPendingFee() > 0) return "Clear pending fee before deadline.";
        return "Keep up the good performance.";
    }

    public Student copy() {
        return new Student(id, name, course, password, totalClasses, attendedClasses, feeTotal, feePaid, marks);
    }
}
