package one.marshangeriksen.loloaldrin.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import one.marshangeriksen.loloaldrin.myapplication.objectModels.HighScore;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.Word;

import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_EXAMPLE;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_EXAMPLE_TRANS;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_EXPLANATION;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_FAVORITE;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_ID;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_ID_HIGH_SCORE;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_ID_TEMP;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_NAME;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_PASS;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_TOPIC;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_TRANSLATION;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_VOCAB;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_VOCAL;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.TABLE_HIGH_SCORE;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.TABLE_WORD;


public class BaseDatabaseModel extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "toeic600.db";
    protected SQLiteDatabase db;

    public BaseDatabaseModel(Context context) {
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

    public List<HighScore> getHighScoreList() {
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

    public int resetHighScore() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PASS, 0);
        values.put(KEY_NAME, "No name");
        // updating rows
        return db.update(TABLE_HIGH_SCORE, values, null, null);
    }

    Word getRandomFavoriteWord() {
        String numberOfFavoriteWords = "SELECT " + KEY_ID + " FROM " + TABLE_WORD
                + " WHERE " + KEY_FAVORITE + " = " + " 1";
        String query = "SELECT * FROM " + TABLE_WORD
                + " WHERE " + KEY_ID + " IN (" + numberOfFavoriteWords + ")";
        Cursor c = db.rawQuery(query, null);
        List<Word> words = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Word word = new Word();
                word.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                word.setTopic(c.getInt(c.getColumnIndex(KEY_TOPIC)));
                word.setIdTemp(c.getInt(c.getColumnIndex(KEY_ID_TEMP)));
                word.setVocabulary(c.getString(c.getColumnIndex(KEY_VOCAB)));
                word.setVocalization(c.getString(c.getColumnIndex(KEY_VOCAL)));
                word.setExample(c.getString(c.getColumnIndex(KEY_EXAMPLE)));
                word.setExampleTranslation(c.getString(c.getColumnIndex(KEY_EXAMPLE_TRANS)));
                word.setExplanation(c.getString(c.getColumnIndex(KEY_EXPLANATION)));
                word.setTranslation(c.getString(c.getColumnIndex(KEY_TRANSLATION)));
                if (c.getInt(c.getColumnIndex(KEY_FAVORITE)) == 0)
                    word.setFavorite(false);
                else
                    word.setFavorite(true);
                // adding to deck icon_decks
                words.add(word);
            } while (c.moveToNext());
            c.close();
        } else {
            c.close();
            return null;
        }
        int position = new Random().nextInt(words.size());
        return words.get(position);
    }

    public void close() {
        if (db != null && db.isOpen())
            db.close();
    }
}
