package com.mfeldsztejn.despegar.repositories;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitHelper {

    private static final String BASE_URL = "https://private-a2ba2-jovenesdealtovuelo.apiary-mock.com";
    private static List<? extends Interceptor> interceptors;

    public static void withInterceptors(List<? extends Interceptor> interceptors){
        RetrofitHelper.interceptors = interceptors;
    }

    public static Retrofit create(){
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson));
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (interceptors != null){
            for (Interceptor interceptor : interceptors) {
                clientBuilder.addInterceptor(interceptor);
            }
        }
        retrofitBuilder.client(clientBuilder.build());
        return retrofitBuilder.build();
    }

}
