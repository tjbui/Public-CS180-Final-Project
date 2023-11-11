
public class Product {

    private String name;
    private int storeId;
    private String description;
    private int quantity;
    private double price;
    private static int count = 0;
    private int id;

    public Product(String name, String description, int storeId, int quantity, double price, int id) {
        this.name = name;
        this.description = description;
        this.storeId = storeId;
        this.quantity = quantity;
        this.price = price;
        this.id = id;
    }

    public Product(String name, String description, int storeId, int quantity, double price) {
        this(name, description, storeId, quantity, price, count++);
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

    public int getStoreId() {
        return this.storeId;
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
        if (quantity <= this.quantity || quantity < 0) {
            return true;
        } else {
            return false;
        }
    }

    public void purchase(int quantity) {
        this.quantity -= quantity;
    }

    public String toStringFormat() {
        return String.format("%s,%s,%d,%d,%f,%d", this.name, this.description, this.storeId, 
        this.quantity, this.price, this.id);
    }

    public static Product fromStringFormat(String raw) {
        String[] parts = raw.split(",");

        return new Product(parts[0], parts[1], Integer.valueOf(parts[2]), Integer.valueOf(parts[3]), 
        Float.valueOf(parts[4]), Integer.valueOf(parts[5]));
    }
}
