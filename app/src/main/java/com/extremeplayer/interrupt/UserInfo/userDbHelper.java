package com.extremeplayer.interrupt.UserInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class userDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "userDetails.db";
    private static final int DATABASE_VERSION = 4;

    public userDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserContract.UserEntry.TABLE_NAME + " ("
                + UserContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserContract.UserEntry.COLUMN_USER_NAME + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_USER_IMAGE_URI + " STRING, "
                + UserContract.UserEntry.COLUMN_USER_MAIL + " VARCHAR(50) NOT NULL, "
                + UserContract.UserEntry.COLUMN_USER_PASSWORD + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_USER_MOBILE_NUMBER + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_USER_ADDRESS + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_COLLEGE_NAME + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_USER_EVENTS + " TEXT DEFAULT '');";

        db.execSQL(SQL_CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME);
        onCreate(db);
    }

    public String verifyAuthenticity(String email, String pass) {
        String answer = "not found";
        String getEmail;
        String getPass;

        String selectQuery = "SELECT " + UserContract.UserEntry.COLUMN_USER_MAIL + "," + UserContract.UserEntry.COLUMN_USER_PASSWORD
                + " FROM " + UserContract.UserEntry.TABLE_NAME + ";";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                getEmail = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USER_MAIL));
                getPass = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USER_PASSWORD));

                if (email.equals(getEmail) && pass.equals(getPass)) {
                    answer = "found";
                    break;
                }

            } while (cursor.moveToNext());

        }
        return answer;
    }

    public void updateDetails(String name, String path, String address, String email, String mobile, String password, String collegeName) {
        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = "UPDATE " + UserContract.UserEntry.TABLE_NAME + " SET "
                + UserContract.UserEntry.COLUMN_USER_NAME + " = \'" + name + "\'" + ","
                + UserContract.UserEntry.COLUMN_USER_IMAGE_URI + " = \'" + path + "\'" + ","
                + UserContract.UserEntry.COLUMN_USER_ADDRESS + " = \'" + address + "\'" + ","
                + UserContract.UserEntry.COLUMN_USER_MOBILE_NUMBER + " = \'" + mobile + "\'" + ","
                + UserContract.UserEntry.COLUMN_USER_PASSWORD + " = \'" + password + "\'" + ","
                + UserContract.UserEntry.COLUMN_COLLEGE_NAME + " = \'" + collegeName + "\'"
                + " WHERE " + UserContract.UserEntry.COLUMN_USER_MAIL + " = \'"
                + email + "\'" + ";";

        Cursor c = database.rawQuery(selectQuery, null);
        System.out.println(c.getCount());
    }

    public void updateEvents(String eventsList, String userMail) {
        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = "UPDATE " + UserContract.UserEntry.TABLE_NAME + " SET "
                + UserContract.UserEntry.COLUMN_USER_EVENTS + " = \'" + eventsList + "\'" + " WHERE " + UserContract.UserEntry.COLUMN_USER_MAIL + " = \'"
                + userMail + "\'" + ";";

        Cursor c = database.rawQuery(selectQuery, null);
        System.out.println(c.getCount());
    }

    public String verifyRedundancy(String email) {
        String result = "not found";
        String getEmail;
        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT " + UserContract.UserEntry.COLUMN_USER_MAIL
                + " FROM " + UserContract.UserEntry.TABLE_NAME + ";";

        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                getEmail = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USER_MAIL));
                if (email.equals(getEmail)) {
                    result = "found";
                    break;
                }

            } while (cursor.moveToNext());
        }

        return result;
    }


    public ArrayList<UserDetail> getAllDetails(String mail, String pass) {
        ArrayList<UserDetail> mUserDetail = new ArrayList<UserDetail>();
        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + UserContract.UserEntry.TABLE_NAME + " WHERE " + UserContract.UserEntry.COLUMN_USER_MAIL + " = \'"
                + mail + "\'" + " AND " + UserContract.UserEntry.COLUMN_USER_PASSWORD + " =\'" + pass + "\'" + ";";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UserDetail userDetail = new UserDetail();

                String Name = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USER_NAME));
                userDetail.setmUserName(Name);

                String path = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USER_IMAGE_URI));
                userDetail.setmUserImage(path);

                String Mail = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USER_MAIL));
                userDetail.setmUserMail(Mail);

                String MobNum = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USER_MOBILE_NUMBER));
                userDetail.setmUserMobileNum(MobNum);

                String Address = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USER_ADDRESS));
                userDetail.setmUserAddress(Address);

                String College = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_COLLEGE_NAME));
                userDetail.setmUserCollegeName(College);

                String Events = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USER_EVENTS));
                userDetail.setmUserEvents(Events);

                mUserDetail.add(userDetail);
            } while (cursor.moveToNext());
        }
        return mUserDetail;
    }

}
