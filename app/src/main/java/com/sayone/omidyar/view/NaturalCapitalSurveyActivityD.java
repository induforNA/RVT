package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.CashFlow;
import com.sayone.omidyar.model.Component;
import com.sayone.omidyar.model.CostElementYears;
import com.sayone.omidyar.model.Frequency;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.Quantity;
import com.sayone.omidyar.model.RevenueProduct;
import com.sayone.omidyar.model.RevenueProductYears;
import com.sayone.omidyar.model.Survey;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class NaturalCapitalSurveyActivityD extends BaseActivity implements View.OnClickListener {

    Realm realm;
    SharedPreferences sharedPref;
    String serveyId;
    Context context;
    String currentSocialCapitalServey;

    Spinner spinnerYear,spinnerUnit,spinnerCurrency,spinnerTimePeriod;
    String year,unit,currency,numberTimes,timePeriod;
    Button buttonBack,buttonNext;
    TextView loadQuestions;
    Button saveBtn;

    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView textViewAbout;
    private TextView logout;
    private TextView startSurvey;
    private DrawerLayout menuDrawerLayout;
    private TextView surveyIdDrawer;

    RealmList<RevenueProduct> revenueProducts;
    int totalCostProductCount = 0;
    int currentCostProductIndex = 0;
    int totalYearsCount = 0;
    int currentYearIndex = 0;
    int currentCostProductIndexSave = 0;
    int currentYearIndexSave = 0;

    ArrayAdapter<String> timePeriod_adapter;
    ArrayList<String> timePeriodList;

    ArrayAdapter<String> year_adapter;
    ArrayList<String> yearList;

    ArrayAdapter<String> unit_adapter;
    ArrayList<String> unitList;

    TextView quantityQuestion;
    TextView productQuestion;
    EditText noOfTimesEdit;
    EditText quanityEdit;
    EditText priceEdit;

    double inflationRate = 0.05;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_capital_survey_d);

        context = this;
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = sharedPref.getString("surveyId","");
        currentSocialCapitalServey = sharedPref.getString("currentSocialCapitalServey","");

        spinnerYear = (Spinner)findViewById(R.id.spinner_year);
        spinnerUnit = (Spinner)findViewById(R.id.spinner_unit);
        spinnerCurrency = (Spinner)findViewById(R.id.spinner_currency);
        spinnerTimePeriod = (Spinner)findViewById(R.id.spinner_time_period);
        buttonBack = (Button)findViewById(R.id.button_back);
        buttonNext = (Button)findViewById(R.id.button_next);
        loadQuestions = (TextView) findViewById(R.id.load_questions);
        saveBtn = (Button) findViewById(R.id.save_btn);
        quantityQuestion = (TextView) findViewById(R.id.quantity_question);
        productQuestion = (TextView) findViewById(R.id.product_question);
        noOfTimesEdit = (EditText) findViewById(R.id.no_of_times_edit);
        quanityEdit = (EditText) findViewById(R.id.quanity_edit);
        priceEdit = (EditText) findViewById(R.id.price_edit);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        textViewAbout = (TextView) findViewById(R.id.text_view_about);
        logout = (TextView) findViewById(R.id.logout);
        startSurvey=(TextView)findViewById(R.id.text_start_survey);
        surveyIdDrawer=(TextView)findViewById(R.id.text_view_id);

        revenueProducts = new RealmList<>();
        totalCostProductCount = 0;
        currentCostProductIndex = 0;
        totalYearsCount = 0;
        currentYearIndex = 0;
        currentCostProductIndexSave = 0;
        currentYearIndexSave = 0;
        inflationRate = 0.05;

        yearList = new ArrayList<>();
        timePeriodList = new ArrayList<>();
        unitList = new ArrayList<>();

        year_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearList);
        timePeriod_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timePeriodList);
        unit_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitList);


        Survey results = realm.where(Survey.class)
                .equalTo("surveyId",serveyId)
                .findFirst();

        RealmResults<Frequency> frequencyResult = realm.where(Frequency.class).findAll();
        for(Frequency frequency:frequencyResult){
            timePeriodList.add(frequency.getHarvestFrequency());
        }

        RealmResults<Quantity> quantityResult = realm.where(Quantity.class).findAll();
        for(Quantity quantity:quantityResult){
            unitList.add(quantity.getQuantityName());
        }








