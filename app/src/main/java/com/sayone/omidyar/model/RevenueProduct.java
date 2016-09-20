package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 15/9/16.
 */
public class RevenueProduct extends RealmObject {

    @PrimaryKey
    private long id;

    private String name;

    private String type;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
