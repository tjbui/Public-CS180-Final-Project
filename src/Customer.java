import java.util.ArrayList;

public class Customer extends User {

    private ArrayList<Integer> ids;
    private ArrayList<Integer> quantities;

    public Customer(String email, String password, ArrayList<Integer> ids, ArrayList<Integer> quantities) {
        super(email, password);
        this.ids = ids;
        this.quantities = quantities;
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
}
