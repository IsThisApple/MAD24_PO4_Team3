package sg.edu.np.mad.nearbuy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    Context context;
    List<Product> data;

    private PreferenceManager preferenceManager;
    private TextToSpeech tts;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300; // in milliseconds

    public ProductAdapter(List<Product> input, Context context, TextToSpeech tts){
        this.context = context;
        data = input;
        this.preferenceManager = new PreferenceManager(context);
        this.tts = tts;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_activity_main,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = data.get(position);
        holder.productname.setText(product.getName());
        holder.productprice.setText("$" + product.getPrice());
        holder.productimg.setImageResource(product.getProductimg());

        holder.productcard.setOnClickListener(new View.OnClickListener() {
            boolean isAccessibilityEnabled = preferenceManager.isAccessibilityEnabled();
            private long lastClickTime = 0;

            @Override
            public void onClick(View v) {
                long clickTime = System.currentTimeMillis();
                if (isAccessibilityEnabled) {
                    if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                        // double click
                        tts.speak(product.getName(), TextToSpeech.QUEUE_FLUSH, null, null);
                        Intent productpage = new Intent(context, ProductPage.class);
                        productpage.putExtra("name", product.getName());
                        productpage.putExtra("price", product.getPrice());
                        productpage.putExtra("img", product.getProductimg());
                        context.startActivity(productpage);
                    } else {
                        // single click
                        tts.speak(product.getName(), TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                    lastClickTime = clickTime;
                } else {
                    if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                        // double click
                        //
                    } else {
                        // single click
                        Intent productpage = new Intent(context, ProductPage.class);
                        productpage.putExtra("name", product.getName());
                        productpage.putExtra("price", product.getPrice());
                        productpage.putExtra("img", product.getProductimg());
                        context.startActivity(productpage);
                    }
                    lastClickTime = clickTime;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}