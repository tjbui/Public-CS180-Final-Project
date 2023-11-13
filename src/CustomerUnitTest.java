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

public class CustomerUnitTest {
   public static void main(String[] args) {
       ArrayList<Integer> ids = new ArrayList<>();
       ArrayList<Integer> quantities = new ArrayList<>();
       Customer c = new Customer("email", "password", ids, quantities);


       System.out.println(c.getEmail().equals("email"));
       System.out.println(c.getPassword().equals("password"));
       System.out.println(c.getIds().isEmpty());
       System.out.println(c.getQuantities().isEmpty());


       ArrayList<Integer> ids2 = new ArrayList<>();
       ids2.add(1);
       ArrayList<Integer> quantities2 = new ArrayList<>();
       quantities2.add(2);


       c.setIds(ids2);
       c.setQuantities(quantities2);


       System.out.println(c.getIds().get(0) == 1);
       System.out.println(c.getQuantities().get(0) == 2);


   }
}
