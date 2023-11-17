import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ServerThread extends Thread {
    private Socket socket;
    private DataManager dm;
    private User currentUser;
    
    private Scanner s;
    private PrintWriter pw;

    private boolean running;

    public ServerThread(Socket socket, DataManager dm) {
        this.socket = socket;
        this.dm = dm;
        this.currentUser = null;

        try {
            this.s = new Scanner(socket.getInputStream());
            this.pw = new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("Connection error encountered");
        }

        this.running = true;
    }

    public boolean isRunning() {
        return this.running;
    }
    
    public void run() {
        String command = "";

        do {
            command = this.s.nextLine();

            if (command.equals("save")) {
                this.dm.saveToFile();
            } else if (command.equals("loadProducts")) {
                String filename = this.s.nextLine();

                this.dm.loadProductsFromFile(currentUser, filename);
            } else if (command.equals("exportPurchases")) {
                String filename = this.s.nextLine();

                this.dm.exportPurchaseHistory(currentUser, filename);
            } else if (command.equals("exportProducts")) {
                String filename = this.s.nextLine();

                this.dm.exportProductData(currentUser, filename);
            } else if (command.equals("getCurrentStoreId")) {
                int currentStoreId = this.dm.getCurrentStoreId();

                this.pw.println(currentStoreId);
            } else if (command.equals("incrementCurrentStoreId")) {
                this.dm.incrementCurrentStoreId();
            } else if (command.equals("getUser")) {
                String email = this.s.nextLine();

                User user = this.dm.getUser(email);

                this.pw.println(user.toStringFormat());
            } else if (command.equals("userLogin")) {
                String email = this.s.nextLine();
                String password = this.s.nextLine();

                boolean success = this.dm.checkUserLogin(email, password);

                if (success) {
                    this.currentUser = this.dm.getUser(email);
                }

                this.pw.println(success);
            } else if (command.equals("deleteCurrentUser")) {
                this.dm.deleteCurrentUser(this.currentUser);

                this.currentUser = null;
            } else if (command.equals("addUser")) {
                User newUser = User.fromStringFormat(this.s.nextLine());

                this.dm.addUser(newUser);
            } else if (command.equals("editCurrentUser")) {
                String newEmail = this.s.nextLine();
                String newPassword = this.s.nextLine();

                this.dm.editCurrentUser(this.currentUser, newEmail, newPassword);
            } else if (command.equals("getProductList")) {
                int by = Integer.parseInt(this.s.nextLine());
                int sort = Integer.parseInt(this.s.nextLine());

                ArrayList<Product> products = this.dm.getProductList(by, sort);

                for (int i = 0; i < products.size(); i++) {
                    this.pw.println(products.get(i).toStringFormat());
                }

                this.pw.println("END");
            } else if (command.equals("getProduct")) {
                int id = Integer.parseInt(this.s.nextLine());

                this.pw.println(this.dm.getProduct(id).toStringFormat());
            } else if (command.equals("addProduct")) {
                try {
                    Product newProduct = Product.fromStringFormat(this.s.nextLine());

                    this.dm.addProduct(currentUser, newProduct);
                } catch (Exception e) {}
            } else if (command.equals("editProduct")) {
                int id = Integer.parseInt(this.s.nextLine());
                String name = this.s.nextLine();
                String description = this.s.nextLine();
                int quantity = Integer.parseInt(this.s.nextLine());
                double price = Float.parseFloat(this.s.nextLine());

                try {
                    this.dm.editProduct(currentUser, id, name, description, quantity, price);
                } catch (Exception e) {}
            } else if (command.equals("deleteProduct")) {
                int id = Integer.parseInt(this.s.nextLine());

                this.dm.deleteProduct(currentUser, id);
            } else if (command.equals("getOwnedStores")) {
                ArrayList<Store> stores = this.dm.getOwnedStores(currentUser);

                for (int i = 0; i < stores.size(); i++) {
                    this.pw.println(stores.get(i).toStringFormat());
                }

                this.pw.println("END");
            } else if (command.equals("currentUserOwnsStore")) {
                Store store = Store.fromStringFormat(this.s.nextLine());

                boolean result = this.dm.currentUserOwnsStore(currentUser, store);

                this.pw.println(result);
            } else if (command.equals("getStoreProducts")) {
                Store store = Store.fromStringFormat(this.s.nextLine());

                ArrayList<Product> products = this.dm.getStoreProducts(currentUser, store);

                if (products != null) {
                    for (int i = 0; i < products.size(); i++) {
                        this.pw.println(products.get(i).toStringFormat());
                    }
                }

                this.pw.println("END");
            } else if (command.equals("getStore")) {
                int id = Integer.parseInt(this.s.nextLine());

                Store store = this.dm.getStore(id);

                this.pw.println(store.toStringFormat());
            } else if (command.equals("addStore")) {
                Store newStore = Store.fromStringFormat(this.s.nextLine());

                this.dm.addStore(newStore);
            } else if (command.equals("deleteStore")) {
                int id = Integer.parseInt(this.s.nextLine());

                this.dm.deleteStore(currentUser, id);
            } else if (command.equals("editStore")) {
                int id = Integer.parseInt(this.s.nextLine());
                String name = this.s.nextLine();

                this.dm.editStore(id, name);
            } else if (command.equals("search")) {
                String term = this.s.nextLine();

                ArrayList<Product> results = this.dm.search(term);

                for (int i = 0; i < results.size(); i++) {
                    this.pw.println(results.get(i).toStringFormat());
                }

                this.pw.println("END");
            } else if (command.equals("makePurchase")) {
                try {
                    Product product = Product.fromStringFormat(this.s.nextLine());
                    int quantity = Integer.parseInt(this.s.nextLine());

                    this.dm.makePurchase(currentUser, product, quantity);

                    this.pw.println("SUCCESS");
                } catch (Exception e) {
                    this.pw.println("FAIL");
                }
            } else if (command.equals("getPurchaseHistory")) {
                ArrayList<Transaction> transactions = this.dm.getPurchaseHistory(currentUser);

                if (transactions != null) {
                    for (int i = 0; i < transactions.size(); i++) {
                        this.pw.println(transactions.get(i).toStringFormat());
                    }
                }

                this.pw.println("END");
            } else if (command.equals("getSaleData")) {
                Store store = Store.fromStringFormat(this.s.nextLine());

                ArrayList<String[]> saleData = this.dm.getSaleData(currentUser, store);

                if (saleData != null) {
                    for (int i = 0; i < saleData.size(); i++) {
                        String line = "";

                        for (int j = 0; j < saleData.get(i).length; i++) {
                            line = line + saleData.get(i)[j] + ",";
                        }

                        line = line.substring(0, line.length() - 1);

                        this.pw.println(line);
                    }
                }

                this.pw.println("END");
            } else if (command.equals("getSellerShoppingCartView")) {
                ArrayList<String[]> data = dm.getSellerShoppingCartView(currentUser);

                if (data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        String line = "";

                        for (int j = 0; j < data.get(i).length; j++) {
                            line = line + data.get(i)[j] + ",";
                        }

                        line = line.substring(0, line.length() - 1);

                        this.pw.println(line);
                    }
                }

                this.pw.println("END");
            }
        } while (!command.equals("quit"));

        System.out.println("Closing connection...");

        dm.saveToFile();

        this.running = false;

        try {
            this.socket.close();
        } catch (Exception e) {
            System.out.println("Disconnection error encountered");
        }
    }
}
