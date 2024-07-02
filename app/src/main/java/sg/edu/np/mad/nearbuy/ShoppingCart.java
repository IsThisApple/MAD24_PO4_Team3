package sg.edu.np.mad.nearbuy;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart extends AppCompatActivity {


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

        //Navigation Panel
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_cart);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;

            } else if (itemId == R.id.bottom_map) {
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
                finish();
                return true;

            } else if (itemId == R.id.bottom_chat) {
                startActivity(new Intent(getApplicationContext(), MessageActivity.class));
                finish();
                return true;

            } else if (itemId == R.id.bottom_cart) {
                Toast.makeText(this, "It is on the page already", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });


        // Fetch products from the database
        ShoppingCartDbHandler dbHandler = new ShoppingCartDbHandler(this,null,null,1);
        ArrayList<Product> productList = dbHandler.getAllProducts();

        RecyclerView shoppingcartrecyclerview = findViewById(R.id.shoppingcartrecyclerview);
        ShoppingCartAdapter shoppingcartadapter = new ShoppingCartAdapter(productList,this);
        LinearLayoutManager layoutManager =  new LinearLayoutManager(this);
        shoppingcartrecyclerview.setLayoutManager(layoutManager);
        shoppingcartrecyclerview.setItemAnimator(new DefaultItemAnimator());
        shoppingcartrecyclerview.setAdapter(shoppingcartadapter);

    }

}