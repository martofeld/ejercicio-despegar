package com.mfeldsztejn.despegar.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.mfeldsztejn.despegar.dtos.Hotel;
import com.mfeldsztejn.despegar.repositories.DefaultCallback;
import com.mfeldsztejn.despegar.repositories.DespegarRepository;
import com.mfeldsztejn.despegar.repositories.RetrofitHelper;

import java.util.List;

public class MainViewModel extends ViewModel implements DefaultCallback.Callback<List<Hotel>> {
    // TODO: Implement the ViewModel
    private MutableLiveData<List<Hotel>> hotels = new MutableLiveData<>();

    public MainViewModel(){
        RetrofitHelper.create()
                .create(DespegarRepository.class)
                .hotels()
                .enqueue(new DefaultCallback<>(this));
    }

    public LiveData<List<Hotel>> getHotels() {
        return hotels;
    }

    @Override
    public void onSuccess(List<Hotel> value) {
        hotels.setValue(value);
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
