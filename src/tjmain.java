import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class tjmain {


    /**
     * Done
     * ?? does it need to check for whether or not the file is valid or will that be thrown in the loadProductsFromFile() method ??
     */
    public static void getProductsFromFile() {
        String filename = JOptionPane.showInputDialog(null, "What is the file name from which you would like to import products?",
                "File Name", JOptionPane.QUESTION_MESSAGE);
        dataManager.loadProductsFromFile(filename);
        System.out.println("Products loaded!");
    }

    /**
     * Done
     * I put the static final String[] right above the method, we can change the way it's organized if we want
     * Also need to add while loop for if they exit or click out of GUI?
     */
    private static final String[] loginOptions = {"Log in", "Sign up",
            "Exit"};
    public static void initialize() throws InvalidQuantityError, InvalidPriceError {
        dataManager.logoutCurrentUser();

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
                dataManager.saveToFile();
                System.exit(0);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid input", "Error message",
                        JOptionPane.ERROR_MESSAGE);
                initialize();
                break;
        }
    }
    public static void login() throws InvalidQuantityError, InvalidPriceError { //TODO
        System.out.println("Enter email:");
        String email = scan();
        System.out.println("Enter password:");
        String password = scan();

        if (dataManager.checkUserLogin(email, password)) {
            dataManager.setCurrentUser(email);
            System.out.println("Login successful");
            if (dataManager.getCurrentUser() instanceof Seller) {
                seller();
            } else {
                customer();
            }
        } else {
            boolean running = true;
            while (running) {
                System.out.println("Wrong email or password");
                System.out.println("[1] Try again\n" +
                        "[2] Back");
                int input = 0;
                try {
                    input = Integer.parseInt(scan());
                } catch (NumberFormatException e) {
                    System.out.println("input is invalid, please input 1 or 2");
                    continue;
                }
                switch (input) {
                    case 1:
                        running = false;
                        login();
                        break;
                    case 2:
                        running = false;
                        initialize();
                        break;
                    default:
                        System.out.println("input is invalid, please input 1 or 2");
                        break;
                }
            }
        }
    }
    public static void signup() throws InvalidQuantityError, InvalidPriceError { //TODO
        System.out.println("Enter a new email:");
        String email = scan();
        System.out.println("Create a password:");
        String password = scan();

        if (dataManager.getUser(email).getEmail().equals("User not found")) {
            System.out.println("What kind of account do you want to create?\n" +
                    "[1] Seller \n[2] Customer\n[3] Back to menu");
            int input = 0;
            try { // ADDDED CODE
                input = Integer.parseInt(scan()); //not int error: RESOLVED
            } catch (NumberFormatException e) { // ADDED CODE
                System.out.println("Please either 1, 2, or 3"); // ADDED CODE
                signup(); // ADDED CODE
            } // ADDED CODE
            switch (input) {
                case 1:
                    ArrayList<Integer> storeIds = new ArrayList<>();
                    Seller seller = new Seller(email, password, storeIds);
                    dataManager.addUser(seller);
                    dataManager.setCurrentUser(email);
                    System.out.println("Seller account created and logged in!");
                    seller();
                    break;
                case 2:
                    ArrayList<Integer> ids = new ArrayList<>();
                    ArrayList<Integer> quantities = new ArrayList<>();
                    Customer customer = new Customer(email, password, ids, quantities);
                    dataManager.addUser(customer);
                    dataManager.setCurrentUser(email);
                    System.out.println("Customer account created and logged in!");
                    customer();
                    break;
                case 3:
                    System.out.println("Account not created as seller or customer was not selected");
                    initialize();
                default:
                    System.out.println("Please either 1, 2, or 3");
                    signup();
            }
        } else {
            System.out.println("Email already exists. Please use a new email");
            signup();
        }
    }
    public static void customer() throws InvalidQuantityError, InvalidPriceError { //TODO
        System.out.println(
                "[1] Go to cart\n" +
                        "[2] Search products\n"+
                        "[3] See purchase history\n" +
                        "[4] Log out\n" +
                        "[5] Export transaction history");
        int option = 0;
        try {
            option = Integer.parseInt(scan());
        } catch (NumberFormatException e) {
            System.out.println("Input invalid: please choose 1, 2, 3, or 4.");
            customer();
        }
        switch (option) {
            case 1:
                seeCart();
                customer();
                break;
            case 2:
                search();
                putInCart();
                customer();
                break;
            case 3:
                seeCustomerPurchaseHistory();
                customer();
                break;
            case 4:
                dataManager.saveToFile();
                initialize();
                break;
            case 5:
                System.out.println("Enter the name of the file to which to export:");

                String filename = scan();

                try {
                    PrintWriter pw = new PrintWriter(new File(filename));

                    ArrayList<Transaction> transactions = dataManager.getPurchaseHistory();

                    for (int i = 0; i < transactions.size(); i++) {
                        pw.println(transactions.get(i).toString());
                    }

                    pw.close();
                } catch (Exception e) {
                    System.out.println("Error occurred during export");
                }
            default:
                System.out.println("Input invalid: please choose 1, 2, 3, or 4.");
                customer();
                break;
        }
    }
    public static void seeCart() throws InvalidQuantityError, InvalidPriceError { //TODO
        if (dataManager.getCurrentUser() instanceof Customer) {
            if (((Customer) dataManager.getCurrentUser()).getIds().isEmpty()) {
                System.out.println("Cart is empty");
                customer();
            } else {
                System.out.println("Items in cart - (quantities): ");
                for (int i = 0; i < ((Customer) dataManager.getCurrentUser()).getIds().size(); i++) {
                    System.out.println(dataManager.getProduct(((Customer) dataManager.getCurrentUser()).getIds().get(i)).getName() + " "
                            + "- (" + ((Customer) dataManager.getCurrentUser()).getQuantities().get(i) + ")");
                    //testing needed
                }
                boolean running;
                do {//fix
                    System.out.println("[1] Buy all\n" +
                            "[2] Delete product from cart\n" +
                            "[3] Back to customer menu");
                    int input = 0;
                    try {
                        input = Integer.parseInt(scan());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input: please input 1, 2, or 3");
                        running = true;
                        continue;
                    }
                    switch (input) {
                        case 1:
                            running = false;
                            for (int i = 0; i < ((Customer) dataManager.getCurrentUser()).getIds().size(); i++) {
                                try {
                                    dataManager.makePurchase(dataManager.getProduct(((Customer) dataManager.getCurrentUser()).getIds().get(i)), (((Customer) dataManager.getCurrentUser()).getQuantities().get(i)));
                                    System.out.println(dataManager.getProduct(((Customer) dataManager.getCurrentUser()).getIds().get(i)).getName() + " purchase complete!");
                                } catch (InvalidQuantityError e) {
                                    System.out.println("Quantity is too high for " + dataManager.getProduct(((Customer) dataManager.getCurrentUser()).getIds().get(i))
                                            + "\n This product cannot be purchased");
                                }
                            }
                            break;
                        case 2:
                            running = false;
                            boolean running2 = true;
                            while (running2) {
                                System.out.println("Which product will you like to delete from cart?");
                                int numberInCart = 0;
                                for (int i = 0; i < ((Customer) dataManager.getCurrentUser()).getIds().size(); i++) {
                                    numberInCart++;
                                    System.out.println("[" + (i + 1) + "] " + dataManager.getProduct(((Customer)
                                            dataManager.getCurrentUser()).getIds().get(i)).toStringFormat());
                                }
                                int option = 0;
                                try {
                                    option = Integer.parseInt(scan()) - 1;
                                } catch (NumberFormatException e) {
                                    System.out.println("Error please input a valid number");
                                    continue;
                                }
                                System.out.println( "numincart" + numberInCart);
                                System.out.println( "pop" + option);

                                if (option < numberInCart && option >= 0) {
                                    ((Customer) dataManager.getCurrentUser()).removeProduct(option);
                                    running2 = false;
                                } else {
                                    System.out.println("Error please input a valid number");
                                }
                            }
                            break;
                        case 3:
                            running = false;
                            customer();
                            break;
                        default:
                            System.out.println("Invalid input: please input 1, 2, or 3");
                            running = true;
                            break;
                    }
                } while (running);
            }
        } else {
            System.out.println("error current user is not a customer. Sorry error has occurred please re-login");
            initialize();
        }
    }
    public static void search() { //TODO
        boolean gotIntInput = false;
        int input = 1;

        do {
            System.out.println("[1] Search by keywords\n" +
                    "[2] Sort all products by Price\n" +
                    "[3] Sort all products by Quantities");
            try {
                input = Integer.parseInt(scan());
                gotIntInput = true;
            } catch (Exception e) {
                System.out.println("Enter an integer input!");
            }
        } while (!gotIntInput);

        switch (input) {
            case 1:
                System.out.println("Input search word(s): ");
                String search = scan();
                if (dataManager.search(search).isEmpty()) {
                    System.out.println("No results from search");
                } else {
                    System.out.println("Results:");
                    for (int i = 0; i < dataManager.search(search).size(); i++) {
                        System.out.println(dataManager.search(search).get(i).getName());
                    }
                }
                break;
            case 2:
                System.out.println("[1] Search by ascending prices\n" +
                        "[2] Search by descending prices");
                int option = 0;
                try {
                    option = Integer.parseInt(scan());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input");
                    search();
                }
                switch (option) {
                    case 1:
                        if (dataManager.getProductList().isEmpty()) {
                            System.out.println("No products listed");
                        } else {
                            for (int i = 0; i < dataManager.getProductList().size(); i++) {
                                System.out.println(dataManager.getProductList(DataManager.BY_PRICE, DataManager.SORTED_ASC).get(i).getName());
                            }
                        }
                        break;
                    case 2:
                        if (dataManager.getProductList().isEmpty()) {
                            System.out.println("No products listed");
                        } else {
                            for (int i = 0; i < dataManager.getProductList().size(); i++) {
                                System.out.println(dataManager.getProductList(DataManager.BY_PRICE, DataManager.SORTED_DESC).get(i).getName());
                            }
                        }
                        break;
                    default:
                        System.out.println("Invalid input");
                        search();
                        break;
                }
                break;

            case 3:
                System.out.println("[1] Search by ascending quantity\n" +
                        "[2] Search by descending quantity");
                int option2 = 0;
                try {
                    option2 = Integer.parseInt(scan());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input");
                    search();
                }
                switch (option2) {
                    case 1:
                        if (dataManager.getProductList().isEmpty()) {
                            System.out.println("No products listed");
                        } else {
                            for (int i = 0; i < dataManager.getProductList().size(); i++) {
                                System.out.println(dataManager.getProductList(DataManager.BY_QUANTITY, DataManager.SORTED_ASC).get(i).getName());
                            }
                        }
                        break;
                    case 2:
                        if (dataManager.getProductList().isEmpty()) {
                            System.out.println("No products listed");
                        } else {
                            for (int i = 0; i < dataManager.getProductList().size(); i++) {
                                System.out.println(dataManager.getProductList(DataManager.BY_QUANTITY, DataManager.SORTED_DESC).get(i).getName());
                            }
                        }
                        break;
                    default:
                        System.out.println("Invalid input");
                        search();
                        break;
                }
                break;
            default:
                System.out.println("Invalid input, please input 1, 2, or 3");
                search();
                break;
        }
    }
}