package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 12/10/16.
 */

public class Quantity extends RealmObject {

    @PrimaryKey
    private long id;

    private String quantityName;

    private String quantityNameHindi;

    private String quantityType;

    private double quantityValue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuantityNameHindi() {
        return quantityNameHindi;
    }

    public void setQuantityNameHindi(String quantityNameHindi) {
        this.quantityNameHindi = quantityNameHindi;
    }

    public String getQuantityName() {
        return quantityName;
    }

    public void setQuantityName(String quantityName) {
        this.quantityName = quantityName;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public double getQuantityValue() {
        return quantityValue;
    }

    public void setQuantityValue(double quantityValue) {
        this.quantityValue = quantityValue;
    }
}
