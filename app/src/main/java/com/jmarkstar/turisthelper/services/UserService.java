package com.jmarkstar.turisthelper.services;

import com.jmarkstar.turisthelper.models.Session;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jmarkstar on 27/08/2016.
 */
public interface UserService {

    @GET("users/self")
    Call<Session> getUserInformation();


}
