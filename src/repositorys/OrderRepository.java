package repositorys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Order;

public class OrderRepository {
	static Connection con;
	static PreparedStatement ps = null;
	static ResultSet rs = null;
	static Statement st = null;
	public Order saveOrder(Order order) {
	    try {
	        con = utils.ConnectDB.getConnection();
	        String query = "INSERT INTO orders(user_id, order_type, currency, price, quantity, status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
	        ps = con.prepareStatement(query);
	        ps.setString(1, order.getUserId());
	        ps.setString(2, order.getOrderType());
	        ps.setString(3, order.getCurrency());
	        ps.setDouble(4, order.getPrice());
	        ps.setDouble(5, order.getQuantity());
	 
	        ps.setString(6, order.getStatus());
	        ps.setTimestamp(7, order.getCreatedAt());
	      
	        ps.executeUpdate();
	        double amount = order.getPrice()*order.getQuantity();
	        BalanceOrder( order.getUserId(), amount);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return order;
	}
	public boolean BalanceOrder(String walletId, double amount) {
        if (amount <= 0) {
            System.out.println("Số tiền rút phải lớn hơn 0.");
            return false;
        }

        try {
        	con = utils.ConnectDB.getConnection();
            // Kiểm tra số dư hiện tại của ví
            String queryCheckBalance = "SELECT balance FROM wallet_transactions WHERE wallet_id = ? ORDER BY created_at DESC LIMIT 1";
            PreparedStatement checkBalanceStmt = con.prepareStatement(queryCheckBalance);
            checkBalanceStmt.setString(1, walletId);
            ResultSet rs = checkBalanceStmt.executeQuery();
            
            double currentBalance = 0.0;
            if (rs.next()) {
                currentBalance = rs.getDouble("balance");
            } else {
                System.out.println("Không tìm thấy ví hoặc ví chưa có giao dịch nào.");
                return false;
            }

            // Kiểm tra xem số dư có đủ để rút không
            if (currentBalance < amount) {
                System.out.println("Số dư không đủ để thực hiện giao dịch.");
                return false;
            }

            // Tính toán số dư mới
            double newBalance = currentBalance - amount;

            // Thêm giao dịch mới vào bảng wallet_transactions
            String insertTransaction = "INSERT INTO wallet_transactions (wallet_id, transaction_type, amount, balance) VALUES (?, 'withdrawal', ?, ?)";
            PreparedStatement insertStmt = con.prepareStatement(insertTransaction);
            insertStmt.setString(1, walletId);
            insertStmt.setDouble(2, amount);
            insertStmt.setDouble(3, newBalance);

            int rowsInserted = insertStmt.executeUpdate();

//            if (rowsInserted > 0) {
//            	updateWalletBalanceForAll(walletId,newBalance);
//                System.out.println("Rút tiền thành công! Số dư mới: " + newBalance);
//                return true;
//            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi rút tiền: " + e.getMessage());
        }
        return false;
    }
}
