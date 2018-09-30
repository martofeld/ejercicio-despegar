package com.mfeldsztejn.despegar;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mfeldsztejn.despegar.dtos.Hotel;
import com.mfeldsztejn.despegar.events.AddToolbarEvent;
import com.mfeldsztejn.despegar.events.FragmentTransactionEvent;
import com.mfeldsztejn.despegar.ui.detail.DetailFragment;
import com.mfeldsztejn.despegar.ui.main.MainFragment;
import com.mfeldsztejn.despegar.ui.main.adapter.HotelsAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

    private static final String BACKSTACK_KEY = "BACKSTACK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onExecuteFragmentTransactionEvent(FragmentTransactionEvent event) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (event.getSharedViews() != null) {
            for (View view : event.getSharedViews()) {
                transaction.addSharedElement(view, ViewCompat.getTransitionName(view));
            }
        }
        transaction
                .replace(R.id.container, event.getFragment())
                .addToBackStack(BACKSTACK_KEY)
                .commit();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onAddToolbarEvent(AddToolbarEvent event) {
        setSupportActionBar(event.getToolbar());
    }
}
