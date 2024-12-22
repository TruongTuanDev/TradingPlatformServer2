package services;

import model.Wallet;
import repositorys.Walletrepository;

public class WalletService {
	private Wallet wallet;
	private Walletrepository walletRepository;
	public double getBalanceByAccountId(String account_id) {
		  double balance = 0;
		  walletRepository = new Walletrepository();
		  balance = walletRepository.getBalanceByAccountId(account_id);
	      return balance;
	  }
	public boolean depositToWallet(String walletId, double amount) {
		  boolean check = false;
		  walletRepository = new Walletrepository();
		  check = walletRepository.depositToWallet(walletId, amount);
		  return check;
	}
	public boolean withdrawFromWallet(String walletId, double amount) {
		  boolean check = false;
		  walletRepository = new Walletrepository();
		  check = walletRepository.withdrawFromWallet(walletId, amount);
		  return check;
	}
	public double getCurencyQuantity(String account_id, String symbol) {
		  double quantity_curency = 0;
		  walletRepository = new Walletrepository();
		  quantity_curency = walletRepository.getCurencyQuantity(account_id, symbol);
	      return quantity_curency;
	  }
}
