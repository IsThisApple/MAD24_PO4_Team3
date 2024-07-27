package sg.edu.np.mad.nearbuy;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

//Adapter for place recyclerview
public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {
    private List<FoursquareResponse.Place> placesList;
    private List<Pair<String, Integer>> nameToImageList;
    private FoursquareService service;
    private Context context;

    //List of placeholder images
    private int[] placeholderImages = {
            R.drawable.placeholder2,
            R.drawable.placeholder4,
            R.drawable.placeholder5,
            R.drawable.placeholder6,
            R.drawable.placeholder3,
            R.drawable.placeholder8
    };

    //List of specific supermarkets and their specific images
    private void initializeNameToImageList() {
        nameToImageList = new ArrayList<>();
        nameToImageList.add(new Pair<>("Fairprice", R.drawable.fairprice));
        nameToImageList.add(new Pair<>("U Stars",R.drawable.ustar));
        nameToImageList.add(new Pair<>("Cold Storage", R.drawable.coldstorage));
        nameToImageList.add(new Pair<>("Buzz", R.drawable.placeholder7));
        nameToImageList.add(new Pair<>("Giant", R.drawable.giant));
        nameToImageList.add(new Pair<>("Donki",R.drawable.donki));
        // Add more mappings as needed
    }

    public PlacesAdapter(Context context, List<FoursquareResponse.Place> placesList, FoursquareService service) {
        this.context = context;
        this.placesList = placesList;
        this.service = service;
        initializeNameToImageList();
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
        //Log.d("To see JSON", place.getFsq_id());
        //Setting the details of each place
        holder.placeName.setText(place.getName());
        holder.placeAddress.setText(place.getLocation().getAddress());
        //Image/Icon the API gives
        loadVenueImage(place, holder.placeImage);

        //Placeholder Images for places if API gives icon
        int index = position % placeholderImages.length;
        int placeholderImage = placeholderImages[index];
        Integer imageResource = null;

        //Checking for known places for their specific ages
        for (Pair<String, Integer> entry : nameToImageList) {
            if (place.getName().contains(entry.first)) {
                imageResource = entry.second;
                //Log.d("Checking imageResource", Integer.toString(imageResource));
                break;
            }
        }
        if (imageResource != null){
            holder.placeholdersImage.setImageResource(imageResource);
            placeholderImage = imageResource;
        } else {
            holder.placeholdersImage.setImageResource(placeholderImage);
        }
        int finalPlaceholderImage = placeholderImage;

        //Button to see specific details on the places
        holder.placecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent placepage = new Intent(context, PlacePage.class);
                placepage.putExtra("name", place.getName());
                placepage.putExtra("location", place.getLocation().getFormatted_address());
                placepage.putExtra("img", finalPlaceholderImage);
                placepage.putExtra("extendedaddress", place.getLocation().getAddress_extended());
                placepage.putExtra("distance", place.getDistance());
                context.startActivity(placepage);

            }
        });
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }
    //loading images from api
    private void loadVenueImage(FoursquareResponse.Place place, ImageView imageView) {
        List<FoursquareResponse.Category> categoryList = place.getCategories();
        FoursquareResponse.Category category = categoryList.get(0);
        String imageUrl = category.getFullIconUrl();
        Log.d("PlacesAdapter", "imageURL: " +  imageUrl);
        if (imageUrl != null){
            Picasso.get()
                    //.load(imageUrl)
                    .load("https://ss3.4sqi.net/img/categories_v2/shops/food_grocery_120.png")
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(imageView);

        }
        else{
            Log.d("PlacesAdapter", "No photos found for venueID: " + place.getFsq_id());
            imageView.setImageResource(R.drawable.placeholder);

        }
    }

    //Viewholder for places recyclerview
    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView placeName;
        TextView placeAddress;
        ImageView placeImage;
        ImageView placeholdersImage;

        CardView placecard;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.placeName);
            placeAddress = itemView.findViewById(R.id.placeAddress);
            placeImage = itemView.findViewById(R.id.placeImage);
            placeholdersImage = itemView.findViewById(R.id.placeholdersImage);
            placecard = itemView.findViewById(R.id.placecard);
        }
    }

}