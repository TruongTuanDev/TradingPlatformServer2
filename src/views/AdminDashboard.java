package views;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public AdminDashboard() {
        // Thiết lập cửa sổ lớn hơn và giao diện tươi sáng
        setTitle("Admin Dashboard");
        setSize(1200, 800); // Kích thước lớn hơn
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // CardLayout để chuyển giữa các trang
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Thêm hai trang JPanel vào mainPanel
        mainPanel.add(createCoinListPanel(), "coinList");
        mainPanel.add(createClientListPanel(), "clientList");
        // Tạo sidebar điều hướng với icon và màu sắc tươi sáng
        JPanel sidebarPanel = createSidebar();

        // Thêm vào JFrame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidebarPanel, BorderLayout.WEST);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    // Sidebar điều hướng với icon và màu sắc tươi sáng
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(5, 1, 10, 10));
        sidebar.setBackground(new Color(180, 225, 255)); // Màu xanh dương nhạt sáng
        sidebar.setPreferredSize(new Dimension(250, 800)); // Kích thước lớn hơn

        JButton coinListButton = createSidebarButton("List Coin", "icons/coin.png");
        JButton clientListButton = createSidebarButton("List Client", "icons/client.png");

        coinListButton.addActionListener(e -> cardLayout.show(mainPanel, "coinList"));
        clientListButton.addActionListener(e -> cardLayout.show(mainPanel, "clientList"));

        sidebar.add(coinListButton);
        sidebar.add(clientListButton);
        return sidebar;
    }

    // Tạo các nút sidebar với icon và không màu tùy chỉnh
    private JButton createSidebarButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18)); // Font chữ Arial hiện đại
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 50));

        // Thêm icon vào nút
        ImageIcon icon = new ImageIcon(iconPath);
        button.setIcon(icon);

        return button; // Sử dụng màu mặc định cho nút (bỏ màu sắc)
    }

    // Trang List Coin với thiết kế font chữ và màu sắc đẹp
 // Trang List Coin với thiết kế font chữ và màu sắc đẹp
    private JPanel createCoinListPanel() {
        JPanel coinPanel = new JPanel();
        coinPanel.setLayout(new BorderLayout());
        coinPanel.setBackground(new Color(240, 248, 255)); // Màu nền xanh nhạt

        // Form nhập thông tin coin với font và cỡ chữ đẹp
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(new Color(240, 248, 255)); // Màu nền nhạt sáng
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel coinNameLabel = new JLabel("Coin Name:");
        coinNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField coinNameField = new JTextField();
        coinNameField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel symbolLabel = new JLabel("Symbol:");
        symbolLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField symbolField = new JTextField();
        symbolField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel currentPriceLabel = new JLabel("Current Price:");
        currentPriceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField currentPriceField = new JTextField();
        currentPriceField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField dateField = new JTextField();
        dateField.setFont(new Font("Arial", Font.PLAIN, 14));

        inputPanel.add(coinNameLabel);
        inputPanel.add(coinNameField);
        inputPanel.add(symbolLabel);
        inputPanel.add(symbolField);
        inputPanel.add(currentPriceLabel);
        inputPanel.add(currentPriceField);
        inputPanel.add(dateLabel);
        inputPanel.add(dateField);

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        inputPanel.add(searchButton);
        inputPanel.add(new JLabel()); // chừa chỗ trống

        // Bảng danh sách coin với màu sắc tươi sáng
        String[] columns = {"Coin Name", "Symbol", "Current Price", "Date"};
        Object[][] data = {{"Bitcoin", "BTC", "$60,000", "2024-10-22"}, 
                           {"Ethereum", "ETH", "$4,000", "2024-10-22"}};
        JTable coinTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(coinTable);
        coinTable.setFont(new Font("Arial", Font.PLAIN, 14));
        coinTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        coinPanel.add(inputPanel, BorderLayout.NORTH);
        coinPanel.add(scrollPane, BorderLayout.CENTER);

        return coinPanel;
    }


    // Trang List Client với thiết kế font chữ và màu sắc đẹp
    private JPanel createClientListPanel() {
        JPanel clientPanel = new JPanel(new BorderLayout());
        clientPanel.setBackground(new Color(240, 248, 255)); // Màu nền sáng nhạt

        // Bảng danh sách client
        String[] columns = {"Client ID", "Request Type", "Request Details"};
        Object[][] data = {{"123", "Withdraw", "100 BTC"}, {"456", "Deposit", "50 ETH"}};
        JTable clientTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(clientTable);
        clientTable.setFont(new Font("Arial", Font.PLAIN, 14));
        clientTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

        clientPanel.add(scrollPane, BorderLayout.CENTER);
        return clientPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminDashboard dashboard = new AdminDashboard();
            dashboard.setVisible(true);
        });
    }
}
