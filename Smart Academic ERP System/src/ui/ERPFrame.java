package ui;

import model.Student;
import service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.nio.file.Path;
import java.util.List;

public class ERPFrame extends JFrame {
    private final StudentService studentService = new StudentService();
    private final NotificationService notificationService = new NotificationService();
    private final ReportService reportService = new ReportService();
    private final ExportService exportService = new ExportService();

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField idField, nameField, courseField, passField;
    private JTextField totalField, attendedField, feeTotalField, feePaidField, marksField, searchField, notifyTargetField;
    private JTextArea reportArea, notifyArea;
    private JLabel statusLabel;
    private JProgressBar attendanceBar, marksBar, feeBar;
    private JLabel riskLabel, suggestionLabel;

    public ERPFrame() {
        Theme.applyFrame(this);
        setTitle("Faculty ERP Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 760));
        setLayout(new BorderLayout(10, 10));

        JLabel title = Theme.title("Faculty ERP Dashboard");
        title.setBorder(BorderFactory.createEmptyBorder(12, 18, 8, 18));
        add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Students", studentsPanel());
        tabs.addTab("Insights", insightsPanel());
        tabs.addTab("Reports", reportsPanel());
        tabs.addTab("Notifications", notificationsPanel());
        add(tabs, BorderLayout.CENTER);

        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        add(statusLabel, BorderLayout.SOUTH);

        refreshTable();
        setVisible(true);
    }

    private JPanel studentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Theme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel form = Theme.cardPanel();
        form.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        idField = new JTextField(10);
        nameField = new JTextField(12);
        courseField = new JTextField(12);
        passField = new JTextField(10);
        totalField = new JTextField(8);
        attendedField = new JTextField(8);
        feeTotalField = new JTextField(8);
        feePaidField = new JTextField(8);
        marksField = new JTextField(8);
        searchField = new JTextField(10);

        JButton addBtn = Theme.styledButton("Add");
        JButton updateBtn = Theme.styledButton("Update");
        JButton deleteBtn = Theme.styledButton("Delete");
        JButton clearBtn = Theme.styledButton("Clear");
        JButton searchBtn = Theme.styledButton("Search");
        JButton refreshBtn = Theme.styledButton("Refresh");

        int y = 0;
        c.gridx = 0; c.gridy = y; form.add(new JLabel("ID"), c);
        c.gridx = 1; form.add(idField, c);
        c.gridx = 2; form.add(new JLabel("Name"), c);
        c.gridx = 3; form.add(nameField, c);
        c.gridx = 4; form.add(new JLabel("Course"), c);
        c.gridx = 5; form.add(courseField, c);

        y++;
        c.gridx = 0; c.gridy = y; form.add(new JLabel("Password"), c);
        c.gridx = 1; form.add(passField, c);
        c.gridx = 2; form.add(new JLabel("Total Classes"), c);
        c.gridx = 3; form.add(totalField, c);
        c.gridx = 4; form.add(new JLabel("Attended"), c);
        c.gridx = 5; form.add(attendedField, c);

        y++;
        c.gridx = 0; c.gridy = y; form.add(new JLabel("Fee Total"), c);
        c.gridx = 1; form.add(feeTotalField, c);
        c.gridx = 2; form.add(new JLabel("Fee Paid"), c);
        c.gridx = 3; form.add(feePaidField, c);
        c.gridx = 4; form.add(new JLabel("Marks"), c);
        c.gridx = 5; form.add(marksField, c);

        y++;
        c.gridx = 0; c.gridy = y; form.add(addBtn, c);
        c.gridx = 1; form.add(updateBtn, c);
        c.gridx = 2; form.add(deleteBtn, c);
        c.gridx = 3; form.add(clearBtn, c);
        c.gridx = 4; form.add(refreshBtn, c);
        c.gridx = 5; form.add(new JLabel("Search ID"), c);

        y++;
        c.gridx = 5; c.gridy = y; form.add(searchField, c);
        c.gridx = 4; form.add(searchBtn, c);

        panel.add(form, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Course", "Attendance %", "Pending Fee", "Grade", "Risk"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromSelection();
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = Theme.cardPanel();
        bottom.setLayout(new GridLayout(2, 2, 12, 12));
        attendanceBar = progress("Attendance");
        marksBar = progress("Marks");
        feeBar = progress("Fee Clearance");
        riskLabel = labelBox("Risk: -");
        suggestionLabel = labelBox("Suggestion: -");

        bottom.add(attendanceBar);
        bottom.add(marksBar);
        bottom.add(feeBar);
        bottom.add(riskLabel);
        panel.add(bottom, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addStudent());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        clearBtn.addActionListener(e -> clearFields());
        searchBtn.addActionListener(e -> searchStudent());
        refreshBtn.addActionListener(e -> refreshTable());

        return panel;
    }

    private JPanel insightsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Theme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel top = Theme.cardPanel();
        top.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton riskBtn = Theme.styledButton("Risk Prediction");
        JButton suggestBtn = Theme.styledButton("Auto Suggestions");
        JButton summaryBtn = Theme.styledButton("Summary");
        top.add(riskBtn);
        top.add(suggestBtn);
        top.add(summaryBtn);

        reportArea = new JTextArea();
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setEditable(false);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        riskBtn.addActionListener(e -> reportArea.setText(reportService.riskReport()));
        suggestBtn.addActionListener(e -> reportArea.setText(reportService.suggestionReport()));
        summaryBtn.addActionListener(e -> reportArea.setText(reportService.overallSummary()));

        return panel;
    }

