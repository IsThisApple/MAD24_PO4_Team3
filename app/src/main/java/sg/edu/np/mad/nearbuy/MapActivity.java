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

import android.speech.tts.TextToSpeech;
import java.util.Locale;

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
    private PreferenceManager preferenceManager;
    private TextToSpeech tts;

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

        preferenceManager = new PreferenceManager(this);

        ImageView findsupermarket = findViewById(R.id.findsupermarket);
        findsupermarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findSupermarketsNearby();
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        // recyclerview setup
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FoursquareService service = FoursquareClient.getClient().create(FoursquareService.class);
        placesAdapter = new PlacesAdapter(this, placesList, service);
        recyclerView.setAdapter(placesAdapter);

        // initialise tts
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        initializeNavigationBar();
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
                if (location != null) {
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

        LatLng currentLatLng = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
        myMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        myMap.animateCamera(CameraUpdateFactory.zoomBy(12));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
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
        int radius = 1000; // set the radius to 1000 meters (1 km)

        FoursquareService service = FoursquareClient.getClient().create(FoursquareService.class);
        Call<FoursquareResponse> call = service.searchPlaces(latLng, "supermarket", radius, CLIENT_ID, CLIENT_SECRET, VERSION); // Include the radius

        call.enqueue(new Callback<FoursquareResponse>() {
            @Override
            public void onResponse(Call<FoursquareResponse> call, Response<FoursquareResponse> response) {
                if (response.isSuccessful()) {
                    FoursquareResponse responseBody = response.body();
                    if (responseBody != null) {
                        runOnUiThread(() -> {
                            List<FoursquareResponse.Place> places = responseBody.getResults();
                            myMap.clear(); // clear existing markers
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

    private void initializeNavigationBar() {
        boolean isAccessibilityEnabled = preferenceManager.isAccessibilityEnabled();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_map);

        // create listeners for each menu item

        View.OnClickListener homeSingleClickListener;
        View.OnClickListener homeDoubleClickListener = null;

        View.OnClickListener mapSingleClickListener;
        View.OnClickListener mapDoubleClickListener = null;

        View.OnClickListener chatSingleClickListener;
        View.OnClickListener chatDoubleClickListener = null;

        View.OnClickListener cartSingleClickListener;
        View.OnClickListener cartDoubleClickListener = null;

        if (isAccessibilityEnabled) {
            homeSingleClickListener = v -> speak("Open home");
            homeDoubleClickListener = v -> {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            };
        } else {
            homeSingleClickListener = v -> {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            };
        }

        if (isAccessibilityEnabled) {
            mapSingleClickListener = v -> speak("Already in map");
            mapDoubleClickListener = v -> Toast.makeText(this, "It is on the page already", Toast.LENGTH_SHORT).show();
        } else {
            mapSingleClickListener = v -> Toast.makeText(this, "It is on the page already", Toast.LENGTH_SHORT).show();
        }



        if (isAccessibilityEnabled) {
            cartSingleClickListener = v -> speak("Open cart");
            cartDoubleClickListener = v -> {
                startActivity(new Intent(getApplicationContext(), ShoppingCart.class));
                finish();
            };
        } else {
            cartSingleClickListener = v -> {
                startActivity(new Intent(getApplicationContext(), ShoppingCart.class));
                finish();
            };
        }

        // set listeners
        bottomNavigationView.findViewById(R.id.bottom_home).setOnTouchListener(new DoubleClickListener(homeSingleClickListener, homeDoubleClickListener));
        bottomNavigationView.findViewById(R.id.bottom_map).setOnTouchListener(new DoubleClickListener(mapSingleClickListener, mapDoubleClickListener));
        bottomNavigationView.findViewById(R.id.bottom_cart).setOnTouchListener(new DoubleClickListener(cartSingleClickListener, cartDoubleClickListener));
    }

    // method to speak text using tts
    private void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

}