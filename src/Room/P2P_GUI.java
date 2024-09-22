package Room;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class P2P_GUI {
	    private static final int PORT = 12345;
	    private JTextArea textArea;
	    private JTextField textField;
	    private String userName;
	    private Socket socket;
	    private PrintWriter out;

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> new P2P_GUI().createAndShowGUI());
	    }

	    public void createAndShowGUI() {
	        JFrame frame = new JFrame("Peer Chat");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(400, 400);
	        frame.setLocationRelativeTo(null);

	        JPanel panel = new JPanel();
	        panel.setLayout(new GridBagLayout());
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.insets = new Insets(10, 10, 10, 10);

	        // Username field
	        JLabel nameLabel = new JLabel("Nickname");
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        gbc.anchor = GridBagConstraints.EAST;
	        panel.add(nameLabel, gbc);

	        JTextField nameField = new JTextField(15);
	        gbc.gridx = 1;
	        gbc.gridy = 0;
	        gbc.anchor = GridBagConstraints.WEST;
	        panel.add(nameField, gbc);

	        // Server IP field
	        JLabel ipLabel = new JLabel("Server IP");
	        gbc.gridx = 0;
	        gbc.gridy = 1;
	        gbc.anchor = GridBagConstraints.EAST;
	        panel.add(ipLabel, gbc);

	        JTextField ipField = new JTextField(15);
	        gbc.gridx = 1;
	        gbc.gridy = 1;
	        gbc.anchor = GridBagConstraints.WEST;
	        panel.add(ipField, gbc);

	        // Start button
	        JButton startButton = new JButton("Bắt đầu");
	        gbc.gridx = 0;
	        gbc.gridy = 2;
	        gbc.gridwidth = 2;
	        gbc.fill = GridBagConstraints.HORIZONTAL;
	        panel.add(startButton, gbc);

	        frame.getContentPane().add(panel, BorderLayout.CENTER);
	        frame.setVisible(true);

	        startButton.addActionListener(e -> {
	            userName = nameField.getText();
	            String serverIp = ipField.getText();

	            if (userName == null || userName.trim().isEmpty() || serverIp == null || serverIp.trim().isEmpty()) {
	                JOptionPane.showMessageDialog(frame, "Vui lòng nhập đầy đủ thông tin.");
	                return;
	            }

	            frame.getContentPane().remove(panel);
	            frame.setLayout(new BorderLayout());

	            textArea = new JTextArea();
	            textArea.setEditable(false);
	            JScrollPane scrollPane = new JScrollPane(textArea);
	            frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

	            textField = new JTextField();
	            JButton sendButton = new JButton("SEND");
	            sendButton.addActionListener(e1 -> sendMessage());
	            JPanel chatPanel = new JPanel(new BorderLayout());
	            chatPanel.add(textField, BorderLayout.CENTER);
	            chatPanel.add(sendButton, BorderLayout.EAST);
	            frame.getContentPane().add(chatPanel, BorderLayout.SOUTH);

	            frame.revalidate();
	            frame.repaint();

	            // Start client connection to server
	            new Thread(() -> connectToServer(serverIp)).start();
	        });
	    }

	    private void connectToServer(String serverIp) {
	        try {
	            socket = new Socket(serverIp, PORT);
	            out = new PrintWriter(socket.getOutputStream(), true);
	            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            String localIp = InetAddress.getLocalHost().getHostAddress();
	            // Lấy ngày hiện tại
	            LocalDate currentDate = LocalDate.now();

	            // Định dạng ngày theo định dạng mong muốn
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	            String formattedDate = currentDate.format(formatter);
	            out.println("IP của "+userName+ " : " + localIp + " tham gia vào lúc : "+ formattedDate);
	            String message;
	            while ((message = in.readLine()) != null) {
	                textArea.append(message + "\n");
	                
	            }
	        } catch (IOException e) {
	            JOptionPane.showMessageDialog(null, "Lỗi kết nối đến server: " + e.getMessage());
	        }
	    }

	    private void sendMessage() {
	        String message = textField.getText();
	        if (message != null && !message.trim().isEmpty()) {
	            out.println(userName + ": " + message);
	            textField.setText("");
	        }
	    }
	}
