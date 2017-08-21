package com.extremeplayer.interrupt.homeactivities;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.extremeplayer.interrupt.R;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ShimmerFrameLayout shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmerAnimation();
    }
}
