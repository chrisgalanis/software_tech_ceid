package com.example.roomie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "RoomieApp.db";
  private static final int DATABASE_VERSION = 10;

  // Table: users
  public static final String TABLE_USERS = "users";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_USER_EMAIL = "email";
  public static final String COLUMN_USER_PASSWORD = "password";
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
  public static final String TABLE_PHOTOS = "user_photos";
  public static final String COLUMN_PHOTO_ID = "_id";
  public static final String COLUMN_PHOTO_USER = "user_id";
  public static final String COLUMN_PHOTO_URI = "uri";

  // interests table
  public static final String TABLE_INTERESTS = "user_interests";
  public static final String COLUMN_INTEREST_ID = "_id";
  public static final String COLUMN_INTEREST_USER = "user_id";
  public static final String COLUMN_INTEREST_STRING = "interest";

  // Table: houses
  public static final String TABLE_HOUSES = "houses";
  public static final String COLUMN_HOUSE_ID = "_id";
  public static final String COLUMN_HOUSE_OWNER = "owner_id";
  public static final String COLUMN_HOUSE_OWNER_NAME = "owner_name";
  public static final String COLUMN_HOUSE_ADDRESS = "address";
  public static final String COLUMN_HOUSE_RENT = "rent";
  public static final String COLUMN_HOUSE_AREA = "area";
  public static final String COLUMN_HOUSE_FLOOR = "floor";
  public static final String COLUMN_HOUSE_LAT = "latitude";
  public static final String COLUMN_HOUSE_LNG = "longitude";

  // table: house_photos
  public static final String TABLE_HOUSE_PHOTOS = "house_photos";
  public static final String COLUMN_HOUSE_PHOTO_ID = "_id";
  public static final String COLUMN_HOUSE_PHOTO_HOUSE = "house_id";
  public static final String COLUMN_HOUSE_PHOTO_URI = "uri";

  // likes table
  public static final String TABLE_LIKES = "likes";
  public static final String COLUMN_LIKE_USER = "userId";
  public static final String COLUMN_LIKE_TARGET = "targetId";
  public static final String COLUMN_IS_LIKE = "isLike";

  // Table: matches
  public static final String TABLE_MATCHES = "matches";
  public static final String COLUMN_MATCH_ID = "match_id";
  public static final String COLUMN_MATCHES_USER_ID = "user_id";
  public static final String COLUMN_MATCH_TIMESTAMP = "match_timestamp";

  // chat messages
  public static final String TABLE_CHAT_MESSAGES = "chat_messages";
  public static final String COLUMN_MESSAGE_ID = "_id";
  public static final String COLUMN_MESSAGE_SENDER_ID = "sender_user_id";
  public static final String COLUMN_MESSAGE_RECEIVER_ID = "receiver_user_id";
  public static final String COLUMN_MESSAGE_TEXT = "message_text";
  public static final String COLUMN_MESSAGE_TIMESTAMP_MS = "timestamp_ms";

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
          + COLUMN_USER_AVATAR_URL
          + " TEXT, "
          + COLUMN_USER_BUDGET_FROM
          + " INTEGER DEFAULT 0, "
          + COLUMN_USER_BUDGET_TO
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
          + " INTEGER NOT NULL, "
          + COLUMN_MATCHES_USER_ID
          + " INTEGER NOT NULL, "
          + "FOREIGN KEY("
          + COLUMN_MATCHES_USER_ID
          + ") REFERENCES "
          + TABLE_USERS
          + "("
          + COLUMN_ID
          + ") ON DELETE CASCADE, "
          + "PRIMARY KEY ("
          + COLUMN_MATCH_ID
          + ", "
          + COLUMN_MATCHES_USER_ID
          + ")"
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
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
    // Drop tables in reverse-dependency order
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_MESSAGES);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIKES);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOUSE_PHOTOS);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOUSES);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERESTS);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
    onCreate(db);
  }

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

  // Save budget & city for this user
  public void updateUserPreferences(long userId, int from, int to, String city) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues cv = new ContentValues();
    cv.put(COLUMN_USER_BUDGET_FROM, from);
    cv.put(COLUMN_USER_BUDGET_TO, to);
    cv.put(COLUMN_USER_CITY, city);
    db.update(TABLE_USERS, cv, COLUMN_ID + "=?", new String[] {String.valueOf(userId)});
    db.close();
  }

  // Insert a new house and return its ID
  public long insertHouse(
      long ownerId,
      String address,
      double rent,
      double area,
      int floor,
      double latitude,
      double longitude) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues cv = new ContentValues();
    cv.put(COLUMN_HOUSE_OWNER, ownerId);
    cv.put(COLUMN_HOUSE_ADDRESS, address);
    cv.put(COLUMN_HOUSE_RENT, rent);
    cv.put(COLUMN_HOUSE_AREA, area);
    cv.put(COLUMN_HOUSE_FLOOR, floor);
    cv.put(COLUMN_HOUSE_LAT, latitude);
    cv.put(COLUMN_HOUSE_LNG, longitude);
    return db.insert(TABLE_HOUSES, null, cv);
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

  // Fetch all houses
  public List<House> getAllHouses() {
    List<House> list = new ArrayList<>();
    SQLiteDatabase db = getReadableDatabase();

    String sql =
        "SELECT h."
            + COLUMN_HOUSE_ID
            + ","
            + "h."
            + COLUMN_HOUSE_OWNER
            + ","
            + "h."
            + COLUMN_HOUSE_ADDRESS
            + ","
            + "h."
            + COLUMN_HOUSE_RENT
            + ","
            + "h."
            + COLUMN_HOUSE_AREA
            + ","
            + "h."
            + COLUMN_HOUSE_FLOOR
            + ","
            + "h."
            + COLUMN_HOUSE_LAT
            + ","
            + "h."
            + COLUMN_HOUSE_LNG
            + ","
            + "u."
            + COLUMN_USER_FIRSTNAME
            + ","
            + "u."
            + COLUMN_USER_LASTNAME
            + ","
            + "u."
            + COLUMN_USER_AVATAR_URL
            + " FROM "
            + TABLE_HOUSES
            + " h"
            + " LEFT JOIN "
            + TABLE_USERS
            + " u"
            + " ON h."
            + COLUMN_HOUSE_OWNER
            + " = u."
            + COLUMN_ID;

    Cursor c = db.rawQuery(sql, null);
    while (c.moveToNext()) {
      long id = c.getLong(0);
      long ownerId = c.getLong(1);
      String addr = c.getString(2);
      double rent = c.getDouble(3);
      double area = c.getDouble(4);
      int floor = c.getInt(5);
      double lat = c.getDouble(6);
      double lng = c.getDouble(7);
      String firstName = c.getString(8);
      String lastName = c.getString(9);
      String avatarUrl = c.getString(10);

      List<String> photos = new ArrayList<>();
      Cursor pc =
          db.query(
              TABLE_HOUSE_PHOTOS,
              new String[] {COLUMN_HOUSE_PHOTO_URI},
              COLUMN_HOUSE_PHOTO_HOUSE + "=?",
              new String[] {String.valueOf(id)},
              null,
              null,
              null);
      while (pc.moveToNext()) {
        photos.add(pc.getString(pc.getColumnIndexOrThrow(COLUMN_HOUSE_PHOTO_URI)));
      }
      pc.close();

      String ownerName =
          ((firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "")).trim();

      list.add(
          new House(
              id,
              ownerId,
              addr,
              rent,
              area,
              floor,
              photos,
              ownerName,
              avatarUrl,
              new LatLng(lat, lng)));
    }
    c.close();
    return list;
  }

  // Fetch a single House by id
  public House getHouseById(long houseId) {
    SQLiteDatabase db = getReadableDatabase();

    String sql =
        "SELECT h."
            + COLUMN_HOUSE_ID
            + ","
            + "h."
            + COLUMN_HOUSE_OWNER
            + ","
            + "h."
            + COLUMN_HOUSE_ADDRESS
            + ","
            + "h."
            + COLUMN_HOUSE_RENT
            + ","
            + "h."
            + COLUMN_HOUSE_AREA
            + ","
            + "h."
            + COLUMN_HOUSE_FLOOR
            + ","
            + "h."
            + COLUMN_HOUSE_LAT
            + ","
            + "h."
            + COLUMN_HOUSE_LNG
            + ","
            + "u."
            + COLUMN_USER_FIRSTNAME
            + ","
            + "u."
            + COLUMN_USER_LASTNAME
            + ","
            + "u."
            + COLUMN_USER_AVATAR_URL
            + " FROM "
            + TABLE_HOUSES
            + " h"
            + " LEFT JOIN "
            + TABLE_USERS
            + " u"
            + " ON h."
            + COLUMN_HOUSE_OWNER
            + " = u."
            + COLUMN_ID
            + " WHERE h."
            + COLUMN_HOUSE_ID
            + "=?";

    Cursor c = db.rawQuery(sql, new String[] {String.valueOf(houseId)});
    if (!c.moveToFirst()) {
      c.close();
      return null;
    }

    long id = c.getLong(0);
    long ownerId = c.getLong(1);
    String addr = c.getString(2);
    double rent = c.getDouble(3);
    double area = c.getDouble(4);
    int floor = c.getInt(5);
    double lat = c.getDouble(6);
    double lng = c.getDouble(7);
    String firstName = c.getString(8);
    String lastName = c.getString(9);
    String avatarUrl = c.getString(10);
    c.close();

    List<String> photos = new ArrayList<>();
    Cursor pc =
        db.query(
            TABLE_HOUSE_PHOTOS,
            new String[] {COLUMN_HOUSE_PHOTO_URI},
            COLUMN_HOUSE_PHOTO_HOUSE + "=?",
            new String[] {String.valueOf(houseId)},
            null,
            null,
            null);
    while (pc.moveToNext()) {
      photos.add(pc.getString(pc.getColumnIndexOrThrow(COLUMN_HOUSE_PHOTO_URI)));
    }
    pc.close();

    String ownerName =
        ((firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "")).trim();

    return new House(
        id, ownerId, addr, rent, area, floor, photos, ownerName, avatarUrl, new LatLng(lat, lng));
  }

  // Fetch a user’s email by their ID
  public String getUserEmailById(long userId) {
    SQLiteDatabase db = getReadableDatabase();
    Cursor c =
        db.query(
            TABLE_USERS,
            new String[] {COLUMN_USER_EMAIL},
            COLUMN_ID + "=?",
            new String[] {String.valueOf(userId)},
            null,
            null,
            null);
    String email = "";
    if (c.moveToFirst()) {
      email = c.getString(c.getColumnIndexOrThrow(COLUMN_USER_EMAIL));
    }
    c.close();
    return email;
  }

  // Insert mock houses
  public void seedMockHouses(long ownerId) {
    String[] addrs = {"Lemesou 20, Patras", "Agiou Nikolaou 5, Patras", "Sisinis 25, Patras"};
    double[] rents = {180.0, 200.0, 400.0};
    double[] areas = {88.0, 95.0, 109.0};
    int[] floors = {3, 2, 4};
    double[] lats = {38.2599, 38.2489, 38.2429};
    double[] lngs = {21.7423, 21.7352, 21.7363};
    String[][] photoUrls = {
      {"https://example.com/img1.jpg", "https://example.com/img2.jpg"},
      {"https://example.com/a1.jpg", "https://example.com/a2.jpg"},
      {"https://example.com/b1.jpg", "https://example.com/b2.jpg"}
    };

    long houseId = -1;
    int idx = (int) (ownerId % addrs.length);

    houseId =
        insertHouse(ownerId, addrs[idx], rents[idx], areas[idx], floors[idx], lats[idx], lngs[idx]);

    List<Uri> uris = new ArrayList<>();
    for (String url : photoUrls[idx]) {
      uris.add(Uri.parse(url));
    }
    insertHousePhotos(houseId, uris);
  }

  public void seedMockDataIfEmpty() {
    SQLiteDatabase db = getWritableDatabase();

    long houseCount = DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM " + TABLE_HOUSES, null);
    if (houseCount > 0) {
      return;
    }

    String[][] DEMO_USERS = {
      {
        "kwsmav@gmail.com",
        "password123",
        "Kwstas",
        "Mavridis",
        "M",
        "2004-04-05",
        "https://i.pravatar.cc/150?u=kwstas"
      },
      {
        "raftheman@gmail.com",
        "hunter2",
        "Rafas",
        "Pieris",
        "M",
        "2003-11-03",
        "https://i.pravatar.cc/150?u=rafas"
      },
      {
        "mikemits@gmail.com",
        "mikepass",
        "Mike",
        "Mitsainas",
        "M",
        "2004-09-03",
        "https://i.pravatar.cc/150?u=mike"
      }
    };

    List<Long> ownerIds = new ArrayList<>(DEMO_USERS.length);
    for (String[] u : DEMO_USERS) {
      String email = u[0];
      long userId = getUserIdByEmail(email);
      if (userId < 0) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_EMAIL, u[0]);
        cv.put(COLUMN_USER_PASSWORD, u[1]);
        cv.put(COLUMN_USER_FIRSTNAME, u[2]);
        cv.put(COLUMN_USER_LASTNAME, u[3]);
        cv.put(COLUMN_USER_GENDER, u[4]);
        cv.put(COLUMN_USER_BIRTHDAY, u[5]);
        cv.put(COLUMN_USER_AVATAR_URL, u[6]);
        userId = db.insert(TABLE_USERS, null, cv);
      }
      ownerIds.add(userId);
    }

    for (long ownerId : ownerIds) {
      seedMockHouses(ownerId);
    }
  }

  public void seedMockUsers() {
    if (getUserIdByEmail("mikmits@gmail.com") < 0) {
      insertUser("mikmits@gmail.com", "pass123", "Mike", "Mitsainas", "M", "2004-09-13");
      insertUser("kwsmav@gmail.com", "qwerty", "Kwstas", "Mavridis", "M", "2004-04-05");
    }
  }

  // Completely remove all houses + photos
  public void clearAllHousesAndPhotos() {
    SQLiteDatabase db = getWritableDatabase();
    db.beginTransaction();
    try {
      db.delete(TABLE_HOUSE_PHOTOS, null, null);
      db.delete(TABLE_HOUSES, null, null);
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
    }
  }

  // Record a like or dislike
  public void likeUser(long userId, long targetId) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues cv = new ContentValues();
    cv.put(COLUMN_LIKE_USER, userId);
    cv.put(COLUMN_LIKE_TARGET, targetId);
    db.insertWithOnConflict(TABLE_LIKES, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
    db.close();
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

  // Fetch a User object by its ID
  public User getUserById(long id) {
    SQLiteDatabase db = getReadableDatabase();
    Cursor c =
        db.query(
            TABLE_USERS,
            null,
            COLUMN_ID + "=?",
            new String[] {String.valueOf(id)},
            null,
            null,
            null);
    User u = null;
    if (c.moveToFirst()) {
      u = new User();
      u.id = c.getLong(c.getColumnIndexOrThrow(COLUMN_ID));
      u.firstName = c.getString(c.getColumnIndexOrThrow(COLUMN_USER_FIRSTNAME));
      u.lastName = c.getString(c.getColumnIndexOrThrow(COLUMN_USER_LASTNAME));
    }
    c.close();
    return u;
  }

  public long insertMatchEntry(long matchEventId, long userId) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(COLUMN_MATCH_ID, matchEventId);
    values.put(COLUMN_MATCHES_USER_ID, userId);

    long newRowId = -1;
    try {
      newRowId = db.insertOrThrow(TABLE_MATCHES, null, values);
    } catch (android.database.sqlite.SQLiteConstraintException e) {
      Log.e("DatabaseHelper", "Constraint violation on inserting match entry: " + e.getMessage());
    } catch (Exception e) {
      Log.e("DatabaseHelper", "Error inserting match entry: " + e.getMessage());
    }
    return newRowId;
  }

  public long createMatch(long userId1, long userId2) {
    if (userId1 == userId2) {
      Log.w("DatabaseHelper", "Cannot create match: userId1 and userId2 are the same.");
      return -1;
    }

    SQLiteDatabase db = this.getWritableDatabase();
    long newMatchEventId = -1;

    Cursor c =
        db.rawQuery(
            "SELECT IFNULL(MAX(" + COLUMN_MATCH_ID + "), 0) + 1 FROM " + TABLE_MATCHES, null);
    if (c.moveToFirst()) {
      newMatchEventId = c.getLong(0);
    }
    c.close();

    if (newMatchEventId == 0) newMatchEventId = 1;

    if (newMatchEventId == -1) {
      Log.e("DatabaseHelper", "Failed to generate a new match_event_id.");
      return -1;
    }

    db.beginTransaction();
    try {
      ContentValues values1 = new ContentValues();
      values1.put(COLUMN_MATCH_ID, newMatchEventId);
      values1.put(COLUMN_MATCHES_USER_ID, userId1);
      long result1 = db.insert(TABLE_MATCHES, null, values1);

      ContentValues values2 = new ContentValues();
      values2.put(COLUMN_MATCH_ID, newMatchEventId);
      values2.put(COLUMN_MATCHES_USER_ID, userId2);
      long result2 = db.insert(TABLE_MATCHES, null, values2);

      if (result1 != -1 && result2 != -1) {
        db.setTransactionSuccessful();
        Log.d("DatabaseHelper", "Match created successfully with event ID: " + newMatchEventId);
        return newMatchEventId;
      } else {
        Log.e("DatabaseHelper", "Failed to insert match entries.");
        return -1;
      }
    } catch (Exception e) {
      Log.e("DatabaseHelper", "Error during createMatch transaction: " + e.getMessage());
      return -1;
    } finally {
      db.endTransaction();
    }
  }

  public List<Long> getMatchedUserIds(long userId) {
    List<Long> matchedUserIds = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();

    String queryString =
        "SELECT DISTINCT m2."
            + COLUMN_MATCHES_USER_ID
            + " FROM "
            + TABLE_MATCHES
            + " m1"
            + " INNER JOIN "
            + TABLE_MATCHES
            + " m2 ON m1."
            + COLUMN_MATCH_ID
            + " = m2."
            + COLUMN_MATCH_ID
            + " WHERE m1."
            + COLUMN_MATCHES_USER_ID
            + " = ? AND m2."
            + COLUMN_MATCHES_USER_ID
            + " != ?";

    Cursor cursor =
        db.rawQuery(queryString, new String[] {String.valueOf(userId), String.valueOf(userId)});
    try {
      if (cursor.moveToFirst()) {
        do {
          matchedUserIds.add(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_MATCHES_USER_ID)));
        } while (cursor.moveToNext());
      }
    } catch (Exception e) {
      Log.e("DatabaseHelper", "Error getting matched user IDs: " + e.getMessage());
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return matchedUserIds;
  }

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
      long currentUserId, long chatPartnerId, int chatPartnerProfileImageRes) {
    List<ChatMessage> messages = new ArrayList<>();
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
      String.valueOf(currentUserId), String.valueOf(chatPartnerId),
      String.valueOf(chatPartnerId), String.valueOf(currentUserId)
    };
    String orderBy = COLUMN_MESSAGE_TIMESTAMP_MS + " ASC";

    Cursor cursor = null;
    try {
      cursor = db.query(TABLE_CHAT_MESSAGES, null, selection, selectionArgs, null, null, orderBy);
      if (cursor != null && cursor.moveToFirst()) {
        do {
          String senderIdFromDbStr =
              String.valueOf(
                  cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_SENDER_ID)));
          String receiverIdFromDbStr =
              String.valueOf(
                  cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_RECEIVER_ID)));
          String messageText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_TEXT));
          long timestampMs =
              cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_TIMESTAMP_MS));

          String timeString =
              android.text.format.DateFormat.format("HH:mm", timestampMs).toString();
          boolean isSentByCurrentUser = (Long.parseLong(senderIdFromDbStr) == currentUserId);
          int profileImgRes = isSentByCurrentUser ? 0 : chatPartnerProfileImageRes;

          String chatMessageSenderId =
              isSentByCurrentUser ? String.valueOf(currentUserId) : String.valueOf(chatPartnerId);
          String chatMessageReceiverId =
              isSentByCurrentUser ? String.valueOf(chatPartnerId) : String.valueOf(currentUserId);

          ChatMessage message =
              new ChatMessage(
                  chatMessageSenderId,
                  chatMessageReceiverId,
                  messageText,
                  timeString,
                  isSentByCurrentUser,
                  isSentByCurrentUser ? 0 : chatPartnerProfileImageRes);
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
    return messages;
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
}
