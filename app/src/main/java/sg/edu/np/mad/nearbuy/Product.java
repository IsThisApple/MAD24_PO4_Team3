package sg.edu.np.mad.nearbuy;

public class Product {
    public String name;
    public String price;
    public int itemcount;
    public int productimg;
    public boolean bought;
    public Product(String name, String price, int itemcount, int productimg, boolean bought) {
        this.name = name;
        this.price = price;
        this.itemcount = itemcount;
        this.productimg = productimg;
        this.bought = bought;
    }
    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getItemcount() {
        return itemcount;
    }
    public int getProductimg() { return productimg; }
    public Boolean getBought() {
        return bought;
    }
}
