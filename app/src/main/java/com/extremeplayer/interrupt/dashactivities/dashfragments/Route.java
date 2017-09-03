package com.extremeplayer.interrupt.dashactivities.dashfragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.extremeplayer.interrupt.R;


public class Route extends Fragment {


    public Route() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_route, container, false);

        WebView browser = (WebView) view.findViewById(R.id.webview);
        browser.loadUrl("https://www.svce.ac.in/departments/transport/routes.php#nav");
        return view;
    }

}
