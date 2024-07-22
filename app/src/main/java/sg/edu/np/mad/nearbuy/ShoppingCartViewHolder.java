package sg.edu.np.mad.nearbuy;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class ShoppingCartViewHolder extends RecyclerView.ViewHolder {

    ImageView imageproduct;
    TextView priceproduct;
    TextView nameproduct;
    TextView totalpriceproduct;
    TextView quantity;
    ImageView addition;
    ImageView subtraction;


    public ShoppingCartViewHolder(@NonNull View itemView) {
        super(itemView);
        imageproduct = itemView.findViewById(R.id.imageView);
        priceproduct = itemView.findViewById(R.id.priceproduct);
        nameproduct = itemView.findViewById(R.id.nameproduct);
        totalpriceproduct = itemView.findViewById(R.id.totalpriceproduct);
        quantity = itemView.findViewById(R.id.shoppingcartquantity);
        addition = itemView.findViewById(R.id.addition);
        subtraction = itemView.findViewById(R.id.subtraction);
    }
}