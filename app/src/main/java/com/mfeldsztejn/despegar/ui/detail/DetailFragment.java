package com.mfeldsztejn.despegar.ui.detail;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mfeldsztejn.despegar.R;
import com.mfeldsztejn.despegar.dtos.Hotel;
import com.mfeldsztejn.despegar.dtos.hotel.HotelExpansion;
import com.mfeldsztejn.despegar.dtos.hotel.Review;
import com.mfeldsztejn.despegar.ui.detail.adapter.ReviewsAdapter;
import com.mfeldsztejn.despegar.ui.main.adapter.AmenitiesAdapter;

import java.util.List;

public class DetailFragment extends Fragment implements View.OnClickListener {

    private static final int DESCRIPTION_MAX_LINES = 5;
    private static final String HOTEL_KEY = "HOTEL_KEY";
    private static final String MAP_URL = "http://maps.google.com/maps/api/staticmap?center=%f,%f&zoom=15&size=200x200&sensor=false&key=%s";

    private Hotel hotel;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private DetailViewModel viewModel;
    private TextView description;
    private ImageView location;

    public static DetailFragment newInstance(Hotel hotel) {
        Bundle args = new Bundle();
        args.putSerializable(HOTEL_KEY, hotel);

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            initWithBundle(getArguments());
        } else {
            initWithBundle(savedInstanceState);
        }

        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        viewModel.setHotel(hotel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    private void initWithBundle(Bundle bundle) {
        hotel = (Hotel) bundle.getSerializable(HOTEL_KEY);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(HOTEL_KEY, hotel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment, container, false);

        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(hotel.getName());

        final ImageView headerImage = view.findViewById(R.id.header_image);
        headerImage.setOnClickListener(this);

        final int primaryColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);

        Glide.with(getContext())
                .load(hotel.getMainPicture())
                .apply(new RequestOptions().centerCrop())
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                        Palette generate = Palette.from(bitmap).generate();
                        collapsingToolbarLayout.setContentScrimColor(generate.getVibrantColor(primaryColor));
                        return false;
                    }
                })
                .into(headerImage);
        ViewCompat.setTransitionName(headerImage, getResources().getString(R.string.transition_name_template, hotel.getId(), "image"));

        RecyclerView amenities = view.findViewById(R.id.hotel_amenities);
        amenities.setLayoutManager(new GridLayoutManager(getContext(), 2));
        amenities.setAdapter(new AmenitiesAdapter(hotel.getAmenities(), true));

        TextView address = view.findViewById(R.id.hotel_address);
        address.setText(hotel.getAddress());

        description = view.findViewById(R.id.hotel_description);
        description.setOnClickListener(this);
        description.setMaxLines(DESCRIPTION_MAX_LINES);

        location = view.findViewById(R.id.hotel_location);

        viewModel.getHotel().observe(this, new Observer<HotelExpansion>() {
            @Override
            public void onChanged(@Nullable HotelExpansion hotelExpansion) {
                description.setText(hotelExpansion.getDescription());

                if (hotelExpansion.getReviews() == null){
                    getView().findViewById(R.id.hotel_reviews_container).setVisibility(View.GONE);
                } else {
                    RecyclerView reviewsRecyclerView = getView().findViewById(R.id.hotel_reviews);
                    reviewsRecyclerView.setAdapter(new ReviewsAdapter(hotelExpansion.getReviews()));
                    reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }

                double latitude = hotelExpansion.getGeoLocation().getLatitude();
                double longitude = hotelExpansion.getGeoLocation().getLongitude();
                Glide.with(location)
                        .load(String.format(MAP_URL, latitude, longitude, getString(R.string.maps_key)))
                        .into(location);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hotel_description:
                TextView tv = (TextView) v;
                if (tv.getMaxLines() == Integer.MAX_VALUE) {
                    tv.setMaxLines(DESCRIPTION_MAX_LINES);
                } else {
                    tv.setMaxLines(Integer.MAX_VALUE);
                }
                break;
            case R.id.header_image:
                // TODO: Open image full screen
                break;
        }
    }
}
