package sg.edu.np.mad.nearbuy;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Payment extends AppCompatActivity {
    private RecyclerView itemsRecyclerView;
    private PayItemAdapter adapter;
    private List<PayDataModel> itemList;
    private TextToSpeech tts;

    ArrayList<String> dataSource;
    LinearLayoutManager linearLayoutManager;
    AddressAdapter addressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // initialise tts
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        String paymentType = getIntent().getStringExtra("paymentType");
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        TextView paymentTypeTextView = findViewById(R.id.paymentType);
        paymentTypeTextView.setText(paymentType);

        TextView subtotalPriceTextView = findViewById(R.id.subtotalPrice);
        TextView deliveryPriceTextView = findViewById(R.id.deliveryPrice);
        TextView totalFinalPriceTextView = findViewById(R.id.totalFinalPrice);

        subtotalPriceTextView.setText(String.format("$%.2f", totalPrice));

        double deliveryPrice = 0.0;
        deliveryPriceTextView.setText(String.format("$%.2f", deliveryPrice));

        double totalFinalPrice = totalPrice + deliveryPrice;
        totalFinalPriceTextView.setText(String.format("$%.2f", totalFinalPrice));

        RecyclerView addressView = findViewById(R.id.addressView);
        dataSource = new ArrayList<>();
        dataSource.add("Home");
        dataSource.add("Work");
        dataSource.add("New House");

        linearLayoutManager = new LinearLayoutManager(Payment.this, LinearLayoutManager.HORIZONTAL, false);
        addressAdapter = new AddressAdapter(dataSource);
        addressView.setLayoutManager(linearLayoutManager);
        addressView.setAdapter(addressAdapter);

        ImageView backButton = findViewById(R.id.backButton);
        // set up back button click listener to return to checkout
        backButton.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Back to checkout"),
                v -> finish()
        ));

        Button confirmPaymentButton = findViewById(R.id.confirmPayment);

        // handle button click to proceed to end
        confirmPaymentButton.setOnClickListener(v -> {
            Intent intent = new Intent(Payment.this, MainActivity.class);
            startActivity(intent);
        });

        setupAdditionalTTSAndClickListeners();
    }

    private void setupAdditionalTTSAndClickListeners() {
        TextView subtotalPrice = findViewById(R.id.subtotalPrice);
        TextView deliveryPrice = findViewById(R.id.deliveryPrice);
        TextView totalFinalPrice = findViewById(R.id.totalFinalPrice);
        TextView paymentType = findViewById(R.id.paymentType);
        EditText postalCode = findViewById(R.id.postalCode);
        EditText street = findViewById(R.id.street);
        EditText unitNo = findViewById(R.id.unitNo);
        EditText saveAs = findViewById(R.id.saveAs);
        Button saveLabel = findViewById(R.id.saveLabel);
        Button confirmPaymentButton = findViewById(R.id.confirmPayment);

        subtotalPrice.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Subtotal:" + subtotalPrice.getText().toString()),
                v -> speakText("Subtotal:" + subtotalPrice.getText().toString())
        ));

        deliveryPrice.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Delivery fee:" + deliveryPrice.getText().toString()),
                v -> speakText("Delivery fee:" + deliveryPrice.getText().toString())
        ));

        totalFinalPrice.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Total:" + totalFinalPrice.getText().toString()),
                v -> speakText("Total:" + totalFinalPrice.getText().toString())
        ));

        paymentType.setOnTouchListener(new DoubleClickListener(
                v -> speakText(paymentType.getText().toString()),
                v -> speakText(paymentType.getText().toString())
        ));

        postalCode.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Enter postal code"),
                v -> {
                    speakText("Enter postal code");
                    postalCode.requestFocus(); // double-click action
                }
        ));

        street.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Enter street address"),
                v -> {
                    speakText("Enter street address");
                    street.requestFocus(); // double-click action
                }
        ));

        unitNo.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Enter unit number (N.A. if none)"),
                v -> {
                    speakText("Enter unit number (N.A. if none)");
                    unitNo.requestFocus(); // double-click action
                }
        ));

        saveLabel.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Save address"),
                v -> {
                    speakText("Save address");
                    String label = saveAs.getText().toString();
                    if (!label.isEmpty()) {
                        dataSource.add(label);
                        addressAdapter.notifyDataSetChanged(); // double-click action
                    }
                }
        ));

        confirmPaymentButton.setOnTouchListener(new DoubleClickListener(
                v -> speakText("Confirm payment"),
                v -> {
                    speakText("Confirm payment");
                    Intent intent = new Intent(Payment.this, MainActivity.class);
                    startActivity(intent); // double-click action
                }
        ));
    }

    class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressHolder>{
        ArrayList<String> addData;

        public AddressAdapter(ArrayList<String> addData) {
            this.addData = addData;
        }

        @NonNull
        @Override
        public AddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(Payment.this).inflate(R.layout.each_address, parent, false);
            return new AddressHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AddressHolder holder, int position) {
            holder.addressTitle.setText(addData.get(position));
        }

        @Override
        public int getItemCount() {
            return addData.size();
        }

        class AddressHolder extends RecyclerView.ViewHolder{
            TextView addressTitle;
            public AddressHolder(@NonNull View itemView) {
                super(itemView);
                addressTitle = itemView.findViewById(R.id.addressTitle);
            }
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