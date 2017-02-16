package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 4/10/16.
 */

public class SharedCostElementYears extends RealmObject {

    @PrimaryKey
    private long id;

    private String surveyId;

    private String landKind;

    private long costElementId;

    private double costFrequencyValue;

    private double costFrequencyUnit;

    private double costPerPeriodValue;

    private String costPerPeriodUnit;

    private double costPerUnitValue;

    private String costPerUnitUnit;

    private int year;

    private double projectedIndex;

    private double subtotal;

    private double households;

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

    public double getCostFrequencyValue() {
        return costFrequencyValue;
    }

    public void setCostFrequencyValue(double costFrequencyValue) {
        this.costFrequencyValue = costFrequencyValue;
    }

    public double getCostFrequencyUnit() {
        return costFrequencyUnit;
    }

    public void setCostFrequencyUnit(double costFrequencyUnit) {
        this.costFrequencyUnit = costFrequencyUnit;
    }

    public double getCostPerPeriodValue() {
        return costPerPeriodValue;
    }

    public void setCostPerPeriodValue(double costPerPeriodValue) {
        this.costPerPeriodValue = costPerPeriodValue;
    }

    public String getCostPerPeriodUnit() {
        return costPerPeriodUnit;
    }

    public void setCostPerPeriodUnit(String costPerPeriodUnit) {
        this.costPerPeriodUnit = costPerPeriodUnit;
    }

    public double getCostPerUnitValue() {
        return costPerUnitValue;
    }

    public void setCostPerUnitValue(double costPerUnitValue) {
        this.costPerUnitValue = costPerUnitValue;
    }

    public String getCostPerUnitUnit() {
        return costPerUnitUnit;
    }

    public void setCostPerUnitUnit(String costPerUnitUnit) {
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

    public double getProjectedIndex() {
        return projectedIndex;
    }

    public void setProjectedIndex(double projectedIndex) {
        this.projectedIndex = projectedIndex;
    }

    public double getHouseholds() {
        return households;
    }

    public void setHouseholds(double households) {
        this.households = households;
    }

    @Override
    public String toString() {
        return "CostElementYears{" +
                "id=" + id +
                ", surveyId='" + surveyId + '\'' +
                ", landKind='" + landKind + '\'' +
                ", costElementId=" + costElementId +
                ", costFrequencyValue=" + costFrequencyValue +
                ", costFrequencyUnit=" + costFrequencyUnit +
                ", costPerPeriodValue=" + costPerPeriodValue +
                ", costPerPeriodUnit='" + costPerPeriodUnit + '\'' +
                ", costPerUnitValue=" + costPerUnitValue +
                ", costPerUnitUnit='" + costPerUnitUnit + '\'' +
                ", year=" + year +
                ", projectedIndex=" + projectedIndex +
                ", subtotal=" + subtotal +
                ", households=" + households +
                '}';
    }
}
