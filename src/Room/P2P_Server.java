package Room;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

//172.20.10.4
 class P2P_Server {
    private static final int PORT = 12345;
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Chat server is running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
        	int count= 0;
            while (true) {
            	count = count +1;
                new ClientHandler(serverSocket.accept()).start();
                System.out.println("Client số  " + count+  " vừa vào cuộc trò chuyện");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // Get input/output streams
                InputStream input = socket.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                OutputStream output = socket.getOutputStream();
                out = new PrintWriter(output, true);

                // Add client writer to set
                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                // Broadcast messages to all clients
                String message;
                while ((message = in.readLine()) != null) {
                    synchronized (clientWriters) {
                        for (PrintWriter writer : clientWriters) {
                            writer.println(message);
                        }
                    }
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("Client disconnected: " + e.getMessage());
            } finally {
                // Remove client writer when done
                synchronized (clientWriters) {
                    clientWriters.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
