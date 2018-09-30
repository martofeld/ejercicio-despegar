package com.mfeldsztejn.despegar.dtos;

import java.io.Serializable;
import java.util.List;

public class Hotel implements Serializable {
    private String id;
    private int stars;
    private String name;
    private String address;
    private String mainPicture;
    private float rating;
    private List<Amenity> amenities;
    private Price price;

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public String getMainPicture() {
        return mainPicture;
    }

    public int getStars() {
        return stars;
    }

    public String getAddress() {
        return address;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public String getId() {
        return id;
    }
}
