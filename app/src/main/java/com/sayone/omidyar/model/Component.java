package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 15/9/16.
 */
public class Component extends RealmObject {

    @PrimaryKey
    private long id;

    private String surveyId;

    private double croplandValue;

    private double croplandCompleteness;

    private double croplandSocialCapitalScore;

    private double forestValue;

    private double forestCompleteness;

    private double forestSocialCapitalScore;

    private double pastureValue;

    private double pastureCompleteness;

    private double pastureSocialCapitalScore;

    private double miningLandValue;

    private double miningLandCompleteness;

    private double miningSocialCapitalScore;

    private double totalValue;

    private String totalValuePerHaStr;

    private String totalValueStr;

    private double totalSocialCapitalScore;

    private double sharedCostValue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getCroplandValue() {
        return croplandValue;
    }

    public void setCroplandValue(double croplandValue) {
        this.croplandValue = croplandValue;
    }

    public double getCroplandCompleteness() {
        return croplandCompleteness;
    }

    public void setCroplandCompleteness(double croplandCompleteness) {
        this.croplandCompleteness = croplandCompleteness;
    }

    public double getCroplandSocialCapitalScore() {
        return croplandSocialCapitalScore;
    }

    public void setCroplandSocialCapitalScore(double croplandSocialCapitalScore) {
        this.croplandSocialCapitalScore = croplandSocialCapitalScore;
    }

    public double getForestValue() {
        return forestValue;
    }

    public void setForestValue(double forestValue) {
        this.forestValue = forestValue;
    }

    public double getForestCompleteness() {
        return forestCompleteness;
    }

    public void setForestCompleteness(double forestCompleteness) {
        this.forestCompleteness = forestCompleteness;
    }

    public double getForestSocialCapitalScore() {
        return forestSocialCapitalScore;
    }

    public void setForestSocialCapitalScore(double forestSocialCapitalScore) {
        this.forestSocialCapitalScore = forestSocialCapitalScore;
    }

    public double getPastureValue() {
        return pastureValue;
    }

    public void setPastureValue(double pastureValue) {
        this.pastureValue = pastureValue;
    }

    public double getPastureCompleteness() {
        return pastureCompleteness;
    }

    public void setPastureCompleteness(double pastureCompleteness) {
        this.pastureCompleteness = pastureCompleteness;
    }

    public double getPastureSocialCapitalScore() {
        return pastureSocialCapitalScore;
    }

    public void setPastureSocialCapitalScore(double pastureSocialCapitalScore) {
        this.pastureSocialCapitalScore = pastureSocialCapitalScore;
    }

    public double getMiningLandValue() {
        return miningLandValue;
    }

    public void setMiningLandValue(double miningLandValue) {
        this.miningLandValue = miningLandValue;
    }

    public double getMiningLandCompleteness() {
        return miningLandCompleteness;
    }

    public void setMiningLandCompleteness(double miningLandCompleteness) {
        this.miningLandCompleteness = miningLandCompleteness;
    }

    public double getMiningSocialCapitalScore() {
        return miningSocialCapitalScore;
    }

    public void setMiningSocialCapitalScore(double miningSocialCapitalScore) {
        this.miningSocialCapitalScore = miningSocialCapitalScore;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public double getTotalSocialCapitalScore() {
        return totalSocialCapitalScore;
    }

    public void setTotalSocialCapitalScore(double totalSocialCapitalScore) {
        this.totalSocialCapitalScore = totalSocialCapitalScore;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getTotalValueStr() {
        return totalValueStr;
    }

    public void setTotalValueStr(String totalValueStr) {
        this.totalValueStr = totalValueStr;
    }

    public double getSharedCostValue() {
        return sharedCostValue;
    }

    public void setSharedCostValue(double sharedCostValue) {
        this.sharedCostValue = sharedCostValue;
    }

    public String getTotalValuePerHa() {
        return totalValuePerHaStr;
    }

    public void setTotalValuePerHa(String totalValuePerHaStr) {
        this.totalValuePerHaStr = totalValuePerHaStr;
    }
}
