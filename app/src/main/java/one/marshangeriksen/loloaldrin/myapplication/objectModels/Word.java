package one.marshangeriksen.loloaldrin.myapplication.objectModels;

import java.io.Serializable;


public class Word implements Serializable {
    private int id;
    private int topic;
    private int idTemp;
    private String vocabulary;
    private String vocalization;
    private String explanation;
    private String translation;
    private String example;
    private String exampleTranslation;
    private boolean favorite;

    public Word(int id, int topic, int id_temp, String vocabulary, String vocalization,
                String explanation, String translation, String example,
                String exampleTranslation, boolean favorite) {
        this.id = id;
        this.topic = topic;
        this.idTemp = id_temp;
        this.vocabulary = vocabulary;
        this.vocalization = vocalization;
        this.explanation = explanation;
        this.translation = translation;
        this.example = example;
        this.exampleTranslation = exampleTranslation;
        this.favorite = favorite;
    }

    public Word() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public int getIdTemp() {
        return idTemp;
    }

    public void setIdTemp(int idTemp) {
        this.idTemp = idTemp;
    }

    public String getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(String vocabulary) {
        this.vocabulary = vocabulary;
    }

    public String getVocalization() {
        return vocalization;
    }

    public void setVocalization(String vocalization) {
        this.vocalization = vocalization;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getExampleTranslation() {
        return exampleTranslation;
    }

    public void setExampleTranslation(String exampleTranslation) {
        this.exampleTranslation = exampleTranslation;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
