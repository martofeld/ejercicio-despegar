package com.mfeldsztejn.despegar.ui.detail;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mfeldsztejn.despegar.R;
import com.mfeldsztejn.despegar.ResourceParser;
import com.mfeldsztejn.despegar.dtos.hotel.HotelExpansion;
import com.mfeldsztejn.despegar.ui.detail.adapter.ReviewsAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import java.io.IOException;

@RunWith(RobolectricTestRunner.class)
// Robolectric has issues with API 28 until the 4.0 release
@Config(sdk = 27)
public class SuccessObserverTest {

    private HotelExpansion hotelExpansion;

    @Before
    public void setUp() throws IOException {
        hotelExpansion = ResourceParser.getTestResourceObject("hotel_complete.json", HotelExpansion.class);
    }

    @Test
    public void testOnChange_shouldBindMissingViews_withReviews() {
        View view = Mockito.mock(View.class);

        TextView descriptionTextView = Mockito.mock(TextView.class);
        RecyclerView reviewsRecyclerView = Mockito.mock(RecyclerView.class);
        ImageView locationImageView = Mockito.mock(ImageView.class);
        Drawable drawable = Mockito.mock(Drawable.class);
        ProgressBar progressBar = Mockito.mock(ProgressBar.class);

        Mockito.doReturn(drawable).when(progressBar).getIndeterminateDrawable();

        Mockito.doReturn(descriptionTextView).when(view).findViewById(R.id.hotel_description);
        Mockito.doReturn(reviewsRecyclerView).when(view).findViewById(R.id.hotel_reviews);
        Mockito.doReturn(locationImageView).when(view).findViewById(R.id.hotel_location);
        Mockito.doReturn(progressBar).when(view).findViewById(R.id.hotel_location_placeholder);

        FragmentActivity activity = Robolectric.setupActivity(FragmentActivity.class);

        DetailFragment detailFragment = Mockito.spy(new DetailFragment());
        Mockito.doReturn(view).when(detailFragment).getView();
        Mockito.doReturn(activity).when(detailFragment).getActivity();
        Mockito.doReturn(activity).when(detailFragment).getContext();
        Mockito.doReturn(activity.getResources()).when(detailFragment).getResources();

        SuccessObserver observer = Mockito.spy(new SuccessObserver(detailFragment));

        Mockito.doNothing().when(observer).loadImage(Mockito.anyString(), Mockito.any(ImageView.class), Mockito.any(Drawable.class));

        observer.onChanged(hotelExpansion);

        Mockito.verify(descriptionTextView).setText(hotelExpansion.getDescription());
        Mockito.verify(reviewsRecyclerView).setLayoutManager(Mockito.any(LinearLayoutManager.class));
        Mockito.verify(reviewsRecyclerView).setAdapter(Mockito.any(ReviewsAdapter.class));
        Mockito.verify(observer).loadImage(Mockito.anyString(), Mockito.eq(locationImageView), Mockito.eq(drawable));
        Mockito.verify(progressBar).setVisibility(View.GONE);
        Mockito.verify(progressBar).getIndeterminateDrawable();
    }

    @Test
    public void testOnChange_shouldBindMissingViews_withoutReviews() {
        View view = Mockito.mock(View.class);

        TextView descriptionTextView = Mockito.mock(TextView.class);
        RecyclerView reviewsRecyclerView = Mockito.mock(RecyclerView.class);
        ImageView locationImageView = Mockito.mock(ImageView.class);
        TextView emptyReviewsTextView = Mockito.mock(TextView.class);
        Drawable drawable = Mockito.mock(Drawable.class);
        ProgressBar progressBar = Mockito.mock(ProgressBar.class);

        Mockito.doReturn(drawable).when(progressBar).getIndeterminateDrawable();

        Mockito.doReturn(descriptionTextView).when(view).findViewById(R.id.hotel_description);
        Mockito.doReturn(reviewsRecyclerView).when(view).findViewById(R.id.hotel_reviews);
        Mockito.doReturn(locationImageView).when(view).findViewById(R.id.hotel_location);
        Mockito.doReturn(emptyReviewsTextView).when(view).findViewById(R.id.hotel_reviews_empty);
        Mockito.doReturn(progressBar).when(view).findViewById(R.id.hotel_location_placeholder);

        FragmentActivity activity = Robolectric.setupActivity(FragmentActivity.class);

        DetailFragment detailFragment = Mockito.spy(new DetailFragment());
        Mockito.doReturn(view).when(detailFragment).getView();
        Mockito.doReturn(activity).when(detailFragment).getActivity();
        Mockito.doReturn(activity).when(detailFragment).getContext();
        Mockito.doReturn(activity.getResources()).when(detailFragment).getResources();

        ReflectionHelpers.setField(hotelExpansion, "reviews", null);

        SuccessObserver observer = Mockito.spy(new SuccessObserver(detailFragment));

        Mockito.doNothing().when(observer).loadImage(Mockito.anyString(), Mockito.any(ImageView.class), Mockito.any(Drawable.class));

        observer.onChanged(hotelExpansion);

        Mockito.verify(descriptionTextView).setText(hotelExpansion.getDescription());
        Mockito.verify(reviewsRecyclerView).setVisibility(View.GONE);
        Mockito.verify(emptyReviewsTextView).setVisibility(View.VISIBLE);
        Mockito.verify(reviewsRecyclerView, Mockito.never()).setAdapter(Mockito.any(ReviewsAdapter.class));
        Mockito.verify(observer).loadImage(Mockito.anyString(), Mockito.eq(locationImageView), Mockito.eq(drawable));
        Mockito.verify(progressBar).setVisibility(View.GONE);
        Mockito.verify(progressBar).getIndeterminateDrawable();
    }


}