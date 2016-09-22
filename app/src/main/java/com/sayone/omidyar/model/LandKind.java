package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by sayone on 15/9/16.
 */
public class LandKind extends RealmObject {

    @PrimaryKey
    private long id;

    private String surveyId;

    @Required
    private String name;

    private SocialCapital socialCapitals;

    private ForestLand forestLand;

    private CropLand cropLand;

    private PastureLand pastureLand;

    private MiningLand miningLand;

    private String status;

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

    public SocialCapital getSocialCapitals() {
        return socialCapitals;
    }

    public void setSocialCapitals(SocialCapital socialCapitals) {
        this.socialCapitals = socialCapitals;
    }

    public ForestLand getForestLand() {
        return forestLand;
    }

    public void setForestLand(ForestLand forestLand) {
        this.forestLand = forestLand;
    }

    public CropLand getCropLand() {
        return cropLand;
    }

    public void setCropLand(CropLand cropLand) {
        this.cropLand = cropLand;
    }

    public PastureLand getPastureLand() {
        return pastureLand;
    }

    public void setPastureLand(PastureLand pastureLand) {
        this.pastureLand = pastureLand;
    }

    public MiningLand getMiningLand() {
        return miningLand;
    }

    public void setMiningLand(MiningLand miningLand) {
        this.miningLand = miningLand;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }
}
