package sg.edu.np.mad.nearbuy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PayItemAdapter extends RecyclerView.Adapter<PayItemAdapter.ItemViewHolder> {

    private List<PayDataModel> mList; // List of PayDataModel objects
    private PaySelectedItem selectedItem; // Callback for item selection
    private Context context; // Context for accessing resources
    private DBCard dbCard; // Database handler for card operations

    // Constructor to initialize adapter with context, data list, and item selection callback
    public PayItemAdapter(Context context, List<PayDataModel> mList, PaySelectedItem selectedItem) {
        this.context = context;
        this.mList = mList;
        this.selectedItem = selectedItem;
        this.dbCard = new DBCard(context); // Initialize DBCard instance
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate each_item layout for each item in the list
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_item, parent, false);
        return new ItemViewHolder(view); // Return the ViewHolder for the inflated view
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Get PayDataModel object at the current position
        PayDataModel model = mList.get(position);
        holder.mTextView.setText(model.getItemText()); // Set item text

        boolean isExpandable = model.isExpandable(); // Check if item is expandable
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE); // Show or hide expandable layout

        // Set arrow image resource based on expandable state
        if (isExpandable) {
            holder.mArrowImage.setImageResource(R.drawable.baseline_arrow_down);
        } else {
            holder.mArrowImage.setImageResource(R.drawable.baseline_arrow_right);
        }

        // Setup nested RecyclerView with PayNestedAdapter
        PayNestedAdapter adapter = new PayNestedAdapter(model.getNestedList(), selectedItem, position, context, this, dbCard);
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.nestedRecyclerView.setHasFixedSize(true); // Optimize RecyclerView size
        holder.nestedRecyclerView.setAdapter(adapter); // Set nested RecyclerView adapter

        // Toggle expandable state when clicked
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setExpandable(!model.isExpandable()); // Toggle expand/collapse
                notifyDataSetChanged(); // Refresh the list
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size(); // Return the number of items in the list
    }

    // ViewHolder class to hold item views
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout; // Main layout for item
        private RelativeLayout expandableLayout; // Layout to expand/collapse
        private TextView mTextView; // TextView for item text
        private ImageView mArrowImage; // ImageView for arrow icon
        private RecyclerView nestedRecyclerView; // Nested RecyclerView for expandable items

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            mTextView = itemView.findViewById(R.id.itemTv);
            mArrowImage = itemView.findViewById(R.id.arro_imageview);
            nestedRecyclerView = itemView.findViewById(R.id.child_rv);
        }
    }
}
