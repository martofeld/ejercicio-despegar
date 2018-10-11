package com.mfeldsztejn.despegar.ui.main.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mfeldsztejn.despegar.R;
import com.mfeldsztejn.despegar.dtos.Hotel;

import java.util.List;

public class HotelsAdapter extends RecyclerView.Adapter<HotelViewHolder> {

    private List<Hotel> hotels;

    public HotelsAdapter(List<Hotel> hotels) {
        this.hotels = hotels;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new HotelViewHolder(inflater.inflate(R.layout.hotel_view_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        holder.bind(hotels.get(position));
    }

    @Override
    public int getItemCount() {
        return hotels == null ? 0 : hotels.size();
    }
}
