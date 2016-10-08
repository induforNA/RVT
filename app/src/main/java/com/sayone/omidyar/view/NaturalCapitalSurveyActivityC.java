package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.Participant;
import com.sayone.omidyar.model.RevenueProduct;
import com.sayone.omidyar.model.RevenueProductYears;
import com.sayone.omidyar.model.Survey;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;

public class NaturalCapitalSurveyActivityC extends BaseActivity implements View.OnClickListener {

    Realm realm;
    SharedPreferences sharedPref;
    String serveyId;

    Spinner spinnerYear;
    String year;
    Button buttonBack,buttonNext;
    LinearLayout allEditText;
    Context context;
    Button addYearsButton;
    ArrayList<EditText> editTexts;
    RealmList<RevenueProductYears> revenueProductYearsArrayList;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_capital_survey_c);

        context = this;
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = sharedPref.getString("surveyId","");

        editTexts = new ArrayList<>();
        revenueProductYearsArrayList = new RealmList<>();



        spinnerYear=(Spinner)findViewById(R.id.spinner_year);
        buttonBack=(Button)findViewById(R.id.button_back);
        buttonNext=(Button)findViewById(R.id.button_next);
        allEditText = (LinearLayout) findViewById(R.id.all_edit_text);
        addYearsButton = (Button) findViewById(R.id.add_years_button);

        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        addYearsButton.setOnClickListener(this);

        Survey results = realm.where(Survey.class)
                .equalTo("surveyId",serveyId)
                .findFirst();
        for(LandKind landKind:results.getLandKinds()){
            if(landKind.getName().equals("Forestland")){
                if(landKind.getForestLand().getRevenueProducts().size() > 0){
                    loadYears(landKind.getForestLand().getRevenueProducts().get(0).getRevenueProductYearses());
                }


                for (RevenueProduct revenueProduct1: landKind.getForestLand().getRevenueProducts()){
                    Log.e("LAND ", revenueProduct1.getName());
                }
            }
        }
        if(i == 0){
            i = 1;
        }








        ArrayAdapter<CharSequence> year_adapter = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_item);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerYear.setAdapter(year_adapter);

        //year= spinnerYear.getSelectedItem().toString();

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                year= parent.getItemAtPosition(pos).toString();
                //Log.e("Year ",year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void loadYears(RealmList<RevenueProductYears> revenueProductYearsRealmList){
        i = 1;
        LinearLayout.LayoutParams mRparams = new LinearLayout.LayoutParams(400, LinearLayout.LayoutParams.WRAP_CONTENT);
        for(RevenueProductYears revenueProductYears : revenueProductYearsRealmList){
            EditText myEditText = new EditText(context);
            myEditText.setLayoutParams(mRparams);
            myEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            myEditText.setId(i);
            myEditText.setHint("Enter year "+i);
            myEditText.setText(revenueProductYears.getYear()+"");
            allEditText.addView(myEditText);
            editTexts.add(myEditText);
            i++;
        }
    }

    public void generateYearFields(int j){
        LinearLayout.LayoutParams mRparams = new LinearLayout.LayoutParams(400, LinearLayout.LayoutParams.WRAP_CONTENT);
        int k;
        for(k=1; k<=j; k++){
            EditText myEditText = new EditText(context);
            myEditText.setLayoutParams(mRparams);
            myEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            myEditText.setId(k);
            myEditText.setHint("Enter year "+k);
            allEditText.addView(myEditText);
            editTexts.add(myEditText);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){

            case R.id.button_next:
                saveYears();


                intent=new Intent(getApplicationContext(),NaturalCapitalSurveyActivityD.class);
                startActivity(intent);
                break;

            case R.id.button_back:
                finish();
                break;

            case R.id.add_years_button:
                //Log.e("Year ",year);
                if(!year.equals("year")){
                    generateYearFields(Integer.parseInt(year));
                }else{
                    Toast.makeText(context,"Select no of years",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    public void saveYears(){
        Survey results = realm.where(Survey.class).findFirst();
        for(LandKind landKind:results.getLandKinds()){
            if(landKind.getName().equals("Forestland")){
                for (RevenueProduct revenueProduct1: landKind.getForestLand().getRevenueProducts()){
                    //Log.e("LAND ", revenueProduct1.getName());
                    //Log.e("LAND AA ", revenueProduct1.getRevenueProductYearses().size()+"");


                    for(EditText editText : editTexts){
                        //editText.setText("233");
                        //Log.e("SSS ",editText.getText().toString());
                        revenueProductYearsArrayList.add(saveProductYears(Integer.parseInt(editText.getText().toString()), revenueProduct1.getId(), "Forestland"));
                    }


                    realm.beginTransaction();
                    revenueProduct1.setRevenueProductYearses(revenueProductYearsArrayList);
                    realm.commitTransaction();
                    revenueProductYearsArrayList.clear();
                }
            }
        }

        Survey results1 = realm.where(Survey.class).findFirst();
        for(LandKind landKind:results1.getLandKinds()){
            if(landKind.getName().equals("Forestland")){
                for (RevenueProduct revenueProduct1: landKind.getForestLand().getRevenueProducts()){
                    Log.e("YEAR 1 ",revenueProduct1.getName()+"");

                    for(RevenueProductYears revenueProductYears:revenueProduct1.getRevenueProductYearses()){

                        Log.e("YEAR ",revenueProductYears.getYear()+" "+revenueProductYears.getId());
                    }
                }
            }
        }
    }


    public RevenueProductYears saveProductYears(int yearVal, long revenueProductId, String landKindName){
        Log.e("CCC ",serveyId+" "+yearVal+" "+revenueProductId+" "+landKindName);
        RevenueProductYears revenueProductYearsCheck = realm.where(RevenueProductYears.class)
                .equalTo("surveyId",serveyId)
                .equalTo("landKind",landKindName)
                .equalTo("revenueProductId",revenueProductId)
                .equalTo("year",yearVal)
                .findFirst();
        for(RevenueProductYears revenueProductYears1: realm.where(RevenueProductYears.class).findAll()){
            Log.e("BBB ", revenueProductYears1.toString());
        }
        Log.e("AA ", String.valueOf(revenueProductYearsCheck == null));
        if(revenueProductYearsCheck == null){
            realm.beginTransaction();
            RevenueProductYears revenueProductYears = realm.createObject(RevenueProductYears.class);
            revenueProductYears.setId(getNextKeyRevenueProductYears());
            revenueProductYears.setYear(yearVal);
            revenueProductYears.setRevenueProductId(revenueProductId);
            revenueProductYears.setLandKind(landKindName);
            revenueProductYears.setSurveyId(serveyId);
            realm.commitTransaction();
            return revenueProductYears;
        }else {
            return revenueProductYearsCheck;
        }
    }

    public int getNextKeyRevenueProductYears() {
        return realm.where(RevenueProductYears.class).max("id").intValue() + 1;
    }
}
