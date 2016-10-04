package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 4/10/16.
 */

public class CostElementYears extends RealmObject {

    @PrimaryKey
    private long id;

    private String surveyId;

    private int expenseFrequencyValue;

    private int expenseFrequencyUnit;

    private int costPerPeriodValue;

    private int costPerPeriodUnit;

    private int costPerUnitValue;

    private int costPerUnitUnit;

    private int year;

    private double subtotal;

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

    public int getExpenseFrequencyValue() {
        return expenseFrequencyValue;
    }

    public void setExpenseFrequencyValue(int expenseFrequencyValue) {
        this.expenseFrequencyValue = expenseFrequencyValue;
    }

    public int getExpenseFrequencyUnit() {
        return expenseFrequencyUnit;
    }

    public void setExpenseFrequencyUnit(int expenseFrequencyUnit) {
        this.expenseFrequencyUnit = expenseFrequencyUnit;
    }

    public int getCostPerPeriodValue() {
        return costPerPeriodValue;
    }

    public void setCostPerPeriodValue(int costPerPeriodValue) {
        this.costPerPeriodValue = costPerPeriodValue;
    }

    public int getCostPerPeriodUnit() {
        return costPerPeriodUnit;
    }

    public void setCostPerPeriodUnit(int costPerPeriodUnit) {
        this.costPerPeriodUnit = costPerPeriodUnit;
    }

    public int getCostPerUnitValue() {
        return costPerUnitValue;
    }

    public void setCostPerUnitValue(int costPerUnitValue) {
        this.costPerUnitValue = costPerUnitValue;
    }

    public int getCostPerUnitUnit() {
        return costPerUnitUnit;
    }

    public void setCostPerUnitUnit(int costPerUnitUnit) {
        this.costPerUnitUnit = costPerUnitUnit;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
