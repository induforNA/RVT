package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 21/9/16.
 */

public class SocialCapitalAnswerOptions extends RealmObject {

    @PrimaryKey
    private long id;

    private String options;

    private String optionsHindi;

    private int val;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getOptionsHindi() {
        return optionsHindi;
    }

    public void setOptionsHindi(String optionsHindi) {
        this.optionsHindi = optionsHindi;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
}
