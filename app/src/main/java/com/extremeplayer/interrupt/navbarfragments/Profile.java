package com.extremeplayer.interrupt.navbarfragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
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

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.extremeplayer.interrupt.MainActivity;
import com.extremeplayer.interrupt.UserInfo.UserContract;
import com.extremeplayer.interrupt.app.AppController;
import com.extremeplayer.interrupt.app.AppConfig;
import com.extremeplayer.interrupt.MyInterface;
import com.extremeplayer.interrupt.R;
import com.extremeplayer.interrupt.SignupActivity;
import com.extremeplayer.interrupt.UserInfo.userDbHelper;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Profile extends Fragment implements MyInterface {

    private static final String TAG = MainActivity.class.getSimpleName();
    ShimmerTextView signupLink;
    Shimmer shimmer;
    ProgressDialog progressDialog;
    StringBuffer sb = new StringBuffer("");
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

        emailText.setText("");
        passText.setText("");

        loginButton = (Button) rootView.findViewById(R.id.btn_login);
        signupLink = (ShimmerTextView) rootView.findViewById(R.id.link_signup);
        shimmer = new Shimmer();
        shimmer.start(signupLink);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        mUserDbHelper = new userDbHelper(getActivity());

        try {
            if (!isConnected()) {
                Toast.makeText(getActivity(), "Please connect to the Internet.", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException f) {

        } catch (IOException e) {

        }


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

        if (!validate()) {
            onLoginFailed();
            return;
        }


        final String emailInput = emailText.getText().toString().trim();
        final String passInput = passText.getText().toString().trim();

        checkLogin(emailInput, passInput);
    }

    public boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec(command).waitFor() == 0);
    }

    public void onLoginFailed() {
        Toast.makeText(getActivity(), "Please enter valid credentials.", Toast.LENGTH_SHORT).show();
        emailText.setText("");
        passText.setText("");
        loginButton.setEnabled(true);
    }

    private void checkLogin(final String email, final String password) {
        String tag_string_req = "req_login";

        progressDialog.setMessage("Authenticating ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        JSONObject user = jObj.getJSONObject("user");
                        JSONObject eventslist = jObj.getJSONObject("eventslist");

                        String name = user.getString("name");
                        String email = user.getString("email");
                        String password = user.getString("password");
                        String mobile = user.getString("phoneNumber");
                        String collegeName = user.getString("collegeName");

                        String codeninja = eventslist.getString("codeninja");
                        String codesprint = eventslist.getString("codesprint");
                        String cognitionquest = eventslist.getString("cognitionquest");
                        String datadequeue = eventslist.getString("datadequeue");
                        String donoflogic = eventslist.getString("donoflogic");
                        String gameofarchives = eventslist.getString("gameofarchives");
                        String myb = eventslist.getString("myb");
                        String picturesque = eventslist.getString("picturesque");

                        ContentValues values = new ContentValues();
                        values.put(UserContract.UserEntry.COLUMN_USER_NAME, name);
                        values.put(UserContract.UserEntry.COLUMN_USER_MAIL, email);
                        values.put(UserContract.UserEntry.COLUMN_USER_PASSWORD, password);
                        values.put(UserContract.UserEntry.COLUMN_USER_MOBILE_NUMBER, mobile);
                        values.put(UserContract.UserEntry.COLUMN_COLLEGE_NAME, collegeName);

                        if (codeninja.equals("F") && codesprint.equals("F") && cognitionquest.equals("F") && datadequeue.equals("F")
                                && donoflogic.equals("F") && gameofarchives.equals("F") && myb.equals("F") && picturesque.equals("F")) {
                            values.put(UserContract.UserEntry.COLUMN_USER_EVENTS, "Nil");
                        } else {
                            if (codeninja.equals("T")) {
                                sb.append("Code Ninja" + ", ");
                            }
                            if (codesprint.equals("T")) {
                                sb.append("Code Sprint" + ", ");
                            }
                            if (cognitionquest.equals("T")) {
                                sb.append("Cognition Quest" + ", ");
                            }
                            if (datadequeue.equals("T")) {
                                sb.append("Data Dequeue" + ", ");
                            }
                            if (donoflogic.equals("T")) {
                                sb.append("Don of Logic" + ", ");
                            }
                            if (gameofarchives.equals("T")) {
                                sb.append("Game of Archives" + ", ");
                            }
                            if (myb.equals("T")) {
                                sb.append("Myb" + ", ");
                            }
                            if (picturesque.equals("T")) {
                                sb.append("Picturesque" + ", ");
                            }
                            sb.deleteCharAt(sb.length() - 2);
                            values.put(UserContract.UserEntry.COLUMN_USER_EVENTS, sb.toString());
                        }


                        getActivity().getContentResolver().insert(UserContract.UserEntry.CONTENT_URI, values);

                        Intent intent = new Intent(getActivity(),
                                MainActivity.class);
                        intent.putExtra("usermail", email);
                        intent.putExtra("username", name);
                        intent.putExtra("usernum", mobile);
                        startActivity(intent);
                    } else {
                        emailText.setText("");
                        passText.setText("");
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        "Sorry! Please try again later.", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
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
