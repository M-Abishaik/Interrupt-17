package com.extremeplayer.interrupt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.extremeplayer.interrupt.UserInfo.userDbHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class UpdateActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;
    String name, email, mobile;
    private int flag;
    private String userChoosenTask;
    private String userName;
    private String userMail;
    private String userImage;
    private String userNum;
    private EditText nameText;
    private EditText addressText;
    private EditText emailText;
    private EditText mobNumText;
    private EditText passText;
    private EditText rePasText;
    private EditText collegeNameText;
    private Button updateAccount;
    private ImageView imageview;
    private Bitmap resized = null, bm = null;
    private userDbHelper mUserDbHelper;
    private Uri imageUri;
    private int ultimateFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Bundle userInfo = getIntent().getExtras();
        if (userInfo == null)
            return;
        else {
            userName = userInfo.getString("name");
            userMail = userInfo.getString("email");
            userImage = userInfo.getString("image");
            userNum = userInfo.getString("usernum");
            ultimateFlag = 2;
        }

        Toast.makeText(UpdateActivity.this, "Update the details wherever necessary.", Toast.LENGTH_SHORT).show();

        nameText = (EditText) findViewById(R.id.input_name);
        addressText = (EditText) findViewById(R.id.input_address);
        emailText = (EditText) findViewById(R.id.input_email);
        mobNumText = (EditText) findViewById(R.id.input_mobile);
        passText = (EditText) findViewById(R.id.input_password);
        rePasText = (EditText) findViewById(R.id.input_reEnterPassword);
        collegeNameText = (EditText) findViewById(R.id.input_collegeName);
        updateAccount = (Button) findViewById(R.id.btn_update);
        imageview = (ImageView) findViewById(R.id.user_profile_photo);
        mUserDbHelper = new userDbHelper(getApplicationContext());

        nameText.setText(userName);
        emailText.setText(userMail);
        emailText.setEnabled(false);

        Uri imageUri = Uri.parse(userImage);
        if (imageUri == null) {
            imageview.setImageResource(R.mipmap.ic_launcher);
        } else {
            try {
                boolean result = Utility.checkPermission(UpdateActivity.this);
                if (result) {
                    InputStream image_stream = getContentResolver().openInputStream(imageUri);
                    Bitmap bm = BitmapFactory.decodeStream(image_stream);
                    resized = Bitmap.createScaledBitmap(bm, (int) (bm.getWidth() * 0.8), (int) (bm.getHeight() * 0.8), true);
                    imageview.setImageBitmap(resized);
                    flag = 1;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        updateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel", "Remove Photo"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(UpdateActivity.this);
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
            Toast.makeText(UpdateActivity.this, "Please set a photo first.", Toast.LENGTH_SHORT).show();
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent;
        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

        final ProgressDialog progressDialog = new ProgressDialog(UpdateActivity.this,
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

        final ProgressDialog progressDialog = new ProgressDialog(UpdateActivity.this,
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

    private void update() {
        if (!validate()) {
            onUpdateFailed();
            return;
        }

        updateAccount.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(UpdateActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating Account...");
        progressDialog.show();

        name = nameText.getText().toString();
        String address = addressText.getText().toString();
        email = emailText.getText().toString();
        mobile = mobNumText.getText().toString();
        String password = passText.getText().toString();
        String collegeName = collegeNameText.getText().toString();

        if (ultimateFlag == 1) {
            mUserDbHelper.updateDetails(name, imageUri.toString(), address, email, mobile, password, collegeName);
        } else if (ultimateFlag == 2) {
            mUserDbHelper.updateDetails(name, userImage, address, email, mobile, password, collegeName);
        } else {
            mUserDbHelper.updateDetails(name, null, address, email, mobile, password, collegeName);
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onUpdateSuccess();
                        progressDialog.dismiss();
                        attachIntent(0);
                    }
                }, 2000);
    }

    public void onUpdateSuccess() {
        Toast.makeText(UpdateActivity.this, "Successfully updated.", Toast.LENGTH_SHORT).show();
        updateAccount.setEnabled(true);
    }

    private void onUpdateFailed() {
        Toast.makeText(UpdateActivity.this, "Update failed.Please enter valid credentials.", Toast.LENGTH_SHORT).show();
        updateAccount.setEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dummy_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.SignOut:
                Intent i = new Intent(UpdateActivity.this, MainActivity.class);
                i.putExtra("username", "anonymous");
                i.putExtra("userphoto", "null");
                finish();
                startActivity(i);
                return true;
            case R.id.directToHome:
                attachIntent(1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setTitle("Stay Interrupted!!");
        builder.setMessage("Do you really want to exit the app?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(UpdateActivity.this, "Never Mind!", Toast.LENGTH_SHORT).show();
                moveTaskToBack(true);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(UpdateActivity.this, "Thank You!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void attachIntent(int flag) {
        Intent i = new Intent(UpdateActivity.this, MainActivity.class);
        if (flag == 0) {
            i.putExtra("username", name);
            i.putExtra("usermail", email);
            i.putExtra("usernum", mobile);
            if (ultimateFlag == 1) {
                i.putExtra("userphoto", imageUri.toString());
            } else if (ultimateFlag == 2) {
                i.putExtra("userphoto", userImage);
            } else {
                i.putExtra("userphoto", "null");
            }
        } else if (flag == 1) {
            i.putExtra("username", userName);
            i.putExtra("userphoto", userImage);
            i.putExtra("usermail", userMail);
            i.putExtra("usernum", userNum);
            finish();
        }
        startActivity(i);
    }

    public boolean validate() {
        boolean valid = true;

        name = nameText.getText().toString();
        String address = addressText.getText().toString();
        String email = emailText.getText().toString();
        String mobile = mobNumText.getText().toString();
        String password = passText.getText().toString();
        String reEnterPassword = rePasText.getText().toString();

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
