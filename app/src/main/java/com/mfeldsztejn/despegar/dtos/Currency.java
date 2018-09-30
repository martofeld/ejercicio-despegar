package com.mfeldsztejn.despegar.dtos;

import java.io.Serializable;

public class Currency implements Serializable {
    private String id;
    private String mask;
    private double ratio;

    public String getMask() {
        return mask;
    }
}
