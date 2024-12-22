package repositorys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Walletrepository {
	static Connection con;
	static PreparedStatement ps = null;
	static ResultSet rs = null;
	static Statement st = null;
	public double getBalanceByAccountId(String accountId) {
        double balance = 0.0; // Mặc định balance là 0
        try {
            // Kết nối đến cơ sở dữ liệu
            con = utils.ConnectDB.getConnection();
            
            // Câu truy vấn để lấy balance theo account_id
            String query = "SELECT balance FROM wallet_transactions WHERE wallet_id = ? ORDER BY created_at DESC LIMIT 1";
            ps = con.prepareStatement(query);
            
            // Gán giá trị account_id vào câu truy vấn
            ps.setString(1, accountId);

            // Thực thi câu truy vấn và lấy kết quả
            rs = ps.executeQuery();
            
            // Nếu có kết quả trả về, lấy balance
            if (rs.next()) {
                balance = rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đảm bảo đóng tài nguyên sau khi sử dụng
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return balance; // Trả về balance
    }
	public boolean depositToWallet(String walletId, double amount) {
        if (amount <= 0) {
            System.out.println("Số tiền nạp phải lớn hơn 0.");
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
            }

            // Tính toán số dư mới
            double newBalance = currentBalance + amount;

            // Thêm giao dịch mới vào bảng wallet_transactions
            String insertTransaction = "INSERT INTO wallet_transactions (wallet_id, transaction_type, amount, balance) VALUES (?, 'deposit', ?, ?)";
            PreparedStatement insertStmt = con.prepareStatement(insertTransaction);
            insertStmt.setString(1, walletId);
            insertStmt.setDouble(2, amount);
            insertStmt.setDouble(3, newBalance);

            int rowsInserted = insertStmt.executeUpdate();

            if (rowsInserted > 0) {
            	updateWalletBalanceForAll(walletId,newBalance);
                System.out.println("Nạp tiền thành công! Số dư mới: " + newBalance);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi nạp tiền: " + e.getMessage());
        }
        return false;
    }

	public boolean withdrawFromWallet(String walletId, double amount) {
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

            if (rowsInserted > 0) {
            	updateWalletBalanceForAll(walletId,newBalance);
                System.out.println("Rút tiền thành công! Số dư mới: " + newBalance);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi rút tiền: " + e.getMessage());
        }
        return false;
    }
	public int updateWalletBalanceForAll(String accountId, double newBalance) {
        if (newBalance < 0) {
            System.out.println("Giá trị balance không thể nhỏ hơn 0.");
            return 0;
        }
        con = utils.ConnectDB.getConnection();
        String updateQuery = "UPDATE wallets SET balance = ? WHERE account_id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(updateQuery)) {
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setString(2, accountId);

            int rowsUpdated = preparedStatement.executeUpdate();

            System.out.println("Số dòng được cập nhật: " + rowsUpdated);
            return rowsUpdated;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật balance: " + e.getMessage());
        }
        return 0;
    }
	public double getCurencyQuantity(String accountId,String symbol) {
        double quantity_curency = 0.0; // Mặc định balance là 0
        try {
            // Kết nối đến cơ sở dữ liệu
            con = utils.ConnectDB.getConnection();
            
            // Câu truy vấn để lấy balance theo account_id
            String query = "SELECT quantity_curency FROM wallets WHERE account_id = ? AND symbol = ?";
            ps = con.prepareStatement(query);
            
            // Gán giá trị account_id vào câu truy vấn
            ps.setString(1, accountId);
            ps.setString(2, symbol);
            // Thực thi câu truy vấn và lấy kết quả
            rs = ps.executeQuery();
            
            // Nếu có kết quả trả về, lấy balance
            if (rs.next()) {
            	quantity_curency = rs.getDouble("quantity_curency");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đảm bảo đóng tài nguyên sau khi sử dụng
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return quantity_curency; // Trả về balance
    }
	
}
