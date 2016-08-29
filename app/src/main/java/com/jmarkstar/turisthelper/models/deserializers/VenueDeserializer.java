package com.jmarkstar.turisthelper.models.deserializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jmarkstar.turisthelper.models.Venue;
import com.jmarkstar.turisthelper.utils.Constant;
import com.jmarkstar.turisthelper.utils.LogUtils;
import java.lang.reflect.Type;

/** gson deserializer for venues.
 * Created by jmarkstar on 28/08/2016.
 */
public class VenueDeserializer implements JsonDeserializer<Venue> {

    private static final String TAG = "VenueDeserializer";

    @Override
    public Venue deserialize(JsonElement json, Type typeOfT,
                             JsonDeserializationContext context) throws JsonParseException {

        LogUtils.info(TAG, json.toString());

        JsonObject Item = json.getAsJsonObject();
        JsonObject venue = Item.getAsJsonObject("venue");
        JsonObject location = venue.getAsJsonObject("location");
        JsonArray formattedAddress = location.getAsJsonArray("formattedAddress");

        String id = venue.get("id").getAsString();
        String name = venue.get("name").getAsString();
        String lat = location.get("lat").getAsString();
        String lng = location.get("lng").getAsString();
        String distance = location.get("distance").getAsString();

        StringBuilder formattedAddressResult = new StringBuilder();
        for(JsonElement element: formattedAddress){
            formattedAddressResult.append(element.getAsString());
            formattedAddressResult.append(Constant.SPACE);
        }

        String rating = venue.get("rating").getAsString();
        String url = (venue.get("url") == null)?Constant.EMPTY : venue.get("url").getAsString();

        Venue venueResponse = new Venue();
        venueResponse.setId(id);
        venueResponse.setName(name);
        venueResponse.setLatitude(lat);
        venueResponse.setLongitude(lng);
        venueResponse.setFormattedAddress(formattedAddressResult.toString().trim());
        venueResponse.setRating(rating);
        venueResponse.setUrl(url);
        venueResponse.setDistance(distance);

        return venueResponse;
    }
}
