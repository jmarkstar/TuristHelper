package com.jmarkstar.turisthelper.services;

import com.jmarkstar.turisthelper.services.response.VenuePhotoResponse;
import com.jmarkstar.turisthelper.services.response.VenueResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jmarkstar on 27/08/2016.
 */
public interface VenueService {

    @GET("venues/explore")
    Call<VenueResponse> getVenues(@Query("ll") String latlong,
                                  @Query("limit") Integer limit );

    @GET("venues/{venueid}/photos")
    Call<VenuePhotoResponse> getPhotos(@Path("venueid") String venueId);
}
