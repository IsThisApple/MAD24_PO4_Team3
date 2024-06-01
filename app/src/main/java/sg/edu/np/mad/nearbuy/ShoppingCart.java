package sg.edu.np.mad.nearbuy;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private ShoppingCartDbHandler databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.shoppingcartrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new ShoppingCartDbHandler(this,null,null,1);
        productList = new ArrayList<>();


        // Fetch products from the database
        loadProductsFromDatabase();

        productAdapter = new ProductAdapter(productList,this);
        recyclerView.setAdapter(productAdapter);
    }
    private void loadProductsFromDatabase() {
        Cursor cursor = databaseHelper.getAllProducts();
        if (cursor != null && cursor.moveToFirst()) {
            int imageResIdIndex = cursor.getColumnIndex("image_res_id");
            int nameIndex = cursor.getColumnIndex("name");
            int priceIndex = cursor.getColumnIndex("price");

            // Check if column indices are valid
            if (imageResIdIndex == -1 || nameIndex == -1 || priceIndex == -1) {
                Log.e("ShoppingCart", "Invalid column indices. Check your database schema.");
                Toast.makeText(this, "Database error: invalid column indices.", Toast.LENGTH_SHORT).show();
                return;
            }

            do {
                int imageResId = cursor.getInt(imageResIdIndex);
                String name = cursor.getString(nameIndex);
                double price = cursor.getDouble(priceIndex);
                productList.add(new Product(name, Double.toString(price),imageResId));
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
    }
}