package sg.edu.np.mad.nearbuy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {
    private List<FoursquareResponse.Place> placesList;
    private FoursquareService service;
    private Context context;

    private static final String CLIENT_ID = "QHTCMWLTLHVTMMIWYLG52ZFHZLC3F2ZDLDUQO4YVCMWPZTC4";
    private static final String CLIENT_SECRET = "DRF1GTV03UDSHXC5UJOLULB1O5ZMSRPMTKHJHRT5VYGD4K33";
    private static final String VERSION = "20230712";

    public PlacesAdapter(Context context, List<FoursquareResponse.Place> placesList, FoursquareService service) {
        this.context = context;
        this.placesList = placesList;
        this.service = service;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        FoursquareResponse.Place place = placesList.get(position);
        holder.placeName.setText(place.getName());
        holder.placeAddress.setText(place.getLocation().getAddress());
        loadVenueImage(place, holder.placeImage);
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    private void loadVenueImage(FoursquareResponse.Place place, ImageView imageView) {
        List<FoursquareResponse.Category> categoryList = place.getCategories();
        FoursquareResponse.Category category = categoryList.get(0);
        String imageUrl = category.getFullIconUrl();
        Log.d("PlacesAdapter", "imageURL: " +  imageUrl);
        if (imageUrl != null){
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(imageView);
            // imageView.setVisibility(View.VISIBLE);
        }
        else{
            Log.d("PlacesAdapter", "No photos found for venueID: " + place.getFsq_id());
            imageView.setImageResource(R.drawable.placeholder);

        }
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView placeName;
        TextView placeAddress;
        ImageView placeImage;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.placeName);
            placeAddress = itemView.findViewById(R.id.placeAddress);
            placeImage = itemView.findViewById(R.id.placeImage);
        }
    }

}