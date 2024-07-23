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

    private PreferenceManager preferenceManager;

    public ShoppingCartAdapter(List<Product> input, Context context, ShoppingCart shoppingCart) {
        this.context = context;
        data = input;
        this.shoppingCart = shoppingCart;
        this.preferenceManager = new PreferenceManager(context);
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

        boolean isAccessibilityEnabled = preferenceManager.isAccessibilityEnabled();

        // handle button for in-cart add
        if (isAccessibilityEnabled) {
            holder.addition.setOnTouchListener(new DoubleClickListener(
                    v -> shoppingCart.speak("Add quantity of " + product.getName()), // single-click action
                    v -> {
                        shoppingCart.speak("Add quantity of " + product.getName());
                        ShoppingCartDbHandler dbHandler = new ShoppingCartDbHandler(context, null, null, 1);
                        product.addquantity();
                        dbHandler.addSingleQuantity(product, product.getName());
                        holder.quantity.setText(Integer.toString(product.getQuantity()));
                        holder.totalpriceproduct.setText("Total Price: $" + String.format("%.2f", product.getTotalprice()));
                        shoppingCart.refreshProductList();
                    } // double-click action
            ));
        } else {
            holder.addition.setOnTouchListener(new DoubleClickListener(
                    v -> {
                        ShoppingCartDbHandler dbHandler = new ShoppingCartDbHandler(context, null, null, 1);
                        product.addquantity();
                        dbHandler.addSingleQuantity(product, product.getName());
                        holder.quantity.setText(Integer.toString(product.getQuantity()));
                        holder.totalpriceproduct.setText("Total Price: $" + String.format("%.2f", product.getTotalprice()));
                        shoppingCart.refreshProductList();
                    }, // single-click action
                    v -> {} // double-click action
            ));
        }

        // handle button for in-cart subtract
        if (isAccessibilityEnabled) {
            holder.subtraction.setOnTouchListener(new DoubleClickListener(
                    v -> shoppingCart.speak("Subtract quantity of " + product.getName()), // single-click action
                    v -> {
                        shoppingCart.speak("Subtract quantity of " + product.getName());
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
                        shoppingCart.refreshProductList();
                    } // double-click action
            ));
        } else {
            holder.subtraction.setOnTouchListener(new DoubleClickListener(
                    v -> {
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
                        shoppingCart.refreshProductList();
                    }, // single-click action
                    v -> {} // double-click action
            ));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(List<Product> newData) {
        data = newData;
        notifyDataSetChanged();
    }

}