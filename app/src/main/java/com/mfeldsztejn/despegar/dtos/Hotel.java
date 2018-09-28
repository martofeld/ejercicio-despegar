package com.mfeldsztejn.despegar.dtos;

import java.util.List;

public class Hotel {
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
}
