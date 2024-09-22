package P2P;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class PeerConnection {
    private Socket socket;
    private ServerSocket serverSocket;
    DataInputStream dataIn;
    DataOutputStream dataOut;
    private final int PORT = 12345;
    private JTextArea chatArea;
    private PeerFileHandler fileHandler;
    private PeerUI ui;

    public PeerConnection(JTextArea chatArea, PeerUI ui) {
        this.chatArea = chatArea;
        this.ui = ui;
        fileHandler = new PeerFileHandler(this, ui);
    }

    public void listenForConnection() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                ui.appendMessage("Listening for connection on port " + PORT + "...");
                socket = serverSocket.accept();
                ui.appendMessage("Peer connected!");

                initializeStreams();
                listenForMessages();
            } catch (IOException ex) {
                ex.printStackTrace();
                ui.appendMessage("Error: " + ex.getMessage());
            }
        }).start();
    }

    public void setupConnection(String ipAddress) {
        new Thread(() -> {
            try {
                if (!ipAddress.isEmpty()) {
                    ui.appendMessage("Attempting to connect to peer...");
                    socket = new Socket(ipAddress, PORT);
                    ui.appendMessage("Connected to peer.");

                    initializeStreams();
                    listenForMessages();
                } else {
                    ui.appendMessage("Please enter a valid IP address.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                ui.appendMessage("Connection failed: " + ex.getMessage());
            }
        }).start();
    }

    private void initializeStreams() throws IOException {
        dataIn = new DataInputStream(socket.getInputStream());
        dataOut = new DataOutputStream(socket.getOutputStream());
    }

    private void listenForMessages() {
        new Thread(() -> {
            try {
                String message;
                while ((message = dataIn.readUTF()) != null) {
                    if (message.startsWith("FILE:")) {
                        fileHandler.handleFileReception(message);
                    } else {
                        ui.appendMessage("Peer: " + message);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                ui.appendMessage("Error while receiving message: " + ex.getMessage());
            }
        }).start();
    }

    public void sendMessage(String message) {
        try {
            dataOut.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
            ui.appendMessage("Error sending message: " + e.getMessage());
        }
    }

    public void sendFile() {
        fileHandler.sendFile();
    }

    public void closeResources() {
        try {
            if (dataIn != null) dataIn.close();
            if (dataOut != null) dataOut.close();
            if (socket != null) socket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
