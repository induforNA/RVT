package com.sayone.omidyar.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sayone.omidyar.R;

import java.sql.ResultSet;

import io.realm.Realm;

public class NaturalCapitalTrend extends AppCompatActivity {

    private Button buttonBack;
    private Button buttonNext;
    private Context mContext;
    private Realm realm;
    private SharedPreferences sharedPref;
    private String mSurveyId;
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_capital_trend);

        mContext = this;
        realm = Realm.getDefaultInstance();
        sharedPref = mContext.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mSurveyId = sharedPref.getString("surveyId", "");

        buttonBack = (Button) findViewById(R.id.button_back);
        buttonNext = (Button) findViewById(R.id.button_next);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
    }
}
