package com.jmarkstar.turisthelper.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jmarkstar.turisthelper.R;
import com.jmarkstar.turisthelper.models.Session;
import com.jmarkstar.turisthelper.utils.LogUtils;
import butterknife.BindView;
import io.realm.Realm;
/**
 * @author jmarkstar
 * */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity";

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation_view)
    protected NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initNavigationView();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass;
        switch(item.getItemId()) {
            case R.id.drawer_map:
                fragmentClass = GoogleMapFragment.class;
                break;
            default:
                fragmentClass = GoogleMapFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentContent, fragment).commit();
        item.setChecked(true);

        setTitle(item.getTitle());
        mDrawerLayout.closeDrawers();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name));
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initNavigationView(){
        mNavigationView.setNavigationItemSelectedListener(this);

        onNavigationItemSelected(mNavigationView.getMenu().getItem(0));

        realm = Realm.getDefaultInstance();
        Session session = realm.where(Session.class).findFirst();
        if(session != null){
            LogUtils.info(TAG, session.toString());
            final ImageView imAvatar = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.im_avatar);
            final TextView tvUsername = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tv_username);
            final TextView tvEmail = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tv_email);

            tvUsername.setText(session.getFullName());
            tvEmail.setText(session.getEmail());
            Glide.with(getApplicationContext()).load(session.getPhoto()).asBitmap().centerCrop()
                    .into(new BitmapImageViewTarget(imAvatar) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imAvatar.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
    }
}
