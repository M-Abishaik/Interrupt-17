package com.extremeplayer.interrupt.homeactivities;

import android.app.Activity;
import android.os.Bundle;

import com.extremeplayer.interrupt.R;
import com.facebook.shimmer.ShimmerFrameLayout;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ShimmerFrameLayout shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmerAnimation();
    }
}
