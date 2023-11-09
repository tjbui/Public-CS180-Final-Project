public class Transaction {
    private String[] transaction = new String[6];
    private int productId;
    private int storeId;
    private String customerEmail;
    private String sellerEmail;
    private int quantitySold;
    private double price;

    public Transaction(int productId, int storeId, String customerEmail, String sellerEmail, int quantitySold, double price) {
        transaction[0] = String.valueOf(productId);
        transaction[1] = String.valueOf(storeId);
        transaction[2] = customerEmail;
        transaction[3] = sellerEmail;
        transaction[4] = String.valueOf(quantitySold);
        transaction[5] = String.valueOf(price);
        this.productId = productId;
        this.storeId = storeId;
        this.customerEmail = customerEmail;
        this.sellerEmail = sellerEmail;
        this.quantitySold = quantitySold;
        this.price = price;
    }
    public Transaction(String productId, String storeId, String customerEmail, String sellerEmail, String quantitySold, String price) {
        transaction[0] = productId;
        transaction[1] = storeId;
        transaction[2] = customerEmail;
        transaction[3] = sellerEmail;
        transaction[4] = quantitySold;
        transaction[5] = price;

        this.productId = Integer.parseInt(productId);
        this.storeId = Integer.parseInt(storeId);
        this.customerEmail = customerEmail;
        this.sellerEmail = sellerEmail;
        this.quantitySold = Integer.parseInt(quantitySold);
        this.price = Double.parseDouble(price);
    }

    //getters
    public String[] getTransaction() {
        return transaction;
    }
    public int getProductId() {
        return productId;
    }
    public int getStoreId() {
        return storeId;
    }
    public String getSellerEmail() {
        return sellerEmail;
    }
    public String getCustomerEmail() {
        return customerEmail;
    }
    public int getQuantitySold() {
        return quantitySold;
    }
    public double getPrice() {
        return price;
    }

    //setters
    public void setTransaction(String[] transaction) {
        this.transaction = transaction;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }
}
