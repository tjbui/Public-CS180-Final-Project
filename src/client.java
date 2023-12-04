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
     *
     * checkUserLogin() doesn't return anything ??? Tried debugging but not sure how the implementation works **RESOLVED**
     * works**
     */
    private static final String[] tryAgainOptions = {"Try Again", "Back"};
    public static void login() throws InvalidQuantityError, InvalidPriceError {
        String email = JOptionPane.showInputDialog(null, "Enter email",
                "Email", JOptionPane.QUESTION_MESSAGE);
        String password = JOptionPane.showInputDialog(null, "Enter password",
                "Password", JOptionPane.QUESTION_MESSAGE);
        if (interpreter.checkUserLogin(email, password)) {
            interpreter.checkUserLogin(email, password);
            JOptionPane.showMessageDialog(null, "Login successful!", "successful message",
                    JOptionPane.INFORMATION_MESSAGE);
            if (interpreter.getUser(email) instanceof Seller) {
                //seller();
            } else {
                //customer();
            }
        } else {
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
     * Problem with interpreter.getUser(email): does not return anything **RESOLVED**
     */
    private static final String[] accountCreateOptions = {"Seller", "Customer", "Back"};
    public static void signup() throws InvalidQuantityError, InvalidPriceError {
        String email = JOptionPane.showInputDialog(null, "Enter a new email",
                "Email", JOptionPane.QUESTION_MESSAGE);
        String password = JOptionPane.showInputDialog(null, "Create a password",
                "Password", JOptionPane.QUESTION_MESSAGE);

        System.out.println(interpreter.getUser(email)); // doesn't return anything. it just keeps running infinitely. need to debug getUser() ???? **RESOLVED**

        if (interpreter.getUser(email).getEmail().equals("User not found")) {
            String option = (String) JOptionPane.showInputDialog(null, "Choose option",
                    "Options", JOptionPane.QUESTION_MESSAGE, null, accountCreateOptions,
                    accountCreateOptions[0]);
            switch (option) {
                case "Seller":
                    ArrayList<Integer> storeIds = new ArrayList<>();
                    Seller seller = new Seller(email, password, storeIds);
                    interpreter.addUser(seller);
                    interpreter.checkUserLogin(email, password); // supposed to be setCurrentUser() but there is no method **RESOLVED**
                    JOptionPane.showMessageDialog(null, "Seller account created and logged in!", "successful message",
                            JOptionPane.INFORMATION_MESSAGE);
                    //seller();
                    break;
                case "Customer":
                    ArrayList<Integer> ids = new ArrayList<>();
                    ArrayList<Integer> quantities = new ArrayList<>();
                    Customer customer = new Customer(email, password, ids, quantities);
                    interpreter.addUser(customer);
                    interpreter.checkUserLogin(email, password); // supposed to be setCurrentUser() but there is no method **RESOLVED**
                    JOptionPane.showMessageDialog(null, "Customer account created and logged in!", "successful message",
                            JOptionPane.INFORMATION_MESSAGE);
                    customer();
                    break;
                case "Back":
                    JOptionPane.showMessageDialog(null, "Account not created as seller or customer was not selected", "Back message",
                            JOptionPane.INFORMATION_MESSAGE);
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
                seeCart();
                customer();
                break;
            case "Search products":
                search();
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

    /**
     * Untested (needs other methods for testing)
     * delete products to do, idk how to implement that ill figure it out
     * @throws InvalidQuantityError
     * @throws InvalidPriceError
     */
    private static final String[] cartOptions = {"Buy all", "Delete product from cart","Back to customer menu"};
    public static void seeCart() throws InvalidQuantityError, InvalidPriceError {
        if (interpreter.getCurrentUser() instanceof Customer) { //doesn't work for some reason so it always thinks not costumer ??
            if (((Customer) interpreter.getCurrentUser()).getIds().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Cart is empty", "Empty cart message",
                        JOptionPane.INFORMATION_MESSAGE);
                customer();
            } else {
                JOptionPane.showMessageDialog(null, "Items in cart - (quantities): ", "Cart message",
                        JOptionPane.INFORMATION_MESSAGE);
                for (int i = 0; i < ((Customer) interpreter.getCurrentUser()).getIds().size(); i++) {
                    JOptionPane.showMessageDialog(null, interpreter.getProduct(((Customer) interpreter.getCurrentUser()).getIds().get(i)).getName() + " "
                                    + "- (" + ((Customer) interpreter.getCurrentUser()).getQuantities().get(i) + ")", "Cart message",
                            JOptionPane.INFORMATION_MESSAGE);
                    // ****** TESTING NEEDED ******
                }
                boolean running;
                do {//fix
                    String option = (String) JOptionPane.showInputDialog(null, "Choose option",
                            "Options", JOptionPane.QUESTION_MESSAGE, null, cartOptions,
                            cartOptions[0]);
                    switch (option) {
                        case "Buy all":
                            running = false;
                            for (int i = 0; i < ((Customer) interpreter.getCurrentUser()).getIds().size(); i++) {
                                try {
                                    interpreter.makePurchase(interpreter.getProduct(((Customer) interpreter.getCurrentUser()).getIds().get(i)), (((Customer) interpreter.getCurrentUser()).getQuantities().get(i)));
                                    String mes = interpreter.getProduct(((Customer) interpreter.getCurrentUser()).getIds().get(i)).getName() + " purchase complete!";
                                    JOptionPane.showMessageDialog(null, mes, "Cart message",
                                            JOptionPane.INFORMATION_MESSAGE);
                                } catch (InvalidQuantityError e) {
                                    String mes = ("Quantity is too high for " + interpreter.getProduct(((Customer) interpreter.getCurrentUser()).getIds().get(i))
                                            + "\n This product cannot be purchased");
                                    JOptionPane.showMessageDialog(null, mes, "Quantity too high message",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            break;
                            // NOT TESTED
                        case "Delete product from cart":
                            running = false;
                            int numberInCart = 0;
                            for (int i = 0; i < ((Customer) interpreter.getCurrentUser()).getIds().size(); i++) {
                                numberInCart++;
                            }
                            String[] products = new String[numberInCart];
                            for (int i = 0; i < ((Customer) interpreter.getCurrentUser()).getIds().size(); i++) {
                                products[i] = interpreter.getProduct(((Customer)
                                        interpreter.getCurrentUser()).getIds().get(i)).toStringFormat();
                            }
                            String product = (String) JOptionPane.showInputDialog(null, "Which product will you like to delete from cart?",
                                    "Options", JOptionPane.QUESTION_MESSAGE, null, products,
                                    products[0]);
                            int indexOfProduct = 0;
                            for (int i = 0; i < ((Customer) interpreter.getCurrentUser()).getIds().size(); i++) {
                                if ((interpreter.getProduct(((Customer) interpreter.getCurrentUser()).getIds().get(i)).toStringFormat()).equals(product)) {
                                    indexOfProduct = i;
                                }
                            }
                            ((Customer) interpreter.getCurrentUser()).removeProduct(indexOfProduct);
                            break;
                        case "Back to customer menu":
                            running = false;
                            customer();
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Invalid input", "Error message",
                                    JOptionPane.ERROR_MESSAGE);
                            running = true;
                            break;
                    }
                } while (running);
            }
        } else {
            JOptionPane.showMessageDialog(null, "error current user is not a customer. Sorry error has occurred please re-login",
                    "Error message", JOptionPane.ERROR_MESSAGE);
            initialize();
        }
    }

    /**
     * not tested
     */
    private static final String[] searchOptions = {"Search by keywords", "Sort all products by Price","Sort all products by Quantities"};
    private static final String[] priceSortOptions = {"Search by ascending price", "Search by descending prices"};
    private static final String[] quantitySortOptions = {"Search by ascending price", "Search by descending prices"};
    public static void search() {
        String option = (String) JOptionPane.showInputDialog(null, "Choose option",
                "Options", JOptionPane.QUESTION_MESSAGE, null, searchOptions,
                searchOptions[0]);
        switch (option) {
            case "Search by keywords":
                String search = JOptionPane.showInputDialog(null, "Input search word(s)",
                        "Email", JOptionPane.QUESTION_MESSAGE);
                if (interpreter.search(search).isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No results from search", "Search message",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String message = "Results:\n";
                    for (int i = 0; i < interpreter.search(search).size(); i++) {
                        message += interpreter.search(search).get(i).getName() + "\n";
                    }
                    JOptionPane.showMessageDialog(null, message, "Search message",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                break;
            case "Sort all products by Price":
                String sortPriceOption = (String) JOptionPane.showInputDialog(null, "Choose option",
                        "Options", JOptionPane.QUESTION_MESSAGE, null, priceSortOptions,
                        priceSortOptions[0]);
                switch (sortPriceOption) {
                    case "Search by ascending price":
                        if (interpreter.getProductList(0,0).isEmpty()) { //Not sure about getProductList() arguments
                            JOptionPane.showMessageDialog(null, "No Products listed", "Search message",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            for (int i = 0; i < interpreter.getProductList(0,0).size(); i++) {
                                System.out.println(interpreter.getProductList(DataManager.BY_PRICE, DataManager.SORTED_ASC).get(i).getName());
                            }
                        }
                        break;
                    case "Search by descending prices":
                        if (interpreter.getProductList(0,0).isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No Products listed", "Search message",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            String mes = "";
                            for (int i = 0; i < interpreter.getProductList(0,0).size(); i++) {
                                mes += (interpreter.getProductList(DataManager.BY_PRICE, DataManager.SORTED_DESC).get(i).getName()); // idk if this will work
                            }
                            JOptionPane.showMessageDialog(null, mes, "Search message",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid input",
                                "Invalid input error message", JOptionPane.ERROR_MESSAGE);
                        search();
                        break;
                }
                break;

            case "Sort all products by Quantities":
                String sortQuantityOption = (String) JOptionPane.showInputDialog(null, "Choose option",
                        "Options", JOptionPane.QUESTION_MESSAGE, null, quantitySortOptions,
                        quantitySortOptions[0]);
                switch (sortQuantityOption) {
                    case "Search by ascending price":
                        if (interpreter.getProductList(0,0).isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No Products listed", "Search message",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            String mes = "";
                            for (int i = 0; i < interpreter.getProductList(0,0).size(); i++) {
                                mes += (interpreter.getProductList(DataManager.BY_QUANTITY, DataManager.SORTED_ASC).get(i).getName());
                            }
                            JOptionPane.showMessageDialog(null, mes, "Search message",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    case "Search by descending prices":
                        if (interpreter.getProductList(0,0).isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No Products listed", "Search message",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            String mes = "";
                            for (int i = 0; i < interpreter.getProductList(0,0).size(); i++) {
                                mes += (interpreter.getProductList(DataManager.BY_QUANTITY, DataManager.SORTED_DESC).get(i).getName());
                            }
                            JOptionPane.showMessageDialog(null, mes, "Search message",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid input",
                                "Error message", JOptionPane.ERROR_MESSAGE);
                        search();
                        break;
                }
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid input",
                        "Error message", JOptionPane.ERROR_MESSAGE);
                search();
                break;
        }
    }

    /**
     * Not tested
     */
    public static void getProductsFromFile() {
        String fileName = JOptionPane.showInputDialog(null, "What is the file name from which you would like to import products?",
                "File name", JOptionPane.QUESTION_MESSAGE);
        interpreter.loadProducts(fileName);
        JOptionPane.showMessageDialog(null, "Products loaded!", "Products loaded message",
                JOptionPane.INFORMATION_MESSAGE);
    }
}