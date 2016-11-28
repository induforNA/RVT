package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 22/9/16.
 */

public class MultipleAnswer extends RealmObject {

    @PrimaryKey
    private long id;

    private int answer;

    private long answerValue;

    private int questionNo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public long getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(long answerValue) {
        this.answerValue = answerValue;
    }

    public int getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(int questionNo) {
        this.questionNo = questionNo;
    }
}
