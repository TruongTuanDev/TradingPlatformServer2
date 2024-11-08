package views;

import javax.swing.*;
import model.User;
import services.UserService;

import java.awt.*;
import java.util.List;

public class UserListPanel extends JPanel {
    UserService userService;

    public UserListPanel() {
        userService = new UserService();
        List<User> userList = userService.getListUsers();

        // Thiết lập layout cho panel
        setLayout(new BorderLayout());

        // Tạo tên cột cho bảng người dùng
        String[] columnNames = {"User ID", "Username", "Email", "Status"};

        // Chuẩn bị dữ liệu cho bảng
        Object[][] data = new Object[userList.size()][4];
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            data[i][0] = user.getId();
            data[i][1] = user.getUsername();
            data[i][2] = user.getEmail();
            data[i][3] = user.isOnline() ? "Online" : "Offline";
        }

        // Tạo bảng và thêm dữ liệu
        JTable userTable = new JTable(data, columnNames);

        // Thay đổi kích thước chữ trong bảng
        userTable.setFont(new Font("Arial", Font.PLAIN, 16)); // Chữ trong bảng lớn hơn
        userTable.setRowHeight(30); // Tăng chiều cao mỗi hàng trong bảng

        // Thay đổi kích thước chữ của tiêu đề bảng
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18)); // Tiêu đề lớn hơn

        JScrollPane userScrollPane = new JScrollPane(userTable);

        // Thay đổi kích thước cột
        userTable.getColumnModel().getColumn(0).setPreferredWidth(50); // Cột "User ID" nhỏ hơn
        userTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Cột "Username" lớn hơn
        userTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Cột "Email" lớn hơn
        userTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Cột "Status" lớn hơn

        // Thêm bảng vào panel với BorderLayout.CENTER
        add(userScrollPane, BorderLayout.CENTER);
    }
}
