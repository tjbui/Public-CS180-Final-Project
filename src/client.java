import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
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
//    public static void signup() throws InvalidQuantityError, InvalidPriceError {
//        String email = JOptionPane.showInputDialog(null, "Enter a new email",
//                "Email", JOptionPane.QUESTION_MESSAGE);
//        String password = JOptionPane.showInputDialog(null, "Create a password",
//                "Password", JOptionPane.QUESTION_MESSAGE);
//        String password = scan();
//
//        if (dataManager.getUser(email).getEmail().equals("User not found")) {
//            System.out.println("What kind of account do you want to create?\n" +
//                    "[1] Seller \n[2] Customer\n[3] Back to menu");
//            int input = 0;
//            try { // ADDDED CODE
//                input = Integer.parseInt(scan()); //not int error: RESOLVED
//            } catch (NumberFormatException e) { // ADDED CODE
//                System.out.println("Please either 1, 2, or 3"); // ADDED CODE
//                signup(); // ADDED CODE
//            } // ADDED CODE
//            switch (input) {
//                case 1:
//                    ArrayList<Integer> storeIds = new ArrayList<>();
//                    Seller seller = new Seller(email, password, storeIds);
//                    dataManager.addUser(seller);
//                    dataManager.setCurrentUser(email);
//                    System.out.println("Seller account created and logged in!");
//                    seller();
//                    break;
//                case 2:
//                    ArrayList<Integer> ids = new ArrayList<>();
//                    ArrayList<Integer> quantities = new ArrayList<>();
//                    Customer customer = new Customer(email, password, ids, quantities);
//                    dataManager.addUser(customer);
//                    dataManager.setCurrentUser(email);
//                    System.out.println("Customer account created and logged in!");
//                    customer();
//                    break;
//                case 3:
//                    System.out.println("Account not created as seller or customer was not selected");
//                    initialize();
//                default:
//                    System.out.println("Please either 1, 2, or 3");
//                    signup();
//            }
//        } else {
//            System.out.println("Email already exists. Please use a new email");
//            signup();
//        }
//    }
}