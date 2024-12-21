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
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return order;
	}
}
