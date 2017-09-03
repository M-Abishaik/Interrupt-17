package com.extremeplayer.interrupt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request.Method;
import com.extremeplayer.interrupt.UserInfo.userDbHelper;
import com.extremeplayer.interrupt.app.AppConfig;
import com.extremeplayer.interrupt.app.AppController;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class QrActivity extends AppCompatActivity {

    private static final String TAG = QrActivity.class.getSimpleName();
    String eventsList, userName, userMail, userNum;
    ImageView imageView;
    private userDbHelper mUserDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle list = getIntent().getExtras();
        if (list == null) {
        } else {
            eventsList = list.getString("eventslist");
            userName = list.getString("name");
            userMail = list.getString("usermail");
            userNum = list.getString("usernum");
        }
        setContentView(R.layout.activity_qr);
        System.out.println(eventsList);
        mUserDbHelper = new userDbHelper(getApplicationContext());

        imageView = (ImageView) findViewById(R.id.qr);

        updateEvents(eventsList, userMail);

        /*MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(userName + ", " + userMail + ", " + userNum + ", " + eventsList, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        Toast.makeText(QrActivity.this, "Please take a screenshot of this.", Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(QrActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void updateEvents(final String eventsList, final String email) {
        String tag_string_req = "update_events";

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_EVENTS_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(QrActivity.this, "Events registered.", Toast.LENGTH_SHORT).show();
                        mUserDbHelper.updateEvents(eventsList, email);
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Events update Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("eventslist", eventsList);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}