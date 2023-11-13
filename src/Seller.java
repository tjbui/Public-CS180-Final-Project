import java.util.ArrayList;

/**
 * Project-04 – Seller
 *
 *
 * @author Gunho Park, Seth Hartzler, Isaac Shane, Tru Bui; Lab - L16
 *

 * @version Nov. 13, 2023
 *
 */
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

    @Override
    public String toStringFormat() {
        String storeIdString = "";

        for (int i = 0; i < this.storeIDs.size(); i++) {
            storeIdString = storeIdString + Integer.toString(this.storeIDs.get(i)) + "&";
        }

        if (storeIdString.length() > 0) {
            storeIdString = storeIdString.substring(0, storeIdString.length() - 1);
        } else {
            storeIdString = "&";
        }

        return String.format("s,%s,%s,%s", this.getEmail(), this.getPassword(), storeIdString);
    }

    public static Seller fromStringFormat(String raw) {
        String[] parts = raw.split(",");

        ArrayList<Integer> storeIdList = new ArrayList<Integer>();

        String[] storeIdStrings = parts[3].split("&");

        for (int i = 0; i < storeIdStrings.length; i++) {
            storeIdList.add(Integer.valueOf(storeIdStrings[i]));
        }

        return new Seller(parts[1], parts[2], storeIdList);
    }
}
