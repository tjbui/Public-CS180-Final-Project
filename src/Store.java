import java.util.ArrayList;
/**
 * Store class
 *
 * @author Tru Bui
 * @version November 8, 2023
 */
public class Store {
    ArrayList<Integer> products; // will be arraylist of products
    String name;
    String sellerEmail;
    int id;

    public Store(ArrayList<Integer> products, String name, String sellerEmail, int id) { // constructor
        this.products = products;
        this.name = name;
        this.sellerEmail = sellerEmail;
        this.id = id;
    }
    public ArrayList<Integer> getProducts() { // getters
        return products;
    }
    public String getName() {
        return name;
    }
    public String getSellerEmail() {
        return sellerEmail;
    }
    public int getId() {
        return id;
    }

    public void setProducts(ArrayList<Integer> products) { // setters
        this.products = products;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }
    public void setId(int id) {
        this.id = id;
    }
}
