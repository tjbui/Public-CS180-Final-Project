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
