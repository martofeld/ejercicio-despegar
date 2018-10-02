package com.mfeldsztejn.despegar.ui.detail;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mfeldsztejn.despegar.R;
import com.mfeldsztejn.despegar.dtos.Hotel;
import com.mfeldsztejn.despegar.dtos.hotel.HotelExpansion;
import com.mfeldsztejn.despegar.events.FragmentTransactionEvent;
import com.mfeldsztejn.despegar.ui.main.adapter.AmenitiesAdapter;
import com.mfeldsztejn.despegar.ui.picture.PictureFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;

public class DetailFragment extends Fragment implements View.OnClickListener {

    private static final String HOTEL_KEY = "HOTEL_KEY";

    private Hotel hotel;
    private DetailViewModel viewModel;
    private ImageView headerImage;
    private SuccessObserver successObserver;
    private ErrorObserver errorObserver;

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
        return inflater.inflate(R.layout.detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setUpToolbar(view);

        preload(view);

        successObserver = new SuccessObserver(this);
        viewModel.getHotel().observe(this, successObserver);

        errorObserver = new ErrorObserver(this);
        viewModel.getError().observe(this, errorObserver);
    }

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void preload(View view) {
        // Name
        TextView name = view.findViewById(R.id.hotel_name);
        name.setText(hotel.getName());
        ViewCompat.setTransitionName(name, getContext().getString(R.string.transition_name_template, hotel.getId(), "name"));

        // Price
        TextView price = view.findViewById(R.id.hotel_price);
        price.setText(getContext().getString(R.string.hotel_price, hotel.getPrice().getCurrency().getMask(), hotel.getPrice().getBase()));

        // Stars
        LinearLayout starsContainer = view.findViewById(R.id.hotel_stars_container);
        Drawable starDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_star);
        int padding = getContext().getResources().getDimensionPixelSize(R.dimen.detail_fragment_sides_margin) / 2;
        for (int i = 0; i < hotel.getStars(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageDrawable(starDrawable);
            imageView.setPadding(padding, 0, padding, 0);
            starsContainer.addView(imageView);
        }

        // Image
        headerImage = view.findViewById(R.id.header_image);
        ViewCompat.setTransitionName(headerImage, getContext().getString(R.string.transition_name_template, hotel.getId(), "image"));
        headerImage.setOnClickListener(this);
        loadImage(hotel.getMainPicture(), headerImage);

        // Amenities
        RecyclerView amenities = view.findViewById(R.id.hotel_amenities);
        amenities.setLayoutManager(new GridLayoutManager(getContext(), 2));
        amenities.setAdapter(new AmenitiesAdapter(hotel.getAmenities(), true));

        // Address
        TextView address = view.findViewById(R.id.hotel_address);
        address.setText(hotel.getAddress());
    }

    @VisibleForTesting
    /* default */ void loadImage(String url, ImageView view) {
        Glide.with(getContext())
                .load(url)
                .apply(RequestOptions.centerCropTransform().error(R.drawable.ic_image_error))
                .into(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_image:
                EventBus.getDefault().post(
                        new FragmentTransactionEvent(
                                PictureFragment.newInstance(hotel.getMainPicture(), ViewCompat.getTransitionName(headerImage)),
                                Collections.singletonList(headerImage)));
                break;
        }
    }
}
