package sg.edu.np.mad.nearbuy;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Path;

public interface FoursquareService {
    //Query to search for places
    @GET("places/search")
    Call<FoursquareResponse> searchPlaces(
            @Query("ll") String ll,
            @Query("query") String query,
            @Query("radius") int radius,
            @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret,
            @Query("v") String version
    );

}