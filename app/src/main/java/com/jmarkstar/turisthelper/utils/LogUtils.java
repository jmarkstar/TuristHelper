package com.jmarkstar.turisthelper.utils;

import android.util.Log;
import com.crashlytics.android.Crashlytics;

/** Shows logs on logcat and sends the logs to fabric enviroment.
 * Created by jmarkstar on 26/08/2016.
 */
public class LogUtils {

    public static void info(String tag, String message){
        Crashlytics.log(Log.VERBOSE, tag, message);
    }

    public static void error(String tag, String message){
        Crashlytics.log(Log.ERROR, tag, message);
    }
}
