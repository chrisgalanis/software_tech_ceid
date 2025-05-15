package com.example.roomie;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREFS_NAME     = "roomie_session";
    private static final String KEY_USER_ID    = "key_user_id";

    private static SessionManager instance;
    private final SharedPreferences prefs;

    private SessionManager(Context ctx) {
        prefs = ctx.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void init(Context ctx) {
        if (instance == null) {
            instance = new SessionManager(ctx);
        }
    }

    public static SessionManager get() {
        if (instance == null) {
            throw new IllegalStateException("SessionManager not initialized!");
        }
        return instance;
    }

    public void setUserId(long userId) {
        prefs.edit().putLong(KEY_USER_ID, userId).apply();
    }

    public long getUserId() {
        return prefs.getLong(KEY_USER_ID, -1L);
    }
}
