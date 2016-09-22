package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 15/9/16.
 */
public class PastureLand extends RealmObject {
    @PrimaryKey
    private long id;

    private String surveyId;
}
