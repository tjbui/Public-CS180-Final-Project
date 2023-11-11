public class Transaction {
   private int productId;
   private int storeId;
   private String customerEmail;
   private String sellerEmail;
   private int quantitySold;
   private double price;


   public Transaction(int productId, int storeId, String customerEmail, String sellerEmail, int quantitySold, double price) {
       this.productId = productId;
       this.storeId = storeId;
       this.customerEmail = customerEmail;
       this.sellerEmail = sellerEmail;
       this.quantitySold = quantitySold;
       this.price = price;
   }


   //getters
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
