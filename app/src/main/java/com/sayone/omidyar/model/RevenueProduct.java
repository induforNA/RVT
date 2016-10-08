package com.sayone.omidyar.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 15/9/16.
 */
public class RevenueProduct extends RealmObject {

    @PrimaryKey
    private long id;

    private String landKind;

    private String surveyId;

    private String name;

    private String type;

    private RealmList<RevenueProductYears> revenueProductYearses;

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

    public RealmList<RevenueProductYears> getRevenueProductYearses() {
        return revenueProductYearses;
    }

    public void setRevenueProductYearses(RealmList<RevenueProductYears> revenueProductYearses) {
        this.revenueProductYearses = revenueProductYearses;
    }

    public String getLandKind() {
        return landKind;
    }

    public void setLandKind(String landKind) {
        this.landKind = landKind;
    }
}
