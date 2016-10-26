package com.sayone.omidyar.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 22/9/16.
 */

public class SocialCapitalAnswer extends RealmObject {

    @PrimaryKey
    private long id;

    private String surveyId;

    private SocialCapitalQuestions socialCapitalQuestion;

    private RealmList<MultipleAnswer> multipleAnswers;

    private double factorScore;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public SocialCapitalQuestions getSocialCapitalQuestion() {
        return socialCapitalQuestion;
    }

    public void setSocialCapitalQuestion(SocialCapitalQuestions socialCapitalQuestion) {
        this.socialCapitalQuestion = socialCapitalQuestion;
    }

    public double getFactorScore() {
        return factorScore;
    }

    public void setFactorScore(double factorScore) {
        this.factorScore = factorScore;
    }

    public RealmList<MultipleAnswer> getMultipleAnswers() {
        return multipleAnswers;
    }

    public void setMultipleAnswers(RealmList<MultipleAnswer> multipleAnswers) {
        this.multipleAnswers = multipleAnswers;
    }
}
