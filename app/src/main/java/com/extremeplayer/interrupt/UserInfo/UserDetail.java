package com.extremeplayer.interrupt.UserInfo;


public class UserDetail {

    private String mUserName;
    private String mUserImage;
    private String mUserMail;
    private String mUserPass;
    private String mUserMobileNum;
    private String mUserAddress;
    private String mUserCollegeName;
    private String mUserEvents;

    public UserDetail() {
    }

    public UserDetail(String username, String path, String userMail, String userPass, String userMobileNum, String userAddress, String userCollegeName, String userEvents) {
        this.mUserName = username;
        this.mUserImage = path;
        this.mUserMail = userMail;
        this.mUserPass = userPass;
        this.mUserMobileNum = userMobileNum;
        this.mUserAddress = userAddress;
        this.mUserCollegeName = userCollegeName;
        this.mUserEvents = userEvents;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String userName) {
        mUserName = userName;
    }

    public String getmUserImage() {
        return mUserImage;
    }

    public void setmUserImage(String path) {
        mUserImage = path;
    }

    public String getmUserMail() {
        return mUserMail;
    }

    public void setmUserMail(String userMail) {
        mUserMail = userMail;
    }

    public String getmUserMobileNum() {
        return mUserMobileNum;
    }

    public void setmUserMobileNum(String number) {
        mUserMobileNum = number;
    }

    public String getmUserAddress() {
        return mUserAddress;
    }

    public void setmUserAddress(String userAddress) {
        mUserAddress = userAddress;
    }

    public String getmUserCollegeName() {
        return mUserCollegeName;
    }

    public void setmUserCollegeName(String userCollegeName) {
        mUserCollegeName = userCollegeName;
    }

    public String getmUserEvents() {
        return mUserEvents;
    }

    public void setmUserEvents(String userEvents) {
        mUserEvents = userEvents;
    }
}
