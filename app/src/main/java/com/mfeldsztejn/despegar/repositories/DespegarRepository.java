package com.mfeldsztejn.despegar.repositories;

import com.mfeldsztejn.despegar.dtos.HotelsResponse;
import com.mfeldsztejn.despegar.dtos.hotel.HotelResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DespegarRepository {

    @GET("/hotels")
    Call<HotelsResponse> hotels();

    @GET("/hotels/{id}")
    Call<HotelResponse> hotel(@Path("id") String id);
}
