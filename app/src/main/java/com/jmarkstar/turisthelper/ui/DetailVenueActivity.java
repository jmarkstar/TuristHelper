package com.jmarkstar.turisthelper.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jmarkstar.turisthelper.R;
import com.jmarkstar.turisthelper.extensions.CustomImageView;
import com.jmarkstar.turisthelper.helpers.RetrofitHelper;
import com.jmarkstar.turisthelper.models.Venue;
import com.jmarkstar.turisthelper.services.VenueService;
import com.jmarkstar.turisthelper.services.response.VenuePhotoResponse;
import com.jmarkstar.turisthelper.utils.Constant;
import com.jmarkstar.turisthelper.utils.LogUtils;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailVenueActivity extends BaseActivity {

    private static final String TAG = "DetailVenueActivity";
    public static final String SELECTED_VENUE = "selected_venue";

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar)
    protected CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.tv_address)
    protected TextView tvAddress;

    @BindView(R.id.tv_distance)
    protected TextView tvDistance;

    @BindView(R.id.tv_url)
    protected TextView tvUrl;

    @BindView(R.id.im_venue_photo)
    protected CustomImageView imVenuePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_venue);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Venue selectedVenue = getIntent().getExtras().getParcelable(SELECTED_VENUE);
        if(selectedVenue != null){
            LogUtils.info(TAG, "selected="+selectedVenue.toString());
            collapsingToolbarLayout.setTitle(selectedVenue.getName());
            tvAddress.setText(selectedVenue.getFormattedAddress());
            String distance = selectedVenue.getDistance()+ Constant.SPACE+Constant.METERS;
            tvDistance.setText(distance);
            tvUrl.setText(selectedVenue.getUrl());
            loadVenuePhoto(selectedVenue.getId());
        }
    }

    private void loadVenuePhoto(String venueId){
        VenueService venueService = RetrofitHelper.getRetrofitInstance().create(VenueService.class);
        Call<VenuePhotoResponse> request = venueService.getPhotos(venueId);
        LogUtils.info(TAG, "Url="+request.request().url().toString());
        request.enqueue(new Callback<VenuePhotoResponse>() {
            @Override
            public void onResponse(Call<VenuePhotoResponse> call, Response<VenuePhotoResponse> response) {

                if(response.body().getVenuePhotos().size() > 0) {
                    Glide.with(getApplicationContext()).load(response.body().getVenuePhotos().get(0).getUrl())
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imVenuePhoto);
                }
            }

            @Override
            public void onFailure(Call<VenuePhotoResponse> call, Throwable t) {
                Toast.makeText(DetailVenueActivity.this, R.string.detail_venue_error_getting_photo, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
