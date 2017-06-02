package one.marshangeriksen.loloaldrin.myapplication.objectModels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Question implements Serializable {
    public static final int VOCAB_QUESTION = 0;
    public static final int MEANING_QUESTION = 1;
    public static final int EXPLANATION_QUESTION = 2;
    private Word word;
    private List<String> answer;
    private int type;

    public Question(Word word, List<String> answer, int type) {
        this.word = word;
        this.answer = answer;
        this.type = type;
        Collections.shuffle(this.answer);
    }

    public Question() {
        answer = new ArrayList<>();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }
}
