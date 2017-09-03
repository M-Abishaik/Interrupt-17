package com.extremeplayer.interrupt.navbarfragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.extremeplayer.interrupt.MainActivity;
import com.extremeplayer.interrupt.MyInterface;

import com.extremeplayer.interrupt.R;
import com.extremeplayer.interrupt.UpdateActivity;
import com.extremeplayer.interrupt.UserInfo.UserInfoList;

/**
 * Created by Abishaik on 03-09-2017.
 */

public class PostLogin extends Fragment implements MyInterface {

    private String userMail;
    private UserInfoList mUserInfoList = new UserInfoList();
    private TextView userNameText;
    private TextView userMailText;
    private TextView mobileNumText;
    private TextView collegeText;
    private TextView eventsText;
    private FloatingActionButton floatingActionButton;

        public PostLogin(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            String userMail = getArguments().getString("email");

            View v = inflater.inflate(R.layout.activity_post_login, container, false);

            mUserInfoList.userDetail(getActivity().getApplicationContext(), userMail);
            userNameText = (TextView)v.findViewById(R.id.UsernameInput);
            userMailText = (TextView)v.findViewById(R.id.mailInput);
            mobileNumText = (TextView)v.findViewById(R.id.mobileInput);
            collegeText = (TextView)v.findViewById(R.id.collegeInput);
            eventsText = (TextView)v.findViewById(R.id.eventsInput);
            floatingActionButton = (FloatingActionButton)v.findViewById(R.id.fab);
            bindDetails();

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), UpdateActivity.class);
                    i.putExtra("name", mUserInfoList.getmUserName());
                    i.putExtra("email", mUserInfoList.getmUserMail());
                    i.putExtra("usernum", mUserInfoList.getmUserMobileNum());
                    startActivity(i);
                }
            });
        return v;
    }

    private void bindDetails() {
        userNameText.setText(mUserInfoList.getmUserName());
        userMailText.setText(mUserInfoList.getmUserMail());
        mobileNumText.setText(mUserInfoList.getmUserMobileNum());
        collegeText.setText(mUserInfoList.getmUserCollegeName());
        eventsText.setText(mUserInfoList.getmUserEvents());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.dummy_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.SignOut:
                i = new Intent(getActivity(), MainActivity.class);
                i.putExtra("username", "anonymous");
                startActivity(i);
                return true;

            /*case R.id.directToHome:
                attachIntent();
                return true;*/
        }
        return super.onOptionsItemSelected(item);
    }

    /*private void attachIntent() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.putExtra("username", mUserInfoList.getmUserName());
        i.putExtra("usermail", mUserInfoList.getmUserMail());
        i.putExtra("usernum", mUserInfoList.getmUserMobileNum());
        startActivity(i);
    }*/

    @Override
    public void fragmentNowVisible() {
        Log.d("Debug", "PostLogin visible");
    }
}
