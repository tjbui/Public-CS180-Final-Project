import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            int port = 4242;

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
