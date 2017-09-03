package com.extremeplayer.interrupt.UserInfo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class UserProvider extends ContentProvider {

    private static final int USERS = 100;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(UserContract.CONTENT_AUTHORITY, UserContract.PATH_USER_DATA, USERS);
    }

    private userDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new userDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS:
                cursor = database.query(UserContract.UserEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS:
                return UserContract.UserEntry.CONTENT_LIST_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS:
                return insertDetails(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertDetails(Uri uri, ContentValues values) {
        String userName = values.getAsString(UserContract.UserEntry.COLUMN_USER_NAME);
        if (userName == null)
            throw new IllegalArgumentException("UserName is mandatory");

        String userMail = values.getAsString(UserContract.UserEntry.COLUMN_USER_MAIL);
        if (userMail == null)
            throw new IllegalArgumentException("E-mail is mandatory");

        String userPass = values.getAsString(UserContract.UserEntry.COLUMN_USER_PASSWORD);
        if (userPass == null)
            throw new IllegalArgumentException("Password is mandatory");

        String userMobileNumber = values.getAsString(UserContract.UserEntry.COLUMN_USER_MOBILE_NUMBER);
        if (userMobileNumber == null)
            throw new IllegalArgumentException("Mobile Number is mandatory");

        String userCollegeName = values.getAsString(UserContract.UserEntry.COLUMN_COLLEGE_NAME);
        if (userCollegeName == null)
            throw new IllegalArgumentException("College Name is mandatory");

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(UserContract.UserEntry.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case USERS:
                rowsDeleted = database.delete(UserContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0)
                    getContext().getContentResolver().notifyChange(uri, null);
                return rowsDeleted;

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS:
                return updateUserDetails(uri, values, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateUserDetails(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(UserContract.UserEntry.COLUMN_USER_NAME)) {
            String userName = values.getAsString(UserContract.UserEntry.COLUMN_USER_NAME);
            if (userName == null)
                throw new IllegalArgumentException("UserName is mandatory");
        }

        if (values.containsKey(UserContract.UserEntry.COLUMN_USER_MAIL)) {
            String userMail = values.getAsString(UserContract.UserEntry.COLUMN_USER_MAIL);
            if (userMail == null)
                throw new IllegalArgumentException("E-mail is mandatory");
        }

        /*if (values.containsKey(UserContract.UserEntry.COLUMN_USER_PASSWORD)) {
            String userPass = values.getAsString(UserContract.UserEntry.COLUMN_USER_PASSWORD);
            if (userPass == null)
                throw new IllegalArgumentException("Password is mandatory");
        }*/

        if (values.containsKey(UserContract.UserEntry.COLUMN_USER_MOBILE_NUMBER)) {
            String userMobileNumber = values.getAsString(UserContract.UserEntry.COLUMN_USER_MOBILE_NUMBER);
            if (userMobileNumber == null)
                throw new IllegalArgumentException("Mobile Number is mandatory");
        }

        if (values.containsKey(UserContract.UserEntry.COLUMN_COLLEGE_NAME)) {
            String userCollegeName = values.getAsString(UserContract.UserEntry.COLUMN_COLLEGE_NAME);
            if (userCollegeName == null)
                throw new IllegalArgumentException("College Name is mandatory");
        }

        if (values.size() == 0)
            return 0;

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(UserContract.UserEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }
}

