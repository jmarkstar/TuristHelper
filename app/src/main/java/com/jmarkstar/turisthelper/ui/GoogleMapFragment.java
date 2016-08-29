package com.jmarkstar.turisthelper.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jmarkstar.turisthelper.R;
import com.jmarkstar.turisthelper.helpers.RetrofitHelper;
import com.jmarkstar.turisthelper.models.Venue;
import com.jmarkstar.turisthelper.models.deserialize.RouteDeserializer;
import com.jmarkstar.turisthelper.services.RouteService;
import com.jmarkstar.turisthelper.services.VenueService;
import com.jmarkstar.turisthelper.services.response.RouteResponse;
import com.jmarkstar.turisthelper.services.response.VenueResponse;
import com.jmarkstar.turisthelper.utils.Constant;
import com.jmarkstar.turisthelper.utils.LogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jmarkstar on 29/08/2016.
 */
public class GoogleMapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = "MainActivity";

    protected MapView mMapView;

    private GoogleMap mMap;

    private Marker mSelectedPositionMarker;

    private Retrofit retrofit;
    private List<Polyline> polylineList;
    private HashMap<Marker, Venue> venueMarkerMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) view.findViewById(R.id.mv_map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);
        return view;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        getVenues();
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(googleMap != null){
            mMap = googleMap;
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMapClickListener(this);
            mMap.setOnMarkerClickListener(this);
            mMap.setMyLocationEnabled(true);
            mMap.setOnInfoWindowClickListener(this);
            getVenues();
        }else{
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(mSelectedPositionMarker == null){
            mSelectedPositionMarker = mMap.addMarker(new MarkerOptions().position(latLng));
        }else{
            mSelectedPositionMarker.setPosition(latLng);
        }
        createRoute(getMyCurrentLocation(), latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        createRoute(getMyCurrentLocation(), marker.getPosition());
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Venue venue = venueMarkerMap.get(marker);
        Intent intent = new Intent(getActivity(), DetailVenueActivity.class);
        intent.putExtra(DetailVenueActivity.SELECTED_VENUE, venue);
        getContext().startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /** get the directions of the goolge directions api and draw the lines on the map.
     * */
    private void createRoute(LatLng startLatLng, LatLng endLatLng){
        if(retrofit == null){
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(RouteResponse.class, new RouteDeserializer())
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.GOOGLEDIRECTION_API_BASE)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        String startLocation = startLatLng.latitude+Constant.COMMA+startLatLng.longitude;
        String endLocation = endLatLng.latitude+Constant.COMMA+endLatLng.longitude;

        RouteService routeService = retrofit.create(RouteService.class);
        Call<RouteResponse> request = routeService.getDirection(startLocation,
                endLocation,
                Constant.GOOGLEDIRECTION_SENSOR,
                Constant.GOOGLEDIRECTION_MODE );

        request.enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                List<LatLng> latlngList = response.body().getRoutes();
                drawRoute(latlngList);
            }

            @Override
            public void onFailure(Call<RouteResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Try again please", Toast.LENGTH_LONG).show();
            }
        });
    }

    /** draw the route on the map, if there is a created route,
     * It will be removed to create another.
     * */
    private void drawRoute(List<LatLng> latlngList){
        if(latlngList != null){
            if(polylineList != null){
                for(Polyline polyline : polylineList){
                    polyline.remove();
                }
            }
            polylineList = new ArrayList<Polyline>();

            for (int i = 0; i < latlngList.size() - 1; i++) {
                LatLng src = latlngList.get(i);
                LatLng dest = latlngList.get(i + 1);
                Polyline polyLine = mMap.addPolyline(
                        new PolylineOptions()
                                .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,dest.longitude))
                                .width(4)
                                .color(Color.BLUE)
                                .geodesic(true));
                polylineList.add(polyLine);
            }
        }
    }

    /** Get current location.
     * */
    private LatLng getMyCurrentLocation(){
        LocationManager service = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        Location location = service.getLastKnownLocation(provider);
        return new LatLng(location.getLatitude(),location.getLongitude());
    }

    /** Get venues ffrom foursquare api.
     * */
    private void getVenues(){
        LatLng latLng = getMyCurrentLocation();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom( latLng, 15.0f));

        VenueService venueService = RetrofitHelper.getRetrofitInstance().create(VenueService.class);

        String llParam = latLng.latitude+","+latLng.longitude;
        final Call<VenueResponse> request = venueService.getVenues(llParam, 20);
        LogUtils.info(TAG, "Url="+request.request().url().toString());
        request.enqueue(new Callback<VenueResponse>() {
            @Override
            public void onResponse(Call<VenueResponse> call, Response<VenueResponse> response) {
                ArrayList<Venue> venues = response.body().getVenues();
                if(venues != null){
                    if(venueMarkerMap != null){
                        venueMarkerMap.clear();
                    }
                    venueMarkerMap = new HashMap<Marker, Venue>();
                    for(Venue venue : venues){
                        drawVenueMarker(venue);
                    }
                }
            }

            @Override
            public void onFailure(Call<VenueResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Error getting the venues", Toast.LENGTH_LONG).show();
            }
        });
    }

    /** Draw a marker according the venue's location.
     * */
    private void drawVenueMarker(Venue venue){
        Double lat = Double.parseDouble(venue.getLatitude());
        Double lng = Double.parseDouble(venue.getLongitude());
        LatLng latLng = new LatLng(lat, lng);
        Marker venueMarker = mMap.addMarker(new MarkerOptions()
            .position(latLng)
            .title(venue.getName())
            .snippet(venue.getFormattedAddress())
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        venueMarkerMap.put(venueMarker, venue);
    }
}
