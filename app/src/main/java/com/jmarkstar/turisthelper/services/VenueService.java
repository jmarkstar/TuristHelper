package com.jmarkstar.turisthelper.services;

import com.jmarkstar.turisthelper.services.response.VenuePhotoResponse;
import com.jmarkstar.turisthelper.services.response.VenueResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/** Foursquare API's Client to get venue information.
 * documentation of the api https://developer.foursquare.com/docs/
 * Created by jmarkstar on 27/08/2016.
 */
public interface VenueService {

    /** Gets a array of venues.
     * @param latlong required unless near is provided. Latitude and longitude of the user's location.
     * @param limit Number of results to return, up to 50.
     *
     * @return {@link VenueResponse} that contains a {@link java.util.ArrayList<com.jmarkstar.turisthelper.models.Venue>}
     * */
    @GET("venues/explore")
    Call<VenueResponse> getVenues(@Query("ll") String latlong,
                                  @Query("limit") Integer limit );

    /** Gets a array of photos according to the venue id.
     * @param venueId The venue you want photos for.
     * @return {@link VenuePhotoResponse} that contains a {@link java.util.ArrayList<com.jmarkstar.turisthelper.models.VenuePhoto>}
     * */
    @GET("venues/{venueid}/photos")
    Call<VenuePhotoResponse> getPhotos(@Path("venueid") String venueId);
}
