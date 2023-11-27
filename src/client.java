import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
public class client {
    static Interpreter interpreter;
    public static void main(String[] args) throws InvalidQuantityError, InvalidPriceError {
        String hostname = JOptionPane.showInputDialog(null, "What is the host name of the server you would like to connect to",
                "Host Name", JOptionPane.QUESTION_MESSAGE);
        int port = Integer.parseInt(JOptionPane.showInputDialog(null, "What is the port number of the server you would like to connect to",
                "Host Name", JOptionPane.QUESTION_MESSAGE));
        try {
            Socket socket = new Socket(hostname, port);
            interpreter = new Interpreter(socket);
            initialize();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Invalid host", "Error message",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static final String[] loginOptions = {"Log in", "Sign up", "Exit"};
    public static void initialize() throws InvalidQuantityError, InvalidPriceError {
        // DataManager.logoutCurrentUser();
        String option = (String) JOptionPane.showInputDialog(null, "Choose option",
                "Options", JOptionPane.QUESTION_MESSAGE, null, loginOptions,
                loginOptions[0]);
        switch (option) {
            case "Log in":
                //login();
                break;
            case "Sign up":
                //signup();
                break;
            case "Exit":
                interpreter.save();
                System.exit(0);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid input", "Error message",
                        JOptionPane.ERROR_MESSAGE);
                initialize();
                break;
        }
    }
}