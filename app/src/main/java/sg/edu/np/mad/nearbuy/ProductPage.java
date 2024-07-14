package sg.edu.np.mad.nearbuy;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProductPage extends AppCompatActivity {

    private int currentImageIndex = 0;
    private int[] productImages = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        int image = getIntent().getIntExtra("img", 0);

        Product product = new Product(name, price, image);

        TextView productname = findViewById(R.id.tvproductname);
        TextView productprice = findViewById(R.id.tvproductprice);
        ImageView productimage = findViewById(R.id.tvproductimage);
        TextView productquantity = findViewById(R.id.quantity);
        Button addbutton = findViewById(R.id.add);
        Button subtractbutton = findViewById(R.id.subtract);
        ImageView arrowLeft = findViewById(R.id.arrow_left);
        ImageView arrowRight = findViewById(R.id.arrow_right);
        ImageView addtocart = findViewById(R.id.addtocart);
        ImageView backbutton = findViewById(R.id.backbtn);

        productname.setText(product.getName());
        productprice.setText("$" + product.getPrice());
        productquantity.setText(String.valueOf(product.getQuantity()));

        // set up product images
        setUpProductImages(product.getName());
        productimage.setImageResource(productImages[currentImageIndex]);

        // set up image click listener to show enlarged image
        productimage.setOnClickListener(v -> showEnlargedImage(productImages[currentImageIndex]));

        arrowLeft.setOnClickListener(v -> {
            if (currentImageIndex > 0) {
                currentImageIndex--;
                productimage.setImageResource(productImages[currentImageIndex]);
                productimage.setOnClickListener(view -> showEnlargedImage(productImages[currentImageIndex]));
            }
        });

        arrowRight.setOnClickListener(v -> {
            if (currentImageIndex < productImages.length - 1) {
                currentImageIndex++;
                productimage.setImageResource(productImages[currentImageIndex]);
                productimage.setOnClickListener(view -> showEnlargedImage(productImages[currentImageIndex]));
            }
        });

        //set up back button click listener to return to MainActivity
        backbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCartDbHandler dbHandler = new ShoppingCartDbHandler(ProductPage.this, null, null, 1);
                if (dbHandler.isProductExist(product.getName()) == true) {
                    Toast.makeText(ProductPage.this,"product exist", Toast.LENGTH_SHORT).show();
                    dbHandler.addQuantity(product, product.getName());
                }else{
                    dbHandler.addProduct(product);
                }
                Toast.makeText(ProductPage.this,"Product Added",Toast.LENGTH_SHORT).show();
            }
        });

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.addquantity();
                productquantity.setText(String.valueOf(product.getQuantity()));
            }
        });

        subtractbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.getQuantity() == 0) {
                    Toast.makeText(ProductPage.this, "Cannot have less than 0 items.", Toast.LENGTH_SHORT).show();
                } else {
                    product.subtractquantity();
                    productquantity.setText(String.valueOf(product.getQuantity()));
                }
            }
        });
    }

    private void showEnlargedImage(int imageResId) {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_enlarged_image);

        ImageView enlargedImage = dialog.findViewById(R.id.enlarged_image);
        enlargedImage.setImageResource(imageResId);

        // close dialog on image click
        enlargedImage.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void setUpProductImages(String productName) {
        // setting up image resources based on the product name
        switch (productName) {
            case "Black Angus Beef Flank Steak":
                productImages = new int[]{R.drawable.product_1_1, R.drawable.product_1_2};
                break;
            case "Black Angus Free Range Minced Beef":
                productImages = new int[]{R.drawable.product_2_1, R.drawable.product_2_2};
                break;
            case "Whole Chicken Cut":
                productImages = new int[]{R.drawable.product_3_1, R.drawable.product_3_2};
                break;
            case "Boneless Chicken Breast":
                productImages = new int[]{R.drawable.product_4_1, R.drawable.product_4_2};
                break;
            case "Frozen Cod Fingers":
                productImages = new int[]{R.drawable.product_5_1, R.drawable.product_5_2, R.drawable.product_5_3};
                break;
            case "Frozen Hokkaido Scallop":
                productImages = new int[]{R.drawable.product_6_1, R.drawable.product_6_2, R.drawable.product_6_3};
                break;
            case "Prepacked Carrots":
                productImages = new int[]{R.drawable.product_7_1};
                break;
            case "Thai Chinese Parsley":
                productImages = new int[]{R.drawable.product_8_1, R.drawable.product_8_2};
                break;
            case "Fresh Tomatoes":
                productImages = new int[]{R.drawable.product_9_1, R.drawable.product_9_2};
                break;
            case "Australian Avocados":
                productImages = new int[]{R.drawable.product_10_1, R.drawable.product_10_2};
                break;
            case "Turkey Apricots":
                productImages = new int[]{R.drawable.product_11_1, R.drawable.product_11_2, R.drawable.product_11_3};
                break;
            case "Fuji Apples":
                productImages = new int[]{R.drawable.product_12_1};
                break;
            case "100% Fresh Bottle Milk":
                productImages = new int[]{R.drawable.product_13_1};
                break;
            case "Low Fat Mango Yoghurt":
                productImages = new int[]{R.drawable.product_14_1, R.drawable.product_14_2, R.drawable.product_14_3};
                break;
            case "Fresh Eggs":
                productImages = new int[]{R.drawable.product_15_1, R.drawable.product_15_2, R.drawable.product_15_3};
                break;
            case "Quail Eggs":
                productImages = new int[]{R.drawable.product_16_1, R.drawable.product_16_2, R.drawable.product_16_3};
                break;
            case "Cheddar Cheese Slices":
                productImages = new int[]{R.drawable.product_17_1, R.drawable.product_17_2, R.drawable.product_17_3};
                break;
            case "Cheese Tofu":
                productImages = new int[]{R.drawable.product_18_1};
                break;
            case "Mineral Bottle Water":
                productImages = new int[]{R.drawable.product_19_1, R.drawable.product_19_2};
                break;
            case "100% Natural Coconut Water":
                productImages = new int[]{R.drawable.product_20_1, R.drawable.product_20_2, R.drawable.product_20_3};
                break;
            case "Orange Bottle Juice":
                productImages = new int[]{R.drawable.product_21_1, R.drawable.product_21_2};
                break;
            case "Oolong Tea Bottle Drink":
                productImages = new int[]{R.drawable.product_22_1, R.drawable.product_22_2, R.drawable.product_22_3};
                break;
            case "Nescafe Gold Original":
                productImages = new int[]{R.drawable.product_23_1, R.drawable.product_23_2};
                break;
            case "100 Plus Isotonic Bottle Drink":
                productImages = new int[]{R.drawable.product_24_1, R.drawable.product_24_2, R.drawable.product_24_3};
                break;
            case "Enriched White Bread":
                productImages = new int[]{R.drawable.product_25_1};
                break;
            case "Hokkaido Hi-Calcium Milk Bread":
                productImages = new int[]{R.drawable.product_26_1, R.drawable.product_26_2, R.drawable.product_26_3};
                break;
            case "Original Wraps":
                productImages = new int[]{R.drawable.product_27_1, R.drawable.product_27_2};
                break;
            case "Wholegrain Wraps":
                productImages = new int[]{R.drawable.product_28_1, R.drawable.product_28_2};
                break;
            case "Jumbo Taco Shells":
                productImages = new int[]{R.drawable.product_29_1, R.drawable.product_29_2};
                break;
            case "Buttermilk Pancakes":
                productImages = new int[]{R.drawable.product_30_1, R.drawable.product_30_2, R.drawable.product_30_3};
                break;
            case "Hazelnut Spread":
                productImages = new int[]{R.drawable.product_31_1, R.drawable.product_31_2};
                break;
            case "Koko Krunch Economic Pack":
                productImages = new int[]{R.drawable.product_32_1, R.drawable.product_32_2, R.drawable.product_32_3};
                break;
            case "100% Wholegrain Whole Rolled Oats":
                productImages = new int[]{R.drawable.product_33_1, R.drawable.product_33_2, R.drawable.product_33_3};
                break;
            case "Original Baked Beans":
                productImages = new int[]{R.drawable.product_34_1, R.drawable.product_34_2};
                break;
            case "Light Chilli Canned Tuna":
                productImages = new int[]{R.drawable.product_35_1, R.drawable.product_35_2, R.drawable.product_35_3};
                break;
            case "Mi Goreng Instant Noodles":
                productImages = new int[]{R.drawable.product_36_1, R.drawable.product_36_2};
                break;
            case "Senbei Rice Crackers":
                productImages = new int[]{R.drawable.product_37_1, R.drawable.product_37_2};
                break;
            case "Plain Cracker":
                productImages = new int[]{R.drawable.product_38_1, R.drawable.product_38_2};
                break;
            case "Pringles Potato Chips":
                productImages = new int[]{R.drawable.product_39_1};
                break;
            case "Hot & Spicy Crispy Potato Chips":
                productImages = new int[]{R.drawable.product_40_1, R.drawable.product_40_2};
                break;
            case "Almond & Chocolate Pepero Stick Biscuits":
                productImages = new int[]{R.drawable.product_41_1, R.drawable.product_41_2};
                break;
            case "Original Cookies Multipack":
                productImages = new int[]{R.drawable.product_42_1, R.drawable.product_42_2};
                break;
            case "Tuna & Snapper in Gravy Cat Can Food":
                productImages = new int[]{R.drawable.product_43_1, R.drawable.product_43_2, R.drawable.product_43_3};
                break;
            case "Chicken Cat Treats":
                productImages = new int[]{R.drawable.product_44_1, R.drawable.product_44_2};
                break;
            case "Tender Lamb with Country Vegetables":
                productImages = new int[]{R.drawable.product_45_1};
                break;
            case "Beef Dog Snacks":
                productImages = new int[]{R.drawable.product_46_1, R.drawable.product_46_2};
                break;
            case "Hamster Food":
                productImages = new int[]{R.drawable.product_47_1};
                break;
            case "Turtle and Terrapin Food":
                productImages = new int[]{R.drawable.product_48_1};
                break;
            default:
                productImages = new int[]{R.drawable.close_icon}; // default image if no specific images are found
                break;
        }
    }
}