package com.jmarkstar.turisthelper.models;

import com.google.gson.annotations.SerializedName;

/** Response for {@link com.jmarkstar.turisthelper.services.AccessTokenService}
 * Created by jmarkstar on 27/08/2016.
 */
public final class AccessToken {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("error")
    private String error;

    public String getAccessToken() {
        return accessToken;
    }

    public String getError() {
        return error;
    }
}
