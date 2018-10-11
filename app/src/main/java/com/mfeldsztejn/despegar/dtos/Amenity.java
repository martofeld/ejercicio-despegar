package com.mfeldsztejn.despegar.dtos;

import java.io.Serializable;

public class Amenity implements Serializable {
    private String id;
    private String description;

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
