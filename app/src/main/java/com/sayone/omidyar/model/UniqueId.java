package com.sayone.omidyar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sayone on 24/2/17.
 */

public class UniqueId {

    @SerializedName("unique_id")
    private String uniqueId;

    @SerializedName("response")
    private String response;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
