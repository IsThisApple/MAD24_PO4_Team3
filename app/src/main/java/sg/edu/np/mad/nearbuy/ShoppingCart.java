package sg.edu.np.mad.nearbuy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class ShoppingCart extends AppCompatActivity {

    private TextView totalSumPrice;
    private ShoppingCartAdapter shoppingcartadapter;
    private ShoppingCartDbHandler dbHandler;
    private ArrayList<Product> productList;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        // initialise views
        totalSumPrice = findViewById(R.id.totalSumPrice);
        RecyclerView shoppingcartrecyclerview = findViewById(R.id.shoppingcartrecyclerview);
        Button toCheckoutButton = findViewById(R.id.toCheckout);

        // enable edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            v.setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
            return insets.consumeSystemWindowInsets();
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
                Toast.makeText(this, "You are already on the cart page", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        */

        // fetch products from the database
        dbHandler = new ShoppingCartDbHandler(this, null, null, 1);
        productList = dbHandler.getAllProducts();

        // set up RecyclerView
        shoppingcartadapter = new ShoppingCartAdapter(productList, this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        shoppingcartrecyclerview.setLayoutManager(layoutManager);
        shoppingcartrecyclerview.setItemAnimator(new DefaultItemAnimator());
        shoppingcartrecyclerview.setAdapter(shoppingcartadapter);

        // calculate initial total price
        calculateTotalPrice();

        // handle button click to proceed to payment
        toCheckoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(ShoppingCart.this, PaymentType.class);
            double totalPrice = getTotalPrice();
            intent.putExtra("totalPrice", totalPrice);
            startActivity(intent);
        });
    }

    public void calculateTotalPrice() {
        double totalPrice = getTotalPrice();
        totalSumPrice.setText(String.format("$%.2f", totalPrice));
    }

    private double getTotalPrice() {
        double totalPrice = 0.0;
        for (Product product : productList) {
            totalPrice += product.getTotalprice();
        }
        return totalPrice;
    }

    private void initializeNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_cart);

        // create listeners for each menu item
        View.OnClickListener homeSingleClickListener = v -> speak("Open home");
        View.OnClickListener homeDoubleClickListener = v -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        };

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

        View.OnClickListener cartSingleClickListener = v -> speak("Already on cart");
        View.OnClickListener cartDoubleClickListener = v -> Toast.makeText(this, "It is on the page already", Toast.LENGTH_SHORT).show();

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