package sg.edu.np.mad.nearbuy;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProductPage extends AppCompatActivity {

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

        productname.setText(product.getName());
        productprice.setText("$" + product.getPrice());
        productimage.setImageResource(product.productimg);
    }
}