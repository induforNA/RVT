package com.sayone.omidyar.view;

import android.os.Bundle;
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

import io.realm.Realm;
import io.realm.RealmResults;

public class AdminRatesActivity extends BaseActivity implements View.OnClickListener {

    public Spinner surveyIdSpinner, landKindSpinner;
    private EditText discountRate, discountRateOverride;
    private ArrayList<String> surveyIds = new ArrayList<>();
    private ArrayList<String> surveyLandKinds = new ArrayList<>();
    private Button buttonSave, buttonREstore;
    private Realm realm;
    private SocialCapital socialCapital;
    private ArrayAdapter landKindAdapter;
    private ArrayAdapter surveyIdAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_rates);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        discountRate = (EditText) findViewById(R.id.edittext_discount_rate);
        discountRateOverride = (EditText) findViewById(R.id.edittext_overridediscount);
        buttonREstore = (Button) findViewById(R.id.button_restore_original_discount_rate);
        buttonSave = (Button) findViewById(R.id.button_save);
        surveyIdSpinner = (Spinner) findViewById(R.id.spinner_survey_id);
        landKindSpinner = (Spinner) findViewById(R.id.spinner_land_kind);

        realm = Realm.getDefaultInstance();
        RealmResults<Survey> surveyList = realm.where(Survey.class).findAll();

        surveyIds.add(0, "--Select Survey Id--");
        surveyLandKinds.add(0, "--Select Land Kind--");

        for (Survey survey : surveyList)
            surveyIds.add(survey.getSurveyId());

        surveyIdAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                surveyIds);
        landKindAdapter = new ArrayAdapter(AdminRatesActivity.this, android.R.layout.simple_spinner_dropdown_item,
                surveyLandKinds);

        surveyIdSpinner.setAdapter(surveyIdAdapter);
        landKindSpinner.setAdapter(landKindAdapter);

        surveyIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    Survey survey = realm.where(Survey.class).equalTo("surveyId", surveyIds.get(position))
                            .findFirst();
                    for (LandKind landKind : survey.getLandKinds()) {
                        if (landKind.getStatus().equals("active"))
                            surveyLandKinds.add(landKind.getName());
                        else
                            surveyLandKinds.remove(landKind.getName());
                    }
                    landKindSpinner.setAdapter(landKindAdapter);
                } else {
                    surveyLandKinds.clear();
                    surveyLandKinds.add(0, "--Select Land Kind--");
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
                    LandKind landKind = realm.where(LandKind.class).equalTo("name", surveyLandKinds.get(position))
                            .findFirst();
                    socialCapital = landKind.getSocialCapitals();
                    discountRate.setText(String.valueOf(socialCapital.getDiscountRate()));
                    discountRate.setEnabled(false);
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

                if(surveyIdSpinner.getSelectedItemPosition() != 0 || landKindSpinner.getSelectedItemPosition() !=0) {
                    realm.beginTransaction();
                    socialCapital.setDiscountRateOverride(0.0);
                    realm.commitTransaction();
                } else
                    Toast.makeText(this,"Select Survey Id and Land Kind to Save",Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_save:

                if((surveyIdSpinner.getSelectedItemPosition() != 0 || landKindSpinner.getSelectedItemPosition() !=0) &&
                        socialCapital != null) {
                    realm.beginTransaction();

                    if (!discountRateOverride.getText().toString().equals(""))
                        socialCapital.setDiscountRateOverride(Double.parseDouble(discountRateOverride.getText().toString()));
                    else
                        socialCapital.setDiscountRateOverride(0.0);
                    realm.commitTransaction();
                } else
                    Toast.makeText(this,"Select Survey Id and Land Kind to Save",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
