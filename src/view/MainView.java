package view;

import javax.swing.*;

public class MainView extends JFrame {
    
    public MainView() {
        setTitle("News Information Management System - Main");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JLabel welcome = new JLabel("Welcome to the News System", SwingConstants.CENTER);
        add(welcome);
    }
}
