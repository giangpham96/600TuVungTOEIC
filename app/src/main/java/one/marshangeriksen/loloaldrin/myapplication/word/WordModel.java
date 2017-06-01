package one.marshangeriksen.loloaldrin.myapplication.word;

import android.content.Context;
import android.database.Cursor;

import one.marshangeriksen.loloaldrin.myapplication.BaseModel;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.Word;

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


class WordModel extends BaseModel {
    WordModel(Context context) {
        super(context);
    }

    Word getWordBaseOnId(int id) {
        Word word = new Word();
        String query = "SELECT * FROM " + TABLE_WORD + " WHERE " + KEY_ID + " = " + id;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
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
        }
        c.close();
        return word;
    }
}
