package com.sayone.omidyar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sayone on 8/12/16.
 */

public class ExportData {

    @SerializedName("response")
    private String response;

    public ExportData(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ExportData{" +
                "response='" + response + '\'' +
                '}';
    }
}
