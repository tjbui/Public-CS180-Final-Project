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

    public String toStringFormat() {
        return String.format("%s,%s", this.email, this.password);
    }

    public static User fromStringFormat(String raw) {
        String[] parts = raw.split(",");

        return new User(parts[0], parts[1]);
    }
}
