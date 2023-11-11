import java.util.ArrayList;

public class DataManagerUnitTest {
    public static void main(String[] args) {
        DataManager dm = new DataManager();

        checkObject(dm.getCurrentUser(), null);

        User s = new Seller("seller", "seller", new ArrayList<Integer>());
        User c = new Customer("customer", "customer", new ArrayList<Integer>(), 
            new ArrayList<Integer>());
        
        dm.addUser(s);

        checkObject(dm.getCurrentUser(), s);

        dm.logoutCurrentUser();

        checkObject(dm.getCurrentUser(), null);

        dm.addUser(s);

        checkObject(dm.getCurrentUser(), null);

        dm.addUser(c);
        dm.editCurrentUser("customer", "pass");

        checkString(dm.getCurrentUser().getPassword(), "pass");

        dm.logoutCurrentUser();

        Store store = new Store(new ArrayList<Integer>(), "store", "seller", 0);

        dm.setCurrentUser(s.getEmail());

        dm.addStore(store);

        checkBoolean(dm.currentUserOwnsStore(store), true);

        dm.logoutCurrentUser();

        dm.setCurrentUser(c.getEmail());

        checkBoolean(dm.currentUserOwnsStore(store), false);

        dm.logoutCurrentUser();

        dm.setCurrentUser(s.getEmail());

        try {
            Product p1 = new Product("product", "product", 0, 1, 
                2.0, 0);
            Product p2 = new Product("product", "product", 0, 2, 
                1.0, 1);

            dm.addProduct(p1);
            dm.addProduct(p2);
        } catch (Exception e) {}

        ArrayList<Product> l1 = dm.getProductList(DataManager.BY_PRICE, DataManager.SORTED_ASC);
        ArrayList<Product> l2 = dm.getProductList(DataManager.BY_PRICE, DataManager.SORTED_DESC);
        ArrayList<Product> l3 = dm.getProductList(DataManager.BY_QUANTITY, DataManager.SORTED_ASC);
        ArrayList<Product> l4 = dm.getProductList(DataManager.BY_QUANTITY, DataManager.SORTED_DESC);

        checkInt(l1.get(0).getId(), 1);
        checkInt(l1.get(1).getId(), 0);

        checkInt(l2.get(0).getId(), 0);
        checkInt(l2.get(1).getId(), 1);

        checkInt(l3.get(0).getId(), 0);
        checkInt(l3.get(1).getId(), 1);

        checkInt(l4.get(0).getId(), 1);
        checkInt(l4.get(1).getId(), 0);
    }

    public static void checkString(String s1, String s2) {
        System.out.println(s1.equals(s2));
    }

    public static void checkInt(int i1, int i2) {
        System.out.println(i1 == i2);
    }

    public static void checkDouble(double d1, double d2) {
        System.out.println(d1 == d2);
    }

    public static void checkBoolean(boolean b1, boolean b2) {
        System.out.println(b1 == b2);
    }

    public static void checkObject(Object o1, Object o2) {
        System.out.println(o1 == o2);
    }
}
