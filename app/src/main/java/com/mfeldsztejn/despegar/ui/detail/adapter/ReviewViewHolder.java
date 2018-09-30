package com.mfeldsztejn.despegar.ui.detail.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mfeldsztejn.despegar.R;
import com.mfeldsztejn.despegar.dtos.hotel.Review;

/* default */ class ReviewViewHolder extends RecyclerView.ViewHolder {

    private static final String FLAG_IDENTIFIER_FORMAT = "ic_%s_flag";

    private TextView title, positiveReview, negativeReview, user;

    /* default */ ReviewViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.review_title);
        positiveReview = itemView.findViewById(R.id.review_positive);
        negativeReview = itemView.findViewById(R.id.review_negative);
        user = itemView.findViewById(R.id.review_user);
    }

    /* default */ void bind(Review review) {
        configureTextView(title, review.getComments().getTitle());
        configureTextView(positiveReview, review.getComments().getGood());
        configureTextView(negativeReview, review.getComments().getBad());
        configureTextView(user, review.getUser().getName());
        @DrawableRes int flagDrawableResource
                = user.getResources().getIdentifier(String.format(FLAG_IDENTIFIER_FORMAT, review.getUser().getCountry().toLowerCase()), "drawable", user.getContext().getPackageName());
        Drawable flagDrawable = ContextCompat.getDrawable(user.getContext(), flagDrawableResource);

        // Normaly the flag would already be round and the required size
        Glide.with(user)
                .load(flagDrawable)
                .apply(RequestOptions.circleCropTransform())
                .into(new CustomViewTarget<TextView, Drawable>(user) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {

                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        user.setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null);
                    }

                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private void configureTextView(TextView textView, String text) {
        textView.setVisibility(text == null || text.isEmpty() ? View.GONE : View.VISIBLE);
        textView.setText(text);
    }
}
