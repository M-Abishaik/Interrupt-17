package com.extremeplayer.interrupt.dashactivities.dashfragments.events;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.extremeplayer.interrupt.R;


public class Picturesque extends Fragment {


    public Picturesque() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_picturesque, container, false);
    }

}
