package com.mfeldsztejn.despegar.ui.detail;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfeldsztejn.despegar.MockInterceptor;
import com.mfeldsztejn.despegar.R;
import com.mfeldsztejn.despegar.ResourceParser;
import com.mfeldsztejn.despegar.dtos.Hotel;
import com.mfeldsztejn.despegar.dtos.hotel.HotelExpansion;
import com.mfeldsztejn.despegar.repositories.RetrofitHelper;
import com.mfeldsztejn.despegar.ui.detail.adapter.ReviewsAdapter;
import com.mfeldsztejn.despegar.ui.main.adapter.AmenitiesAdapter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;
import org.robolectric.util.ReflectionHelpers;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Collections;

@RunWith(RobolectricTestRunner.class)
// Robolectric has issues with API 28 until the 4.0 release
@Config(sdk = 27)
public class DetailFragmentTest {
    /**
     * Should match DetailFragment#HOTEL_KEY
     */
    private static final String HOTEL_KEY = "HOTEL_KEY";

    private Hotel hotel;

    @Before
    public void setUp() throws IOException {
        hotel = ResourceParser.getTestResourceObject("hotel_from_list.json", Hotel.class);

        RetrofitHelper.withInterceptors(Collections.singletonList(new MockInterceptor("hotel_complete.json")));
    }

    @Test
    public void testNewInstance_shouldCreateFragmentAndAddArgs() {
        DetailFragment fragment = DetailFragment.newInstance(hotel);

        Assert.assertNotNull(fragment.getArguments());
        Assert.assertEquals(hotel, fragment.getArguments().getSerializable(HOTEL_KEY));
        Assert.assertEquals(1, fragment.getArguments().size());
    }

    @Test
    @Config(sdk = 19) // To avoid setting animations
    public void testOnCreate_firstTime_shouldGetHotelFromArguments() {
        Bundle arguments = Mockito.spy(new Bundle());
        arguments.putSerializable(HOTEL_KEY, hotel);

        FragmentActivity activity = Mockito.mock(FragmentActivity.class);
        Application application = Mockito.mock(Application.class);

        DetailFragment fragment = Mockito.spy(new DetailFragment());
        fragment.setArguments(arguments);
        Mockito.doReturn(activity).when(fragment).getContext();
        Mockito.doReturn(activity).when(fragment).getActivity();
        Mockito.doReturn(application).when(activity).getApplication();

        fragment.onCreate(null);

        Mockito.verify(fragment).getArguments();
        Mockito.verify(arguments).getSerializable(HOTEL_KEY);
    }

    @Test
    @Config(sdk = 19) // To avoid setting animations
    public void testOnCreate_afterRecreation_shouldGetHotelFromSavedInstanceState() {
        Bundle arguments = Mockito.spy(new Bundle());
        arguments.putSerializable(HOTEL_KEY, hotel);

        Bundle savedInstanceState = Mockito.spy(new Bundle());
        savedInstanceState.putSerializable(HOTEL_KEY, hotel);

        FragmentActivity activity = Mockito.mock(FragmentActivity.class);
        Application application = Mockito.mock(Application.class);

        DetailFragment fragment = Mockito.spy(new DetailFragment());
        fragment.setArguments(arguments);
        Mockito.doReturn(activity).when(fragment).getContext();
        Mockito.doReturn(activity).when(fragment).getActivity();
        Mockito.doReturn(application).when(activity).getApplication();

        fragment.onCreate(savedInstanceState);

        Mockito.verify(savedInstanceState, Mockito.times(1)).getSerializable(HOTEL_KEY);
        Mockito.verify(arguments, Mockito.never()).getSerializable(HOTEL_KEY);
    }

    @Test
    public void testOnCreateView_shouldBindViews() {
        DetailFragment detailFragment = Mockito.spy(new DetailFragment());
        ReflectionHelpers.setField(detailFragment, "hotel", hotel);

        DetailViewModel viewModel = Mockito.mock(DetailViewModel.class);
        LiveData<HotelExpansion> hotelLiveData = Mockito.mock(LiveData.class);
        Mockito.doReturn(hotelLiveData).when(viewModel).getHotel();
        LiveData<Throwable> errorLiveData = Mockito.mock(LiveData.class);
        Mockito.doReturn(errorLiveData).when(viewModel).getError();
        ReflectionHelpers.setField(detailFragment, "viewModel", viewModel);

        View view = Mockito.mock(View.class);

        Toolbar toolbar = Mockito.mock(Toolbar.class);
        TextView nameTextView = Mockito.mock(TextView.class);
        TextView priceTextView = Mockito.mock(TextView.class);
        TextView addressTextView = Mockito.mock(TextView.class);
        RecyclerView amenitiesRecyclerView = Mockito.mock(RecyclerView.class);
        ImageView headerImageView = Mockito.mock(ImageView.class);
        LinearLayout starsLinearLayout = Mockito.mock(LinearLayout.class);

        Mockito.doReturn(toolbar).when(view).findViewById(R.id.toolbar);
        Mockito.doReturn(nameTextView).when(view).findViewById(R.id.hotel_name);
        Mockito.doReturn(priceTextView).when(view).findViewById(R.id.hotel_price);
        Mockito.doReturn(addressTextView).when(view).findViewById(R.id.hotel_address);
        Mockito.doReturn(amenitiesRecyclerView).when(view).findViewById(R.id.hotel_amenities);
        Mockito.doReturn(headerImageView).when(view).findViewById(R.id.header_image);
        Mockito.doReturn(starsLinearLayout).when(view).findViewById(R.id.hotel_stars_container);

        Mockito.doNothing().when(detailFragment).loadImage(hotel.getMainPicture(), headerImageView);
        //Mockito.when(detailFragment.loadImage(hotel.getMainPicture(), headerImageView)).thenReturn(true);
        Mockito.doReturn(RuntimeEnvironment.application).when(detailFragment).getContext();
        //Mockito.doCallRealMethod().when(detailFragment).onViewCreated(Mockito.any(View.class), (Bundle) Mockito.isNull());

        detailFragment.onViewCreated(view, null);

        Mockito.verify(nameTextView).setText(hotel.getName());
        Mockito.verify(priceTextView).setText("Precio por noche: $" + hotel.getPrice().getBase());
        Mockito.verify(addressTextView).setText(hotel.getAddress());
        Mockito.verify(amenitiesRecyclerView).setLayoutManager(Mockito.any(GridLayoutManager.class));
        Mockito.verify(amenitiesRecyclerView).setAdapter(Mockito.any(AmenitiesAdapter.class));
        Mockito.verify(starsLinearLayout, Mockito.times(hotel.getStars())).addView(Mockito.any(ImageView.class));
        Mockito.verify(detailFragment).loadImage(hotel.getMainPicture(), headerImageView);

        // Check is registered to callbacks
        Mockito.verify(hotelLiveData).observe(detailFragment, Mockito.any(SuccessObserver.class));
        Mockito.verify(errorLiveData).observe(detailFragment, Mockito.any(ErrorObserver.class));
    }

    @Test
    public void testOnCreateView_usesCorrectLayout(){
        DetailFragment detailFragment = new DetailFragment();

        LayoutInflater inflater = Mockito.mock(LayoutInflater.class);
        ViewGroup container = Mockito.mock(ViewGroup.class);
        detailFragment.onCreateView(inflater, container, null);

        Mockito.verify(inflater).inflate(R.layout.detail_fragment, container, false);
    }
}