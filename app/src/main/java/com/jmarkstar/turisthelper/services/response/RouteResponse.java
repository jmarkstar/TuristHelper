package com.jmarkstar.turisthelper.services.response;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by jmarkstar on 29/08/2016.
 */
public class RouteResponse {

    private List<LatLng> routes;

    public List<LatLng> getRoutes() {
        return routes;
    }

    public void setRoutes(List<LatLng> routes) {
        this.routes = routes;
    }
}
