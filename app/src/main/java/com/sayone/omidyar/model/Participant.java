package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by sayone on 15/9/16.
 */
public class Participant extends RealmObject {

    @PrimaryKey
    private long id;

    private String surveyId;

    @Required
    private String name;

    private String occupation;

    private String gender;

    private int age;

    private int yearsOfEdu;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getYearsOfEdu() {
        return yearsOfEdu;
    }

    public void setYearsOfEdu(int yearsOfEdu) {
        this.yearsOfEdu = yearsOfEdu;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", occupation='" + occupation + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", yearsOfEdu=" + yearsOfEdu +
                '}';
    }
}