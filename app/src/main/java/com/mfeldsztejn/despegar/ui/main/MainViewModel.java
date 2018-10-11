package com.mfeldsztejn.despegar.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.mfeldsztejn.despegar.dtos.Hotel;
import com.mfeldsztejn.despegar.dtos.HotelsResponse;
import com.mfeldsztejn.despegar.repositories.DefaultCallback;
import com.mfeldsztejn.despegar.repositories.DespegarRepository;
import com.mfeldsztejn.despegar.repositories.RetrofitHelper;

import java.util.List;

public class MainViewModel extends ViewModel implements DefaultCallback.Callback<HotelsResponse> {
    // TODO: Implement the ViewModel
    private MutableLiveData<List<Hotel>> hotels = new MutableLiveData<>();
    private MutableLiveData<Throwable> error = new MutableLiveData<>();

    public MainViewModel() {
        requestHotels();
    }

    @Override
    protected void onCleared() {
        hotels = null;
        error = null;
    }

    /* default */ LiveData<List<Hotel>> getHotels() {
        return hotels;
    }

    /* default */ LiveData<Throwable> getError() {
        return error;
    }

    @Override
    public void onSuccess(HotelsResponse value) {
        hotels.setValue(value.getItems());
    }

    @Override
    public void onFailure(Throwable t) {
        error.setValue(t);
    }

    /* default */ void requestHotels() {
        RetrofitHelper.create()
                .create(DespegarRepository.class)
                .hotels()
                .enqueue(new DefaultCallback<>(this));

        error.setValue(null);
    }
}
