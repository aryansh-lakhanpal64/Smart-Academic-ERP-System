package ui;

import java.awt.*;
import javax.swing.*;

public class Theme {
    public static final Color BG = new Color(245, 247, 250);
    public static final Color CARD = Color.WHITE;
    public static final Color PRIMARY = new Color(44, 62, 80);
    public static final Color ACCENT = new Color(52, 152, 219);
    public static final Color SUCCESS = new Color(46, 204, 113);
    public static final Color WARNING = new Color(241, 196, 15);
    public static final Color DANGER = new Color(231, 76, 60);

    public static void applyFrame(JFrame frame) {
        frame.getContentPane().setBackground(BG);
    }

    public static JPanel cardPanel() {
        JPanel p = new JPanel();
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return p;
    }

    public static JLabel title(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(PRIMARY);
        l.setFont(new Font("SansSerif", Font.BOLD, 24));
        return l;
    }

    public static JButton styledButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(ACCENT);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        return b;
    }
}
