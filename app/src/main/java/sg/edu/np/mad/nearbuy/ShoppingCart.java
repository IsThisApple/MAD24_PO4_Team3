package sg.edu.np.mad.nearbuy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.speech.tts.TextToSpeech;
import java.util.Locale;
import java.util.ArrayList;

public class ShoppingCart extends AppCompatActivity {
    private TextView totalSumPrice;
    private ShoppingCartAdapter shoppingcartadapter;
    private ShoppingCartDbHandler dbHandler;
    private ArrayList<Product> productList;
    private PreferenceManager preferenceManager;
    private TextToSpeech tts;
    private ImageView addMoreItems;
    private TextView addItemsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        preferenceManager = new PreferenceManager(this);

        // Initialise views
        totalSumPrice = findViewById(R.id.totalSumPrice);
        RecyclerView shoppingcartrecyclerview = findViewById(R.id.shoppingcartrecyclerview);
        Button toCheckoutButton = findViewById(R.id.toCheckout);
        addMoreItems = findViewById(R.id.addMoreItems);
        addItemsText = findViewById(R.id.addItemText);

        // Handle image click to bring to main page to add more items
        addMoreItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // Handle text click to bring to main page to add more items
        addItemsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // Enable edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            v.setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
            return insets.consumeSystemWindowInsets();
        });

        // Initialise tts
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
            }
        });

        initializeNavigationBar();

        // Fetch products from the database
        dbHandler = new ShoppingCartDbHandler(this, null, null, 1);
        productList = dbHandler.getAllProducts();

        // Set up recyclerview
        shoppingcartadapter = new ShoppingCartAdapter(productList, this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        shoppingcartrecyclerview.setLayoutManager(layoutManager);
        shoppingcartrecyclerview.setItemAnimator(new DefaultItemAnimator());
        shoppingcartrecyclerview.setAdapter(shoppingcartadapter);

        // Calculate initial total price
        calculateTotalPrice();

        boolean isAccessibilityEnabled = preferenceManager.isAccessibilityEnabled();

        // Handle button click to proceed to checkout
        if (isAccessibilityEnabled) {
            toCheckoutButton.setOnTouchListener(new DoubleClickListener(
                    v -> speak("Proceed to checkout"), // single-click action
                    v -> {
                        speak("Proceed to checkout");
                        double totalPrice = getTotalPrice();
                        if (totalPrice > 0) {
                            Intent intent = new Intent(ShoppingCart.this, PaymentType.class);
                            intent.putExtra("totalPrice", totalPrice);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ShoppingCart.this, "Cart is empty. Add items to proceed.", Toast.LENGTH_SHORT).show();
                        }
                    } // double-click action
            ));
        } else {
            toCheckoutButton.setOnTouchListener(new DoubleClickListener(
                    v -> {
                        double totalPrice = getTotalPrice();
                        if (totalPrice > 0) {
                            Intent intent = new Intent(ShoppingCart.this, PaymentType.class);
                            intent.putExtra("totalPrice", totalPrice);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ShoppingCart.this, "Cart is empty. Add items to proceed.", Toast.LENGTH_SHORT).show();
                        }
                    }, // single-click action
                    v -> {} // double-click action
            ));
        }

        // Handle total price tts
        if (isAccessibilityEnabled) {
            totalSumPrice.setOnTouchListener(new DoubleClickListener(
                    v -> speakTotalPrice(), // single-click action
                    v -> speakTotalPrice() // double-click action
            ));
        } else {
            totalSumPrice.setOnTouchListener(new DoubleClickListener(
                    v -> {}, // single-click action
                    v -> {} // double-click action
            ));
        }

        // Handle image click to bring to main page to add more items
        if (isAccessibilityEnabled) {
            addMoreItems.setOnTouchListener(new DoubleClickListener(
                    v -> speak("Add more items"), // single-click action
                    v -> {
                        speak("Add more items");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } // double-click action
            ));
        } else {
            addMoreItems.setOnTouchListener(new DoubleClickListener(
                    v -> {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }, // single-click action
                    v -> {} // double-click action
            ));
        }
    }

    private void speakTotalPrice() {
        double totalPrice = getTotalPrice();
        speak("Total price is $" + String.format("%.2f", totalPrice));
    }

    public void calculateTotalPrice() {
        double totalPrice = getTotalPrice();
        totalSumPrice.setText(String.format("$%.2f", totalPrice));

        // Show or hide the addMoreItems and addItemsText views based on the total price
        if (totalPrice == 0) {
            addMoreItems.setVisibility(View.VISIBLE);
            addItemsText.setVisibility(View.VISIBLE);
        } else {
            addMoreItems.setVisibility(View.GONE);
            addItemsText.setVisibility(View.GONE);
        }
    }

    private double getTotalPrice() {
        double totalPrice = 0.0;
        for (Product product : productList) {
            totalPrice += product.getTotalprice();
        }
        return totalPrice;
    }

    public void updateTotalPrice(double totalPrice) {
        totalSumPrice.setText(String.format("$%.2f", totalPrice));
    }

    public void refreshProductList() {
        productList = dbHandler.getAllProducts();
        shoppingcartadapter.updateData(productList);
        calculateTotalPrice();
    }

    private void initializeNavigationBar() {
        boolean isAccessibilityEnabled = preferenceManager.isAccessibilityEnabled();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_cart);

        // Create listeners for each menu item

        View.OnClickListener homeSingleClickListener;
        View.OnClickListener homeDoubleClickListener = null;

        View.OnClickListener mapSingleClickListener;
        View.OnClickListener mapDoubleClickListener = null;

        View.OnClickListener chatSingleClickListener;
        View.OnClickListener chatDoubleClickListener = null;

        View.OnClickListener cartSingleClickListener;
        View.OnClickListener cartDoubleClickListener = null;

        if (isAccessibilityEnabled) {
            homeSingleClickListener = v -> speak("Open home");
            homeDoubleClickListener = v -> {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            };
        } else {
            homeSingleClickListener = v -> {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            };
        }

        if (isAccessibilityEnabled) {
            mapSingleClickListener = v -> speak("Open map");
            mapDoubleClickListener = v -> {
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
                finish();
            };
        } else {
            mapSingleClickListener = v -> {
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
                finish();
            };
        }

        if (isAccessibilityEnabled) {
            cartSingleClickListener = v -> speak("Already in cart");
            cartDoubleClickListener = v -> Toast.makeText(this, "It is on the page already", Toast.LENGTH_SHORT).show();
        } else {
            cartSingleClickListener = v -> Toast.makeText(this, "It is on the page already", Toast.LENGTH_SHORT).show();
        }

        // Set listeners
        bottomNavigationView.findViewById(R.id.bottom_home).setOnTouchListener(new DoubleClickListener(homeSingleClickListener, homeDoubleClickListener));
        bottomNavigationView.findViewById(R.id.bottom_map).setOnTouchListener(new DoubleClickListener(mapSingleClickListener, mapDoubleClickListener));
        bottomNavigationView.findViewById(R.id.bottom_cart).setOnTouchListener(new DoubleClickListener(cartSingleClickListener, cartDoubleClickListener));
    }

    // Method to speak text using tts
    public void speak(String text) {
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
