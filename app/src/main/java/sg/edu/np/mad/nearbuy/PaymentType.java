package sg.edu.np.mad.nearbuy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PaymentType extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<PayDataModel> mList;
    private PaySelectedItem selectedItem;
    private PayItemAdapter adapter;
    private double totalPrice;
    private DBCard dbCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_type);

        dbCard = new DBCard(this);

        recyclerView = findViewById(R.id.main_recyclervie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mList = new ArrayList<>();
        selectedItem = new PaySelectedItem();
        adapter = new PayItemAdapter(this, mList, selectedItem); // Pass context here
        recyclerView.setAdapter(adapter);

        totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        TextView totalSumPrice = findViewById(R.id.totalSumPrice);
        totalSumPrice.setText(String.format("$%.2f", totalPrice));

        Button addAccount = findViewById(R.id.newAcc);
        Button proceedToPaymentButton = findViewById(R.id.toPayment);
        proceedToPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedToPayment();
            }
        });

        addAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PayAddCard.class);
            startActivity(intent);
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadCardData();
    }

    private void loadCardData() {
        mList.clear();

        String userId = getCurrentUserId();

        String[] cardTypes = {"MasterCard", "VISA", "GrabPay", "MayBank", "CitiBank", "GXSBank"};

        for (String cardType : cardTypes) {
            List<String> nestedList = new ArrayList<>();
            Cursor cursor = dbCard.getCardsByType(userId, cardType);
            if (cursor != null) {
                int labelIndex = cursor.getColumnIndex("label");
                int cardNumberIndex = cursor.getColumnIndex("card_number");

                if (labelIndex >= 0 && cardNumberIndex >= 0) {
                    while (cursor.moveToNext()) {
                        String label = cursor.getString(labelIndex);
                        String cardNumber = cursor.getString(cardNumberIndex);
                        nestedList.add(label);
                    }
                }
                cursor.close();
            }
            mList.add(new PayDataModel(nestedList, cardType));
        }

        adapter.notifyDataSetChanged();
    }

    private String getCurrentUserId() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getString("currentUserId", ""); // Return the stored user ID or an empty string if not found
    }

    private void proceedToPayment() {
        if (selectedItem.parentIndex != -1 && selectedItem.nestedIndex != -1) {
            String paymentType = mList.get(selectedItem.parentIndex).getItemText() + ": " + mList.get(selectedItem.parentIndex).getNestedList().get(selectedItem.nestedIndex);

            Intent intent = new Intent(PaymentType.this, Payment.class);
            intent.putExtra("paymentType", paymentType);
            intent.putExtra("totalPrice", totalPrice);
            intent.putParcelableArrayListExtra("itemList", new ArrayList<>(mList)); // Pass the list of items

            startActivity(intent);
        } else {
            Toast.makeText(PaymentType.this, "Please select a payment type", Toast.LENGTH_SHORT).show();
        }
    }
}

/*
package sg.edu.np.mad.nearbuy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PaymentType extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<PayDataModel> mList;
    private PaySelectedItem selectedItem;
    private PayItemAdapter adapter;
    private double totalPrice;
    private DBCard dbCard;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_type);

        // initialise tts
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        dbCard = new DBCard(this);

        recyclerView = findViewById(R.id.main_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mList = new ArrayList<>();
        selectedItem = new PaySelectedItem();
        adapter = new PayItemAdapter(mList, selectedItem, tts);
        recyclerView.setAdapter(adapter);

        totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        TextView totalSumPrice = findViewById(R.id.totalSumPrice);
        totalSumPrice.setText(String.format("$%.2f", totalPrice));

        Button addAccount = findViewById(R.id.newAcc);
        Button proceedToPaymentButton = findViewById(R.id.toPayment);

        // handle total sum price tts
        totalSumPrice.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Total price is" + totalSumPrice.getText().toString()),
                v -> speakText("Total price is" + totalSumPrice.getText().toString())
        ));

        // handle button click to proceed to payment
        proceedToPaymentButton.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Proceed to payment"),
                v -> {
                    speakText("Proceed to payment");
                    proceedToPayment();
                }
        ));

        // handle button click to add new account
        addAccount.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Add new account"),
                v -> {
                    speakText("Add new account");
                    Intent intent = new Intent(getApplicationContext(), PayAddCard.class);
                    startActivity(intent);
                }
        ));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCardData();
    }

    private void loadCardData() {
        mList.clear();

        String userId = getCurrentUserId();

        String[] cardTypes = {"MasterCard", "VISA", "GrabPay", "MayBank", "CitiBank", "GXSBank"};

        for (String cardType : cardTypes) {
            List<String> nestedList = new ArrayList<>();
            Cursor cursor = dbCard.getCardsByType(userId, cardType);
            if (cursor != null) {
                int labelIndex = cursor.getColumnIndex("label");
                int cardNumberIndex = cursor.getColumnIndex("card_number");

                if (labelIndex >= 0 && cardNumberIndex >= 0) {
                    while (cursor.moveToNext()) {
                        String label = cursor.getString(labelIndex);
                        String cardNumber = cursor.getString(cardNumberIndex);
                        nestedList.add(label);
                    }
                }
                cursor.close();
            }
            mList.add(new PayDataModel(nestedList, cardType));
        }

        adapter.notifyDataSetChanged();
    }

    private String getCurrentUserId() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getString("currentUserId", ""); // return the stored user id or an empty string if not found
    }

    private void proceedToPayment() {
        if (selectedItem.parentIndex != -1 && selectedItem.nestedIndex != -1) {
            String paymentType = mList.get(selectedItem.parentIndex).getItemText() + ": " + mList.get(selectedItem.parentIndex).getNestedList().get(selectedItem.nestedIndex);

            Intent intent = new Intent(PaymentType.this, Payment.class);
            intent.putExtra("paymentType", paymentType);
            intent.putExtra("totalPrice", totalPrice);
            intent.putParcelableArrayListExtra("itemList", new ArrayList<>(mList)); // pass the list of items

            startActivity(intent);
        } else {
            Toast.makeText(PaymentType.this, "Please select a payment type", Toast.LENGTH_SHORT).show();
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
*/