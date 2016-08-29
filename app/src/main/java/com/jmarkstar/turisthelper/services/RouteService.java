package com.jmarkstar.turisthelper.services;

import com.jmarkstar.turisthelper.services.response.RouteResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/** Google Maps Directions API's client
 * Created by jmarkstar on 29/08/2016.
 */
public interface RouteService {

    /** Gets the a array of {@link com.google.android.gms.maps.model.LatLng} to create a {@link com.google.android.gms.maps.model.Polyline}
     * on google maps.
     * @param origin start location with the following format -33.88,151.20
     * @param destination end location with the following format -34.44,151.30
     * @param  sensor its a boolean
     * @param mode mode to get the directions example walking, driving,
     *
     * @return {@link RouteResponse} contatins a list of {@link com.google.android.gms.maps.model.LatLng}
     * */
    @GET("directions/json")
    Call<RouteResponse> getDirection( @Query("origin") String origin,
                                      @Query("destination") String destination,
                                      @Query("sensor") String sensor,
                                      @Query("mode") String mode);
}
