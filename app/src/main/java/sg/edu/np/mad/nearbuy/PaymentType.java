package sg.edu.np.mad.nearbuy;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
        adapter = new PayItemAdapter(mList, selectedItem);
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

        String[] cardTypes = {"MasterCard", "VISA", "GrabPay", "MayBank", "CitiBank", "GXSBank"};

        for (String cardType : cardTypes) {
            List<String> nestedList = new ArrayList<>();
            Cursor cursor = dbCard.getCardsByType(cardType);
            if (cursor != null) {
                int labelIndex = cursor.getColumnIndex("label");
                int cardNumberIndex = cursor.getColumnIndex("card_number");

                if (labelIndex >= 0 && cardNumberIndex >= 0) {
                    while (cursor.moveToNext()) {
                        String label = cursor.getString(labelIndex);
                        String cardNumber = cursor.getString(cardNumberIndex);
                        nestedList.add(label + " - " + cardNumber);
                    }
                }
                cursor.close();
            }
            mList.add(new PayDataModel(nestedList, cardType));
        }

        adapter.notifyDataSetChanged();
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
