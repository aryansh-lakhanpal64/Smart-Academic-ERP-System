package ui;

import model.Notification;
import model.Student;
import service.NotificationService;
import service.StudentService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentFrame extends JFrame {
    private final int studentId;
    private final StudentService studentService = new StudentService();
    private final NotificationService notificationService = new NotificationService();

    private JLabel nameLabel, courseLabel, attendanceLabel, feeLabel, gradeLabel, gpaLabel, riskLabel, suggestionLabel;
    private JProgressBar attendanceBar, marksBar, feeBar;
    private JTextArea notesArea;
    private JLabel statusLabel;

    public StudentFrame(int studentId) {
        this.studentId = studentId;
        Theme.applyFrame(this);
        setTitle("Student Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1100, 720));
        setLayout(new BorderLayout(10, 10));

        JLabel title = Theme.title("Student Dashboard");
        title.setBorder(BorderFactory.createEmptyBorder(12, 18, 8, 18));
        add(title, BorderLayout.NORTH);

        add(centerPanel(), BorderLayout.CENTER);
        add(bottomBar(), BorderLayout.SOUTH);

        refreshData();
        setVisible(true);
    }

    private JPanel centerPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBackground(Theme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel top = Theme.cardPanel();
        top.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;

        nameLabel = new JLabel("-");
        courseLabel = new JLabel("-");
        attendanceLabel = new JLabel("-");
        feeLabel = new JLabel("-");
        gradeLabel = new JLabel("-");
        gpaLabel = new JLabel("-");
        riskLabel = new JLabel("-");
        suggestionLabel = new JLabel("-");

        attendanceBar = progress("Attendance");
        marksBar = progress("Marks");
        feeBar = progress("Fee");

        JButton refreshBtn = Theme.styledButton("Refresh");

        int y = 0;
        c.gridx = 0; c.gridy = y; top.add(new JLabel("Student ID:"), c);
        c.gridx = 1; top.add(new JLabel(String.valueOf(studentId)), c);

        y++;
        c.gridx = 0; c.gridy = y; top.add(new JLabel("Name:"), c);
        c.gridx = 1; top.add(nameLabel, c);

        y++;
        c.gridx = 0; c.gridy = y; top.add(new JLabel("Course:"), c);
        c.gridx = 1; top.add(courseLabel, c);

        y++;
        c.gridx = 0; c.gridy = y; top.add(new JLabel("Attendance:"), c);
        c.gridx = 1; top.add(attendanceLabel, c);

        y++;
        c.gridx = 0; c.gridy = y; top.add(new JLabel("Fee Status:"), c);
        c.gridx = 1; top.add(feeLabel, c);

        y++;
        c.gridx = 0; c.gridy = y; top.add(new JLabel("Grade:"), c);
        c.gridx = 1; top.add(gradeLabel, c);

        y++;
        c.gridx = 0; c.gridy = y; top.add(new JLabel("GPA:"), c);
        c.gridx = 1; top.add(gpaLabel, c);

        y++;
        c.gridx = 0; c.gridy = y; top.add(new JLabel("Risk:"), c);
        c.gridx = 1; top.add(riskLabel, c);

        y++;
        c.gridx = 0; c.gridy = y; top.add(new JLabel("Suggestion:"), c);
        c.gridx = 1; top.add(suggestionLabel, c);

        y++;
        c.gridx = 0; c.gridy = y; top.add(attendanceBar, c);

        y++;
        c.gridx = 0; c.gridy = y; top.add(marksBar, c);

        y++;
        c.gridx = 0; c.gridy = y; top.add(feeBar, c);

        y++;
        c.gridx = 1; c.gridy = y; top.add(refreshBtn, c);

        refreshBtn.addActionListener(e -> refreshData());

        JPanel bottom = Theme.cardPanel();
        bottom.setLayout(new BorderLayout(8, 8));
        bottom.setBorder(BorderFactory.createTitledBorder("My Notifications"));
        notesArea = new JTextArea();
        notesArea.setEditable(false);
        notesArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        bottom.add(new JScrollPane(notesArea), BorderLayout.CENTER);

        panel.add(top);
        panel.add(bottom);
        return panel;
    }

    private JPanel bottomBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        panel.add(statusLabel, BorderLayout.CENTER);
        return panel;
    }

    private JProgressBar progress(String label) {
        JProgressBar pb = new JProgressBar(0, 100);
        pb.setStringPainted(true);
        pb.setString(label);
        return pb;
    }

    private void refreshData() {
        Student s = studentService.findById(studentId);
        if (s == null) {
            JOptionPane.showMessageDialog(this, "Student not found.");
            dispose();
            new LoginFrame();
            return;
        }

        nameLabel.setText(s.getName());
        courseLabel.setText(s.getCourse());

        attendanceLabel.setText(String.format("%.2f%%", s.getAttendancePercentage()));
        feeLabel.setText(s.getPendingFee() > 0 ? "Pending ₹" + String.format("%.2f", s.getPendingFee()) : "Clear");
        gradeLabel.setText(s.getGrade());
        gpaLabel.setText(String.format("%.1f", s.getGPA()));
        riskLabel.setText(s.getRiskLevel());
        suggestionLabel.setText(s.getSuggestion());

        attendanceBar.setValue((int)Math.round(s.getAttendancePercentage()));
        attendanceBar.setString("Attendance: " + String.format("%.1f%%", s.getAttendancePercentage()));
        marksBar.setValue(s.getMarks());
        marksBar.setString("Marks: " + s.getMarks() + "/100");
        int feePercent = s.getFeeTotal() <= 0 ? 100 : (int)Math.round((s.getFeePaid() / s.getFeeTotal()) * 100.0);
        feeBar.setValue(Math.max(0, Math.min(100, feePercent)));
        feeBar.setString("Fee Paid: " + Math.max(0, Math.min(100, feePercent)) + "%");

        List<Notification> notes = notificationService.getNotificationsForStudent(studentId);
        StringBuilder sb = new StringBuilder();
        for (Notification n : notes) {
            sb.append("- ").append(n.getMessage()).append("\n");
        }
        if (sb.length() == 0) sb.append("No notifications.\n");
        notesArea.setText(sb.toString());

        statusLabel.setText("Loaded student " + studentId);
    }
}
