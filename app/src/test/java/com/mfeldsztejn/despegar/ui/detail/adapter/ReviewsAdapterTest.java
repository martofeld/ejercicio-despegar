package com.mfeldsztejn.despegar.ui.detail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mfeldsztejn.despegar.R;
import com.mfeldsztejn.despegar.dtos.hotel.Review;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class ReviewsAdapterTest {

    @Mock
    private List<Review> reviews;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnCreateViewHolder_shouldReturnAReviewViewHolder() {
        ReviewsAdapter adapter = Mockito.spy(new ReviewsAdapter(reviews));
        ViewGroup parent = Mockito.mock(ViewGroup.class);
        Context context = Mockito.mock(Context.class);
        LayoutInflater inflater = Mockito.mock(LayoutInflater.class);
        View view = Mockito.mock(View.class);
        TextView textView = Mockito.mock(TextView.class);

        Mockito.doReturn(textView).when(view).findViewById(Mockito.anyInt());
        Mockito.doReturn(view).when(inflater).inflate(R.layout.review_view_holder, parent, false);
        Mockito.doReturn(context).when(parent).getContext();
        Mockito.doReturn(inflater).when(adapter).getLayoutInflater(context);

        RecyclerView.ViewHolder holder = adapter.onCreateViewHolder(parent, 0);

        Assert.assertTrue(holder instanceof ReviewViewHolder);
    }

    @Test
    public void onBindViewHolder() {
        ReviewsAdapter adapter = new ReviewsAdapter(reviews);
        ReviewViewHolder holder = Mockito.mock(ReviewViewHolder.class);

        Review review = Mockito.mock(Review.class);
        Mockito.doReturn(review).when(reviews).get(0);

        adapter.onBindViewHolder(holder, 0);

        Mockito.verify(holder).bind(review);
    }

    @Test
    public void getItemCount() {
        Mockito.doReturn(5).when(reviews).size();

        ReviewsAdapter adapter = new ReviewsAdapter(reviews);

        Assert.assertEquals(5, adapter.getItemCount());
    }
}