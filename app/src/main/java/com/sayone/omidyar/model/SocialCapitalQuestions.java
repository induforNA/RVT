package com.sayone.omidyar.model;

import io.realm.RealmList;
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

    private RealmList<SocialCapitalAnswerOptions> socialCapitalAnswerOptionses;

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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }


    public RealmList<SocialCapitalAnswerOptions> getSocialCapitalAnswerOptionses() {
        return socialCapitalAnswerOptionses;
    }

    public void setSocialCapitalAnswerOptionses(RealmList<SocialCapitalAnswerOptions> socialCapitalAnswerOptionses) {
        this.socialCapitalAnswerOptionses = socialCapitalAnswerOptionses;
    }
}
