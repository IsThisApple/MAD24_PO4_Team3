package sg.edu.np.mad.nearbuy;

import android.content.Intent;
import android.os.Bundle;
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

public class PaymentType extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<PayDataModel> mList;
    private PaySelectedItem selectedItem;
    private PayItemAdapter adapter;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_type);

        recyclerView = findViewById(R.id.main_recyclervie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mList = new ArrayList<>();

        //list1
        List<String> nestedList1 = new ArrayList<>();
        nestedList1.add("Savings");
        nestedList1.add("Spendings");

        List<String> nestedList2 = new ArrayList<>();
        nestedList2.add("Savings");
        nestedList2.add("Spendings");
        nestedList2.add("Account #3");

        List<String> nestedList3 = new ArrayList<>();
        nestedList3.add("Spendings");

        mList.add(new PayDataModel(nestedList1, "MasterCard"));
        mList.add(new PayDataModel(nestedList2, "Visa"));
        mList.add(new PayDataModel(nestedList3, "GrabPay"));

        selectedItem = new PaySelectedItem();

        adapter = new PayItemAdapter(mList, selectedItem);
        recyclerView.setAdapter(adapter);

        totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        TextView totalSumPrice = findViewById(R.id.totalSumPrice);
        totalSumPrice.setText(String.format("$%.2f", totalPrice));

        Button proceedToPaymentButton = findViewById(R.id.toPayment);
        proceedToPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedToPayment();
            }
        });

        ImageView backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
