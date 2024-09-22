package P2P;

import javax.swing.*;
import java.awt.*;

public class PeerUI {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField chatInput;
    private JButton sendButton, connectButton, listenButton, sendFileButton;
    private JTextField ipInput;
    private PeerConnection connection;

    public PeerUI() {
        // Thiết lập giao diện mới
        frame = new JFrame("P2P Chat");
        frame.setSize(600, 400);
        frame.setMinimumSize(new Dimension(600, 400));

        // Sử dụng màu nền và bố cục mới
        chatArea = new JTextArea(20, 50);
        chatArea.setBackground(Color.WHITE);
        chatArea.setForeground(Color.BLACK);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        
        chatInput = new JTextField(40);
        chatInput.setFont(new Font("Arial", Font.PLAIN, 14));
        chatInput.setPreferredSize(new Dimension(300, 30));

        sendButton = new JButton("Send");
        sendFileButton = new JButton("Send File");
        connectButton = new JButton("Connect");
        listenButton = new JButton("Listen");

        ipInput = new JTextField("Enter IP", 20);
        ipInput.setFont(new Font("Arial", Font.ITALIC, 14));
        ipInput.setPreferredSize(new Dimension(200, 30));

        // Panel chứa các nút điều khiển ở trên cùng
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(173, 216, 230));
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(new JLabel("IP: "));
        controlPanel.add(ipInput);
        controlPanel.add(connectButton);
        controlPanel.add(listenButton);

        // Panel cho khu vực chat và gửi tin nhắn
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBackground(Color.LIGHT_GRAY);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(chatInput);
        inputPanel.add(sendButton);
        inputPanel.add(sendFileButton);

        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        // Bố cục tổng thể
        frame.setLayout(new BorderLayout());
        frame.add(controlPanel, BorderLayout.NORTH); // Đặt controlPanel ở trên cùng
        frame.add(new JScrollPane(chatArea), BorderLayout.CENTER); // Khu vực hiển thị chat
        frame.add(chatPanel, BorderLayout.SOUTH); // Khu vực nhập chat và gửi file

        chatArea.setEditable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        connection = new PeerConnection(chatArea, this);

        // Sự kiện cho các nút
        sendButton.addActionListener(e -> sendMessage(chatInput.getText()));
        connectButton.addActionListener(e -> connection.setupConnection(ipInput.getText()));
        listenButton.addActionListener(e -> connection.listenForConnection());
        sendFileButton.addActionListener(e -> connection.sendFile());
    }

    public void appendMessage(String message) {
        chatArea.append(message + "\n");
    }

    private void sendMessage(String message) {
        appendMessage("You: " + message);
        connection.sendMessage(message);
    }

    public static void main(String[] args) {
        PeerUI peerUI = new PeerUI();
        Runtime.getRuntime().addShutdownHook(new Thread(peerUI.connection::closeResources));
    }
}
