package com.mfeldsztejn.despegar.ui.detail;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.Point;
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
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.mfeldsztejn.despegar.R;
import com.mfeldsztejn.despegar.dtos.Hotel;
import com.mfeldsztejn.despegar.dtos.hotel.HotelExpansion;
import com.mfeldsztejn.despegar.events.AddToolbarEvent;
import com.mfeldsztejn.despegar.ui.ExpandOnClickListener;
import com.mfeldsztejn.despegar.ui.detail.adapter.ReviewsAdapter;
import com.mfeldsztejn.despegar.ui.main.adapter.AmenitiesAdapter;

import org.greenrobot.eventbus.EventBus;

public class DetailFragment extends Fragment implements View.OnClickListener {

    private static final int DESCRIPTION_MAX_LINES = 5;
    private static final String HOTEL_KEY = "HOTEL_KEY";
    private static final String MAP_URL = "https://maps.google.com/maps/api/staticmap?center=%f,%f&scale=4&zoom=17&size=%dx%d&markers=color:red%%7C%f,%f";

    private Hotel hotel;
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

        CollapsingToolbarLayout toolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        toolbarLayout.setTitleEnabled(false);
        toolbarLayout.setContentScrimColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        Toolbar toolbar = toolbarLayout.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        TextView name = view.findViewById(R.id.hotel_name);
        name.setText(hotel.getName());
        ViewCompat.setTransitionName(name, name.getResources().getString(R.string.transition_name_template, hotel.getId(), "name"));

        TextView price = view.findViewById(R.id.hotel_price);
        price.setText(getString(R.string.hotel_price,
                hotel.getPrice().getCurrency().getMask(),
                hotel.getPrice().getBase()));

        LinearLayout starsContainer = view.findViewById(R.id.hotel_stars_container);
        Drawable starDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_star);
        int padding = getResources().getDimensionPixelSize(R.dimen.detail_fragment_sides_margin) / 2;
        for (int i = 0; i < hotel.getStars(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageDrawable(starDrawable);
            imageView.setPadding(padding, 0, padding, 0);
            starsContainer.addView(imageView);
        }

        final ImageView headerImage = view.findViewById(R.id.header_image);
        headerImage.setOnClickListener(this);

        Glide.with(getContext())
                .load(hotel.getMainPicture())
                .apply(RequestOptions.centerCropTransform().error(R.drawable.ic_image_error))
                .into(headerImage);
        ViewCompat.setTransitionName(headerImage, getResources().getString(R.string.transition_name_template, hotel.getId(), "image"));

        RecyclerView amenities = view.findViewById(R.id.hotel_amenities);
        amenities.setLayoutManager(new GridLayoutManager(getContext(), 2));
        amenities.setAdapter(new AmenitiesAdapter(hotel.getAmenities(), true));

        TextView address = view.findViewById(R.id.hotel_address);
        address.setText(hotel.getAddress());

        description = view.findViewById(R.id.hotel_description);
        description.setOnClickListener(new ExpandOnClickListener(DESCRIPTION_MAX_LINES));
        description.setMaxLines(DESCRIPTION_MAX_LINES);

        location = view.findViewById(R.id.hotel_location);

        viewModel.getHotel().observe(this, new Observer<HotelExpansion>() {
            @Override
            public void onChanged(@Nullable HotelExpansion hotelExpansion) {
                description.setText(hotelExpansion.getDescription());

                RecyclerView reviews = getView().findViewById(R.id.hotel_reviews);
                if (hotelExpansion.getReviews() == null) {
                    getView().findViewById(R.id.hotel_reviews_empty).setVisibility(View.VISIBLE);
                    reviews.setVisibility(View.GONE);
                } else {
                    reviews.setAdapter(new ReviewsAdapter(hotelExpansion.getReviews()));
                    reviews.setLayoutManager(new LinearLayoutManager(getContext()));
                }

                double latitude = hotelExpansion.getGeoLocation().getLatitude();
                double longitude = hotelExpansion.getGeoLocation().getLongitude();
                int height = getResources().getDimensionPixelSize(R.dimen.map_height);
                Point point = new Point();
                getActivity().getWindowManager().getDefaultDisplay().getSize(point);
                Glide.with(location.getContext())
                        .load(String.format(MAP_URL, latitude, longitude, point.x, height, latitude, longitude))
                        .apply(RequestOptions.centerCropTransform().error(R.drawable.ic_image_error))
                        .into(location);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_image:
                // TODO: Open image full screen
                break;
        }
    }
}
