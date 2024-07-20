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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.checkerframework.common.subtyping.qual.Bottom;

import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech tts;

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

        // initialise tts
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        initializeNavigationBar();

        /*
        // navigation panel
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                Toast.makeText(this, "It is on the page already", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(getApplicationContext(), ShoppingCart.class));
                finish();
                return true;
            }
            return false;
        });
        */

        List<Product> productsList = new ArrayList<Product>();
        // meat & seafood
        productsList.add(new Product("Black Angus Beef Flank Steak", "18.95", R.drawable.product_1_1));
        productsList.add(new Product("Black Angus Free Range Minced Beef", "17.50", R.drawable.product_2_1));
        productsList.add(new Product("Whole Chicken Cut", "9.90", R.drawable.product_3_1));
        productsList.add(new Product("Boneless Chicken Breast", "5.90", R.drawable.product_4_1));
        productsList.add(new Product("Frozen Cod Fingers", "9.50", R.drawable.product_5_1));
        productsList.add(new Product("Frozen Hokkaido Scallop", "29.95", R.drawable.product_6_1));

        // fruits & vegetables
        productsList.add(new Product("Prepacked Carrots", "0.95", R.drawable.product_7_1));
        productsList.add(new Product("Thai Chinese Parsley", "1.35", R.drawable.product_8_1));
        productsList.add(new Product("Fresh Tomatoes", "1.80", R.drawable.product_9_1));
        productsList.add(new Product("Australian Avocados", "4.80", R.drawable.product_10_1));
        productsList.add(new Product("Turkey Apricots", "3.95", R.drawable.product_11_1));
        productsList.add(new Product("Fuji Apples", "3.25", R.drawable.product_12_1));

        // dairy, chilled & eggs
        productsList.add(new Product("100% Fresh Bottle Milk", "6.55", R.drawable.product_13_1));
        productsList.add(new Product("Low Fat Mango Yoghurt", "1.00", R.drawable.product_14_1));
        productsList.add(new Product("Fresh Eggs", "2.90", R.drawable.product_15_1));
        productsList.add(new Product("Quail Eggs", "2.90", R.drawable.product_16_1));
        productsList.add(new Product("Cheddar Cheese Slices", "13.60", R.drawable.product_17_1));
        productsList.add(new Product("Cheese Tofu", "3.65", R.drawable.product_18_1));

        // drinks
        productsList.add(new Product("Mineral Bottle Water", "15.65", R.drawable.product_19_1));
        productsList.add(new Product("100% Natural Coconut Water", "3.95", R.drawable.product_20_1));
        productsList.add(new Product("Orange Bottle Juice", "4.95", R.drawable.product_21_1));
        productsList.add(new Product("Oolong Tea Bottle Drink", "2.35", R.drawable.product_22_1));
        productsList.add(new Product("Nescafe Gold Original", "16.00", R.drawable.product_23_1));
        productsList.add(new Product("100 Plus Isotonic Bottle Drink", "2.50", R.drawable.product_24_1));

        // bakery
        productsList.add(new Product("Enriched White Bread", "3.20", R.drawable.product_25_1));
        productsList.add(new Product("Hokkaido Hi-Calcium Milk Bread", "3.40", R.drawable.product_26_1));
        productsList.add(new Product("Original Wraps", "5.85", R.drawable.product_27_1));
        productsList.add(new Product("Wholegrain Wraps", "5.85", R.drawable.product_28_1));
        productsList.add(new Product("Jumbo Taco Shells", "7.95", R.drawable.product_29_1));
        productsList.add(new Product("Buttermilk Pancakes", "5.30", R.drawable.product_30_1));

        // food cupboard
        productsList.add(new Product("Hazelnut Spread", "6.05", R.drawable.product_31_1));
        productsList.add(new Product("Koko Krunch Economic Pack", "8.05", R.drawable.product_32_1));
        productsList.add(new Product("100% Wholegrain Whole Rolled Oats", "5.70", R.drawable.product_33_1));
        productsList.add(new Product("Original Baked Beans", "2.15", R.drawable.product_34_1));
        productsList.add(new Product("Light Chilli Canned Tuna", "3.30", R.drawable.product_35_1));
        productsList.add(new Product("Mi Goreng Instant Noodles", "2.05", R.drawable.product_36_1));

        // snacks & confectionery
        productsList.add(new Product("Senbei Rice Crackers", "2.35", R.drawable.product_37_1));
        productsList.add(new Product("Plain Cracker", "6.05", R.drawable.product_38_1));
        productsList.add(new Product("Pringles Potato Crisps", "3.50", R.drawable.product_39_1));
        productsList.add(new Product("Hot & Spicy Crispy Potato Chips", "2.40", R.drawable.product_40_1));
        productsList.add(new Product("Almond & Chocolate Pepero Stick Biscuits", "1.50", R.drawable.product_41_1));
        productsList.add(new Product("Original Cookies Multipack", "4.65", R.drawable.product_42_1));

        // pet supplies
        productsList.add(new Product("Tuna & Snapper in Gravy Cat Can Food", "1.70", R.drawable.product_43_1));
        productsList.add(new Product("Chicken Cat Treats", "4.70", R.drawable.product_44_1));
        productsList.add(new Product("Tender Lamb with Country Vegetables", "2.25", R.drawable.product_45_1));
        productsList.add(new Product("Beef Dog Snacks", "4.20", R.drawable.product_46_1));
        productsList.add(new Product("Hamster Food", "10.00", R.drawable.product_47_1));
        productsList.add(new Product("Turtle and Terrapin Food", "9.90", R.drawable.product_48_1));

        RecyclerView productsrecyclerview = findViewById(R.id.productsrecyclerview);
        ProductAdapter mAdapter = new ProductAdapter(productsList, this, tts);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        productsrecyclerview.setLayoutManager(mLayoutManager);
        productsrecyclerview.setItemAnimator(new DefaultItemAnimator());
        productsrecyclerview.setAdapter(mAdapter);
    }

    private void initializeNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        // create listeners for each menu item
        View.OnClickListener homeSingleClickListener = v -> speak("Already on home");
        View.OnClickListener homeDoubleClickListener = v -> Toast.makeText(this, "It is on the page already", Toast.LENGTH_SHORT).show();

        View.OnClickListener mapSingleClickListener = v -> speak("Open map");
        View.OnClickListener mapDoubleClickListener = v -> {
            startActivity(new Intent(getApplicationContext(), MapActivity.class));
            finish();
        };

        View.OnClickListener chatSingleClickListener = v -> speak("Open chat");
        View.OnClickListener chatDoubleClickListener = v -> {
            startActivity(new Intent(getApplicationContext(), MessageActivity.class));
            finish();
        };

        View.OnClickListener cartSingleClickListener = v -> speak("Open cart");
        View.OnClickListener cartDoubleClickListener = v -> {
            startActivity(new Intent(getApplicationContext(), ShoppingCart.class));
            finish();
        };

        // set listeners
        bottomNavigationView.findViewById(R.id.bottom_home).setOnTouchListener(new DoubleClickListener(homeSingleClickListener, homeDoubleClickListener));
        bottomNavigationView.findViewById(R.id.bottom_map).setOnTouchListener(new DoubleClickListener(mapSingleClickListener, mapDoubleClickListener));
        bottomNavigationView.findViewById(R.id.bottom_chat).setOnTouchListener(new DoubleClickListener(chatSingleClickListener, chatDoubleClickListener));
        bottomNavigationView.findViewById(R.id.bottom_cart).setOnTouchListener(new DoubleClickListener(cartSingleClickListener, cartDoubleClickListener));
    }

    // method to speak text using tts
    private void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

}