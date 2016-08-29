package com.jmarkstar.turisthelper.models.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jmarkstar.turisthelper.models.Session;
import com.jmarkstar.turisthelper.utils.Constant;
import com.jmarkstar.turisthelper.utils.LogUtils;

import java.lang.reflect.Type;

/** Session's deserializer.
 * Created by jmarkstar on 27/08/2016.
 */
public class SessionDeserializer implements JsonDeserializer<Session> {

    private static final String TAG = "SessionDeserializer";

    @Override
    public Session deserialize(JsonElement json, Type typeOfT,
                               JsonDeserializationContext context) throws JsonParseException {

        LogUtils.info(TAG, json.toString());

        JsonObject root = json.getAsJsonObject();
        JsonObject response = root.getAsJsonObject("response");
        JsonObject userObject = response.getAsJsonObject("user");

        String id = userObject.get("id").getAsString();
        String firstName = userObject.get("firstName").getAsString();
        String lastName = userObject.get("lastName").getAsString();

        JsonObject photoObject = userObject.getAsJsonObject("photo");
        String prefix = photoObject.get("prefix").getAsString();
        String suffix = photoObject.get("suffix").getAsString();

        JsonObject contactObject = userObject.getAsJsonObject("contact");
        String email = contactObject.get("email").getAsString();

        Session user = new Session();
        user.setFullName(firstName+ Constant.SPACE+lastName);
        user.setFourSquareId(id);
        user.setPhoto(prefix+"100x100"+suffix);
        user.setEmail(email);

        return user;
    }
}
