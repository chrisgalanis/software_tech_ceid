package com.example.roomie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME    = "RoomieApp.db";
    private static final int    DATABASE_VERSION = 2;  // bump when schema changes

    // Table: users
    public static final String TABLE_USERS           = "users";
    public static final String COLUMN_ID             = "_id";
    public static final String COLUMN_USER_EMAIL     = "email";
    public static final String COLUMN_USER_PASSWORD  = "password";
    public static final String COLUMN_USER_FIRSTNAME = "first_name";
    public static final String COLUMN_USER_LASTNAME  = "last_name";

    // Table: user_photos
    public static final String TABLE_PHOTOS       = "user_photos";
    public static final String COLUMN_PHOTO_ID    = "_id";
    public static final String COLUMN_PHOTO_USER  = "user_id";
    public static final String COLUMN_PHOTO_URI   = "uri";

    // Table: user_interests
    public static final String TABLE_INTERESTS        = "user_interests";
    public static final String COLUMN_INTEREST_ID     = "_id";
    public static final String COLUMN_INTEREST_USER   = "user_id";
    public static final String COLUMN_INTEREST_STRING = "interest";

    // Create users table (adds first & last name columns)
    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " ("
                    + COLUMN_ID            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_USER_EMAIL    + " TEXT UNIQUE, "
                    + COLUMN_USER_PASSWORD + " TEXT, "
                    + COLUMN_USER_FIRSTNAME+ " TEXT, "
                    + COLUMN_USER_LASTNAME + " TEXT"
                    + ");";

    // Create photos table
    private static final String SQL_CREATE_PHOTOS =
            "CREATE TABLE " + TABLE_PHOTOS + " ("
                    + COLUMN_PHOTO_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_PHOTO_USER + " INTEGER, "
                    + COLUMN_PHOTO_URI  + " TEXT, "
                    + "FOREIGN KEY(" + COLUMN_PHOTO_USER + ") REFERENCES "
                    + TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
                    + ");";

    // Create interests table
    private static final String SQL_CREATE_INTERESTS =
            "CREATE TABLE " + TABLE_INTERESTS + " ("
                    + COLUMN_INTEREST_ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_INTEREST_USER   + " INTEGER, "
                    + COLUMN_INTEREST_STRING + " TEXT, "
                    + "FOREIGN KEY(" + COLUMN_INTEREST_USER + ") REFERENCES "
                    + TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
                    + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS);
        db.execSQL(SQL_CREATE_PHOTOS);
        db.execSQL(SQL_CREATE_INTERESTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Simple drop-and-recreate for demo; in prod, migrate carefully!
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    /** Called after login to get the user's ID */
    public long getUserIdByEmail(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_USER_EMAIL + "=?",
                new String[]{email},
                null,null,null);
        long id = -1;
        if (c.moveToFirst()) {
            id = c.getLong(c.getColumnIndexOrThrow(COLUMN_ID));
        }
        c.close();
        return id;
    }

    /** Update first+last name for this user */
    public void updateUserName(long userId, String first, String last) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_FIRSTNAME, first);
        cv.put(COLUMN_USER_LASTNAME, last);
        db.update(TABLE_USERS, cv, COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
    }

    /** Insert each photo URI for this user */
    public void insertUserPhotos(long userId, List<Uri> uris) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (Uri uri : uris) {
                if (uri == null) continue;
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_PHOTO_USER, userId);
                cv.put(COLUMN_PHOTO_URI, uri.toString());
                db.insert(TABLE_PHOTOS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /** Insert each interest for this user */
    public void insertUserInterests(long userId, List<String> interests) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (String interest : interests) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_INTEREST_USER, userId);
                cv.put(COLUMN_INTEREST_STRING, interest);
                db.insert(TABLE_INTERESTS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
