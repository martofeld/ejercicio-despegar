package com.mfeldsztejn.despegar.ui.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mfeldsztejn.despegar.R;
import com.mfeldsztejn.despegar.dtos.Hotel;

public class HotelViewHolder extends RecyclerView.ViewHolder {

    private TextView nameTextView;
    private TextView priceTextView;

    public HotelViewHolder(View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.hotelName);
        priceTextView = itemView.findViewById(R.id.hotelPrice);
    }

    public void bind(Hotel hotel) {
        nameTextView.setText(hotel.getName());
        priceTextView.setText(hotel.getPrice().getCurrency().getMask() + hotel.getPrice().getBase());
    }
}
