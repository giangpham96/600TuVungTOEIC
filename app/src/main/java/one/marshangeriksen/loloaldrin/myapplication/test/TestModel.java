package one.marshangeriksen.loloaldrin.myapplication.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import one.marshangeriksen.loloaldrin.myapplication.BaseModel;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.HighScore;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.Question;
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

/**
 * Created by conme on 01-Jun-17.
 */

class TestModel extends BaseModel {
    private ArrayList<Word> words;
    private Random random;

    TestModel(Context context) {
        super(context);
        words = new ArrayList<>();
        random = new Random();
    }

    List<Question> getQuestionList(List<Integer> topicIds) {
        List<Question> questions = new ArrayList<>();
        String topicIdsString = "";
        for (int i : topicIds) {
            topicIdsString += i + ",";
        }
        if (topicIdsString.endsWith(",")) {
            topicIdsString = topicIdsString.substring(0, topicIdsString.length() - 1);
        }
        String query = "SELECT * FROM " + TABLE_WORD + " WHERE " + KEY_TOPIC + " IN (" + topicIdsString + ")";
        words.clear();
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
                if (c.getInt(c.getColumnIndex(KEY_FAVORITE)) == 0) {
                    word.setFavorite(false);
                } else {
                    word.setFavorite(true);
                }
                words.add(word);
            } while (c.moveToNext());
        }
        c.close();
        Collections.shuffle(words);
        for (int i = 0; i < words.size(); i++) {
            Question question;
            if (i > 0 && i <= words.size() / 3)
                question = getVocabQuestion(i);
            else if (i > words.size() / 3 && i <= words.size() * 2 / 3)
                question = getMeaningQuestion(i);
            else
                question = getExplanationQuestion(i);
            questions.add(question);
            Log.e("question " + i, question.toString());
        }
        return questions;
    }


    private Question getMeaningQuestion(int position) {
        Question question = new Question();
        question.setType(Question.MEANING_QUESTION);
        question.setWord(words.get(position));
        question.getAnswer().add(question.getWord().getTranslation());
        List<Integer> fakeAnswerId = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int fakeAnswer;
            do {
                fakeAnswer = random.nextInt(words.size());
            } while (fakeAnswer == position || fakeAnswerId.contains(fakeAnswer));
            fakeAnswerId.add(fakeAnswer);
            question.getAnswer().add(words.get(fakeAnswer).getTranslation());
        }
        Collections.shuffle(question.getAnswer());
        return question;
    }

    private Question getExplanationQuestion(int position) {
        Question question = new Question();
        question.setType(Question.EXPLANATION_QUESTION);
        question.setWord(words.get(position));
        question.getAnswer().add(question.getWord().getExplanation());
        List<Integer> fakeAnswerId = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int fakeAnswer;
            do {
                fakeAnswer = random.nextInt(words.size());
            } while (fakeAnswer == position || fakeAnswerId.contains(fakeAnswer));
            fakeAnswerId.add(fakeAnswer);
            question.getAnswer().add(words.get(fakeAnswer).getExplanation());
        }
        Collections.shuffle(question.getAnswer());
        return question;
    }

    private Question getVocabQuestion(int position) {
        Question question = new Question();
        question.setType(Question.VOCAB_QUESTION);
        question.setWord(words.get(position));
        question.getAnswer().add(question.getWord().getVocabulary());
        List<Integer> fakeAnswerId = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int fakeAnswer;
            do {
                fakeAnswer = random.nextInt(words.size());
            } while (fakeAnswer == position || fakeAnswerId.contains(fakeAnswer));
            fakeAnswerId.add(fakeAnswer);
            question.getAnswer().add(words.get(fakeAnswer).getVocabulary());
        }
        Collections.shuffle(question.getAnswer());
        return question;
    }

    boolean isHighScore(int score) {
        List<HighScore> highScores = getHighScoreList();
        for (HighScore highScore : highScores) {
            if (highScore.getPassnumber_highscore() < score)
                return true;
        }
        return false;
    }

    int updateHighScore(String name, int score) {
        HighScore lowestHighScore = getHighScoreList().get(getHighScoreList().size() - 1);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PASS, score);
        values.put(KEY_NAME, name);
        // updating row
        return db.update(TABLE_HIGH_SCORE, values, KEY_ID_HIGH_SCORE + " = ?",
                new String[]{String.valueOf(lowestHighScore.get_id_highscore())});
    }
}
