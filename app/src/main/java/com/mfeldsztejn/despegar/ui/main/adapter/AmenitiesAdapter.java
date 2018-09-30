package com.mfeldsztejn.despegar.ui.main.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mfeldsztejn.despegar.R;
import com.mfeldsztejn.despegar.dtos.Amenity;

import java.util.List;

public class AmenitiesAdapter extends RecyclerView.Adapter<AmenityViewHolder> {
    private List<Amenity> amenities;
    private boolean showTitles;

    public AmenitiesAdapter(List<Amenity> amenities, boolean showTitles) {
        this.amenities = amenities;
        this.showTitles = showTitles;
    }

    @NonNull
    @Override
    public AmenityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new AmenityViewHolder(inflater.inflate(R.layout.amenity_view_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AmenityViewHolder holder, int position) {
        holder.bind(amenities.get(position), showTitles);
    }

    @Override
    public int getItemCount() {
        return amenities == null ? 0 : amenities.size();
    }
}
