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

    private double totalRevenue;

    private double totalCost;

    private double totalOutlay;

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

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getTotalOutlay() {
        return totalOutlay;
    }

    public void setTotalOutlay(double totalOutlay) {
        this.totalOutlay = totalOutlay;
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
