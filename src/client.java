import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.io.*;

public class client {
    static Interpreter interpreter;

    /**
     * The main method takes the hostname and port number, then initializes the static Interpreter with the new socket. Then it runs the initialize() method
     *
     */
    public static void main(String[] args) throws InvalidQuantityError, InvalidPriceError {
        String hostname = JOptionPane.showInputDialog(null, "What is the host name of the server you would like to connect to",
                "Host Name", JOptionPane.QUESTION_MESSAGE);
        int port = Integer.parseInt(JOptionPane.showInputDialog(null, "What is the port number of the server you would like to connect to",
                "Host Name", JOptionPane.QUESTION_MESSAGE));
        try {
            Socket socket = new Socket(hostname, port);
            interpreter = new Interpreter(socket);
            JOptionPane.showMessageDialog(null, "Connection successful!", "successful message",
                    JOptionPane.INFORMATION_MESSAGE);
            initialize();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Invalid host", "Error message",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * ??? DOES IT NEED TO LOGOUT CURRENT USER ??? ~ commented out datamanger method since it doesn't exist.
     * There is no logout method in Interpreter so I'm not sure
     */
    private static final String[] loginOptions = {"Log in", "Sign up", "Exit"};
    public static void initialize() throws InvalidQuantityError, InvalidPriceError {
        // DataManager.logoutCurrentUser();
        String option = (String) JOptionPane.showInputDialog(null, "Choose option",
                "Options", JOptionPane.QUESTION_MESSAGE, null, loginOptions,
                loginOptions[0]);
        switch (option) {
            case "Log in":
                login();
                break;
            case "Sign up":
                signup();
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
    /**
     * LOGIN DOESN'T WORK:
     *      - checkUserLogin() doesn't return anything ??? Tried debugging but not sure how the implementation works
     */
    private static final String[] tryAgainOptions = {"Try Again", "Back"};
    public static void login() throws InvalidQuantityError, InvalidPriceError {
        String email = JOptionPane.showInputDialog(null, "Enter email",
                "Email", JOptionPane.QUESTION_MESSAGE);
        String password = JOptionPane.showInputDialog(null, "Enter password",
                "Password", JOptionPane.QUESTION_MESSAGE);
        //System.out.println(interpreter.checkUserLogin(email, password));
        if (interpreter.checkUserLogin(email, password)) {
            //System.out.println("test");
            interpreter.editCurrentUser(email, password);
            JOptionPane.showMessageDialog(null, "Login successful!", "successful message",
                    JOptionPane.INFORMATION_MESSAGE);
            if (interpreter.getUser(email) instanceof Seller) {
                //seller();
            } else {
                //customer();
            }
        } else {
            //System.out.println("test 2");
            boolean running = true;
            while (running) {
                JOptionPane.showMessageDialog(null, "Wrong email or password", "Wrong login message",
                        JOptionPane.ERROR_MESSAGE);
                String option = (String) JOptionPane.showInputDialog(null, "Choose option",
                        "Options", JOptionPane.QUESTION_MESSAGE, null, tryAgainOptions,
                        tryAgainOptions[0]);
                switch (option) {
                    case "Try Again":
                        running = false;
                        login();
                        break;
                    case "Back":
                        running = false;
                        initialize();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid input", "Error message",
                                JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }
        }
    }

    /**
     * Problem with interpreter.getUser(email): does not return anything
     */
    private static final String[] accountCreateOptions = {"Seller", "Customer", "Back"};
    public static void signup() throws InvalidQuantityError, InvalidPriceError {
        String email = JOptionPane.showInputDialog(null, "Enter a new email",
                "Email", JOptionPane.QUESTION_MESSAGE);
        String password = JOptionPane.showInputDialog(null, "Create a password",
                "Password", JOptionPane.QUESTION_MESSAGE);

        System.out.println(interpreter.getUser(email)); // doesn't return anything. it just keeps running infinitely. need to debug getUser() ????

        if (interpreter.getUser(email).getEmail().equals("User not found")) {
            String option = (String) JOptionPane.showInputDialog(null, "Choose option",
                    "Options", JOptionPane.QUESTION_MESSAGE, null, accountCreateOptions,
                    accountCreateOptions[0]);
            switch (option) {
                case "Seller":
                    ArrayList<Integer> storeIds = new ArrayList<>();
                    Seller seller = new Seller(email, password, storeIds);
                    interpreter.addUser(seller);
                    interpreter.editCurrentUser(email, password); // supposed to be setCurrentUser() but there is no method
                    JOptionPane.showMessageDialog(null, "Seller account created and logged in!", "successful message",
                            JOptionPane.INFORMATION_MESSAGE);
                    //seller();
                    break;
                case "Customer":
                    ArrayList<Integer> ids = new ArrayList<>();
                    ArrayList<Integer> quantities = new ArrayList<>();
                    Customer customer = new Customer(email, password, ids, quantities);
                    interpreter.addUser(customer);
                    interpreter.editCurrentUser(email, password); // supposed to be setCurrentUser() but there is no method
                    JOptionPane.showMessageDialog(null, "Customer account created and logged in!", "successful message",
                            JOptionPane.INFORMATION_MESSAGE);
                    //customer();
                    customer();
                    break;
                case "Back":
                    JOptionPane.showMessageDialog(null, "Account not created as seller or customer was not selected", "Error message",
                            JOptionPane.ERROR_MESSAGE);
                    initialize();
                default:
                    JOptionPane.showMessageDialog(null, "Invalid input", "Error message",
                            JOptionPane.ERROR_MESSAGE);
                    signup();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Email already exists. Try again", "Email exists message",
                    JOptionPane.ERROR_MESSAGE);
            signup();
        }
    }

    /**
     * Not tested
     */
    private static final String[] customerOptions = {"Go to cart", "Search products", "See purchase history", "Log out", "Export transaction history"};
    public static void customer() throws InvalidQuantityError, InvalidPriceError {
        String option = (String) JOptionPane.showInputDialog(null, "Choose option",
                "Options", JOptionPane.QUESTION_MESSAGE, null, customerOptions,
                customerOptions[0]);
        switch (option) {
            case "Go to cart":
                //seeCart();
                customer();
                break;
            case "Search products":
                //search();
                //putInCart();
                customer();
                break;
            case "See purchase history":
                //seeCustomerPurchaseHistory();
                customer();
                break;
            case "Log out":
                interpreter.save();
                initialize();
                break;
            case "Export transaction history":
                String filename = JOptionPane.showInputDialog(null, "Enter the name of the file to which to export:",
                        "File", JOptionPane.QUESTION_MESSAGE);
                try {
                    PrintWriter pw = new PrintWriter(new File(filename));

                    ArrayList<Transaction> transactions = interpreter.getPurchaseHistory();

                    for (int i = 0; i < transactions.size(); i++) {
                        pw.println(transactions.get(i).toString());
                    }

                    pw.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error occured during export", "Export error message",
                            JOptionPane.ERROR_MESSAGE);
                }
            default:
                JOptionPane.showMessageDialog(null, "Invalid input", "Invalid input error message",
                        JOptionPane.ERROR_MESSAGE);
                customer();
                break;
        }
    }
}