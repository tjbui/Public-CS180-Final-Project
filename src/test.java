import java.util.ArrayList;

/**
 * Project-04 – CustomerUnitTest
 *
 *
 * @author Gunho Park, Seth Hartzler, Isaac Shane, Tru Bui; Lab - L16
 *

 * @version Nov. 13, 2023
 *
 */
public class test {
    public static void main(String[] args) {
        DataManager dataManager = new DataManager();
        ArrayList<Integer> storeIds = new ArrayList<Integer>();
        ArrayList<Integer> productArr = new ArrayList<Integer>();
        Store store = new Store(productArr,"poop","email",1);
        dataManager.addStore(store);
        storeIds.add(1);
        Seller seller = new Seller("email","pass",storeIds);
        dataManager.addUser(seller);
        System.out.println(dataManager.setCurrentUser("email"));
        dataManager.loadProductsFromFile("C:\\Users\\tjtbu\\IdeaProjects\\Project4\\src\\products.csv");
        System.out.println(dataManager.getProductList());
    }
}
