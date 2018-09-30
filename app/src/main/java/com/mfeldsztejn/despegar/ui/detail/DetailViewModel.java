package com.mfeldsztejn.despegar.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.mfeldsztejn.despegar.dtos.Hotel;
import com.mfeldsztejn.despegar.dtos.hotel.HotelExpansion;
import com.mfeldsztejn.despegar.dtos.hotel.HotelResponse;
import com.mfeldsztejn.despegar.repositories.DefaultCallback;
import com.mfeldsztejn.despegar.repositories.DespegarRepository;
import com.mfeldsztejn.despegar.repositories.RetrofitHelper;

public class DetailViewModel extends ViewModel implements DefaultCallback.Callback<HotelResponse> {
    private MutableLiveData<HotelExpansion> hotelLiveData = new MutableLiveData<>();
    private Hotel hotel;
    private GoogleMap googleMap;

    public void setHotel(Hotel hotel) {
        // Only set hotel once
        if (this.hotel == null) {
            this.hotel = hotel;
            requestHotel();
        }
    }

    private void requestHotel() {
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
        Log.e("TAG", "Error getting detail", t);
//        TODO: hotelLiveData.setValue(hotel);
    }

    public LiveData<HotelExpansion> getHotel() {
        return hotelLiveData;
    }
}
