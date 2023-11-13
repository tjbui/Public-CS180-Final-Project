import java.util.ArrayList;

public class Customer extends User {

    private ArrayList<Integer> ids;
    private ArrayList<Integer> quantities;

    public Customer(String email, String password, ArrayList<Integer> ids, ArrayList<Integer> quantities) {
        super(email, password);
        this.ids = ids;
        this.quantities = quantities;
    }

    public void addProduct(int id, int quantity) {
        ids.add(id);
        quantities.add(quantity);
    }

    public void removeProduct(int id) {
        int index = ids.indexOf(id);
        ids.remove(index);
        quantities.remove(index);
    }


    public ArrayList<Integer> getIds() {
        return ids;
    }

    public ArrayList<Integer> getQuantities() {
        return quantities;
    }

    public void setIds(ArrayList<Integer> ids) {
        this.ids = ids;
    }

    public void setQuantities(ArrayList<Integer> quantities) {
        this.quantities = quantities;
    }

    @Override
    public String toStringFormat() {
        String productIdString = "";
        String quantityIdString = "";

        for (int i = 0; i < this.ids.size(); i++) {
            productIdString = productIdString + this.ids.get(i).toString() + "&";
            quantityIdString = quantityIdString + this.quantities.get(i).toString() + "&";
        }

        if (productIdString.length() > 0) {
            productIdString = productIdString.substring(0, productIdString.length() - 1);
            quantityIdString = quantityIdString.substring(0, quantityIdString.length() - 1);
        } else {
            productIdString = "&";
            quantityIdString = "&";
        }

        return String.format("c,%s,%s,%s,%s", this.getEmail(), this.getPassword(), productIdString,
                quantityIdString);
    }

    public static Customer fromStringFormat(String raw) {
        String[] parts = raw.split(",");

        ArrayList<Integer> productIds = new ArrayList<Integer>();
        ArrayList<Integer> productQuantities = new ArrayList<Integer>();

        String[] productIdParts = parts[3].split("&");
        String[] quantityIdParts = parts[4].split("&");

        for (int i = 0; i < productIdParts.length; i++) {
            productIds.add(Integer.valueOf(productIdParts[i]));
            productQuantities.add(Integer.valueOf(quantityIdParts[i]));
        }

        return new Customer(parts[1], parts[2], productIds, productQuantities);
    }

}
