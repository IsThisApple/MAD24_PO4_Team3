package sg.edu.np.mad.nearbuy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VendorMessageAdapter extends RecyclerView.Adapter<VendorMessageAdapter.VendorMessageViewHolder> {
    private List<Message> messageList;

    public VendorMessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public VendorMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_vendor, parent, false);
        return new VendorMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VendorMessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class VendorMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMessage;
        private TextView textViewSender;

        public VendorMessageViewHolder(View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textmessage_vendor);
            textViewSender = itemView.findViewById(R.id.textsender_vendor);
        }

        public void bind(Message message) {
            textViewMessage.setText(message.getText());
            textViewSender.setText(message.getSender());
        }
    }
}