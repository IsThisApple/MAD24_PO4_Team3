package sg.edu.np.mad.nearbuy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartViewHolder> {
    Context context;
    List<Product> data;
    ShoppingCart shoppingCart;

    public ShoppingCartAdapter(List<Product> input, Context context, ShoppingCart shoppingCart) {
        this.context = context;
        data = input;
        this.shoppingCart = shoppingCart;
    }

    @NonNull
    @Override
    public ShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShoppingCartViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_activity_shopping_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartViewHolder holder, int position) {
        Product product = data.get(position);
        holder.nameproduct.setText(product.getName());
        holder.imageproduct.setImageResource(product.getProductimg());
        holder.priceproduct.setText("Base Price: $" + product.getPrice());
        holder.totalpriceproduct.setText("Total Price: $" + String.format("%.2f", product.getTotalprice()));
        holder.quantity.setText(Integer.toString(product.getQuantity()));

        //Button to add in shoppingcart
        holder.addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCartDbHandler dbHandler = new ShoppingCartDbHandler(context, null, null, 1);
                product.addquantity();
                dbHandler.addSingleQuantity(product, product.getName());
                holder.quantity.setText(Integer.toString(product.getQuantity()));
                holder.totalpriceproduct.setText("Total Price: $" + String.format("%.2f", product.getTotalprice()));
                shoppingCart.calculateTotalPrice();
            }
        });

        //button to subtract in shoppingcart
        holder.subtraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCartDbHandler dbHandler = new ShoppingCartDbHandler(context, null, null, 1);
                if (product.getQuantity() == 1) {
                    dbHandler.deleteProduct(product.getName());
                    data = dbHandler.getAllProducts();
                    notifyDataSetChanged();
                } else {
                    product.subtractquantity();
                    dbHandler.subtractSingleQuantity(product, product.getName());
                    holder.quantity.setText(Integer.toString(product.getQuantity()));
                    holder.totalpriceproduct.setText("Total Price: $" + String.format("%.2f", product.getTotalprice()));
                }
                shoppingCart.calculateTotalPrice();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
