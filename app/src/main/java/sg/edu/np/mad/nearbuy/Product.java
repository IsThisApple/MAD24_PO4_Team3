package sg.edu.np.mad.nearbuy;

public class Product {
    public String name;
    public String price;
    public int quantity;
    public int productimg;
    public double totalprice;

    public Product(String name, String price, int productimg) {
        this.name = name;
        this.price = price;
        this.productimg = productimg;

    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getQuantity(){ return quantity; }

    public int getProductimg() { return productimg; }

    public double getTotalprice() {
        return totalprice;
    }

    public void addquantity(){
        quantity = quantity + 1;
        totalprice = quantity * Double.parseDouble(price);
    }

    public void subtractquantity(){
        quantity = quantity - 1;
        totalprice = quantity * Double.parseDouble(price);
    }

}