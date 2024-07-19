package sg.edu.np.mad.nearbuy;

import com.google.gson.annotations.SerializedName;
import java.util.List;

import com.google.gson.Gson;

public class FoursquareResponse {
    private List<Place> results;

    public List<Place> getResults() {
        return results;
    }

    public void setResults(List<Place> results) {
        this.results = results;
    }

    public static class Place {
        private String fsq_id;
        private String name;
        private List<Category> categories;
        private List<Chain> chains;
        private String closed_bucket;
        private int distance;
        private Geocodes geocodes;
        private String link;
        private Location location;
        private String timezone;

        public String getFsq_id() {
            return fsq_id;
        }

        public void setFsq_id(String fsq_id) {
            this.fsq_id = fsq_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Category> getCategories() {
            return categories;
        }

        public void setCategories(List<Category> categories) {
            this.categories = categories;
        }

        public List<Chain> getChains() {
            return chains;
        }

        public void setChains(List<Chain> chains) {
            this.chains = chains;
        }

        public String getClosed_bucket() {
            return closed_bucket;
        }

        public void setClosed_bucket(String closed_bucket) {
            this.closed_bucket = closed_bucket;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public Geocodes getGeocodes() {
            return geocodes;
        }

        public void setGeocodes(Geocodes geocodes) {
            this.geocodes = geocodes;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }
    }

    public static class Geocodes {
        private DropOff drop_off;
        private Main main;
        private Roof roof;

        public DropOff getDrop_off() {
            return drop_off;
        }

        public void setDrop_off(DropOff drop_off) {
            this.drop_off = drop_off;
        }

        public Main getMain() {
            return main;
        }

        public void setMain(Main main) {
            this.main = main;
        }

        public Roof getRoof() {
            return roof;
        }

        public void setRoof(Roof roof) {
            this.roof = roof;
        }
    }

    public static class DropOff {
        private double latitude;
        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    public static class Main {
        private double latitude;
        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    public static class Roof {
        private double latitude;
        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    public static class Location {
        private String address;
        private String country;
        private String cross_street;
        private String formatted_address;
        private String locality;
        private String post_town;
        private String postcode;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCross_street() {
            return cross_street;
        }

        public void setCross_street(String cross_street) {
            this.cross_street = cross_street;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public String getLocality() {
            return locality;
        }

        public void setLocality(String locality) {
            this.locality = locality;
        }

        public String getPost_town() {
            return post_town;
        }

        public void setPost_town(String post_town) {
            this.post_town = post_town;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }
    }

    public static class Category {
        private int id;
        private String name;
        private String short_name;
        private String plural_name;
        private Icon icon;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getShort_name() {
            return short_name;
        }

        public void setShort_name(String short_name) {
            this.short_name = short_name;
        }

        public String getPlural_name() {
            return plural_name;
        }

        public void setPlural_name(String plural_name) {
            this.plural_name = plural_name;
        }

        public Icon getIcon() {
            return icon;
        }

        public void setIcon(Icon icon) {
            this.icon = icon;
        }

        public String getFullIconUrl(){
            if (icon != null){
                return icon.getPrefix() + "120" + icon.getSuffix();
            }
            return null;
        }
    }

    public static class Icon {
        private String prefix;
        private String suffix;

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }
    }

    public static class Chain {
        // define fields for chain if needed
    }

}