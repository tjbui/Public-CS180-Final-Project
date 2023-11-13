import java.util.ArrayList;
import java.util.Scanner;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean run = false;


        String email = "";
        String password = "";


        do {
            DataManager dataManager = new DataManager();
            System.out.println("Welcome to Amazon!");
            String userStatus = "";
            do {
                System.out.println("Press 1 for LOGIN, 2 for SIGN UP, 3 for EXIT");
                userStatus = scanner.nextLine();


            } while (!(userStatus.equals("1")) && !(userStatus.equals("2")) && !(userStatus.equals("3")));

            boolean repeat = true;


            if (userStatus.equals("1")) {
                do {
                    System.out.println("Enter email:");
                    email = scanner.nextLine();
                    System.out.println("Enter password: ");
                    password = scanner.nextLine();


                } while (!dataManager.checkUserLogin(email, password));


            } else if (userStatus.equals("2")) {
                System.out.println("Enter email:");
                email = scanner.nextLine();
                System.out.println("Enter password:");
                password = scanner.nextLine();
                String sellerOrCustomer = "";
                do {
                    System.out.println("Press 1 if you are a SELLER, 2 if you are a CUSTOMER");
                    sellerOrCustomer = scanner.nextLine();
                } while (!(sellerOrCustomer.equals("1")) && !(sellerOrCustomer.equals("2")));


                User user;
                if (sellerOrCustomer.equals("1")) {//seller
                    user = new Seller(email, password, new ArrayList<Integer>());
                } else {
                    user = new Customer(email, password, new ArrayList<Integer>(), new ArrayList<Integer>());
                }


                dataManager.addUser(user);


            } else {
                dataManager.saveToFile();
                System.out.println("Goodbye!");
                run = false;
                repeat = false;
            }


            dataManager.setCurrentUser(email);
            User currentUser = dataManager.getCurrentUser();



            /*
            Here starts options given if the user is a seller.
             */

            while (repeat) {
                if (currentUser instanceof Seller) {
                    String sellerOption;

                    do {
                        System.out.println("Press 1 to create product");
                        System.out.println("Press 2 to edit product");
                        System.out.println("Press 3 to delete product");
                        System.out.println("Press 4 to view a list of sales");
                        System.out.println("Press 5 to go back");
                        sellerOption = scanner.nextLine();


                    } while (!(sellerOption.equals("1")) && !(sellerOption.equals("2")) && !(sellerOption.equals("3")) && !(sellerOption.equals("4")) && !(sellerOption.equals("5")));

                    if (!sellerOption.equals("5")) {
                        ArrayList<Store> stores = dataManager.getOwnedStores();
                        boolean storeSelected = false;
                        Store currentStore = new Store();
                        if (stores.size() == 0) {
                            System.out.println("No stores owned");
                        } else {
                            do {
                                System.out.println("Select a store by providing the ID");
                                for (int i = 0; i < stores.size(); i++) {
                                    System.out.println(stores.get(i).toStringFormat());
                                    String inputID = scanner.nextLine();
                                    int targetID;
                                    try {
                                        targetID = Integer.parseInt(inputID);
                                        currentStore = dataManager.getStore(targetID);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        storeSelected = true;
                                    }
                                }
                            } while (storeSelected);

                            if (sellerOption.equals("1")) {
                                System.out.println("Provide NAME of the product:");
                                String productName = scanner.nextLine();
                                System.out.println("Provide DESCRIPTION of the product:");
                                String productDescription = scanner.nextLine();
                                System.out.println("Provide QUANTITY of the product:");
                                int quantity = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Provide PRICE of the product");
                                double price = scanner.nextDouble();
                                scanner.nextLine();
                                System.out.println("Provide ID of the product");
                                int addProductID = scanner.nextInt();
                                scanner.nextLine();
                                try {
                                    dataManager.addProduct(new Product(productName, productDescription, currentStore.getId(), quantity, price, addProductID));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                            if (sellerOption.equals("2")) {
                                System.out.println("Select a product");
                                for (int j = 0; j < dataManager.getStoreProducts(currentStore).size(); j++) {
                                    System.out.println(dataManager.getStoreProducts(currentStore).get(j).toStringFormat());
                                }
                                System.out.println("Provide the ID of the product you wish to edit");
                                int editProductID = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Provide new NAME for the product:");
                                String newProductName = scanner.nextLine();
                                System.out.println("Provide new DESCRIPTION for the product");
                                String newProductDescription = scanner.nextLine();
                                System.out.println("Provide new QUANTITY for the product");
                                int newProductQuantity = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Provide new PRICE for the product");
                                double newProductPrice = scanner.nextDouble();
                                scanner.nextLine();
                                try {
                                    dataManager.editProduct(editProductID, newProductName, newProductDescription, newProductQuantity, newProductPrice);
                                    System.out.println("Edit SUCCESSFUL");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            if (sellerOption.equals("3")) {
                                System.out.println("Select a product");
                                for (int k = 0; k < dataManager.getStoreProducts(currentStore).size(); k++) {
                                    System.out.println(dataManager.getStoreProducts(currentStore).get(k).toStringFormat());
                                }
                                System.out.println("Provide the ID of the product you wish to edit");
                                int deleteProductID = scanner.nextInt();
                                scanner.nextLine();
                                try {
                                    dataManager.deleteProduct(deleteProductID);
                                    System.out.println("Delete SUCCESSFUL");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if (sellerOption.equals("4")) {
                                ArrayList<String[]> saleDatas = dataManager.getSaleData(currentStore);
                                if (saleDatas.size() == 0) {
                                    System.out.println("No sale data");
                                } else {
                                    for (int ll = 0; ll < saleDatas.size(); ll++) {
                                        String[] saleData = saleDatas.get(ll);
                                        System.out.println(saleData[0] + " : " + saleData[1]);
                                    }
                                }
                            }
                        }

                        String repeatStatusSeller;
                        do {
                            System.out.println("Press 1 if you would like to REPEAT, 2 to Go back to the Login Screen, 3 to EXIT");
                            repeatStatusSeller = scanner.nextLine();
                        } while (!(repeatStatusSeller.equals("1")) && !(repeatStatusSeller.equals("2")) && !(repeatStatusSeller.equals("3")));

                        if (repeatStatusSeller.equals("1")) {
                            repeat = true;
                        } else if (repeatStatusSeller.equals("2")){
                            repeat = false;
                            run = true;
                            dataManager.saveToFile();
                        } else {
                            repeat = false;
                            run = false;
                            dataManager.saveToFile();
                            System.out.println("GOODBYE!");
                        }

                    } else {
                        run = true;
                        repeat = false;
                    }

                }


                /*
                From Here starts options given if the user if a customer

                 */
                if (currentUser instanceof Customer) {

                    String customerOption;
                    do {
                        System.out.println("Press 1 to view overall marketplace listing products for sale");
                        System.out.println("Press 2 to search for specific products");
                        System.out.println("Press 3 to sort the marketplace on price or quantity available");
                        System.out.println("Press 4 to edit shopping cart");
                        System.out.println("Press 5 to purchase items");
                        System.out.println("Press 6 to review previously purchased items");
                        System.out.println("Press 7 to go back");
                        customerOption = scanner.nextLine();


                    } while (!(customerOption.equals("1")) && !(customerOption.equals("2")) && !(customerOption.equals("3")) && !(customerOption.equals("4")) && !(customerOption.equals("5")) && !(customerOption.equals("6")) && !(customerOption.equals("7")));

                    if (!customerOption.equals("7")) {
                        if (customerOption.equals("1")) {
                            ArrayList<Product> allProducts = dataManager.getProductList();
                            for (int l = 0; l < allProducts.size(); l++) {
                                System.out.println(allProducts.get(l).toStringFormat());
                            }
                        }

                        if (customerOption.equals("2")) {
                            System.out.println("Provide a keyword");
                            String keyword = scanner.nextLine();
                            ArrayList<Product> targetProducts = dataManager.search(keyword);
                            if (targetProducts.size() == 0) {
                                System.out.println("No product found with the keyword " + keyword);
                            } else {
                                for (int ii = 0; ii < targetProducts.size(); ii++) {
                                    System.out.println(targetProducts.get(ii).toStringFormat());
                                }
                            }
                        }

                        if (customerOption.equals("3")) {
                            String sortKeyword;
                            do {
                                System.out.println("Press 1 to sort by quantity");
                                System.out.println("Press 2 to sort by price");

                                sortKeyword = scanner.nextLine();

                            } while (!(sortKeyword.equals("1")) && !(sortKeyword.equals("2")));

                            String sortMethod;
                            do {
                                System.out.println("Press 1 to sort in an ascending order");
                                System.out.println("Press 2 to sort in a descending order");
                                sortMethod = scanner.nextLine();
                            } while (!(sortMethod.equals("1")) && !(sortMethod.equals("2")));

                            ArrayList<Product> sortedProducts = dataManager.getProductList(Integer.parseInt(sortKeyword), Integer.parseInt(sortMethod));
                            for (int jj = 0; jj < sortedProducts.size(); jj++) {
                                System.out.println(sortedProducts.get(jj).toStringFormat());
                            }

                        }

                        if (customerOption.equals("4")) {

                            ArrayList<Integer> currentShoppingCartIDs = ((Customer) currentUser).getIds();
                            ArrayList<Integer> currentShoppingCartQuantities = ((Customer) currentUser).getQuantities();
                            String shoppingCartStatus = "";
                            do {
                                System.out.println("Current Shopping Cart List: ");
                                for (int iii = 0; iii < currentShoppingCartIDs.size(); iii++) {
                                    System.out.println(dataManager.getProduct(currentShoppingCartIDs.get(iii)).getName() + " : " + currentShoppingCartQuantities);
                                }

                                System.out.println("Press 1 to edit shopping cart, 2 to complete shopping cart");
                                shoppingCartStatus = scanner.nextLine();

                                if (shoppingCartStatus.equals("1")) {
                                    System.out.println("Provide the id of the product: ");
                                    int productID;
                                    boolean productFound = false;
                                    boolean cancelCode = false;
                                    do {
                                        productID = scanner.nextInt();
                                        scanner.nextLine();
                                        for (int findProduct = 0; findProduct < dataManager.getProductList().size(); findProduct++) {
                                            if (dataManager.getProductList().get(findProduct).getId() == productID) {
                                                productFound = true;
                                            }
                                        }

                                        if (productID == 0) {
                                            productFound = true;
                                            cancelCode = true;
                                        }

                                        if (productFound == false) {
                                            System.out.println("This is an invalid Product ID, provide a valid Product ID. Press 0 to cancel");
                                        }
                                    } while (!productFound);

                                    if (!cancelCode) {
                                        int indexProduct = ((Customer) currentUser).getIds().indexOf(productID);
                                        System.out.println("Set a quantity");
                                        int shopQuantity = scanner.nextInt();
                                        scanner.nextLine();
                                        if (indexProduct == -1) {
                                            ((Customer) currentUser).addProduct(productID, shopQuantity);
                                        } else {
                                            ((Customer) currentUser).getQuantities().set(indexProduct, shopQuantity);
                                        }
                                    }
                                }

                            } while (!(shoppingCartStatus.equals("1")) && !(shoppingCartStatus.equals("2")));
                        }

                        if (customerOption.equals("5")) {
                            for (int cartList = 0; cartList < ((Customer) currentUser).getIds().size(); cartList++) {
                                try {
                                    dataManager.makePurchase(dataManager.getProduct(((Customer) currentUser).getIds().get(cartList)), ((Customer) currentUser).getQuantities().get(cartList));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if (customerOption.equals("6")) {
                            ArrayList<Transaction> purchaseHistory = dataManager.getPurchaseHistory();
                            for (int kk = 0; kk < purchaseHistory.size(); kk++) {
                                System.out.println(purchaseHistory.get(kk));
                            }
                        }

                        String repeatStatusCustomer;
                        do {
                            System.out.println("Press 1 if you would like to REPEAT, 2 to Go back to the Login Screen, 3 to EXIT");
                            repeatStatusCustomer = scanner.nextLine();
                        } while (!(repeatStatusCustomer.equals("1")) && !(repeatStatusCustomer.equals("2")) && !(repeatStatusCustomer.equals("3")));

                        if (repeatStatusCustomer.equals("1")) {
                            repeat = true;
                        } else if (repeatStatusCustomer.equals("2")){
                            repeat = false;
                            run = true;
                            dataManager.saveToFile();
                        } else {
                            repeat = false;
                            run = false;
                            dataManager.saveToFile();
                            System.out.println("GOODBYE!");
                        }

                    } else {
                        run = true;
                        repeat = false;
                    }


                }
            };

        } while (run);




    }
}

