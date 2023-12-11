/**
 * Project-04 – User
 *
 * @author Gunho Park, Seth Hartzler, Isaac Shane, Tru Bui; Lab - L16
 * @version Nov. 13, 2023
 */
public class User {
    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return this.email;
    }

    public String toStringFormat() {
        return String.format("%s,%s", this.email, this.password);
    }

    public static User fromStringFormat(String raw) {
        if (raw.charAt(0) == 's') {
            return Seller.fromStringFormat(raw);
        } else if (raw.charAt(0) == 'c') {
            return Customer.fromStringFormat(raw);
        } else {
            String[] parts = raw.split(",");
            if (parts.length == 2) { // NECESSARY CAUSE IF NOT FOUND, raw will be "User not found," which will make parts length 1
                return new User(parts[0], parts[1]);
            }
            return new User(parts[0], "password");
        }
    }
}
