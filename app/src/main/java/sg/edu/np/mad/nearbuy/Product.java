package sg.edu.np.mad.nearbuy;


import java.io.Serializable;
import java.util.List;


public class Product implements Serializable {
    private String name;
    private String price;
    private int id;
    private List<Integer> images;
    private boolean isFavorite;


    public Product(String name, String price, int id, List<Integer> images, boolean isFavorite) {
        this.name = name;
        this.price = price;
        this.id = id;
        this.images = images;
        this.isFavorite = isFavorite;
    }


    public String getName() {
        return name;
    }


    public String getPrice() {
        return price;
    }


    public int getId() {
        return id;
    }


    public List<Integer> getProductImages() {
        return images;
    }


    public boolean isFavorite() {
        return isFavorite;
    }
}