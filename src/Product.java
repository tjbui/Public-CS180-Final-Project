
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
        this.id = count;
        this.store = store;
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

    public int getStore() {
        return this.store;
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
}
