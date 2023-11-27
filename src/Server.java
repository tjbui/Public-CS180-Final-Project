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
            int port = 12345;

            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

          
                ServerThread serverThread = new ServerThread(clientSocket, Main3.dataManager);
                serverThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
