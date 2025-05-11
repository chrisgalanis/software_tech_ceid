package com.example.roomie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RoomieApp.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_LISTINGS = "house_listings";
    // For simplicity, we'll handle messages differently for now

    // Common column names
    public static final String COLUMN_ID = "_id";

    // Users table columns
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    // Add other user info columns as needed

    // House listings table columns
    public static final String COLUMN_LISTING_OWNER_ID = "owner_id";
    public static final String COLUMN_LISTING_ADDRESS = "address";
    public static final String COLUMN_LISTING_RENT = "rent";
    // Add other listing info columns as needed

    // SQL statement to create the users table
    private static final String SQL_CREATE_USERS_TABLE =
            "CREATE TABLE " + TABLE_USERS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_USER_EMAIL + " TEXT UNIQUE," +
                    COLUMN_USER_PASSWORD + " TEXT" +
                    ");";

    // SQL statement to create the house listings table
    private static final String SQL_CREATE_LISTINGS_TABLE =
            "CREATE TABLE " + TABLE_LISTINGS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_LISTING_OWNER_ID + " INTEGER," + // Foreign key to users table?
                    COLUMN_LISTING_ADDRESS + " TEXT," +
                    COLUMN_LISTING_RENT + " REAL" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_LISTINGS_TABLE);
        // Create other tables here if needed (e.g., for matches later)
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method is called when the database needs to be upgraded.
        // You should handle schema changes here.
        // For simplicity, we'll just drop the old tables and create new ones.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTINGS);
        onCreate(db);
    }
}