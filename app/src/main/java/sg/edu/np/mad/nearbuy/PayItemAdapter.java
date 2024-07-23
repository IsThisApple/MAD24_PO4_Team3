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

    private List<PayDataModel> mList;
    private PaySelectedItem selectedItem;
    private Context context;
    private DBCard dbCard;

    public PayItemAdapter(Context context, List<PayDataModel> mList, PaySelectedItem selectedItem) {
        this.context = context;
        this.mList = mList;
        this.selectedItem = selectedItem;
        this.dbCard = new DBCard(context); // Initialize DBCard instance here
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        PayDataModel model = mList.get(position);
        holder.mTextView.setText(model.getItemText());

        boolean isExpandable = model.isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        if (isExpandable) {
            holder.mArrowImage.setImageResource(R.drawable.baseline_arrow_down);
        } else {
            holder.mArrowImage.setImageResource(R.drawable.baseline_arrow_right);
        }

        PayNestedAdapter adapter = new PayNestedAdapter(model.getNestedList(), selectedItem, position, context, this, dbCard);
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.nestedRecyclerView.setHasFixedSize(true);
        holder.nestedRecyclerView.setAdapter(adapter);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setExpandable(!model.isExpandable());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private RelativeLayout expandableLayout;
        private TextView mTextView;
        private ImageView mArrowImage;
        private RecyclerView nestedRecyclerView;

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

/*
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

public class PayItemAdapter extends RecyclerView.Adapter<PayItemAdapter.ItemViewHolder> {
    private List<PayDataModel> mList;
    private PaySelectedItem selectedItem;
    private TextToSpeech tts;

    public PayItemAdapter(List<PayDataModel> mList, PaySelectedItem selectedItem, TextToSpeech tts) {
        this.mList = mList;
        this.selectedItem = selectedItem;
        this.tts = tts;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        PayDataModel model = mList.get(position);
        holder.mTextView.setText(model.getItemText());

        boolean isExpandable = model.isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        if (isExpandable) {
            holder.mArrowImage.setImageResource(R.drawable.baseline_arrow_down);
        } else {
            holder.mArrowImage.setImageResource(R.drawable.baseline_arrow_right);
        }

        Context context = holder.itemView.getContext();
        PayNestedAdapter adapter = new PayNestedAdapter(
                model.getNestedList(),
                selectedItem,
                position,
                context,
                this,
                tts
        );
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.nestedRecyclerView.setHasFixedSize(true);
        holder.nestedRecyclerView.setAdapter(adapter);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setExpandable(!model.isExpandable());
                notifyDataSetChanged();
            }
        });

        holder.paymentTitle.setOnTouchListener(new DoubleClickListener(
                v -> speakText(model.getItemText()), // single-click tts
                v -> {
                    speakText(model.getItemText());
                    model.setExpandable(!model.isExpandable());
                    notifyDataSetChanged();
                }  // double-click toggle expand/collapse
        ));
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

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private RelativeLayout expandableLayout;
        private TextView mTextView;
        private ImageView mArrowImage;
        private RecyclerView nestedRecyclerView;
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
*/