package com.mfeldsztejn.despegar.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mfeldsztejn.despegar.MainActivity;
import com.mfeldsztejn.despegar.R;
import com.mfeldsztejn.despegar.dtos.APIError;
import com.mfeldsztejn.despegar.dtos.Hotel;
import com.mfeldsztejn.despegar.ui.ErrorView;
import com.mfeldsztejn.despegar.ui.main.adapter.HotelsAdapter;

import java.util.List;

public class MainFragment extends Fragment {

    private ProgressBar progressBar;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private MainViewModel viewModel;
    private RecyclerView recyclerView;
    private ErrorView errorView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        // Activity should already exist at this point
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);

        recyclerView = view.findViewById(R.id.recyclerView);
        errorView = view.findViewById(R.id.error_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        decoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_drawable));
        recyclerView.addItemDecoration(decoration);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getHotels().observe(this, new Observer<List<Hotel>>() {
            @Override
            public void onChanged(@Nullable List<Hotel> hotels) {
                recyclerView.setAdapter(new HotelsAdapter(hotels));
                progressBar.setVisibility(View.GONE);
            }
        });
        viewModel.getError().observe(this, new Observer<Throwable>() {
            @Override
            public void onChanged(@Nullable Throwable throwable) {
                if (throwable == null) {
                    errorView.setVisibility(View.GONE);
                    return;
                }
                errorView.setVisibility(View.VISIBLE);
                errorView.setText(R.string.generic_error);
                if (throwable instanceof APIError && ((APIError) throwable).getStatusCode() >= 500) {
                    errorView.setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewModel.requestHotels();
                        }
                    });
                    errorView.setText(R.string.server_error);
                }
                progressBar.setVisibility(View.GONE);
            }
        });

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        return view;
    }

}
