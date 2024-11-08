package repositorys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Token;
import model.User;

public class TokenRepository {
	static Connection con;
	static PreparedStatement ps = null;
	static ResultSet rs = null;
	static Statement st = null;
	public Token save(Token token) {
		try {
			con = utils.ConnectDB.getConnection();
			String query = "INSERT INTO tokens(name,symbol,current_price,date,marketcap,quantity) VALUES (?,?,?,?,?,?)";
			ps = con.prepareStatement(query);
			ps.setString(1, token.getName());
			ps.setString(2, token.getSymbol());
			ps.setDouble(3, token.getCurrent_price());
			ps.setString(4, token.getDate());
			ps.setDouble(5, token.getMarketcap());
			ps.setDouble(6, token.getQuantity());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return token;
    }
	public List<Token> findAllTokens() {
	    List<Token> tokens = new ArrayList<>();
	    try {
	        con = utils.ConnectDB.getConnection();
	        String query = "SELECT * FROM tokens ORDER BY date DESC";  
	        ps = con.prepareStatement(query);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Token token = new Token();
	            token.setToken_id(rs.getInt("token_id"));
	            token.setName(rs.getString("name"));
	            token.setSymbol(rs.getString("symbol"));
	            token.setCurrent_price(rs.getDouble("current_price"));
	            token.setDate(rs.getString("date"));
	            token.setMarketcap(rs.getDouble("marketcap"));
	            token.setQuantity(rs.getDouble("quantity"));
	            tokens.add(token);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return tokens;
	}
	public List<Token> findTokenByKeyword(String keyword) {
	    List<Token> tokens = new ArrayList<>();
	    try {
	        con = utils.ConnectDB.getConnection();
	        String query = "SELECT * FROM tokens WHERE name LIKE ? OR symbol LIKE ?";
	        ps = con.prepareStatement(query);
	        ps.setString(1, "%" + keyword + "%"); 
	        ps.setString(2, "%" + keyword + "%");
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Token token = new Token();
	            token.setToken_id(rs.getInt("token_id"));
	            token.setName(rs.getString("name"));
	            token.setSymbol(rs.getString("symbol"));
	            token.setCurrent_price(rs.getDouble("current_price"));
	            token.setDate(rs.getString("date"));
	            token.setMarketcap(rs.getDouble("marketcap"));
	            token.setQuantity(rs.getDouble("quantity"));
	            tokens.add(token);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return tokens;
	}



}
