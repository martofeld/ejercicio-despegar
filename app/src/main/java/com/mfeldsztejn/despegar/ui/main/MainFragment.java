package com.mfeldsztejn.despegar.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mfeldsztejn.despegar.R;
import com.mfeldsztejn.despegar.dtos.Hotel;
import com.mfeldsztejn.despegar.ui.main.adapter.HotelsAdapter;

import java.util.List;

public class MainFragment extends Fragment {

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private MainViewModel viewModel;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getHotels().observe(this, new Observer<List<Hotel>>() {
            @Override
            public void onChanged(@Nullable List<Hotel> hotels) {
                recyclerView.setAdapter(new HotelsAdapter(hotels));
            }
        });
        return view;
    }

}
