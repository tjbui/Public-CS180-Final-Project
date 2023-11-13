
/**
 * Project-04 – TransactionUnitTest
 *
 *
 * @author Gunho Park, Seth Hartzler, Isaac Shane, Tru Bui; Lab - L16
 *

 * @version Nov. 13, 2023
 *
 */
public class TransactionUnitTest {
   public static void main(String[] args) {
       Transaction t = new Transaction(0,1,"c email", "s email", 2, 1.10);


       System.out.println(t.getProductId() == 0);
       System.out.println(t.getStoreId() == 1);
       System.out.println(t.getCustomerEmail().equals("c email"));
       System.out.println(t.getSellerEmail().equals("s email"));
       System.out.println(t.getQuantitySold() == 2);
       System.out.println(t.getPrice() == 1.10);


       t.setProductId(1);
       t.setStoreId(2);
       t.setCustomerEmail("c email2");
       t.setSellerEmail("s email2");
       t.setQuantitySold(3);
       t.setPrice(2.2);


       System.out.println(t.getProductId() == 1);
       System.out.println(t.getStoreId() == 2);
       System.out.println(t.getCustomerEmail().equals("c email2"));
       System.out.println(t.getSellerEmail().equals("s email2"));
       System.out.println(t.getQuantitySold() == 3);
       System.out.println(t.getPrice() == 2.20);
      
       //doesn't catch negative prices as an error but
       //shouldn't matter because prices cannot be negative in the first place
      
   }
}
