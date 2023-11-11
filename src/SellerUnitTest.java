import java.util.ArrayList;


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
