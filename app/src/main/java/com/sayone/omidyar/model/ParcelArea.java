package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 16/2/17.
 */

public class ParcelArea extends RealmObject {

    @PrimaryKey
    private long id;

    private String surveyId;

    private double area;

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

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

    @Override
    public String toString() {
        return "ParcelArea{" +
                "id=" + id +
                ", surveyId='" + surveyId + '\'' +
                ", area=" + area +
                '}';
    }
}
