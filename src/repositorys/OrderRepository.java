package repositorys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

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
	        matchOrders();
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
	
	
	
	public void matchOrders() {
	    try {
	        con = utils.ConnectDB.getConnection();

	        
	        String sellQuery = "SELECT * FROM orders WHERE order_type = 'sell' AND status = 'wait' ORDER BY price ASC, created_at ASC";
	        ps = con.prepareStatement(sellQuery);
	        ResultSet sellOrders = ps.executeQuery();

	        while (sellOrders.next()) {
	            int sellOrderId = sellOrders.getInt("order_id");
	            String sellUserId = sellOrders.getString("user_id");
	            double sellPrice = sellOrders.getDouble("price");
	            double sellQuantity = sellOrders.getDouble("quantity");

	           
	            String buyQuery = "SELECT * FROM orders WHERE order_type = 'buy' AND status = 'wait' AND price >= ? ORDER BY price DESC, created_at ASC";
	            PreparedStatement buyPs = con.prepareStatement(buyQuery);
	            buyPs.setDouble(1, sellPrice);
	            ResultSet buyOrders = buyPs.executeQuery();

	            while (buyOrders.next()) {
	                int buyOrderId = buyOrders.getInt("order_id");
	                String buyUserId = buyOrders.getString("user_id");
	                double buyPrice = buyOrders.getDouble("price");
	                double buyQuantity = buyOrders.getDouble("quantity");

	               
	                double matchedQuantity = Math.min(sellQuantity, buyQuantity);

	                
	                sellQuantity -= matchedQuantity;
	                if (sellQuantity == 0) {
	                    updateOrderStatus(sellOrderId, 0, "matched");
	                    break;
	                } else {
	                    updateOrderQuantity(sellOrderId, sellQuantity);
	                }

	                
	                buyQuantity -= matchedQuantity;
	                if (buyQuantity == 0) {
	                    updateOrderStatus(buyOrderId, 0, "matched");
	                } else {
	                    updateOrderQuantity(buyOrderId, buyQuantity);
	                }

	              
	                saveTransaction(buyUserId, sellUserId, matchedQuantity, sellPrice);
	            }

	            buyPs.close();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	// Hàm cập nhật trạng thái lệnh
	private void updateOrderStatus(int orderId, double quantity, String status) {
	    try {
	        String query = "UPDATE orders SET quantity = ?, status = ? WHERE order_id = ?";
	        ps = con.prepareStatement(query);
	        ps.setDouble(1, quantity);
	        ps.setString(2, status);
	        ps.setInt(3, orderId);
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	// Hàm cập nhật số lượng lệnh
	private void updateOrderQuantity(int orderId, double quantity) {
	    try {
	        String query = "UPDATE orders SET quantity = ? WHERE order_id = ?";
	        ps = con.prepareStatement(query);
	        ps.setDouble(1, quantity);
	        ps.setInt(2, orderId);
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	// Hàm lưu lịch sử giao dịch
	private void saveTransaction(String buyerId, String sellerId, double quantity, double price) {
	    try {
	        String query = "INSERT INTO transactions(buyer_id, seller_id, quantity, price, created_at) VALUES (?, ?, ?, ?, ?)";
	        ps = con.prepareStatement(query);
	        ps.setString(1, buyerId);
	        ps.setString(2, sellerId);
	        ps.setDouble(3, quantity);
	        ps.setDouble(4, price);
	        ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

}
