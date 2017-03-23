package com.sayone.omidyar.model;

import com.sayone.omidyar.view.LocationConverter;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 16/2/17.
 */

public class ParcelLocation extends RealmObject{

    @PrimaryKey
    private long id;

    private String surveyId;

    private double lat_1;

    private double lat_2;

    private double lat_3;

    private double lat_4;

    private double lat_5;

    private double lat_6;

    private double lng_1;

    private double lng_2;

    private double lng_3;

    private double lng_4;

    private double lng_5;

    private double lng_6;

    private float area;

    public ParcelLocation() {
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public double getLng_6() {
        return lng_6;
    }

    public void setLng_6(double lng_6) {
        this.lng_6 = lng_6;
    }

    public double getLat_1() {
        return lat_1;
    }

    public void setLat_1(double lat_1) {
        this.lat_1 = lat_1;
    }

    public double getLat_2() {
        return lat_2;
    }

    public void setLat_2(double lat_2) {
        this.lat_2 = lat_2;
    }

    public double getLat_3() {
        return lat_3;
    }

    public void setLat_3(double lat_3) {
        this.lat_3 = lat_3;
    }

    public double getLat_4() {
        return lat_4;
    }

    public void setLat_4(double lat_4) {
        this.lat_4 = lat_4;
    }

    public double getLat_5() {
        return lat_5;
    }

    public void setLat_5(double lat_5) {
        this.lat_5 = lat_5;
    }

    public double getLat_6() {
        return lat_6;
    }

    public void setLat_6(double lat_6) {
        this.lat_6 = lat_6;
    }

    public double getLng_1() {
        return lng_1;
    }

    public void setLng_1(double lng_1) {
        this.lng_1 = lng_1;
    }

    public double getLng_2() {
        return lng_2;
    }

    public void setLng_2(double lng_2) {
        this.lng_2 = lng_2;
    }

    public double getLng_3() {
        return lng_3;
    }

    public void setLng_3(double lng_3) {
        this.lng_3 = lng_3;
    }

    public double getLng_4() {
        return lng_4;
    }

    public void setLng_4(double lng_4) {
        this.lng_4 = lng_4;
    }

    public double getLng_5() {
        return lng_5;
    }

    public void setLng_5(double lng_5) {
        this.lng_5 = lng_5;
    }

    public String getCoordinateOne(){
        if (getLat_1()!=0){
            return  LocationConverter.getLatitudeDMS(getLat_1()) + ", " + LocationConverter.getLongitudeDMS(getLng_1());
        }else{
            return "Not Set";
        }

    }

    public String getCoordinateTwo(){
        if (getLat_2()!=0){
            return  LocationConverter.getLatitudeDMS(getLat_2()) + ", " + LocationConverter.getLongitudeDMS(getLng_2());
        }else{
            return "Not Set";
        }
    }

    public String getCoordinateThree(){
        if (getLat_3()!=0){
            return  LocationConverter.getLatitudeDMS(getLat_3()) + ", " + LocationConverter.getLongitudeDMS(getLng_3());
        }else{
            return "Not Set";
        }
    }

    public String getCoordinateFour(){
        if (getLat_4()!=0){
            return LocationConverter.getLatitudeDMS(getLat_4()) + ", " + LocationConverter.getLongitudeDMS(getLng_4());
        }else{
            return "Not Set";
        }
    }

    public String getCoordinateFive(){
        if (getLat_5()!=0){
            return LocationConverter.getLatitudeDMS(getLat_5()) + ", " + LocationConverter.getLongitudeDMS(getLng_5());
        }else{
            return "Not Set";
        }
    }

    public String getCoordinateSix(){
        if (getLat_6()!=0){
            return  LocationConverter.getLatitudeDMS(getLat_6()) + ", " + LocationConverter.getLongitudeDMS(getLng_6());
        }else{
            return "Not Set";
        }
    }

    @Override
    public String toString() {
        return "ParcelLocation{" +
                "surveyId='" + surveyId + '\'' +
                ", lat_1=" + lat_1 +
                ", lat_2=" + lat_2 +
                ", lat_3=" + lat_3 +
                ", lat_4=" + lat_4 +
                ", lat_5=" + lat_5 +
                ", lat_6=" + lat_6 +
                ", lng_1=" + lng_1 +
                ", lng_2=" + lng_2 +
                ", lng_3=" + lng_3 +
                ", lng_4=" + lng_4 +
                ", lng_5=" + lng_5 +
                ", lng_6=" + lng_6 +
                ", area=" + area +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
