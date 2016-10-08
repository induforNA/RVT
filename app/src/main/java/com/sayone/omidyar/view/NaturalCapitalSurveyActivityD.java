package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.RevenueProduct;
import com.sayone.omidyar.model.Survey;

import io.realm.Realm;
import io.realm.RealmList;

public class NaturalCapitalSurveyActivityD extends BaseActivity implements View.OnClickListener {

    Realm realm;
    SharedPreferences sharedPref;
    String serveyId;
    Context context;

    Spinner spinnerYear,spinnerUnit,spinnerCurrency,spinnerNumberTimes,spinnerTimePeriod;
    String year,unit,currency,numberTimes,timePeriod;
    Button buttonBack,buttonNext;
    TextView loadQuestions;
    Button saveBtn;

    RealmList<RevenueProduct> revenueProducts;
    int totalCostProductCount = 0;
    int currentCostProductIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_capital_survey_d);

        context = this;
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = sharedPref.getString("surveyId","");

        spinnerYear=(Spinner)findViewById(R.id.spinner_year);
        spinnerUnit=(Spinner)findViewById(R.id.spinner_unit);
        spinnerCurrency=(Spinner)findViewById(R.id.spinner_currency);
        spinnerNumberTimes=(Spinner)findViewById(R.id.spinner_number_times);
        spinnerTimePeriod=(Spinner)findViewById(R.id.spinner_time_period);
        buttonBack=(Button)findViewById(R.id.button_back);
        buttonNext=(Button)findViewById(R.id.button_next);
        loadQuestions = (TextView) findViewById(R.id.load_questions);
        saveBtn = (Button) findViewById(R.id.save_btn);

        revenueProducts = new RealmList<>();
        totalCostProductCount = 0;
        currentCostProductIndex = 0;


        Survey results = realm.where(Survey.class)
                .equalTo("surveyId",serveyId)
                .findFirst();
        for(LandKind landKind:results.getLandKinds()){
            if(landKind.getName().equals("Forestland")){
                if(landKind.getForestLand().getRevenueProducts().size() > 0){
                    //loadYears(landKind.getForestLand().getRevenueProducts().get(0).getRevenueProductYearses());
                    revenueProducts = landKind.getForestLand().getRevenueProducts();
                    totalCostProductCount = revenueProducts.size();
                    loadRevenueProduct(landKind.getForestLand().getRevenueProducts().get(0));
                    currentCostProductIndex++;
                }
            }
        }




        ArrayAdapter<CharSequence> year_adapter = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_item);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> unit_adapter = ArrayAdapter.createFromResource(this,
                R.array.unit_array, android.R.layout.simple_spinner_item);
        unit_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> currency_adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);
        currency_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> numberTimes_adapter = ArrayAdapter.createFromResource(this,
                R.array.number_of_harvests, android.R.layout.simple_spinner_item);
        numberTimes_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<CharSequence> timePeriod_adapter = ArrayAdapter.createFromResource(this,
                R.array.time_period_array, android.R.layout.simple_spinner_item);
        timePeriod_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerYear.setAdapter(year_adapter);
        spinnerUnit.setAdapter(unit_adapter);
        spinnerCurrency.setAdapter(currency_adapter);
        spinnerNumberTimes.setAdapter(numberTimes_adapter);
        spinnerTimePeriod.setAdapter(timePeriod_adapter);

        year= spinnerYear.getSelectedItem().toString();
        unit= spinnerUnit.getSelectedItem().toString();
        currency= spinnerCurrency.getSelectedItem().toString();
        numberTimes= spinnerNumberTimes.getSelectedItem().toString();
        timePeriod= spinnerTimePeriod.getSelectedItem().toString();

        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        saveBtn.setOnClickListener(this);



        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                year= parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                unit= parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                currency= parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        spinnerNumberTimes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                numberTimes= parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        spinnerTimePeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                timePeriod= parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.button_next:
                intent=new Intent(getApplicationContext(),CertificateActivity.class);
                startActivity(intent);
                break;

            case R.id.button_back:
                intent=new Intent(getApplicationContext(),NaturalCapitalSurveyActivityD.class);
                startActivity(intent);
                break;

            case R.id.save_btn:
                if(currentCostProductIndex < totalCostProductCount){
                    loadRevenueProduct(revenueProducts.get(currentCostProductIndex));
                    currentCostProductIndex++;
                }
                break;

        }
    }

    public void loadRevenueProduct(RevenueProduct revenueProductLoad){
        loadQuestions.setText(revenueProductLoad.getName());
    }
}
