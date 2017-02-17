package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 16/2/17.
 */

public class ParcelLocation extends RealmObject{
    @PrimaryKey
    private long id;

    private String surveyId;

    private int corner;

    private double latitude;

    private double longitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public int getCorner() {
        return corner;
    }

    public void setCorner(int corner) {
        this.corner = corner;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "ParcelLocation{" +
                "id=" + id +
                ", surveyId='" + surveyId + '\'' +
                ", corner=" + corner +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
