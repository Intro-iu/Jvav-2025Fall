package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import model.User;
import service.UserService;

public class LoginView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private UserService userService = new UserService();

    public LoginView() {
        setTitle("登录 - 新闻信息管理系统");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示
        setLayout(new BorderLayout());

        // 标题
        JLabel lblTitle = new JLabel("用户登录", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SimHei", Font.BOLD, 24)); // 使用黑体以支持中文
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // 表单面板
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        formPanel.add(new JLabel("用户名:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("密  码:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        // 占位
        formPanel.add(new JLabel("")); 
        btnLogin = new JButton("登录");
        formPanel.add(btnLogin);

        add(formPanel, BorderLayout.CENTER);

        // 事件监听
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入用户名和密码。", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = userService.login(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "登录成功！欢迎, " + user.getNickname());
            dispose(); // 关闭登录窗口
            new MainView().setVisible(true); // 打开主界面
        } else {
            JOptionPane.showMessageDialog(this, "用户名或密码错误。", "登录失败", JOptionPane.ERROR_MESSAGE);
        }
    }
}