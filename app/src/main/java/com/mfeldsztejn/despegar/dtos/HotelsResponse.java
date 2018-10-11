package com.mfeldsztejn.despegar.dtos;

import java.util.List;

public class HotelsResponse {
    private Metadata metadata;
    private List<Hotel> items;

    public List<Hotel> getItems() {
        return items;
    }

    private static class Metadata {
        private String message;
        private String code;
        private String uow;
    }
}
