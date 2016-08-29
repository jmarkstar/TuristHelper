package com.jmarkstar.turisthelper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import com.jmarkstar.turisthelper.R;
import com.jmarkstar.turisthelper.helpers.FoursquareHelper;
import com.jmarkstar.turisthelper.utils.LogUtils;

/** shows the logo of x seconds.
 *  @author jmarkstar
 * */
public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LogUtils.info(TAG, "Starting");

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent;
                if(FoursquareHelper.HasSession()){
                    mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                }else{
                    mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
