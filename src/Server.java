import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    public Server() {
        //?
    }
    @Override
    public void run() {
        try {
            // Initialize your server socket here, e.g., ServerSocket serverSocket = new ServerSocket(port);
            int port = 12345;
            while (true) {


                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();

                // Create a ServerThread for each client connection
                ServerThread serverThread = new ServerThread(clientSocket, Main3.dataManager);
                serverThread.start();
            }
        } catch (IOException e) {
            // Handle exceptions here, e.g., log the error
            e.printStackTrace();
        } finally {
            // Close resources (e.g., serverSocket) if needed
        }
    }
}
