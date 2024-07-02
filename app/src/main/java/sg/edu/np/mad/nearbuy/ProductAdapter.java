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
        Product product = data.get(position);
        holder.productname.setText(product.getName());
        holder.productprice.setText("$" + product.getPrice());
        holder.productimg.setImageResource(product.getProductimg());

        holder.productcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productpage = new Intent(context, ProductPage.class);
                productpage.putExtra("name", product.getName());
                productpage.putExtra("price", product.getPrice());
                productpage.putExtra("img", product.getProductimg());
                context.startActivity(productpage);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}