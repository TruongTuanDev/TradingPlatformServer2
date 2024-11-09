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
import services.TokenService;
import services.UserService;

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
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            ObjectOutputStream objectWriter = new ObjectOutputStream(clientSocket.getOutputStream()); 
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Client gửi: " + message);
                String email="";
                String[] messageSplit = message.split(",");
                int lengthMessage = messageSplit.length;
	            String username = messageSplit[1];
	            String password = messageSplit[2];
	            if(lengthMessage==4) {
	            	email = messageSplit[3];
	            }
             
                Object[] row = {clientIP, username, email, connectTime};
//                Object[] row = {1, 1, 1, 1};
	            tableModel.addRow(row);

                // Xử lý thông điệp và thực hiện hành động
                switch (messageSplit[0]) {
                    case "request-login":
                        boolean check=userService.checkUser(username, password);
                        System.out.println(check);
                        if(check) {
                        	out.println("login-success"+","+username);
                        }else {
                        	out.println("login-fail");
                        }
                        break;

                    case "request-register":
                        User user = userService.createUser(username, password, email);
                        if (user != null) {
                            out.println("register-success");
                        } else {
                            out.println("register-false");
                        }
                        break;
                    case "request-getlistcoin" :
                    	List<Token> tokens = tokenService.getListTokens();
                    	if(tokens != null) {
                    		objectWriter.writeObject(tokens);
                    		objectWriter.flush();
                    		out.println("getlistcoin-success");
                        } else {
                            out.println("getlistcoin-false");
                        }
                    	break;
                    	
                    case "request-check-username":
                        // Thực hiện kiểm tra tên người dùng (nếu cần)
                        break;

                    default:
                        throw new IllegalArgumentException("Unexpected value: " + messageSplit[0]);
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
            // Xóa client khỏi danh sách và cập nhật số lượng client
            server.removeClient(clientSocket);
        }
    }
}
