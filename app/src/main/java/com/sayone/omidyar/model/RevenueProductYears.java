package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 3/10/16.
 */

public class RevenueProductYears extends RealmObject {

    @PrimaryKey
    private long id;

    private String surveyId;

    private String landKind;

    private long revenueProductId;

    private double harvestFrequencyValue;

    private double harvestFrequencyUnit;

    private double quantityValue;

    private double quantityUnit;

    private double marketPriceValue;

    private double marketPriceCurrency;

    private int year;

    private int projectedIndex;

    private double subtotal;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getHarvestFrequencyValue() {
        return harvestFrequencyValue;
    }

    public void setHarvestFrequencyValue(double harvestFrequencyValue) {
        this.harvestFrequencyValue = harvestFrequencyValue;
    }

    public double getHarvestFrequencyUnit() {
        return harvestFrequencyUnit;
    }

    public void setHarvestFrequencyUnit(double harvestFrequencyUnit) {
        this.harvestFrequencyUnit = harvestFrequencyUnit;
    }

    public double getQuantityValue() {
        return quantityValue;
    }

    public void setQuantityValue(double quantityValue) {
        this.quantityValue = quantityValue;
    }

    public double getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(double quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public double getMarketPriceValue() {
        return marketPriceValue;
    }

    public void setMarketPriceValue(double marketPriceValue) {
        this.marketPriceValue = marketPriceValue;
    }

    public double getMarketPriceCurrency() {
        return marketPriceCurrency;
    }

    public void setMarketPriceCurrency(double marketPriceCurrency) {
        this.marketPriceCurrency = marketPriceCurrency;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getProjectedIndex() {
        return projectedIndex;
    }

    public void setProjectedIndex(int projectedIndex) {
        this.projectedIndex = projectedIndex;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public long getRevenueProductId() {
        return revenueProductId;
    }

    public void setRevenueProductId(long revenueProductId) {
        this.revenueProductId = revenueProductId;
    }

    public String getLandKind() {
        return landKind;
    }

    public void setLandKind(String landKind) {
        this.landKind = landKind;
    }

    @Override
    public String toString() {
        return "RevenueProductYears{" +
                "id=" + id +
                ", surveyId='" + surveyId + '\'' +
                ", landKind='" + landKind + '\'' +
                ", revenueProductId=" + revenueProductId +
                ", harvestFrequencyValue=" + harvestFrequencyValue +
                ", harvestFrequencyUnit=" + harvestFrequencyUnit +
                ", quantityValue=" + quantityValue +
                ", quantityUnit=" + quantityUnit +
                ", marketPriceValue=" + marketPriceValue +
                ", marketPriceCurrency=" + marketPriceCurrency +
                ", year=" + year +
                ", projectedIndex=" + projectedIndex +
                ", subtotal=" + subtotal +
                '}';
    }
}
