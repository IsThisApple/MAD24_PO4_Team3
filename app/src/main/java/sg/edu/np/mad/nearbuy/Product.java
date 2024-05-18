package sg.edu.np.mad.nearbuy;

public class Product {
    public String name;
    public String price;
    public int itemcount;
    public boolean bought;
    public Product(String name, String price, int itemcount, boolean bought) {
        this.name = name;
        this.price = price;
        this.itemcount = itemcount;
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

    public Boolean getBought() {
        return bought;
    }
}
