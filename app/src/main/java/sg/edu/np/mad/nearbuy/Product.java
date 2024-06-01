package sg.edu.np.mad.nearbuy;

public class Product {
    public String name;
    public String price;
    public int productimg;
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

    public int getProductimg() { return productimg; }
}
