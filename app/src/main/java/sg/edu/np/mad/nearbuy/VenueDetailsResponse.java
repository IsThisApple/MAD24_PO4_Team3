package sg.edu.np.mad.nearbuy;

import java.util.List;

public class VenueDetailsResponse {
    private Venue venue;

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public static class Venue {
        private List<Photo> photos;

        public List<Photo> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photo> photos) {
            this.photos = photos;
        }
    }

    public static class Photo {
        private String prefix;
        private String suffix;

        public String getUrl() {
            return prefix + "120" + suffix; // adjust size as needed
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }
    }

}