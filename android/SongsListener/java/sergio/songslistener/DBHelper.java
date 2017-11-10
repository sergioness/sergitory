package sergio.lab7thirdtry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
    private static final String TAG = "DBHelper";
    private static final String DATABASE_NAME = "Lab7_3.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "SONGS";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ARTIST = "ARTIST";
    private static final String COLUMN_SONG = "SONG";
    private static final String COLUMN_TIME = "TIME";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ARTIST + " TEXT, " +
                COLUMN_SONG + " TEXT, " +
                COLUMN_TIME + " LONG)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
    public boolean addData(String artist, String song) {
        if (!verify(artist, song)) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ARTIST, artist);
        contentValues.put( COLUMN_SONG, song);
        contentValues.put(COLUMN_TIME, System.currentTimeMillis());
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    private boolean verify (String artist, String song)
    {
        Cursor data = getData();
        while(data.moveToNext()){
            if (data.getString(1).equals(artist) && data.getString(2).equals(song)) {
                return false;
            }
        }
        return true;
    }
    public Cursor getData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;
        db.execSQL(query);
    }
}
