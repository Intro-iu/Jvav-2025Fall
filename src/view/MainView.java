package view;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    
    public MainView() {
        setTitle("新闻信息管理系统 - 主界面");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JLabel welcome = new JLabel("欢迎使用新闻信息管理系统", SwingConstants.CENTER);
        welcome.setFont(new Font("SimHei", Font.BOLD, 20));
        add(welcome);
    }
}