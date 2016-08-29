package com.jmarkstar.turisthelper.services.response;

import com.jmarkstar.turisthelper.models.VenuePhoto;

import java.util.List;

/**
 * Created by jmarkstar on 29/08/2016.
 */
public class VenuePhotoResponse {

    private List<VenuePhoto> venuePhotos;

    public List<VenuePhoto> getVenuePhotos() {
        return venuePhotos;
    }

    public void setVenuePhotos(List<VenuePhoto> venuePhotos) {
        this.venuePhotos = venuePhotos;
    }
}
