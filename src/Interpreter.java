import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;

public class Interpreter {
    private Socket socket;

    private Scanner s;
    private PrintWriter pw;

    public Interpreter(Socket socket) {
        this.socket = socket;

        try {
            this.s = new Scanner(socket.getInputStream());
            this.pw = new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("Connection error encountered");
        }
    }

    public void close() {
        this.pw.println("quit");
        try {
            this.socket.close();
        } catch (Exception e) {
            System.out.println("Disconnection error encountered");
        }
    }

    public void save() {
        this.pw.println("save");
    }
}
