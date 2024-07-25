package sg.edu.np.mad.nearbuy;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PayAddCard extends AppCompatActivity {

    // Data source for card types
    ArrayList<String> dataSource;
    private int selectedPosition = -1; // Track selected card type
    CardAdapter cardAdapter;
    LinearLayoutManager linearLayoutManager;
    DBCard dbCard; // Database helper for card operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enable edge-to-edge display
        setContentView(R.layout.activity_pay_add_card);

        // Initialize database helper
        dbCard = new DBCard(this);

        // Setup RecyclerView for card types
        RecyclerView cardView = findViewById(R.id.card);
        dataSource = new ArrayList<>();
        dataSource.add("MasterCard");
        dataSource.add("VISA");
        dataSource.add("GrabPay");
        dataSource.add("MayBank");
        dataSource.add("CitiBank");
        dataSource.add("GXSBank");

        linearLayoutManager = new LinearLayoutManager(PayAddCard.this, LinearLayoutManager.HORIZONTAL, false);
        cardAdapter = new CardAdapter(dataSource);
        cardView.setLayoutManager(linearLayoutManager);
        cardView.setAdapter(cardAdapter);

        ImageView backbtn = findViewById(R.id.backbtn);
        // Find back button by ID

        // Handle back button click to return to the ShoppingCart activity
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Setup add account button click listener
        Button addAccButton = findViewById(R.id.addAcc);
        addAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccount(); // Call add account method when button is clicked
            }
        });
    }

    private void addAccount() {
        // Check if a card type is selected
        if (selectedPosition == -1) {
            Toast.makeText(this, "Please select a card type", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get input fields
        String cardType = dataSource.get(selectedPosition);
        EditText cardNumberField = findViewById(R.id.cardNum);
        EditText expirationMonthField = findViewById(R.id.month);
        EditText expirationYearField = findViewById(R.id.year);
        EditText cvnField = findViewById(R.id.cvn);
        EditText labelField = findViewById(R.id.label);

        // Retrieve and clean input data
        String cardNumber = cardNumberField.getText().toString().replaceAll("[^\\d]", ""); // Remove non-digits
        String expirationMonth = expirationMonthField.getText().toString();
        String expirationYear = expirationYearField.getText().toString();
        String cvn = cvnField.getText().toString();
        String label = labelField.getText().toString();

        // Validate card number
        if (cardNumber.length() != 16) {
            Toast.makeText(this, "Card number must have exactly 16 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if card number is unique within the card type
        if (!isCardNumberUnique(cardType, cardNumber)) {
            Toast.makeText(this, "Card number must be unique within this card type", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate expiration month
        if (!expirationMonth.matches("0[1-9]|1[0-2]")) {
            Toast.makeText(this, "Expiration month must be between 01 and 12", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate expiration year
        int year;
        try {
            year = Integer.parseInt(expirationYear);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Expiration year must be a valid number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (year <= 2024) {
            Toast.makeText(this, "Expiration year must be greater than the current year", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate CVN
        if (!cvn.matches("\\d{3}")) {
            Toast.makeText(this, "CVN must be exactly 3 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate label
        if (label.isEmpty()) {
            Toast.makeText(this, "Label must be provided", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isLabelUnique(cardType, label)) {
            Toast.makeText(this, "Label must be unique within this card type", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add card to database
        boolean isInserted = dbCard.addCard(getCurrentUserId(), cardType, cardNumber,
                Integer.parseInt(expirationMonth), year, cvn, label);

        if (isInserted) {
            Toast.makeText(this, "Account added successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close activity
        } else {
            Toast.makeText(this, "Failed to add account", Toast.LENGTH_SHORT).show();
        }
    }

    // Check if the card number is unique for the card type
    private boolean isCardNumberUnique(String cardType, String cardNumber) {
        return dbCard.isCardNumberUnique(getCurrentUserId(), cardType, cardNumber);
    }

    // Check if the label is unique for the card type
    private boolean isLabelUnique(String cardType, String label) {
        Cursor cursor = dbCard.getCardsByType(getCurrentUserId(), cardType);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String existingLabel = cursor.getString(cursor.getColumnIndex("label"));
                if (label.equals(existingLabel)) {
                    cursor.close();
                    return false; // Label already exists
                }
            }
            cursor.close();
        }
        return true; // Label is unique
    }

    // Get the current user ID from SharedPreferences
    private String getCurrentUserId() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getString("currentUserId", ""); // Return the stored user ID or an empty string if not found
    }

    // Adapter for displaying card types in RecyclerView
    class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder> {
        ArrayList<String> addData;

        public CardAdapter(ArrayList<String> addData) {
            this.addData = addData;
        }

        @NonNull
        @Override
        public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(PayAddCard.this).inflate(R.layout.each_card, parent, false);
            return new CardHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CardHolder holder, int position) {
            holder.cardTitle.setText(addData.get(position));

            // Highlight selected item
            if (position == selectedPosition) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(PayAddCard.this, R.color.lightblue));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(PayAddCard.this, android.R.color.transparent));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = holder.getAdapterPosition();
                    notifyDataSetChanged(); // Refresh all items
                }
            });
        }

        @Override
        public int getItemCount() {
            return addData.size();
        }

        // ViewHolder for card item
        class CardHolder extends RecyclerView.ViewHolder {
            TextView cardTitle;

            public CardHolder(@NonNull View itemView) {
                super(itemView);
                cardTitle = itemView.findViewById(R.id.cardTitle);
            }
        }
    }
}
