import java.util.ArrayList;
import java.io.*;

/**
 * The DataManager class is designed to handle the other classes in the system in aggregate form.
 * Its core is 4 ArrayLists, which contain the products, users, stores, and transactions tracked by
 * the progam. It also contains a User field to track the current user, which is useful in checking
 * permissions.
 *
 * @author Seth Hartzler
 * @version November 8, 2023
 */

public class DataManager {
    private ArrayList<Product> products;
    private ArrayList<User> users;
    private ArrayList<Store> stores;
    private ArrayList<Transaction> transactions;

    private int currentProductId;
    private int currentStoreId;

    private Product dummyProduct;
    public User dummyUser;
    public Store dummyStore;

    public static final int BY_NOTHING = 0;
    public static final int BY_QUANTITY = 1;
    public static final int BY_PRICE = 2;

    public static final int NOT_SORTED = 0;
    public static final int SORTED_ASC = 1;
    public static final int SORTED_DESC = 2;

    private static Object gatekeeper = new Object();

    public DataManager() {
        this.products = new ArrayList<Product>();
        this.users = new ArrayList<User>();
        this.stores = new ArrayList<Store>();
        this.transactions = new ArrayList<Transaction>();

        try {
            this.dummyProduct = new Product("Product not found",
                    "Product description not found", -1, 0, 0.0, -1);
        } catch (Exception e) {
            System.out.println("File not found");
        }
        this.dummyUser = new User("User not found", "");
        this.dummyStore = new Store(new ArrayList<Integer>(), "Store not found",
                "User not found", -1);

        try {
            File fProduct = new File("products.csv");
            FileReader frProduct = new FileReader(fProduct);
            BufferedReader bfrProduct = new BufferedReader(frProduct);

            String line = bfrProduct.readLine();

            while (line != null) {
                Product newProduct = Product.fromStringFormat(line);
                this.products.add(newProduct);
                line = bfrProduct.readLine();
            }

            bfrProduct.close();
        } catch (Exception e) {
            System.out.println("Error file not found");
        }

        try {
            File fUser = new File("users.csv");
            FileReader frUser = new FileReader(fUser);
            BufferedReader bfrUser = new BufferedReader(frUser);

            String line = bfrUser.readLine();

            while (line != null) {
                if (line.charAt(0) == 's') {
                    Seller newSeller = Seller.fromStringFormat(line);
                    this.users.add(newSeller);
                } else if (line.charAt(0) == 'c') {
                    Customer newCustomer = Customer.fromStringFormat(line);
                    this.users.add(newCustomer);
                }

                line = bfrUser.readLine();
            }

            bfrUser.close();
        } catch (Exception e) {
            System.out.println("Error file not found");
        }

        try {
            File fStore = new File("stores.csv");
            FileReader frStore = new FileReader(fStore);
            BufferedReader bfrStore = new BufferedReader(frStore);

            String line = bfrStore.readLine();

            while (line != null) {
                Store newStore = Store.fromStringFormat(line);
                this.stores.add(newStore);
                line = bfrStore.readLine();
            }

            bfrStore.close();
        } catch (Exception e) {}

        try {
            File fTransaction = new File("transactions.csv");
            FileReader frTransaction = new FileReader(fTransaction);
            BufferedReader bfrTransaction = new BufferedReader(frTransaction);

            String line = bfrTransaction.readLine();

            while (line != null) {
                Transaction newTransaction = Transaction.fromStringFormat(line);
                this.transactions.add(newTransaction);
                line = bfrTransaction.readLine();
            }

            bfrTransaction.close();
        } catch (Exception e) {}

        try {
            File fCurrentIds = new File("ids.txt");
            FileReader frCurrentIds = new FileReader(fCurrentIds);
            BufferedReader bfrCurrentIds = new BufferedReader(frCurrentIds);

            this.currentProductId = Integer.valueOf(bfrCurrentIds.readLine());
            this.currentStoreId = Integer.valueOf(bfrCurrentIds.readLine());

            bfrCurrentIds.close();
        } catch (Exception e) {
            this.currentProductId = 0;
            this.currentStoreId = 0;
        }

        Product.setCount(this.currentProductId);
    }

