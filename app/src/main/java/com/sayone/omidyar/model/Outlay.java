package com.sayone.omidyar.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 15/9/16.
 */
public class Outlay extends RealmObject {

    @PrimaryKey
    private long id;

    private String surveyId;

    private String landKind;

    private String type;

    private String itemName;

    private RealmList<OutlayYears> outlayYearses;

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

    public String getLandKind() {
        return landKind;
    }

    public void setLandKind(String landKind) {
        this.landKind = landKind;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public RealmList<OutlayYears> getOutlayYearses() {
        return outlayYearses;
    }

    public void setOutlayYearses(RealmList<OutlayYears> outlayYearses) {
        this.outlayYearses = outlayYearses;
    }
}
