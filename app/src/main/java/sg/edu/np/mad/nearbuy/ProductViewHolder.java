package sg.edu.np.mad.nearbuy;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    ImageView productimg;
    TextView productname;
    TextView productprice;
    CardView productcard;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        productimg = itemView.findViewById(R.id.productimg);
        productname = itemView.findViewById(R.id.productname);
        productprice = itemView.findViewById(R.id.productprice);
        productcard = itemView.findViewById(R.id.productcard);
    }

}