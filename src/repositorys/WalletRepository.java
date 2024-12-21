package repositorys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WalletRepository {
	static Connection con;
	static PreparedStatement ps = null;
	static ResultSet rs = null;
	static Statement st = null;
	public boolean updateBalanceByAccountId(String accountID) {
	    boolean isUpdated = false; 
	    try {
	        // Kết nối cơ sở dữ liệu
	        con = utils.ConnectDB.getConnection();

	        // Câu lệnh SQL để cập nhật balance của một account cụ thể
	        String query = "UPDATE wallets " +
	                       "SET balance = (SELECT SUM(balance) " +
	                       "FROM wallets AS w WHERE w.account_id = ?) " +
	                       "WHERE account_id = ?";

	        // Chuẩn bị câu lệnh SQL
	        ps = con.prepareStatement(query);
	        ps.setString(1, accountID); // Tham số cho SUM(balance)
	        ps.setString(2, accountID); // Tham số cho WHERE account_id

	        // Thực thi câu lệnh
	        int rowsUpdated = ps.executeUpdate();

	        // Kiểm tra kết quả cập nhật
	        if (rowsUpdated > 0) {
	            isUpdated = true;
	            System.out.println("Cập nhật thành công " + rowsUpdated + " dòng cho accountID: " + accountID);
	        } else {
	            System.out.println("Không có dòng nào được cập nhật cho accountID: " + accountID);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        // Đóng kết nối
	        try {
	            if (ps != null) ps.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return isUpdated;
	}

}
