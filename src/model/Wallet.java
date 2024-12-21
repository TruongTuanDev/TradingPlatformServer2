package model;

import java.io.Serializable;

public class Wallet implements Serializable {
    private static final long serialVersionUID = 1L; // Đảm bảo tương thích tuần tự hóa

    private double price;
    private double quantity;
    private double quantityCurency;
    private String symbol;
    private String buyDate;
    private String accountID;
    private double balance;
    private String type;
    private boolean isActive;
    private double quantityOrder;
    
    // Constructor mặc định
    public Wallet() {}

    // Constructor với tất cả các tham số
    public Wallet(double price, double quantity, double quantityCurency, String symbol, String buyDate,
                  String accountID, double balance, String type, boolean isActive) {
        this.price = price;
        this.quantity = quantity;
        this.quantityCurency = quantityCurency;
        this.symbol = symbol;
        this.buyDate = buyDate;
        this.accountID = accountID;
        this.balance = balance;
        this.type = type;
        this.isActive = isActive;
    }

    // Phương thức getter và setter
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getQuantityCurency() {
        return quantityCurency;
    }

    public void setQuantityCurency(double quantityCurency) {
        this.quantityCurency = quantityCurency;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }
    
    public double getQuantityOrder() {
		return quantityOrder;
	}

	public void setQuantityOrder(double quantityOrder) {
		this.quantityOrder = quantityOrder;
	}

    // Override phương thức toString để in thông tin của Wallet
    @Override
    public String toString() {
        return "Wallet{" +
                "price=" + price +
                ", quantity=" + quantity +
                ", quantityCurency=" + quantityCurency +
                ", symbol='" + symbol + '\'' +
                ", buyDate='" + buyDate + '\'' +
                ", accountID='" + accountID + '\'' +
                ", balance=" + balance +
                ", type='" + type + '\'' +
", isActive=" + isActive +
                '}';
    }
}