package com.mfeldsztejn.despegar.dtos.hotel;

import com.mfeldsztejn.despegar.dtos.Hotel;

import java.util.List;

public class HotelExpansion extends Hotel {
    private String description;
    private GeoLocation geoLocation;
    private Location city;
    private Location country;
    private Location administrativeDivision;
    private List<Review> reviews;

    public Location getCountry() {
        return country;
    }

    public Location getAdministrativeDivision() {
        return administrativeDivision;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public String getDescription() {
        return description;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public Location getCity() {
        return city;
    }
}
