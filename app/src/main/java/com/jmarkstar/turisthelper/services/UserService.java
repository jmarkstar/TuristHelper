package com.jmarkstar.turisthelper.services;

import com.jmarkstar.turisthelper.models.Session;
import retrofit2.Call;
import retrofit2.http.GET;

/** Foursquare API's Client to get user information.
 * Created by jmarkstar on 27/08/2016.
 */
public interface UserService {

    /** Gets the information of the token's owner.
     * @return {@link Session}
     * */
    @GET("users/self")
    Call<Session> getUserInformation();


}
