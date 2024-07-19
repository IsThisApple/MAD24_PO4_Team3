package sg.edu.np.mad.nearbuy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Payment extends AppCompatActivity {

    private RecyclerView itemsRecyclerView;
    private PayItemAdapter adapter;
    private List<PayDataModel> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

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

        Button addressButton = findViewById(R.id.addressInput);
        EditText addressEditText = findViewById(R.id.addressEditText);

        addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressEditText.setVisibility(View.VISIBLE);
                addressButton.setVisibility(View.GONE);
            }
        });

        // return button
        ImageView backbutton = findViewById(R.id.backButton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addressEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String address = addressEditText.getText().toString();
                    addressButton.setText(address);
                    addressButton.setVisibility(View.VISIBLE);
                    addressEditText.setVisibility(View.GONE);
                }
            }
        });
        Button confirmPaymentButton = findViewById(R.id.confirmPayment);

        // handle button click to proceed to end
        confirmPaymentButton.setOnClickListener(v -> {
            Intent intent = new Intent(Payment.this, MainActivity.class);
            startActivity(intent);
        });
    }

}