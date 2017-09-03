package com.extremeplayer.interrupt.UserInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class userDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "userDetails.db";
    private static final int DATABASE_VERSION = 5;

    public userDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserContract.UserEntry.TABLE_NAME + " ("
                + UserContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserContract.UserEntry.COLUMN_USER_NAME + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_USER_MAIL + " VARCHAR(50) NOT NULL, "
                + UserContract.UserEntry.COLUMN_USER_PASSWORD + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_USER_MOBILE_NUMBER + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_COLLEGE_NAME + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_USER_EVENTS + " TEXT DEFAULT '');";

        db.execSQL(SQL_CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME);
        onCreate(db);
    }


    public void updateDetails(String name, String email, String mobile, String password, String collegeName) {
        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = "UPDATE " + UserContract.UserEntry.TABLE_NAME + " SET "
                + UserContract.UserEntry.COLUMN_USER_NAME + " = \'" + name + "\'" + ","
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


    public ArrayList<UserDetail> getAllDetails(String mail) {
        ArrayList<UserDetail> mUserDetail = new ArrayList<UserDetail>();
        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + UserContract.UserEntry.TABLE_NAME + " WHERE " + UserContract.UserEntry.COLUMN_USER_MAIL + " = \'"
                + mail + "\'" + ";";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UserDetail userDetail = new UserDetail();

                String Name = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USER_NAME));
                userDetail.setmUserName(Name);

                String Mail = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USER_MAIL));
                userDetail.setmUserMail(Mail);

                String MobNum = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USER_MOBILE_NUMBER));
                userDetail.setmUserMobileNum(MobNum);

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
