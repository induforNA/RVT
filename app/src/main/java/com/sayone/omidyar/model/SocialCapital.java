package com.sayone.omidyar.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 15/9/16.
 */
public class SocialCapital extends RealmObject {

    @PrimaryKey
    private long id;

    private String surveyId;

    RealmList<SocialCapitalAnswer> socialCapitalAnswers;

    int score;

    String rating;

    String sovereign;

    String spread;

    String discountRate;

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
}
