package com.sayone.omidyar.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 15/9/16.
 */
public class SharedCostElement extends RealmObject {

    @PrimaryKey
    private long id;

    private String landKind;

    private String surveyId;

    private String name;

    private String type;

    private RealmList<SharedCostElementYears> costElementYearses;

    public SharedCostElementYears getCostElementTrend() {
        return costElementTrend;
    }

    public void setCostElementTrend(SharedCostElementYears costElementTrend) {
        this.costElementTrend = costElementTrend;
    }

    private SharedCostElementYears costElementTrend;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLandKind() {
        return landKind;
    }

    public void setLandKind(String landKind) {
        this.landKind = landKind;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
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


    public RealmList<SharedCostElementYears> getCostElementYearses() {
        return costElementYearses;
    }

    public void setCostElementYearses(RealmList<SharedCostElementYears> costElementYearses) {
        this.costElementYearses = costElementYearses;
    }
}
