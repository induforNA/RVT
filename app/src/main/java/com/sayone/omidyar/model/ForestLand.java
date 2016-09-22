package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 15/9/16.
 */
public class ForestLand extends RealmObject {

    @PrimaryKey
    private long id;

    private String surveyId;

    private RevenueProduct revenueProducts;

    private CostElement costElements;

    private Outlay outlay;

    private CashFlow cashFlow;

    private int discountPercentage;

    private DiscountingFactor discountingFactor;

    private DiscountedCashFlow discountedCashFlow;

    private double netPresentValue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RevenueProduct getRevenueProducts() {
        return revenueProducts;
    }

    public void setRevenueProducts(RevenueProduct revenueProducts) {
        this.revenueProducts = revenueProducts;
    }

    public CostElement getCostElements() {
        return costElements;
    }

    public void setCostElements(CostElement costElements) {
        this.costElements = costElements;
    }

    public Outlay getOutlay() {
        return outlay;
    }

    public void setOutlay(Outlay outlay) {
        this.outlay = outlay;
    }

    public CashFlow getCashFlow() {
        return cashFlow;
    }

    public void setCashFlow(CashFlow cashFlow) {
        this.cashFlow = cashFlow;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public DiscountingFactor getDiscountingFactor() {
        return discountingFactor;
    }

    public void setDiscountingFactor(DiscountingFactor discountingFactor) {
        this.discountingFactor = discountingFactor;
    }

    public DiscountedCashFlow getDiscountedCashFlow() {
        return discountedCashFlow;
    }

    public void setDiscountedCashFlow(DiscountedCashFlow discountedCashFlow) {
        this.discountedCashFlow = discountedCashFlow;
    }

    public double getNetPresentValue() {
        return netPresentValue;
    }

    public void setNetPresentValue(double netPresentValue) {
        this.netPresentValue = netPresentValue;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }
}
