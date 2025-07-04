package com.example.roomie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME    = "RoomieApp.db";
    private static final int    DATABASE_VERSION = 24;   // bumped to include reports table

    // users table
    public static final String TABLE_USERS           = "users";
    public static final String COLUMN_ID             = "_id";
    public static final String COLUMN_USER_EMAIL     = "email";
    public static final String COLUMN_USER_PASSWORD  = "password";
    public static final String COLUMN_USER_FIRSTNAME = "first_name";
    public static final String COLUMN_USER_LASTNAME = "last_name";
    public static final String COLUMN_USER_GENDER = "gender";
    public static final String COLUMN_USER_BIRTHDAY = "birthday";
    public static final String COLUMN_USER_AVATAR_URL = "avatar_url";
    // new columns
    public static final String COLUMN_USER_BUDGET_FROM = "budget_from";
    public static final String COLUMN_USER_BUDGET_TO = "budget_to";
    public static final String COLUMN_USER_CITY = "city";




    // photos table
    public static final String TABLE_PHOTOS       = "user_photos";
    public static final String COLUMN_PHOTO_ID    = "_id";
    public static final String COLUMN_PHOTO_USER  = "user_id";
    public static final String COLUMN_PHOTO_URI   = "uri";

    // interests table
    public static final String TABLE_INTERESTS        = "user_interests";
    public static final String COLUMN_INTEREST_ID     = "_id";
    public static final String COLUMN_INTEREST_USER   = "user_id";
    public static final String COLUMN_INTEREST_STRING = "interest";

    // Table: houses
    public static final String TABLE_HOUSES = "houses";
    public static final String COLUMN_HOUSE_ID = "_id";
    public static final String COLUMN_HOUSE_OWNER = "owner_id";
    public static final String COLUMN_HOUSE_ADDRESS = "address";
    public static final String COLUMN_HOUSE_RENT = "rent";
    public static final String COLUMN_HOUSE_AREA = "area";
    public static final String COLUMN_HOUSE_FLOOR = "floor";
    public static final String COLUMN_HOUSE_LAT = "latitude";
    public static final String COLUMN_HOUSE_LNG = "longitude";

    //table : house_listings
    public static final String TABLE_HOUSE_LISTINGS          = "house_listings";
    public static final String COLUMN_LISTING_ID              = "_id";
    public static final String COLUMN_LISTING_HOUSE_ID        = "house_id";
    public static final String COLUMN_LISTING_OWNER_NAME      = "owner_name";
    public static final String COLUMN_LISTING_OWNER_AVATAR_URL= "owner_avatar_url";
    public static final String COLUMN_LISTING_HOUSE_AVATAR_URL= "house_avatar_url";

    public static final String COLUMN_LISTING_IS_APPROVED = "is_approved";


    // table: house_photos
    public static final String TABLE_HOUSE_PHOTOS = "house_photos";
    public static final String COLUMN_HOUSE_PHOTO_ID = "_id";
    public static final String COLUMN_HOUSE_PHOTO_HOUSE = "house_id";
    public static final String COLUMN_HOUSE_PHOTO_URI = "uri";

    // likes table
    public static final String TABLE_LIKES = "likes";
    public static final String COLUMN_LIKE_USER = "userId";
    public static final String COLUMN_LIKE_TARGET = "targetId";

  // ─── MATCHES TABLE ───────────────────────────────────────────────────────────
  public static final String TABLE_MATCHES = "matches";
  public static final String COLUMN_MATCH_ID = "match_id";
  public static final String COLUMN_MATCHES_USER_ID1 = "user_id1";
  public static final String COLUMN_MATCHES_USER_ID2 = "user_id2";
  public static final String COLUMN_MATCH_TIMESTAMP = "match_timestamp";

    // chat messages
    public static final String TABLE_CHAT_MESSAGES = "chat_messages";
    public static final String COLUMN_MESSAGE_ID = "_id";
    public static final String COLUMN_MESSAGE_SENDER_ID = "sender_user_id";
    public static final String COLUMN_MESSAGE_RECEIVER_ID = "receiver_user_id";
    public static final String COLUMN_MESSAGE_TEXT = "message_text";
    public static final String COLUMN_MESSAGE_TIMESTAMP_MS = "timestamp_ms";

    // ─── REPORTS TABLE ───────────────────────────────────────────────────────────
    public static final String TABLE_REPORTS         = "reports";
    public static final String COLUMN_REPORT_ID      = "_id";
    public static final String COLUMN_REPORT_USER_ID = "reported_user_id";
    public static final String COLUMN_REPORT_TEXT    = "text";
    public static final String COLUMN_REPORT_STATUS  = "status";      // "PENDING", "DISMISSED", "WARNED"

    // ─── WARNINGS TABLE ───────────────────────────────────────────────────────────
    public static final String TABLE_WARNINGS        = "warnings";
    public static final String COLUMN_WARNING_ID     = "_id";
    public static final String COLUMN_WARNING_USER   = "user_id";
    public static final String COLUMN_WARNING_TEXT   = "text";
    public static final String COLUMN_WARNING_STATUS = "status";      // "PENDING" or "ACKNOWLEDGED"
    public static final String COLUMN_WARNING_TIME   = "timestamp";



    // Message‐reports table
    public static final String TABLE_MESSAGE_REPORTS           = "message_reports";
    public static final String COLUMN_MSGREP_ID                = "_id";
    public static final String COLUMN_MSGREP_MESSAGE_ID        = "message_id";
    public static final String COLUMN_MSGREP_TEXT              = "text";
    public static final String COLUMN_MSGREP_STATUS            = "status";

    // Saved‐houses table
    public static final String TABLE_SAVED_HOUSES      = "saved_houses";
    public static final String COLUMN_SAVED_ID         = "_id";
    public static final String COLUMN_SAVED_USER_ID    = "user_id";
    public static final String COLUMN_SAVED_HOUSE_ID   = "house_id";
    public static final String COLUMN_SAVED_TIMESTAMP  = "saved_at";



    // ─── CREATE TABLE STATEMENTS ─────────────────────────────────────────────────
    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " ("
                    + COLUMN_ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_USER_EMAIL        + " TEXT UNIQUE, "
                    + COLUMN_USER_PASSWORD     + " TEXT, "
                    + COLUMN_USER_FIRSTNAME    + " TEXT, "
                    + COLUMN_USER_LASTNAME     + " TEXT, "
                    + COLUMN_USER_GENDER       + " TEXT, "
                    + COLUMN_USER_BIRTHDAY     + " TEXT, "
                    + COLUMN_USER_CITY         + " TEXT, "
                    + COLUMN_USER_BUDGET_FROM  + " INTEGER DEFAULT 0, "
                    + COLUMN_USER_BUDGET_TO    + " INTEGER DEFAULT 0, "
                    + COLUMN_USER_AVATAR_URL   + " TEXT"
                    + ");";

    private static final String SQL_CREATE_PHOTOS =
            "CREATE TABLE "
                    + TABLE_PHOTOS
                    + " ("
                    + COLUMN_PHOTO_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_PHOTO_USER
                    + " INTEGER, "
                    + COLUMN_PHOTO_URI
                    + " TEXT, "
                    + "FOREIGN KEY("
                    + COLUMN_PHOTO_USER
                    + ") REFERENCES "
                    + TABLE_USERS
                    + "("
                    + COLUMN_ID
                    + ") ON DELETE CASCADE"
                    + ");";

    private static final String SQL_CREATE_INTERESTS =
            "CREATE TABLE "
                    + TABLE_INTERESTS
                    + " ("
                    + COLUMN_INTEREST_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_INTEREST_USER
                    + " INTEGER, "
                    + COLUMN_INTEREST_STRING
                    + " TEXT, "
                    + "FOREIGN KEY("
                    + COLUMN_INTEREST_USER
                    + ") REFERENCES "
                    + TABLE_USERS
                    + "("
                    + COLUMN_ID
                    + ") ON DELETE CASCADE"
                    + ");";

    private static final String SQL_CREATE_HOUSES =
            "CREATE TABLE "
                    + TABLE_HOUSES
                    + " ("
                    + COLUMN_HOUSE_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_HOUSE_OWNER
                    + " INTEGER, "
                    + COLUMN_HOUSE_ADDRESS
                    + " TEXT, "
                    + COLUMN_HOUSE_RENT
                    + " REAL, "
                    + COLUMN_HOUSE_AREA
                    + " REAL, "
                    + COLUMN_HOUSE_FLOOR
                    + " INTEGER, "
                    + COLUMN_HOUSE_LAT
                    + " REAL, "
                    + COLUMN_HOUSE_LNG
                    + " REAL, "
                    + "FOREIGN KEY("
                    + COLUMN_HOUSE_OWNER
                    + ") REFERENCES "
                    + TABLE_USERS
                    + "("
                    + COLUMN_ID
                    + ") ON DELETE CASCADE"
                    + ");";

    private static final String SQL_CREATE_HOUSE_LISTINGS =
            "CREATE TABLE " + TABLE_HOUSE_LISTINGS + " ("
                    + COLUMN_LISTING_ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_LISTING_HOUSE_ID         + " INTEGER UNIQUE, "
                    + COLUMN_LISTING_OWNER_NAME       + " TEXT, "
                    + COLUMN_LISTING_OWNER_AVATAR_URL + " TEXT, "
                    + COLUMN_LISTING_HOUSE_AVATAR_URL + " TEXT, "
                    + COLUMN_LISTING_IS_APPROVED      + " INTEGER DEFAULT 0, "
                    + "FOREIGN KEY(" + COLUMN_LISTING_HOUSE_ID + ") REFERENCES "
                    + TABLE_HOUSES + "(" + COLUMN_HOUSE_ID + ") ON DELETE CASCADE"
                    + ");";

    private static final String SQL_CREATE_HOUSE_PHOTOS =
            "CREATE TABLE "
                    + TABLE_HOUSE_PHOTOS
                    + " ("
                    + COLUMN_HOUSE_PHOTO_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_HOUSE_PHOTO_HOUSE
                    + " INTEGER, "
                    + COLUMN_HOUSE_PHOTO_URI
                    + " TEXT, "
                    + "FOREIGN KEY("
                    + COLUMN_HOUSE_PHOTO_HOUSE
                    + ") REFERENCES "
                    + TABLE_HOUSES
                    + "("
                    + COLUMN_HOUSE_ID
                    + ") ON DELETE CASCADE"
                    + ");";

    private static final String SQL_CREATE_LIKES =
            "CREATE TABLE "
                    + TABLE_LIKES
                    + " ("
                    + COLUMN_LIKE_USER
                    + " INTEGER, "
                    + COLUMN_LIKE_TARGET
                    + " INTEGER, "
                    + "PRIMARY KEY("
                    + COLUMN_LIKE_USER
                    + ","
                    + COLUMN_LIKE_TARGET
                    + "), "
                    + "FOREIGN KEY("
                    + COLUMN_LIKE_USER
                    + ") REFERENCES "
                    + TABLE_USERS
                    + "("
                    + COLUMN_ID
                    + ") ON DELETE CASCADE, "
                    + "FOREIGN KEY("
                    + COLUMN_LIKE_TARGET
                    + ") REFERENCES "
                    + TABLE_USERS
                    + "("
                    + COLUMN_ID
                    + ") ON DELETE CASCADE"
                    + ");";

  private static final String SQL_CREATE_MATCHES =
      "CREATE TABLE "
          + TABLE_MATCHES
          + " ("
          + COLUMN_MATCH_ID
          + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " // Made it auto-incrementing PK
          + COLUMN_MATCHES_USER_ID1
          + " INTEGER NOT NULL, "
          + COLUMN_MATCHES_USER_ID2
          + " INTEGER NOT NULL, "
          + COLUMN_MATCH_TIMESTAMP
          + " DATETIME DEFAULT CURRENT_TIMESTAMP, " // Added timestamp
          + "FOREIGN KEY("
          + COLUMN_MATCHES_USER_ID1
          + ") REFERENCES "
          + TABLE_USERS
          + "("
          + COLUMN_ID
          + ") ON DELETE CASCADE, "
          + "FOREIGN KEY("
          + COLUMN_MATCHES_USER_ID2
          + ") REFERENCES "
          + TABLE_USERS
          + "("
          + COLUMN_ID
          + ") ON DELETE CASCADE, "
          + "CHECK ("
          + COLUMN_MATCHES_USER_ID1
          + " != "
          + COLUMN_MATCHES_USER_ID2
          + ")" // Prevent self-matches at DB level
          + ");";

    private static final String SQL_CREATE_CHAT_MESSAGES =
            "CREATE TABLE "
                    + TABLE_CHAT_MESSAGES
                    + " ("
                    + COLUMN_MESSAGE_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_MESSAGE_SENDER_ID
                    + " INTEGER NOT NULL, "
                    + COLUMN_MESSAGE_RECEIVER_ID
                    + " INTEGER NOT NULL, "
                    + COLUMN_MESSAGE_TEXT
                    + " TEXT NOT NULL, "
                    + COLUMN_MESSAGE_TIMESTAMP_MS
                    + " INTEGER NOT NULL, "
                    + "FOREIGN KEY("
                    + COLUMN_MESSAGE_SENDER_ID
                    + ") REFERENCES "
                    + TABLE_USERS
                    + "("
                    + COLUMN_ID
                    + ") ON DELETE CASCADE, "
                    + "FOREIGN KEY("
                    + COLUMN_MESSAGE_RECEIVER_ID
                    + ") REFERENCES "
                    + TABLE_USERS
                    + "("
                    + COLUMN_ID
                    + ") ON DELETE CASCADE"
                    + ");";

    private static final String SQL_CREATE_REPORTS =
            "CREATE TABLE " + TABLE_REPORTS + " ("
                    + COLUMN_REPORT_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_REPORT_USER_ID + " INTEGER, "
                    + COLUMN_REPORT_TEXT    + " TEXT, "
                    + COLUMN_REPORT_STATUS  + " TEXT DEFAULT 'PENDING', "
                    + "FOREIGN KEY(" + COLUMN_REPORT_USER_ID + ") REFERENCES "
                    + TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
                    + ");";

    private static final String SQL_CREATE_WARNINGS =
            "CREATE TABLE " + TABLE_WARNINGS + " ("
                    + COLUMN_WARNING_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_WARNING_USER + " INTEGER, "
                    + COLUMN_WARNING_TEXT + " TEXT, "
                    + COLUMN_WARNING_STATUS+ " TEXT DEFAULT 'PENDING', "
                    + COLUMN_WARNING_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY(" + COLUMN_WARNING_USER + ") REFERENCES "
                    + TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
                    + ");";

    private static final String SQL_CREATE_MESSAGE_REPORTS =
            "CREATE TABLE " + TABLE_MESSAGE_REPORTS + " ("
                    + COLUMN_MSGREP_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_MSGREP_MESSAGE_ID + " INTEGER NOT NULL, "
                    + COLUMN_MSGREP_TEXT       + " TEXT NOT NULL, "
                    + COLUMN_MSGREP_STATUS     + " TEXT DEFAULT 'PENDING', "
                    + "FOREIGN KEY(" + COLUMN_MSGREP_MESSAGE_ID + ") REFERENCES "
                    + TABLE_CHAT_MESSAGES + "(" + COLUMN_MESSAGE_ID + ") ON DELETE CASCADE"
                    + ");";


    private static final String SQL_CREATE_SAVED_HOUSES =
            "CREATE TABLE " + TABLE_SAVED_HOUSES + " ("
                    + COLUMN_SAVED_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_SAVED_USER_ID   + " INTEGER NOT NULL, "
                    + COLUMN_SAVED_HOUSE_ID  + " INTEGER NOT NULL, "
                    + COLUMN_SAVED_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY(" + COLUMN_SAVED_USER_ID  + ") REFERENCES "
                    + TABLE_USERS       + "(" + COLUMN_ID       + ") ON DELETE CASCADE, "
                    + "FOREIGN KEY(" + COLUMN_SAVED_HOUSE_ID + ") REFERENCES "
                    + TABLE_HOUSES      + "(" + COLUMN_HOUSE_ID + ") ON DELETE CASCADE, "
                    + "UNIQUE(" + COLUMN_SAVED_USER_ID + "," + COLUMN_SAVED_HOUSE_ID + ")"
                    + ");";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS);
        db.execSQL(SQL_CREATE_PHOTOS);
        db.execSQL(SQL_CREATE_INTERESTS);
        db.execSQL(SQL_CREATE_HOUSES);
        db.execSQL(SQL_CREATE_HOUSE_PHOTOS);
        db.execSQL(SQL_CREATE_LIKES);
        db.execSQL(SQL_CREATE_MATCHES);
        db.execSQL(SQL_CREATE_CHAT_MESSAGES);
        db.execSQL(SQL_CREATE_REPORTS);
        db.execSQL(SQL_CREATE_WARNINGS);
        db.execSQL(SQL_CREATE_HOUSE_LISTINGS);
        db.execSQL(SQL_CREATE_MESSAGE_REPORTS);
        db.execSQL(SQL_CREATE_SAVED_HOUSES);
        insertDefaultAdmin(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_HOUSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE_REPORTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOUSE_LISTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIKES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOUSE_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOUSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WARNINGS);
        onCreate(db);
    }

    /*** ORIGINAL METHODS ***/

    private void insertDefaultAdmin(SQLiteDatabase db) {
        // you can choose any email/password you like
        final String adminEmail = "admin";
        // password should be hashed in real apps!
        final String adminPass  = "admin";

        // Use INSERT OR IGNORE so this only runs once
        db.execSQL(
                "INSERT OR IGNORE INTO " + TABLE_USERS + "("
                        + COLUMN_USER_EMAIL + ","
                        + COLUMN_USER_PASSWORD + ","
                        + COLUMN_USER_FIRSTNAME + ","
                        + COLUMN_USER_LASTNAME + ","
                        + COLUMN_USER_GENDER + ","
                        + COLUMN_USER_BIRTHDAY + ","
                        + COLUMN_USER_CITY + ","
                        + COLUMN_USER_BUDGET_FROM + ","
                        + COLUMN_USER_BUDGET_TO + ","
                        + COLUMN_USER_AVATAR_URL
                        + ") VALUES(?,?,?,?,?,?,?,?,?,?);",
                new Object[]{
                        adminEmail,
                        adminPass,
                        "Admin",
                        "User",
                        "OTHER",          // or “UNSPECIFIED”
                        "1970-01-01",     // dummy birthday
                        "Headquarters",   // dummy city
                        0,                // budget_from
                        0,                // budget_to
                        ""                // avatar_url
                }
        );
    }
    public long getUserIdByEmail(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_USERS,
                new String[]{ COLUMN_ID },
                COLUMN_USER_EMAIL + " = ?",
                new String[]{ email },
                null, null, null
        );

        long id = -1;
        if (c.moveToFirst()) {
            id = c.getLong(c.getColumnIndexOrThrow(COLUMN_ID));
        }
        c.close();
        return id;
    }
    // Check profile completeness
    public boolean isProfileComplete(long userId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] cols = {
                COLUMN_USER_FIRSTNAME, COLUMN_USER_LASTNAME, COLUMN_USER_GENDER, COLUMN_USER_BIRTHDAY
        };
        String sel = COLUMN_ID + "=?";
        String[] args = {String.valueOf(userId)};
        Cursor c = db.query(TABLE_USERS, cols, sel, args, null, null, null);
        boolean complete = false;
        if (c.moveToFirst()) {
            boolean missingFirst =
                    c.isNull(c.getColumnIndexOrThrow(COLUMN_USER_FIRSTNAME))
                            || c.getString(c.getColumnIndexOrThrow(COLUMN_USER_FIRSTNAME)).trim().isEmpty();
            boolean missingLast =
                    c.isNull(c.getColumnIndexOrThrow(COLUMN_USER_LASTNAME))
                            || c.getString(c.getColumnIndexOrThrow(COLUMN_USER_LASTNAME)).trim().isEmpty();
            boolean missingGender =
                    c.isNull(c.getColumnIndexOrThrow(COLUMN_USER_GENDER))
                            || c.getString(c.getColumnIndexOrThrow(COLUMN_USER_GENDER)).trim().isEmpty();
            boolean missingBirth =
                    c.isNull(c.getColumnIndexOrThrow(COLUMN_USER_BIRTHDAY))
                            || c.getString(c.getColumnIndexOrThrow(COLUMN_USER_BIRTHDAY)).trim().isEmpty();
            complete = !(missingFirst || missingLast || missingGender || missingBirth);
        }
        c.close();
        return complete;
    }

    public long insertUser(
            String email,
            String password,
            String firstName,
            String lastName,
            String gender,
            String birthday) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_EMAIL, email);
        cv.put(COLUMN_USER_PASSWORD, password);
        cv.put(COLUMN_USER_FIRSTNAME, firstName);
        cv.put(COLUMN_USER_LASTNAME, lastName);
        cv.put(COLUMN_USER_GENDER, gender);
        cv.put(COLUMN_USER_BIRTHDAY, birthday);
        return db.insert(TABLE_USERS, null, cv);
    }

    // Update profile details
    public void updateUserProfile(
            long userId, String firstName, String lastName, String gender, String birthday) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_FIRSTNAME, firstName);
        cv.put(COLUMN_USER_LASTNAME, lastName);
        cv.put(COLUMN_USER_GENDER, gender);
        cv.put(COLUMN_USER_BIRTHDAY, birthday);
        db.update(TABLE_USERS, cv, COLUMN_ID + "=?", new String[] {String.valueOf(userId)});
        db.close();
    }

    // Photos CRUD
    public void insertUserPhotos(long userId, List<Uri> uris) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (Uri uri: uris) {
                if (uri == null) continue;
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_PHOTO_USER, userId);
                cv.put(COLUMN_PHOTO_URI,  uri.toString());
                db.insert(TABLE_PHOTOS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<String> getUserPhotos(long userId) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_PHOTOS,
                new String[]{ COLUMN_PHOTO_URI },
                COLUMN_PHOTO_USER + "=?",
                new String[]{ String.valueOf(userId) },
                null, null, null
        );
        while (c.moveToNext()) {
            list.add(c.getString(c.getColumnIndexOrThrow(COLUMN_PHOTO_URI)));
        }
        c.close();
        return list;
    }

    // Interests CRUD
    public void insertUserInterests(long userId, List<String> interests) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (String interest: interests) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_INTEREST_USER,   userId);
                cv.put(COLUMN_INTEREST_STRING, interest);
                db.insert(TABLE_INTERESTS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<String> getUserInterests(long userId) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_INTERESTS,
                new String[]{ COLUMN_INTEREST_STRING },
                COLUMN_INTEREST_USER + "=?",
                new String[]{ String.valueOf(userId) },
                null, null, null
        );
        while (c.moveToNext()) {
            list.add(c.getString(c.getColumnIndexOrThrow(COLUMN_INTEREST_STRING)));
        }
        c.close();
        return list;
    }

    /** Save budget & city for this user */
    public void updateUserPreferences(long userId, int from, int to, String city) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_BUDGET_FROM, from);
        cv.put(COLUMN_USER_BUDGET_TO,   to);
        cv.put(COLUMN_USER_CITY,        city);
        db.update(
                TABLE_USERS,
                cv,
                COLUMN_ID + "=?",
                new String[]{ String.valueOf(userId) }
        );
        db.close();
    }

    // Insert a new house and return its ID
    public long insertHouse(House house) {
        SQLiteDatabase db = getWritableDatabase();
        long houseId = -1;

        db.beginTransaction();
        try {
            // 1) insert into houses
            ContentValues cvHouse = new ContentValues();
            cvHouse.put(COLUMN_HOUSE_OWNER,     house.ownerId);
            cvHouse.put(COLUMN_HOUSE_ADDRESS,   house.address);
            cvHouse.put(COLUMN_HOUSE_RENT,      house.rent);
            cvHouse.put(COLUMN_HOUSE_AREA,      house.area);
            cvHouse.put(COLUMN_HOUSE_FLOOR,     house.floor);
            cvHouse.put(COLUMN_HOUSE_LAT,       house.latitude);
            cvHouse.put(COLUMN_HOUSE_LNG,       house.longitude);
            houseId = db.insert(TABLE_HOUSES, null, cvHouse);

            if (houseId == -1) {
                // insertion failed
                return -1;
            }

            // 2) insert each photo URL
            if (house.photoUrls != null) {
                for (String url : house.photoUrls) {
                    if (url == null) continue;
                    ContentValues cvPhoto = new ContentValues();
                    cvPhoto.put(COLUMN_HOUSE_PHOTO_HOUSE, houseId);
                    cvPhoto.put(COLUMN_HOUSE_PHOTO_URI,   url);
                    db.insert(TABLE_HOUSE_PHOTOS, null, cvPhoto);
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return houseId;
    }

    // Insert photo URIs for a house
    public void insertHousePhotos(long houseId, List<Uri> uris) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (Uri uri : uris) {
                if (uri == null) continue;
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_HOUSE_PHOTO_HOUSE, houseId);
                cv.put(COLUMN_HOUSE_PHOTO_URI, uri.toString());
                db.insert(TABLE_HOUSE_PHOTOS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<String> getHousePhotos(long houseId) {
        List<String> photos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_HOUSE_PHOTOS,
                new String[]{ COLUMN_HOUSE_PHOTO_URI },
                COLUMN_HOUSE_PHOTO_HOUSE + " = ?",
                new String[]{ String.valueOf(houseId) },
                null, null, null
        );
        while (c.moveToNext()) {
            photos.add(
                    c.getString(c.getColumnIndexOrThrow(COLUMN_HOUSE_PHOTO_URI))
            );
        }
        c.close();
        return photos;
    }

    //get house id from user id
    public long getHouseIdByUserId(long userId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_HOUSES,
                new String[]{ COLUMN_HOUSE_ID },                   // only need the PK
                COLUMN_HOUSE_OWNER + " = ?",                       // WHERE owner_id = ?
                new String[]{ String.valueOf(userId) },
                null, null, null
        );
        if (!c.moveToFirst()) {
            c.close();
            return -1;
        }
        long houseId = c.getLong(c.getColumnIndexOrThrow(COLUMN_HOUSE_ID));
        c.close();

        return houseId;
    }


    // Fetch a single House by id
    public House getHouseById(long houseId) {
        SQLiteDatabase db = getReadableDatabase();

        // 1) Query the houses table
        Cursor c = db.query(
                TABLE_HOUSES,
                new String[]{
                        COLUMN_HOUSE_ID,
                        COLUMN_HOUSE_OWNER,
                        COLUMN_HOUSE_ADDRESS,
                        COLUMN_HOUSE_RENT,
                        COLUMN_HOUSE_AREA,
                        COLUMN_HOUSE_FLOOR,
                        COLUMN_HOUSE_LAT,
                        COLUMN_HOUSE_LNG
                },
                COLUMN_HOUSE_ID + " = ?",
                new String[]{ String.valueOf(houseId) },
                null, null, null
        );
        if (!c.moveToFirst()) {
            c.close();
            return null;
        }

        long   id        = c.getLong(c.getColumnIndexOrThrow(COLUMN_HOUSE_ID));
        long   ownerId   = c.getLong(c.getColumnIndexOrThrow(COLUMN_HOUSE_OWNER));
        String address   = c.getString(c.getColumnIndexOrThrow(COLUMN_HOUSE_ADDRESS));
        double rent      = c.getDouble(c.getColumnIndexOrThrow(COLUMN_HOUSE_RENT));
        double area      = c.getDouble(c.getColumnIndexOrThrow(COLUMN_HOUSE_AREA));
        int    floor     = c.getInt(c.getColumnIndexOrThrow(COLUMN_HOUSE_FLOOR));
        double latitude  = c.getDouble(c.getColumnIndexOrThrow(COLUMN_HOUSE_LAT));
        double longitude = c.getDouble(c.getColumnIndexOrThrow(COLUMN_HOUSE_LNG));
        c.close();

        // 2) Load all photo URLs
        List<String> photoUrls = new ArrayList<>();
        Cursor pc = db.query(
                TABLE_HOUSE_PHOTOS,
                new String[]{ COLUMN_HOUSE_PHOTO_URI },
                COLUMN_HOUSE_PHOTO_HOUSE + " = ?",
                new String[]{ String.valueOf(houseId) },
                null, null, null
        );
        while (pc.moveToNext()) {
            photoUrls.add(pc.getString(pc.getColumnIndexOrThrow(COLUMN_HOUSE_PHOTO_URI)));
        }
        pc.close();

        // 3) Build and return House
        return new House(
                id,
                ownerId,
                address,
                rent,
                area,
                floor,
                latitude,
                longitude,
                photoUrls
        );
    }
    // Fetch all house listings
    /**
     * Returns only the approved listings.
     */
    public List<HouseListing> getApprovedHouseListings() {
        List<HouseListing> listings = new ArrayList<>();
        String sql =
                "SELECT "
                        +     COLUMN_LISTING_HOUSE_ID + ","
                        +     COLUMN_LISTING_OWNER_NAME + ","
                        +     COLUMN_LISTING_OWNER_AVATAR_URL + ","
                        +     COLUMN_LISTING_HOUSE_AVATAR_URL + ","
                        +     COLUMN_LISTING_IS_APPROVED
                        + " FROM " + TABLE_HOUSE_LISTINGS
                        + " WHERE " + COLUMN_LISTING_IS_APPROVED + "=1";

        try (Cursor c = getReadableDatabase().rawQuery(sql, null)) {
            while (c.moveToNext()) {
                long   houseId        = c.getLong(0);
                String ownerName      = c.getString(1);
                String ownerAvatarUrl = c.getString(2);
                String houseAvatarUrl = c.getString(3);
                boolean isApproved    = c.getInt(4) == 1;

                House house = getHouseById(houseId);
                if (house != null) {
                    listings.add(new HouseListing(
                            house,
                            ownerName,
                            ownerAvatarUrl,
                            houseAvatarUrl,
                            isApproved
                    ));
                }
            }
        }
        return listings;
    }


    /**
     * Delete a House (and all its dependent rows via ON DELETE CASCADE).
     * @param houseId the ID of the house to remove
     * @return true if at least one row was deleted
     */
    public boolean deleteHouse(long houseId) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(
                TABLE_HOUSES,
                COLUMN_HOUSE_ID + " = ?",
                new String[]{ String.valueOf(houseId) }
        );
        db.close();
        return rows > 0;
    }

    public long insertHouseListing(HouseListing listing) {
        SQLiteDatabase db = getWritableDatabase();
        long listingId = -1;

        db.beginTransaction();
        try {
            // 1) Ensure the House exists in the houses table
            if (listing.house.id <= 0) {
                long newHouseId = insertHouse(listing.house);
                if (newHouseId <= 0) {
                    throw new android.database.SQLException(
                            "Failed to insert House for listing"
                    );
                }
                // update the in‐memory object so downstream code can see it
                listing.house.id = newHouseId;
            }

            // 2) Now insert the listing row with a valid house_id
            ContentValues cvList = new ContentValues();
            cvList.put(COLUMN_LISTING_HOUSE_ID,          listing.house.id);
            cvList.put(COLUMN_LISTING_OWNER_NAME,        listing.ownerName);
            cvList.put(COLUMN_LISTING_OWNER_AVATAR_URL,  listing.ownerAvatarUrl);
            cvList.put(COLUMN_LISTING_HOUSE_AVATAR_URL,  listing.houseAvatarUrl);
            cvList.put(COLUMN_LISTING_IS_APPROVED,       listing.isApproved ? 1 : 0);

            listingId = db.insert(
                    TABLE_HOUSE_LISTINGS,
                    null,
                    cvList
            );
            if (listingId == -1) {
                throw new android.database.SQLException(
                        "Error inserting listing for house_id=" + listing.house.id
                );
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(
                    "DatabaseHelper",
                    "Error inserting house listing: " + e.getMessage()
            );
        } finally {
            db.endTransaction();
        }

        return listingId;
    }

    /**
     * Fetches all house listings that are not yet approved.
     */
    public List<HouseListing> getUnapprovedHouseListings() {
        List<HouseListing> listings = new ArrayList<>();
        String sql =
                "SELECT "
                        +     COLUMN_LISTING_HOUSE_ID + ","
                        +     COLUMN_LISTING_OWNER_NAME + ","
                        +     COLUMN_LISTING_OWNER_AVATAR_URL + ","
                        +     COLUMN_LISTING_HOUSE_AVATAR_URL + ","
                        +     COLUMN_LISTING_IS_APPROVED
                        + " FROM " + TABLE_HOUSE_LISTINGS
                        + " WHERE " + COLUMN_LISTING_IS_APPROVED + "=0";

        try (Cursor c = getReadableDatabase().rawQuery(sql, null)) {
            while (c.moveToNext()) {
                long   houseId        = c.getLong(0);
                String ownerName      = c.getString(1);
                String ownerAvatarUrl = c.getString(2);
                String houseAvatarUrl = c.getString(3);
                boolean isApproved    = c.getInt(4) == 1;

                House house = getHouseById(houseId);
                if (house != null) {
                    listings.add(new HouseListing(
                            house,
                            ownerName,
                            ownerAvatarUrl,
                            houseAvatarUrl,
                            isApproved
                    ));
                }
            }
        }
        return listings;
    }

    public boolean approveListing(HouseListing listing) {
        if (listing == null || listing.house == null) return false;

        long houseId = listing.house.id;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LISTING_IS_APPROVED, 1);
        int rows = db.update(
                TABLE_HOUSE_LISTINGS,
                cv,
                COLUMN_LISTING_HOUSE_ID + " = ?",
                new String[]{ String.valueOf(houseId) }
        );
        db.close();
        return rows > 0;
    }

    public boolean disapproveListing(HouseListing listing) {
        if (listing == null || listing.house == null) return false;
        long houseId = listing.house.id;
        SQLiteDatabase db = this.getWritableDatabase();
        deleteHouse(houseId);
        int rows = db.delete(
                TABLE_HOUSE_LISTINGS,
                COLUMN_LISTING_HOUSE_ID + " = ?",
                new String[]{ String.valueOf(houseId) }
        );
        db.close();
        return rows > 0;
    }

    // Record a like or dislike
    public void likeUser(long userId, long targetId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LIKE_USER,   userId);
        cv.put(COLUMN_LIKE_TARGET, targetId);
        db.insertWithOnConflict(
                TABLE_LIKES,
                null,
                cv,
                SQLiteDatabase.CONFLICT_IGNORE
        );
        db.close();
    }

    /** Remove a like: delete the row entirely */

    public boolean setAvatarUrl(long userId, Uri avatarUri) {
        if (avatarUri == null) return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_AVATAR_URL, avatarUri.toString());
        int rowsUpdated = db.update(
                TABLE_USERS,
                cv,
                COLUMN_ID + " = ?",
                new String[]{ String.valueOf(userId) }
        );
        db.close();
        return rowsUpdated > 0;
    }

  public void unlikeUser(long userId, long targetId) {
    SQLiteDatabase db = getWritableDatabase();
    db.delete(
        TABLE_LIKES,
        COLUMN_LIKE_USER + "=? AND " + COLUMN_LIKE_TARGET + "=?",
        new String[] {String.valueOf(userId), String.valueOf(targetId)});
    db.close();
  }

  public List<Long> getUsersWhoLikedMe(long userId) {
    List<Long> list = new ArrayList<>();
    SQLiteDatabase db = getReadableDatabase();
    Cursor c =
        db.query(
            TABLE_LIKES,
            new String[] {COLUMN_LIKE_USER},
            COLUMN_LIKE_TARGET + "=?",
            new String[] {String.valueOf(userId)},
            null,
            null,
            null);
    while (c.moveToNext()) {
      list.add(c.getLong(c.getColumnIndexOrThrow(COLUMN_LIKE_USER)));
    }
    c.close();
    return list;
  }

    /**
     * Fetch a User object by its ID.
     */
    public User getUserById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_USERS,
                null,
                COLUMN_ID + "=?",
                new String[]{ String.valueOf(id) },
                null, null, null
        );
        User u = null;
        if (c.moveToFirst()) {
            u = new User();
            u.id        = c.getLong(c.getColumnIndexOrThrow(COLUMN_ID));
            u.firstName = c.getString(c.getColumnIndexOrThrow(COLUMN_USER_FIRSTNAME));
            u.lastName  = c.getString(c.getColumnIndexOrThrow(COLUMN_USER_LASTNAME));
            u.gender    = c.getString(c.getColumnIndexOrThrow(COLUMN_USER_GENDER));
            u.birthday  = c.getString(c.getColumnIndexOrThrow(COLUMN_USER_BIRTHDAY));
            u.city      = c.getString(c.getColumnIndexOrThrow(COLUMN_USER_CITY));
            u.minBudget = c.getInt(c.getColumnIndexOrThrow(COLUMN_USER_BUDGET_FROM));
            u.maxBudget = c.getInt(c.getColumnIndexOrThrow(COLUMN_USER_BUDGET_TO));
            u.avatarUrl = c.getString(c.getColumnIndexOrThrow(COLUMN_USER_AVATAR_URL));
        }
        c.close();
        return u;
    }

  // ─── MATCH METHODS ──────────────────────────────────────────────────────────

  public boolean createMatch(long userId1, long userId2) {
    if (userId1 == userId2) {
      Log.w("DatabaseHelper", "User cannot match with themselves. userId1: " + userId1);
      return false;
    }

    SQLiteDatabase db = this.getWritableDatabase();
    db.beginTransaction();
    boolean success = false;

    try {

      // Entry 1: User1 -> User2
      ContentValues values = new ContentValues();
      values.put(COLUMN_MATCHES_USER_ID1, userId1);
      values.put(COLUMN_MATCHES_USER_ID2, userId2);
      long result = db.insert(TABLE_MATCHES, null, values);

      if (result != -1) {
        db.setTransactionSuccessful();
        success = true;
        Log.d(
            "DatabaseHelper",
            "Bidirectional match entries created for users: "
                + userId1
                + " & "
                + userId2
                + ". Row IDs: "
                + result);
      } else {
        Log.e(
            "DatabaseHelper",
            "Failed to insert one or both match entries. " + "Result1: " + result);
      }
    } catch (Exception e) {
      Log.e(
          "DatabaseHelper", "Error during createMatchAndItsInverse transaction: " + e.getMessage());
    } finally {
      db.endTransaction();
    }
    return success;
  }

  public List<Long> getMatches(long userId) {
    List<Long> partnerIds = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase(); // Use getReadableDatabase for queries
    Cursor cursor = null;

    // Using an alias "partner_id" for the selected column makes it easier to retrieve
    String query =
        "SELECT "
            + COLUMN_MATCHES_USER_ID2
            + " AS partner_id FROM "
            + TABLE_MATCHES
            + " WHERE "
            + COLUMN_MATCHES_USER_ID1
            + " = ? "
            + "UNION "
            + // UNION ensures distinct partner IDs
            "SELECT "
            + COLUMN_MATCHES_USER_ID1
            + " AS partner_id FROM "
            + TABLE_MATCHES
            + " WHERE "
            + COLUMN_MATCHES_USER_ID2
            + " = ?";

    // Pass the userId as selection arguments to prevent SQL injection
    String[] selectionArgs = {String.valueOf(userId), String.valueOf(userId)};

    try {
      cursor = db.rawQuery(query, selectionArgs);

      if (cursor != null && cursor.moveToFirst()) {
        int partnerIdColumnIndex =
            cursor.getColumnIndex("partner_id"); // Get column index using alias
        if (partnerIdColumnIndex == -1) {
          // This should not happen if the query is correct and uses 'AS partner_id'
          Log.e(
              "DatabaseHelper",
              "Error: 'partner_id' column not found in getAllPartnersForUser query result.");
          return partnerIds; // Return empty list or throw exception
        }
        do {
          partnerIds.add(cursor.getLong(partnerIdColumnIndex));
        } while (cursor.moveToNext());
      }
    } catch (Exception e) {
      Log.e("DatabaseHelper", "Error in getAllPartnersForUser: " + e.getMessage());
    } finally {
      if (cursor != null) {
        cursor.close();
      }
      // Do not close the db instance here if it's managed externally (e.g., singleton)
      // or if more operations are expected on the same instance.
    }
    Log.d(
        "DatabaseHelper",
        "User " + userId + " has comprehensive matches with partners: " + partnerIds.toString());
    return partnerIds;
  }

  // ─── CHAT METHODS ──────────────────────────────────────────────────────────

  public long addChatMessage(long senderId, long receiverId, String messageText, long timestampMs) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(COLUMN_MESSAGE_SENDER_ID, senderId);
    values.put(COLUMN_MESSAGE_RECEIVER_ID, receiverId);
    values.put(COLUMN_MESSAGE_TEXT, messageText);
    values.put(COLUMN_MESSAGE_TIMESTAMP_MS, timestampMs);

    long newRowId = -1;
    try {
      newRowId = db.insertOrThrow(TABLE_CHAT_MESSAGES, null, values);
    } catch (Exception e) {
      Log.e("DatabaseHelper", "Error inserting chat message: " + e.getMessage());
    }
    return newRowId;
  }

  public List<ChatMessage> getChatMessagesBetweenUsers(
      long currentUserId,
      long chatPartnerId) { // chatPartnerProfileImageRes was correctly removed from params

    List<ChatMessage> messages = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();

    // --- >>> ADD THESE LINES TO FETCH USER OBJECTS <<< ---
    User currentUserObj = getUserById(currentUserId);
    User chatPartnerObj = getUserById(chatPartnerId);
    // --- >>> END OF ADDED LINES <<< ---

    // Now currentUserObj and chatPartnerObj are defined and can be used
    String currentUserAvatarUrl =
        (currentUserObj != null && currentUserObj.avatarUrl != null)
            ? currentUserObj.avatarUrl
            : null;
    String chatPartnerAvatarUrl =
        (chatPartnerObj != null && chatPartnerObj.avatarUrl != null)
            ? chatPartnerObj.avatarUrl
            : null;

    String selection =
        "("
            + COLUMN_MESSAGE_SENDER_ID
            + " = ? AND "
            + COLUMN_MESSAGE_RECEIVER_ID
            + " = ?) OR "
            + "("
            + COLUMN_MESSAGE_SENDER_ID
            + " = ? AND "
            + COLUMN_MESSAGE_RECEIVER_ID
            + " = ?)";
    String[] selectionArgs = {
      String.valueOf(currentUserId), String.valueOf(chatPartnerId),
      String.valueOf(chatPartnerId), String.valueOf(currentUserId)
    };
    String orderBy = COLUMN_MESSAGE_TIMESTAMP_MS + " ASC";

    Cursor cursor = null;
    try {
      cursor = db.query(TABLE_CHAT_MESSAGES, null, selection, selectionArgs, null, null, orderBy);
      if (cursor != null && cursor.moveToFirst()) {
        do {
            long messageId=cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_ID));
          long senderIdFromDb =
              cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_SENDER_ID));
          long receiverIdFromDb = // Stays as long, used for ChatMessage constructor
              cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_RECEIVER_ID));
          String messageText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_TEXT));
          long timestampMs =
              cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_TIMESTAMP_MS));

          String timeString =
              android.text.format.DateFormat.format("HH:mm", timestampMs).toString();
          boolean isSentByCurrentUser = (senderIdFromDb == currentUserId);

          String avatarUrlForThisMessage;
          ChatMessage message;

          if (isSentByCurrentUser) {
            avatarUrlForThisMessage = currentUserAvatarUrl;
            message =
                new ChatMessage(messageId,
                    senderIdFromDb, // currentUserId as string
                    receiverIdFromDb, // chatPartnerId as string
                    messageText,
                    timeString,
                    true, // isSentByCurrentUser
                    avatarUrlForThisMessage // current user's avatar URL
                    );
          } else {
            avatarUrlForThisMessage = chatPartnerAvatarUrl;
            message =
                new ChatMessage(
                        messageId,
                    senderIdFromDb, // chatPartnerId as string
                    receiverIdFromDb, // currentUserId as string
                    messageText,
                    timeString,
                    false, // isSentByCurrentUser
                    avatarUrlForThisMessage // chat partner's avatar URL
                    );
          }
          messages.add(message);

        } while (cursor.moveToNext());
      }
    } catch (Exception e) {
      Log.e("DatabaseHelper", "Error getting chat messages: " + e.getMessage());
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    Log.d(
        "DatabaseHelper",
        "Retrieved "
            + messages.size()
            + " messages between "
            + currentUserId
            + " and "
            + chatPartnerId);
    return messages;
  }

  public ChatMessage getLatestChatMessage(
      long currentUserId, long partnerId) { // chatPartnerProfileImageRes removed
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = null;
    ChatMessage latestMessage = null;

    String selection = COLUMN_MESSAGE_SENDER_ID + " = ? AND " + COLUMN_MESSAGE_RECEIVER_ID + " = ?";
    String[] selectionArgs = {String.valueOf(partnerId), String.valueOf(currentUserId)};
    String orderBy = COLUMN_MESSAGE_TIMESTAMP_MS + " DESC";
    String limit = "1";

    try {
      cursor =
          db.query(TABLE_CHAT_MESSAGES, null, selection, selectionArgs, null, null, orderBy, limit);

      if (cursor != null && cursor.moveToFirst()) {
          long messageId=   cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_ID));
        String messageText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_TEXT));
        long timestampMs =
            cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_TIMESTAMP_MS));
        String timeString = android.text.format.DateFormat.format("HH:mm", timestampMs).toString();

        // Fetch partner's avatar URL
        User partnerUser = getUserById(partnerId); // Use the updated getUserById
        String partnerAvatarUrl =
            (partnerUser != null && partnerUser.avatarUrl != null) ? partnerUser.avatarUrl : null;

        latestMessage =
            new ChatMessage(
                    messageId,
                partnerId,
                currentUserId,
                messageText,
                timeString,
                false, // isSentByCurrentUser is false (message is from partner)
                partnerAvatarUrl // Pass the String URL
                );
      }
    } catch (Exception e) {
      Log.e("DatabaseHelper", "Error getting latest chat message from partner: " + e.getMessage());
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }

    if (latestMessage != null) {
      Log.d(
          "DatabaseHelper",
          "Latest message from partner "
              + partnerId
              + " to user "
              + currentUserId
              + ": '"
              + latestMessage.messageText
              + "'");
    } else {
      Log.d(
          "DatabaseHelper",
          "No messages found from partner " + partnerId + " to user " + currentUserId);
    }
    return latestMessage;
  }

  public long findOrInsertUser(
      String email,
      String defaultPassword,
      String defaultFirstName,
      String defaultLastName,
      String defaultGender,
      String defaultBirthday) {
    SQLiteDatabase db = this.getReadableDatabase();
    long userId = -1;

    Cursor cursor =
        db.query(
            TABLE_USERS,
            new String[] {COLUMN_ID},
            COLUMN_USER_EMAIL + "=?",
            new String[] {email},
            null,
            null,
            null);

    if (cursor != null && cursor.moveToFirst()) {
      userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
    }
    if (cursor != null) {
      cursor.close();
    }

    if (userId == -1) {
      db = this.getWritableDatabase();
      ContentValues values = new ContentValues();
      values.put(COLUMN_USER_EMAIL, email);
      values.put(COLUMN_USER_PASSWORD, defaultPassword);
      values.put(COLUMN_USER_FIRSTNAME, defaultFirstName);
      values.put(COLUMN_USER_LASTNAME, defaultLastName);
      values.put(COLUMN_USER_GENDER, defaultGender);
      values.put(COLUMN_USER_BIRTHDAY, defaultBirthday);

      try {
        userId = db.insertOrThrow(TABLE_USERS, null, values);
      } catch (Exception e) {
        Log.e("DatabaseHelper", "Error inserting dummy user " + email + ": " + e.getMessage());
        return -1;
      }
    }
    return userId;
  }

  public boolean hasMessagesBetweenUsers(long userId1, long userId2) {
    SQLiteDatabase db = this.getReadableDatabase();
    String selection =
        "("
            + COLUMN_MESSAGE_SENDER_ID
            + " = ? AND "
            + COLUMN_MESSAGE_RECEIVER_ID
            + " = ?) OR "
            + "("
            + COLUMN_MESSAGE_SENDER_ID
            + " = ? AND "
            + COLUMN_MESSAGE_RECEIVER_ID
            + " = ?)";
    String[] selectionArgs = {
      String.valueOf(userId1), String.valueOf(userId2),
      String.valueOf(userId2), String.valueOf(userId1)
    };
    Cursor cursor = null;
    try {
      cursor =
          db.query(
              TABLE_CHAT_MESSAGES,
              new String[] {COLUMN_MESSAGE_ID},
              selection,
              selectionArgs,
              null,
              null,
              null,
              "1");
      return cursor != null && cursor.getCount() > 0;
    } catch (Exception e) {
      Log.e("DatabaseHelper", "Error checking if messages exist: " + e.getMessage());
      return false;
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

    // Get all other users as recommendations
    public List<User> getRecommendations(long currentUserId) {
        List<User> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c =
                db.query(
                        TABLE_USERS,
                        null,
                        COLUMN_ID + "!=?",
                        new String[] {String.valueOf(currentUserId)},
                        null,
                        null,
                        null);

        while (c.moveToNext()) {

            User u = new User();
            u.id = c.getLong(c.getColumnIndexOrThrow(COLUMN_ID));
            u.firstName = c.getString(c.getColumnIndexOrThrow(COLUMN_USER_FIRSTNAME));
            u.lastName = c.getString(c.getColumnIndexOrThrow(COLUMN_USER_LASTNAME));
            u.minBudget = c.getInt(c.getColumnIndexOrThrow(COLUMN_USER_BUDGET_FROM));
            u.maxBudget = c.getInt(c.getColumnIndexOrThrow(COLUMN_USER_BUDGET_TO));
            u.city = c.getString(c.getColumnIndexOrThrow(COLUMN_USER_CITY));
            list.add(u);
        }
        c.close();
        return list;
    }

    // ─── REPORTS METHODS ──────────────────────────────────────────────────────────

    /** Insert a new report for a user */
    public long insertReport(long reportedUserId, String text) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_REPORT_USER_ID, reportedUserId);
        cv.put(COLUMN_REPORT_TEXT,    text);
        long id = db.insert(TABLE_REPORTS, null, cv);
        db.close();
        return id;
    }

    /** Fetch all PENDING reports */
    public List<Report> getAllPendingReports() {
        List<Report> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] cols = {
                COLUMN_REPORT_ID,
                COLUMN_REPORT_USER_ID,
                COLUMN_REPORT_TEXT
        };
        String sel  = COLUMN_REPORT_STATUS + "=?";
        String[] args= { "PENDING" };

        Cursor c = db.query(
                TABLE_REPORTS,
                cols,
                sel,
                args,
                null, null,
                COLUMN_REPORT_ID + " DESC"
        );
        while (c.moveToNext()) {
            long   rid  = c.getLong(c.getColumnIndexOrThrow(COLUMN_REPORT_ID));
            long   uid  = c.getLong(c.getColumnIndexOrThrow(COLUMN_REPORT_USER_ID));
            String txt = c.getString(c.getColumnIndexOrThrow(COLUMN_REPORT_TEXT));
            list.add(new Report(rid, uid, "", "", txt));
        }
        c.close();
        db.close();
        return list;
    }

    /** Mark a report DISMISSED */
    public void dismissReport(long reportId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_REPORT_STATUS, "DISMISSED");
        db.update(
                TABLE_REPORTS,
                cv,
                COLUMN_REPORT_ID + "=?",
                new String[]{ String.valueOf(reportId) }
        );
        db.close();
    }

    public void fullfillReport(long reportId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_REPORT_STATUS, "fullfilled");
        db.update(
                TABLE_REPORTS,
                cv,
                COLUMN_REPORT_ID + "=?",
                new String[]{ String.valueOf(reportId) }
        );
        db.close();
    }

    /**
     * Warn a user and mark report WARNED
     *
     * @return
     */


    public long insertWarning(Warning warning) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_WARNING_USER,   warning.userId);
        cv.put(COLUMN_WARNING_TEXT,   warning.text);
        cv.put(COLUMN_WARNING_STATUS, warning.status);     // e.g. "PENDING"
        // timestamp column has DEFAULT CURRENT_TIMESTAMP, so we don’t set it here
        long id = db.insert(TABLE_WARNINGS, null, cv);
        db.close();
        return id;
    }
    public List<Warning> getWarningsForUser(long userId) {
        List<Warning> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] cols = {
                COLUMN_WARNING_ID,
                COLUMN_WARNING_USER,
                COLUMN_WARNING_TEXT,
                COLUMN_WARNING_STATUS,
                COLUMN_WARNING_TIME
        };
        String   sel  = COLUMN_WARNING_USER + "=?";
        String[] args = { String.valueOf(userId) };

        Cursor c = db.query(
                TABLE_WARNINGS,
                cols,
                sel,
                args,
                null, null,
                COLUMN_WARNING_TIME + " DESC"
        );
        while (c.moveToNext()) {
            long   id    = c.getLong(c.getColumnIndexOrThrow(COLUMN_WARNING_ID));
            String txt   = c.getString(c.getColumnIndexOrThrow(COLUMN_WARNING_TEXT));
            String stat  = c.getString(c.getColumnIndexOrThrow(COLUMN_WARNING_STATUS));
            String time  = c.getString(c.getColumnIndexOrThrow(COLUMN_WARNING_TIME));
            list.add(new Warning(id, userId, txt, stat, time));
        }
        c.close();
        db.close();
        return list;
    }

    /** Mark a warning as acknowledged */
    public void acknowledgeWarning(long warningId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_WARNING_STATUS, "ACKNOWLEDGED");
        db.update(
                TABLE_WARNINGS,
                cv,
                COLUMN_WARNING_ID + "=?",
                new String[]{ String.valueOf(warningId) }
        );
        db.close();
    }

    public long insertMessageReport(long messageId, String text) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MSGREP_MESSAGE_ID, messageId);
        cv.put(COLUMN_MSGREP_TEXT,       text);
        // status will default to 'PENDING'
        long id = db.insert(TABLE_MESSAGE_REPORTS, null, cv);
        db.close();
        return id;
    }

    public boolean deleteMessageReport(long reportId) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(
                TABLE_MESSAGE_REPORTS,
                COLUMN_MSGREP_ID + " = ?",
                new String[]{ String.valueOf(reportId) }
        );
        db.close();
        return rows > 0;
    }
    public ChatMessage getChatMessageById(long messageId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_CHAT_MESSAGES,
                new String[]{
                        COLUMN_MESSAGE_ID,
                        COLUMN_MESSAGE_SENDER_ID,
                        COLUMN_MESSAGE_RECEIVER_ID,
                        COLUMN_MESSAGE_TEXT,
                        COLUMN_MESSAGE_TIMESTAMP_MS
                },
                COLUMN_MESSAGE_ID + " = ?",
                new String[]{ String.valueOf(messageId) },
                null, null, null
        );

        ChatMessage message = null;
        if (c.moveToFirst()) {
            long senderId    = c.getLong(c.getColumnIndexOrThrow(COLUMN_MESSAGE_SENDER_ID));
            long receiverId  = c.getLong(c.getColumnIndexOrThrow(COLUMN_MESSAGE_RECEIVER_ID));
            String text      = c.getString(c.getColumnIndexOrThrow(COLUMN_MESSAGE_TEXT));
            long tsMillis    = c.getLong(c.getColumnIndexOrThrow(COLUMN_MESSAGE_TIMESTAMP_MS));
            String timeString = DateFormat.format("HH:mm", tsMillis).toString();

            // Look up sender avatar URL (may be null)
            User senderUser = getUserById(senderId);
            String avatarUrl = (senderUser != null) ? senderUser.avatarUrl : null;

            // We can't know "current user" here, so set isSentByCurrentUser = false
            message = new ChatMessage(
                    messageId,
                    senderId,
                    receiverId,
                    text,
                    timeString,
                    false,        // or adjust if your logic knows the current user ID
                    avatarUrl
            );
        }
        c.close();
        return message;
    }
    public List<MessageReport> getAllPendingMessageReports() {
        List<MessageReport> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_MESSAGE_REPORTS,
                new String[]{ COLUMN_MSGREP_ID, COLUMN_MSGREP_MESSAGE_ID, COLUMN_MSGREP_TEXT, COLUMN_MSGREP_STATUS },
                COLUMN_MSGREP_STATUS + "=?",
                new String[]{ "PENDING" },
                null, null,
                COLUMN_MSGREP_ID + " DESC"
        );
        while (c.moveToNext()) {
            list.add(new MessageReport(
                    c.getLong(0),
                    getChatMessageById(c.getLong(1)),
                    c.getString(2),
                    c.getString(3)
            ));
        }
        c.close();
        return list;
    }


    public boolean deleteChatMessage(long messageId) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(
                TABLE_CHAT_MESSAGES,
                COLUMN_MESSAGE_ID + " = ?",
                new String[]{ String.valueOf(messageId) }
        );
        db.close();
        return rows > 0;
    }

    public long insertSavedHouse(long userId, long houseId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SAVED_USER_ID,  userId);
        cv.put(COLUMN_SAVED_HOUSE_ID, houseId);
        long id = db.insertWithOnConflict(
                TABLE_SAVED_HOUSES,
                null,
                cv,
                SQLiteDatabase.CONFLICT_IGNORE  // ignores duplicates
        );
        db.close();
        return id;
    }

    public HouseListing getHouseListingByHouseId(long houseId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_HOUSE_LISTINGS,
                new String[]{
                        COLUMN_LISTING_OWNER_NAME,
                        COLUMN_LISTING_OWNER_AVATAR_URL,
                        COLUMN_LISTING_HOUSE_AVATAR_URL,
                        COLUMN_LISTING_IS_APPROVED
                },
                COLUMN_LISTING_HOUSE_ID + "=?",
                new String[]{ String.valueOf(houseId) },
                null, null, null
        );
        if (!c.moveToFirst()) {
            c.close();
            return null;
        }
        String ownerName      = c.getString(0);
        String ownerAvatarUrl = c.getString(1);
        String houseAvatarUrl = c.getString(2);
        boolean isApproved    = c.getInt(3) == 1;
        c.close();

        House h = getHouseById(houseId);
        return (h != null)
                ? new HouseListing(h, ownerName, ownerAvatarUrl, houseAvatarUrl, isApproved)
                : null;
    }

    /** Get all saved listings for a user */
    public List<HouseListing> getSavedHouseListingsForUser(long userId) {
        List<HouseListing> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_SAVED_HOUSES,
                new String[]{ COLUMN_SAVED_HOUSE_ID },
                COLUMN_SAVED_USER_ID + "=?",
                new String[]{ String.valueOf(userId) },
                null, null, null
        );
        while (c.moveToNext()) {
            long hid = c.getLong(0);
            HouseListing hl = getHouseListingByHouseId(hid);
            if (hl != null) list.add(hl);
        }
        c.close();
        return list;
    }


    /** Un‐save a house for a user; returns true if deleted. */
    public boolean deleteSavedHouse(long userId, long houseId) {
        SQLiteDatabase db = getWritableDatabase();
        HouseListing hl = getHouseListingByHouseId(houseId);
        int rows = db.delete(
                TABLE_SAVED_HOUSES,
                COLUMN_SAVED_USER_ID + "=? AND " + COLUMN_SAVED_HOUSE_ID + "=?",
                new String[]{ String.valueOf(userId), String.valueOf(houseId) }
        );
        db.close();
        return rows > 0;
    }

    /** Returns true if this user has already saved this house */
    public boolean isHouseSaved(long userId, long houseId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_SAVED_HOUSES,
                new String[]{ COLUMN_SAVED_ID },
                COLUMN_SAVED_USER_ID + "=? AND " + COLUMN_SAVED_HOUSE_ID + "=?",
                new String[]{ String.valueOf(userId), String.valueOf(houseId) },
                null, null, null
        );
        boolean exists = (c != null && c.getCount() > 0);
        if (c != null) c.close();
        return exists;
    }

}