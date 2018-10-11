package com.mfeldsztejn.despegar.events;

import android.support.v4.app.Fragment;
import android.view.View;

import java.util.List;

public class FragmentTransactionEvent {

    private final Fragment fragment;
    private final List<? extends View> sharedViews;

    public FragmentTransactionEvent(Fragment fragment, List<? extends View> sharedViews){
        this.fragment = fragment;
        this.sharedViews = sharedViews;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public List<? extends View> getSharedViews() {
        return sharedViews;
    }
}
