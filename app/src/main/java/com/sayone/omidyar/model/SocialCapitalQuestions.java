package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 15/9/16.
 */
public class SocialCapitalQuestions extends RealmObject {

    @PrimaryKey
    private long id;

    private String question;

    private String questionHindi;

    private String optionType;

//    private HashMap<String,String> options;
//
//    private HashMap<String,String> optionsHindi;

    private int weight;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionHindi() {
        return questionHindi;
    }

    public void setQuestionHindi(String questionHindi) {
        this.questionHindi = questionHindi;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

//    public HashMap<String, String> getOptions() {
//        return options;
//    }
//
//    public void setOptions(HashMap<String, String> options) {
//        this.options = options;
//    }
//
//    public HashMap<String, String> getOptionsHindi() {
//        return optionsHindi;
//    }
//
//    public void setOptionsHindi(HashMap<String, String> optionsHindi) {
//        this.optionsHindi = optionsHindi;
//    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
