package model;

import java.io.Serializable;

public class Token implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String token_id;
	private String name;
	private String symbol;
	private double current_price;
	private String date;
	private double marketcap;
	private double quantity;
	public Token() {
	}
	public Token(String token_id, String name, String symbol, double current_price, String date, double marketcap,
			double quantity) {
		super();
		this.token_id = token_id;
		this.name = name;
		this.symbol = symbol;
		this.current_price = current_price;
		this.date = date;
		this.marketcap = marketcap;
		this.quantity = quantity;
	}

	public String getToken_id() {
		return token_id;
	}
	public void setToken_id(String token_id) {
		this.token_id = token_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public double getCurrent_price() {
		return current_price;
	}
	public void setCurrent_price(double current_price) {
		this.current_price = current_price;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getMarketcap() {
		return marketcap;
	}
	public void setMarketcap(double marketcap) {
		this.marketcap = marketcap;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "Token [token_id=" + token_id + ", name=" + name + ", symbol=" + symbol + ", current_price="
				+ current_price + ", date=" + date + ", marketcap=" + marketcap + ", quantity=" + quantity + "]";
	}
	
	
	
}
