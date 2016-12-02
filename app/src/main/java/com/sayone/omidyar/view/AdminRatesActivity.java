package com.sayone.omidyar.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.SocialCapital;
import com.sayone.omidyar.model.Survey;

import java.util.ArrayList;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class AdminRatesActivity extends BaseActivity implements View.OnClickListener {

    public Spinner surveyIdSpinner, landKindSpinner;
    private EditText discountRate, discountRateOverride,inflationRate;;
    private ArrayList<String> surveyIds = new ArrayList<>();
    private ArrayList<String> surveyLandKinds = new ArrayList<>();
    private ArrayList<String> surveyLandKindsHindi = new ArrayList<>();
    private Button buttonSave, buttonREstore,saveButtonInflation;
    private Realm realm;
    private SocialCapital socialCapital;
    private ArrayAdapter landKindAdapter;
    private ArrayAdapter surveyIdAdapter;
    private String language;
    private String inflationrateString;
    private Context context;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        preferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_admin_rates);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        discountRate = (EditText) findViewById(R.id.edittext_discount_rate);
        discountRateOverride = (EditText) findViewById(R.id.edittext_overridediscount);
        buttonREstore = (Button) findViewById(R.id.button_restore_original_discount_rate);
        buttonSave = (Button) findViewById(R.id.button_save);
        surveyIdSpinner = (Spinner) findViewById(R.id.spinner_survey_id);
        landKindSpinner = (Spinner) findViewById(R.id.spinner_land_kind);
        inflationRate = (EditText) findViewById(R.id.inflation_rate);
        saveButtonInflation = (Button) findViewById(R.id.button_save_inlation);

        inflationRate.setText(preferences.getString("inflationRate", ""));

        realm = Realm.getDefaultInstance();
        language = Locale.getDefault().getDisplayLanguage();
        RealmResults<Survey> surveyList = realm.where(Survey.class).findAll();

        surveyIds.add(0,getResources().getString(R.string.select_suvey_id));
        surveyLandKinds.add(0,getResources().getString(R.string.select_landkind));

        for (Survey survey : surveyList) {
            surveyIds.add(survey.getSurveyId());
        }

        surveyIdAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                surveyIds);
        landKindAdapter = new ArrayAdapter(AdminRatesActivity.this, android.R.layout.simple_spinner_dropdown_item,
                surveyLandKinds);

        surveyIdSpinner.setAdapter(surveyIdAdapter);
        landKindSpinner.setAdapter(landKindAdapter);
        saveButtonInflation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                inflationrateString = inflationRate.getText().toString();
                editor.putString("inflationRate", inflationrateString);
                editor.apply();
                Toast toast = Toast.makeText(context,getResources().getText(R.string.text_data_saved), Toast.LENGTH_SHORT);
                toast.show();
                String rate = preferences.getString("inflationRate", "");
                Log.e("Rate :", rate);
            }
        });

        surveyIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                surveyLandKinds.clear();
                if(position != 0){
                    surveyLandKinds.add(0,getResources().getString(R.string.select_landkind));
                }
                if (position != 0) {
                    Survey survey = realm.where(Survey.class).equalTo("surveyId", surveyIds.get(position))
                            .findFirst();
                    for (LandKind landKind : survey.getLandKinds()) {
                        if (landKind.getStatus().equals("active")) {
//                            if(language.equals("हिन्दी")){
//                                if(landKind.getName().equals("Forestland")){
//                                    surveyLandKinds.add("वन भूम");
//                                }
//                                if(landKind.getName().equals("Cropland")){
//                                    surveyLandKinds.add("खेती");
//                                }
//                                if(landKind.getName().equals("Pastureland")){
//                                    surveyLandKinds.add("चरागाह भूमि");
//                                }
//                                if(landKind.getName().equals("Mining Land")){
//                                    surveyLandKinds.add("खनन भूमि");
//                                }
//                            }
//                            else{
                                surveyLandKinds.add(landKind.getName());
//                            }

                        }else {
                            surveyLandKinds.remove(landKind.getName());
                        }
                    }
                    landKindSpinner.setAdapter(landKindAdapter);
                } else {
                    surveyLandKinds.clear();
                    surveyLandKinds.add(0,getResources().getString(R.string.select_landkind));
                    landKindSpinner.setAdapter(landKindAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        landKindSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    LandKind landKind = realm.where(LandKind.class)
                            .equalTo("surveyId",surveyIdSpinner.getSelectedItem().toString())
                            .equalTo("status","active")
                            .equalTo("name", surveyLandKinds.get(position))
                            .findFirst();
                    socialCapital = landKind.getSocialCapitals();
                    if(socialCapital.isDiscountFlag()) {
                        discountRate.setText(String.valueOf(socialCapital.getDiscountRateOverride()));
                        discountRateOverride.setText(String.valueOf(socialCapital.getDiscountRateOverride()));
                        discountRate.setEnabled(false);
                    }else {
                        discountRate.setText(String.valueOf(socialCapital.getDiscountRate()));
                        discountRate.setEnabled(false);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonREstore.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_restore_original_discount_rate:
                discountRateOverride.setText(String.valueOf(0.0));

                if((surveyIdSpinner.getSelectedItemPosition() != 0 || landKindSpinner.getSelectedItemPosition() !=0) &&
                        socialCapital != null) {
                    realm.beginTransaction();
                    socialCapital.setDiscountRateOverride(0.0);
                    discountRate.setText(""+socialCapital.getDiscountRate());
                    Toast.makeText(this,getResources().getText(R.string.value_restored),Toast.LENGTH_SHORT).show();
                    socialCapital.setDiscountFlag(false);
                    realm.commitTransaction();

                } else
                    Toast.makeText(this,getResources().getText(R.string.select_surveyid_landkind),Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_save:

                if((surveyIdSpinner.getSelectedItemPosition() != 0 || landKindSpinner.getSelectedItemPosition() !=0) &&
                        socialCapital != null) {
                    realm.beginTransaction();

                    if (!discountRateOverride.getText().toString().equals("")) {
                        socialCapital.setDiscountRateOverride(Double.parseDouble(discountRateOverride.getText().toString()));
                        discountRate.setText(discountRateOverride.getText().toString());
                        socialCapital.setDiscountFlag(true);
                    }else {
                        socialCapital.setDiscountRateOverride(0.0);
                    }

                    realm.commitTransaction();
                    Toast.makeText(this,getResources().getText(R.string.value_saved_success),Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,getResources().getText(R.string.select_surveyid_landkind), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
