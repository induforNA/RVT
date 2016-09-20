package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 15/9/16.
 */
public class SocialCapital extends RealmObject {

    @PrimaryKey
    private long id;

    private SocialCapitalQuestions socialCapitalQuestion;

    private int answer;

    private int factorScore;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SocialCapitalQuestions getSocialCapitalQuestion() {
        return socialCapitalQuestion;
    }

    public void setSocialCapitalQuestion(SocialCapitalQuestions socialCapitalQuestion) {
        this.socialCapitalQuestion = socialCapitalQuestion;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public int getFactorScore() {
        return factorScore;
    }

    public void setFactorScore(int factorScore) {
        this.factorScore = factorScore;
    }
}
