package com.mfeldsztejn.despegar.ui;

import android.view.View;
import android.widget.TextView;

public class ExpandOnClickListener implements View.OnClickListener {

    private final int maxLines;

    public ExpandOnClickListener(int maxLines) {
        this.maxLines = maxLines;
    }

    @Override
    public void onClick(View v) {
        if (!(v instanceof TextView)) {
            throw new IllegalArgumentException("ExpandOnClickListener can only be used with views that extend TextView");
        }
        TextView vAsTv = (TextView) v;
        vAsTv.setMaxLines(vAsTv.getMaxLines() == maxLines ? Integer.MAX_VALUE : maxLines);
    }
}
