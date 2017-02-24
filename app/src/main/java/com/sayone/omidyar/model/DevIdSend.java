package com.sayone.omidyar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sayone on 23/2/17.
 */

public class DevIdSend {
    @SerializedName("device_id")
    private String device_id;

    public DevIdSend(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
