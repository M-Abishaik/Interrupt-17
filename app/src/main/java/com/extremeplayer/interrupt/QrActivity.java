package com.extremeplayer.interrupt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.extremeplayer.interrupt.UserInfo.userDbHelper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QrActivity extends AppCompatActivity {

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
        mUserDbHelper = new userDbHelper(getApplicationContext());

        imageView = (ImageView) findViewById(R.id.qr);

        mUserDbHelper.updateEvents(eventsList, userMail);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(userName + ", " + userMail + ", " + userNum + ", " + eventsList, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        Toast.makeText(QrActivity.this, "Please take a screenshot of this.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(QrActivity.this, MainActivity.class);
        startActivity(intent);
    }
}