package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.Participant;
import com.sayone.omidyar.model.SocialCapitalAnswerOptions;
import com.sayone.omidyar.model.SocialCapitalQuestions;
import com.sayone.omidyar.model.Survey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by sayone on 21/9/16.
 */

public class LoadingActivity extends BaseActivity {

    private Realm realm;
    Context context;
    private SharedPreferences preferences;
    String quetionsLoadStatus;

    public static final String TAG = LoadingActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        context = this;
        realm = Realm.getDefaultInstance();
        preferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        quetionsLoadStatus = preferences.getString("questionsLoaded", "false");


        if (quetionsLoadStatus.equals("false")) {

            try {
                JSONObject reader = new JSONObject(loadJSONFromAsset());
                JSONArray jsonArray = reader.getJSONArray("socialCapitalQuestions");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    JSONArray optionsJsonArray = jsonObject.getJSONArray("options");
                    Log.e("LOADING ", optionsJsonArray.toString());

                    RealmList<SocialCapitalAnswerOptions> socialCapitalAnswerOptionses = new RealmList<>();
                    for (int j = 0; j < optionsJsonArray.length(); j++) {
                        JSONObject jsonObjectOptions = new JSONObject(optionsJsonArray.get(j).toString());
                        socialCapitalAnswerOptionses.add(insertOptions(jsonObjectOptions.getString("opt"), jsonObjectOptions.getString("optHindi"), jsonObjectOptions.getString("val")));
                    }
                    realm.beginTransaction();
                    SocialCapitalQuestions socialCapitalQuestions = realm.createObject(SocialCapitalQuestions.class);
                    socialCapitalQuestions.setId(getNextKeySocialCapitalQuestions());
                    socialCapitalQuestions.setQuestion(jsonObject.getString("question"));
                    socialCapitalQuestions.setQuestionHindi(jsonObject.getString("questionHindi"));
                    socialCapitalQuestions.setOptionType(jsonObject.getString("optiontype"));
                    socialCapitalQuestions.setWeight(Integer.parseInt(jsonObject.getString("weight")));
                    socialCapitalQuestions.setSocialCapitalAnswerOptionses(socialCapitalAnswerOptionses);
                    realm.commitTransaction();
                }
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("questionsLoaded", "true");
                editor.apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        RealmResults<SocialCapitalQuestions> results = realm.where(SocialCapitalQuestions.class).findAll();
        for (SocialCapitalQuestions socialCapitalQuestions1 : results) {
            Log.e(TAG + "abcd", socialCapitalQuestions1.toString());

            //Log.e(TAG, String.valueOf(survey1.getParticipants().size()));
            for (SocialCapitalAnswerOptions socialCapitalAnswerOptions : socialCapitalQuestions1.getSocialCapitalAnswerOptionses()) {
                Log.e(TAG + "abcd", socialCapitalAnswerOptions.toString());
            }
        }

//
//
//
        Intent intent = new Intent(LoadingActivity.this,RegistrationActivity.class);
        startActivity(intent);
    }

    public SocialCapitalAnswerOptions insertOptions(String option, String optionHindi, String val) {
        realm.beginTransaction();
        SocialCapitalAnswerOptions socialCapitalAnswerOptions = realm.createObject(SocialCapitalAnswerOptions.class);
        socialCapitalAnswerOptions.setId(getNextKeySocialCapitalAnswerOptions());
        socialCapitalAnswerOptions.setOptions(option);
        socialCapitalAnswerOptions.setOptionsHindi(optionHindi);
        socialCapitalAnswerOptions.setVal(Integer.parseInt(val));
        realm.commitTransaction();
        return socialCapitalAnswerOptions;
    }

    public int getNextKeySocialCapitalQuestions() {
        if (realm.where(SocialCapitalQuestions.class).max("id") == null) {
            return 1;
        }
        return realm.where(SocialCapitalQuestions.class).max("id").intValue() + 1;
    }

    public int getNextKeySocialCapitalAnswerOptions() {
        if (realm.where(SocialCapitalAnswerOptions.class).max("id") == null) {
            return 1;
        }
        return realm.where(SocialCapitalAnswerOptions.class).max("id").intValue() + 1;
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("socialCapitalQuestions.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
