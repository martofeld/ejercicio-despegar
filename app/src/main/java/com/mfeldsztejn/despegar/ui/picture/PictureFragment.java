package com.mfeldsztejn.despegar.ui.picture;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mfeldsztejn.despegar.MainActivity;
import com.mfeldsztejn.despegar.R;

public class PictureFragment extends Fragment {
    private static final String URL_KEY = "URL";
    private static final String TRANSITION_NAME_KEY = "TRANSITION_NAME";

    public static PictureFragment newInstance(String url, String transitionName) {

        Bundle args = new Bundle();
        args.putString(URL_KEY, url);
        args.putString(TRANSITION_NAME_KEY, transitionName);
        PictureFragment fragment = new PictureFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
//        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String url = getArguments().getString(URL_KEY);
        String transitionName = getArguments().getString(TRANSITION_NAME_KEY);

        View view = inflater.inflate(R.layout.picture_fragment, container, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        ImageView imageView = view.findViewById(R.id.hotel_image);
        ViewCompat.setTransitionName(imageView, transitionName);
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);

        return view;
    }
}
