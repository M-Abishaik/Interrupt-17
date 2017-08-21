package com.extremeplayer.interrupt.navbarfragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.extremeplayer.interrupt.MyInterface;
import com.extremeplayer.interrupt.PostLoginActivity;
import com.extremeplayer.interrupt.R;
import com.extremeplayer.interrupt.SignupActivity;
import com.extremeplayer.interrupt.UserInfo.userDbHelper;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

public class Profile extends Fragment implements MyInterface {

    ShimmerTextView signupLink;
    Shimmer shimmer;
    private EditText emailText;
    private EditText passText;
    private Button loginButton;
    private userDbHelper mUserDbHelper;


    public Profile() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_login, container, false);

        emailText = (EditText) rootView.findViewById(R.id.input_email);
        passText = (EditText) rootView.findViewById(R.id.input_password);
        loginButton = (Button) rootView.findViewById(R.id.btn_login);
        signupLink = (ShimmerTextView) rootView.findViewById(R.id.link_signup);
        shimmer = new Shimmer();
        shimmer.start(signupLink);
        mUserDbHelper = new userDbHelper(getActivity());

        emailText.setText("");
        passText.setText("");

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SignupActivity.class);
                startActivity(i);
            }
        });


        return rootView;

    }

    private void login() {
        String result;

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String emailInput = emailText.getText().toString();
        final String passInput = passText.getText().toString();


        result = mUserDbHelper.verifyAuthenticity(emailInput, passInput);

        if (result.equals("found")) {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onLoginSuccess();
                            progressDialog.dismiss();
                            Intent i = new Intent(getActivity(), PostLoginActivity.class);
                            i.putExtra("email", emailInput);
                            i.putExtra("password", passInput);
                            startActivity(i);
                        }
                    }, 2000);
        } else {
            onLoginFailed();
            progressDialog.dismiss();
        }
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        getActivity().finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getActivity(), "Please enter valid credentials.", Toast.LENGTH_SHORT).show();
        emailText.setText("");
        passText.setText("");
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passText.setError(null);
        }
        return valid;
    }

    @Override
    public void fragmentNowVisible() {
        Log.d("Debug", "Profile visible");
    }

}
