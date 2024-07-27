package sg.edu.np.mad.nearbuy;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class PlacePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_place_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Getting the information of the places from the card the user clicked
        String name = getIntent().getStringExtra("name");
        String address = getIntent().getStringExtra("location");
        String extendedaddress = getIntent().getStringExtra("extendedaddress");
        int distance  =getIntent().getIntExtra("distance", 0);
        int image = getIntent().getIntExtra("img",  R.drawable.placeholder);

        //Identifying the text and images from the xml
        TextView placename = findViewById(R.id.name);
        TextView placeaddress = findViewById(R.id.address);
        TextView placeaddressextended = findViewById(R.id.addressextended);
        TextView placedistance = findViewById(R.id.distance);
        ImageView locationimage = findViewById(R.id.locationimage);

        //Showing the text and images
        placename.setText(name);
        placeaddress.setText(address);
        placeaddressextended.setText(extendedaddress);
        placedistance.setText("Distance: " + distance + "m");
        locationimage.setImageResource(image);


        //Creating back button to return to map
        ImageView backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}