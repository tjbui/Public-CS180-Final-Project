import java.net.Socket;
import java.util.ArrayList;
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
        this.pw.flush();

        try {
            this.socket.close();
        } catch (Exception e) {
            System.out.println("Disconnection error encountered");
        }
    }

    public void save() {
        this.pw.println("save");
        this.pw.flush();
    }

    public void loadProducts(String filename) {
        this.pw.println("loadProducts");

        this.pw.println(filename);

        this.pw.flush();
    }

    public void exportPurchases(String filename) {
        this.pw.println("exportPurchases");

        this.pw.println(filename);

        this.pw.flush();
    }

    public void exportProducts(String filename) {
        this.pw.println("exportProducts");

        this.pw.println(filename);

        this.pw.flush();
    }

    public int getCurrentStoreId() {
        this.pw.println("getCurrentStoreId");

        this.pw.flush();

        return Integer.parseInt(this.s.nextLine());
    }

    public void incrementCurrentStoreId() {
        this.pw.println("incrementCurrentStoreId");

        this.pw.flush();
    }

    public void logout() {
        this.pw.println("logout");

        this.pw.flush();
    }

    public User getUser(String email) {
        this.pw.println("getUser");

        this.pw.println(email);

        this.pw.flush();

        User user = User.fromStringFormat(this.s.nextLine());

        return user;
    }

    public boolean checkUserLogin(String email, String password) {
        this.pw.println("userLogin");
        this.pw.println(email);
        this.pw.println(password);

        this.pw.flush();

        return Boolean.parseBoolean(this.s.nextLine());
    }

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

    public void deleteCurrentUser() {
        this.pw.println("deleteCurrentUser");

        this.pw.flush();
    }

    public void addUser(User user) {
        this.pw.println("addUser");

        this.pw.println(user.toStringFormat());

        this.pw.flush();
    }

    public void editCurrentUser(String email, String password) {
        this.pw.println("editCurrentUser");

        this.pw.println(email);
        this.pw.println(password);

        this.pw.flush();
    }

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

    public void addProduct(Product product) {
        this.pw.println("addProduct");

        this.pw.println(product.toStringFormat());

        this.pw.flush();
    }

    public void editProduct(int id, String name, String description, int quantity, double price) {
        this.pw.println("editProduct");

        this.pw.println(id);
        this.pw.println(name);
        this.pw.println(description);
        this.pw.println(quantity);
        this.pw.println(price);

        this.pw.flush();
    }

    public void deleteProduct(int id) {
        this.pw.println("deleteProduct");

        this.pw.println(id);

        this.pw.flush();
    }

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

    public boolean currentUserOwnsStore(Store store) {
        this.pw.println("currentUserOwnsStore");

        this.pw.println(store.toStringFormat());

        this.pw.flush();

        return Boolean.parseBoolean(this.s.nextLine());
    }

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

    public Store getStore(int id) {
        this.pw.println("getStore");

        this.pw.println(id);

        this.pw.flush();

        return Store.fromStringFormat(this.s.nextLine());
    }

    public void addStore(Store store) {
        this.pw.println("addStore");

        this.pw.println(store.toStringFormat());

        this.pw.flush();
    }

    public void deleteStore(int id) {
        this.pw.println("deleteStore");

        this.pw.println(id);

        this.pw.flush();
    }

    public void editStore(int id, String name) {
        this.pw.println("editStore");

        this.pw.println(id);
        this.pw.println(name);

        this.pw.flush();
    }

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
