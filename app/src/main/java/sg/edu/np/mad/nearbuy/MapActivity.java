package sg.edu.np.mad.nearbuy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap myMap;
    Location currentlocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    private static final String CLIENT_ID = "QHTCMWLTLHVTMMIWYLG52ZFHZLC3F2ZDLDUQO4YVCMWPZTC4";
    private static final String CLIENT_SECRET = "DRF1GTV03UDSHXC5UJOLULB1O5ZMSRPMTKHJHRT5VYGD4K33";
    private static final String VERSION = "20230712";

    private PlacesClient placesClient;
    private RecyclerView recyclerView;
    private PlacesAdapter placesAdapter;
    private List<FoursquareResponse.Place> placesList = new ArrayList<>();
    private FoursquareService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView findsupermarket = findViewById(R.id.findsupermarket);
        findsupermarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findSupermarketsNearby();
            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FoursquareService service = FoursquareClient.getClient().create(FoursquareService.class);
        placesAdapter = new PlacesAdapter(this, placesList, service);
        recyclerView.setAdapter(placesAdapter);

        //Navigation Panel
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_map);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;

            } else if (itemId == R.id.bottom_map) {
                Toast.makeText(this, "It is on the page already", Toast.LENGTH_SHORT).show();
                return true;

            } else if (itemId == R.id.bottom_chat) {
                startActivity(new Intent(getApplicationContext(), MessageActivity.class));
                finish();
                return true;

            } else if (itemId == R.id.bottom_cart) {
                startActivity(new Intent(getApplicationContext(), ShoppingCart.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentlocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapActivity.this);
                }
            }
        });
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
        }

        LatLng currentLatLng = new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude());
        myMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        myMap.animateCamera(CameraUpdateFactory.zoomBy(12));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else{
                Toast.makeText(this, "Location is denied, please allow permission to use map", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void findSupermarketsNearby() {
        if (currentlocation == null) {
            Toast.makeText(this, "Current location not available", Toast.LENGTH_SHORT).show();
            return;
        }

        String latLng = currentlocation.getLatitude() + "," + currentlocation.getLongitude();
        int radius = 1000; // Set the radius to 1000 meters (1 km)

        FoursquareService service = FoursquareClient.getClient().create(FoursquareService.class);
        Call<FoursquareResponse> call = service.searchPlaces(latLng, "supermarket", radius,CLIENT_ID, CLIENT_SECRET, VERSION); // Include the radius

        call.enqueue(new Callback<FoursquareResponse>() {
            @Override
            public void onResponse(Call<FoursquareResponse> call, Response<FoursquareResponse> response) {
                if (response.isSuccessful()) {
                    FoursquareResponse responseBody = response.body();
                    if (responseBody != null) {
                        runOnUiThread(() -> {
                            List<FoursquareResponse.Place> places = responseBody.getResults();
                            myMap.clear(); // Clear existing markers
                            placesList.clear();
                            for (FoursquareResponse.Place place : places) {
                                LatLng placeLatLng = new LatLng(place.getGeocodes().getMain().getLatitude(), place.getGeocodes().getMain().getLongitude());
                                myMap.addMarker(new MarkerOptions().position(placeLatLng).title(place.getName()));
                                placesList.add(place);
                            }
                            placesAdapter.notifyDataSetChanged();
                            recyclerView.setVisibility(View.VISIBLE);
                        });
                    }
                } else {
                    try {
                        String error = response.errorBody().string();
                        Toast.makeText(MapActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MapActivity.this, "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FoursquareResponse> call, Throwable t) {
                Toast.makeText(MapActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    //Code used for previous map api

    /*
    public void onShowSupermarketsClicked(View view) {
        if (currentlocation != null) {
            double latitude = currentlocation.getLatitude();
            double longitude = currentlocation.getLongitude();
            int radius = 1000; // Radius in meters (adjust as needed)

            // Execute Overpass API query task
            new OverpassAPIQueryTask(latitude, longitude, radius, new OverpassAPIQueryTask.OverpassAPIQueryListener() {
                @Override
                public void onSupermarketFound(double latitude, double longitude) {
                    // Add marker for each supermarket found
                    LatLng supermarketLatLng = new LatLng(latitude, longitude);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(supermarketLatLng)
                            .title("Supermarket");
                    myMap.addMarker(markerOptions);
                }
            }).execute();
        } else {
            Toast.makeText(this, "Current location not available", Toast.LENGTH_SHORT).show();
        }
    }

    private static class OverpassAPIQueryTask extends AsyncTask<Void, Void, String> {

        private double latitude;
        private double longitude;
        private int radius;
        private OverpassAPIQueryListener listener;

        public OverpassAPIQueryTask(double latitude, double longitude, int radius, OverpassAPIQueryListener listener) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.radius = radius;
            this.listener = listener;
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonString = null;

            try {
                // Construct the Overpass API query URL
                String query = buildOverpassQuery();
                URL url = new URL(query);

                // Establish connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                if (builder.length() == 0) {
                    return null;
                }
                jsonString = builder.toString();
            } catch (IOException e) {
                Log.e("OverpassAPIQueryTask", "Error ", e);
                return null;
            } finally {
                // Clean up resources
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("OverpassAPIQueryTask", "Error closing stream", e);
                    }
                }
            }
            return jsonString;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            if (jsonString != null) {
                try {
                    // Parse JSON response
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray elements = jsonObject.getJSONArray("elements");

                    // Process each element (supermarket)
                    for (int i = 0; i < elements.length(); i++) {
                        JSONObject element = elements.getJSONObject(i);
                        double lat = element.getDouble("lat");
                        double lon = element.getDouble("lon");
                        // Callback to listener with supermarket location
                        listener.onSupermarketFound(lat, lon);
                    }
                } catch (JSONException e) {
                    Log.e("OverpassAPIQueryTask", "Error parsing JSON", e);
                }
            } else {
                Log.e("OverpassAPIQueryTask", "Empty response");
            }
        }

        private String buildOverpassQuery() {
            // Construct Overpass API query with parameters
            return "https://overpass-api.de/api/interpreter?data=" +
                    "[out:json];" +
                    "node[\"shop\"=\"supermarket\"](around:" + radius + "," + latitude + "," + longitude + ");" +
                    "out body;" +
                    ">;" +
                    "out skel qt;";
        }

        // Interface for callback
        public interface OverpassAPIQueryListener {
            void onSupermarketFound(double latitude, double longitude);
        }
    }

     */

}