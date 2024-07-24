package sg.edu.np.mad.nearbuy;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Payment extends AppCompatActivity {

    private RecyclerView addressView;
    private AddressAdapter addressAdapter;
    private ArrayList<String> addresses;
    private TextView paymentTypeTextView;
    private TextView subtotalPriceTextView;
    private TextView deliveryPriceTextView;
    private TextView totalFinalPriceTextView;

    private static final int REQUEST_CODE_ADD_ADDRESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize UI components
        paymentTypeTextView = findViewById(R.id.paymentType);
        subtotalPriceTextView = findViewById(R.id.subtotalPrice);
        deliveryPriceTextView = findViewById(R.id.deliveryPrice);
        totalFinalPriceTextView = findViewById(R.id.totalFinalPrice);
        addressView = findViewById(R.id.addressView);

        // Get intent extras
        String paymentType = getIntent().getStringExtra("paymentType");
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        // Set payment type and prices
        paymentTypeTextView.setText(paymentType);
        subtotalPriceTextView.setText(String.format("$%.2f", totalPrice));

        double deliveryPrice = 0.0;
        deliveryPriceTextView.setText(String.format("$%.2f", deliveryPrice));

        double totalFinalPrice = totalPrice + deliveryPrice;
        totalFinalPriceTextView.setText(String.format("$%.2f", totalFinalPrice));

        // Initialize and set up RecyclerView
        addresses = new ArrayList<>();
        addressAdapter = new AddressAdapter(addresses, new AddressAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(String address, int position) {
                DBAddress dbAddress = new DBAddress(Payment.this);
                dbAddress.deleteAddress(address); // Delete address from database
                addresses.remove(position); // Remove from list
                addressAdapter.notifyItemRemoved(position); // Notify adapter
                Toast.makeText(Payment.this, "Address removed", Toast.LENGTH_SHORT).show();
            }
        }, this); // Pass context to adapter
        addressView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        addressView.setAdapter(addressAdapter);

        // Load addresses from database
        loadAddresses();

        // Set up buttons
        Button addAddress = findViewById(R.id.newAdd);
        Button confirmPaymentButton = findViewById(R.id.confirmPayment);

        addAddress.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddAddress.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS);
        });

        confirmPaymentButton.setOnClickListener(v -> {
            Intent intent = new Intent(Payment.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_ADDRESS && resultCode == RESULT_OK) {
            loadAddresses(); // Reload addresses after returning from AddAddress activity
        }
    }

    private void loadAddresses() {
        DBAddress dbAddress = new DBAddress(this);
        Cursor cursor = dbAddress.getAllAddresses();
        addresses.clear(); // Clear existing addresses

        if (cursor != null) {
            int labelIndex = cursor.getColumnIndex("label");
            if (labelIndex != -1 && cursor.moveToFirst()) {
                do {
                    String addressLabel = cursor.getString(labelIndex);
                    addresses.add(addressLabel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        addressAdapter.notifyDataSetChanged(); // Notify adapter to update UI
    }

    // AddressAdapter class inside Payment activity
    static class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressHolder> {
        private ArrayList<String> addData;
        private OnDeleteClickListener onDeleteClickListener;
        private int selectedPosition = RecyclerView.NO_POSITION;
        private int selectedColor;
        private int defaultColor;

        public AddressAdapter(ArrayList<String> addData, OnDeleteClickListener onDeleteClickListener, Context context) {
            this.addData = addData;
            this.onDeleteClickListener = onDeleteClickListener;
            this.selectedColor = ContextCompat.getColor(context, R.color.lightblue); // Get lightblue color from resources
            this.defaultColor = Color.TRANSPARENT; // Default color
        }

        @NonNull
        @Override
        public AddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_address, parent, false);
            return new AddressHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AddressHolder holder, int position) {
            String address = addData.get(position);
            holder.addressTitle.setText(address);

            // Set background color based on selected position
            if (selectedPosition == position) {
                holder.itemView.setBackgroundColor(selectedColor);
            } else {
                holder.itemView.setBackgroundColor(defaultColor);
            }

            holder.itemView.setOnClickListener(v -> {
                // Update the selected position
                notifyItemChanged(selectedPosition);
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(selectedPosition);
            });

            holder.delAdd.setOnClickListener(v -> {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(address, position);
                    if (position == selectedPosition) {
                        selectedPosition = RecyclerView.NO_POSITION;
                    }
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                }
            });
        }

        @Override
        public int getItemCount() {
            return addData.size();
        }

        static class AddressHolder extends RecyclerView.ViewHolder {
            TextView addressTitle;
            TextView delAdd;

            public AddressHolder(@NonNull View itemView) {
                super(itemView);
                addressTitle = itemView.findViewById(R.id.addressTitle);
                delAdd = itemView.findViewById(R.id.delAdd);
            }
        }

        interface OnDeleteClickListener {
            void onDeleteClick(String address, int position);
        }
    }
}
