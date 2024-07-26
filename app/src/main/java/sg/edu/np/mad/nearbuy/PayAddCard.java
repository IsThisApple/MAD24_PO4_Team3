package sg.edu.np.mad.nearbuy;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
import java.util.Locale;

public class PayAddCard extends AppCompatActivity {
    // Data source for card types
    ArrayList<String> dataSource;
    private int selectedPosition = -1; // Track selected card type
    CardAdapter cardAdapter;
    LinearLayoutManager linearLayoutManager;
    DBCard dbCard; // Database helper for card operations
    private PreferenceManager preferenceManager;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enable edge-to-edge display
        setContentView(R.layout.activity_pay_add_card);

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

        setupTTSAndClickListeners();
    }

    private void setupTTSAndClickListeners() {
        TextView chooseCardText = findViewById(R.id.carcar);
        EditText cardNumberField = findViewById(R.id.cardNum);
        EditText expirationMonthField = findViewById(R.id.month);
        EditText expirationYearField = findViewById(R.id.year);
        TextView cvnDesc = findViewById(R.id.cvndesc);
        EditText cvnField = findViewById(R.id.cvn);
        EditText labelField = findViewById(R.id.label);
        Button addAccButton = findViewById(R.id.addAcc);
        ImageView backButton = findViewById(R.id.backButton);

        boolean isAccessibilityEnabled = preferenceManager.isAccessibilityEnabled();

        if(isAccessibilityEnabled) {
            chooseCardText.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Choose card"),
                    v -> speakText("Choose card")
            ));
        } else {}

        if(isAccessibilityEnabled) {
            cardNumberField.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Enter card number"),
                    v -> {
                        speakText("Enter card number");
                        cardNumberField.requestFocus(); // double-click action
                    }
            ));
        } else {
            cardNumberField.setOnTouchListener(new DoubleClickListener(
                    v -> cardNumberField.requestFocus(),
                    v -> {}
            ));
        }

        if(isAccessibilityEnabled) {
            expirationMonthField.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Enter card expiration month"),
                    v -> {
                        speakText("Enter card expiration month");
                        expirationMonthField.requestFocus(); // double-click action
                    }
            ));
        } else {
            expirationMonthField.setOnTouchListener(new DoubleClickListener(
                    v -> expirationMonthField.requestFocus(),
                    v -> {}
            ));
        }

        if(isAccessibilityEnabled) {
            expirationYearField.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Enter card expiration year"),
                    v -> {
                        speakText("Enter card expiration year");
                        expirationYearField.requestFocus(); // double-click action
                    }
            ));
        } else {
            expirationYearField.setOnTouchListener(new DoubleClickListener(
                    v -> expirationYearField.requestFocus(),
                    v -> {}
            ));
        }

        if(isAccessibilityEnabled) {
            cvnDesc.setOnTouchListener(new DoubleClickListener(
                    v -> speakText(cvnDesc.getText().toString()),
                    v -> speakText(cvnDesc.getText().toString())
            ));
        } else {}

        if(isAccessibilityEnabled) {
            cvnField.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Enter CVN"),
                    v -> {
                        speakText("Enter CVN");
                        cvnField.requestFocus(); // double-click action
                    }
            ));
        } else {
            cvnField.setOnTouchListener(new DoubleClickListener(
                    v -> cvnField.requestFocus(),
                    v -> {}
            ));
        }

        if(isAccessibilityEnabled) {
            labelField.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Enter account label"),
                    v -> {
                        speakText("Enter account label");
                        labelField.requestFocus(); // double-click action
                    }
            ));
        } else {
            labelField.setOnTouchListener(new DoubleClickListener(
                    v -> labelField.requestFocus(),
                    v -> {}
            ));
        }

        // Setup add account button click listener
        if(isAccessibilityEnabled) {
            addAccButton.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Add account"),
                    v -> {
                        speakText("Add account");
                        addAccount(); // call add account method when button is clicked
                        // double-click action
                    }
            ));
        } else {
            addAccButton.setOnTouchListener(new DoubleClickListener(
                    v -> addAccount(), // call add account method when button is clicked,
                    v -> {}
            ));
        }

        if(isAccessibilityEnabled) {
            backButton.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Back to checkout"),
                    v -> {
                        speakText("Back to checkout");
                        finish();
                        // double-click action
                    }
            ));
        } else {
            backButton.setOnTouchListener(new DoubleClickListener(
                    v -> finish(),
                    v -> {}
            ));
        }
    }

    private void addAccount() {
        String cardType = dataSource.get(selectedPosition);
        EditText cardNumberField = findViewById(R.id.cardNum);
        EditText expirationMonthField = findViewById(R.id.month);
        EditText expirationYearField = findViewById(R.id.year);
        EditText cvnField = findViewById(R.id.cvn);
        EditText labelField = findViewById(R.id.label);

        boolean isAccessibilityEnabled = preferenceManager.isAccessibilityEnabled();

        // Check if a card type is selected
        if (selectedPosition == -1) {
            Toast.makeText(this, "Please select a card type", Toast.LENGTH_SHORT).show();
            return;
        }

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
        boolean isInserted = dbCard.addCard(getCurrentUserId(), cardType, cardNumber, Integer.parseInt(expirationMonth), Integer.parseInt(expirationYear), cvn, label);

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

            boolean isAccessibilityEnabled = preferenceManager.isAccessibilityEnabled();

            if(isAccessibilityEnabled) {
                holder.itemView.setOnTouchListener(new DoubleClickListener(
                        v -> speakText(addData.get(position)), // single-click tts
                        v -> {
                            speakText(addData.get(position));
                            selectedPosition = holder.getAdapterPosition();
                            notifyDataSetChanged(); // notify the adapter to refresh all items
                        } // double-click action
                ));
            } else {
                holder.itemView.setOnTouchListener(new DoubleClickListener(
                        v -> {
                            selectedPosition = holder.getAdapterPosition();
                            notifyDataSetChanged(); // notify the adapter to refresh all items, // single-click tts
                        },
                        v -> {} // double-click action
                ));
            }
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