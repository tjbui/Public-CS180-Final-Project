/**
 * Project-04 – UserUnitTest
 *
 *
 * @author Gunho Park, Seth Hartzler, Isaac Shane, Tru Bui; Lab - L16
 *

 * @version Nov. 13, 2023
 *
 */
public class UserUnitTest {
   public static void main(String[] args) {
       User u = new User("email", "password");


       System.out.println(u.getEmail().equals("email"));
       System.out.println(u.getPassword().equals("password"));


       u.setEmail("email2");
       u.setPassword("password2");


       System.out.println(u.getEmail().equals("email2"));
       System.out.println(u.getPassword().equals("password2"));
   }
}
