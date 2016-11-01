package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 15/9/16.
 */
public class CashFlow extends RealmObject {

    @PrimaryKey
    private long id;

    private String surveyId;

    private int year;

    private double value;

    private double discountingFactor;

    private double discountedCashFlow;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public double getDiscountingFactor() {
        return discountingFactor;
    }

    public void setDiscountingFactor(double discountingFactor) {
        this.discountingFactor = discountingFactor;
    }

    public double getDiscountedCashFlow() {
        return discountedCashFlow;
    }

    public void setDiscountedCashFlow(double discountedCashFlow) {
        this.discountedCashFlow = discountedCashFlow;
    }

    @Override
    public String toString() {
        return "CashFlow{" +
                "id=" + id +
                ", surveyId='" + surveyId + '\'' +
                ", year=" + year +
                ", value=" + value +
                ", discountingFactor=" + discountingFactor +
                ", discountedCashFlow=" + discountedCashFlow +
                '}';
    }
}
