import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class client {
    static Interpreter interpreter;

    /**
     * The main method takes the hostname and port number, then initializes the static Interpreter with the new socket. Then it runs the initialize() method
     *
     */
    public static void main(String[] args) throws InvalidQuantityError, InvalidPriceError {
        try {
            String hostname = JOptionPane.showInputDialog(null, "What is the host name of the server you would like to connect to",
                    "Host Name", JOptionPane.QUESTION_MESSAGE);
            int port = Integer.parseInt(JOptionPane.showInputDialog(null, "What is the port number of the server you would like to connect to",
                    "Host Name", JOptionPane.QUESTION_MESSAGE));
            Socket socket = new Socket(hostname, port);
            interpreter = new Interpreter(socket);
            JOptionPane.showMessageDialog(null, "Connection successful!", "successful message",
                    JOptionPane.INFORMATION_MESSAGE);
            initialize();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Invalid host", "Error message",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid host", "Error message",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * ??? DOES IT NEED TO LOGOUT CURRENT USER ??? ~ commented out datamanger method since it doesn't exist.
     * There is no logout method in Interpreter so I'm not sure
     */
    private static final String[] loginOptions = {"Log in", "Sign up", "Exit"};
    private static final String[] sellerOptions = {"Stores Options", "View Data", "Import products from a File", "Export products to file", "Log Out"};
    public static void initialize() throws InvalidQuantityError, InvalidPriceError {
        interpreter.logout();
        String option = (String) JOptionPane.showInputDialog(null, "Choose option",
                "Options", JOptionPane.QUESTION_MESSAGE, null, loginOptions,
                loginOptions[0]);
        try {
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
        } catch (NullPointerException e) {
            System.out.println("User canceled");
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
                seller();
            } else {
                customer();
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
     *
     */
    private static final String[] accountCreateOptions = {"Seller", "Customer", "Back"};
    public static void signup() throws InvalidQuantityError, InvalidPriceError {
        String email = JOptionPane.showInputDialog(null, "Enter a new email",
                "Email", JOptionPane.QUESTION_MESSAGE);
        String password = JOptionPane.showInputDialog(null, "Create a password",
                "Password", JOptionPane.QUESTION_MESSAGE);
        if (email.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(null, "Email or password is empty. Please try again", "Email empty message",
                    JOptionPane.ERROR_MESSAGE);
            signup();
        } else if (interpreter.getUser(email).getEmail().equals("User not found")) {
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
                    seller();
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
        try { // Catches nullpointerexception and saves if user closes gui
            switch (option) {
                case "Go to cart":
                    seeCart();
                    customer();
                    break;
                case "Search products":
                    search();
                    putInCart();
                    customer();
                    break;
                case "See purchase history":
                    seeCustomerPurchaseHistory();
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
                        interpreter.exportPurchases(filename);
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
        } catch (NullPointerException e) {
            interpreter.save();
            System.out.println("User closed GUI");
        }
    }

    /**
     * Untested (needs other methods for testing)
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
                                    interpreter.emptyCart();
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
                            String mes = "";
                            for (int i = 0; i < interpreter.getProductList(0,0).size(); i++) {
                                mes += interpreter.getProductList(DataManager.BY_PRICE, DataManager.SORTED_ASC).get(i).getName();
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

    public static void seeCustomerPurchaseHistory() {
        String purchaseHistory = "";

        ArrayList<Transaction> history = interpreter.getPurchaseHistory();

        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have no past transactions", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            for (int i = 0; i < history.size(); i++) {
                purchaseHistory += interpreter.formatTransaction(history.get(i));
                purchaseHistory += "\n";
            }
            JOptionPane.showMessageDialog(null, purchaseHistory,"Purchase History",
                    JOptionPane.INFORMATION_MESSAGE);

        }
    }
    public static void seller() throws InvalidQuantityError, InvalidPriceError {
        boolean running = true;
        try { // Try to catch nullpointerexception if user closes gui
            while (running) {
                String option = (String) JOptionPane.showInputDialog(null, "Choose option",
                        "Options", JOptionPane.QUESTION_MESSAGE, null, sellerOptions,
                        sellerOptions[0]);
                switch (option) {
                    case "Stores Options":
                        storeOptions();
                        seller();
                        running = false;
                        break;
                    case "View Data":
                        String[] viewDataOptions = {"View popular product data", "View Store Sales"};
                        String viewDataOption = (String) JOptionPane.showInputDialog(null, "Choose option",
                                "Options", JOptionPane.QUESTION_MESSAGE, null, viewDataOptions,
                                viewDataOptions[0]);
                        switch (viewDataOption) {
                            case "View popular product data":
                                productData();
                                break;
                            case "View Store Sales":
                                storeSalesData();
                                break;
                        }
                        seller();
                        running = false;
                        break;
                    case "Import products from a File":
                        getProductsFromFile();
                        seller();
                        break;
                    case "Export products to file":
                        String filename = JOptionPane.showInputDialog(null, "Enter the name of the file to which to export",
                                "File Name", JOptionPane.QUESTION_MESSAGE);
                        try {
                            PrintWriter pw = new PrintWriter(new File(filename));
                            ArrayList<Product> products = interpreter.getProductList(0, 0);
                            for (int i = 0; i < products.size(); i++) {
                                Product p = products.get(i);


                                if (interpreter.getStore(p.getStoreId()).getSellerEmail().equals(interpreter.getCurrentUser().getEmail())) { //TODO
                                    pw.println(p.toStringFormat());
                                }
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Export failed", "Error message", JOptionPane.ERROR_MESSAGE);
                        }
                    case "Log Out":
                        running = false;
                        interpreter.save();
                        initialize();
                        break;
                }
            }
        } catch (NullPointerException e) {
            interpreter.save();
            System.out.println("User closed GUI");
        }
    }


    public static void storeOptions() throws InvalidQuantityError, InvalidPriceError {
        String[] viewStoreOptions = {"Create new store", "Edit/Delete store", "Back to seller menu"};


        String storeOption = (String) JOptionPane.showInputDialog(null, "Choose option",
                "Options", JOptionPane.QUESTION_MESSAGE, null, viewStoreOptions,
                viewStoreOptions[0]);


        switch (storeOption) {
            case "Create new store":

                String storeName = JOptionPane.showInputDialog(null, "Create store name",
                        "Store Name", JOptionPane.QUESTION_MESSAGE);
                if (storeName.equals("") || storeName.isEmpty()) {
                    storeOptions();
                    break;
                }

                ArrayList<Integer> empty = new ArrayList<>();
                interpreter.addStore(new Store(empty, storeName, interpreter.getCurrentUser().getEmail(), interpreter.getCurrentStoreId())); //TODO
                interpreter.incrementCurrentStoreId();
                JOptionPane.showMessageDialog(null, "Store has been created. If you would like to add products go to edit store", "successful message",
                        JOptionPane.INFORMATION_MESSAGE);
                seller();
                break;


            case "Edit/Delete store":
                String[] viewStoreNames = new String[interpreter.getOwnedStores().size()]; //TODO
                ArrayList<String> storeNamesArrayList = new ArrayList<String>();

                for (int i = 0; i < viewStoreNames.length; i++) {
                    viewStoreNames[i] = interpreter.getOwnedStores().get(i).getName();
                }

                for (int q = 0; q < viewStoreNames.length; q++) {
                    storeNamesArrayList.add(viewStoreNames[q]);
                }




                String storeNameForConversion = (String) JOptionPane.showInputDialog(null, "Choose option",
                        "Options", JOptionPane.QUESTION_MESSAGE, null, viewStoreNames,
                        viewStoreNames[0]);


                editStore(storeNamesArrayList.indexOf(storeNameForConversion));
                //editStore(Arrays.binarySearch(viewStoreNames, storeNameForConversion));

                break;


            case "Back to seller menu":
                seller();
                break;


        }


    }


    public static void editStore(int indexOfStore) throws InvalidPriceError, InvalidQuantityError {
        Store store = interpreter.getOwnedStores().get(indexOfStore);
        int storeId = store.getId();


        String[] viewEditStoreOptions = {"Change store name", "Add new product", "Delete product", "Delete store", "Edit product"};


        String editStoreOption = (String) JOptionPane.showInputDialog(null, "Choose option",
                "Options", JOptionPane.QUESTION_MESSAGE, null, viewEditStoreOptions,
                viewEditStoreOptions[0]);

        switch (editStoreOption) {
            case "Change store name":
                String storeName = JOptionPane.showInputDialog(null, "Enter new store name:",
                        "Store Name", JOptionPane.QUESTION_MESSAGE);

                if (storeName.equals("") || storeName.isEmpty()) {
                    editStore(indexOfStore);
                    break;
                }
                interpreter.editStore(storeId, storeName);
                break;

            case "Add new product":
                try {
                    String productName = JOptionPane.showInputDialog(null, "Name of product",
                            "Edit Store", JOptionPane.QUESTION_MESSAGE);

                    if (productName.equals("") || productName.isEmpty()) {
                        editStore(indexOfStore);
                        break;
                    }
                    String description = JOptionPane.showInputDialog(null, "Description of product",
                            "Edit Store", JOptionPane.QUESTION_MESSAGE);

                    if (description.equals("") || description.isEmpty()) {
                        editStore(indexOfStore);
                        break;
                    }

                    String quantityString = JOptionPane.showInputDialog(null, "Quantity in stock",
                            "Edit Store", JOptionPane.QUESTION_MESSAGE);
                    int quantity = 1;

                    do {
                        try {
                            quantity = Integer.parseInt(quantityString);

                            if (quantity >= 0) {
                                break;
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Invalid input", "Error message", JOptionPane.ERROR_MESSAGE);
                        }

                        quantityString = JOptionPane.showInputDialog(null, "Enter an integer greater than or equal to 0:",
                            "Edit Store", JOptionPane.QUESTION_MESSAGE);
                    } while (true);

                    String priceProductString = JOptionPane.showInputDialog(null, "Price of product",
                            "Edit Store", JOptionPane.QUESTION_MESSAGE);
                    double priceProduct = 1.1;

                    do {
                        try {
                            priceProduct = Double.parseDouble(priceProductString);

                            if (priceProduct > 0.0) {
                                break;
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Invalid input", "Error message", JOptionPane.ERROR_MESSAGE);
                        }

                        priceProductString = JOptionPane.showInputDialog(null, "Enter a decimal greater than 0:",
                            "Edit Store", JOptionPane.QUESTION_MESSAGE);
                    } while (true);

                    Product product = new Product(productName, description, storeId, quantity, priceProduct, interpreter.getCurrentProductId());
                    interpreter.incrementCurrentProductId();
                    interpreter.addProduct(product);
                    store = interpreter.getStore(storeId);
                    System.out.println(store.toStringFormat());
                    JOptionPane.showMessageDialog(null, "Product added!", "successful message",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (InvalidPriceError e) {
                    JOptionPane.showMessageDialog(null, "Invalid input", "Error message", JOptionPane.ERROR_MESSAGE);
                }

                break;

            case "Delete product":
                store = interpreter.getStore(storeId);
                System.out.println(store.getProducts().size());
                if (store.getProducts().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No products in Store", "Error message", JOptionPane.ERROR_MESSAGE);
                    seller();
                } else {
                    String[] productList = new String[store.getProducts().size()];
                    for (int i = 0; i < store.getProducts().size(); i++) {
                        productList[i] = (interpreter.getProduct(store.getProducts().get(i)).getName());
                    }

                    ArrayList<String> productArrayList = new ArrayList<String>();
                    for (int w = 0; w < productList.length; w++) {
                        productArrayList.add(productList[w]);
                    }
                    String deleteProductOption = (String) JOptionPane.showInputDialog(null, "Which product would you like to delete?",
                            "Options", JOptionPane.QUESTION_MESSAGE, null, productList,
                            productList[0]);

                    int indexOfProduct = productArrayList.indexOf(deleteProductOption);
                    System.out.println("index of deleting product:" + indexOfProduct);
                    System.out.println(store.getProducts().toString());
                    int id = store.getProducts().get(indexOfProduct);
                    System.out.println("id of product deleting:" + id);
                    interpreter.deleteProduct(id);
                }

                break;

            case "Delete store":
                interpreter.deleteStore(storeId);
                break;

            case "Edit product":
                Store currentStore = interpreter.getStore(storeId);

                if (currentStore.getProducts().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No products in Store", "Error message", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String[] productList = new String[currentStore.getProducts().size()];
                for (int i = 0; i < currentStore.getProducts().size(); i++) {
                    productList[i] = (interpreter.getProduct(currentStore.getProducts().get(i)).getName());
                }
                String deleteProductOption = (String) JOptionPane.showInputDialog(null, "Which product would you like to edit?",
                        "Options", JOptionPane.QUESTION_MESSAGE, null, productList,
                        productList[0]);

                ArrayList<String> productArrayList = new ArrayList<String>();
                for (int e = 0; e < productList.length; e++) {
                    productArrayList.add(productList[e]);
                }

                int indexOfProduct = productArrayList.indexOf(deleteProductOption);

                int editProductID = interpreter.getStoreProducts(currentStore).get(indexOfProduct).getId();

                String newProductName = JOptionPane.showInputDialog(null, "Provide new NAME for the product",
                        "Edit Product", JOptionPane.QUESTION_MESSAGE);

                if (newProductName.equals("") || newProductName.isEmpty()) {
                    editStore(indexOfStore);
                    break;
                }

                String newProductDescription = JOptionPane.showInputDialog(null, "Provide new DESCRIPTION for the product",
                        "Edit Product", JOptionPane.QUESTION_MESSAGE);

                if (newProductDescription.equals("") || newProductDescription.isEmpty()) {
                    editStore(indexOfStore);
                    break;
                }

                String newProductQuantityString = JOptionPane.showInputDialog(null, "Provide new QUANTITY for the product",
                        "Edit Store", JOptionPane.QUESTION_MESSAGE);
                int newProductQuantity = 1;

                do {
                    try {
                        newProductQuantity = Integer.parseInt(newProductQuantityString);

                        if (newProductQuantity >= 0) {
                            break;
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Invalid input", "Error message", JOptionPane.ERROR_MESSAGE);
                    }

                    newProductQuantityString = JOptionPane.showInputDialog(null, "Enter an integer greater than or equal to 0:",
                        "Edit Store", JOptionPane.QUESTION_MESSAGE);
                } while (true);


                String newProductPriceString = JOptionPane.showInputDialog(null, "Provide new PRICE for the product",
                        "Edit Store", JOptionPane.QUESTION_MESSAGE);
                double newProductPrice = 1.1;

                do {
                    try {
                        newProductPrice = Double.parseDouble(newProductPriceString);

                        if (newProductPrice > 0) {
                            break;
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Invalid input", "Error message", JOptionPane.ERROR_MESSAGE);
                    }

                    newProductPriceString = JOptionPane.showInputDialog(null, "Enter a decimal greater than 0:",
                        "Edit Store", JOptionPane.QUESTION_MESSAGE);
                } while (true);

                try {
                    interpreter.editProduct(editProductID, newProductName, newProductDescription, newProductQuantity, newProductPrice);
                    JOptionPane.showMessageDialog(null, "Edit SUCCESSFUL!", "successful message", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error occurred while editing product", "Error message", JOptionPane.ERROR_MESSAGE);
                }
                break;
            default:
                break;


        }
    }


    public static void storeSalesData() {
        ArrayList<Store> stores = interpreter.getOwnedStores();
        String[] storeNames = new String[stores.size()];

        for (int i = 0; i < stores.size(); i++) {
            storeNames[i] = stores.get(i).getName();
        }

        String storeName = (String) JOptionPane.showInputDialog(null, "\"Select the store from which you want sales data",
                "Options", JOptionPane.QUESTION_MESSAGE, null, storeNames,
                storeNames[0]);

        int indexOfStore = Arrays.binarySearch(storeNames, storeName);
        ArrayList<String[]> salesData = interpreter.getSaleData(interpreter.getOwnedStores().get(indexOfStore));

        String viewSalesData = "";
        for (int j = 0; j < salesData.size(); j++) {
            viewSalesData += Arrays.toString(salesData.get(j));
            viewSalesData += "\n";
        }

        JOptionPane.showMessageDialog(null, viewSalesData, "View Sales Data",
                JOptionPane.INFORMATION_MESSAGE);

    }

    public static void productData() {
        ArrayList<String[]> totalData = interpreter.getSellerShoppingCartView();

        String viewProductData = "";
        viewProductData += "Data format: Customer email, product name, store name, quantity sold.";
        viewProductData += "\n";

        for (int i = 0; i < totalData.size(); i++) {
            viewProductData += Arrays.toString(totalData.get(i));
            viewProductData += "\n";

        }
        if (totalData.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No products are currently in any customers' cart", "Empty Cart",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, viewProductData, "View Product Data", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void putInCart() {

        int cart = JOptionPane.showConfirmDialog (null, "Would you like to put an item in your cart?","Cart",JOptionPane.YES_NO_OPTION);

        if (cart == JOptionPane.YES_OPTION) {
            String search = JOptionPane.showInputDialog(null, "Enter the name of the product which you would like to put in cart",
                    "Cart", JOptionPane.QUESTION_MESSAGE);
            ArrayList<Product> results = interpreter.search(search);

            if (results.size() == 0) {
                JOptionPane.showMessageDialog(null, "No such product found!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] productNames = new String[results.size()];
            for (int i = 0; i < results.size(); i++) {
                productNames[i] = results.get(i).getName();
            }


            String productName = (String) JOptionPane.showInputDialog(null, "\"Select the store from which you want sales data",
                    "Options", JOptionPane.QUESTION_MESSAGE, null, productNames,
                    productNames[0]);

            int quantity = 0;
            int index = Arrays.binarySearch(productNames, productName);

            while (true) {
                String quantityString = JOptionPane.showInputDialog(null, "How many would you like to add to cart?",
                        "Cart", JOptionPane.QUESTION_MESSAGE);
                try {
                    quantity = Integer.parseInt(quantityString);
                    if (quantity > 0) {
                        break;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Invalid Input", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            Product product = interpreter.search(search).get(index);
            if (interpreter.getCurrentUser() instanceof Customer) {
                interpreter.addToCart(product, quantity);
                System.out.println(((Customer) interpreter.getCurrentUser()).getIds());
                JOptionPane.showMessageDialog(null, "Item successfully added", "Cart",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        }
    }

}