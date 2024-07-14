package sg.edu.np.mad.nearbuy;

import com.google.gson.annotations.SerializedName;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FoursquareResponse {
    @SerializedName("results")
    private List<Place> results;

    public List<Place> getResults() {
        return results;
    }

    public class Place {
        @SerializedName("name")
        private String name;
        @SerializedName("geocodes")
        private Geocodes geocodes;

        public String getName() {
            return name;
        }

        public Geocodes getGeocodes() {
            return geocodes;
        }
    }

    public class Geocodes {
        @SerializedName("main")
        private Coordinates main;

        public Coordinates getMain() {
            return main;
        }
    }

    public class Coordinates {
        @SerializedName("latitude")
        private double latitude;
        @SerializedName("longitude")
        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }
}