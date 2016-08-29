package com.jmarkstar.turisthelper.models.deserialize;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jmarkstar.turisthelper.models.VenuePhoto;
import com.jmarkstar.turisthelper.utils.LogUtils;
import java.lang.reflect.Type;

/**
 * Created by jmarkstar on 29/08/2016.
 */
public class VenuePhotoDeserializer implements JsonDeserializer<VenuePhoto> {

    private static final String TAG = "VenuePhotoDeserializer";

    @Override
    public VenuePhoto deserialize(JsonElement json, Type typeOfT,
                                  JsonDeserializationContext context) throws JsonParseException {

        LogUtils.info(TAG, json.toString());

        JsonObject Item = json.getAsJsonObject();
        String prefix = Item.get("prefix").getAsString();
        String suffix = Item.get("suffix").getAsString();

        String url = prefix+"500x500"+suffix;

        VenuePhoto photo = new VenuePhoto();
        photo.setUrl(url);
        return photo;
    }
}
