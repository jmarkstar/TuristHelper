package com.jmarkstar.turisthelper.models.deserializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jmarkstar.turisthelper.helpers.GoogleMapsHelper;
import com.jmarkstar.turisthelper.services.response.RouteResponse;
import com.jmarkstar.turisthelper.utils.LogUtils;
import java.lang.reflect.Type;

/**
 * Created by jmarkstar on 29/08/2016.
 */
public class RouteDeserializer implements JsonDeserializer<RouteResponse> {

    private static final String TAG = "RouteDeserializer";

    @Override
    public RouteResponse deserialize(JsonElement json, Type typeOfT,
                 JsonDeserializationContext context) throws JsonParseException {

        LogUtils.info(TAG, json.toString());

        JsonObject root = json.getAsJsonObject();
        JsonArray routesArray = root.getAsJsonArray("routes");
        // Grab the first route
        JsonObject route = routesArray.get(0).getAsJsonObject();
        JsonObject poly = route.getAsJsonObject("overview_polyline");
        String polyline = poly.get("points").getAsString();

        RouteResponse routeResponse = new RouteResponse();
        routeResponse.setRoutes(GoogleMapsHelper.decodePoly(polyline));
        return routeResponse;
    }
}
