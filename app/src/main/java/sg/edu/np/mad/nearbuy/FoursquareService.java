package sg.edu.np.mad.nearbuy;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Path;

public interface FoursquareService {
    @GET("places/search")
    Call<FoursquareResponse> searchPlaces(
            @Query("ll") String ll,
            @Query("query") String query,
            @Query("radius") int radius,
            @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret,
            @Query("v") String version
    );

    @GET("venues/{venueId}")
    Call<VenueDetailsResponse> getVenueDetails(
            @Path("venueId") String venueId,
            @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret,
            @Query("v") String version
    );

}