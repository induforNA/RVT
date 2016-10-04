package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.LandKind;

import io.realm.Realm;
import io.realm.RealmResults;

public class NaturalCapitalSurveyStartActivity extends BaseActivity implements View.OnClickListener {

    private TextView landType;
    private Realm realm;

    private SharedPreferences preferences;
    Context context;
    Button buttonNext,buttonBack;
    String currentSocialCapitalServey;
    String serveyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_capital_survey_start);

        context = this;
        realm = Realm.getDefaultInstance();
        preferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = preferences.getString("surveyId","");
        currentSocialCapitalServey = preferences.getString("currentSocialCapitalServey","");

        landType = (TextView)findViewById(R.id.land_type);
        buttonBack=(Button)findViewById(R.id.button_back);
        buttonNext=(Button)findViewById(R.id.button_next);

        buttonBack.setOnClickListener(this);
        buttonNext.setOnClickListener(this);

        if(currentSocialCapitalServey.equals("")){
            RealmResults<LandKind> landKindRealmResults = realm.where(LandKind.class)
                    .equalTo("surveyId",serveyId)
                    .equalTo("status","active")
                    .findAll();
            SharedPreferences.Editor editor = preferences.edit();
            currentSocialCapitalServey = landKindRealmResults.get(0).getName();
            editor.putString("currentSocialCapitalServey",currentSocialCapitalServey);
            editor.apply();
        }
        landType.setText(currentSocialCapitalServey);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){

            case R.id.button_next:
                intent=new Intent(getApplicationContext(),NaturalCapitalSurveyActivity.class);
                startActivity(intent);
                break;
            case R.id.button_back:
                finish();
                break;

        }
    }
}
