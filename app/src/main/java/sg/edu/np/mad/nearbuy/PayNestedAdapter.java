package sg.edu.np.mad.nearbuy;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public PayNestedAdapter(List<String> mList, PaySelectedItem selectedItem, int parentPosition, Context context, PayItemAdapter itemAdapter){
        this.mList = mList;
        this.selectedItem = selectedItem;
        this.parentPosition = parentPosition;
        this.context = context;
        this.itemAdapter = itemAdapter;
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

        // check if this item is the selected item
        if (selectedItem.parentIndex == parentPosition && selectedItem.nestedIndex == position) {
            int colorLightBlue = ContextCompat.getColor(context, R.color.lightblue);
            holder.itemView.setBackgroundColor(colorLightBlue);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class NestedViewHolder extends RecyclerView.ViewHolder{
        private TextView mTv;
        public NestedViewHolder(@NonNull View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.nestedItemTv);
        }
    }

}