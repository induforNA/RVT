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

    public RealmList<SocialCapitalAnswer> getSocialCapitalAnswers() {
        return socialCapitalAnswers;
    }

    public void setSocialCapitalAnswers(RealmList<SocialCapitalAnswer> socialCapitalAnswers) {
        this.socialCapitalAnswers = socialCapitalAnswers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSovereign() {
        return sovereign;
    }

    public void setSovereign(String sovereign) {
        this.sovereign = sovereign;
    }

    public String getSpread() {
        return spread;
    }

    public void setSpread(String spread) {
        this.spread = spread;
    }

    public String getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(String discountRate) {
        this.discountRate = discountRate;
    }
}
