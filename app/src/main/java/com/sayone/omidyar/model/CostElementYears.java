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

    private String landKind;

    private long costElementId;

    private int costFrequencyValue;

    private int costFrequencyUnit;

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

    public int getCostFrequencyValue() {
        return costFrequencyValue;
    }

    public void setCostFrequencyValue(int costFrequencyValue) {
        this.costFrequencyValue = costFrequencyValue;
    }

    public int getCostFrequencyUnit() {
        return costFrequencyUnit;
    }

    public void setCostFrequencyUnit(int costFrequencyUnit) {
        this.costFrequencyUnit = costFrequencyUnit;
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

    public long getCostElementId() {
        return costElementId;
    }

    public void setCostElementId(long costElementId) {
        this.costElementId = costElementId;
    }

    public String getLandKind() {
        return landKind;
    }

    public void setLandKind(String landKind) {
        this.landKind = landKind;
    }
}
