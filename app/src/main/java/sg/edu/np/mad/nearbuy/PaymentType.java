package sg.edu.np.mad.nearbuy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PaymentType extends AppCompatActivity {
    private RecyclerView recyclerView; // RecyclerView to display payment methods
    private List<PayDataModel> mList; // List to hold payment method data
    private PaySelectedItem selectedItem; // Object to keep track of selected payment item
    private PayItemAdapter adapter; // Adapter to bind data to RecyclerView
    private double totalPrice; // Total price to be displayed and passed to the Payment activity
    private DBCard dbCard; // Database helper for managing card data
    private PreferenceManager preferenceManager;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_type); // Set layout for this activity

        preferenceManager = new PreferenceManager(this);

        // initialise tts
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        // Initialise database helper
        dbCard = new DBCard(this);

        recyclerView = findViewById(R.id.main_recyclervie); // Find RecyclerView by ID
        recyclerView.setHasFixedSize(true); // Optimize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set layout manager

        mList = new ArrayList<>(); // Initialize the list for payment data
        selectedItem = new PaySelectedItem(); // Initialize selected item tracker
        adapter = new PayItemAdapter(this, mList, selectedItem, dbCard, tts); // Initialize adapter with data and selected item
        recyclerView.setAdapter(adapter); // Set adapter to RecyclerView

        totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0); // Retrieve total price from intent

        ImageView backbutton = findViewById(R.id.backbtn); // Find back button by ID

        boolean isAccessibilityEnabled = preferenceManager.isAccessibilityEnabled();

        // handle back button click to return to cart
        if (isAccessibilityEnabled) {
            backbutton.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Back to cart"),
                    v -> {
                        speakText("Back to cart");
                        finish();
                    }
            ));
        } else {
            backbutton.setOnTouchListener(new DoubleClickListener(
                    v -> finish(),
                    v -> {}
            ));
        }

        TextView totalSumPrice = findViewById(R.id.totalSumPrice); // Find TextView for total price
        totalSumPrice.setText(String.format("$%.2f", totalPrice)); // Display total price

        Button addAccount = findViewById(R.id.newAcc); // Find "Add Account" button by ID
        Button proceedToPaymentButton = findViewById(R.id.toPayment); // Find "Proceed to Payment" button by ID

        // handle total sum price tts
        if (isAccessibilityEnabled) {
            totalSumPrice.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Total price is" + totalSumPrice.getText().toString()),
                    v -> speakText("Total price is" + totalSumPrice.getText().toString())
            ));
        } else {
            // no single-click or double-click action needed
        }

        // handle button click to proceed to payment
        if (isAccessibilityEnabled) {
            proceedToPaymentButton.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Proceed to payment"),
                    v -> {
                        // speakText("Proceed to payment");
                        proceedToPayment();
                    }
            ));
        } else {
            proceedToPaymentButton.setOnTouchListener(new DoubleClickListener(
                    v -> proceedToPayment(),
                    v -> {}
            ));
        }

        // handle button click to add new account
        if (isAccessibilityEnabled) {
            addAccount.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Add new account"),
                    v -> {
                        speakText("Add new account");
                        Intent intent = new Intent(getApplicationContext(), PayAddCard.class);
                        startActivity(intent);
                    }
            ));
        } else {
            addAccount.setOnTouchListener(new DoubleClickListener(
                    v -> {
                        Intent intent = new Intent(getApplicationContext(), PayAddCard.class);
                        startActivity(intent);
                    },
                    v -> {}
            ));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCardData(); // Reload card data when the activity resumes
    }

    // Load card data from the database and update the RecyclerView
    private void loadCardData() {
        mList.clear(); // Clear existing data

        String userId = getCurrentUserId(); // Get current user ID

        String[] cardTypes = {"MasterCard", "VISA", "GrabPay", "MayBank", "CitiBank", "GXSBank"}; // Array of card types

        for (String cardType : cardTypes) {
            List<String> nestedList = new ArrayList<>(); // List to hold card labels
            Cursor cursor = dbCard.getCardsByType(userId, cardType); // Get cards by type from database
            if (cursor != null) {
                int labelIndex = cursor.getColumnIndex("label"); // Get column index for label
                int cardNumberIndex = cursor.getColumnIndex("card_number"); // Get column index for card number

                if (labelIndex >= 0 && cardNumberIndex >= 0) {
                    while (cursor.moveToNext()) { // Iterate through cursor
                        String label = cursor.getString(labelIndex); // Get label from cursor
                        String cardNumber = cursor.getString(cardNumberIndex); // Get card number from cursor
                        nestedList.add(label); // Add label to nested list
                    }
                }
                cursor.close(); // Close cursor
            }
            mList.add(new PayDataModel(nestedList, cardType)); // Add new PayDataModel to the list
        }

        adapter.notifyDataSetChanged(); // Notify adapter of data changes
    }

    // Retrieve the current user ID from SharedPreferences
    private String getCurrentUserId() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getString("currentUserId", ""); // Return stored user ID or empty string if not found
    }

    // Handle proceeding to the Payment activity
    private void proceedToPayment() {
        if (selectedItem.parentIndex != -1 && selectedItem.nestedIndex != -1) { // Check if a payment type is selected
            String paymentType = mList.get(selectedItem.parentIndex).getItemText() + ": " + mList.get(selectedItem.parentIndex).getNestedList().get(selectedItem.nestedIndex);

            Intent intent = new Intent(PaymentType.this, Payment.class); // Create intent to start Payment activity
            intent.putExtra("paymentType", paymentType); // Pass selected payment type
            intent.putExtra("totalPrice", totalPrice); // Pass total price
            intent.putParcelableArrayListExtra("itemList", new ArrayList<>(mList)); // Pass list of items

            startActivity(intent); // Start Payment activity
        } else {
            Toast.makeText(PaymentType.this, "Please select a payment type", Toast.LENGTH_SHORT).show(); // Show error if no payment type is selected
        }
    }

    private void speakText(String text) {
        if (tts != null && !tts.isSpeaking()) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
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