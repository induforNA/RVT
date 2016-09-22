package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 22/9/16.
 */

public class SocialCapitalAnswer extends RealmObject {

    @PrimaryKey
    private long id;

    private String surveyId;

    private SocialCapitalQuestions socialCapitalQuestion;

    private int answer;

    private int factorScore;


}
