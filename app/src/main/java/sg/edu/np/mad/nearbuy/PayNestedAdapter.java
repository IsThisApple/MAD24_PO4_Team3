package sg.edu.np.mad.nearbuy;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PayNestedAdapter extends RecyclerView.Adapter<PayNestedAdapter.NestedViewHolder> {
    private List<String> mList;
    private PaySelectedItem selectedItem;
    private int parentPosition;
    private Context context;
    private PayItemAdapter itemAdapter;
    private DBCard dbCard;
    private PreferenceManager preferenceManager;
    private TextToSpeech tts;

    public PayNestedAdapter(List<String> mList, PaySelectedItem selectedItem, int parentPosition, Context context, PayItemAdapter itemAdapter, DBCard dbCard, TextToSpeech tts) {
        this.mList = mList;
        this.selectedItem = selectedItem;
        this.parentPosition = parentPosition;
        this.context = context;
        this.itemAdapter = itemAdapter;
        this.dbCard = dbCard;
        this.preferenceManager = new PreferenceManager(context);
        this.tts = tts;
    }

    @NonNull
    @Override
    public NestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nested_item , parent , false);
        return new NestedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedViewHolder holder, int position) {
        holder.mTv.setText(mList.get(position));
        String label = mList.get(position);
        holder.mTv.setText(label);

        // check if this item is the selected item
        if (selectedItem.parentIndex == parentPosition && selectedItem.nestedIndex == position) {
            int colorLightBlue = ContextCompat.getColor(context, R.color.lightblue);
            holder.itemView.setBackgroundColor(colorLightBlue);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        boolean isAccessibilityEnabled = preferenceManager.isAccessibilityEnabled();

        // set touch listener to handle double-click and tts
        if (isAccessibilityEnabled) {
            holder.itemView.setOnTouchListener(new DoubleClickListener(
                    v -> speakText(label), // single-click action
                    v -> {
                        speakText(label);
                        int clickedPosition = holder.getAdapterPosition();
                        if (selectedItem.parentIndex == parentPosition && selectedItem.nestedIndex == clickedPosition) {
                            // if the currently selected item is clicked again, unselect it
                            selectedItem.clearSelection();
                        } else {
                            // select the new item
                            selectedItem.parentIndex = parentPosition;
                            selectedItem.nestedIndex = clickedPosition;
                        }
                        itemAdapter.notifyDataSetChanged(); // notify the parent adapter to refresh all items
                        // double-click action
                    }
            ));
        } else {
            holder.itemView.setOnTouchListener(new DoubleClickListener(
                    v -> {
                        int clickedPosition = holder.getAdapterPosition();
                        if (selectedItem.parentIndex == parentPosition && selectedItem.nestedIndex == clickedPosition) {
                            // if the currently selected item is clicked again, unselect it
                            selectedItem.clearSelection();
                        } else {
                            // select the new item
                            selectedItem.parentIndex = parentPosition;
                            selectedItem.nestedIndex = clickedPosition;
                        }
                        itemAdapter.notifyDataSetChanged(); // notify the parent adapter to refresh all items
                    }, // single-click action
                    v -> {} // double-click action
            ));
        }

        if (isAccessibilityEnabled) {
            holder.delAcc.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Delete card"), // single-click action
                    v -> {
                        speakText("Delete card");
                        String userId = getCurrentUserId(); // method to get the current user ID
                        boolean isDeleted = dbCard.deleteCard(userId, label);
                        if (isDeleted) {
                            mList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mList.size());
                            Toast.makeText(context, "Card deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to delete card", Toast.LENGTH_SHORT).show();
                        }
                        // double-click action
                    }
            ));
        } else {
            holder.delAcc.setOnTouchListener(new DoubleClickListener(
                    v -> {
                        String userId = getCurrentUserId(); // method to get the current user ID
                        boolean isDeleted = dbCard.deleteCard(userId, label);
                        if (isDeleted) {
                            mList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mList.size());
                            Toast.makeText(context, "Card deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to delete card", Toast.LENGTH_SHORT).show();
                        }
                    }, // single-click action
                    v -> {}
                        // double-click action
            ));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void speakText(String text) {
        if (tts != null && !tts.isSpeaking()) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public class NestedViewHolder extends RecyclerView.ViewHolder {
        private TextView mTv;
        private TextView delAcc;

        public NestedViewHolder(@NonNull View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.nestedItemTv);
            delAcc = itemView.findViewById(R.id.delAcc);
        }
    }


    private void removeNestedItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size());
        selectedItem.clearSelection(); // clear selection if needed
        itemAdapter.notifyDataSetChanged(); // refresh the parent adapter
    }

    private String getCurrentUserId() {
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return prefs.getString("currentUserId", "");
    }
}