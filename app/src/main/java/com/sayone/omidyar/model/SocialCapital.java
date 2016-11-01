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

    double score;

    String rating;

    double sovereign;

    double spread;

    double discountRate;

    double discountRateOverride;

    private boolean discountFlag;

    public boolean isDiscountFlag() {
        return discountFlag;
    }

    public void setDiscountFlag(boolean discountFlag) {
        this.discountFlag = discountFlag;
    }

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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public double getSovereign() {
        return sovereign;
    }

    public void setSovereign(double sovereign) {
        this.sovereign = sovereign;
    }

    public double getSpread() {
        return spread;
    }

    public void setSpread(double spread) {
        this.spread = spread;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getDiscountRateOverride() {
        return discountRateOverride;
    }

    public void setDiscountRateOverride(double discountRate) {
        this.discountRateOverride = discountRate;
    }
}
