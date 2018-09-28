package com.mfeldsztejn.despegar.repositories;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DefaultCallback<T> implements Callback<T> {

    private Callback<T> callback;

    public DefaultCallback(Callback<T> callback){
        this.callback = callback;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()){
            callback.onSuccess(response.body());
        } else {
            // TODO?
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        callback.onFailure(t);
    }

    public interface Callback<T> {
        void onSuccess(T value);

        void onFailure(Throwable t);
    }
}
