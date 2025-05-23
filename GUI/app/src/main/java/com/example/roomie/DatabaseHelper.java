package com.example.roomie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME    = "RoomieApp.db";
    private static final int    DATABASE_VERSION = 7;  // bumped

    // Table: users
    public static final String TABLE_USERS            = "users";
    public static final String COLUMN_ID              = "_id";
    public static final String COLUMN_USER_EMAIL      = "email";
    public static final String COLUMN_USER_PASSWORD   = "password";
    public static final String COLUMN_USER_FIRSTNAME  = "first_name";
    public static final String COLUMN_USER_LASTNAME   = "last_name";
    public static final String COLUMN_USER_GENDER     = "gender";
    public static final String COLUMN_USER_BIRTHDAY   = "birthday";
    public static final String COLUMN_USER_AVATAR_URL = "avatar_url";

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

    // Table: houses
    public static final String TABLE_HOUSES         = "houses";
    public static final String COLUMN_HOUSE_ID      = "_id";
    public static final String COLUMN_HOUSE_OWNER   = "owner_id";     // user who added it
    public static final String COLUMN_HOUSE_OWNER_NAME = "owner_name";
    public static final String COLUMN_HOUSE_ADDRESS = "address";
    public static final String COLUMN_HOUSE_RENT    = "rent";
    public static final String COLUMN_HOUSE_AREA    = "area";
    public static final String COLUMN_HOUSE_FLOOR   = "floor";
    public static final String COLUMN_HOUSE_LAT     = "latitude";
    public static final String COLUMN_HOUSE_LNG     = "longitude";


    // table: house_photos
    public static final String TABLE_HOUSE_PHOTOS       = "house_photos";
    public static final String COLUMN_HOUSE_PHOTO_ID    = "_id";
    public static final String COLUMN_HOUSE_PHOTO_HOUSE = "house_id";
    public static final String COLUMN_HOUSE_PHOTO_URI   = "uri";

    // Create users table
    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " ("
                    + COLUMN_ID            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_USER_EMAIL    + " TEXT UNIQUE, "
                    + COLUMN_USER_PASSWORD + " TEXT, "
                    + COLUMN_USER_FIRSTNAME+ " TEXT, "
                    + COLUMN_USER_LASTNAME + " TEXT, "
                    + COLUMN_USER_GENDER   + " TEXT, "
                    + COLUMN_USER_BIRTHDAY + " TEXT, "
                    + COLUMN_USER_AVATAR_URL +
                    " TEXT"   // <-- new!
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


    // inside DatabaseHelper class

    private static final String SQL_CREATE_HOUSES =
            "CREATE TABLE " + TABLE_HOUSES + " ("
                    + COLUMN_HOUSE_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_HOUSE_OWNER   + " INTEGER, "
                    + COLUMN_HOUSE_ADDRESS + " TEXT, "
                    + COLUMN_HOUSE_RENT    + " REAL, "
                    + COLUMN_HOUSE_AREA    + " REAL, "
                    + COLUMN_HOUSE_FLOOR   + " INTEGER, "
                    + COLUMN_HOUSE_LAT     + " REAL, "
                    + COLUMN_HOUSE_LNG     + " REAL, "
                    + "FOREIGN KEY(" + COLUMN_HOUSE_OWNER + ") REFERENCES "
                    + TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
                    + ");";

    private static final String SQL_CREATE_HOUSE_PHOTOS =
            "CREATE TABLE " + TABLE_HOUSE_PHOTOS + " ("
                    + COLUMN_HOUSE_PHOTO_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_HOUSE_PHOTO_HOUSE + " INTEGER, "
                    + COLUMN_HOUSE_PHOTO_URI   + " TEXT, "
                    + "FOREIGN KEY(" + COLUMN_HOUSE_PHOTO_HOUSE + ") REFERENCES "
                    + TABLE_HOUSES + "(" + COLUMN_HOUSE_ID + ") ON DELETE CASCADE"
                    + ");";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // enable FKs so ON DELETE CASCADE works
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // drop in reverse order of dependencies
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOUSE_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOUSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    /** Get a user’s ID by their email. */
    public long getUserIdByEmail(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_USERS,
                new String[]{ COLUMN_ID },
                COLUMN_USER_EMAIL + "=?",
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

    /** Update both name, gender & birthday for this user in one go. */
    public void updateUserProfile(long userId,
                                  String first, String last,
                                  String gender, String birthday) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_FIRSTNAME, first);
        cv.put(COLUMN_USER_LASTNAME,  last);
        cv.put(COLUMN_USER_GENDER,    gender);
        cv.put(COLUMN_USER_BIRTHDAY,  birthday);
        db.update(
                TABLE_USERS,
                cv,
                COLUMN_ID + "=?",
                new String[]{ String.valueOf(userId) }
        );
    }

    /** Insert each photo URI for this user. */
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

    /** Pull back all photo-URIs for a user. */
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

    /** Insert each interest for this user. */
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

    /** Pull back all interests for a user. */
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

    /**
     * Returns true if first name, last name, gender and birthday are all non-empty
     */
    public boolean isProfileComplete(long userId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] cols = {
                COLUMN_USER_FIRSTNAME,
                COLUMN_USER_LASTNAME,
                COLUMN_USER_GENDER,
                COLUMN_USER_BIRTHDAY
        };
        String   sel  = COLUMN_ID + "=?";
        String[] args = { String.valueOf(userId) };

        Cursor c = db.query(
                TABLE_USERS,
                cols,
                sel,
                args,
                null, null, null
        );

        boolean complete = false;
        if (c.moveToFirst()) {
            // get column indexes once
            int idxFirst    = c.getColumnIndexOrThrow(COLUMN_USER_FIRSTNAME);
            int idxLast     = c.getColumnIndexOrThrow(COLUMN_USER_LASTNAME);
            int idxGender   = c.getColumnIndexOrThrow(COLUMN_USER_GENDER);
            int idxBirthday = c.getColumnIndexOrThrow(COLUMN_USER_BIRTHDAY);

            // check for NULL or blank
            boolean missingFirst    = c.isNull(idxFirst)    || c.getString(idxFirst).trim().isEmpty();
            boolean missingLast     = c.isNull(idxLast)     || c.getString(idxLast).trim().isEmpty();
            boolean missingGender   = c.isNull(idxGender)   || c.getString(idxGender).trim().isEmpty();
            boolean missingBirthday = c.isNull(idxBirthday) || c.getString(idxBirthday).trim().isEmpty();

            complete = !(missingFirst || missingLast || missingGender || missingBirthday);
        }

        c.close();
        return complete;
    }

    /** Insert a new house and return its ID */
    public long insertHouse(long ownerId,
                            String address,
                            double rent,
                            double area,
                            int floor,
                            double latitude,
                            double longitude) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_HOUSE_OWNER,   ownerId);
        cv.put(COLUMN_HOUSE_ADDRESS, address);
        cv.put(COLUMN_HOUSE_RENT,    rent);
        cv.put(COLUMN_HOUSE_AREA,    area);
        cv.put(COLUMN_HOUSE_FLOOR,   floor);
        cv.put(COLUMN_HOUSE_LAT,     latitude);
        cv.put(COLUMN_HOUSE_LNG,     longitude);
        return db.insert(TABLE_HOUSES, null, cv);
    }

    /** Insert photo URIs for a house */
    public void insertHousePhotos(long houseId, List<Uri> uris) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (Uri uri : uris) {
                if (uri == null) continue;
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_HOUSE_PHOTO_HOUSE, houseId);
                cv.put(COLUMN_HOUSE_PHOTO_URI,   uri.toString());
                db.insert(TABLE_HOUSE_PHOTOS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /** Fetch all houses (optionally you can filter via SQL WHERE) */
    public List<House> getAllHouses() {
        List<House> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String sql =
                "SELECT h." + COLUMN_HOUSE_ID      + ","
                        + "h." + COLUMN_HOUSE_OWNER   + ","
                        + "h." + COLUMN_HOUSE_ADDRESS + ","
                        + "h." + COLUMN_HOUSE_RENT    + ","
                        + "h." + COLUMN_HOUSE_AREA    + ","
                        + "h." + COLUMN_HOUSE_FLOOR   + ","
                        + "h." + COLUMN_HOUSE_LAT     + ","
                        + "h." + COLUMN_HOUSE_LNG     + ","
                        + "u." + COLUMN_USER_FIRSTNAME+ ","
                        + "u." + COLUMN_USER_LASTNAME + ","
                        + "u." + COLUMN_USER_AVATAR_URL +
                        " FROM " + TABLE_HOUSES + " h" +
                        " LEFT JOIN " + TABLE_USERS + " u" +
                        " ON h." + COLUMN_HOUSE_OWNER +
                        " = u." + COLUMN_ID;

        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            long   id        = c.getLong(0);
            long   ownerId   = c.getLong(1);
            String addr      = c.getString(2);
            double rent      = c.getDouble(3);
            double area      = c.getDouble(4);
            int    floor     = c.getInt(5);
            double lat       = c.getDouble(6);
            double lng       = c.getDouble(7);
            String firstName = c.getString(8);
            String lastName  = c.getString(9);
            String avatarUrl = c.getString(10);

            // load photos
            List<String> photos = new ArrayList<>();
            Cursor pc = db.query(
                    TABLE_HOUSE_PHOTOS,
                    new String[]{ COLUMN_HOUSE_PHOTO_URI },
                    COLUMN_HOUSE_PHOTO_HOUSE + "=?",
                    new String[]{ String.valueOf(id) },
                    null, null, null
            );
            while (pc.moveToNext()) {
                photos.add(pc.getString(
                        pc.getColumnIndexOrThrow(COLUMN_HOUSE_PHOTO_URI)));
            }
            pc.close();

            String ownerName =
                    ((firstName!=null?firstName:"") + " " +
                            (lastName !=null?lastName :"")).trim();

            list.add(new House(
                    id, ownerId, addr, rent, area, floor,
                    photos, ownerName, avatarUrl,
                    new LatLng(lat, lng)
            ));
        }
        c.close();
        return list;
    }



    // Fetch a single House (and its photos) by id, or null if not found.
    public House getHouseById(long houseId) {
        SQLiteDatabase db = getReadableDatabase();

        String sql =
                "SELECT h." + COLUMN_HOUSE_ID      + ","
                        + "h." + COLUMN_HOUSE_OWNER   + ","
                        + "h." + COLUMN_HOUSE_ADDRESS + ","
                        + "h." + COLUMN_HOUSE_RENT    + ","
                        + "h." + COLUMN_HOUSE_AREA    + ","
                        + "h." + COLUMN_HOUSE_FLOOR   + ","
                        + "h." + COLUMN_HOUSE_LAT     + ","
                        + "h." + COLUMN_HOUSE_LNG     + ","
                        + "u." + COLUMN_USER_FIRSTNAME+ ","
                        + "u." + COLUMN_USER_LASTNAME + ","
                        + "u." + COLUMN_USER_AVATAR_URL +
                        " FROM "   + TABLE_HOUSES + " h" +
                        " LEFT JOIN " + TABLE_USERS + " u" +
                        " ON h." + COLUMN_HOUSE_OWNER +
                        " = u." + COLUMN_ID +
                        " WHERE h." + COLUMN_HOUSE_ID + "=?";

        Cursor c = db.rawQuery(sql, new String[]{ String.valueOf(houseId) });
        if (!c.moveToFirst()) {
            c.close();
            return null;
        }

        long   id        = c.getLong(0);
        long   ownerId   = c.getLong(1);
        String addr      = c.getString(2);
        double rent      = c.getDouble(3);
        double area      = c.getDouble(4);
        int    floor     = c.getInt(5);
        double lat       = c.getDouble(6);
        double lng       = c.getDouble(7);
        String firstName = c.getString(8);
        String lastName  = c.getString(9);
        String avatarUrl = c.getString(10);
        c.close();

        // load photos (same as above)
        List<String> photos = new ArrayList<>();
        Cursor pc = db.query(
                TABLE_HOUSE_PHOTOS,
                new String[]{ COLUMN_HOUSE_PHOTO_URI },
                COLUMN_HOUSE_PHOTO_HOUSE + "=?",
                new String[]{ String.valueOf(houseId) },
                null, null, null
        );
        while (pc.moveToNext()) {
            photos.add(pc.getString(
                    pc.getColumnIndexOrThrow(COLUMN_HOUSE_PHOTO_URI)));
        }
        pc.close();

        String ownerName = ((firstName!=null?firstName:"") + " " +
                (lastName !=null?lastName :"")).trim();

        return new House(
                id, ownerId, addr, rent, area, floor,
                photos, ownerName, avatarUrl,
                new LatLng(lat, lng)
        );
    }

    /** Fetch a user’s email by their ID */
    public String getUserEmailById(long userId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_USERS,
                new String[]{ COLUMN_USER_EMAIL },
                COLUMN_ID + "=?",
                new String[]{ String.valueOf(userId) },
                null, null, null
        );
        String email = "";
        if (c.moveToFirst()) {
            email = c.getString(c.getColumnIndexOrThrow(COLUMN_USER_EMAIL));
        }
        c.close();
        return email;
    }


    /**
     * Insert a few mock houses + photos for development/demo purposes.
     * Owner ID should be a valid user in your users table (e.g. currentUserId).
     */
    /**
     * Insert exactly one demo house for this owner.
     * Chooses from 3 templates based on ownerId % 3.
     */
    public void seedMockHouses(long ownerId) {
        // 1) define parallel arrays of demo data
        String[] addrs = {
                "Lemesou 20, Patras",
                "Agiou Nikolaou 5, Patras",
                "Sisinis 25, Patras"
        };
        double[] rents   = {180.0, 200.0, 400.0};
        double[] areas   = {88.0, 95.0, 109.0};
        int[]    floors  = {3, 2, 4};
        double[] lats    = {38.2599, 38.2489, 38.2429};
        double[] lngs    = {21.7423, 21.7352, 21.7363};
        String[][] photoUrls = {
                {"https://example.com/img1.jpg", "https://example.com/img2.jpg"},
                {"https://example.com/a1.jpg",    "https://example.com/a2.jpg"},
                {"https://example.com/b1.jpg",    "https://example.com/b2.jpg"}
        };

        // 2) pick template index by ownerId so each user gets one unique house
        long houseId = -1;
        int idx = (int) (ownerId % addrs.length); // Use modulo to cycle through templates

        // 3) insert house
        houseId = insertHouse(
                ownerId,
                addrs[idx],
                rents[idx],
                areas[idx],
                floors[idx],
                lats[idx],
                lngs[idx]
        );

        // 4) insert its photos
        List<Uri> uris = new ArrayList<>();
        for (String url : photoUrls[idx]) {
            uris.add(Uri.parse(url));
        }
        insertHousePhotos(houseId, uris);
    }


    /**
     * If there are no houses yet, ensure each demo user exists
     * (insert or lookup by email), then seed a single batch of houses
     * for each one.
     */
    public void seedMockDataIfEmpty() {
        SQLiteDatabase db = getWritableDatabase();

        // 1) If we already have ANY houses, bail out
        long houseCount = DatabaseUtils.longForQuery(
                db, "SELECT COUNT(*) FROM " + TABLE_HOUSES, null
        );
        if (houseCount > 0) {
            return;
        }

        // 2) Define your demo users
        String[][] DEMO_USERS = {
                { "kwsmav@gmail.com",  "password123", "Kwstas", "Mavridis",   "M", "2004-04-05", "https://i.pravatar.cc/150?u=kwstas" },
                { "raftheman@gmail.com","hunter2",     "Rafas",  "Pieris",     "M", "2003-11-03", "https://i.pravatar.cc/150?u=rafas"  },
                { "mikemits@gmail.com", "mikepass",    "Mike",   "Mitsainas",  "M", "2004-09-03", "https://i.pravatar.cc/150?u=mike"   }
        };

        // 3) For each demo user: lookup or insert, collect ID
        List<Long> ownerIds = new ArrayList<>(DEMO_USERS.length);
        for (String[] u : DEMO_USERS) {
            String email = u[0];
            long userId  = getUserIdByEmail(email);
            if (userId < 0) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_USER_EMAIL,     u[0]);
                cv.put(COLUMN_USER_PASSWORD,  u[1]);
                cv.put(COLUMN_USER_FIRSTNAME, u[2]);
                cv.put(COLUMN_USER_LASTNAME,  u[3]);
                cv.put(COLUMN_USER_GENDER,    u[4]);
                cv.put(COLUMN_USER_BIRTHDAY,  u[5]);
                cv.put(COLUMN_USER_AVATAR_URL,u[6]);
                userId = db.insert(TABLE_USERS, null, cv);
            }
            ownerIds.add(userId);
        }

        // 4) Seed exactly one batch of houses for each of those IDs
        for (long ownerId : ownerIds) {
            seedMockHouses(ownerId);
        }
    }


    /** Completely remove all houses + photos. */
    public void clearAllHousesAndPhotos() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_HOUSE_PHOTOS, null, null);
            db.delete(TABLE_HOUSES,       null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

}

