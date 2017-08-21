package com.extremeplayer.interrupt;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.extremeplayer.interrupt.UserInfo.UserContract;
import com.extremeplayer.interrupt.UserInfo.userDbHelper;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SignupActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;
    String userChoosenTask;
    Bitmap bm = null;
    Bitmap resized = null;
    ShimmerTextView loginLink;
    Shimmer shimmer;
    private EditText nameText;
    private EditText addressText;
    private EditText emailText;
    private EditText mobNumText;
    private EditText passText;
    private EditText rePasText;
    private EditText collegeNameText;
    private Button createAccount;
    private userDbHelper mUserDbHelper;
    private ImageView imageview;
    private int flag = 0;
    private Uri imageUri;
    private String name;
    private int ultimateFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rounded_layout);

        nameText = (EditText) findViewById(R.id.input_name);
        addressText = (EditText) findViewById(R.id.input_address);
        emailText = (EditText) findViewById(R.id.input_email);
        mobNumText = (EditText) findViewById(R.id.input_mobile);
        passText = (EditText) findViewById(R.id.input_password);
        rePasText = (EditText) findViewById(R.id.input_reEnterPassword);
        collegeNameText = (EditText) findViewById(R.id.input_collegeName);
        loginLink = (ShimmerTextView) findViewById(R.id.link_login);
        createAccount = (Button) findViewById(R.id.btn_signup);
        imageview = (ImageView) findViewById(R.id.user_profile_photo);
        mUserDbHelper = new userDbHelper(getApplicationContext());

        shimmer = new Shimmer();
        shimmer.start(loginLink);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.animator.push_left_in, R.animator.push_left_out);
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel", "Remove Photo"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(SignupActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (items[item].equals("Remove Photo")) {
                    removeImage();
                }
            }
        });
        builder.show();
    }

    private void removeImage() {
        if (flag == 1) {
            imageview.setImageResource(R.mipmap.ic_launcher);
            flag = 0;
            ultimateFlag = 0;
        } else {
            Toast.makeText(SignupActivity.this, "Please set a photo first.", Toast.LENGTH_SHORT).show();
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent;
        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Gallery"))
                        galleryIntent();
                } else {
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                imageUri = data.getData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        resized = Bitmap.createScaledBitmap(bm, (int) (bm.getWidth() * 0.8), (int) (bm.getHeight() * 0.8), true);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Uploading Pic...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        imageview.setImageBitmap(resized);
                        progressDialog.dismiss();
                    }
                }, 3000);
        ultimateFlag = 1;
        flag = 1;
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageUri = data.getData();

        resized = Bitmap.createScaledBitmap(thumbnail, (int) (thumbnail.getWidth() * 0.8), (int) (thumbnail.getHeight() * 0.8), true);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Uploading Pic...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        imageview.setImageBitmap(resized);
                        progressDialog.dismiss();
                    }
                }, 3000);
        ultimateFlag = 1;
        flag = 1;
    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }

        createAccount.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        name = nameText.getText().toString();
        String address = addressText.getText().toString();
        final String email = emailText.getText().toString();
        final String mobile = mobNumText.getText().toString();
        String password = passText.getText().toString();
        String collegeName = collegeNameText.getText().toString();

        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_USER_NAME, name);
        if (ultimateFlag == 1)
            values.put(UserContract.UserEntry.COLUMN_USER_IMAGE_URI, imageUri.toString());
        else
            values.put(UserContract.UserEntry.COLUMN_USER_IMAGE_URI, "");

        values.put(UserContract.UserEntry.COLUMN_USER_MAIL, email);
        values.put(UserContract.UserEntry.COLUMN_USER_PASSWORD, password);
        values.put(UserContract.UserEntry.COLUMN_USER_MOBILE_NUMBER, mobile);
        values.put(UserContract.UserEntry.COLUMN_USER_ADDRESS, address);
        values.put(UserContract.UserEntry.COLUMN_COLLEGE_NAME, collegeName);
        values.put(UserContract.UserEntry.COLUMN_USER_EVENTS, "Nil");

        getContentResolver().insert(UserContract.UserEntry.CONTENT_URI, values);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onSignupSuccess();
                        progressDialog.dismiss();
                        Intent i = new Intent(SignupActivity.this, MainActivity.class);
                        i.putExtra("username", name);
                        i.putExtra("usermail", email);
                        i.putExtra("usernum", mobile);
                        if (ultimateFlag == 1) {
                            i.putExtra("userphoto", imageUri.toString());
                        } else {
                            i.putExtra("userphoto", "null");
                        }
                        startActivity(i);
                    }
                }, 2000);
    }

    public void onSignupSuccess() {
        createAccount.setEnabled(true);
        setResult(RESULT_OK, null);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
        createAccount.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(i);
    }


    public boolean validate() {
        boolean valid = true;
        String result;

        String name = nameText.getText().toString();
        String address = addressText.getText().toString();
        String email = emailText.getText().toString();
        String mobile = mobNumText.getText().toString();
        String password = passText.getText().toString();
        String reEnterPassword = rePasText.getText().toString();

        result = mUserDbHelper.verifyRedundancy(email);

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (address.isEmpty()) {
            addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            addressText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else if (result.equals("found")) {
            emailText.setError("E-mail id exists! Please choose a new one.");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            mobNumText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            mobNumText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            rePasText.setError("Password Do not match");
            valid = false;
        } else {
            rePasText.setError(null);
        }

        return valid;
    }
}

class Utility {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}

