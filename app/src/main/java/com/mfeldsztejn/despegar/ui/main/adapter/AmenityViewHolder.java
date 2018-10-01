package com.mfeldsztejn.despegar.ui.main.adapter;

import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfeldsztejn.despegar.R;
import com.mfeldsztejn.despegar.dtos.Amenity;

public class AmenityViewHolder extends RecyclerView.ViewHolder {

    private ImageView icon;
    private TextView text;
    private LinearLayout container;

    public AmenityViewHolder(View itemView) {
        super(itemView);
        container = (LinearLayout) itemView;
        icon = itemView.findViewById(R.id.amenity_icon);
        text = itemView.findViewById(R.id.amenity_text);
    }

    /* default */ void bind(Amenity amenity, boolean showTitles) {
        @DrawableRes int drawable = icon.getResources()
                .getIdentifier("ic_" + amenity.getId().toLowerCase(), "drawable", icon.getContext().getPackageName());
        if (drawable != 0) {
            icon.setImageDrawable(ContextCompat.getDrawable(icon.getContext(), drawable));
        }
        text.setVisibility(showTitles ? View.VISIBLE : View.GONE);
        container.getLayoutParams().width = showTitles ? RecyclerView.LayoutParams.MATCH_PARENT : RecyclerView.LayoutParams.WRAP_CONTENT;
        if (showTitles) {
            text.setText(amenity.getDescription());
            container.setGravity(Gravity.CENTER);
        }
    }
}