//        year_adapter = ArrayAdapter.createFromResource(this,
//                R.array.year_array, android.R.layout.simple_spinner_item);

        //year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        ArrayAdapter<CharSequence> unit_adapter = ArrayAdapter.createFromResource(this,
//                R.array.unit_array, android.R.layout.simple_spinner_item);
//        unit_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> currency_adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);
        currency_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        final ArrayAdapter<CharSequence> timePeriod_adapter = ArrayAdapter.createFromResource(this,
//                R.array.time_period_array, android.R.layout.simple_spinner_item);
//        timePeriod_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerYear.setAdapter(year_adapter);
        spinnerUnit.setAdapter(unit_adapter);
        spinnerCurrency.setAdapter(currency_adapter);
        spinnerTimePeriod.setAdapter(timePeriod_adapter);


        for(LandKind landKind:results.getLandKinds()){
            if(landKind.getName().equals("Forestland") && currentSocialCapitalServey.equals("Forestland")){
                if(landKind.getForestLand().getRevenueProducts().size() > 0){
                    //loadYears(landKind.getForestLand().getRevenueProducts().get(0).getRevenueProductYearses());
                    revenueProducts = landKind.getForestLand().getRevenueProducts();
                    totalCostProductCount = revenueProducts.size();
                    currentCostProductIndex = 0;
                    currentYearIndex = 0;
                    if(currentCostProductIndex < totalCostProductCount){
                        loadRevenueProduct(landKind.getForestLand().getRevenueProducts().get(currentCostProductIndex));
                    }
                }
            }else if(landKind.getName().equals("Cropland") && currentSocialCapitalServey.equals("Cropland")){
                if(landKind.getCropLand().getRevenueProducts().size() > 0){
                    //loadYears(landKind.getForestLand().getRevenueProducts().get(0).getRevenueProductYearses());
                    revenueProducts = landKind.getCropLand().getRevenueProducts();
                    totalCostProductCount = revenueProducts.size();
                    currentCostProductIndex = 0;
                    currentYearIndex = 0;
                    if(currentCostProductIndex < totalCostProductCount){
                        loadRevenueProduct(landKind.getCropLand().getRevenueProducts().get(currentCostProductIndex));
                    }
                }
            }else if(landKind.getName().equals("Pastureland") && currentSocialCapitalServey.equals("Pastureland")){
                if(landKind.getPastureLand().getRevenueProducts().size() > 0){
                    //loadYears(landKind.getForestLand().getRevenueProducts().get(0).getRevenueProductYearses());
                    revenueProducts = landKind.getPastureLand().getRevenueProducts();
                    totalCostProductCount = revenueProducts.size();
                    currentCostProductIndex = 0;
                    currentYearIndex = 0;
                    if(currentCostProductIndex < totalCostProductCount){
                        loadRevenueProduct(landKind.getPastureLand().getRevenueProducts().get(currentCostProductIndex));
                    }
                }
            }else if(landKind.getName().equals("Mining Land") && currentSocialCapitalServey.equals("Mining Land")){
                if(landKind.getMiningLand().getRevenueProducts().size() > 0){
                    //loadYears(landKind.getForestLand().getRevenueProducts().get(0).getRevenueProductYearses());
                    revenueProducts = landKind.getMiningLand().getRevenueProducts();
                    totalCostProductCount = revenueProducts.size();
                    currentCostProductIndex = 0;
                    currentYearIndex = 0;
                    if(currentCostProductIndex < totalCostProductCount){
                        loadRevenueProduct(landKind.getMiningLand().getRevenueProducts().get(currentCostProductIndex));
                    }
                }
            }
        }





        ///year= spinnerYear.getSelectedItem().toString();
        unit= spinnerUnit.getSelectedItem().toString();
        currency= spinnerCurrency.getSelectedItem().toString();
//        timePeriod= spinnerTimePeriod.getSelectedItem().toString();

        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        imageViewMenuIcon.setOnClickListener(this);
        drawerCloseBtn.setOnClickListener(this);
        textViewAbout.setOnClickListener(this);
        logout.setOnClickListener(this);
        startSurvey.setOnClickListener(this);
        surveyIdDrawer.setText(serveyId);



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
                allCashFlow();
                calculateNPV();

                intent=new Intent(getApplicationContext(),NaturalCapitalCostActivityA.class);
                startActivity(intent);
                break;

            case R.id.button_back:
//                intent=new Intent(getApplicationContext(),NaturalCapitalSurveyActivityD.class);
//                startActivity(intent);
                finish();
                break;

            case R.id.image_view_menu_icon:
                toggleMenuDrawer();
                break;
            case  R.id.drawer_close_btn:
                toggleMenuDrawer();
                break;
            case  R.id.text_view_about:
                Intent i = new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(i);
                break;
            case R.id.text_start_survey:
                Intent intentt = new Intent(getApplicationContext(),MainActivity.class);
                intentt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentt);
                break;
            case R.id.logout:
                Intent intents = new Intent(getApplicationContext(),RegistrationActivity.class);
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intents);
                break;


            case R.id.save_btn:
                saveYearlyDatas(revenueProducts.get(currentCostProductIndexSave));

                if(currentCostProductIndex < totalCostProductCount){
                    loadRevenueProduct(revenueProducts.get(currentCostProductIndex));
                    //currentCostProductIndex++;
                }else{
                    Toast.makeText(context,"Completed ",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    public void loadRevenueProduct(RevenueProduct revenueProductLoad){
        if(revenueProductLoad.getType().equals("Timber")){
            loadQuestions.setText(getResources().getString(R.string.qn_natural_complex_1_1)+revenueProductLoad.getName()+getResources().getString(R.string.qn_natural_complex_1_2)+"?");
            quantityQuestion.setText(getResources().getString(R.string.qn_natural_complex_2_1)+revenueProductLoad.getName()+getResources().getString(R.string.qn_natural_complex_2_2));
            productQuestion.setText(getResources().getString(R.string.qn_natural_complex_3_1)+revenueProductLoad.getName()+getResources().getString(R.string.qn_natural_complex_3_2));
        }else if(revenueProductLoad.getType().equals("Non Timber")){
            loadQuestions.setText(getResources().getString(R.string.qn_natural_complex_1_1)+revenueProductLoad.getName()+getResources().getString(R.string.qn_natural_complex_1_2)+"?");
            quantityQuestion.setText(getResources().getString(R.string.qn_natural_complex_2_1)+revenueProductLoad.getName()+getResources().getString(R.string.qn_natural_complex_2_2));
            productQuestion.setText(getResources().getString(R.string.qn_natural_complex_3_1)+revenueProductLoad.getName()+getResources().getString(R.string.qn_natural_complex_3_2));
        }else {
            loadQuestions.setText(getResources().getString(R.string.qn_natural_complex_1_1)+revenueProductLoad.getName()+getResources().getString(R.string.qn_natural_complex_1_2)+"?");
            quantityQuestion.setText(getResources().getString(R.string.qn_natural_complex_2_1)+revenueProductLoad.getName()+getResources().getString(R.string.qn_natural_complex_2_2));
            productQuestion.setText(getResources().getString(R.string.qn_natural_complex_3_1)+revenueProductLoad.getName()+getResources().getString(R.string.qn_natural_complex_3_2));
        }






        //yearList = revenueProductLoad.getRevenueProductYearses();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        yearList.clear();
        totalYearsCount = 0;
        for(RevenueProductYears revenueProductYears:revenueProductLoad.getRevenueProductYearses()){
            if(revenueProductYears.getYear() < currentYear && revenueProductYears.getYear() != 0){
                yearList.add(revenueProductYears.getYear()+"");
                year_adapter.notifyDataSetChanged();

                Log.e("REV PROD ID ",revenueProductYears.getRevenueProductId()+"");
                totalYearsCount++;
            }
        }

        //totalYearsCount = revenueProductLoad.getRevenueProductYearses().size();
        Log.e("Current year ",currentYearIndex+"");
        Log.e("Total year ",totalYearsCount+"");
        Log.e("Current cost ",currentCostProductIndex+"");
        Log.e("Total cost ",totalCostProductCount+"");

        currentCostProductIndexSave = currentCostProductIndex;
        currentYearIndexSave = currentYearIndex;

        if(currentYearIndex < totalYearsCount) {
            loadRevenueYears(revenueProductLoad.getRevenueProductYearses().get(currentYearIndex), revenueProductLoad);
            if (currentYearIndex == totalYearsCount) {
                currentCostProductIndex++;
                currentYearIndex = 0;
            }
        }
        Log.e("Current year ",currentYearIndex+"");
        Log.e("Total year ",totalYearsCount+"");
        Log.e("Current cost ",currentCostProductIndex+"");
        Log.e("Total cost ",totalCostProductCount+"");
    }

    public void loadRevenueYears(RevenueProductYears revenueProductYearsLoad, RevenueProduct revenueProductLoad1){
        if(revenueProductYearsLoad.getHarvestFrequencyValue() != 0){
            noOfTimesEdit.setText(revenueProductYearsLoad.getHarvestFrequencyValue()+"");
        }
        if(revenueProductYearsLoad.getQuantityValue() != 0){
            quanityEdit.setText(revenueProductYearsLoad.getQuantityValue()+"");
        }
        if(revenueProductYearsLoad.getMarketPriceValue() != 0){
            priceEdit.setText(revenueProductYearsLoad.getMarketPriceValue()+"");
        }



        spinnerYear.setSelection(currentYearIndex);

        Frequency frequency = realm.where(Frequency.class)
                .equalTo("frequencyValue",(int) revenueProductYearsLoad.getHarvestFrequencyUnit())
                .findFirst();

        if(timePeriodList.size() != 0 && frequency != null){
            Log.e("TEST FRE ", timePeriod_adapter.getPosition(frequency.getHarvestFrequency())+"");

            spinnerTimePeriod.setSelection(timePeriod_adapter.getPosition(frequency.getHarvestFrequency()));
            //spinnerTimePeriod.setSelection(timePeriodList.indexOf(frequency.getHarvestFrequency()));
        }


        Quantity quantity = realm.where(Quantity.class)
                .equalTo("quantityName",revenueProductYearsLoad.getQuantityUnit())
                .findFirst();

        if(unitList.size() != 0 && quantity != null){
            Log.e("QUANTITY ", unit_adapter.getPosition(quantity.getQuantityName())+"");
            spinnerUnit.setSelection(unit_adapter.getPosition(quantity.getQuantityName()));
        }

        currentYearIndex++;
    }

    public void saveYearlyDatas(RevenueProduct revenueProduct2){
        Log.e("REVE PRO ",revenueProduct2.toString()+" YEAR "+spinnerYear.getSelectedItem());
        for(RevenueProductYears revenueProductYears1:revenueProduct2.getRevenueProductYearses()){
            if(String.valueOf(revenueProductYears1.getYear()).equals(spinnerYear.getSelectedItem()) ){
                Log.e("REVE YEAR ",revenueProductYears1.toString());
                String spinnerTimePeriodStr = spinnerTimePeriod.getSelectedItem().toString();
                Frequency frequency = realm.where(Frequency.class)
                        .equalTo("harvestFrequency",spinnerTimePeriodStr)
                        .findFirst();



                int yearCurent = Calendar.getInstance().get(Calendar.YEAR);
                int yearIndex = revenueProductYears1.getYear() - yearCurent;


                if(noOfTimesEdit.getText().toString().equals("")){
                    noOfTimesEdit.setText("0");
                }
                if(quanityEdit.getText().toString().equals("")){
                    quanityEdit.setText("0");
                }
                if(priceEdit.getText().toString().equals("")){
                    priceEdit.setText("0");
                }


                double total = frequency.getFrequencyValue()
                        * Integer.parseInt(noOfTimesEdit.getText().toString())
                        * Double.parseDouble(priceEdit.getText().toString())
                        * Double.parseDouble(quanityEdit.getText().toString());



                realm.beginTransaction();
                revenueProductYears1.setHarvestFrequencyValue(Integer.parseInt(noOfTimesEdit.getText().toString()));
                revenueProductYears1.setHarvestFrequencyUnit(frequency.getFrequencyValue());
                revenueProductYears1.setQuantityValue(Double.parseDouble(quanityEdit.getText().toString()));
                revenueProductYears1.setQuantityUnit(spinnerUnit.getSelectedItem().toString());
                revenueProductYears1.setMarketPriceValue(Double.parseDouble(priceEdit.getText().toString()));
                revenueProductYears1.setMarketPriceCurrency(spinnerCurrency.getSelectedItem().toString());
                revenueProductYears1.setProjectedIndex(yearIndex);
                revenueProductYears1.setSubtotal(total);
                realm.commitTransaction();

                Log.e("RE CHECK ",revenueProductYears1.toString());
            }
        }
        calculateTrend(revenueProduct2.getId());
    }

    public void calculateTrend(long revenueProductIdLong){
        RevenueProduct revenueProduct4 = realm.where(RevenueProduct.class)
                .equalTo("id",revenueProductIdLong)
                .findFirst();
        double harvestFre = 0;
        double harvestTimes = 0;
        double harvestPrice = 0;

        double freqUnit = 0;
        String quaUnit = "";
        String priceCurrency = "";
        if(revenueProduct4.getRevenueProductYearses().size() > 0){
            if(revenueProduct4.getRevenueProductYearses().get(0).getHarvestFrequencyUnit() == 0){
                for(RevenueProductYears revenueProductYears:revenueProduct4.getRevenueProductYearses()){
                    if(revenueProductYears.getProjectedIndex() < 0){
                        harvestFre = 0;
                        if(harvestTimes < revenueProductYears.getQuantityValue()){
                            harvestTimes = revenueProductYears.getQuantityValue();
                        }
                        if(harvestPrice < revenueProductYears.getMarketPriceValue()){
                            harvestPrice = revenueProductYears.getMarketPriceValue();
                        }
                        freqUnit = revenueProductYears.getHarvestFrequencyUnit();
                        quaUnit = revenueProductYears.getQuantityUnit();
                        priceCurrency = revenueProductYears.getMarketPriceCurrency();
                    }
                }
            }else{
                int eleCount = 0;
                for(RevenueProductYears revenueProductYears:revenueProduct4.getRevenueProductYearses()){
                    if(revenueProductYears.getProjectedIndex() < 0){
                        harvestFre = revenueProductYears.getHarvestFrequencyValue();
                        harvestTimes = harvestTimes + revenueProductYears.getQuantityValue();
                        harvestPrice = harvestPrice + revenueProductYears.getMarketPriceValue();

                        freqUnit = revenueProductYears.getHarvestFrequencyUnit();
                        quaUnit = revenueProductYears.getQuantityUnit();
                        priceCurrency = revenueProductYears.getMarketPriceCurrency();

                        eleCount++;
                    }
                }

                harvestTimes = harvestTimes/eleCount;
                harvestPrice = harvestPrice/eleCount;
            }
        }

        for(RevenueProductYears revenueProductYears:revenueProduct4.getRevenueProductYearses()){
            if(revenueProductYears.getProjectedIndex() == 0){
                realm.beginTransaction();
                revenueProductYears.setHarvestFrequencyValue((int) harvestFre);
                revenueProductYears.setHarvestFrequencyUnit(freqUnit);
                revenueProductYears.setQuantityValue(harvestTimes);
                revenueProductYears.setQuantityUnit(quaUnit);
                revenueProductYears.setMarketPriceValue(harvestPrice);
                revenueProductYears.setMarketPriceCurrency(priceCurrency);
                revenueProductYears.setProjectedIndex(0);
                revenueProductYears.setSubtotal(0);
                realm.commitTransaction();
            }
            if(revenueProductYears.getProjectedIndex() > 0){
                double marketPriceVal = harvestPrice * Math.pow((1 + inflationRate), revenueProductYears.getProjectedIndex());


                double totalVal = freqUnit
                        * harvestFre
                        * harvestTimes
                        * marketPriceVal;

                realm.beginTransaction();
                revenueProductYears.setHarvestFrequencyValue((int) harvestFre);
                revenueProductYears.setHarvestFrequencyUnit(freqUnit);
                revenueProductYears.setQuantityValue(harvestTimes);
                revenueProductYears.setQuantityUnit(quaUnit);
                revenueProductYears.setMarketPriceValue(marketPriceVal);
                revenueProductYears.setMarketPriceCurrency(priceCurrency);
                revenueProductYears.setSubtotal(totalVal);
                realm.commitTransaction();
            }
        }


//        realm.beginTransaction();
//        revenueProductYears1.setHarvestFrequencyValue(Integer.parseInt(noOfTimesEdit.getText().toString()));
//        revenueProductYears1.setHarvestFrequencyUnit(frequency.getFrequencyValue());
//        revenueProductYears1.setQuantityValue(Double.parseDouble(quanityEdit.getText().toString()));
//        revenueProductYears1.setQuantityUnit(spinnerUnit.getSelectedItem().toString());
//        revenueProductYears1.setMarketPriceValue(Double.parseDouble(priceEdit.getText().toString()));
//        revenueProductYears1.setMarketPriceCurrency(spinnerCurrency.getSelectedItem().toString());
//        revenueProductYears1.setProjectedIndex(yearIndex);
//        revenueProductYears1.setSubtotal(total);
//        realm.commitTransaction();
    }

    public void allCashFlow(){
        Survey results = realm.where(Survey.class)
                .equalTo("surveyId",serveyId)
                .findFirst();
        RealmList<CashFlow> cashFlows = new RealmList<>();
        for(LandKind landKind:results.getLandKinds()){

            if(landKind.getName().equals("Forestland") && currentSocialCapitalServey.equals("Forestland")){
                Log.e("DIS RATE ", landKind.getSocialCapitals().getDiscountRate()+"");
                int k = 0;
                for(RevenueProduct revenueProduct:landKind.getForestLand().getRevenueProducts()){
                    if(k <= 0) {
                        for (RevenueProductYears revenueProductYears : revenueProduct.getRevenueProductYearses()) {
                            cashFlows.add(calculateCashFlow("Forestland",revenueProductYears.getYear(),landKind.getSocialCapitals().getDiscountRate()));
                        }
                    }
                    k++;
                }
                realm.beginTransaction();
                landKind.getForestLand().setCashFlows(cashFlows);
                realm.commitTransaction();
            }else if(landKind.getName().equals("Cropland") && currentSocialCapitalServey.equals("Cropland")){
                int k = 0;
                for(RevenueProduct revenueProduct:landKind.getCropLand().getRevenueProducts()){
                    if(k <= 0) {
                        for (RevenueProductYears revenueProductYears : revenueProduct.getRevenueProductYearses()) {
                            cashFlows.add(calculateCashFlow("Cropland",revenueProductYears.getYear(),landKind.getSocialCapitals().getDiscountRate()));
                        }
                    }
                    k++;
                }
                realm.beginTransaction();
                landKind.getCropLand().setCashFlows(cashFlows);
                realm.commitTransaction();
            }else if(landKind.getName().equals("Pastureland") && currentSocialCapitalServey.equals("Pastureland")){
                int k = 0;
                for(RevenueProduct revenueProduct:landKind.getPastureLand().getRevenueProducts()){
                    if(k <= 0) {
                        for (RevenueProductYears revenueProductYears : revenueProduct.getRevenueProductYearses()) {
                            cashFlows.add(calculateCashFlow("Pastureland",revenueProductYears.getYear(),landKind.getSocialCapitals().getDiscountRate()));
                        }
                    }
                    k++;
                }
                realm.beginTransaction();
                landKind.getPastureLand().setCashFlows(cashFlows);
                realm.commitTransaction();
            }else if(landKind.getName().equals("Mining Land") && currentSocialCapitalServey.equals("Mining Land")){
                int k = 0;
                for(RevenueProduct revenueProduct:landKind.getMiningLand().getRevenueProducts()){
                    if(k <= 0) {
                        for (RevenueProductYears revenueProductYears : revenueProduct.getRevenueProductYearses()) {
                            cashFlows.add(calculateCashFlow("Mining Land",revenueProductYears.getYear(),landKind.getSocialCapitals().getDiscountRate()));
                        }
                    }
                    k++;
                }
                realm.beginTransaction();
                landKind.getMiningLand().setCashFlows(cashFlows);
                realm.commitTransaction();
            }
        }


    }

    public CashFlow calculateCashFlow(String landKind, int year, double disRatePersent){
        RevenueProductYears revenueProductYears = realm.where(RevenueProductYears.class)
                .equalTo("surveyId",serveyId)
                .equalTo("landKind",landKind)
                .equalTo("year",year)
                .findFirst();

        CostElementYears costElementYears = realm.where(CostElementYears.class)
                .equalTo("surveyId",serveyId)
                .equalTo("landKind",landKind)
                .equalTo("year",year)
                .findFirst();


        double revenueTotal = 0;
        double costTotal = 0;
        double disRate = disRatePersent/100;

        double disFactor = 0;

        if(revenueProductYears != null){
            revenueTotal = revenueProductYears.getSubtotal();
            disFactor = 1 / Math.pow(1+disRate,revenueProductYears.getProjectedIndex());
        }
        if(costElementYears != null){
            costTotal = costElementYears.getSubtotal();
            disFactor = 1 / Math.pow(1+disRate,costElementYears.getProjectedIndex());
        }
        double cashFlowVal = revenueTotal - costTotal;


        double discountedCashFlow = cashFlowVal * disFactor;

        Log.e("DIS FACT ",disFactor+"");

        realm.beginTransaction();
        CashFlow cashFlow = realm.createObject(CashFlow.class);
        cashFlow.setId(getNextKeyCashFlow());
        cashFlow.setSurveyId(serveyId);
        cashFlow.setYear(year);
        cashFlow.setValue(cashFlowVal);
        cashFlow.setDiscountingFactor(disFactor);
        cashFlow.setDiscountedCashFlow(discountedCashFlow);
        realm.commitTransaction();
        return cashFlow;
    }

    public void calculateNPV(){
        Survey results = realm.where(Survey.class)
                .equalTo("surveyId",serveyId)
                .findFirst();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for(LandKind landKind:results.getLandKinds()){
            if(landKind.getName().equals("Forestland") && currentSocialCapitalServey.equals("Forestland")){

                double npv = 0;
                for(CashFlow cashFlow :landKind.getForestLand().getCashFlows()){
                    if(cashFlow.getYear() >= year){
                        npv = npv + cashFlow.getDiscountedCashFlow();
                    }
                }
                if(results.getComponents() == null){
                    realm.beginTransaction();
                    Component component = realm.createObject(Component.class);
                    component.setId(getNextKeyComponent());
                    component.setSurveyId(serveyId);
                    component.setForestValue(npv);
                    realm.commitTransaction();

                    realm.beginTransaction();
                    results.setComponents(component);
                    realm.commitTransaction();
                }else{
                    realm.beginTransaction();
                    results.getComponents().setForestValue(npv);
                    realm.commitTransaction();
                }
            }else if(landKind.getName().equals("Cropland") && currentSocialCapitalServey.equals("Cropland")){
                double npv = 0;
                for(CashFlow cashFlow :landKind.getCropLand().getCashFlows()){
                    if(cashFlow.getYear() >= year){
                        npv = npv + cashFlow.getDiscountedCashFlow();
                    }
                }
                if(results.getComponents() == null){
                    realm.beginTransaction();
                    Component component = realm.createObject(Component.class);
                    component.setId(getNextKeyComponent());
                    component.setSurveyId(serveyId);
                    component.setCroplandValue(npv);
                    realm.commitTransaction();

                    realm.beginTransaction();
                    results.setComponents(component);
                    realm.commitTransaction();
                }else{
                    realm.beginTransaction();
                    results.getComponents().setCroplandValue(npv);
                    realm.commitTransaction();
                }

            }else if(landKind.getName().equals("Pastureland") && currentSocialCapitalServey.equals("Pastureland")){
                double npv = 0;
                for(CashFlow cashFlow :landKind.getPastureLand().getCashFlows()){
                    if(cashFlow.getYear() >= year){
                        npv = npv + cashFlow.getDiscountedCashFlow();
                    }
                }
                if(results.getComponents() == null){
                    realm.beginTransaction();
                    Component component = realm.createObject(Component.class);
                    component.setId(getNextKeyComponent());
                    component.setSurveyId(serveyId);
                    component.setPastureValue(npv);
                    realm.commitTransaction();

                    realm.beginTransaction();
                    results.setComponents(component);
                    realm.commitTransaction();
                }else{
                    realm.beginTransaction();
                    results.getComponents().setPastureValue(npv);
                    realm.commitTransaction();
                }
            }else if(landKind.getName().equals("Mining Land") && currentSocialCapitalServey.equals("Mining Land")){
                double npv = 0;
                for(CashFlow cashFlow :landKind.getMiningLand().getCashFlows()){
                    if(cashFlow.getYear() >= year){
                        npv = npv + cashFlow.getDiscountedCashFlow();
                    }
                }
                if(results.getComponents() == null){
                    realm.beginTransaction();
                    Component component = realm.createObject(Component.class);
                    component.setId(getNextKeyComponent());
                    component.setSurveyId(serveyId);
                    component.setMiningLandValue(npv);
                    realm.commitTransaction();

                    realm.beginTransaction();
                    results.setComponents(component);
                    realm.commitTransaction();
                }else{
                    realm.beginTransaction();
                    results.getComponents().setMiningLandValue(npv);
                    realm.commitTransaction();
                }
            }
        }
    }

    public int getNextKeyCashFlow() {
        return realm.where(CashFlow.class).max("id").intValue() + 1;
    }

    public int getNextKeyComponent() {
        return realm.where(Component.class).max("id").intValue() + 1;
    }

    public void toggleMenuDrawer(){
        if(menuDrawerLayout.isDrawerOpen(GravityCompat.START)){
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            menuDrawerLayout.openDrawer(GravityCompat.START);
        }
    }
}
