package com.mfeldsztejn.despegar.dtos;

public class Price {
    private Currency currency;
    private boolean finalPrice;
    private int base;
    private int best;

    public int getBase() {
        return base;
    }

    public int getBest() {
        return best;
    }

    public Currency getCurrency() {
        return currency;
    }
}
