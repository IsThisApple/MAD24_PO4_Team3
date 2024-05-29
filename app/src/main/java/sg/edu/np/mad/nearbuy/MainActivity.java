package sg.edu.np.mad.nearbuy;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        List<Product> productsList = new ArrayList<Product>();
        for (int i = 0; i < 20; i++) {
            Random rand = new Random();
            int val = rand.nextInt(1000000);
            String rannum = Integer.toString(val);
            productsList.add(new Product("item" + rannum, rannum, 0, R.drawable.ic_launcher_background,false));
        }

        RecyclerView productsrecyclerview = findViewById(R.id.productsrecyclerview);
        ProductAdapter mAdapter = new ProductAdapter(productsList, this);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        productsrecyclerview.setLayoutManager(mLayoutManager);
        productsrecyclerview.setItemAnimator(new DefaultItemAnimator());
        productsrecyclerview.setAdapter(mAdapter);
    }
}