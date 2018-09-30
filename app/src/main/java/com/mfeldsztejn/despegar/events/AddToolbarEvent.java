package com.mfeldsztejn.despegar.events;

import android.support.v7.widget.Toolbar;

public class AddToolbarEvent {

    private final Toolbar toolbar;

    public AddToolbarEvent(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
