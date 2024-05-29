package sg.edu.np.mad.nearbuy;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.Serializable;


import androidx.appcompat.app.AppCompatActivity;


import java.util.List;


public class ProductInfoActivity extends AppCompatActivity {


    private ImageView productImageView;
    private TextView productNameTextView;
    private TextView productPriceTextView;
    private TextView productDescriptionTextView;


    private List<Integer> productImages;
    private int currentImageIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);


        productImageView = findViewById(R.id.product_image);
        productNameTextView = findViewById(R.id.product_name);
        productPriceTextView = findViewById(R.id.product_price);
        productDescriptionTextView = findViewById(R.id.product_description);


        Product product = (Product) getIntent().getSerializableExtra("product");
        if (product != null) {
            productImages = product.getProductImages();
            productNameTextView.setText(product.getName());
            productPriceTextView.setText(product.getPrice());
            productDescriptionTextView.setText("This is a description of " + product.getName());


            if (!productImages.isEmpty()) {
                productImageView.setImageResource(productImages.get(0));
            }


            productImageView.setOnClickListener(v -> openFullScreenImage());
        }


        findViewById(R.id.arrow_left).setOnClickListener(v -> showPreviousImage());
        findViewById(R.id.arrow_right).setOnClickListener(v -> showNextImage());
        findViewById(R.id.share_button).setOnClickListener(v -> shareProduct());
    }


    private void showPreviousImage() {
        if (productImages != null && currentImageIndex > 0) {
            currentImageIndex--;
            productImageView.setImageResource(productImages.get(currentImageIndex));
        }
    }


    private void showNextImage() {
        if (productImages != null && currentImageIndex < productImages.size() - 1) {
            currentImageIndex++;
            productImageView.setImageResource(productImages.get(currentImageIndex));
        }
    }


    private void shareProduct() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this product on Nearbuy! *link to selected product*");
        startActivity(Intent.createChooser(shareIntent, "Share product via"));
    }


    private void openFullScreenImage() {
        Intent intent = new Intent(this, FullScreenImageActivity.class);
        intent.putExtra("images", (Serializable) productImages);
        intent.putExtra("index", currentImageIndex);
        startActivity(intent);
    }
}