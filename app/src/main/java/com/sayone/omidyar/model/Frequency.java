package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 8/10/16.
 */

public class Frequency extends RealmObject {

    @PrimaryKey
    private long id;

    private String harvestFrequency;

    private int frequencyValue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHarvestFrequency() {
        return harvestFrequency;
    }

    public void setHarvestFrequency(String harvestFrequency) {
        this.harvestFrequency = harvestFrequency;
    }

    public int getFrequencyValue() {
        return frequencyValue;
    }

    public void setFrequencyValue(int frequencyValue) {
        this.frequencyValue = frequencyValue;
    }

    @Override
    public String toString() {
        return "Frequency{" +
                "id=" + id +
                ", harvestFrequency='" + harvestFrequency + '\'' +
                ", frequencyValue=" + frequencyValue +
                '}';
    }
}
