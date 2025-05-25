package com.example.roomie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "RoomieApp.db";
  private static final int DATABASE_VERSION = 7;

  // users table
  public static final String TABLE_USERS = "users";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_USER_EMAIL = "email";
  public static final String COLUMN_USER_PASSWORD = "password";
  public static final String COLUMN_USER_FIRSTNAME = "first_name";
  public static final String COLUMN_USER_LASTNAME = "last_name";
  public static final String COLUMN_USER_GENDER = "gender";
  public static final String COLUMN_USER_BIRTHDAY = "birthday";
  // new preference columns
  public static final String COLUMN_USER_BUDGET = "budget";
  public static final String COLUMN_USER_CITY = "city";

  // photos table
  public static final String TABLE_PHOTOS = "user_photos";
  public static final String COLUMN_PHOTO_ID = "_id";
  public static final String COLUMN_PHOTO_USER = "user_id";
  public static final String COLUMN_PHOTO_URI = "uri";

  // interests table
  public static final String TABLE_INTERESTS = "user_interests";
  public static final String COLUMN_INTEREST_ID = "_id";
  public static final String COLUMN_INTEREST_USER = "user_id";
  public static final String COLUMN_INTEREST_STRING = "interest";

  // likes table
  public static final String TABLE_LIKES = "likes";
  public static final String COLUMN_LIKE_USER = "userId";
  public static final String COLUMN_LIKE_TARGET = "targetId";
  public static final String COLUMN_IS_LIKE = "isLike";

  // CREATE statements
  private static final String SQL_CREATE_USERS =
      "CREATE TABLE "
          + TABLE_USERS
          + " ("
          + COLUMN_ID
          + " INTEGER PRIMARY KEY AUTOINCREMENT, "
          + COLUMN_USER_EMAIL
          + " TEXT UNIQUE, "
          + COLUMN_USER_PASSWORD
          + " TEXT, "
          + COLUMN_USER_FIRSTNAME
          + " TEXT, "
          + COLUMN_USER_LASTNAME
          + " TEXT, "
          + COLUMN_USER_GENDER
          + " TEXT, "
          + COLUMN_USER_BIRTHDAY
          + " TEXT, "
          + COLUMN_USER_BUDGET
          + " INTEGER DEFAULT 0, "
          + COLUMN_USER_CITY
          + " TEXT"
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

  private static final String SQL_CREATE_LIKES =
      "CREATE TABLE "
          + TABLE_LIKES
          + " ("
          + COLUMN_LIKE_USER
          + " INTEGER, "
          + COLUMN_LIKE_TARGET
          + " INTEGER, "
          + COLUMN_IS_LIKE
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
    db.execSQL(SQL_CREATE_LIKES);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
    // Drop all tables in dependency order
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIKES);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERESTS);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
    // Recreate
    onCreate(db);
  }

  /*** ORIGINAL METHODS ***/

  // Fetch user ID by email
  public long getUserIdByEmail(String email) {
    SQLiteDatabase db = getReadableDatabase();
    Cursor c =
        db.query(
            TABLE_USERS,
            new String[] {COLUMN_ID},
            COLUMN_USER_EMAIL + "=?",
            new String[] {email},
            null,
            null,
            null);
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

  // Update profile details
  public void updateUserProfile(
      long userId, String first, String last, String gender, String birthday) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues cv = new ContentValues();
    cv.put(COLUMN_USER_FIRSTNAME, first);
    cv.put(COLUMN_USER_LASTNAME, last);
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

  public List<String> getUserPhotos(long userId) {
    List<String> list = new ArrayList<>();
    SQLiteDatabase db = getReadableDatabase();
    Cursor c =
        db.query(
            TABLE_PHOTOS,
            new String[] {COLUMN_PHOTO_URI},
            COLUMN_PHOTO_USER + "=?",
            new String[] {String.valueOf(userId)},
            null,
            null,
            null);
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

  public List<String> getUserInterests(long userId) {
    List<String> list = new ArrayList<>();
    SQLiteDatabase db = getReadableDatabase();
    Cursor c =
        db.query(
            TABLE_INTERESTS,
            new String[] {COLUMN_INTEREST_STRING},
            COLUMN_INTEREST_USER + "=?",
            new String[] {String.valueOf(userId)},
            null,
            null,
            null);
    while (c.moveToNext()) {
      list.add(c.getString(c.getColumnIndexOrThrow(COLUMN_INTEREST_STRING)));
    }
    c.close();
    return list;
  }

  /** Save budget & city for this user */
  public void updateUserPreferences(long userId, int budget, String city) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues cv = new ContentValues();
    cv.put(COLUMN_USER_BUDGET, budget);
    cv.put(COLUMN_USER_CITY, city);
    db.update(TABLE_USERS, cv, COLUMN_ID + "=?", new String[] {String.valueOf(userId)});
    db.close();
  }

  /** Get all other users as recommendations */
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
      u.budget = c.getInt(c.getColumnIndexOrThrow(COLUMN_USER_BUDGET));
      u.city = c.getString(c.getColumnIndexOrThrow(COLUMN_USER_CITY));
      list.add(u);
    }
    c.close();
    return list;
  }

  /** Record a like (true) or dislike (false) */
  public void markLike(long userId, long targetId, boolean isLike) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues cv = new ContentValues();
    cv.put(COLUMN_LIKE_USER, userId);
    cv.put(COLUMN_LIKE_TARGET, targetId);
    cv.put(COLUMN_IS_LIKE, isLike ? 1 : 0);
    db.insertWithOnConflict(TABLE_LIKES, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    db.close();
  }
}