    /**
     * Saves the DataManager's information to a series of files. Should be called before exiting the
     * program so that data is not lost.
     */
    public void saveToFile() {
        synchronized(gatekeeper) {
            try {
                File fProduct = new File("products.csv");
                PrintWriter pwProduct = new PrintWriter(fProduct);

                for (int i = 0; i < this.products.size(); i++) {
                    String line = this.products.get(i).toStringFormat();
                    pwProduct.println(line);
                }

                pwProduct.close();

                File fUser = new File("users.csv");
                PrintWriter pwUser = new PrintWriter(fUser);

                for (int i = 0; i < this.users.size(); i++) {
                    String line = this.users.get(i).toStringFormat();
                    pwUser.println(line);
                }

                pwUser.close();

                File fStore = new File("stores.csv");
                PrintWriter pwStore = new PrintWriter(fStore);

                for (int i = 0; i < this.stores.size(); i++) {
                    String line = this.stores.get(i).toStringFormat();
                    pwStore.println(line);
                }

                pwStore.close();

                File fTransaction = new File("transactions.csv");
                PrintWriter pwTransaction = new PrintWriter(fTransaction);

                for (int i = 0; i < this.transactions.size(); i++) {
                    String line = this.transactions.get(i).toStringFormat();
                    pwTransaction.println(line);
                }

                pwTransaction.close();

                File fCurrentIds = new File("ids.txt");
                PrintWriter pwCurrentIds = new PrintWriter(fCurrentIds);

                pwCurrentIds.println(Product.getCount());
                pwCurrentIds.println(this.currentStoreId);

                pwCurrentIds.close();
            } catch (Exception e) {}
        }
    }

    /**
     * Loads products from a CSV file. If any product entry has an id matching the id of an existing
     * product in the database, that product entry will nto be added.
     *
     * @param filename The file from which to load product data
     */
    public void loadProductsFromFile(User currentUser, String filename) {
        if (currentUser != null && currentUser instanceof Seller) {
            try {
                File f = new File(filename);
                FileReader fr = new FileReader(f);
                BufferedReader bfr = new BufferedReader(fr);
                String line = bfr.readLine();

                while (line != null) {
                    Product product = Product.fromStringFormat(line);

                    if (this.getProduct(product.getId()) == this.dummyProduct &&
                            this.getStore(product.getStoreId()).getSellerEmail()
                                    .equals(currentUser.getEmail())) {
                        this.products.add(product);
                    }
                }

                bfr.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("File not found");
            }
        }
    }

    /**
     * Exports all transactions associated with the current user to a file at the given location.
     *
     * @param filename The file to which the transaction data will be written
     */
    public void exportPurchaseHistory(User currentUser, String filename) {
        if (currentUser != null && currentUser instanceof Customer) {
            try {
                File f = new File(filename);
                PrintWriter pw = new PrintWriter(f);

                for (int i = 0; i < this.transactions.size(); i++) {
                    if (this.transactions.get(i).getCustomerEmail()
                            .equals(currentUser.getEmail())) {
                        String line = this.transactions.get(i).toStringFormat();

                        pw.println(line);
                    }
                }

                pw.close();
            } catch (Exception e) {}
        }
    }

    /**
     * Exports product data in CSV form to a given file source.
     *
     * @param filename The file to which the product data will be written
     */
    public void exportProductData(User currentUser, String filename) {
        if (currentUser != null && currentUser instanceof Seller) {
            try {
                File f = new File(filename);
                PrintWriter pw = new PrintWriter(f);

                for (int i = 0; i < this.products.size(); i++) {
                    if (this.getStore(this.products.get(i).getStoreId()).getSellerEmail()
                            .equals(currentUser.getEmail())) {
                        String formattedProduct = this.products.get(i).toStringFormat();

                        pw.println(formattedProduct);
                    }
                }

                pw.close();
            } catch (Exception e) {}
        }
    }

    /**
     *
     * @return The ID that will be assigned to the next created Store object
     */
    public int getCurrentStoreId() {
        return this.currentStoreId;
    }

    /**
     * Increments the ID that will be assigned to the next created Store object. This function
     * should always be called after creating a new Store object that uses the getCurrentStoreId()
     * function.
     */
    public void incrementCurrentStoreId() {
        synchronized(gatekeeper) {
            this.currentStoreId++;
        }
    }

