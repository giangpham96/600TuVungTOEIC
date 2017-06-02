package one.marshangeriksen.loloaldrin.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import one.marshangeriksen.loloaldrin.myapplication.objectModels.HighScore;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.Word;

import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_FAVORITE;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_ID;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_ID_HIGH_SCORE;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_NAME;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_PASS;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.TABLE_HIGH_SCORE;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.TABLE_WORD;


public class BaseModel extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "toeic600.db";
    protected SQLiteDatabase db;

    public BaseModel(Context context) {
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

    protected List<HighScore> getHighScoreList() {
        List<HighScore> highScores = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_HIGH_SCORE +
                " ORDER BY " + KEY_PASS + " COLLATE NOCASE DESC";
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                HighScore highScore = new HighScore();
                highScore.set_id_highscore(c.getInt(c.getColumnIndex(KEY_ID_HIGH_SCORE)));
                highScore.setPassnumber_highscore(c.getInt(c.getColumnIndex(KEY_PASS)));
                highScore.setName_highscore(c.getString(c.getColumnIndex(KEY_NAME)));
                highScores.add(highScore);
            } while (c.moveToNext());
        }
        for (HighScore highScore : highScores) {
            Log.e("highscore", highScore.getPassnumber_highscore() + "");
        }
        c.close();
        return highScores;
    }
    public void close() {
        if (db != null && db.isOpen())
            db.close();
    }

    int resetHighScore() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PASS, 0);
        values.put(KEY_NAME, "No name");
        // updating rows
        return db.update(TABLE_HIGH_SCORE, values, null, null);
    }
}
