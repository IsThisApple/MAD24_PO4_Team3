package sg.edu.np.mad.nearbuy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Payment extends AppCompatActivity {

    private RecyclerView itemsRecyclerView;
    private PayItemAdapter adapter;
    private List<PayDataModel> itemList;

    ArrayList<String> dataSource;
    LinearLayoutManager linearLayoutManager;

    AddressAdapter addressAdapter;

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

        RecyclerView addressView = findViewById(R.id.addressView);
        dataSource = new ArrayList<>();
        dataSource.add("Home");
        dataSource.add("Work");
        dataSource.add("New House");

        linearLayoutManager = new LinearLayoutManager(Payment.this, LinearLayoutManager.HORIZONTAL, false);
        addressAdapter = new AddressAdapter(dataSource);
        addressView.setLayoutManager(linearLayoutManager);
        addressView.setAdapter(addressAdapter);

        Button confirmPaymentButton = findViewById(R.id.confirmPayment);

        // Handle button click to proceed to end
        confirmPaymentButton.setOnClickListener(v -> {
            Intent intent = new Intent(Payment.this, MainActivity.class);
            startActivity(intent);
        });

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


}