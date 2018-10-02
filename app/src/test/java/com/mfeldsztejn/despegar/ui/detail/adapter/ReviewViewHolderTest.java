package com.mfeldsztejn.despegar.ui.detail.adapter;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mfeldsztejn.despegar.R;
import com.mfeldsztejn.despegar.dtos.hotel.Review;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import javax.annotation.meta.When;

import static org.junit.Assert.*;

public class ReviewViewHolderTest {

    @Mock
    private View itemView;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor_findsAllViews() {
        Mockito.doNothing().when(itemView).findViewById(Mockito.anyInt());
        new ReviewViewHolder(itemView);

        Mockito.verify(itemView, Mockito.times(4)).findViewById(Mockito.anyInt());
    }

    @Test
    public void testBind_shouldSetUpView() {
        // Mock the views
        TextView positiveTextView = Mockito.mock(TextView.class);
        TextView negativeTextView = Mockito.mock(TextView.class);
        TextView titleTextView = Mockito.mock(TextView.class);
        TextView userTextView = Mockito.mock(TextView.class, Answers.RETURNS_DEEP_STUBS);
        Mockito.doReturn(positiveTextView).when(itemView).findViewById(R.id.review_positive);
        Mockito.doReturn(negativeTextView).when(itemView).findViewById(R.id.review_negative);
        Mockito.doReturn(titleTextView).when(itemView).findViewById(R.id.review_title);
        Mockito.doReturn(userTextView).when(itemView).findViewById(R.id.review_user);
        Mockito.when(userTextView.getContext().getPackageName()).thenReturn("com.package");
        Mockito.when(userTextView.getResources().getIdentifier("ic_ar_flag", "drawable", "com.package")).thenReturn(1);

        // Create a mock review
        Review reviewMock = Mockito.mock(Review.class, Answers.RETURNS_DEEP_STUBS);
        Mockito.when(reviewMock.getUser().getName()).thenReturn("User Name");
        Mockito.when(reviewMock.getUser().getCountry()).thenReturn("AR");
        Mockito.when(reviewMock.getComments().getGood()).thenReturn("Good comment");
        Mockito.when(reviewMock.getComments().getBad()).thenReturn("Bad comment");
        Mockito.when(reviewMock.getComments().getTitle()).thenReturn("Title");

        ReviewViewHolder spyHolder = Mockito.spy(new ReviewViewHolder(itemView));
        Mockito.doNothing().when(spyHolder).bindFlag(Mockito.anyString());
        Mockito.doCallRealMethod().when(spyHolder).bind(reviewMock);

        spyHolder.bind(reviewMock);

        Mockito.verify(positiveTextView, Mockito.times(1)).setText("Good comment");
        Mockito.verify(negativeTextView, Mockito.times(1)).setText("Bad comment");
        Mockito.verify(titleTextView, Mockito.times(1)).setText("Title");
        Mockito.verify(userTextView, Mockito.times(1)).setText("User Name");
        Mockito.verify(spyHolder, Mockito.times(1)).bindFlag("AR");
    }

}