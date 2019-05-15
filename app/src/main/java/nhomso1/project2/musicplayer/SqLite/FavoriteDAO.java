package nhomso1.project2.musicplayer.SqLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import nhomso1.project2.musicplayer.Object.Audio;

public class FavoriteDAO extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Favorite_Manager";

    // Table name: Note.
    private static final String TABLE_FAVORITE = "Favorite";

    private static final String COLUMN_FAVORITE_ID ="Favorite_Id";
    private static final String COLUMN_DATA ="Data";
    private static final String COLUMN_TITLE ="Title";
    private static final String COLUMN_ALBUM ="Album";
    private static final String COLUMN_ARTIRT ="Artist";

    public FavoriteDAO(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script.
        String script = "CREATE TABLE " + TABLE_FAVORITE + "("
                + COLUMN_FAVORITE_ID + " INTEGER PRIMARY KEY," + COLUMN_DATA + " TEXT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_ALBUM + " TEXT,"
                + COLUMN_ARTIRT + " TEXT" + ")";
        // Execute Script.
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);

        // Create tables again
        onCreate(db);
    }

    public void addAudio(Audio au) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATA, au.getData());
        values.put(COLUMN_TITLE, au.getTitle());
        values.put(COLUMN_ALBUM, au.getAlbum());
        values.put(COLUMN_ARTIRT, au.getArtist());


        // Inserting Row
        db.insert(TABLE_FAVORITE, null, values);

        // Closing database connection
        db.close();
    }
    public Audio getFavorite(int id) {
        Log.i(TAG, "MyDatabaseHelper.getNote ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVORITE, new String[] { COLUMN_FAVORITE_ID,
                        COLUMN_DATA, COLUMN_TITLE,COLUMN_ALBUM, COLUMN_ARTIRT }, COLUMN_FAVORITE_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Audio fa = new Audio(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
        // return note
        return fa;
    }

    public List<Audio> getAllFavorite() {
        Log.i(TAG, "MyDatabaseHelper.getAllNotes ... " );

        List<Audio> auList = new ArrayList<Audio>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FAVORITE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Audio au = new Audio();
                au.setId(Integer.parseInt(cursor.getString(0)));
                au.setData(cursor.getString(1));
                au.setTitle(cursor.getString(2));
                au.setAlbum(cursor.getString(3));
                au.setArtist(cursor.getString(4));
                // Adding note to list
                auList.add(au);
            } while (cursor.moveToNext());
        }

        // return note list
        return auList;
    }
    public int getFavoriteCount() {
        Log.i(TAG, "MyDatabaseHelper.getNotesCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_FAVORITE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }
    public void deleteFavorite(Audio au) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITE, COLUMN_DATA + " = ?",
                new String[] { au.getData() });
        db.close();
    }
}
