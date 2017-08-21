package com.extremeplayer.interrupt.navbarfragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.extremeplayer.interrupt.MyInterface;
import com.extremeplayer.interrupt.R;
import com.extremeplayer.interrupt.RegisterActivity;
import com.extremeplayer.interrupt.dashactivities.EventActivity;

public class Dashboard extends Fragment implements MyInterface {

    ImageButton button1, button2, button3, button4, button5;
    Button registerButton;
    String username, usermail, usernum;
    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            Intent intent;

            switch (v.getId()) {
                case R.id.event1:
                    intent = new Intent(getActivity(), EventActivity.class);
                    intent.putExtra("event", "code ninja");
                    startActivity(intent);
                    break;
                case R.id.event2:
                    intent = new Intent(getActivity(), EventActivity.class);
                    intent.putExtra("event", "data de-queue");
                    startActivity(intent);
                    break;
                case R.id.event3:
                    intent = new Intent(getActivity(), EventActivity.class);
                    intent.putExtra("event", "quizzler");
                    startActivity(intent);
                    break;
                case R.id.event4:
                    intent = new Intent(getActivity(), EventActivity.class);
                    intent.putExtra("event", "cognition quest");
                    startActivity(intent);
                    break;
                case R.id.event5:
                    intent = new Intent(getActivity(), EventActivity.class);
                    intent.putExtra("event", "don of logic");
                    startActivity(intent);
                    break;
                case R.id.register:
                    try {
                        if (username.equals("anonymous")) {
                            Toast.makeText(getActivity(), "Please login to register.", Toast.LENGTH_SHORT).show();
                        } else {
                            intent = new Intent(getActivity(), RegisterActivity.class);
                            intent.putExtra("name", username);
                            intent.putExtra("usermail", usermail);
                            intent.putExtra("usernum", usernum);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Please login to register.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    public Dashboard() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        username = getArguments().getString("uname");
        usermail = getArguments().getString("umail");
        usernum = getArguments().getString("unum");

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);


        button1 = (ImageButton) view.findViewById(R.id.event1);
        button2 = (ImageButton) view.findViewById(R.id.event2);
        button3 = (ImageButton) view.findViewById(R.id.event3);
        button4 = (ImageButton) view.findViewById(R.id.event4);
        button5 = (ImageButton) view.findViewById(R.id.event5);
        registerButton = (Button) view.findViewById(R.id.register);

        button1.setOnClickListener(onClickListener);
        button2.setOnClickListener(onClickListener);
        button3.setOnClickListener(onClickListener);
        button4.setOnClickListener(onClickListener);
        button5.setOnClickListener(onClickListener);
        registerButton.setOnClickListener(onClickListener);

        return view;
    }

    @Override
    public void fragmentNowVisible() {
        Log.d("Debug", "Dashboard visible");
    }

}
