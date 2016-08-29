package com.jmarkstar.turisthelper.application;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.jmarkstar.turisthelper.utils.Constant;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/** Supports the configurations of the third-party libraries.
 * Created by jmarkstar on 26/08/2016.
 */
public class TuristHelperApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        settingFabric();
        settingRealm();
    }

    private void settingFabric(){
        Fabric.with(this, new Crashlytics());
    }

    private void settingRealm(){
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
            .name(Constant.DB_NAME)
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
