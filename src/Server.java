import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            // Initialize your server socket here, e.g., ServerSocket serverSocket = new ServerSocket(port);
            int port = 12345;

            ServerSocket serverSocket = new ServerSocket(port);
            DataManager dm = new DataManager();

            while (true) {
                Socket clientSocket = serverSocket.accept();

                // Create a ServerThread for each client connection
                ServerThread serverThread = new ServerThread(clientSocket, dm);
                serverThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