    /**
     * Returns the user with the specified email, if the user exists.
     *
     * @param email
     * @return The User with the provided email, or null if no such user exists
     */
    public User getUser(String email) {
        for (int i = 0; i < this.users.size(); i++) {
            if (this.users.get(i).getEmail().equals(email)) {
                return this.users.get(i);
            }
        }

        return this.dummyUser;
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
        if (email.equals("User not found")) {
            return false;
        }

        User user = this.getUser(email);

        if (user != null && user.getPassword().equals(password)) {
            return true;
        }

        return false;
    }

    /**
     * Deletes the current user from the system if the user is logged in.
     */
    public void deleteCurrentUser(User currentUser) {
        synchronized (gatekeeper) {
            if (currentUser != null) {
                this.users.remove(currentUser);
            }
        }
    }

    /**
     * Adds a newly created User object (if it is not already in the system) and then makes it
     * the current user.
     *
     * @param user
     */
    public void addUser(User user) {
        User existingUser = this.getUser(user.getEmail());

        if (existingUser == this.dummyUser) {
            synchronized (gatekeeper) {
                this.users.add(user);
            }
        }
    }

    /**
     * If a User is logged in, sets the currentUser's email and password to the given parameters
     *
     * @param newEmail
     * @param newPassword
     */
    public void editCurrentUser(User currentUser, String newEmail, String newPassword) {
        if (currentUser != null && 
        this.getUser(newEmail).getEmail().equals(this.dummyUser.getEmail())) {
            synchronized (gatekeeper) {
                currentUser.setEmail(newEmail);
                currentUser.setPassword(newPassword);
            }
        }
    }

    /**
     * @return All products in the database, in an unsorted format
     */
    public ArrayList<Product> getProductList() {
        return this.getProductList(BY_NOTHING, NOT_SORTED);
    }

    /**
     * @param by An integer representing what to sort the products by. Values include BY_NOTHING,
     * BY_QUANTITY, and BY_PRICE
     * @param sort An integer representing how to sort the products. Values include NOT_SORTED,
     * SORTED_ASC, and SORTED_DESC
     * @return The list of all products in the marketplace
     */
    public ArrayList<Product> getProductList(int by, int sort) {
        ArrayList<Product> notSorted = this.products;
        ArrayList<Product> results = new ArrayList<Product>();

        if (by == BY_NOTHING || sort == NOT_SORTED || notSorted.size() < 2) {
            results = notSorted;
        } else {
            if (by == BY_QUANTITY) {
                int current_threshold = Integer.MAX_VALUE;
                int current_threshold_index = 0;

                if (sort == SORTED_DESC) {
                    current_threshold = Integer.MIN_VALUE;
                }

                while (results.size() < notSorted.size()) {
                    for (int i = 0; i < notSorted.size(); i++) {
                        Product product = notSorted.get(i);

                        if ((sort == SORTED_ASC && product.getQuantity() <= current_threshold &&
                                results.indexOf(product) == -1) ||
                                (sort == SORTED_DESC && product.getQuantity() >= current_threshold &&
                                        results.indexOf(product) == -1)) {
                            current_threshold = product.getQuantity();
                            current_threshold_index = i;
                        }
                    }

                    results.add(notSorted.get(current_threshold_index));

                    current_threshold = Integer.MAX_VALUE;

                    if (sort == SORTED_DESC) {
                        current_threshold = Integer.MIN_VALUE;
                    }
                }
            } else if (by == BY_PRICE) {
                double current_threshold = Float.MAX_VALUE;
                int current_threshold_index = 0;

                if (sort == SORTED_DESC) {
                    current_threshold = Float.MIN_VALUE;
                }

                while (results.size() < notSorted.size()) {
                    for (int i = 0; i < notSorted.size(); i++) {
                        Product product = notSorted.get(i);

                        if ((sort == SORTED_ASC && product.getPrice() <= current_threshold &&
                                results.indexOf(product) == -1) ||
                                (sort == SORTED_DESC && product.getPrice() >= current_threshold &&
                                        results.indexOf(product) == -1)) {
                            current_threshold = product.getPrice();
                            current_threshold_index = i;
                        }
                    }

                    results.add(notSorted.get(current_threshold_index));

                    current_threshold = Float.MAX_VALUE;

                    if (sort == SORTED_DESC) {
                        current_threshold = Float.MIN_VALUE;
                    }
                }
            }
        }

        return results;
    }

