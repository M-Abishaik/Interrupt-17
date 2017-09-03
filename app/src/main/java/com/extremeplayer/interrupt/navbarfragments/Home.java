package com.extremeplayer.interrupt.navbarfragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.extremeplayer.interrupt.MyInterface;
import com.extremeplayer.interrupt.R;

public class Home extends Fragment implements MyInterface {

    TextView userName;
    ImageView userImage;

    public Home() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String username = getArguments().getString("uname");

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        userName = (TextView) v.findViewById(R.id.username);
        userImage = (ImageView) v.findViewById(R.id.profilePic);

        try {
            if (username.equals("anonymous")) {
                userName.setText("");
            } else {
                userName.setText(username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void fragmentNowVisible() {
        Log.d("Debug", "Home visible");
    }

}
