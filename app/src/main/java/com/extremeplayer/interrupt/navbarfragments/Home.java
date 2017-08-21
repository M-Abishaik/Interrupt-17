package com.extremeplayer.interrupt.navbarfragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.extremeplayer.interrupt.UpdateActivity;

import java.io.IOException;
import java.io.InputStream;

public class Home extends Fragment implements MyInterface {

    TextView userName;
    ImageView userImage;

    public Home() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String username = getArguments().getString("uname");
        String photoUrl = getArguments().getString("userphoto");

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        userName = (TextView) v.findViewById(R.id.username);
        userImage = (ImageView) v.findViewById(R.id.profilePic);

        try {
            if (photoUrl.equals("null")) {
                userImage.setImageResource(R.drawable.ic_account_circle_black_24dp);
            } else {
                Uri imageUri = Uri.parse(photoUrl);
                try {
                    //boolean result = getActivity().Utility.checkPermission(getActivity());
                    InputStream image_stream = getActivity().getContentResolver().openInputStream(imageUri);
                    Bitmap bm = BitmapFactory.decodeStream(image_stream);
                    Bitmap resized = Bitmap.createScaledBitmap(bm, (int) (bm.getWidth() * 0.8), (int) (bm.getHeight() * 0.8), true);
                    userImage.setImageBitmap(resized);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
