package com.mfeldsztejn.despegar.repositories;

import com.mfeldsztejn.despegar.dtos.Hotel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DespegarRepository {

    @GET("/hotels")
    Call<List<Hotel>> hotels();
}
