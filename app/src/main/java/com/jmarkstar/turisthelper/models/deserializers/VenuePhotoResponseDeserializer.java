package com.jmarkstar.turisthelper.models.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jmarkstar.turisthelper.models.VenuePhoto;
import com.jmarkstar.turisthelper.services.response.VenuePhotoResponse;
import com.jmarkstar.turisthelper.utils.LogUtils;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jmarkstar on 29/08/2016.
 */
public class VenuePhotoResponseDeserializer implements JsonDeserializer<VenuePhotoResponse> {

    private static final String TAG = "VenuePhotoResponseDeserializer";

    @Override
    public VenuePhotoResponse deserialize(JsonElement json, Type typeOfT,
                                          JsonDeserializationContext context) throws JsonParseException {

        LogUtils.info(TAG, json.toString());

        JsonObject root = json.getAsJsonObject();
        JsonObject response = root.getAsJsonObject("response");
        JsonObject photos = response.getAsJsonObject("photos");

        ArrayList<VenuePhoto> venuePhotos = null;

        final VenuePhoto[] venuesPhotoArray = context.deserialize(photos.get("items"), VenuePhoto[].class);
        if(venuesPhotoArray != null){
            venuePhotos = new ArrayList<>(Arrays.asList(venuesPhotoArray));
        }
        VenuePhotoResponse venueResponse = new VenuePhotoResponse();
        venueResponse.setVenuePhotos(venuePhotos);
        return venueResponse;
    }
}
