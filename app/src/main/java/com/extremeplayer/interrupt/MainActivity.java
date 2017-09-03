package com.extremeplayer.interrupt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.extremeplayer.interrupt.dashactivities.EventActivity;
import com.extremeplayer.interrupt.dashactivities.MapActivity;
import com.extremeplayer.interrupt.dashactivities.TransportActivity;
import com.extremeplayer.interrupt.homeactivities.AboutActivity;
import com.extremeplayer.interrupt.navbarfragments.Chat;
import com.extremeplayer.interrupt.navbarfragments.Dashboard;
import com.extremeplayer.interrupt.navbarfragments.Home;
import com.extremeplayer.interrupt.navbarfragments.PostLogin;
import com.extremeplayer.interrupt.navbarfragments.Profile;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends AppCompatActivity {

    static String name = "anonymous";
    static String mailId = "null";
    static String phoneNum = "null";
    NavigationTabBar navigationTabBar;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    ArrayList<NavigationTabBar.Model> barModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle userData = getIntent().getExtras();
        if (userData == null) {
        } else {
            name = userData.getString("username");
            mailId = userData.getString("usermail");
            phoneNum = userData.getString("usernum");
        }
        initNavBar();
    }

    public void initNavBar() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        navigationTabBar = (NavigationTabBar) findViewById(R.id.nav_tb);

        barModel = new ArrayList<>();

        barModel.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_home_black_24dp),
                        R.color.primary_dark)
                        .title("Home")
                        .badgeTitle("NTB HOME")
                        .build()
        );

        barModel.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_dashboard_black_24dp),
                        R.color.primary_dark)
                        .title("Dashboard")
                        .badgeTitle("NTB DASH")
                        .build()
        );

        barModel.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_chat_black_24dp),
                        R.color.primary_dark).
                        title("Chat")
                        .badgeTitle("NTB CHAT")
                        .build()
        );

        barModel.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_person_black_24dp),
                        R.color.primary_dark).
                        title("Profile")
                        .badgeTitle("NTB PROF")
                        .build()
        );

        navigationTabBar.setModels(barModel);
        navigationTabBar.setViewPager(viewPager, 0);

        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                MyInterface fragment = (MyInterface) pagerAdapter.instantiateItem(viewPager, position);
                navigationTabBar.getModels().get(position).hideBadge();
                fragment.fragmentNowVisible();
                Log.d("Debug", String.valueOf(position));

            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }

        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Stay Interrupted!!");
        builder.setMessage("Do you really want to exit the app?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Never Mind!", Toast.LENGTH_SHORT).show();
                moveTaskToBack(true);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Thank You!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }


    public void openAbout(View view) {
        Log.d("Debug", "About visible");

        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);

    }

    public void openContact(View view) {
        Log.d("Debug", "Contact visible");

    }

    public void openEvent(View view) {
        Log.d("Debug", "Events visible");

        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
    }

    public void openMap(View view) {
        Log.d("Debug", "Maps visible");

        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void openTransport(View view) {
        Log.d("Debug", "Transport visible");

        Intent intent = new Intent(this, TransportActivity.class);
        startActivity(intent);
    }

    private static class MyPagerAdapter extends FragmentPagerAdapter {

        Bundle bundle = new Bundle();

        private MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    Home fragobj1 = new Home();
                    bundle.putString("uname", name);
                    fragobj1.setArguments(bundle);
                    return fragobj1;
                case 1:
                    Dashboard fragobj2 = new Dashboard();
                    bundle.putString("uname", name);
                    bundle.putString("umail", mailId);
                    bundle.putString("unum", phoneNum);
                    fragobj2.setArguments(bundle);
                    return fragobj2;
                case 2:
                    return new Chat();
                case 3:
                    try {
                        if (name.equals("anonymous")) {
                            return new Profile();
                        } else {
                            PostLogin fragobj3 = new PostLogin();
                            bundle.putString("email", mailId);
                            fragobj3.setArguments(bundle);
                            return fragobj3;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

    }
}