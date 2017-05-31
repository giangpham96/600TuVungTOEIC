package one.marshangeriksen.loloaldrin.myapplication.Dictionary;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import one.marshangeriksen.loloaldrin.myapplication.BaseController;
import one.marshangeriksen.loloaldrin.myapplication.Models.Word;

import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_EXAMPLE;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_EXAMPLE_TRANS;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_EXPLANATION;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_FAVORITE;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_ID;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_ID_TEMP;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_TOPIC;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_TRANSLATION;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_VOCAB;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_VOCAL;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.TABLE_WORD;

/**
 * Created by conme on 31-May-17.
 */

class DictionaryController extends BaseController {
    DictionaryController(Context context) {
        super(context);
    }
    List<Word> getAllWords() {
        List<Word> words = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_WORD + " ORDER BY " + KEY_VOCAB + " COLLATE NOCASE";
        Cursor c = db.rawQuery(query, null);
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
        }
        c.close();
        return words;
    }
}
