import java.util.ArrayList;

/**
 * Project-04 – StoreUnitTest
 *
 *
 * @author Gunho Park, Seth Hartzler, Isaac Shane, Tru Bui; Lab - L16
 *

 * @version Nov. 13, 2023
 *
 */
public class StoreUnitTest {
    public static void main(String[] args) {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        arr.add(1);
        Store store = new Store(arr, "Target", "target@gmail.com", 12345);
        System.out.println(store.getName().equals("Target"));
        System.out.println(store.getSellerEmail().equals("target@gmail.com"));
        System.out.println(store.getId() == 12345);
        System.out.println(store.getProducts() == arr);
        store.setId(54321);
        System.out.println(store.getId() == 54321);
        store.setName("Walmart");
        System.out.println(store.getName().equals("Walmart"));
        ArrayList<Integer> arr2 = new ArrayList<Integer>();
        arr2.add(2);
        store.setProducts(arr2);
        System.out.println(store.getProducts() == arr2);
        store.setSellerEmail("walmart@gmail.com");
        System.out.println(store.getSellerEmail().equals("walmart@gmail.com"));

        System.out.println(store.toStringFormat().equals("Walmart,walmart@gmail.com,54321,2"));
        System.out.println(Store.fromStringFormat("Walmart,walmart@gmail.com,54321,2").getName().equals("Walmart"));
        System.out.println(Store.fromStringFormat("Walmart,walmart@gmail.com,54321,2").getId() == 54321);
        System.out.println(Store.fromStringFormat("Walmart,walmart@gmail.com,54321,2").getSellerEmail().equals("walmart@gmail.com"));
        System.out.println(Store.fromStringFormat("Walmart,walmart@gmail.com,54321,2").getProducts().get(0) == 2);
        System.out.println(Store.fromStringFormat("Walmart,walmart@gmail.com,54321,2").toStringFormat().equals("Walmart,walmart@gmail.com,54321,2"));

    }
}
