package com.extremeplayer.interrupt.UserInfo;

import android.content.Context;

import java.util.ArrayList;

public class UserInfoList {

    ArrayList<UserDetail> mArrayList = new ArrayList<UserDetail>();
    userDbHelper mUserDbHelper;

    public String getmUserName() {
        return mArrayList.get(0).getmUserName();
    }

    public String getmUserImage() {
        return mArrayList.get(0).getmUserImage();
    }

    public String getmUserMail() {
        return mArrayList.get(0).getmUserMail();
    }

    public String getmUserMobileNum() {
        return mArrayList.get(0).getmUserMobileNum();
    }

    public String getmUserAddress() {
        return mArrayList.get(0).getmUserAddress();
    }

    public String getmUserCollegeName() {
        return mArrayList.get(0).getmUserCollegeName();
    }

    public String getmUserEvents() {
        return mArrayList.get(0).getmUserEvents();
    }


    public void userDetail(Context context, String mail, String pass) {
        mUserDbHelper = new userDbHelper(context);
        mArrayList = mUserDbHelper.getAllDetails(mail, pass);
    }
}
