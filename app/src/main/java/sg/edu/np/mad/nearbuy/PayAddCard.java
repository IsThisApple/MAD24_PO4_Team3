package sg.edu.np.mad.nearbuy;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

    ArrayList<String> dataSource;
    private int selectedPosition = -1;
    CardAdapter cardAdapter;
    LinearLayoutManager linearLayoutManager;
    DBCard dbCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pay_add_card);

        dbCard = new DBCard(this);

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

        Button addAccButton = findViewById(R.id.addAcc);
        addAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccount();
            }
        });

    }

    private void addAccount() {
        if (selectedPosition == -1) {
            Toast.makeText(this, "Please select a card type", Toast.LENGTH_SHORT).show();
            return;
        }

        String cardType = dataSource.get(selectedPosition);
        EditText cardNumberField = findViewById(R.id.cardNum);
        EditText expirationMonthField = findViewById(R.id.month);
        EditText expirationYearField = findViewById(R.id.year);
        EditText cvnField = findViewById(R.id.cvn);
        EditText labelField = findViewById(R.id.label);

        String cardNumber = cardNumberField.getText().toString().replaceAll("[^\\d]", ""); // Remove non-digits
        String expirationMonth = expirationMonthField.getText().toString();
        String expirationYear = expirationYearField.getText().toString();
        String cvn = cvnField.getText().toString();
        String label = labelField.getText().toString();

        // Check if card number is exactly 16 digits
        if (cardNumber.length() != 16) {
            Toast.makeText(this, "Card number must have exactly 16 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if card number is unique within the card type
        if (!isCardNumberUnique(cardType, cardNumber)) {
            Toast.makeText(this, "Card number must be unique within this card type", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if expiration month is between 01 and 12
        if (!expirationMonth.matches("0[1-9]|1[0-2]")) {
            Toast.makeText(this, "Expiration month must be between 01 and 12", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if expiration year is greater than 2024
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

        // Check if CVN is exactly 3 digits
        if (!cvn.matches("\\d{3}")) {
            Toast.makeText(this, "CVN must be exactly 3 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if label is provided and is unique within the selected card type
        if (label.isEmpty()) {
            Toast.makeText(this, "Label must be provided", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isLabelUnique(cardType, label)) {
            Toast.makeText(this, "Label must be unique within this card type", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = dbCard.addCard(getCurrentUserId(), cardType, cardNumber,
                Integer.parseInt(expirationMonth), year, cvn, label);

        if (isInserted) {
            Toast.makeText(this, "Account added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add account", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isCardNumberUnique(String cardType, String cardNumber) {
        return dbCard.isCardNumberUnique(getCurrentUserId(), cardType, cardNumber);
    }


    private boolean isLabelUnique(String cardType, String label) {
        // Retrieve all labels for the given card type
        Cursor cursor = dbCard.getCardsByType(getCurrentUserId(), cardType);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String existingLabel = cursor.getString(cursor.getColumnIndex("label"));
                if (label.equals(existingLabel)) {
                    cursor.close();
                    return false;
                }
            }
            cursor.close();
        }
        return true;
    }




    private String getCurrentUserId() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getString("currentUserId", ""); // Return the stored user ID or an empty string if not found
    }

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

            // Check if this item is the selected item
            if (position == selectedPosition) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(PayAddCard.this, R.color.lightblue));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(PayAddCard.this, android.R.color.transparent));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = holder.getAdapterPosition();
                    notifyDataSetChanged(); // Notify the adapter to refresh all items
                }
            });
        }

        @Override
        public int getItemCount() {
            return addData.size();
        }

        class CardHolder extends RecyclerView.ViewHolder {
            TextView cardTitle;

            public CardHolder(@NonNull View itemView) {
                super(itemView);
                cardTitle = itemView.findViewById(R.id.cardTitle);
            }
        }
    }
}

/*
package sg.edu.np.mad.nearbuy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    ArrayList<String> dataSource;
    private int selectedPosition = -1;
    CardAdapter cardAdapter;
    LinearLayoutManager linearLayoutManager;
    DBCard dbCard;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pay_add_card);

        dbCard = new DBCard(this);

        // initialise tts
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

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

        setupAdditionalTTSAndClickListeners();
    }

    private void setupAdditionalTTSAndClickListeners() {
        TextView carcar = findViewById(R.id.carcar);
        EditText cardNum = findViewById(R.id.cardNum);
        EditText month = findViewById(R.id.month);
        EditText year = findViewById(R.id.year);
        TextView cvndesc = findViewById(R.id.cvndesc);
        EditText cvn = findViewById(R.id.cvn);
        EditText label = findViewById(R.id.label);
        Button addAcc = findViewById(R.id.addAcc);

        carcar.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Select card type"),
                v -> speakText("Select card type")
        ));

        cardNum.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Enter card number"),
                v -> {
                    speakText("Enter card number");
                    cardNum.requestFocus(); // double-click action
                }
        ));

        month.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Enter card expiration month"),
                v -> {
                    speakText("Enter card expiration month");
                    month.requestFocus(); // double-click action
                }
        ));

        year.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Enter card expiration year"),
                v -> {
                    speakText("Enter card expiration year");
                    year.requestFocus(); // double-click action
                }
        ));

        cvndesc.setOnTouchListener(new DoubleClickListener(
                v -> speakText(cvndesc.getText().toString()),
                v -> speakText(cvndesc.getText().toString())
        ));

        cvn.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Enter CVN"),
                v -> {
                    speakText("Enter CVN");
                    cvn.requestFocus(); // double-click action
                }
        ));

        label.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Enter account label"),
                v -> {
                    speakText("Enter account label");
                    label.requestFocus(); // double-click action
                }
        ));

        addAcc.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Add account"),
                v -> {
                    speakText("Add account");
                    // Button addAccButton = findViewById(R.id.addAcc);
                    addAccount(); // double-click action
                }
        ));
    }

    private void addAccount() {
        if (selectedPosition == -1) {
            Toast.makeText(this, "Please select a card type", Toast.LENGTH_SHORT).show();
            return;
        }

        String cardType = dataSource.get(selectedPosition);
        EditText cardNumber = findViewById(R.id.cardNum);
        EditText expirationMonth = findViewById(R.id.month);
        EditText expirationYear = findViewById(R.id.year);
        EditText cvn = findViewById(R.id.cvn);
        EditText label = findViewById(R.id.label);

        if (cardNumber.getText().toString().isEmpty() || expirationMonth.getText().toString().isEmpty() ||
                expirationYear.getText().toString().isEmpty() || cvn.getText().toString().isEmpty() ||
                label.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = getCurrentUserId();

        boolean isInserted = dbCard.addCard(userId, cardType, cardNumber.getText().toString(),
                Integer.parseInt(expirationMonth.getText().toString()),
                Integer.parseInt(expirationYear.getText().toString()),
                cvn.getText().toString(),
                label.getText().toString());

        if (isInserted) {
            Toast.makeText(this, "Account added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add account", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentUserId() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getString("currentUserId", ""); // return the stored user id or an empty string if not found
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

            // check if this item is the selected item
            if (position == selectedPosition) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(PayAddCard.this, R.color.lightblue));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(PayAddCard.this, android.R.color.transparent));
            }

            holder.itemView.setOnTouchListener(new DoubleClickListener(
                    v -> speakText(addData.get(position)), // single-click tts
                    v -> {
                        speakText(addData.get(position));
                        selectedPosition = holder.getAdapterPosition();
                        notifyDataSetChanged(); // notify the adapter to refresh all items
                    } // double-click action
            ));
        }

        @Override
        public int getItemCount() {
            return addData.size();
        }

        class CardHolder extends RecyclerView.ViewHolder {
            TextView cardTitle;

            public CardHolder(@NonNull View itemView) {
                super(itemView);
                cardTitle = itemView.findViewById(R.id.cardTitle);
            }
        }
    }

}
*/