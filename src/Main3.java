import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
/**
 * Main method which is the UI for every user
 */
public class Main3 {
    static DataManager dataManager = new DataManager();
    static Store currentStore;

    public static void main(String[] args) throws InvalidQuantityError, InvalidPriceError {
        initialize();
    }
    public static String scan() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
    public static void initialize() throws InvalidQuantityError, InvalidPriceError {
        dataManager.logoutCurrentUser();
        System.out.println("Choose option:\n" +
                "[1] Log in\n" +
                "[2] Sign up\n" +
                "[3] Exit");
        int input = 0;
        try {
            input = Integer.parseInt(scan());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
            initialize();
        }
        switch (input) {
            case 1:
                login();
                break;
            case 2 :
                signup();
                break;
            case 3:
                dataManager.saveToFile();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input");
                initialize();
                break;
        }
    }
    public static void login() throws InvalidQuantityError, InvalidPriceError {
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
    public static void signup() throws InvalidQuantityError, InvalidPriceError {
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
    public static void customer() throws InvalidQuantityError, InvalidPriceError {
        System.out.println(
                "[1] Go to cart\n" +
                        "[2] Search products\n"+
                        "[3] See purchase history\n" +
                        "[4] Log out");
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
                initialize();
                break;
            default:
                System.out.println("Input invalid: please choose 1, 2, 3, or 4.");
                customer();
                break;
        }
    }
    public static void seeCart() throws InvalidQuantityError, InvalidPriceError {
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
                                    System.out.println(dataManager.getProduct(((Customer) dataManager.getCurrentUser()).getIds().get(i)).getName() + "purchase complete!");
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
    public static void search() {
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
    public static void seeCustomerPurchaseHistory() {
        System.out.println("Past Transactions:");
        if (dataManager.getPurchaseHistory().isEmpty()) {
            System.out.println("You have no past transactions");
        } else {
            System.out.println("Past transactions: ");
            for (int i = 0; i < dataManager.getPurchaseHistory().size(); i++) {
                System.out.println(dataManager.getPurchaseHistory().get(i).toString());
            }
        }
    }
    public static void seller() throws InvalidQuantityError, InvalidPriceError {
        boolean running = true;
        while (running) {
            System.out.println("""
                    [1] Stores Options
                    [2] View Data
                    [3] Log Out
                    """);
            int input = 1;
            try {
                input = Integer.parseInt(scan());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: please enter 1, 2, or 3");
                continue;
            }
            switch (input) {
                case 1:
                    storeOptions();
                    seller();
                    running = false;
                    break;
                case 2:
                    System.out.println("""
                            [1] View popular product data
                                (Shows all your products which are in customer's shopping cart.
                                Shows email of the customer, product, store, and quantity in cart)
                            [2] View Store Sales
                                (Gets sale data (receipts) for a particular store.
                                Shows customer's email and how much they spent on the transaction)
                            """);
                    int option = 1;
                    do {
                        try {
                            input = Integer.parseInt(scan());
                            break;
                        } catch (Exception e) {
                            System.out.println("Invalid input");
                        }
                    } while (true);
                    switch (option) {
                        case 1:
                            productData();
                            break;
                        case 2:
                            storeSalesData();
                            break;
                        default:
                            System.out.println("Invalid input, please input 1 or 2");
                            seller();
                    }
                    seller();
                    running = false;
                    break;
                case 3:
                    running = false;
                    initialize();
                    break;
                default:
                    System.out.println("Invalid input: please enter 1, 2, or 3");
                    break;
            }
        }
    }
    public static void storeOptions() throws InvalidQuantityError, InvalidPriceError {
        System.out.println("""
                [1] Create new store
                [2] Edit/Delete store
                [3] Back to seller menu
                """);

        int input = 3;
        do {
            try {
                input = Integer.parseInt(scan());
                break;
            } catch (Exception e) {
                System.out.println("Invalid input");
            }
        } while (true);

        switch (input) {
            case 1:
                System.out.println("Create store name: ");
                String storeName = scan();
                ArrayList<Integer> empty = new ArrayList<>();
                dataManager.addStore(new Store(empty, storeName, dataManager.getCurrentUser().getEmail(), dataManager.getCurrentStoreId()));
                dataManager.incrementCurrentStoreId();
                System.out.println("Store has been created");
                System.out.println("If you would like to add products go to edit store");
                seller();
                break;
            case 2:
                System.out.println("Which store would you like to edit?");
                for (int i = 0; i < dataManager.getOwnedStores().size(); i++) {
                    System.out.println("[" + (i + 1) + "] " + dataManager.getOwnedStores().get(i).getName());
                }
                int indexOfStore = 0;
                do {
                    try {
                        indexOfStore = Integer.parseInt(scan()) - 1;
                        break;
                    } catch (Exception e) {
                        System.out.println("Invalid input");
                    }
                } while (true);
                editStore(indexOfStore);
                break;
            case 3:
                seller();
                break;
            default:
                System.out.println("Invalid input: please input 1, 2, or 3");
                storeOptions();
                break;
        }
    }
    public static void editStore(int indexOfStore) throws InvalidPriceError, InvalidQuantityError {
        Store store = dataManager.getOwnedStores().get(indexOfStore);
        int storeId = store.getId();
        System.out.println("""
                [1] Change store name
                [2] Add new product
                [3] Delete product
                [4] Delete store
                [5] Edit product
                """);
        int option = 1;

        do {
            try {
                option = Integer.parseInt(scan());
                break;
            } catch (Exception e) {
                System.out.println("Invalid input");
            }
        } while (true);
        String name;
        switch (option) {
            case 1:
                System.out.println("Enter new store name: ");
                name = scan();

                dataManager.editStore(storeId, name);
                break;
            case 2:
                try {
                    System.out.println("Name of product: ");
                    name = scan();
                    System.out.println("Description of product: ");
                    String description = scan();
                    System.out.println("Quantity in stock: ");
                    int quantity = 1;

                    do {
                        try {
                            quantity = Integer.parseInt(scan());
                            break;
                        } catch (Exception e) {
                            System.out.println("Invalid input");
                        }
                    } while (true);
                    System.out.println("Price of product: ");
                    double price = Double.parseDouble(scan());
                    Product product = new Product(name, description, storeId,
                            quantity, price);
                    dataManager.addProduct(product);
                    System.out.println("Product added!");
                } catch (InvalidPriceError e) {
                    System.out.println("Invalid price!");
                }
                seller();
                break;
            case 3:
                if (store.getProducts().isEmpty()) {
                    System.out.println("No products in Store");
                    seller();
                } else {
                    System.out.println("Which product would you like to delete?");
                    for (int i = 0; i < store.getProducts().size(); i++) {
                        System.out.println("[" + (i + 1) + "] " + dataManager.getProduct(store.getProducts().get(i)).getName());
                    }
                    int indexOfProduct = Integer.parseInt(scan());
                    do {
                        try {
                            indexOfProduct = Integer.parseInt(scan());
                            break;
                        } catch (Exception e) {
                            System.out.println("Invalid input");
                        }
                    } while (true);
                    dataManager.deleteProduct(store.getProducts().get(indexOfProduct - 1));
                }
                break;
            case 4:
                dataManager.deleteStore(storeId);
                break;
            case 5:
                Store currentStore = dataManager.getStore(storeId);
                ArrayList<Product> storeProducts = dataManager.getStoreProducts(currentStore);

                if (storeProducts.size() == 0) {
                    System.out.println("No products currently associated with this store");
                    return;
                }
                System.out.println("Select a product");
                for (int j = 0; j < dataManager.getStoreProducts(currentStore).size(); j++) {
                    System.out.println("[" + (j + 1) + "] " + storeProducts.get(j).getName());
                }
                System.out.println("Which product do you want to edit?");
                int editProductIndex = 0;

                do {
                    try {
                        editProductIndex = Integer.parseInt(scan()) - 1;

                        if (editProductIndex < 0 || editProductIndex >= storeProducts.size()) {
                            System.out.printf("Enter a valid integer from 0-%d\n", 
                                storeProducts.size() - 1);
                            continue;
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("Enter a valid integer input");
                    }
                } while (true);

                int editProductID = storeProducts.get(editProductIndex).getId();
                System.out.println("Provide new NAME for the product:");
                String newProductName = scan();
                System.out.println("Provide new DESCRIPTION for the product");
                String newProductDescription = scan();
                System.out.println("Provide new QUANTITY for the product");
                int newProductQuantity = 1;

                do {
                    try {
                        newProductQuantity = Integer.parseInt(scan());

                        if (newProductQuantity < 0) {
                            System.out.println("Invalid quantity");
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("Invalid input");
                    }
                } while (true);
                System.out.println("Provide new PRICE for the product");
                double newProductPrice = 1.0;
                do {
                    try {
                        newProductPrice = Float.parseFloat(scan());

                        if (newProductPrice <= 0.0) {
                            System.out.println("Invalid price");
                            continue;
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("Invalid input");
                    }
                } while (true);
                try {
                    dataManager.editProduct(editProductID, newProductName, newProductDescription, newProductQuantity, newProductPrice);
                    System.out.println("Edit SUCCESSFUL");
                } catch (Exception e) {
                    System.out.println("Error occurred while editing product");
                }
                break;
            default:
                System.out.println("Invalid input: please enter 1, 2, 3, or 4");
                break;
        }
    }
    public static void storeSalesData() {
        System.out.println("Select the store from which you want sales data:");
        for (int i = 0; i < dataManager.getOwnedStores().size(); i++) {
            System.out.println("[" + (i + 1) + "] " + dataManager.getOwnedStores().get(i).getName());
        }
        int indexOfStore = 1;

        do {
            try {
                indexOfStore = Integer.parseInt(scan()) - 1;
                break;
            } catch (Exception e) {
                System.out.println("Invalid input");
            }
        } while (true);
        ArrayList<String[]> salesData = dataManager.getSaleData(dataManager.getOwnedStores().get(indexOfStore));
        System.out.println("Past transactions:");
        for (int i = 0; i < salesData.size(); i++) {
            System.out.println(Arrays.toString(salesData.get(i))); //  prints a reference ??
        }
    }
    public static void productData() {
        ArrayList<String[]> totalData = dataManager.getSellerShoppingCartView();
        if (totalData.isEmpty()) {
            System.out.println("No products are currently in any customers' cart");
        } else {
            System.out.println("Data format:\nCustomer email, product name, store name, quantity sold.");
            for (int i = 0; i < totalData.size(); i++) {
                System.out.println(Arrays.toString(totalData.get(i)));
            }
        }
    }

    public static void putInCart() {
        System.out.println("Would you like to put an item in your cart?");
        System.out.println("[1] Yes" +
                "\n[2] No");
        int input = 2;

        do {
            try {
                input = Integer.parseInt(scan());
                break;
            } catch (Exception e) {
                System.out.println("Invalid input");
            }
        } while (true);

        switch (input) {
            case 1:
                System.out.println("Enter the name of the product which you would like to put in cart:");
                String search = scan();
                ArrayList<Product> results = dataManager.search(search);

                if (results.size() == 0) {
                    System.out.println("No such product found!");
                    return;
                }

                System.out.println("Confirmation of the product which you would like to move to your cart:");
                for (int i = 0; i < results.size(); i++) {
                    System.out.println("[" + (i + 1) + "] " + results.get(i).getName());
                }
                int quantity;

                boolean gotIntInput = false;
                int index = 1;
                do {
                    try {
                        index = Integer.parseInt(scan()) - 1;
                        gotIntInput = true;
                    } catch (Exception e) {
                        System.out.println("Enter an integer input!");
                    }
                } while (!gotIntInput);

                while (true) {
                    System.out.println("How many would you like to add to cart?");
                    try {
                        quantity = Integer.parseInt(scan());
                        if (quantity > 0) {
                            break;
                        }
                    } catch (Exception e) {}
                    System.out.println("Please enter an integer quantity which is greater than 0");
                }
                Product product = dataManager.search(search).get(index);
                if (dataManager.getCurrentUser() instanceof Customer) {
                    ((Customer) dataManager.getCurrentUser()).addProduct(product.getId(), quantity);
                    System.out.println("Item successfully added");
                }
                break;
            case 2:
                break;
            default:
                System.out.println("Invalid input: please input 1 or 2");
                putInCart();
                break;
        }
    }

}
