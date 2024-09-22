package P2P;

import javax.swing.*;
import java.io.*;

public class PeerFileHandler {
    private PeerConnection connection;
    private PeerUI ui;
    private DataOutputStream dataOut;
    private DataInputStream dataIn;

    public PeerFileHandler(PeerConnection connection, PeerUI ui) {
        this.connection = connection;
        this.ui = ui;
    }

    public void sendFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            ui.appendMessage("Sending file: " + file.getName());
            try (FileInputStream fileIn = new FileInputStream(file)) {
                connection.sendMessage("FILE:" + file.getName());
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileIn.read(buffer)) != -1) {
                    connection.dataOut.write(buffer, 0, bytesRead);
                }
                connection.dataOut.flush();
                ui.appendMessage("File sent successfully.");

                // Gửi thông báo xác nhận sau khi gửi file xong
                connection.sendMessage("File " + file.getName() + " has been sent.");
            } catch (IOException e) {
                e.printStackTrace();
                ui.appendMessage("Error sending file: " + e.getMessage());
            }
        }
    }


    public void handleFileReception(String message) throws IOException {
        String fileName = message.substring(5);
        int option = JOptionPane.showConfirmDialog(null, "Do you want to save the file " + fileName + "?", "Save File", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(fileName));
            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try (FileOutputStream fileOut = new FileOutputStream(fileToSave)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = connection.dataIn.read(buffer)) != -1) {
                        fileOut.write(buffer, 0, bytesRead);
                        if (bytesRead < 4096) break; // Kết thúc khi hết dữ liệu
                    }
                    ui.appendMessage("File saved: " + fileToSave.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                    ui.appendMessage("Error saving file: " + e.getMessage());
                }
            }
        } else {
            ui.appendMessage("File save canceled.");
        }

        // Nhận thông báo từ phía gửi và hiển thị thông báo này
        String confirmationMessage = connection.dataIn.readUTF();
        ui.appendMessage("Peer: " + confirmationMessage);
    }

}