    /**
     * @param id
     * @return The product with the given id if it exists, or a dummy if not
     */
    public Product getProduct(int id) {
        for (int i = 0; i < this.products.size(); i++) {
            if (this.products.get(i).getId() == id) {
                return this.products.get(i);
            }
        }

        return this.dummyProduct;
    }

    /**
     * Adds a product to the marketplace if the currentUser is not null and is a Seller, and if
     * the provided product is not already on the marketplace.
     *
     * @param product
     */
    public void addProduct(User currentUser, Product product) {
        if (currentUser != null && currentUser instanceof Seller) {
            if (this.getProduct(product.getId()) == this.dummyProduct) {
                synchronized (gatekeeper) {
                    this.products.add(product);
                }
            }
        }
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
    public void editProduct(User currentUser, int id, String name, String description, int quantity, 
    double price) {
        if (currentUser != null && currentUser instanceof Seller) {
            Product product = this.getProduct(id);

            if (this.currentUserOwnsStore(currentUser, this.getStore(product.getId()))) {
                synchronized (gatekeeper) {
                    product.setName(name);
                    product.setDescription(description);
                    product.setQuantity(quantity);
                    try {
                        product.setPrice(price);
                    } catch (Exception e) {}
                }
            }
        }
    }

    /**
     * Deletes a product. This is permitted only if the given product belongs to the current user.
     *
     * @param id The id of the product
     */
    public void deleteProduct(User currentUser, int id) {
        if (currentUser != null && currentUser instanceof Seller) {
            Product product = this.getProduct(id);

            if (product != this.dummyProduct &&
                    this.currentUserOwnsStore(currentUser, this.getStore(product.getId()))) {
                synchronized (gatekeeper) {
                    this.products.remove(product);
                }
            }
        }
    }

    /**
     * @return An ArrayList of Stores for which the Seller associated with each store is the current
     * user
     */
    public ArrayList<Store> getOwnedStores(User currentUser) {
        ArrayList<Store> results = new ArrayList<Store>();

        for (int i = 0; i < this.stores.size(); i++) {
            if (this.currentUserOwnsStore(currentUser, this.stores.get(i))) {
                synchronized (gatekeeper) {
                    results.add(this.stores.get(i));
                }
            }
        }

        return results;
    }

    /**
     * @param store
     * @return true if the current user owns the given store, false otherwise
     */
    public boolean currentUserOwnsStore(User currentUser, Store store) {
        if (currentUser != null) {
            if (store.getSellerEmail().equals(currentUser.getEmail())) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param currentUser
     * @param store
     * @return The list of products associated with the given store
     */
    public ArrayList<Product> getStoreProducts(User currentUser, Store store) {
        if (currentUser != null && currentUser instanceof Seller &&
                store.getSellerEmail().equals(currentUser.getEmail())) {
            ArrayList<Product> results = new ArrayList<Product>();

            for (int i = 0; i < this.products.size(); i++) {
                if (this.products.get(i).getStoreId() == store.getId()) {
                    results.add(products.get(i));
                }
            }

            return results;
        }

        return null;
    }

    /**
     * @param id
     * @return The store with the given id
     */
    public Store getStore(int id) {
        for (int i = 0; i < this.stores.size(); i++) {
            if (this.stores.get(i).getId() == id) {
                return this.stores.get(i);
            }
        }

        return this.dummyStore;
    }

    /**
     * Adds a store to the marketplace if it does not already exist.
     *
     * @param store
     */
    public void addStore(Store store) {
        synchronized(gatekeeper) {
            Store existingStore = this.getStore(store.getId());

            if (existingStore == this.dummyStore) {
                synchronized (gatekeeper) {
                    this.stores.add(store);
                }
            }
        }
    }

    /**
     * Deletes the store with the given id. Permitted only if the current user owns the store.
     *
     * @param id
     */
    public void deleteStore(User currentUser, int id) {
        synchronized(gatekeeper) {
            Store existingStore = this.getStore(id);

            if (existingStore != this.dummyStore && 
            this.currentUserOwnsStore(currentUser, existingStore)) {
                synchronized (gatekeeper) {
                    this.stores.remove(existingStore);
                }
            }
        }
    }

    /**
     * Edits the store with the given id. Permitted only if the current user owns the store.
     *
     * @param id
     * @param name
     */
    public void editStore(int id, String name) {
        synchronized(gatekeeper) {
            Store store = this.getStore(id);

            if (store != this.dummyStore) {
                store.setName(name);
            }
        }
    }

    /**
     * Searches the product list for products where the name, description, or store name contains
     * the search term.
     *
     * @param term The term to search by
     * @return An ArrayList of products which match the term
     */
    public ArrayList<Product> search(String term) {
        ArrayList<Product> results = new ArrayList<Product>();

        for (int i = 0; i < this.products.size(); i++) {
            Product product = this.products.get(i);

            if (product.getName().contains(term) || product.getDescription().contains(term) ||
                    this.getStore(product.getStoreId()).getName().contains(term)) {
                results.add(product);
            }
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
    public void makePurchase(User currentUser, Product product, int quantity) 
    throws InvalidQuantityError {
        synchronized(gatekeeper) {
            if (product.checkQuantity(quantity)) {
                synchronized (gatekeeper) {
                    product.purchase(quantity);
                    Transaction transaction = new Transaction(product.getId(), product.getStoreId(),
                            currentUser.getEmail(), 
                            this.getStore(product.getStoreId()).getSellerEmail(),
                            quantity, product.getPrice());
                    this.transactions.add(transaction);
                }
            } else {
                throw new InvalidQuantityError("Invalid purchase quantity");
            }
        }
    }

    /**
     * Gets the purchase history of the current user.
     *
     * @return A list of Transactions where the customer making the purchase equals the current user
     */
    public ArrayList<Transaction> getPurchaseHistory(User currentUser) {
        if (currentUser != null && currentUser instanceof Customer) {
            ArrayList<Transaction> results = new ArrayList<Transaction>();

            for (int i = 0; i < this.transactions.size(); i++) {
                if (this.transactions.get(i).getCustomerEmail().equals(currentUser.getEmail())) {
                    results.add(this.transactions.get(i));
                }
            }

            return results;
        }

        return null;
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
    public ArrayList<String[]> getSaleData(User currentUser, Store store) {
        if (currentUser != null && currentUser instanceof Seller &&
                store.getSellerEmail().equals(currentUser.getEmail())) {
            ArrayList<String[]> data = new ArrayList<String[]>();

            for (int i = 0; i < this.transactions.size(); i++) {
                if (this.transactions.get(i).getStoreId() == store.getId()) {
                    String[] datum = {this.transactions.get(i).getCustomerEmail(),
                            String.format("$.2f",
                                    ((double) this.transactions.get(i).getQuantitySold())
                                            * this.transactions.get(i).getPrice())};
                    data.add(datum);
                }
            }

            return data;
        }

        return null;
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
    public ArrayList<String[]> getSellerShoppingCartView(User currentUser) {
        if (currentUser != null && currentUser instanceof Seller) {
            ArrayList<String[]> data = new ArrayList<String[]>();

            for (int i = 0; i < this.users.size(); i++) {
                if (this.users.get(i) instanceof Customer) {
                    ArrayList<Integer> productIds = ((Customer) this.users.get(i)).getIds();
                    ArrayList<Integer> productQuantities =
                            ((Customer) this.users.get(i)).getQuantities();

                    for (int j = 0; j < productIds.size(); j++) {
                        Product product = this.getProduct(((int) productIds.get(j)));

                        if (this.getStore(product.getStoreId()).getSellerEmail()
                                .equals(currentUser.getEmail())) {
                            String[] datum = {
                                    this.users.get(i).getEmail(),
                                    this.getProduct(productIds.get(j)).getName(),
                                    this.getStore(product.getStoreId()).getName(),
                                    productQuantities.get(j).toString()
                            };

                            data.add(datum);
                        }
                    }
                }
            }

            return data;
        }

        return null;
    }
}
