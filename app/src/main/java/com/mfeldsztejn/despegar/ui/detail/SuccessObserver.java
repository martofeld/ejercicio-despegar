package com.mfeldsztejn.despegar.ui.detail;

import android.arch.lifecycle.Observer;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfeldsztejn.despegar.R;
import com.mfeldsztejn.despegar.dtos.hotel.HotelExpansion;
import com.mfeldsztejn.despegar.ui.ExpandOnClickListener;
import com.mfeldsztejn.despegar.ui.detail.adapter.ReviewsAdapter;

import java.lang.ref.WeakReference;

class SuccessObserver implements Observer<HotelExpansion> {
    private static final int DESCRIPTION_MAX_LINES = 5;
    private static final String MAP_URL = "https://maps.google.com/maps/api/staticmap?center=%f,%f&scale=4&zoom=17&size=%dx%d&markers=color:red%%7C%f,%f&key=%s";

    private WeakReference<DetailFragment> fragment;

    public SuccessObserver(DetailFragment fragment) {
        this.fragment = new WeakReference<>(fragment);
    }

    @Override
    public void onChanged(@Nullable HotelExpansion hotelExpansion) {
        DetailFragment fragment;
        //Asign and check at the same time since reference could be collected
        if ((fragment = this.fragment.get()) == null){
            return;
        }
        View v = fragment.getView();
        TextView description = v.findViewById(R.id.hotel_description);
        description.setOnClickListener(new ExpandOnClickListener(DESCRIPTION_MAX_LINES));
        description.setMaxLines(DESCRIPTION_MAX_LINES);
        description.setText(hotelExpansion.getDescription());

        RecyclerView reviews = v.findViewById(R.id.hotel_reviews);
        if (hotelExpansion.getReviews() == null || hotelExpansion.getReviews().isEmpty()) {
            v.findViewById(R.id.hotel_reviews_empty).setVisibility(View.VISIBLE);
            reviews.setVisibility(View.GONE);
        } else {
            reviews.setAdapter(new ReviewsAdapter(hotelExpansion.getReviews()));
            reviews.setLayoutManager(new LinearLayoutManager(fragment.getContext()));
        }

        double latitude = hotelExpansion.getGeoLocation().getLatitude();
        double longitude = hotelExpansion.getGeoLocation().getLongitude();
        int height = fragment.getResources().getDimensionPixelSize(R.dimen.map_height);
        Point point = new Point();
        fragment.getActivity().getWindowManager().getDefaultDisplay().getSize(point);
        String url = String.format(MAP_URL, latitude, longitude, point.x, height, latitude, longitude, fragment.getString(R.string.maps_key));
        ImageView location = v.findViewById(R.id.hotel_location);
        fragment.loadImage(url, location);
    }

    public void cleanUp(){
        this.fragment = null;
    }
}
