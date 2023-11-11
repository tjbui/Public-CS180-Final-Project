
public class ProductUnitTest {
    public static void main(String[] args) throws InvalidPriceError {
        Product p = new Product("Name", "Description", 0, 1, 1.0);

        confirmStringResult(p.getName(), "Name");
        confirmStringResult(p.getDescription(), "Description");
        confirmIntResult(p.getStoreId(), 0);
        confirmIntResult(p.getQuantity(), 1);
        confirmDoubleResult(p.getPrice(), 1.0);

        p.setName("newName");
        p.setDescription("newDescription");
        p.setQuantity(2);
        p.setPrice(2.0);

        confirmStringResult(p.getName(), "newName");
        confirmStringResult(p.getDescription(), "newDescription");
        confirmIntResult(p.getQuantity(), 2);
        confirmDoubleResult(p.getPrice(), 2.0);

        try {
            p.setPrice(-1.0);
        } catch (InvalidPriceError e) {
            System.out.println(true);
        }

        confirmBooleanResult(p.checkQuantity(-1), false);
        confirmBooleanResult(p.checkQuantity(10), false);
        confirmBooleanResult(p.checkQuantity(1), true);

        p.purchase(1);

        confirmIntResult(p.getQuantity(), 1);

        String stringRep = p.toStringFormat();

        confirmStringResult(stringRep, "newName,newDescription,0,1,2.00,0");

        Product p2 = Product.fromStringFormat(stringRep);

        confirmStringResult(p.getName(), p2.getName());
        confirmStringResult(p.getDescription(), p2.getDescription());
        confirmIntResult(p.getStoreId(), p2.getStoreId());
        confirmIntResult(p.getQuantity(), p2.getQuantity());
        confirmDoubleResult(p.getPrice(), p2.getPrice());
        confirmIntResult(p.getId(), p2.getId());
    }

    public static void confirmStringResult(String s1, String s2) {
        System.out.println(s1.equals(s2));
    }

    public static void confirmIntResult(int i1, int i2) {
        System.out.println(i1 == i2);
    }

    public static void confirmDoubleResult(double d1, double d2) {
        System.out.println(d1 == d2);
    }

    public static void confirmBooleanResult(boolean b1, boolean b2) {
        System.out.println(b1 == b2);
    }
}
