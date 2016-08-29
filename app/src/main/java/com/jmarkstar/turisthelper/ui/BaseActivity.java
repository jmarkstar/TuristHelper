package com.jmarkstar.turisthelper.ui;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import io.realm.Realm;

/** contains some usual configurations.
 * Created by jmarkstar on 26/08/2016.
 */
public class BaseActivity extends AppCompatActivity {

    protected Realm realm;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        realm = Realm.getDefaultInstance();
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(realm != null){
            realm.close();
        }
    }
}
