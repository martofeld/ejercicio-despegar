package com.mfeldsztejn.despegar.ui.main.adapter;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfeldsztejn.despegar.R;
import com.mfeldsztejn.despegar.dtos.Amenity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.util.ReflectionHelpers;

public class AmenityViewHolderTest {
    @Mock
    private LinearLayout itemView;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ImageView iconImageViewMock;
    @Mock
    private TextView titleTextViewMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.doReturn(iconImageViewMock).when(itemView).findViewById(R.id.amenity_icon);
        Mockito.doReturn(titleTextViewMock).when(itemView).findViewById(R.id.amenity_text);
    }

    @Test
    public void testBind_withShowTitlesTrue() {
        ReflectionHelpers.setStaticField(Build.VERSION.class, "SDK_INT", 28);

        Amenity amenity = Mockito.mock(Amenity.class);
        Mockito.doReturn("description").when(amenity).getDescription();
        Mockito.doReturn("wifi").when(amenity).getId();

        Mockito.when(iconImageViewMock.getContext().getPackageName()).thenReturn("com.package");
        Mockito.when(iconImageViewMock.getResources().getIdentifier("ic_wifi", "drawable", "com.package")).thenReturn(1);
        Drawable drawable = Mockito.mock(Drawable.class);
        Mockito.when(iconImageViewMock.getContext().getDrawable(1)).thenReturn(drawable);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
        Mockito.doReturn(params).when(itemView).getLayoutParams();

        AmenityViewHolder viewHolder = new AmenityViewHolder(itemView);
        viewHolder.bind(amenity, true);

        Mockito.verify(iconImageViewMock).setImageDrawable(drawable);
        Mockito.verify(titleTextViewMock).setVisibility(View.VISIBLE);
        Mockito.verify(titleTextViewMock).setText(amenity.getDescription());
        Mockito.verify(itemView).setGravity(Gravity.CENTER);
        Assert.assertEquals(LinearLayout.LayoutParams.MATCH_PARENT, params.width);
    }

    @Test
    public void testBind_withShowTitlesFalse() {
        ReflectionHelpers.setStaticField(Build.VERSION.class, "SDK_INT", 28);

        Amenity amenity = Mockito.mock(Amenity.class);
        Mockito.doReturn("description").when(amenity).getDescription();
        Mockito.doReturn("wifi").when(amenity).getId();

        Mockito.when(iconImageViewMock.getContext().getPackageName()).thenReturn("com.package");
        Mockito.when(iconImageViewMock.getResources().getIdentifier("ic_wifi", "drawable", "com.package")).thenReturn(1);
        Drawable drawable = Mockito.mock(Drawable.class);
        Mockito.when(iconImageViewMock.getContext().getDrawable(1)).thenReturn(drawable);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
        Mockito.doReturn(params).when(itemView).getLayoutParams();

        AmenityViewHolder viewHolder = new AmenityViewHolder(itemView);
        viewHolder.bind(amenity, false);

        Mockito.verify(iconImageViewMock).setImageDrawable(drawable);
        Mockito.verify(titleTextViewMock).setVisibility(View.GONE);
        Mockito.verify(titleTextViewMock, Mockito.never()).setText(amenity.getDescription());
        Assert.assertEquals(LinearLayout.LayoutParams.WRAP_CONTENT, params.width);
    }
}