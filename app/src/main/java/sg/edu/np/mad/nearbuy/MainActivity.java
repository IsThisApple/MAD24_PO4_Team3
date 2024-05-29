package sg.edu.np.mad.nearbuy;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView recyclerView = findViewById(R.id.productsrecyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        List<Product> productList = new ArrayList<>();
        for (int i = 1; i <= 48; i++) {
            List<Integer> images = Arrays.asList(
                    getResources().getIdentifier("product_" + i + "_1", "drawable", getPackageName()),
                    getResources().getIdentifier("product_" + i + "_2", "drawable", getPackageName()),
                    getResources().getIdentifier("product_" + i + "_3", "drawable", getPackageName())
            );
            productList.add(new Product("Item " + i, "$" + (i * 10), i, images, false));
        }


        ProductAdapter adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);
    }
}