    private JPanel reportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Theme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel top = Theme.cardPanel();
        top.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton lowBtn = Theme.styledButton("Low Attendance");
        JButton feeBtn = Theme.styledButton("Pending Fees");
        JButton topBtn = Theme.styledButton("Top Performers");
        JButton exportBtn = Theme.styledButton("Export Report");

        top.add(lowBtn);
        top.add(feeBtn);
        top.add(topBtn);
        top.add(exportBtn);

        reportArea = new JTextArea();
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setEditable(false);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        lowBtn.addActionListener(e -> reportArea.setText(reportService.lowAttendanceReport(75.0)));
        feeBtn.addActionListener(e -> reportArea.setText(reportService.pendingFeeReport()));
        topBtn.addActionListener(e -> reportArea.setText(reportService.topPerformersReport()));
        exportBtn.addActionListener(e -> exportCurrentReport());

        return panel;
    }

    private JPanel notificationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Theme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel top = Theme.cardPanel();
        top.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        notifyTargetField = new JTextField(8);
        JTextField messageField = new JTextField(28);

        JButton addBtn = Theme.styledButton("Add");
        JButton refreshBtn = Theme.styledButton("Refresh");
        JButton smartBtn = Theme.styledButton("Generate Smart Alerts");
        JButton clearBtn = Theme.styledButton("Clear All");

        c.gridx = 0; c.gridy = 0; top.add(new JLabel("Target ID (-1 = all)"), c);
        c.gridx = 1; top.add(notifyTargetField, c);
        c.gridx = 2; top.add(new JLabel("Message"), c);
        c.gridx = 3; top.add(messageField, c);

        c.gridx = 1; c.gridy = 1; top.add(addBtn, c);
        c.gridx = 2; top.add(refreshBtn, c);
        c.gridx = 3; top.add(smartBtn, c);
        c.gridx = 4; top.add(clearBtn, c);

        notifyArea = new JTextArea();
        notifyArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        notifyArea.setEditable(false);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(notifyArea), BorderLayout.CENTER);

        addBtn.addActionListener(e -> {
            try {
                int target = Integer.parseInt(notifyTargetField.getText().trim());
                String msg = messageField.getText().trim();
                if (msg.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Enter message.");
                    return;
                }
                notificationService.addNotification(target, msg);
                messageField.setText("");
                refreshNotifications();
                status("Notification added.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid target ID.");
            }
        });

        refreshBtn.addActionListener(e -> refreshNotifications());
        clearBtn.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Clear all notifications?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                notificationService.clearAll();
                refreshNotifications();
                status("Notifications cleared.");
            }
        });

        smartBtn.addActionListener(e -> generateSmartAlerts());

        refreshNotifications();
        return panel;
    }

    private JProgressBar progress(String label) {
        JProgressBar pb = new JProgressBar(0, 100);
        pb.setStringPainted(true);
        pb.setString(label + " -");
        return pb;
    }

    private JLabel labelBox(String text) {
        JLabel l = new JLabel(text);
        l.setOpaque(true);
        l.setBackground(Color.WHITE);
        l.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return l;
    }

    private void status(String s) {
        statusLabel.setText(s);
    }

    private Student readStudentForm() {
        int id = Integer.parseInt(idField.getText().trim());
        String name = nameField.getText().trim();
        String course = courseField.getText().trim();
        String password = passField.getText().trim();
        int total = Integer.parseInt(totalField.getText().trim());
        int attended = Integer.parseInt(attendedField.getText().trim());
        double feeTotal = Double.parseDouble(feeTotalField.getText().trim());
        double feePaid = Double.parseDouble(feePaidField.getText().trim());
        int marks = Integer.parseInt(marksField.getText().trim());

        if (name.isEmpty() || course.isEmpty() || password.isEmpty()) throw new IllegalArgumentException("Fill all fields.");
        if (attended > total) throw new IllegalArgumentException("Attended cannot be greater than total.");
        if (marks < 0 || marks > 100) throw new IllegalArgumentException("Marks must be 0-100.");
        return new Student(id, name, course, password, total, attended, feeTotal, feePaid, marks);
    }

    private void addStudent() {
        try {
            studentService.addStudent(readStudentForm());
            refreshTable();
            clearFields();
            status("Student added.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void updateStudent() {
        try {
            studentService.updateStudent(readStudentForm());
            refreshTable();
            status("Student updated.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void deleteStudent() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            if (JOptionPane.showConfirmDialog(this, "Delete student " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                studentService.deleteStudent(id);
                refreshTable();
                clearFields();
                refreshNotifications();
                status("Student deleted.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid ID.");
        }
    }

    private void searchStudent() {
        try {
            int id = Integer.parseInt(searchField.getText().trim());
            Student s = studentService.findById(id);
            if (s == null) {
                JOptionPane.showMessageDialog(this, "Student not found.");
                return;
            }
            fillForm(s);
            status("Student found.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid ID.");
        }
    }

    private void fillForm(Student s) {
        idField.setText(String.valueOf(s.getId()));
        nameField.setText(s.getName());
        courseField.setText(s.getCourse());
        passField.setText(s.getPassword());
        totalField.setText(String.valueOf(s.getTotalClasses()));
        attendedField.setText(String.valueOf(s.getAttendedClasses()));
        feeTotalField.setText(String.valueOf(s.getFeeTotal()));
        feePaidField.setText(String.valueOf(s.getFeePaid()));
        marksField.setText(String.valueOf(s.getMarks()));

        attendanceBar.setValue((int)Math.round(s.getAttendancePercentage()));
        attendanceBar.setString("Attendance - " + String.format("%.1f%%", s.getAttendancePercentage()));
        marksBar.setValue(s.getMarks());
        marksBar.setString("Marks - " + s.getMarks() + "/100");
        int feePercent = s.getFeeTotal() <= 0 ? 100 : (int)Math.round(((s.getFeePaid() / s.getFeeTotal()) * 100.0));
        feeBar.setValue(Math.max(0, Math.min(100, feePercent)));
        feeBar.setString("Fee Cleared - " + Math.max(0, Math.min(100, feePercent)) + "%");

        riskLabel.setText("Risk: " + s.getRiskLevel() + " | " + s.getRiskReason());
        suggestionLabel.setText("Suggestion: " + s.getSuggestion());
    }

    private void fillFormFromSelection() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int modelRow = table.convertRowIndexToModel(row);
        int id = Integer.parseInt(tableModel.getValueAt(modelRow, 0).toString());
        Student s = studentService.findById(id);
        if (s != null) fillForm(s);
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        courseField.setText("");
        passField.setText("");
        totalField.setText("");
        attendedField.setText("");
        feeTotalField.setText("");
        feePaidField.setText("");
        marksField.setText("");
        searchField.setText("");
        table.clearSelection();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Student> list = studentService.getAllStudents();
        for (Student s : list) {
            tableModel.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getCourse(),
                    String.format("%.1f%%", s.getAttendancePercentage()),
                    String.format("₹%.2f", s.getPendingFee()),
                    s.getGrade(),
                    s.getRiskLevel()
            });
        }
    }

    private void refreshNotifications() {
        StringBuilder sb = new StringBuilder();
        for (model.Notification n : notificationService.getAllNotifications()) {
            sb.append("Target ").append(n.getTargetId()).append(" : ").append(n.getMessage()).append("\n");
        }
        if (sb.length() == 0) sb.append("No notifications.\n");
        notifyArea.setText(sb.toString());
    }

    private void generateSmartAlerts() {
        notificationService.clearAll();
        for (Student s : studentService.getAllStudents()) {
            if (s.getAttendancePercentage() < 75.0) notificationService.addNotification(s.getId(), "Low attendance alert");
            if (s.getPendingFee() > 0) notificationService.addNotification(s.getId(), "Fee pending reminder");
            if (s.getMarks() >= 90) notificationService.addNotification(s.getId(), "Top performer achievement");
        }
        notificationService.addNotification(-1, "General notice: ERP smart alerts generated.");
        refreshNotifications();
        reportArea.setText(reportService.riskReport() + "\n" + reportService.suggestionReport());
        status("Smart alerts generated.");
    }

    private void exportCurrentReport() {
        try {
            String content = reportArea.getText();
            if (content == null || content.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Generate a report first.");
                return;
            }
            Path file = exportService.exportText("ERP_Report.txt", content);
            JOptionPane.showMessageDialog(this, "Exported to:\n" + file.toAbsolutePath());
            status("Report exported.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
