package sg.edu.np.mad.nearbuy;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoursquareService {
    @GET("places/search")
    Call<FoursquareResponse> searchPlaces(
            @Query("ll") String ll,
            @Query("query") String query,
            @Query("radius") int radius // Add this line
    );
}