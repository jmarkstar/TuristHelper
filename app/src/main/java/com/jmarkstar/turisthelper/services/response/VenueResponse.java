package com.jmarkstar.turisthelper.services.response;

import com.jmarkstar.turisthelper.models.Venue;

import java.util.ArrayList;

/**
 * Created by jmarkstar on 28/08/2016.
 */
public class VenueResponse {

    private ArrayList<Venue> venues;

    public ArrayList<Venue> getVenues() {
        return venues;
    }

    public void setVenues(ArrayList<Venue> venues) {
        this.venues = venues;
    }
}
