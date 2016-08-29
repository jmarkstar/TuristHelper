package com.jmarkstar.turisthelper.helpers;

import com.jmarkstar.turisthelper.models.Session;
import io.realm.Realm;

/**
 * Created by jmarkstar on 26/08/2016.
 */
public class FoursquareHelper {

    public static boolean HasSession(){
        Realm realm = Realm.getDefaultInstance();
        Session session = realm.where(Session.class).findFirst();
        return session != null;
    }


}
