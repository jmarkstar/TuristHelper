package com.jmarkstar.turisthelper.services;

import com.jmarkstar.turisthelper.services.response.RouteResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jmarkstar on 29/08/2016.
 */
public interface RouteService {

    @GET("directions/json")
    Call<RouteResponse> getDirection( @Query("origin") String origin,
                                      @Query("destination") String destination,
                                      @Query("sensor") String sensor,
                                      @Query("mode") String mode);
}
