package com.mfeldsztejn.despegar.ui.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mfeldsztejn.despegar.R;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class ErrorObserverTest {

    @Test
    public void testOnChange() {
        DetailFragment fragment = Mockito.spy(new DetailFragment());

        View view = Mockito.mock(View.class);

        RecyclerView reviewsList = Mockito.mock(RecyclerView.class);
        TextView reviewsEmpty = Mockito.mock(TextView.class);
        TextView description = Mockito.mock(TextView.class);
        TextView location = Mockito.mock(TextView.class);

        Mockito.doReturn(reviewsList).when(view).findViewById(R.id.hotel_reviews);
        Mockito.doReturn(reviewsEmpty).when(view).findViewById(R.id.hotel_reviews_empty);
        Mockito.doReturn(description).when(view).findViewById(R.id.hotel_description);
        Mockito.doReturn(location).when(view).findViewById(R.id.hotel_location);

        Mockito.doReturn(view).when(fragment).getView();

        new ErrorObserver(fragment).onChanged(new Exception());

        Mockito.verify(location).setVisibility(View.GONE);
        Mockito.verify(reviewsList).setVisibility(View.GONE);
        Mockito.verify(reviewsEmpty).setVisibility(View.VISIBLE);
        Mockito.verify(description).setText(R.string.no_description);
    }

}