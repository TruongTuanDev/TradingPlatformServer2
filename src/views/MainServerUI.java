package views;
import javax.swing.*;
import java.awt.*;

public class MainServerUI extends JFrame {
	private AdminDashboard adminDashboard; 
    
    public MainServerUI() {
        // Tạo cửa sổ chính
        setTitle("Server");
        setSize(1091, 681);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane(); 
        // Tab 1: Danh sách người dùng
        UserListPanel userListPanel = new UserListPanel();
        tabbedPane.addTab("User List", userListPanel);

        // Tab 2: Hoạt động của client
        TCPServerPanel clientActivityPanel = new TCPServerPanel();
        tabbedPane.addTab("Client Activities", clientActivityPanel);

        // Tab 3: Thêm Token
        AddTokenPanel addtoken = new AddTokenPanel();
        tabbedPane.addTab("Add Info", addtoken);

        // Thêm JTabbedPane vào cửa sổ
        getContentPane().add(tabbedPane);
    }

    public static void main(String[] args) {
        // Tạo và hiển thị giao diện
        SwingUtilities.invokeLater(() -> {
            MainServerUI serverGUI = new MainServerUI();
            serverGUI.setVisible(true);
        });
    }
}
