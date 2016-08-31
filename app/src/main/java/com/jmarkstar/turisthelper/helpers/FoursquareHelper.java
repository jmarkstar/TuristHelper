package com.jmarkstar.turisthelper.helpers;

import android.content.Context;
import android.content.res.AssetManager;
import com.jmarkstar.turisthelper.models.Session;
import com.jmarkstar.turisthelper.utils.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import io.realm.Realm;

/**
 * Created by jmarkstar on 26/08/2016.
 */
public class FoursquareHelper {

    public enum Property {
        CLIENT_ID, CLIENT_SECRET
    }

    public static boolean HasSession(){
        Realm realm = Realm.getDefaultInstance();
        Session session = realm.where(Session.class).findFirst();
        return session != null;
    }

    public static String getProperty(Property property,Context context) {
        Properties properties = new Properties();
        try{
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(Constant.FOURSQUARE_PROPERTIES);
            properties.load(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
        return properties.getProperty(property.name());

    }



}
