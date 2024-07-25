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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Payment extends AppCompatActivity {
    private RecyclerView addressView; // RecyclerView to display addresses
    private AddressAdapter addressAdapter; // Adapter for the RecyclerView
    private ArrayList<String> addresses; // List of addresses
    private TextView paymentTypeTextView; // TextView for displaying payment type
    private TextView subtotalPriceTextView; // TextView for displaying subtotal price
    private TextView deliveryPriceTextView; // TextView for displaying delivery price
    private TextView totalFinalPriceTextView; // TextView for displaying final total price
    private ShoppingCartDbHandler dbHandler; // Database handler for shopping cart

    private static final int REQUEST_CODE_ADD_ADDRESS = 1; // Request code for adding address
    private boolean isAddressSelected = false; // Flag to track if an address is selected

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

        // Initialize database handler
        dbHandler = new ShoppingCartDbHandler(this, null, null, 1);

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
                // Use DBAddress class for address operations
                DBAddress dbAddress = new DBAddress(Payment.this);
                dbAddress.deleteAddress(address); // Delete address from database
                addresses.remove(position); // Remove address from list
                addressAdapter.notifyItemRemoved(position); // Notify adapter of removal
                Toast.makeText(Payment.this, "Address removed", Toast.LENGTH_SHORT).show();
            }
        }, this); // Pass context to adapter
        addressView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        addressView.setAdapter(addressAdapter);

        // Handle back button click to return to PaymentType activity
        ImageView backbutton = findViewById(R.id.backButton);
        backbutton.setOnClickListener(v -> {
            finish();
        });

        // Load addresses from database
        loadAddresses();

        // Set up buttons
        Button addAddress = findViewById(R.id.newAdd);
        Button confirmPaymentButton = findViewById(R.id.confirmPayment);

        // Handle add address button click
        addAddress.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddAddress.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS);
        });

        // Handle confirm payment button click
        confirmPaymentButton.setOnClickListener(v -> {
            if (isAddressSelected) {
                // Clear shopping cart and reset total price
                dbHandler.clearAllProducts();

                // Update the total price in ShoppingCart activity
                Intent intent = new Intent(Payment.this, MainActivity.class);
                intent.putExtra("totalPrice", 0.0); // Pass updated total price
                startActivity(intent);

                Toast.makeText(Payment.this, "Payment confirmed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Payment.this, "Please select an address", Toast.LENGTH_SHORT).show();
            }
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
                    addresses.add(addressLabel); // Add address to list
                } while (cursor.moveToNext());
            }
            cursor.close(); // Close cursor
        }

        addressAdapter.notifyDataSetChanged(); // Notify adapter to update UI
    }

    // AddressAdapter class inside Payment activity
    static class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressHolder> {
        private ArrayList<String> addData; // List of addresses
        private OnDeleteClickListener onDeleteClickListener; // Listener for delete click events
        private int selectedPosition = RecyclerView.NO_POSITION; // Position of selected item
        private int selectedColor; // Color for selected item
        private int defaultColor; // Default color for non-selected items
        private Payment paymentActivity; // Reference to Payment activity

        public AddressAdapter(ArrayList<String> addData, OnDeleteClickListener onDeleteClickListener, Payment paymentActivity) {
            this.addData = addData;
            this.onDeleteClickListener = onDeleteClickListener;
            this.selectedColor = ContextCompat.getColor(paymentActivity, R.color.lightblue); // Get selected color from resources
            this.defaultColor = Color.TRANSPARENT; // Default color
            this.paymentActivity = paymentActivity; // Set Payment activity reference
        }

        @NonNull
        @Override
        public AddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate each_address layout for each address item
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_address, parent, false);
            return new AddressHolder(view); // Return the AddressHolder for the inflated view
        }

        @Override
        public void onBindViewHolder(@NonNull AddressHolder holder, int position) {
            String address = addData.get(position);
            holder.addressTitle.setText(address);

            // Set background color based on selected position
            if (selectedPosition == position) {
                holder.itemView.setBackgroundColor(selectedColor);
                paymentActivity.isAddressSelected = true; // Address is selected
            } else {
                holder.itemView.setBackgroundColor(defaultColor);
            }

            holder.itemView.setOnClickListener(v -> {
                // Update the selected position
                notifyItemChanged(selectedPosition);
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(selectedPosition);
                paymentActivity.isAddressSelected = true; // Address is selected
            });

            holder.delAdd.setOnClickListener(v -> {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(address, position);
                    if (position == selectedPosition) {
                        selectedPosition = RecyclerView.NO_POSITION;
                        paymentActivity.isAddressSelected = false; // No address selected
                    }
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount()); // Notify adapter of removal
                }
            });
        }

        @Override
        public int getItemCount() {
            return addData.size(); // Return the number of addresses
        }

        static class AddressHolder extends RecyclerView.ViewHolder {
            TextView addressTitle; // TextView for address label
            TextView delAdd; // TextView for delete button

            public AddressHolder(@NonNull View itemView) {
                super(itemView);
                addressTitle = itemView.findViewById(R.id.addressTitle);
                delAdd = itemView.findViewById(R.id.delAdd);
            }
        }

        interface OnDeleteClickListener {
            void onDeleteClick(String address, int position); // Interface for delete click event
        }
    }
}
