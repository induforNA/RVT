package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.Frequency;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.Participant;
import com.sayone.omidyar.model.Quantity;
import com.sayone.omidyar.model.SocialCapitalAnswerOptions;
import com.sayone.omidyar.model.SocialCapitalQuestions;
import com.sayone.omidyar.model.SpredTable;
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
    String spredTableLoadStatus;
    String frequencyLoadStatus;
    String quanityLoadStatus;
    int flagVlue = 0;

    public static final String TAG = LoadingActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        flagVlue = 0;

        context = this;
        realm = Realm.getDefaultInstance();
        preferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        quetionsLoadStatus = preferences.getString("questionsLoaded", "false");
        spredTableLoadStatus = preferences.getString("spredTableLoaded", "false");
        frequencyLoadStatus = preferences.getString("frequencyLoadStatus", "false");
        quanityLoadStatus = preferences.getString("quanityLoadStatus", "false");

        goToNext();

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
                flagVlue++;
                goToNext();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (spredTableLoadStatus.equals("false")) {

            try {
                JSONObject reader = new JSONObject(loadSpredJSONFromAsset());
                JSONArray jsonArray = reader.getJSONArray("discountRate");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());

                    realm.beginTransaction();
                    SpredTable spredTable = realm.createObject(SpredTable.class);
                    spredTable.setId(getNextKeySpred());
                    spredTable.setMoreThan(jsonObject.getDouble("moreThan"));
                    spredTable.setLessThan(jsonObject.getDouble("lessThan"));
                    spredTable.setRating(jsonObject.getString("rating"));
                    spredTable.setSpread(jsonObject.getDouble("spread"));
                    realm.commitTransaction();
                }
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("spredTableLoaded", "true");
                editor.apply();
                flagVlue++;
                goToNext();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (frequencyLoadStatus.equals("false")) {

            try {
                JSONObject reader = new JSONObject(loadFrequencyJSONFromAsset());
                JSONArray jsonArray = reader.getJSONArray("frequency");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    Log.e("AA ", jsonObject.getString("harvestFrequency"));
                    Log.e("BB ", jsonObject.getInt("value")+"");

                    realm.beginTransaction();
                    Frequency frequency = realm.createObject(Frequency.class);
                    frequency.setId(getNextKeyFrequency());
                    frequency.setHarvestFrequency(jsonObject.getString("harvestFrequency"));
                    frequency.setFrequencyValue(jsonObject.getInt("value"));
                    realm.commitTransaction();
                }
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("frequencyLoadStatus", "true");
                editor.apply();
                flagVlue++;
                goToNext();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (quanityLoadStatus.equals("false")) {

            try {
                JSONObject reader = new JSONObject(loadQuantityJSONFromAsset());
                JSONArray jsonArray = reader.getJSONArray("quantity");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
//                    Log.e("AA ", jsonObject.getString("harvestFrequency"));
//                    Log.e("BB ", jsonObject.getInt("value")+"");

                    realm.beginTransaction();
                    Quantity quantity = realm.createObject(Quantity.class);
                    quantity.setId(getNextKeyQuantity());
                    quantity.setQuantityName(jsonObject.getString("quantityName"));
                    quantity.setQuantityType(jsonObject.getString("quantityType"));
                    quantity.setQuantityValue(Double.parseDouble(jsonObject.getString("quantityValue")));
                    realm.commitTransaction();
                }
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("quanityLoadStatus", "true");
                editor.apply();
                flagVlue++;
                goToNext();
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

        RealmResults<SpredTable> results1 = realm.where(SpredTable.class).findAll();
        for (SpredTable spredTable : results1) {
            Log.e(TAG + "abcd", spredTable.getRating());
        }

        RealmResults<Frequency> results2 = realm.where(Frequency.class).findAll();
        for (Frequency frequency : results2) {
            Log.e(TAG + "Frequency ", frequency.getHarvestFrequency());
        }

        RealmResults<Quantity> results3 = realm.where(Quantity.class).findAll();
        for (Quantity quantity : results3) {
            Log.e(TAG + "Quantity ", quantity.getQuantityName());
        }



    }

    public void goToNext(){
        if((flagVlue >= 4) || (quetionsLoadStatus.equals("true") && spredTableLoadStatus.equals("true") && frequencyLoadStatus.equals("true") && quanityLoadStatus.equals("true"))){
            Intent intent = new Intent(LoadingActivity.this,RegistrationActivity.class);
            startActivity(intent);
            finish();
        }
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
//        if (realm.where(SocialCapitalQuestions.class).max("id") == null) {
//            return 1;
//        }
        return realm.where(SocialCapitalQuestions.class).max("id").intValue() + 1;
    }

    public int getNextKeySpred() {
        if (realm.where(SpredTable.class).max("id") == null) {
            return 1;
        }
        return realm.where(SpredTable.class).max("id").intValue() + 1;
    }

    public int getNextKeyFrequency() {
        if (realm.where(Frequency.class).max("id") == null) {
            return 1;
        }
        return realm.where(Frequency.class).max("id").intValue() + 1;
    }

    public int getNextKeyQuantity() {
        if (realm.where(Quantity.class).max("id") == null) {
            return 1;
        }
        return realm.where(Quantity.class).max("id").intValue() + 1;
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

    public String loadSpredJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("discountRate.json");

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

    public String loadFrequencyJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("frequency.json");

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

    public String loadQuantityJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("quantity.json");

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
