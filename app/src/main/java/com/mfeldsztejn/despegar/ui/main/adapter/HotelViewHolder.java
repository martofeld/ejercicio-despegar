package com.mfeldsztejn.despegar.ui.main.adapter;

import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mfeldsztejn.despegar.R;
import com.mfeldsztejn.despegar.dtos.Hotel;
import com.mfeldsztejn.despegar.events.FragmentTransactionEvent;
import com.mfeldsztejn.despegar.ui.detail.DetailFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

/* default */ class HotelViewHolder extends RecyclerView.ViewHolder {

    private static final String FORMAT = "%s %s";

    private TextView name;
    private TextView price;
    private TextView originalPrice;
    private TextView stars;
    private TextView address;
    private ImageView image;
    private RecyclerView amenities;

    /* default */ HotelViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.hotel_name);
        price = itemView.findViewById(R.id.hotel_price);
        originalPrice = itemView.findViewById(R.id.hotel_price_original);
        image = itemView.findViewById(R.id.hotel_image);
        stars = itemView.findViewById(R.id.hotel_stars);
        address = itemView.findViewById(R.id.hotel_address);
        amenities = itemView.findViewById(R.id.hotel_amenities);
    }

    /* default */ void bind(final Hotel hotel) {
        Glide.with(image.getContext())
                .load(hotel.getMainPicture())
                .apply(new RequestOptions().centerCrop())
                .into(image);
        ViewCompat.setTransitionName(image, image.getResources().getString(R.string.transition_name_template, hotel.getId(), "image"));
        name.setText(hotel.getName());
        ViewCompat.setTransitionName(name, name.getResources().getString(R.string.transition_name_template, hotel.getId(), "name"));
        stars.setText(String.valueOf(hotel.getStars()));
        address.setText(hotel.getAddress());
        amenities.setLayoutManager(new LinearLayoutManager(amenities.getContext(), LinearLayoutManager.HORIZONTAL, false));
        amenities.setAdapter(new AmenitiesAdapter(hotel.getAmenities(), false));
        bindPrice(hotel);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new FragmentTransactionEvent(DetailFragment.newInstance(hotel), Arrays.asList(image, name)));
            }
        });
    }

    private void bindPrice(Hotel hotel) {
        String mask = hotel.getPrice().getCurrency().getMask();
        int basePrice = hotel.getPrice().getBase();
        int bestPrice = hotel.getPrice().getBest();
        if (basePrice != bestPrice) {
            originalPrice.setText(String.format(FORMAT, mask, basePrice));
            originalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            price.setText(String.format(FORMAT, mask, bestPrice));
            price.setTextColor(price.getResources().getColor(R.color.green));
            price.setVisibility(View.VISIBLE);
        } else {
            originalPrice.setText(String.format(FORMAT, mask, basePrice));
            originalPrice.setPaintFlags(0);
            price.setVisibility(View.GONE);
        }
    }
}
