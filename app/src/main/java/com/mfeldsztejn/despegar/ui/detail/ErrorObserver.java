package com.mfeldsztejn.despegar.ui.detail;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.mfeldsztejn.despegar.R;

import java.lang.ref.WeakReference;


/* default */ class ErrorObserver implements Observer<Throwable> {
    private WeakReference<DetailFragment> fragment;

    public ErrorObserver(DetailFragment fragment) {
        this.fragment = new WeakReference<>(fragment);
    }

    @Override
    public void onChanged(@Nullable Throwable throwable) {
        DetailFragment fragment;
        //Asign and check at the same time since reference could be collected
        if ((fragment = this.fragment.get()) == null){
            return;
        }
        View view = fragment.getView();
        view.findViewById(R.id.hotel_location).setVisibility(View.GONE);
        view.findViewById(R.id.hotel_reviews_empty).setVisibility(View.VISIBLE);
        view.findViewById(R.id.hotel_reviews).setVisibility(View.GONE);
        TextView description = view.findViewById(R.id.hotel_description);
        description.setText(R.string.no_description);
    }

}
