import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.PrintWriter;

/**
 * Communicates with a Client's designated ServerThread to perform operations on the shared
 * DataManager and convert text output to developer-friendly objects.
 * 
 * @author Seth Hartzler
 * @version December 5, 2023
 */
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

    /**
     * Saves the data from the DataManager and closes the connection to the server. Should be
     * called before exiting from the Client.
     */
    public void close() {
        this.pw.println("quit");
        this.pw.flush();

        try {
            this.socket.close();
        } catch (Exception e) {
            System.out.println("Disconnection error encountered");
        }
    }

    /**
     * Saves the DataManager's information to a series of files. Should be called before exiting the
     * program so that data is not lost.
     */
    public void save() {
        this.pw.println("save");
        this.pw.flush();
    }

    /**
     * Loads products from a CSV file. If any product entry has an id matching the id of an existing
     * product in the database, that product entry will nto be added.
     *
     * @param filename The file from which to load product data
     */
    public void loadProducts(String filename) {
        this.pw.println("loadProducts");

        this.pw.println(filename);

        this.pw.flush();
    }

    /**
     * Exports all transactions associated with the current user to a file at the given location.
     *
     * @param filename The file to which the transaction data will be written
     */
    public void exportPurchases(String filename) {
        this.pw.println("exportPurchases");

        this.pw.println(filename);

        this.pw.flush();
    }

    /**
     * Exports product data in CSV form to a given file source.
     *
     * @param filename The file to which the product data will be written
     */
    public void exportProducts(String filename) {
        this.pw.println("exportProducts");

        this.pw.println(filename);

        this.pw.flush();
    }

    /**
     * @return The ID that will be assigned to the next created Store object
     */
    public int getCurrentStoreId() {
        this.pw.println("getCurrentStoreId");

        this.pw.flush();

        return Integer.parseInt(this.s.nextLine());
    }

    /**
     * Increments the ID that will be assigned to the next created Store object. This function
     * should always be called after creating a new Store object that uses the getCurrentStoreId()
     * function.
     */
    public void incrementCurrentStoreId() {
        this.pw.println("incrementCurrentStoreId");

        this.pw.flush();
    }

    /**
     * Log out the current user.
     */
    public void logout() {
        this.pw.println("logout");

        this.pw.flush();
    }

    /**
     * Returns the user with the specified email, if the user exists.
     *
     * @param email
     * @return The User with the provided email, or null if no such user exists
     */
    public User getUser(String email) {
        this.pw.println("getUser");

        this.pw.println(email);

        this.pw.flush();

        User user = User.fromStringFormat(this.s.nextLine());

        return user;
    }

    /**
     * Checks the specified email/password pair against the database. If the user with the provided
     * email exists and that user's password equals the provided password, the method returns true;
     * otherwise, it returns false.
     *
     * @param email
     * @param password
     * @return Whether or not a User with the given email and password exists
     */
    public boolean checkUserLogin(String email, String password) {
        this.pw.println("userLogin");
        this.pw.println(email);
        this.pw.println(password);

        this.pw.flush();

        return Boolean.parseBoolean(this.s.nextLine());
    }

    /**
     * @return The current user
     */
    public User getCurrentUser() {
        this.pw.println("getCurrentUser");

        this.pw.flush();

        String line = this.s.nextLine();

        if (line.equals("NONE")) {
            return null;
        } else {
            return User.fromStringFormat(line);
        }
    }

    /**
     * Deletes the current user from the system if the user is logged in.
     */
    public void deleteCurrentUser() {
        this.pw.println("deleteCurrentUser");

        this.pw.flush();
    }

    /**
     * Adds a newly created User object (if it is not already in the system) and then makes it
     * the current user.
     *
     * @param user
     */
    public void addUser(User user) {
        this.pw.println("addUser");

        this.pw.println(user.toStringFormat());

        this.pw.flush();
    }

    /**
     * If a User is logged in, sets the currentUser's email and password to the given parameters
     *
     * @param newEmail
     * @param newPassword
     */
    public void editCurrentUser(String email, String password) {
        this.pw.println("editCurrentUser");

        this.pw.println(email);
        this.pw.println(password);

        this.pw.flush();
    }

    /**
     * @param by An integer representing what to sort the products by. Values include BY_NOTHING,
     * BY_QUANTITY, and BY_PRICE
     * @param sort An integer representing how to sort the products. Values include NOT_SORTED,
     * SORTED_ASC, and SORTED_DESC
     * @return The list of all products in the marketplace
     */
    public ArrayList<Product> getProductList(int by, int sort) {
        ArrayList<Product> results = new ArrayList<Product>();

        this.pw.println("getProductList");

        this.pw.println(by);
        this.pw.println(sort);

        this.pw.flush();

        String line = this.s.nextLine();

        while (!line.equals("END")) {
            try {
                Product product = Product.fromStringFormat(line);

                results.add(product);
            } catch (Exception e) {}

            line = this.s.nextLine();
        }

        return results;
    }

    /**
     * @param id
     * @return The product with the given id if it exists, or a dummy if not
     */
    public Product getProduct(int id) {
        this.pw.println("getProduct");

        this.pw.println(id);

        this.pw.flush();

        try {
            return Product.fromStringFormat(this.s.nextLine());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Adds a product to the marketplace if the currentUser is not null and is a Seller, and if
     * the provided product is not already on the marketplace.
     *
     * @param product
     */
    public void addProduct(Product product) {
        this.pw.println("addProduct");

        this.pw.println(product.toStringFormat());

        this.pw.flush();
    }

    /**
     * Edits a product. This is permitted only if the given product belongs to the current user.
     *
     * @param id The id of the product
     * @param name
     * @param description
     * @param quantity
     * @param price
     */
    public void editProduct(int id, String name, String description, int quantity, double price) {
        this.pw.println("editProduct");

        this.pw.println(id);
        this.pw.println(name);
        this.pw.println(description);
        this.pw.println(quantity);
        this.pw.println(price);

        this.pw.flush();
    }

    /**
     * Deletes a product. This is permitted only if the given product belongs to the current user.
     *
     * @param id The id of the product
     */
    public void deleteProduct(int id) {
        this.pw.println("deleteProduct");

        this.pw.println(id);

        this.pw.flush();
    }

    /**
     * @return An ArrayList of Stores for which the Seller associated with each store is the current
     * user
     */
    public ArrayList<Store> getOwnedStores() {
        this.pw.println("getOwnedStores");

        this.pw.flush();

        ArrayList<Store> stores = new ArrayList<Store>();

        String line = this.s.nextLine();

        while (!line.equals("END")) {
            Store store = Store.fromStringFormat(line);

            stores.add(store);

            line = this.s.nextLine();
        }

        return stores;
    }

    /**
     * @param store
     * @return true if the current user owns the given store, false otherwise
     */
    public boolean currentUserOwnsStore(Store store) {
        this.pw.println("currentUserOwnsStore");

        this.pw.println(store.toStringFormat());

        this.pw.flush();

        return Boolean.parseBoolean(this.s.nextLine());
    }

    /**
     * @param currentUser
     * @param store
     * @return The list of products associated with the given store
     */
    public ArrayList<Product> getStoreProducts(Store store) {
        this.pw.println("getStoreProducts");

        this.pw.println(store.toStringFormat());

        this.pw.flush();

        ArrayList<Product> products = new ArrayList<Product>();

        String line = this.s.nextLine();

        while (!line.equals("END")) {
            try{
                Product product = Product.fromStringFormat(line);

                products.add(product);
            } catch (Exception e) {}

            line = this.s.nextLine();
        }

        return products;
    }

    /**
     * @param id
     * @return The store with the given id
     */
    public Store getStore(int id) {
        this.pw.println("getStore");

        this.pw.println(id);

        this.pw.flush();

        return Store.fromStringFormat(this.s.nextLine());
    }

    /**
     * Adds a store to the marketplace if it does not already exist.
     *
     * @param store
     */
    public void addStore(Store store) {
        this.pw.println("addStore");

        this.pw.println(store.toStringFormat());

        this.pw.flush();
    }

    /**
     * Deletes the store with the given id. Permitted only if the current user owns the store.
     *
     * @param id
     */
    public void deleteStore(int id) {
        this.pw.println("deleteStore");

        this.pw.println(id);

        this.pw.flush();
    }

    /**
     * Edits the store with the given id.
     *
     * @param id
     * @param name
     */
    public void editStore(int id, String name) {
        this.pw.println("editStore");

        this.pw.println(id);
        this.pw.println(name);

        this.pw.flush();
    }

    /**
     * Searches the product list for products where the name, description, or store name contains
     * the search term.
     *
     * @param term The term to search by
     * @return An ArrayList of products which match the term
     */
    public ArrayList<Product> search(String term) {
        this.pw.println("search");

        this.pw.println(term);

        this.pw.flush();

        ArrayList<Product> results = new ArrayList<Product>();

        String line = this.s.nextLine();

        while (!line.equals("END")) {
            try {
                Product product = Product.fromStringFormat(line);

                results.add(product);
            } catch (Exception e) {}

            line = this.s.nextLine();
        }
        
        return results;
    }

    /**
     * Purchases a single item in the given quantity and creates a corresponding Transaction.
     *
     * @param product
     * @param quantity
     * @throws InvalidQuantityError Thrown if the quantity is negative or greater than the quantity
     * of the Product currently in stock
     */
    public boolean makePurchase(Product product, int quantity) throws InvalidQuantityError { //THROWS INVALID QUANTITY ERROR
        this.pw.println("makePurchase");

        this.pw.println(product.toStringFormat());
        this.pw.println(quantity);

        this.pw.flush();

        String result = this.s.nextLine();

        if (result.equals("SUCCESS")) {
            return true;
        }

        return false;
    }

    /**
     * Gets the purchase history of the current user.
     *
     * @return A list of Transactions where the customer making the purchase equals the current user
     */
    public ArrayList<Transaction> getPurchaseHistory() {
        this.pw.println("getPurchaseHistory");

        this.pw.flush();

        ArrayList<Transaction> data = new ArrayList<Transaction>();

        String line = this.s.nextLine();

        while (!line.equals("END")) {
            Transaction datum = Transaction.fromStringFormat(line);

            data.add(datum);

            line = this.s.nextLine();
        }

        return data;
    }

    /**
     * Gets sale data for a particular store if the current user owns that store. Data is returned
     * as an ArrayList of String arrays. Each string array contains two entries. The first represents
     * the customer making the purchase, and the second represents the revenue made by that
     * transaction (formatted to two decimal places and prefixed with a dollar sign).
     *
     * @param store
     * @return An ArrayList of String arrays in the described format
     */
    public ArrayList<String[]> getSaleData(Store store) {
        this.pw.println("getSaleData");

        this.pw.println(store.toStringFormat());

        this.pw.flush();

        ArrayList<String[]> data = new ArrayList<String[]>();

        String line = this.s.nextLine();

        while (!line.equals("END")) {
            data.add(line.split(","));

            line = this.s.nextLine();
        }

        return data;
    }

    /**
     * Gets all products associated with the Seller currently logged in that are currently in
     * users' shopping carts. Product information is formatted as an array of strings, with the
     * first string in the array containing the email of the customer, the second containing the
     * product name, the third containing the store name, and the fourth containing the quantity
     * of the product currently in the user's shopping cart.
     *
     * @return An ArrayList of String arrays in the specified format
     */
    public ArrayList<String[]> getSellerShoppingCartView() {
        this.pw.println("getSellerShoppingCartView");

        this.pw.flush();

        ArrayList<String[]> data = new ArrayList<String[]>();

        String line = this.s.nextLine();

        while (!line.equals("END")) {
            data.add(line.split(","));

            line = this.s.nextLine();
        }

        return data;
    }
}
