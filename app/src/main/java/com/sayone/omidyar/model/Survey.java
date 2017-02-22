package com.sayone.omidyar.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 15/9/16.
 */
public class Survey extends RealmObject {

    @PrimaryKey
    private long id;

    private String surveyor;

    private String surveyId;

    private String community;

    private String district;

    private String state;

    private String country;

    private String language;

    private String currency;

    private String respondentGroup;

    private Boolean sendStatus;

    private Date date;

    private RealmList<Participant> participants;

    private RealmList<LandKind> landKinds;

    private String inflationRate;

    private String riskRate;

    private String overRideInflationRate;

    private Component components;

    private RealmList<ParcelLocation> parcelLocations;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSurveyor() {
        return surveyor;
    }

    public void setSurveyor(String surveyor) {
        this.surveyor = surveyor;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public boolean isSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(boolean sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getOverRideInflationRate() {
        return overRideInflationRate;
    }

    public void setOverRideInflationRate(String overRideInflationRate) {
        this.overRideInflationRate = overRideInflationRate;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRespondentGroup() {
        return respondentGroup;
    }

    public void setRespondentGroup(String respondentGroup) {
        this.respondentGroup = respondentGroup;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RealmList<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(RealmList<Participant> participants) {
        this.participants = participants;
    }

    public RealmList<LandKind> getLandKinds() {
        return landKinds;
    }

    public void setLandKinds(RealmList<LandKind> landKinds) {
        this.landKinds = landKinds;
    }

    public String getInflationRate() {
        return inflationRate;
    }

    public void setInflationRate(String inflationRate) {
        this.inflationRate = inflationRate;
    }

    public Component getComponents() {
        return components;
    }

    public void setComponents(Component components) {
        this.components = components;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public RealmList<ParcelLocation> getParcelLocations() {
        return parcelLocations;
    }

    public void setParcelLocations(RealmList<ParcelLocation> parcelLocations) {
        this.parcelLocations = parcelLocations;
    }

    public String getRiskRate() {
        return riskRate;
    }

    public void setRiskRate(String riskRate) {
        this.riskRate = riskRate;
    }

    @Override
    public String toString() {
        return "Survey{" +
                "id=" + id +
                ", surveyor='" + surveyor + '\'' +
                ", surveyId='" + surveyId + '\'' +
                ", community='" + community + '\'' +
                ", district='" + district + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", language='" + language + '\'' +
                ", currency='" + currency + '\'' +
                ", respondentGroup='" + respondentGroup + '\'' +
                ", sendStatus=" + sendStatus +
                ", date=" + date +
                ", participants=" + participants +
                ", landKinds=" + landKinds +
                ", inflationRate='" + inflationRate + '\'' +
                ", riskRate='" + riskRate + '\'' +
                ", overRideInflationRate='" + overRideInflationRate + '\'' +
                ", components=" + components +
                ", parcelLocations=" + parcelLocations +
                '}';
    }
}
