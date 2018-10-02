package com.mfeldsztejn.despegar.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.mfeldsztejn.despegar.dtos.Hotel;
import com.mfeldsztejn.despegar.dtos.hotel.HotelExpansion;
import com.mfeldsztejn.despegar.dtos.hotel.HotelResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.robolectric.util.ReflectionHelpers;

import static org.junit.Assert.*;

public class DetailViewModelTest {

    private DetailViewModel viewModel;

    @Before
    public void setUp(){
        viewModel = Mockito.spy(new DetailViewModel());
        Mockito.doNothing().when(viewModel).requestHotel();
    }

    @Test
    public void testSetHotel_shouldSetValue(){
        Hotel mockHotel = Mockito.mock(Hotel.class);
        viewModel.setHotel(mockHotel);

        Hotel hotel = ReflectionHelpers.getField(viewModel, "hotel");
        Assert.assertEquals(mockHotel, hotel);

        Mockito.verify(viewModel, Mockito.times(1)).requestHotel();
    }

    @Test
    public void testSetHotel_shouldNotSetValueIfAlreadyExists(){
        Hotel mockHotel = Mockito.mock(Hotel.class);
        viewModel.setHotel(mockHotel);

        Hotel otherMockHotel = Mockito.mock(Hotel.class);
        viewModel.setHotel(otherMockHotel);

        // Verify its still the first
        Hotel hotel = ReflectionHelpers.getField(viewModel, "hotel");
        Assert.assertEquals(mockHotel, hotel);

        Mockito.verify(viewModel, Mockito.times(1)).requestHotel();
    }

    @Test
    public void testOnRequestSuccess_shouldSetValueToLiveData(){
        MutableLiveData<HotelExpansion> liveData = Mockito.mock(MutableLiveData.class);

        ReflectionHelpers.setField(viewModel, "hotelLiveData", liveData);

        HotelResponse hotelResponse = Mockito.mock(HotelResponse.class);
        HotelExpansion expansion = Mockito.mock(HotelExpansion.class);
        Mockito.doReturn(expansion).when(hotelResponse).getHotel();

        viewModel.onSuccess(hotelResponse);

        Mockito.verify(liveData).setValue(expansion);
    }

    @Test
    public void testOnRequestError_shouldSetValueToLiveData(){
        MutableLiveData<Throwable> liveData = Mockito.mock(MutableLiveData.class);

        ReflectionHelpers.setField(viewModel, "errorLiveData", liveData);

        Throwable throwable = Mockito.mock(Throwable.class);
        viewModel.onFailure(throwable);

        Mockito.verify(liveData).setValue(throwable);
    }
}