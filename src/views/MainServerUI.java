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
//        userListPanel.setLayout(new BorderLayout());
//        String[] columnNames = {"User ID", "Username", "Status"};
//        Object[][] data = {
//            {"1", "User1", "Online"},
//            {"2", "User2", "Offline"},
//        };
//        JTable userTable = new JTable(data, columnNames);
//        JScrollPane userScrollPane = new JScrollPane(userTable);
//        userListPanel.add(userScrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("User List", userListPanel);

        // Tab 2: Hoạt động của client
        TCPServerPanel clientActivityPanel = new TCPServerPanel();
//        clientActivityPanel.setLayout(new BorderLayout());
//        JTextArea activityLog = new JTextArea();
//        JScrollPane activityScrollPane = new JScrollPane(activityLog);
//        clientActivityPanel.add(activityScrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Client Activities", clientActivityPanel);

        // Tab 3: Thêm thông tin
        adminDashboard = new AdminDashboard();
       
        tabbedPane.addTab("Add Info", adminDashboard);

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
