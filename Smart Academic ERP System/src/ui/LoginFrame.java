package ui;

import model.Student;
import service.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JComboBox<String> roleBox;
    private JTextField userField;
    private JPasswordField passField;
    private JLabel hintLabel;
    private final AuthService authService = new AuthService();

    public LoginFrame() {
        Theme.applyFrame(this);
        setTitle("ERP Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1100, 700));
        setLayout(new GridBagLayout());

        JPanel card = Theme.cardPanel();
        card.setPreferredSize(new Dimension(520, 360));
        card.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = Theme.title("College ERP Login");
        title.setHorizontalAlignment(SwingConstants.CENTER);

        roleBox = new JComboBox<String>(new String[]{"Faculty", "Student"});
        userField = new JTextField(20);
        passField = new JPasswordField(20);
        hintLabel = new JLabel("Faculty: faculty / admin123");
        hintLabel.setForeground(Theme.PRIMARY);

        JButton loginBtn = Theme.styledButton("Login");
        JButton demoBtn = Theme.styledButton("Use Demo Faculty");

        roleBox.addActionListener(e -> updateHint());

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        card.add(title, c);

        c.gridy = 1; c.gridwidth = 1;
        card.add(new JLabel("Role"), c);
        c.gridx = 1;
        card.add(roleBox, c);

        c.gridx = 0; c.gridy = 2;
        card.add(new JLabel("Username / ID"), c);
        c.gridx = 1;
        card.add(userField, c);

        c.gridx = 0; c.gridy = 3;
        card.add(new JLabel("Password"), c);
        c.gridx = 1;
        card.add(passField, c);

        c.gridx = 0; c.gridy = 4; c.gridwidth = 2;
        card.add(hintLabel, c);

        JPanel btnRow = new JPanel(new GridLayout(1, 2, 10, 10));
        btnRow.setOpaque(false);
        btnRow.add(loginBtn);
        btnRow.add(demoBtn);

        c.gridy = 5;
        card.add(btnRow, c);

        add(card);

        loginBtn.addActionListener(e -> login());
        demoBtn.addActionListener(e -> {
            roleBox.setSelectedItem("Faculty");
            userField.setText("faculty");
            passField.setText("admin123");
        });

        updateHint();
        setVisible(true);
    }

    private void updateHint() {
        String role = (String) roleBox.getSelectedItem();
        if ("Faculty".equals(role)) hintLabel.setText("Faculty login: faculty / admin123");
        else hintLabel.setText("Student login: Student ID + password");
    }

    private void login() {
        String role = (String) roleBox.getSelectedItem();
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword());

        if ("Faculty".equals(role)) {
            if (authService.loginFaculty(user, pass)) {
                dispose();
                new ERPFrame();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid faculty credentials.");
            }
        } else {
            Student s = authService.loginStudent(user, pass);
            if (s != null) {
                dispose();
                new StudentFrame(s.getId());
            } else {
                JOptionPane.showMessageDialog(this, "Invalid student ID or password.");
            }
        }
    }
}
