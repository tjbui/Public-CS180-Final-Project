import java.util.ArrayList;

public class Seller extends User {

    private ArrayList<Integer> storeIDs;

    public Seller(String email, String password, ArrayList<Integer> storeIDs) {
        super(email, password);
        this.storeIDs = storeIDs;
    }

    public ArrayList<Integer> getStoreIDs() {
        return storeIDs;
    }

    public void setStoreIDs(ArrayList<Integer> storeIDs) {
        this.storeIDs = storeIDs;
    }
}
