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

    private static final String DATABASE_NAME    = "RoomieApp.db";
    private static final int    DATABASE_VERSION = 12;   // bumped to include reports table

    // ─── USERS TABLE ─────────────────────────────────────────────────────────────
    public static final String TABLE_USERS           = "users";
    public static final String COLUMN_ID             = "_id";
    public static final String COLUMN_USER_EMAIL     = "email";
    public static final String COLUMN_USER_PASSWORD  = "password";
    public static final String COLUMN_USER_FIRSTNAME = "first_name";
    public static final String COLUMN_USER_LASTNAME  = "last_name";
    public static final String COLUMN_USER_GENDER    = "gender";
    public static final String COLUMN_USER_BIRTHDAY  = "birthday";
    public static final String COLUMN_USER_BUDGET_FROM = "budget_from";
    public static final String COLUMN_USER_BUDGET_TO   = "budget_to";
    public static final String COLUMN_USER_CITY      = "city";

    // ─── PHOTOS TABLE ────────────────────────────────────────────────────────────
    public static final String TABLE_PHOTOS       = "user_photos";
    public static final String COLUMN_PHOTO_ID    = "_id";
    public static final String COLUMN_PHOTO_USER  = "user_id";
    public static final String COLUMN_PHOTO_URI   = "uri";

    // ─── INTERESTS TABLE ─────────────────────────────────────────────────────────
    public static final String TABLE_INTERESTS        = "user_interests";
    public static final String COLUMN_INTEREST_ID     = "_id";
    public static final String COLUMN_INTEREST_USER   = "user_id";
    public static final String COLUMN_INTEREST_STRING = "interest";

    // ─── LIKES TABLE ─────────────────────────────────────────────────────────────
    public static final String TABLE_LIKES         = "likes";
    public static final String COLUMN_LIKE_USER    = "userId";
    public static final String COLUMN_LIKE_TARGET  = "targetId";

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
    // ─── CREATE TABLE STATEMENTS ─────────────────────────────────────────────────

    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " ("
                    + COLUMN_ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_USER_EMAIL       + " TEXT UNIQUE, "
                    + COLUMN_USER_PASSWORD    + " TEXT, "
                    + COLUMN_USER_FIRSTNAME   + " TEXT, "
                    + COLUMN_USER_LASTNAME    + " TEXT, "
                    + COLUMN_USER_GENDER      + " TEXT, "
                    + COLUMN_USER_BIRTHDAY    + " TEXT, "
                    + COLUMN_USER_BUDGET_FROM + " INTEGER DEFAULT 0, "
                    + COLUMN_USER_BUDGET_TO   + " INTEGER DEFAULT 0, "
                    + COLUMN_USER_CITY        + " TEXT"
                    + ");";

    private static final String SQL_CREATE_PHOTOS =
            "CREATE TABLE " + TABLE_PHOTOS + " ("
                    + COLUMN_PHOTO_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_PHOTO_USER + " INTEGER, "
                    + COLUMN_PHOTO_URI  + " TEXT, "
                    + "FOREIGN KEY(" + COLUMN_PHOTO_USER + ") REFERENCES "
                    + TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
                    + ");";

    private static final String SQL_CREATE_INTERESTS =
            "CREATE TABLE " + TABLE_INTERESTS + " ("
                    + COLUMN_INTEREST_ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_INTEREST_USER   + " INTEGER, "
                    + COLUMN_INTEREST_STRING + " TEXT, "
                    + "FOREIGN KEY(" + COLUMN_INTEREST_USER + ") REFERENCES "
                    + TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
                    + ");";

    private static final String SQL_CREATE_LIKES =
            "CREATE TABLE " + TABLE_LIKES + " ("
                    + COLUMN_LIKE_USER   + " INTEGER, "
                    + COLUMN_LIKE_TARGET + " INTEGER, "
                    + "PRIMARY KEY(" + COLUMN_LIKE_USER + "," + COLUMN_LIKE_TARGET + "), "
                    + "FOREIGN KEY(" + COLUMN_LIKE_USER   + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE, "
                    + "FOREIGN KEY(" + COLUMN_LIKE_TARGET + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
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
        db.execSQL(SQL_CREATE_REPORTS);
        db.execSQL(SQL_CREATE_WARNINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Drop in reverse dependency order
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIKES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WARNINGS);
        onCreate(db);
    }

    // ─── ORIGINAL METHODS ────────────────────────────────────────────────────────

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

    public boolean isProfileComplete(long userId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] cols = {
                COLUMN_USER_FIRSTNAME,
                COLUMN_USER_LASTNAME,
                COLUMN_USER_GENDER,
                COLUMN_USER_BIRTHDAY
        };
        Cursor c = db.query(
                TABLE_USERS,
                cols,
                COLUMN_ID + "=?",
                new String[]{ String.valueOf(userId) },
                null, null, null
        );
        boolean complete = false;
        if (c.moveToFirst()) {
            boolean missingFirst  = c.isNull(c.getColumnIndexOrThrow(COLUMN_USER_FIRSTNAME))
                    || c.getString(c.getColumnIndexOrThrow(COLUMN_USER_FIRSTNAME)).trim().isEmpty();
            boolean missingLast   = c.isNull(c.getColumnIndexOrThrow(COLUMN_USER_LASTNAME))
                    || c.getString(c.getColumnIndexOrThrow(COLUMN_USER_LASTNAME)).trim().isEmpty();
            boolean missingGender = c.isNull(c.getColumnIndexOrThrow(COLUMN_USER_GENDER))
                    || c.getString(c.getColumnIndexOrThrow(COLUMN_USER_GENDER)).trim().isEmpty();
            boolean missingBirth  = c.isNull(c.getColumnIndexOrThrow(COLUMN_USER_BIRTHDAY))
                    || c.getString(c.getColumnIndexOrThrow(COLUMN_USER_BIRTHDAY)).trim().isEmpty();
            complete = !(missingFirst || missingLast || missingGender || missingBirth);
        }
        c.close();
        return complete;
    }

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
        db.close();
    }

    public void insertUserPhotos(long userId, List<Uri> uris) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (Uri uri : uris) {
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

    public void insertUserInterests(long userId, List<String> interests) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (String interest : interests) {
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

    public List<User> getRecommendations(long currentUserId) {
        List<User> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_USERS,
                null,
                COLUMN_ID + "!=?",
                new String[]{ String.valueOf(currentUserId) },
                null, null, null
        );
        while (c.moveToNext()) {
            User u = new User();
            u.id        = c.getLong(c.getColumnIndexOrThrow(COLUMN_ID));
            u.firstName = c.getString(c.getColumnIndexOrThrow(COLUMN_USER_FIRSTNAME));
            u.lastName  = c.getString(c.getColumnIndexOrThrow(COLUMN_USER_LASTNAME));
            u.minBudget = c.getInt(c.getColumnIndexOrThrow(COLUMN_USER_BUDGET_FROM));
            u.maxBudget = c.getInt(c.getColumnIndexOrThrow(COLUMN_USER_BUDGET_TO));
            u.city      = c.getString(c.getColumnIndexOrThrow(COLUMN_USER_CITY));
            list.add(u);
        }
        c.close();
        return list;
    }

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

    public void unlikeUser(long userId, long targetId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(
                TABLE_LIKES,
                COLUMN_LIKE_USER   + "=? AND " + COLUMN_LIKE_TARGET + "=?",
                new String[]{ String.valueOf(userId), String.valueOf(targetId) }
        );
        db.close();
    }

    public List<Long> getUsersWhoLikedMe(long userId) {
        List<Long> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_LIKES,
                new String[]{ COLUMN_LIKE_USER },
                COLUMN_LIKE_TARGET + "=?",
                new String[]{ String.valueOf(userId) },
                null, null, null
        );
        while (c.moveToNext()) {
            list.add(c.getLong(c.getColumnIndexOrThrow(COLUMN_LIKE_USER)));
        }
        c.close();
        return list;
    }

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
        }
        c.close();
        return u;
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
}
