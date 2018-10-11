package com.mfeldsztejn.despegar.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.mfeldsztejn.despegar.dtos.Hotel;
import com.mfeldsztejn.despegar.dtos.hotel.HotelExpansion;
import com.mfeldsztejn.despegar.dtos.hotel.HotelResponse;
import com.mfeldsztejn.despegar.repositories.DefaultCallback;
import com.mfeldsztejn.despegar.repositories.DespegarRepository;
import com.mfeldsztejn.despegar.repositories.RetrofitHelper;

public class DetailViewModel extends ViewModel implements DefaultCallback.Callback<HotelResponse> {
    private MutableLiveData<HotelExpansion> hotelLiveData = new MutableLiveData<>();
    private MutableLiveData<Throwable> errorLiveData = new MutableLiveData<>();
    private Hotel hotel;

    @Override
    protected void onCleared() {
        super.onCleared();
        hotelLiveData = null;
        errorLiveData = null;
    }

    public void setHotel(Hotel hotel) {
        // Only set hotel once
        if (this.hotel == null) {
            this.hotel = hotel;
            requestHotel();
        }
    }

    @VisibleForTesting
    /* default */ void requestHotel() {
        RetrofitHelper.create()
                .create(DespegarRepository.class)
                .hotel(hotel.getId())
                .enqueue(new DefaultCallback<>(this));
    }

    @Override
    public void onSuccess(HotelResponse value) {
        hotelLiveData.setValue(value.getHotel());
    }

    @Override
    public void onFailure(Throwable t) {
        errorLiveData.setValue(t);
    }

    public LiveData<HotelExpansion> getHotel() {
        return hotelLiveData;
    }

    public LiveData<Throwable> getError() {
        return errorLiveData;
    }
}
