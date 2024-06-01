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

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartViewHolder> {
    Context context;
    List<Product> data;

    public ShoppingCartAdapter(List<Product> input, Context context){
        this.context = context;
        data = input;
    }
    @NonNull
    @Override
    public ShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShoppingCartViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_activity_shopping_cart,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartViewHolder holder, int position) {
        Product product = data.get(position);
        holder.nameproduct.setText(product.getName());
        holder.imageproduct.setImageResource(product.getProductimg());
        holder.priceproduct.setText(product.getPrice());
        holder.totalpriceproduct.setText(Double.toString(product.getTotalprice()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

