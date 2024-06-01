package sg.edu.np.mad.nearbuy;

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


        productname.setText(product.getName());
        productprice.setText("$" + product.getPrice());
        productimage.setImageResource(product.productimg);
        productquantity.setText(String.valueOf(product.getQuantity()));
        addbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (product.getQuantity() == 0){
                    ShoppingCartDbHandler dbHandler = new ShoppingCartDbHandler(ProductPage.this,null,null,1);
                    dbHandler.addProduct(product);
                }
                else{
                    ShoppingCartDbHandler dbHandler = new ShoppingCartDbHandler(ProductPage.this,null,null,1);
                    dbHandler.addQuantity(product.getName(),1);

                }
                product.addquantity();
                productquantity.setText(String.valueOf(product.getQuantity()));
            }
        });

        subtractbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (product.getQuantity() == 0){
                    Toast.makeText(ProductPage.this, "Cannot have less than 0 items.", Toast.LENGTH_SHORT).show();
                }
                else {
                    ShoppingCartDbHandler dbHandler = new ShoppingCartDbHandler(ProductPage.this,null,null,1);
                    dbHandler.subtractQuantity(product.name,1);
                    product.subtractquantity();
                    productquantity.setText(String.valueOf(product.getQuantity()));
                }
            }
        });
    }
}