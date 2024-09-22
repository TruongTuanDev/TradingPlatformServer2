package Auth;

import javax.swing.*;

import P2P.PeerUI;
import Room.P2P_GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainUI {
    private JFrame frame;
    private JButton chatRoomButton;
    private JButton privateChatButton;

    public MainUI() {
       
        frame = new JFrame("Chat Application");
        frame.setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

     
        chatRoomButton = new JButton("Join Chat Room");
        privateChatButton = new JButton("Start Private Chat");


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        frame.add(chatRoomButton, gbc);

        
        gbc.gridy = 1;
        frame.add(privateChatButton, gbc);

        
        frame.setVisible(true);

        
        chatRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openChatRoom();
            }
        });

        privateChatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startPrivateChat();
            }
        });
    }

    private void openChatRoom() {
    	SwingUtilities.invokeLater(() -> new P2P_GUI().createAndShowGUI());
        frame.dispose(); 
    }

    private void startPrivateChat() {
    	SwingUtilities.invokeLater(() -> new PeerUI());
        frame.dispose(); // Đóng cửa sổ hiện tại 
    }

    public static void main(String[] args) {
        
        new MainUI();
    }
}
