
public class Product {

    private String name;
    //private Store store;
    private String description;
    private int quantity;
    private double price;
    private static int count = 0;
    private int id;

    public Product(String name, String description, int quantity, double price, int id) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.id = count;
        count++;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public static int getCount() {
        return count;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static void setCount(int count) {
        Product.count = count;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean checkQuantity(int quantity) {
        if (quantity <= this.quantity) {
            return true;
        } else {
            return false;
        }
    }

    public void purchase(int quantity) {
        this.quantity -= quantity;
    }


}
