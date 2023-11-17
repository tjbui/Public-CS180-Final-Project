import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;

public class ServerThread extends Thread {
    private Socket socket;
    private DataManager dm;
    private User currentUser;
    
    private Scanner s;
    private PrintWriter pw;

    public ServerThread(Socket socket, DataManager dm) {
        this.socket = socket;
        this.dm = dm;
        this.currentUser = null;

        try {
            this.s = new Scanner(socket.getInputStream());
            this.pw = new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("Connection error encountered");
        }
    }
    
    public void run() {
        String command = "";

        do {
            command = this.s.nextLine();
        } while (!command.equals("quit"));

        System.out.println("Closing connection...");

        dm.saveToFile();

        try {
            this.socket.close();
        } catch (Exception e) {
            System.out.println("Disconnection error encountered");
        }
    }
}
