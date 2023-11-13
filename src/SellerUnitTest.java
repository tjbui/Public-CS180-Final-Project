import java.util.ArrayList;

/**
 * Project-04 – SellerUnitTest
 *
 *
 * @author Gunho Park, Seth Hartzler, Isaac Shane, Tru Bui; Lab - L16
 *

 * @version Nov. 13, 2023
 *
 */
public class SellerUnitTest {
   public static void main(String[] args) {
       ArrayList<Integer> storeIDs = new ArrayList<>();
       Seller s = new Seller("email", "password", storeIDs);
       System.out.println(s.getEmail().equals("email"));
       System.out.println(s.getPassword().equals("password"));
       System.out.println(s.getStoreIDs().isEmpty());


       ArrayList<Integer> newStoreIds = new ArrayList<>();
       newStoreIds.add(1);
       s.setStoreIDs(newStoreIds);
       System.out.println(s.getStoreIDs().get(0) == 1);
   }
}
