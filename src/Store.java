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

    public String toStringFormat() {
        String productIdString = "";

        for (int i = 0; i < this.products.size(); i++) {
            productIdString = productIdString + this.products.get(i).toString() + "&";
        }

        if (productIdString.length() > 0) {
            productIdString = productIdString.substring(0, productIdString.length() - 1);
        } else {
            productIdString = "&";
        }

        return String.format("%s,%s,%d,%s", this.name, this.sellerEmail, this.id, productIdString);
    }

    public static Store fromStringFormat(String raw) {
        String[] parts = raw.split(",");

        ArrayList<Integer> products = new ArrayList<Integer>();

        String[] productIdList = parts[3].split("&");

        for (int i = 0; i < productIdList.length; i++) {
            products.add(Integer.valueOf(productIdList[i]));
        }

        return new Store(products, parts[0], parts[1], Integer.parseInt(parts[2]));
    }
}
