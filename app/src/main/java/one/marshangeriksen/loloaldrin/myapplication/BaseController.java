package one.marshangeriksen.loloaldrin.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import one.marshangeriksen.loloaldrin.myapplication.Models.Word;

import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_FAVORITE;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_ID;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.TABLE_WORD;

/**
 * Created by conme on 31-May-17.
 */

public class BaseController extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "toeic600.db";
    protected SQLiteDatabase db;

    public BaseController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public int updateFavorite(Word word) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        byte favorite = 0;
        if (word.isFavorite())
            favorite = 1;
        values.put(KEY_FAVORITE, favorite);
        // updating row
        return db.update(TABLE_WORD, values, KEY_ID + " = ?",
                new String[]{String.valueOf(word.getId())});
    }

    public void close() {
        if (db != null && db.isOpen())
            db.close();
    }
}
