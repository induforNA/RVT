package com.sayone.omidyar.model;

import java.text.SimpleDateFormat;
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

    private double inflationRate;

    private double riskRate;

    private double overRideInflationRate;

    private double overRideRiskRate;

    private Component components;

    private RealmList<SharedCostElement> sharedCostElements;

    private RealmList<CashFlow> sharedCashFlows;

    private RealmList<Outlay> sharedOutlays;

    private ParcelLocation parcelLocations;

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

    public double getOverRideInflationRate() {
        return overRideInflationRate;
    }

    public void setOverRideInflationRate(double overRideInflationRate) {
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

    public String getDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
        return dateFormat.format(date);
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

    public double getInflationRate() {
        return inflationRate;
    }

    public void setInflationRate(double inflationRate) {
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

    public ParcelLocation getParcelLocations() {
        return parcelLocations;
    }

    public void setParcelLocations(ParcelLocation parcelLocations) {
        this.parcelLocations = parcelLocations;
    }

    public double getRiskRate() {
        return riskRate;
    }

    public void setRiskRate(double riskRate) {
        this.riskRate = riskRate;
    }

    public double getOverRideRiskRate() {
        return overRideRiskRate;
    }

    public void setOverRideRiskRate(double overRideRiskRate) {
        this.overRideRiskRate = overRideRiskRate;
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
                ", overRideRiskRate='" + overRideRiskRate + '\'' +
                ", components=" + components +
                ", parcelLocations=" + parcelLocations +
                '}';
    }

    public RealmList<SharedCostElement> getSharedCostElements() {
        return sharedCostElements;
    }

    public void setSharedCostElements(RealmList<SharedCostElement> sharedCostElements) {
        this.sharedCostElements = sharedCostElements;
    }

    public RealmList<CashFlow> getSharedCashFlows() {
        return sharedCashFlows;
    }

    public void setSharedCashFlows(RealmList<CashFlow> sharedCashFlows) {
        this.sharedCashFlows = sharedCashFlows;
    }

    public RealmList<Outlay> getSharedOutlays() {
        return sharedOutlays;
    }

    public void setSharedOutlays(RealmList<Outlay> sharedOutlays) {
        this.sharedOutlays = sharedOutlays;
    }
}
