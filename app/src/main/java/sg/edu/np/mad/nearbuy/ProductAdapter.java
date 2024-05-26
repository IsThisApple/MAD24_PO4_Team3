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

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    Context context;
    List<Product> data;

    public ProductAdapter(List<Product> input, Context context){
        this.context = context;
        data = input;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_activity_main,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product products = data.get(position);
        holder.productname.setText(products.getName());
        holder.productprice.setText(products.getPrice());
        holder.productimg.setImageResource(products.getProductimg());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}