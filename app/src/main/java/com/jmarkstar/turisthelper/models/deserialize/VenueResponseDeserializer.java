package com.jmarkstar.turisthelper.models.deserialize;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jmarkstar.turisthelper.models.Venue;
import com.jmarkstar.turisthelper.services.response.VenueResponse;
import com.jmarkstar.turisthelper.utils.LogUtils;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jmarkstar on 28/08/2016.
 */
public class VenueResponseDeserializer implements JsonDeserializer<VenueResponse> {

    private static final String TAG = "VenueResponseDeserializer";

    @Override
    public VenueResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        LogUtils.info(TAG, json.toString());

        JsonObject root = json.getAsJsonObject();
        JsonObject response = root.getAsJsonObject("response");
        JsonArray groups = response.getAsJsonArray("groups");
        ArrayList<Venue> venues = null;

        final Venue[] venuesArray = context.deserialize(groups.get(0).getAsJsonObject().get("items"), Venue[].class);
        if(venuesArray != null){
            venues = new ArrayList<>(Arrays.asList(venuesArray));
        }
        VenueResponse venueResponse = new VenueResponse();
        venueResponse.setVenues(venues);
        return venueResponse;
    }
}
