package sg.edu.np.mad.nearbuy;

import android.content.Context;
import android.speech.tts.TextToSpeech;
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
import java.util.Locale;

public class PayItemAdapter extends RecyclerView.Adapter<PayItemAdapter.ItemViewHolder> {
    private List<PayDataModel> mList; // List of PayDataModel objects
    private PaySelectedItem selectedItem; // Callback for item selection
    private Context context; // Context for accessing resources
    private DBCard dbCard; // Database handler for card operations
    private PreferenceManager preferenceManager;
    private TextToSpeech tts;

    // Constructor to initialize adapter with context, data list, and item selection callback
    public PayItemAdapter(Context context, List<PayDataModel> mList, PaySelectedItem selectedItem, DBCard dbCard, TextToSpeech tts) {
        this.context = context;
        this.mList = mList;
        this.selectedItem = selectedItem;
        this.dbCard = new DBCard(context); // Initialize DBCard instance
        this.preferenceManager = new PreferenceManager(context);
        this.tts = tts;
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
        Context context = holder.itemView.getContext();
        PayNestedAdapter adapter = new PayNestedAdapter(
                model.getNestedList(),
                selectedItem,
                position,
                context,
                this,
                dbCard,
                tts
        );
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.nestedRecyclerView.setHasFixedSize(true); // Optimize RecyclerView size
        holder.nestedRecyclerView.setAdapter(adapter); // Set nested RecyclerView adapter

        boolean isAccessibilityEnabled = preferenceManager.isAccessibilityEnabled();

        if (isAccessibilityEnabled) {
            holder.paymentTitle.setOnTouchListener(new DoubleClickListener(
                    v -> speakText(model.getItemText()), // single-click TTS
                    v -> {
                        speakText(model.getItemText());
                        model.setExpandable(!model.isExpandable());
                        notifyDataSetChanged();
                    }  // double-click toggle expand/collapse
            ));
        } else {
            holder.paymentTitle.setOnTouchListener(new DoubleClickListener(
                    v -> {
                        model.setExpandable(!model.isExpandable());
                        notifyDataSetChanged();
                    }, // single-click TTS
                    v -> {}  // double-click toggle expand/collapse
            ));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size(); // Return the number of items in the list
    }

    private void speakText(String text) {
        if (tts != null && !tts.isSpeaking()) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    // ViewHolder class to hold item views
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout; // Main layout for item
        private RelativeLayout expandableLayout; // Layout to expand/collapse
        private TextView mTextView; // TextView for item text
        private ImageView mArrowImage; // ImageView for arrow icon
        private RecyclerView nestedRecyclerView; // Nested RecyclerView for expandable items
        private View paymentTitle;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            mTextView = itemView.findViewById(R.id.itemTv);
            mArrowImage = itemView.findViewById(R.id.arro_imageview);
            nestedRecyclerView = itemView.findViewById(R.id.child_rv);
            paymentTitle = itemView.findViewById(R.id.paymentTitle);
        }
    }

}