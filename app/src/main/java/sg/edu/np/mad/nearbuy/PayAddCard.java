
package sg.edu.np.mad.nearbuy;

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

        boolean isInserted = dbCard.addCard(cardType, cardNumber.getText().toString(),
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
