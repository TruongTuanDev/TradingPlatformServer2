package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import server_controller.TCPServer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TCPServerPanel extends JPanel {
    private JButton startButton;
    private JButton stopButton;
    private JTable clientTable;
    private DefaultTableModel tableModel;
    private JLabel clientCountLabel;
    private JLabel activeStatusLabel;
    private TCPServer server;

    public TCPServerPanel() {
        // Thiết lập layout cho JPanel
        setLayout(new BorderLayout());

        // Đặt màu nền và màu chữ
        Color backgroundColor = Color.BLACK;
        Color textColor = Color.GREEN;

        // Panel phía trên để chứa thông tin client
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(backgroundColor);

        // Nhãn hiển thị số lượng client
        clientCountLabel = new JLabel("Number Client: 0");
        clientCountLabel.setForeground(textColor);
        topPanel.add(clientCountLabel);

        // Nhãn hiển thị trạng thái hoạt động của server
        activeStatusLabel = new JLabel("Active: No");
        activeStatusLabel.setForeground(textColor);
        topPanel.add(activeStatusLabel);

        add(topPanel, BorderLayout.NORTH);

        // Bảng hiển thị thông tin client
        String[] columnNames = {"IP","Username", "Action", "Connect Time"};
        tableModel = new DefaultTableModel(columnNames, 0);
        clientTable = new JTable(tableModel);
        clientTable.setBackground(backgroundColor);
        clientTable.setForeground(textColor);
        clientTable.setGridColor(textColor);

        JScrollPane scrollPane = new JScrollPane(clientTable);
        scrollPane.getViewport().setBackground(backgroundColor);
        add(scrollPane, BorderLayout.CENTER);

        // Panel chứa các nút điều khiển
        JPanel panel = new JPanel();
        panel.setBackground(backgroundColor);

        startButton = new JButton("Start Server");
        stopButton = new JButton("Stop Server");
        stopButton.setEnabled(false);

        // Đặt màu cho các nút
        startButton.setBackground(Color.GREEN);
        startButton.setForeground(Color.BLACK);

        stopButton.setBackground(Color.RED);
        stopButton.setForeground(Color.BLACK);

        panel.add(startButton);
        panel.add(stopButton);
        add(panel, BorderLayout.SOUTH);

        // Hành động khi nhấn nút "Start Server"
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server = new TCPServer(tableModel, clientCountLabel, activeStatusLabel);
                server.start();
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                activeStatusLabel.setText("Active: Yes");
            }
        });

        // Hành động khi nhấn nút "Stop Server"
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.stopServer();  // Giả sử bạn có hàm dừng server trong TCPServer
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                activeStatusLabel.setText("Active: No");
            }
        });
    }
}
