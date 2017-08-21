package com.extremeplayer.interrupt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.extremeplayer.interrupt.UserInfo.UserInfoList;

import java.io.IOException;
import java.io.InputStream;

public class PostLoginActivity extends AppCompatActivity {
    private String userMail;
    private String userPass;
    private UserInfoList mUserInfoList = new UserInfoList();
    private TextView userNameText;
    private TextView userMailText;
    private TextView mobileNumText;
    private TextView addressText;
    private TextView collegeText;
    private TextView eventsText;
    private FloatingActionButton floatingActionButton;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);

        Bundle userInfo = getIntent().getExtras();
        if (userInfo == null)
            return;
        else {
            userMail = userInfo.getString("email");
            userPass = userInfo.getString("password");
        }

        mUserInfoList.userDetail(getApplicationContext(), userMail, userPass);
        userNameText = (TextView) findViewById(R.id.UsernameInput);
        userMailText = (TextView) findViewById(R.id.mailInput);
        mobileNumText = (TextView) findViewById(R.id.mobileInput);
        addressText = (TextView) findViewById(R.id.addressInput);
        collegeText = (TextView) findViewById(R.id.collegeInput);
        eventsText = (TextView) findViewById(R.id.eventsInput);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        imageView = (ImageView) findViewById(R.id.user_profile_photo);
        bindDetails();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostLoginActivity.this, UpdateActivity.class);
                i.putExtra("name", mUserInfoList.getmUserName());
                i.putExtra("email", mUserInfoList.getmUserMail());
                i.putExtra("image", mUserInfoList.getmUserImage());
                i.putExtra("usernum", mUserInfoList.getmUserMobileNum());
                finish();
                startActivity(i);
            }
        });
    }

    private void bindDetails() {
        userNameText.setText(mUserInfoList.getmUserName());
        userMailText.setText(mUserInfoList.getmUserMail());
        mobileNumText.setText(mUserInfoList.getmUserMobileNum());
        addressText.setText(mUserInfoList.getmUserAddress());
        collegeText.setText(mUserInfoList.getmUserCollegeName());
        eventsText.setText(mUserInfoList.getmUserEvents());

        Uri imageUri = Uri.parse(mUserInfoList.getmUserImage());
        if (imageUri == null) {
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            try {
                boolean result = Utility.checkPermission(PostLoginActivity.this);
                if (result) {
                    InputStream image_stream = getContentResolver().openInputStream(imageUri);
                    Bitmap bm = BitmapFactory.decodeStream(image_stream);
                    Bitmap resized = Bitmap.createScaledBitmap(bm, (int) (bm.getWidth() * 0.8), (int) (bm.getHeight() * 0.8), true);
                    imageView.setImageBitmap(resized);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        attachIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dummy_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.SignOut:
                i = new Intent(PostLoginActivity.this, MainActivity.class);
                i.putExtra("username", "anonymous");
                i.putExtra("userphoto", "null");
                finish();
                startActivity(i);
                return true;

            case R.id.directToHome:
                attachIntent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void attachIntent() {
        Intent i = new Intent(PostLoginActivity.this, MainActivity.class);
        i.putExtra("username", mUserInfoList.getmUserName());
        i.putExtra("userphoto", mUserInfoList.getmUserImage());
        i.putExtra("usermail", mUserInfoList.getmUserMail());
        i.putExtra("usernum", mUserInfoList.getmUserMobileNum());
        finish();
        startActivity(i);
    }
}
