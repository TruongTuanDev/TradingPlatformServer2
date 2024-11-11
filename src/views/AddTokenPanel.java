package views;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;

import model.Token;
import model.User;
import services.TokenService;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class AddTokenPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable tokenTable;
    private TokenService tokenService;
    public AddTokenPanel() {
        tokenService = new TokenService();
        List<Token> tokenList = tokenService.getListTokens();
        setLayout(null);
        
        // Điều chỉnh kích thước inputPanel
        JPanel inputPanel = new JPanel();
        inputPanel.setBounds(10, 10, 400, 500); // Đảm bảo kích thước nhỏ gọn để tăng không gian cho tablePanel
        add(inputPanel);
        inputPanel.setLayout(null);

        JLabel lblNameCoin = new JLabel("Name Coin:");
        lblNameCoin.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNameCoin.setBounds(20, 30, 150, 40);
        inputPanel.add(lblNameCoin);
        
        JTextField txtNameCoin = new JTextField();
        txtNameCoin.setFont(new Font("Tahoma", Font.PLAIN, 16));
        txtNameCoin.setBounds(150, 30, 220, 40);
        inputPanel.add(txtNameCoin);

        JLabel lblSymbol = new JLabel("Symbol:");
        lblSymbol.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblSymbol.setBounds(20, 90, 150, 40);
        inputPanel.add(lblSymbol);
        
        JTextField txtSymbol = new JTextField();
        txtSymbol.setFont(new Font("Tahoma", Font.PLAIN, 16));
        txtSymbol.setBounds(150, 90, 220, 40);
        inputPanel.add(txtSymbol);

        JLabel lblPrice = new JLabel("Price:");
        lblPrice.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblPrice.setBounds(20, 150, 150, 40);
        inputPanel.add(lblPrice);
        
        JTextField txtPrice = new JTextField();
        txtPrice.setFont(new Font("Tahoma", Font.PLAIN, 16));
        txtPrice.setBounds(150, 150, 220, 40);
        inputPanel.add(txtPrice);

        JLabel lblDate = new JLabel("Date:");
        lblDate.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblDate.setBounds(20, 210, 150, 40);
        inputPanel.add(lblDate);

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Tahoma", Font.PLAIN, 16));
        dateChooser.setBounds(150, 210, 220, 40);
        inputPanel.add(dateChooser);

        JLabel lblQuantity = new JLabel("Quantity:");
        lblQuantity.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblQuantity.setBounds(20, 270, 150, 40);
        inputPanel.add(lblQuantity);
        
        JTextField txtQuantity = new JTextField();
        txtQuantity.setFont(new Font("Tahoma", Font.PLAIN, 16));
        txtQuantity.setBounds(150, 270, 220, 40);
        inputPanel.add(txtQuantity);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnSubmit.setBackground(new Color(0, 123, 255));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setBounds(150, 330, 150, 50);
        inputPanel.add(btnSubmit);

        inputPanel.setBackground(new Color(240, 240, 240));

        // Tăng chiều rộng và điều chỉnh vị trí của tablePanel để lớn hơn
        JPanel tablePanel = new JPanel();
        tablePanel.setBounds(420, 10, 600, 500); // Chiều rộng tăng lên 600
        tablePanel.setLayout(new BorderLayout());
        add(tablePanel);

        JLabel lblTokenTable = new JLabel("Token List");
        lblTokenTable.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTokenTable.setHorizontalAlignment(SwingConstants.CENTER);
        tablePanel.add(lblTokenTable, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Name", "Symbol", "Price", "Date", "Quantity", "Marketcap"}, 0);
        tokenTable = new JTable(tableModel);
        tokenTable.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tokenTable.setRowHeight(25);
        tablePanel.add(new JScrollPane(tokenTable), BorderLayout.CENTER);
        for (Token token : tokenList) {
            tableModel.addRow(new Object[]{
                token.getName(), 
                token.getSymbol(), 
                token.getCurrent_price(),
                token.getDate(),
                token.getQuantity(),
                token.getMarketcap()
            });
        }

        btnSubmit.addActionListener(e -> {
            if (!txtNameCoin.getText().isEmpty() && !txtSymbol.getText().isEmpty() && !txtPrice.getText().isEmpty() && dateChooser.getDate() != null && !txtQuantity.getText().isEmpty()) {
                String name = txtNameCoin.getText();
                String symbol = txtSymbol.getText();
                double price = Double.parseDouble(txtPrice.getText());
                
                Date selectedDate = dateChooser.getDate();
                String date = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
                
                double quantity = Double.parseDouble(txtQuantity.getText());
                double marketcap = price * quantity;

                tokenService.createToken(name, symbol, price, date, marketcap, quantity);

                tableModel.addRow(new Object[]{name, symbol, price, date, quantity, marketcap});

                txtNameCoin.setText("");
                txtSymbol.setText("");
                txtPrice.setText("");
                dateChooser.setDate(null);
                txtQuantity.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
