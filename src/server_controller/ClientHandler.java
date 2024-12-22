package server_controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import model.Token;
import model.User;
import repositorys.Walletrepository;
import services.TokenService;
import services.UserService;
import services.WalletService;
import views.UserListPanel;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private DefaultTableModel tableModel;
    private BufferedReader in;
    private PrintWriter out;
    private String clientIP;
    private String connectTime;
    private TCPServer server;
    private UserService userService;
    private TokenService tokenService;
    private WalletService walletService;

    public ClientHandler(Socket socket, DefaultTableModel tableModel, String clientIP, String connectTime, TCPServer server) {
        this.clientSocket = socket;
        this.tableModel = tableModel;
        this.clientIP = clientIP;
        this.connectTime = connectTime;
        this.server = server;
    }
    @Override
    public void run() {
        userService = new UserService();
        tokenService = new TokenService();
        walletService = new WalletService();
        String currentUsername = null;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ObjectOutputStream objectWriter = new ObjectOutputStream(clientSocket.getOutputStream());

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Client gửi: " + message);
                String email = "";
                String[] messageSplit = message.split(",");
                int lengthMessage = messageSplit.length;
                
                String username = null;
                String password = null;
                if (lengthMessage > 1) {
                	username= messageSplit[1];
                	password= messageSplit[2];
                }
               
                if (lengthMessage == 4) {
                    email = messageSplit[3];
                }

                Object[] row = {clientIP, username, email, connectTime};
                tableModel.addRow(row);

                switch (messageSplit[0]) {
                case "request-login":
                    // Kiểm tra đăng nhập và gửi phản hồi login
                    boolean check = userService.checkUser(username, password);
                    double balance = walletService.getBalanceByAccountId(username);
                    currentUsername = username;
                    String responseLogin = check ? "login-success," + username + "," + String.valueOf(balance) : "login-fail";
                    
                    // Gửi phản hồi đăng nhập qua ObjectOutputStream
                    objectWriter.writeObject(responseLogin);
                    objectWriter.flush();
                    
                    // Nếu đăng nhập thành công, gửi danh sách token
                    if (check) {
                    	UserListPanel list = new UserListPanel();
                    	list.updateUserStatus(username, true);
                        List<Token> tokens = tokenService.getListTokens();
                        if (tokens != null) {
                            // In danh sách token để kiểm tra
                            for (Token token : tokens) {
                                System.out.println(token.toString());
                            }
                            // Gửi danh sách token qua ObjectOutputStream
                            objectWriter.writeObject(tokens);
                            objectWriter.flush();
                        } else {
                            out.println("getlistcoin-false");
                        }
                    }
                    
                    break;

                    case "request-register":
                        User user = userService.createUser(username, password, email);
                        String responseRegister = (user != null) ? "register-success" : "register-false";
                        objectWriter.writeObject(responseRegister); // Gửi thông báo qua ObjectOutputStream
                        objectWriter.flush();
                        break;

                    case "request-getlistcoin" :
//                        List<Token> tokens = tokenService.getListTokens();
//                        for (Token token : tokens) {
//							System.out.println(token.toString());
//						}
//                        if (tokens != null) {
//                            objectWriter.writeObject(tokens);
//                            objectWriter.flush();  // Đảm bảo đẩy dữ liệu qua luồng
////                            out.println("getlistcoin-success");
//                            objectWriter.flush();
//                        } else {
//                            out.println("getlistcoin-false");
//                        }
//                       
                        break;
                    	
                    	
                    case "request-deposit" :
                    	double amount_deposit =Double.parseDouble(messageSplit[1]) ;
                    	String wallet_iddeposit = messageSplit[2];
                    	boolean deposit = walletService.depositToWallet(wallet_iddeposit, amount_deposit);
                    	double balance_deposit = walletService.getBalanceByAccountId(wallet_iddeposit);
                    	String responseDeposit = deposit ? "deposit-success," + username + "," + String.valueOf(balance_deposit) : "deposit-fail";
                        
                        // Gửi phản hồi đăng nhập qua ObjectOutputStream
                        objectWriter.writeObject(responseDeposit);
                        objectWriter.flush();
                    	break;
                    case "request-withdraw" :
                    	double amount_withdraw =Double.parseDouble(messageSplit[1]) ;
                    	String wallet_idwithdraw = messageSplit[2];
                    	boolean withdraw = walletService.withdrawFromWallet(wallet_idwithdraw, amount_withdraw);
                    	double balance_withdraw = walletService.getBalanceByAccountId(wallet_idwithdraw);
                    	String responseWithDraw = withdraw ? "withdraw-success," + username + "," + String.valueOf(balance_withdraw) : "withdraw-fail";
                        
                        // Gửi phản hồi đăng nhập qua ObjectOutputStream
                        objectWriter.writeObject(responseWithDraw);
                        objectWriter.flush();
                      break;
                    case "request-quantity_curency" :
                    	String account_name = messageSplit[1] ;
                    	String symbol = messageSplit[2];
                    	double quantity_curency = walletService.getCurencyQuantity(account_name, symbol);
                    	String responseQuantityCurency = "quantity_curency-success," + username + "," + String.valueOf(quantity_curency);
                        
                        // Gửi phản hồi đăng nhập qua ObjectOutputStream
                        objectWriter.writeObject(responseQuantityCurency);
                        objectWriter.flush();
                      break;
                    default:
                        objectWriter.writeObject("Unexpected command: " + messageSplit[0]);
                        objectWriter.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + clientSocket.getInetAddress());
            
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (currentUsername != null) {
            	UserListPanel list = new UserListPanel();
            	list.updateUserStatus(currentUsername, false);
            }
            server.removeClient(clientSocket);
        }
    }

}
