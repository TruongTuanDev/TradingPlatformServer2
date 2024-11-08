package repositorys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.User;

public class UserRepository {
	static Connection con;
	static PreparedStatement ps = null;
	static ResultSet rs = null;
	static Statement st = null;
	public User save(User user) {
		try {
			LocalDate nDate = LocalDate.now();
			String date = nDate + "";
			con = utils.ConnectDB.getConnection();
			String query = "INSERT INTO accounts (username,password,email) VALUES (?,?,?)";
			ps = con.prepareStatement(query);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getEmail());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
    }
	public boolean checklogin(String username, String password) {
	    boolean isValid = false;
	    try {
	        con = utils.ConnectDB.getConnection();
	        String query = "SELECT * FROM accounts WHERE username = ? AND password = ?";
	        ps = con.prepareStatement(query);
	        ps.setString(1, username);
	        ps.setString(2, password);
	        rs = ps.executeQuery();
	        if (rs.next()) {
	            isValid = true; 
	            System.out.println(isValid);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return isValid;
	}
	public List<User> findAll() {
	    List<User> userList = new ArrayList<>(); // Danh sách để lưu tất cả người dùng
	    Connection con = null; 
	    PreparedStatement ps = null; 
	    ResultSet rs = null;

	    try {

	        con = utils.ConnectDB.getConnection();
	        
	        // Câu truy vấn để lấy tất cả người dùng từ bảng 'accounts'
	        String query = "SELECT * FROM accounts";
	        ps = con.prepareStatement(query);
	        
	        // Thực thi câu truy vấn và lấy kết quả trả về
	        rs = ps.executeQuery();
	        
	        // Duyệt qua từng bản jay trong kết quả trả về
	        while (rs.next()) {
	            // Tạo đối tượng User và gán các giá trị từ kết quả truy vấn
	            User user = new User();
	            user.setId(rs.getString("id"));
	            user.setUsername(rs.getString("username"));
	            user.setPassword(rs.getString("password"));
	            user.setEmail(rs.getString("email"));
	            user.setOnline(rs.getBoolean("is_online"));
	            // Thêm người dùng vào danh sách
	            userList.add(user);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } 
	    
	    return userList; // Trả về danh sách người dùng
	}

}
