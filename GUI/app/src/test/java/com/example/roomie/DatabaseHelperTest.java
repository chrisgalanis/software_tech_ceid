package com.example.roomie;

import static org.junit.Assert.*;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.test.core.app.ApplicationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 28)
public class DatabaseHelperTest {
    private static final String DB_NAME = "RoomieApp.db";

    private Context context;
    private DatabaseHelper dbHelper;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        // Ensure a clean database
        context.deleteDatabase(DB_NAME);
        dbHelper = new DatabaseHelper(context);
    }

    @After
    public void tearDown() {
        dbHelper.close();
        context.deleteDatabase(DB_NAME);
    }

    @Test
    public void testInsertAndGetUserByEmail_andGetUserEmailById() {
        long id = dbHelper.insertUser("user@example.com", "pass", "First", "Last", "F", "1990-01-01");
        assertTrue(id > 0);
        long fetched = dbHelper.getUserIdByEmail("user@example.com");
        assertEquals(id, fetched);

        String email = DatabaseUtils.stringForQuery(
                dbHelper.getReadableDatabase(),
                "SELECT " + DatabaseHelper.COLUMN_USER_EMAIL + " FROM " + DatabaseHelper.TABLE_USERS + " WHERE " + DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{ String.valueOf(id) }
        );
        assertEquals("user@example.com", email);
    }

    @Test
    public void testProfileCompleteness_andUpdateUserProfile() {
        long id = dbHelper.insertUser("p@c.com", "p", null, null, null, null);
        assertFalse(dbHelper.isProfileComplete(id));

        dbHelper.updateUserProfile(id, "First", "Last", "M", "1985-05-05");
        assertTrue(dbHelper.isProfileComplete(id));
    }

    @Test
    public void testInsertAndGetUserPhotos() {
        long id = dbHelper.insertUser("a@b.com","pw","F","L","M","2000-01-01");
        List<Uri> uris = Arrays.asList(Uri.parse("uri1"), null, Uri.parse("uri2"));
        dbHelper.insertUserPhotos(id, uris);
        List<String> photos = dbHelper.getUserPhotos(id);
        assertEquals(Arrays.asList("uri1", "uri2"), photos);
    }

    @Test
    public void testInsertAndGetUserInterests() {
        long id = dbHelper.insertUser("i@t.com","pw","F","L","F","2001-02-02");
        List<String> interests = Arrays.asList("music", "sports");
        dbHelper.insertUserInterests(id, interests);
        List<String> fetched = dbHelper.getUserInterests(id);
        assertEquals(interests, fetched);
    }

    @Test
    public void testUpdateUserPreferences_andGetUserById() {
        long id = dbHelper.insertUser("pref@c.com","pw","F","L","F","2002-03-03");
        dbHelper.updateUserPreferences(id, 100, 500, "Athens");
        User u = dbHelper.getUserById(id);
        assertNotNull(u);
        assertEquals(100, u.minBudget);
        assertEquals(500, u.maxBudget);
        assertEquals("Athens", u.city);
    }

    @Test
    public void testFindOrInsertUser() {
        long first = dbHelper.findOrInsertUser("dup@e.com", "pw", "F", "L", "M", "2003-04-04");
        assertTrue(first > 0);
        long second = dbHelper.findOrInsertUser("dup@e.com", "pw2", "X", "Y", "F", "1999-09-09");
        assertEquals(first, second);
    }

    @Test
    public void testGetRecommendations() {
        long u1 = dbHelper.insertUser("r1@c.com","pw","A","B","M","1991-01-01");
        long u2 = dbHelper.insertUser("r2@c.com","pw","C","D","F","1992-02-02");
        long u3 = dbHelper.insertUser("r3@c.com","pw","E","F","M","1993-03-03");
        List<User> recs = dbHelper.getRecommendations(u2);
        assertEquals(2, recs.size());
    }

    @Test
    public void testLikeUnlikeAndGetUsersWhoLikedMe() {
        long a = dbHelper.insertUser("a@x.com","pw","A","X","M","1980-01-01");
        long b = dbHelper.insertUser("b@y.com","pw","B","Y","F","1981-02-02");
        dbHelper.likeUser(a, b);
        List<Long> likers = dbHelper.getUsersWhoLikedMe(b);
        assertEquals(Collections.singletonList(a), likers);

        dbHelper.unlikeUser(a, b);
        likers = dbHelper.getUsersWhoLikedMe(b);
        assertTrue(likers.isEmpty());
    }

    @Test
    public void testCreateAndGetMatches() {
        long a = dbHelper.insertUser("m1@c.com","pw","M","One","M","1970-01-01");
        long b = dbHelper.insertUser("m2@c.com","pw","M","Two","F","1971-02-02");
        assertTrue(dbHelper.createMatch(a, b));
        List<Long> matchesA = dbHelper.getMatches(a);
        List<Long> matchesB = dbHelper.getMatches(b);
        assertEquals(Collections.singletonList(b), matchesA);
        assertEquals(Collections.singletonList(a), matchesB);

        // Self-match should be ignored
        assertFalse(dbHelper.createMatch(a, a));
    }

    @Test
    public void testChatMessages_andHasMessages() {
        long a = dbHelper.insertUser("c1@c.com","pw","C","One","M","1960-01-01");
        long b = dbHelper.insertUser("c2@c.com","pw","C","Two","F","1961-02-02");
        long msg1 = dbHelper.addChatMessage(a, b, "Hello", 1000L);
        long msg2 = dbHelper.addChatMessage(b, a, "Hi", 2000L);
        assertTrue(msg1 > 0);
        assertTrue(msg2 > 0);

        assertTrue(dbHelper.hasMessagesBetweenUsers(a, b));
        List<ChatMessage> conv = dbHelper.getChatMessagesBetweenUsers(a, b);
        assertEquals(2, conv.size());
        ChatMessage latest = dbHelper.getLatestChatMessage(a, b);
        assertEquals("Hi", latest.messageText);
    }

    @Test
    public void testReportsLifecycle() {
        long reported = dbHelper.insertUser("rep@c.com","pw","R","E","M","1950-01-01");
        long rid = dbHelper.insertReport(reported, "Inappropriate");
        assertTrue(rid > 0);
        List<Report> pending = dbHelper.getAllPendingReports();
        assertFalse(pending.isEmpty());

        dbHelper.dismissReport(rid);
        pending = dbHelper.getAllPendingReports();
        assertTrue(pending.isEmpty());

        long rid2 = dbHelper.insertReport(reported, "Spam");
        dbHelper.fullfillReport(rid2);
        pending = dbHelper.getAllPendingReports();
        assertTrue(pending.isEmpty());
    }

    @Test
    public void testWarningsLifecycle() {
        long uid = dbHelper.insertUser("w@c.com","pw","W","User","F","1940-04-04");
        Warning w = new Warning(0, uid, "Warning text", "PENDING", null);
        long wid = dbHelper.insertWarning(w);
        assertTrue(wid > 0);
        List<Warning> ws = dbHelper.getWarningsForUser(uid);
        assertFalse(ws.isEmpty());

        dbHelper.acknowledgeWarning(wid);
        ws = dbHelper.getWarningsForUser(uid);
        assertEquals("ACKNOWLEDGED", ws.get(0).status);
    }

    @Test
    public void testHouseCRUD_andListingApproval() {
        // 1) Insert owner & house
        long owner = dbHelper.insertUser("h@c.com","pw","H","Owner","M","1930-03-03");
        assertTrue(owner > 0);

        // Create a House object with id=0 (will be set by insertHouse)
        House h = new House(0, owner, "Addr", 100, 90, 3, 38.2, 28.4, Collections.emptyList());
        long hid = dbHelper.insertHouse(h);
        assertTrue(hid > 0);
        h.id = hid;  // sync our in-memory object

        // 2) Photos CRUD
        List<Uri> uris = Arrays.asList(Uri.parse("p1"), Uri.parse("p2"));
        dbHelper.insertHousePhotos(hid, uris);
        List<String> photos = dbHelper.getHousePhotos(hid);
        assertEquals(Arrays.asList("p1", "p2"), photos);

        // 3) Listings CRUD
        HouseListing listing = new HouseListing(h, "OwnerName", "", "", false);
        long lid = dbHelper.insertHouseListing(listing);
        assertTrue(lid > 0);
        assertTrue(dbHelper.approveListing(listing));
        assertTrue(dbHelper.disapproveListing(listing));

        // 4) Inline “getAllHouses” logic
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_HOUSES,
                new String[] {
                        DatabaseHelper.COLUMN_HOUSE_ID,
                        DatabaseHelper.COLUMN_HOUSE_OWNER,
                        DatabaseHelper.COLUMN_HOUSE_ADDRESS,
                        DatabaseHelper.COLUMN_HOUSE_RENT,
                        DatabaseHelper.COLUMN_HOUSE_AREA,
                        DatabaseHelper.COLUMN_HOUSE_FLOOR,
                        DatabaseHelper.COLUMN_HOUSE_LAT,
                        DatabaseHelper.COLUMN_HOUSE_LNG
                },
                null, null, null, null, null
        );

        List<House> houses = new ArrayList<>();
        while (cursor.moveToNext()) {
            long   id      = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HOUSE_ID));
            long   ownerId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HOUSE_OWNER));
            String addr    = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HOUSE_ADDRESS));
            double rent    = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HOUSE_RENT));
            double area    = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HOUSE_AREA));
            int    floor   = cursor.getInt   (cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HOUSE_FLOOR));
            double lat     = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HOUSE_LAT));
            double lng     = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HOUSE_LNG));

            // reuse existing helper to get photos
            List<String> photoUrls = dbHelper.getHousePhotos(id);

            houses.add(new House(id, ownerId, addr, rent, area, floor, lat, lng, photoUrls));
        }
        cursor.close();

        // 5) Assertions
        assertFalse(houses.isEmpty());
        House fetched = null;
        for (House x : houses) {
            if (x.id == hid) {
                fetched = x;
                break;
            }
        }
        assertNotNull(fetched);
        assertEquals("Addr", fetched.address);
        assertEquals(owner, fetched.ownerId);
        assertEquals(2, fetched.photoUrls.size());
        assertTrue(fetched.photoUrls.contains("p1"));
        assertTrue(fetched.photoUrls.contains("p2"));
    }

}
