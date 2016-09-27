package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.Participant;
import com.sayone.omidyar.model.SocialCapital;
import com.sayone.omidyar.model.SocialCapitalAnswer;
import com.sayone.omidyar.model.SocialCapitalQuestions;
import com.sayone.omidyar.model.Survey;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by sayone on 20/9/16.
 */
public class SocialCapitalStartActivity extends BaseActivity implements View.OnClickListener{

    public static final String TAG = SocialCapitalStartActivity.class.getName();
    private Realm realm;
    private SharedPreferences preferences;
    Context context;

    String currentSocialCapitalServey;
    String serveyId;
    private TextView landType;
    private Button backButton;
    private Button nextButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_capital_start);

        context = this;
        realm = Realm.getDefaultInstance();
        preferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = preferences.getString("surveyId","");
        currentSocialCapitalServey = preferences.getString("currentSocialCapitalServey","");

        landType = (TextView) findViewById(R.id.land_type);
        backButton = (Button) findViewById(R.id.back_button);
        nextButton = (Button) findViewById(R.id.next_button);

        backButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        if(currentSocialCapitalServey.equals("")){
            RealmResults<LandKind> landKindRealmResults = realm.where(LandKind.class)
                    .equalTo("surveyId",serveyId)
                    .equalTo("status","active")
                    .findAll();
            SharedPreferences.Editor editor = preferences.edit();
            currentSocialCapitalServey = landKindRealmResults.get(0).getName();
            editor.putString("currentSocialCapitalServey",currentSocialCapitalServey);
            editor.apply();
//            for (LandKind landKind : landKindRealmResults) {
//                Log.e("NAME ",landKind.getName());
//            }
        }
//        else if(currentSocialCapitalServey.equals("Forestland")){
//
//        }else if(currentSocialCapitalServey.equals("Cropland")){
//
//        }else if(currentSocialCapitalServey.equals("Pastureland")){
//
//        }else if(currentSocialCapitalServey.equals("Mining Land")){
//
//        }

        LandKind landKind = realm.where(LandKind.class)
                .equalTo("surveyId",serveyId)
                .equalTo("name",currentSocialCapitalServey)
                .equalTo("status","active")
                .findFirst();
        if(landKind.getSocialCapitals().getSocialCapitalAnswers().size() == 0) {
            Log.e("Here ", "NULL");


            RealmResults<SocialCapitalQuestions> socialCapitalQuestionses = realm.where(SocialCapitalQuestions.class).findAll();
            RealmList<SocialCapitalAnswer> socialCapitalAnswers = new RealmList<>();
            for (SocialCapitalQuestions socialCapitalQuestions : socialCapitalQuestionses) {
                Log.e("Social capital", socialCapitalQuestions.getId() + " " + socialCapitalQuestions.getQuestion());
                realm.beginTransaction();
                SocialCapitalAnswer socialCapitalAnswer = realm.createObject(SocialCapitalAnswer.class);
                socialCapitalAnswer.setId(getNextKeySocialCapitalAnswer());
                socialCapitalAnswer.setSurveyId(serveyId);
                socialCapitalAnswer.setSocialCapitalQuestion(socialCapitalQuestions);
                realm.commitTransaction();
                socialCapitalAnswers.add(socialCapitalAnswer);
            }
            realm.beginTransaction();
            landKind.getSocialCapitals().setSocialCapitalAnswers(socialCapitalAnswers);
            realm.commitTransaction();
        }




        LandKind landKind1 = realm.where(LandKind.class)
                .equalTo("surveyId",serveyId)
                .equalTo("name",currentSocialCapitalServey)
                .equalTo("status","active")
                .findFirst();

        Log.e("LLL ", landKind1.getSocialCapitals().getSocialCapitalAnswers().toString());




        landType.setText(currentSocialCapitalServey);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.next_button:
                Intent intent = new Intent(SocialCapitalStartActivity.this, SocialCapitalActivity.class);
                startActivity(intent);
                break;
        }
    }

    public int getNextKeySocialCapitalAnswer() {
        return realm.where(SocialCapitalAnswer.class).max("id").intValue() + 1;
    }
}