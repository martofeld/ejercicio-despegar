package com.mfeldsztejn.despegar.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mfeldsztejn.despegar.R;

public class ErrorView extends RelativeLayout {

    private TextView text;
    private ImageView icon;
    private Button button;

    public ErrorView(Context context) {
        super(context);
        init();
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        View view = inflate(getContext(), R.layout.error_view, this);

        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        text = view.findViewById(R.id.error_text);
        icon = view.findViewById(R.id.error_icon);
        button = view.findViewById(R.id.error_action);
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public void setText(@StringRes int text) {
        this.text.setText(text);
    }

    public void setIcon(@DrawableRes int icon){
        this.icon.setImageDrawable(ContextCompat.getDrawable(getContext(), icon));
    }

    public void setAction(@StringRes int text, OnClickListener listener){
        this.button.setVisibility(VISIBLE);
        this.button.setText(text);
        this.button.setOnClickListener(listener);
    }
}
