package com.sayone.omidyar.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 15/9/16.
 */
public class MiningLand extends RealmObject {

    @PrimaryKey
    private long id;

    private String surveyId;

    private RealmList<RevenueProduct> revenueProducts;

    private RealmList<CostElement> costElements;

    private RealmList<Outlay> outlays;

    private RealmList<CashFlow> cashFlows;

    private int discountPercentage;

    private RealmList<DiscountingFactor> discountingFactors;

    private RealmList<DiscountedCashFlow> discountedCashFlows;

    private double netPresentValue;

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

    public RealmList<RevenueProduct> getRevenueProducts() {
        return revenueProducts;
    }

    public void setRevenueProducts(RealmList<RevenueProduct> revenueProducts) {
        this.revenueProducts = revenueProducts;
    }

    public RealmList<CostElement> getCostElements() {
        return costElements;
    }

    public void setCostElements(RealmList<CostElement> costElements) {
        this.costElements = costElements;
    }

    public RealmList<Outlay> getOutlays() {
        return outlays;
    }

    public void setOutlays(RealmList<Outlay> outlays) {
        this.outlays = outlays;
    }

    public RealmList<CashFlow> getCashFlows() {
        return cashFlows;
    }

    public void setCashFlows(RealmList<CashFlow> cashFlows) {
        this.cashFlows = cashFlows;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public RealmList<DiscountingFactor> getDiscountingFactors() {
        return discountingFactors;
    }

    public void setDiscountingFactors(RealmList<DiscountingFactor> discountingFactors) {
        this.discountingFactors = discountingFactors;
    }

    public RealmList<DiscountedCashFlow> getDiscountedCashFlows() {
        return discountedCashFlows;
    }

    public void setDiscountedCashFlows(RealmList<DiscountedCashFlow> discountedCashFlows) {
        this.discountedCashFlows = discountedCashFlows;
    }

    public double getNetPresentValue() {
        return netPresentValue;
    }

    public void setNetPresentValue(double netPresentValue) {
        this.netPresentValue = netPresentValue;
    }
}
