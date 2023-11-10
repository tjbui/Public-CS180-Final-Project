import java.util.ArrayList;

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

    private User currentUser;

    public static final int BY_NOTHING = 0;
    public static final int BY_QUANTITY = 1;
    public static final int BY_PRICE = 2;

    public static final int NOT_SORTED = 0;
    public static final int SORTED_ASC = 1;
    public static final int SORTED_DESC = 2;

    public DataManager() {
        this.products = new ArrayList<Product>();
        this.users = new ArrayList<User>();
        this.stores = new ArrayList<Store>();
        this.transactions = new ArrayList<Transaction>();

        this.currentUser = null;

        //TODO: read in data from file sources
    }

    /**
     * Returns the current user.
     *
     * @return The User currently logged in
     */
    public User getCurrentUser() {
        return this.currentUser;
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

        return null;
    }

    /**
     * Attempts to log in a user. If a user with the specified email is present in the system, the
     * currentUser field is updated to equal that user. Otherwise, the field is not updated.
     *
     * @param email The email of the new currentUser
     * @return Whether or not the operation was successful
     */
    public boolean setCurrentUser(String email) {
        User user = this.getUser(email);

        if (user == null) {
            return false;
        } else {
            this.currentUser = user;
            return true;
        }
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
        User user = this.getUser(email);

        if (user != null && user.getPassword().equals(password)) {
            return true;
        }

        return false;
    }

    /**
     * Deletes the current user from the system if the user is logged in.
     */
    public void deleteCurrentUser() {
        if (this.currentUser != null) {
            this.users.remove(this.currentUser);
            this.currentUser = null;
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

        if (existingUser == null) {
            this.users.add(user);
            this.setCurrentUser(user.getEmail());
        }
    }

    /**
     * If a User is logged in, sets the currentUser's email and password to the given parameters
     *
     * @param newEmail
     * @param newPassword
     */
    public void editCurrentUser(String newEmail, String newPassword) {
        if (this.currentUser != null) {
            this.currentUser.setEmail(newEmail);
            this.currentUser.setPassword(newPassword);
        }
    }

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

        if (by == BY_NOTHING || sort == NOT_SORTED) {
            results = notSorted;
        } else {
            int counter = 0;
            boolean[] entered = new boolean[notSorted.size()];

            if (by == BY_QUANTITY) {
                int threshold = -1;

                if (sort == SORTED_DESC) {
                    threshold = Integer.MAX_VALUE;
                }

                while (counter < notSorted.size()) {
                    while (!entered[counter]) {
                        counter++;
                    }

                    if ((sort == SORTED_ASC && notSorted.get(counter).getQuantity() >= threshold)
                            || (sort == SORTED_DESC && notSorted.get(counter).getQuantity() <= threshold)) {
                        threshold = notSorted.get(counter).getQuantity();
                        results.add(notSorted.get(counter));
                        entered[counter] = true;
                        counter = 0;
                    }
                }
            } else if (by == BY_PRICE) {
                double threshold = -1.0;

                if (sort == SORTED_DESC) {
                    threshold = Float.MAX_VALUE;
                }

                while (counter < notSorted.size()) {
                    while (!entered[counter]) {
                        counter++;
                    }

                    if ((sort == SORTED_ASC && notSorted.get(counter).getPrice() >= threshold)
                            || (sort == SORTED_DESC && notSorted.get(counter).getPrice() <= threshold)) {
                        threshold = notSorted.get(counter).getPrice();
                        results.add(notSorted.get(counter));
                        entered[counter] = true;
                        counter = 0;
                    }
                }
            }
        }

        return results;
    }

    /**
     * @param id
     * @return The product with the given id if it exists, or null if not
     */
    public Product getProduct(int id) {
        for (int i = 0; i < this.products.size(); i++) {
            if (this.products.get(i).getId() == id) {
                return this.products.get(i);
            }
        }

        return null;
    }

    /**
     * Adds a product to the marketplace if the currentUser is not null and is a Seller, and if
     * the provided product is not already on the marketplace.
     *
     * @param product
     */
    public void addProduct(Product product) {
        if (this.currentUser != null && this.currentUser instanceof Seller) {
            if (this.getProduct(product.getId()) == null) {
                this.products.add(product);
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
    public void editProduct(int id, String name, String description, int quantity, double price) {
        if (this.currentUser != null && this.currentUser instanceof Seller) {
            Product product = this.getProduct(id);

            if (this.currentUserOwnsStore(this.getStore(product.getId()))) {
                product.setName(name);
                product.setDescription(description);
                product.setQuantity(quantity);
                product.setPrice(price);
            }
        }
    }

    /**
     * Deletes a product. This is permitted only if the given product belongs to the current user.
     *
     * @param id The id of the product
     */
    public void deleteProduct(int id) {
        if (this.currentUser != null && this.currentUser instanceof Seller) {
            Product product = this.getProduct(id);

            if (this.currentUserOwnsStore(this.getStore(product.getId()))) {
                this.products.remove(product);
            }
        }
    }

    /**
     * @return An ArrayList of Stores for which the Seller associated with each store is the current
     * user
     */
    public ArrayList<Store> getOwnedStores() {
        ArrayList<Store> results = new ArrayList<Store>();

        for (int i = 0; i < this.stores.size(); i++) {
            if (this.currentUserOwnsStore(this.stores.get(i))) {
                results.add(this.stores.get(i));
            }
        }

        return results;
    }

    /**
     * @param store
     * @return true if the current user owns the given store, false otherwise
     */
    public boolean currentUserOwnsStore(Store store) {
        if (this.currentUser != null) {
            if (store.getSellerEmail().equals(this.currentUser.getEmail())) {
                return true;
            }
        }

        return false;
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

        return null;
    }

    /**
     * Adds a store to the marketplace if it does not already exist.
     *
     * @param store
     */
    public void addStore(Store store) {
        Store existingStore = this.getStore(store.getId());

        if (existingStore == null) {
            this.stores.add(store);
        }
    }

    /**
     * Deletes the store with the given id. Permitted only if the current user owns the store.
     *
     * @param id
     */
    public void deleteStore(int id) {
        Store existingStore = this.getStore(id);

        if (existingStore != null && this.currentUserOwnsStore(existingStore)) {
            this.stores.remove(existingStore);
        }
    }

    /**
     * Edits the store with the given id. Permitted only if the current user owns the store.
     *
     * @param id
     * @param name
     */
    public void editStore(int id, String name) {
        Store store = this.getStore(id);

        if (store != null) {
            store.setName(name);
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
    public void makePurchase(Product product, int quantity) throws InvalidQuantityError {
        if (product.checkQuantity(quantity)) {
            product.purchase(quantity);
            Transaction transaction = new Transaction(product.getId(), product.getStoreId(),
                    this.currentUser.getEmail(), this.getStore(product.getStoreId()).getSellerEmail(), 
                    quantity, product.getPrice());
            this.transactions.add(transaction);
        } else {
            throw new InvalidQuantityError("Invalid purchase quantity");
        }
    }

    /**
     * Gets the purchase history of the current user.
     * 
     * @return A list of Transactions where the customer making the purchase equals the current user
     */
    public ArrayList<Transaction> getPurchaseHistory() {
        if (this.currentUser != null && this.currentUser instanceof Customer) {
            ArrayList<Transaction> results = new ArrayList<Transaction>();

            for (int i = 0; i < this.transactions.size(); i++) {
                if (this.transactions.get(i).getCustomerEmail().equals(this.currentUser.getEmail())) {
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
    public ArrayList<String[]> getSaleData(Store store) {
        if (this.currentUser != null && this.currentUser instanceof Seller &&
                store.getSellerEmail().equals(this.currentUser.getEmail())) {
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
    public ArrayList<String[]> getSellerShoppingCartView() {
        if (this.currentUser != null && this.currentUser instanceof Seller) {
            ArrayList<String[]> data = new ArrayList<String[]>();

            for (int i = 0; i < this.users.size(); i++) {
                if (this.users.get(i) instanceof Customer) {
                    ArrayList<Integer> productIds = ((Customer) this.users.get(i)).getIds();
                    ArrayList<Integer> productQuantities = 
                        ((Customer) this.users.get(i)).getQuantities();

                    for (int j = 0; j < productIds.size(); j++) {
                        Product product = this.getProduct(((int) productIds.get(j)));

                        if (this.getStore(product.getStore()).getSellerEmail()
                            .equals(this.currentUser.getEmail())) {
                                String[] datum = {
                                    this.users.get(i).getEmail(),
                                    this.getProduct(productIds.get(j)).getName(),
                                    this.getStore(product.getStore()).getName(),
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
