package com.mfeldsztejn.despegar.repositories;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitHelper {

    private static final String BASE_URL = "http://private-a2ba2-jovenesdealtovuelo.apiary-mock.com";

    public static Retrofit create(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
