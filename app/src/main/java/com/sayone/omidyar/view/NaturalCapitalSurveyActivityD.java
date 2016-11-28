package com.sayone.omidyar.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
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
import com.sayone.omidyar.model.OutlayYears;
import com.sayone.omidyar.model.Quantity;
import com.sayone.omidyar.model.RevenueProduct;
import com.sayone.omidyar.model.RevenueProductYears;
import com.sayone.omidyar.model.Survey;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
    private TextView landType;
    private TextView startSurvey;
    private DrawerLayout menuDrawerLayout;
    private TextView surveyIdDrawer;
    private TextView areaQuestion;
    private EditText areaEdit;
    TextView timePerHead;
    TextView numTimesHead;

    RealmList<RevenueProduct> revenueProducts;
    int totalCostProductCount = 0;
    int currentCostProductIndex = 0;
    int totalYearsCount = 0;
    int currentYearIndex = 0;
    int currentCostProductIndexSave = 0;
    int currentYearIndexSave = 0;
    int previousYearIndex = 0;
    int previousCostProductIndex = 0;

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
    long productReveneIdCon;
    long productReveneIdCheck = 0;

    String currentProductName;
    private String language;


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
        // saveBtn = (Button) findViewById(R.id.save_btn);
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
        areaQuestion = (TextView) findViewById(R.id.area_question);
        areaEdit = (EditText) findViewById(R.id.area_edit);
        landType=(TextView)findViewById(R.id.land_type);
        timePerHead = (TextView) findViewById(R.id.time_per_head);
        numTimesHead  = (TextView) findViewById(R.id.num_times_head);

        revenueProducts = new RealmList<>();
        totalCostProductCount = 0;
        currentCostProductIndex = 0;
        totalYearsCount = 0;
        currentYearIndex = 0;
        currentCostProductIndexSave = 0;
        currentYearIndexSave = 0;
        previousYearIndex = 0;
        previousCostProductIndex = 0;
        productReveneIdCheck = 0;
        if(currentSocialCapitalServey.equals("Forestland"))
            landType.setText(getResources().getText(R.string.string_forestland));
        if(currentSocialCapitalServey.equals("Pastureland"))
            landType.setText(getResources().getText(R.string.string_pastureland));
        if(currentSocialCapitalServey.equals("Mining Land"))
            landType.setText(getResources().getText(R.string.string_miningland));
        if(currentSocialCapitalServey.equals("Cropland"))
            landType.setText(getResources().getText(R.string.title_cropland));
     //   landType.setText(currentSocialCapitalServey);

        inflationRate = 0.05;

        language = Locale.getDefault().getDisplayLanguage();

        yearList = new ArrayList<>();
        timePeriodList = new ArrayList<>();
        unitList = new ArrayList<>();


        year_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearList);
        timePeriod_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timePeriodList);
        unit_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitList);


        Survey results = realm.where(Survey.class)
                .equalTo("surveyId",serveyId)
                .findFirst();
        if(!results.getInflationRate().equals("")){
            inflationRate = Double.parseDouble(results.getInflationRate());
        }

        RealmResults<Frequency> frequencyResult = realm.where(Frequency.class).findAll();
        for(Frequency frequency:frequencyResult){
            if(language.equals("हिन्दी")) {
                timePeriodList.add(frequency.getHarvestFrequencyHindi());
            }
            else{
                timePeriodList.add(frequency.getHarvestFrequency());
            }
        }

        RealmResults<Quantity> quantityResult = realm.where(Quantity.class).findAll();
        for(Quantity quantity:quantityResult){
            if(language.equals("हिन्दी")) {
                unitList.add(quantity.getQuantityNameHindi());
            }
            else{
                unitList.add(quantity.getQuantityName());
            }
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
                        productReveneIdCon = landKind.getForestLand().getRevenueProducts().get(currentCostProductIndex).getId();
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
                        productReveneIdCon = landKind.getCropLand().getRevenueProducts().get(currentCostProductIndex).getId();
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
                        productReveneIdCon = landKind.getPastureLand().getRevenueProducts().get(currentCostProductIndex).getId();
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
                        productReveneIdCon = landKind.getMiningLand().getRevenueProducts().get(currentCostProductIndex).getId();
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
        //saveBtn.setOnClickListener(this);
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
//                RevenueProductYears revenueProductYearsLoad = realm.where(RevenueProductYears.class)
//                        .equalTo("surveyId",serveyId)
//                        .equalTo("year",year)
//                        .equalTo("landKind",currentSocialCapitalServey)
//                        .findFirst();
//
//                loadRevenueYears(revenueProductYearsLoad, null);
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
                // Log.e("Time period ",timePeriod);
                if(timePeriod.equals("one-time") && !currentSocialCapitalServey.equals("Pastureland")){
                    noOfTimesEdit.setText("1");
                    noOfTimesEdit.setEnabled(false);
                }else{
                    noOfTimesEdit.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

//    @Override
//    public void onBackPressed() {
//        // super.onBackPressed();
//        backButtonAction();
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.e("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Log.e("CDA", "onBackPressed Called");
        backButtonAction();
    }

    public void backButtonAction(){
        int currentYearBackIndex = getIndexYears(spinnerYear.getSelectedItem().toString());
        int currentPrductNameBackIndex = getIndexRevenueProducts(currentProductName);
        if(currentYearBackIndex != -1 &&  currentPrductNameBackIndex != -1){
            if(currentPrductNameBackIndex == 0){
                if(currentYearBackIndex == 0){
                    finish();
                }else{
                    currentYearIndex = currentYearBackIndex - 1;
                    currentCostProductIndex = currentPrductNameBackIndex;
                }
            }else{
                if(currentYearBackIndex == 0){
                    currentCostProductIndex = currentPrductNameBackIndex - 1;
                    currentYearIndex = yearList.size() - 1;
                }else{
                    currentYearIndex = currentYearBackIndex - 1;
                    currentCostProductIndex = currentPrductNameBackIndex;
                }
            }
        }
        if(revenueProducts.size() == 1 && revenueProducts.get(currentCostProductIndex).getRevenueProductYearses().size() == 1){
            finish();
        }else {
            loadRevenueProduct(revenueProducts.get(currentCostProductIndex));
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.button_next:
//                allCashFlow();
//                calculateNPV();
//
//                intent=new Intent(getApplicationContext(),NaturalCapitalCostActivityA.class);
//                startActivity(intent);
                if(buttonNext.isClickable()){
                    buttonNext.setClickable(false);
                    saveYearlyDatas(revenueProducts.get(currentCostProductIndexSave));
                }

//                Log.e("YEAR ","PRE "+previousYearIndex+" Cur "+currentYearIndex);
//                Log.e("COST ","PRE "+previousCostProductIndex+" Cur "+currentCostProductIndex   );
                break;

            case R.id.button_back:
                backButtonAction();
//                int currentYearBackIndex = getIndexYears(spinnerYear.getSelectedItem().toString());
//                int currentPrductNameBackIndex = getIndexRevenueProducts(currentProductName);
//                if(currentYearBackIndex != -1 &&  currentPrductNameBackIndex != -1){
//                    if(currentPrductNameBackIndex == 0){
//                        if(currentYearBackIndex == 0){
//                            finish();
//                        }else{
//                            currentYearIndex = currentYearBackIndex - 1;
//                            currentCostProductIndex = currentPrductNameBackIndex;
//                        }
//                    }else{
//                        if(currentYearBackIndex == 0){
//                            currentCostProductIndex = currentPrductNameBackIndex - 1;
//                            currentYearIndex = yearList.size() - 1;
//                        }else{
//                            currentYearIndex = currentYearBackIndex - 1;
//                            currentCostProductIndex = currentPrductNameBackIndex;
//                        }
//                    }
//                }


//                Log.e("YEAR ","PRE "+previousYearIndex+" Cur "+currentYearIndex);
//                Log.e("COST ","PRE "+previousCostProductIndex+" Cur "+currentCostProductIndex   );

//                currentYearIndex = previousYearIndex;
//                currentCostProductIndex = previousCostProductIndex;
//                if(currentYearIndex > 0){
//                    currentYearIndex--;
//                }else if(currentYearIndex == 0){
//                    if(currentCostProductIndex > 0){
//                        currentCostProductIndex--;
//                        currentYearIndex = totalYearsCount - 1;
//                        // loadRevenueProduct(costOutlays.get(currentCostProductIndex));
//                    }else if(currentCostProductIndex == 0) {
//                        if(productReveneIdCheck == 0 || productReveneIdCheck == productReveneIdCon){
//                            finish();
//                        }else {
//                            currentYearIndex = totalYearsCount - 1;
//                        }
//                    }else{
//                        finish();
//                    }
//                }else{
//                    finish();
//                }



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


//            case R.id.save_btn:
//                // saveYearlyDatas(costOutlays.get(currentCostProductIndexSave));
//
//
//                break;

        }
    }

    public int getIndexRevenueProducts(String itemName) {
        for (int i = 0; i < revenueProducts.size(); i++){
            RevenueProduct revenueProductGet = revenueProducts.get(i);
            if (itemName.equals(revenueProductGet.getName())){
                return i;
            }
        }
        return -1;
    }

    public int getIndexYears(String itemName) {
        for (int i = 0; i < yearList.size(); i++){
            String yearGet = yearList.get(i);
            if (itemName.equals(yearGet)){
                return i;
            }
        }
        return -1;
    }

    public void loadRevenueProduct(RevenueProduct revenueProductLoad){
        // Log.e("LOAD ","REVENUE 11111");
        currentProductName = revenueProductLoad.getName();
        if(revenueProductLoad.getType().equals("Timber")){
            loadQuestions.setText(getResources().getString(R.string.qn_natural_complex_1_1)+" "+revenueProductLoad.getName()+""+getResources().getString(R.string.qn_natural_complex_1_2)+"?");
            quantityQuestion.setText(getResources().getString(R.string.qn_natural_complex_2_1)+" "+revenueProductLoad.getName()+" "+getResources().getString(R.string.qn_natural_complex_2_2));
            productQuestion.setText(getResources().getString(R.string.qn_natural_complex_3_1)+" "+revenueProductLoad.getName()+" "+getResources().getString(R.string.qn_natural_complex_3_2));
            areaQuestion.setText(getResources().getString(R.string.percentage_area_harvested));
        }else if(revenueProductLoad.getType().equals("Non Timber")) {
            loadQuestions.setText(getResources().getString(R.string.qn_natural_complex_1_1) + " " + revenueProductLoad.getName() + getResources().getString(R.string.qn_natural_complex_1_2) + "?");
            quantityQuestion.setText(getResources().getString(R.string.qn_natural_complex_2_1) + " " + revenueProductLoad.getName() + " " + getResources().getString(R.string.qn_natural_complex_2_2));
            productQuestion.setText(getResources().getString(R.string.qn_natural_complex_3_1) + " " + revenueProductLoad.getName() + " " + getResources().getString(R.string.qn_natural_complex_3_2));
            areaQuestion.setText(getResources().getString(R.string.percentage_area_harvested));
        }else if(revenueProductLoad.getType().equals("Livestock")){
            loadQuestions.setText("How many months of a year do you graze "+revenueProductLoad.getName()+" on this piece of land?");
            quantityQuestion.setText("Total amount of fodder consumed per animal per year");
            productQuestion.setText("Market Price of fodder");
            areaQuestion.setText("Number of livestock");
            timePerHead.setVisibility(View.INVISIBLE);
            spinnerTimePeriod.setVisibility(View.INVISIBLE);
            numTimesHead.setVisibility(View.INVISIBLE);
            noOfTimesEdit.setEnabled(true);
        }else {
            loadQuestions.setText(getResources().getString(R.string.qn_natural_complex_1_1)+" "+revenueProductLoad.getName()+getResources().getString(R.string.qn_natural_complex_1_2)+"?");
            quantityQuestion.setText(getResources().getString(R.string.qn_natural_complex_2_1)+" "+revenueProductLoad.getName()+" "+getResources().getString(R.string.qn_natural_complex_2_2));
            productQuestion.setText(getResources().getString(R.string.qn_natural_complex_3_1)+" "+revenueProductLoad.getName()+" "+getResources().getString(R.string.qn_natural_complex_3_2));
            areaQuestion.setText(getResources().getString(R.string.percentage_area_harvested));
        }
        productReveneIdCheck = revenueProductLoad.getId();






        //yearList = revenueProductLoad.getRevenueProductYearses();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        yearList.clear();
        totalYearsCount = 0;
        for(RevenueProductYears revenueProductYears:revenueProductLoad.getRevenueProductYearses()){
            if(revenueProductYears.getYear() < currentYear && revenueProductYears.getYear() != 0){
                yearList.add(revenueProductYears.getYear()+"");
                year_adapter.notifyDataSetChanged();

                // Log.e("REV PROD ID ",revenueProductYears.getRevenueProductId()+"");
                totalYearsCount++;
            }
        }

        //totalYearsCount = revenueProductLoad.getRevenueProductYearses().size();
//        Log.e("Current year ",currentYearIndex+"");
//        Log.e("Total year ",totalYearsCount+"");
//        Log.e("Current cost ",currentCostProductIndex+"");
//        Log.e("Total cost ",totalCostProductCount+"");

        currentCostProductIndexSave = currentCostProductIndex;
        currentYearIndexSave = currentYearIndex;

        if(currentYearIndex < totalYearsCount) {
            loadRevenueYears(revenueProductLoad.getRevenueProductYearses().get(currentYearIndex), revenueProductLoad);
            if (currentYearIndex == totalYearsCount) {
                previousCostProductIndex = currentCostProductIndex;
                currentCostProductIndex++;
                currentYearIndex = 0;
            }
        }
//        Log.e("Current year ",currentYearIndex+"");
//        Log.e("Total year ",totalYearsCount+"");
//        Log.e("Current cost ",currentCostProductIndex+"");
//        Log.e("Total cost ",totalCostProductCount+"");
    }

    public void loadRevenueYears(RevenueProductYears revenueProductYearsLoad, RevenueProduct revenueProductLoad1){
        // Log.e("LOAD ","11111");
        if(revenueProductYearsLoad.getHarvestFrequencyValue() != 0){
            noOfTimesEdit.setText(revenueProductYearsLoad.getHarvestFrequencyValue()+"");
        }else{
            noOfTimesEdit.setText("1");
        }
        if(revenueProductYearsLoad.getQuantityValue() != 0){
            // Log.e("KKKKKKKKKKKKK ",revenueProductYearsLoad.getQuantityValue()+"");
            quanityEdit.setText(revenueProductYearsLoad.getQuantityValue()+"");
        }else{
            quanityEdit.setText("");
        }
        if(revenueProductYearsLoad.getMarketPriceValue() != 0){
            priceEdit.setText(revenueProductYearsLoad.getMarketPriceValue()+"");
        }else{
            priceEdit.setText("");
        }

        if(revenueProductYearsLoad.getHarvestArea() != 0){
            areaEdit.setText(revenueProductYearsLoad.getHarvestArea()+"");
        }else{
            areaEdit.setText("");
        }



        spinnerYear.setSelection(currentYearIndex);

        Frequency frequency = realm.where(Frequency.class)
                .equalTo("frequencyValue",(int) revenueProductYearsLoad.getHarvestFrequencyUnit())
                .findFirst();

        if(timePeriodList.size() != 0 && frequency != null){
            // Log.e("TEST FRE ", timePeriod_adapter.getPosition(frequency.getHarvestFrequency())+"");
            if(language.equals("हिन्दी")) {
                spinnerTimePeriod.setSelection(timePeriod_adapter.getPosition(frequency.getHarvestFrequencyHindi()));
            }
            else{
                spinnerTimePeriod.setSelection(timePeriod_adapter.getPosition(frequency.getHarvestFrequency()));
            }




            //spinnerTimePeriod.setSelection(timePeriodList.indexOf(frequency.getHarvestFrequency()));
        }


        Quantity quantity = realm.where(Quantity.class)
                .equalTo("quantityName",revenueProductYearsLoad.getQuantityUnit())
                .findFirst();

        if(unitList.size() != 0 && quantity != null){
            // Log.e("QUANTITY ", unit_adapter.getPosition(quantity.getQuantityName())+"");
            spinnerUnit.setSelection(unit_adapter.getPosition(quantity.getQuantityName()));
        }

        previousYearIndex = currentYearIndex;
        currentYearIndex++;
    }

    public void saveYearlyDatas(final RevenueProduct revenueProduct2){
        final long revenueProductId = revenueProduct2.getId();
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle(getResources().getString(R.string.loading));
        progress.setMessage(getResources().getString(R.string.wait_while_loading));
        progress.show();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RevenueProduct revenueProduct2 = realm.where(RevenueProduct.class)
                        .equalTo("id",revenueProductId)
                        .findFirst();



                // Log.e("REVE PRO ",revenueProduct2.toString()+" YEAR "+spinnerYear.getSelectedItem());
                for(RevenueProductYears revenueProductYears1:revenueProduct2.getRevenueProductYearses()){
                    if(String.valueOf(revenueProductYears1.getYear()).equals(spinnerYear.getSelectedItem()) ){
                        // Log.e("REVE YEAR ",revenueProductYears1.toString());
                        String spinnerTimePeriodStr = spinnerTimePeriod.getSelectedItem().toString();
                        Frequency frequency;
                        if(language.equals("हिन्दी")) {
                            frequency = realm.where(Frequency.class)
                                    .equalTo("harvestFrequencyHindi",spinnerTimePeriodStr)
                                    .findFirst();
                        }
                        else{
                            frequency = realm.where(Frequency.class)
                                    .equalTo("harvestFrequency",spinnerTimePeriodStr)
                                    .findFirst();
                        }


                        int yearCurent = Calendar.getInstance().get(Calendar.YEAR);
                        int yearIndex = revenueProductYears1.getYear() - yearCurent;

                        runOnUiThread(new Runnable() {
                            public void run(){
//                                if(noOfTimesEdit.getText().toString().equals("")){
//                                    noOfTimesEdit.setText("0");
//                                }
//                                if(quanityEdit.getText().toString().equals("")){
//                                    quanityEdit.setText("0");
//                                }
//                                if(priceEdit.getText().toString().equals("")){
//                                    priceEdit.setText("0");
//                                }
//                                if(areaEdit.getText().toString().equals("")){
//                                    areaEdit.setText("0");
//                                }
                            }
                        });

                        double total = 0;



//                        double total = frequency.getFrequencyValue()
//                                * Integer.parseInt(noOfTimesEdit.getText().toString())
//                                * Double.parseDouble(priceEdit.getText().toString())
//                                * Double.parseDouble(quanityEdit.getText().toString());

                        String areaEditStr = areaEdit.getText().toString();
                        double harverArea = 0;
                        if(areaEditStr.equals("")){
                            harverArea = 0;
                        }else{
                            harverArea = Double.parseDouble(areaEditStr);
                        }

                        String noOfTimesEditStr = noOfTimesEdit.getText().toString();
                        String priceEditStr = priceEdit.getText().toString();
                        String quanityEditStr = quanityEdit.getText().toString();

                        if(noOfTimesEditStr.equals("")){
                            noOfTimesEditStr = "0";
                        }
                        if(priceEditStr.equals("")){
                            priceEditStr = "0";
                        }
                        if(quanityEditStr.equals("")){
                            quanityEditStr = "0";
                        }

                        BigDecimal bigDecimalNoOfTimes = new BigDecimal(noOfTimesEditStr);
                        BigDecimal bigDecimalPrice = new BigDecimal(priceEditStr);
                        BigDecimal bigDecimalQuanityEditStr = new BigDecimal(quanityEditStr);
                        BigDecimal bigDecimalHarverArea = new BigDecimal(harverArea);


//                        Log.e("LAND ", currentSocialCapitalServey);

                        if(currentSocialCapitalServey.equals("Pastureland")){
//                            Log.e("LAND ", "PAS");
//                            total = Integer.parseInt(noOfTimesEditStr)
//                                    * Double.parseDouble(priceEditStr)
//                                    * Double.parseDouble(quanityEditStr)
//                                    * harverArea;

                            BigDecimal bigDecimalTotal = bigDecimalNoOfTimes.multiply(bigDecimalPrice, MathContext.DECIMAL64)
                                    .multiply(bigDecimalQuanityEditStr, MathContext.DECIMAL64)
                                    .multiply(bigDecimalHarverArea, MathContext.DECIMAL64);

//                            Log.e("AA ",Integer.parseInt(noOfTimesEditStr)
//                                    +" "+ Double.parseDouble(priceEditStr)
//                                    +" "+ Double.parseDouble(quanityEditStr)
//                                    +" "+ harverArea);

                            total = bigDecimalTotal.divide(new BigDecimal("12"),MathContext.DECIMAL64).doubleValue();

                            // total = total/12;

                        }else {
                            BigDecimal bigDecimalFrequency = new BigDecimal(frequency.getFrequencyValue());

                            BigDecimal bigDecimalTotal = bigDecimalFrequency.multiply(bigDecimalNoOfTimes, MathContext.DECIMAL64)
                                    .multiply(bigDecimalPrice, MathContext.DECIMAL64)
                                    .multiply(bigDecimalQuanityEditStr, MathContext.DECIMAL64);

                            total = bigDecimalTotal.doubleValue();

//                            total = frequency.getFrequencyValue()
//                                    * Integer.parseInt(noOfTimesEditStr)
//                                    * Double.parseDouble(priceEditStr)
//                                    * Double.parseDouble(quanityEditStr);
                        }

                        //total = roundToTwoDecimal(total);

//                        Log.e("TOTAL ",total+"");



                        //realm.beginTransaction();
                        revenueProductYears1.setHarvestFrequencyValue(Integer.parseInt(noOfTimesEditStr));
                        revenueProductYears1.setHarvestFrequencyUnit(frequency.getFrequencyValue());
                        revenueProductYears1.setQuantityValue(Double.parseDouble(quanityEditStr));
                        revenueProductYears1.setQuantityUnit(spinnerUnit.getSelectedItem().toString());
                        revenueProductYears1.setMarketPriceValue(Double.parseDouble(priceEditStr));
                        revenueProductYears1.setMarketPriceCurrency(spinnerCurrency.getSelectedItem().toString());
                        revenueProductYears1.setProjectedIndex(yearIndex);
                        revenueProductYears1.setSubtotal(total);
                        revenueProductYears1.setHarvestArea(harverArea);
                        //revenueProductYears1.setHarvestArea();
                        //realm.commitTransaction();

                        // Log.e("RE CHECK ",revenueProductYears1.toString());
                    }
                }


                for(RevenueProductYears revenueProductYears11:revenueProduct2.getRevenueProductYearses()){
                    // Log.e("ALL YEARS DATA",revenueProductYears11.toString());
                }



            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                progress.dismiss();
                calculateTrend(revenueProduct2.getId());


                // Log.e("REALM", "All done updating.");
                // Log.d("BG", t.getName());
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // transaction is automatically rolled-back, do any cleanup here
                Log.e("REALM", " ERROR ."+error.toString());
            }
        });













    }

    public void calculateTrend(final long revenueProductIdLong){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RevenueProduct revenueProduct4 = realm.where(RevenueProduct.class)
                        .equalTo("id",revenueProductIdLong)
                        .findFirst();
                double harvestFre = 0;
                double harvestTimes = 0;
                double harvestPrice = 0;
                double harvestArea = 0;

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
                                harvestArea = harvestArea + revenueProductYears.getHarvestArea();
                            }
                        }
                    }else{
                        int eleCount = 0;
                        for(RevenueProductYears revenueProductYears:revenueProduct4.getRevenueProductYearses()){
                            if(revenueProductYears.getProjectedIndex() < 0){
                                harvestFre = revenueProductYears.getHarvestFrequencyValue();
                                harvestTimes = harvestTimes + revenueProductYears.getQuantityValue();
                                harvestPrice = harvestPrice + revenueProductYears.getMarketPriceValue();
                                harvestArea = harvestArea + revenueProductYears.getHarvestArea();

                                freqUnit = revenueProductYears.getHarvestFrequencyUnit();
                                quaUnit = revenueProductYears.getQuantityUnit();
                                priceCurrency = revenueProductYears.getMarketPriceCurrency();

                                eleCount++;
                            }
                        }

                        harvestTimes = harvestTimes/eleCount;
                        harvestPrice = harvestPrice/eleCount;
                        harvestArea = harvestArea/eleCount;
                    }
                }

                for(RevenueProductYears revenueProductYears:revenueProduct4.getRevenueProductYearses()){
                    if(revenueProductYears.getYear() == 0){
                        //realm.beginTransaction();
                        revenueProductYears.setHarvestFrequencyValue((int) harvestFre);
                        revenueProductYears.setHarvestFrequencyUnit(freqUnit);
                        revenueProductYears.setQuantityValue(harvestTimes);
                        revenueProductYears.setQuantityUnit(quaUnit);
                        revenueProductYears.setMarketPriceValue(harvestPrice);
                        revenueProductYears.setMarketPriceCurrency(priceCurrency);
                        revenueProductYears.setProjectedIndex(0);
                        revenueProductYears.setSubtotal(0);
                        if(currentSocialCapitalServey.equals("Pastureland")){
                            revenueProductYears.setHarvestArea(harvestArea);
                        }
                        //realm.commitTransaction();
//                        Log.e("REV ",revenueProductYears.toString());
                    }
                    if(revenueProductYears.getProjectedIndex() > 0){
                        BigDecimal bigDecimalPowerFactor = new BigDecimal(Math.pow((1 + inflationRate), revenueProductYears.getProjectedIndex()), MathContext.DECIMAL64);
                        BigDecimal bigDecimalHarvestPrice = new BigDecimal(harvestPrice);

                        double marketPriceVal = bigDecimalPowerFactor.multiply(bigDecimalHarvestPrice, MathContext.DECIMAL64).doubleValue();

                        // double marketPriceVal = harvestPrice * Math.pow((1 + inflationRate), revenueProductYears.getProjectedIndex());
                        //marketPriceVal = roundToTwoDecimal(marketPriceVal);

                        BigDecimal bigDecimalfreqUnit = new BigDecimal(freqUnit);
                        BigDecimal bigDecimalharvestFre = new BigDecimal(harvestFre);
                        BigDecimal bigDecimalharvestTimes = new BigDecimal(harvestTimes);
                        BigDecimal bigDecimalmarketPriceVal = new BigDecimal(marketPriceVal);
                        BigDecimal bigDecimalHarvestArea = new BigDecimal(harvestArea);

                        double totalVal = bigDecimalfreqUnit.multiply(bigDecimalharvestFre, MathContext.DECIMAL64)
                                .multiply(bigDecimalharvestTimes, MathContext.DECIMAL64)
                                .multiply(bigDecimalmarketPriceVal, MathContext.DECIMAL64).doubleValue();

                        if(currentSocialCapitalServey.equals("Pastureland")){
                            BigDecimal bigDecimal12 = new BigDecimal("12");
                            BigDecimal bigDecimalNewHarvestArea = bigDecimalharvestFre.divide(bigDecimal12, MathContext.DECIMAL64);
                            totalVal = bigDecimalNewHarvestArea.multiply(bigDecimalharvestTimes, MathContext.DECIMAL64)
                                    .multiply(bigDecimalmarketPriceVal, MathContext.DECIMAL64)
                                    .multiply(bigDecimalHarvestArea, MathContext.DECIMAL64)
                                    .doubleValue();
                        }

//                        double totalVal = freqUnit
//                                * harvestFre
//                                * harvestTimes
//                                * marketPriceVal;
                        //totalVal = roundToTwoDecimal(totalVal);

                        //realm.beginTransaction();
                        revenueProductYears.setHarvestFrequencyValue((int) harvestFre);
                        revenueProductYears.setHarvestFrequencyUnit(freqUnit);
                        revenueProductYears.setQuantityValue(harvestTimes);
                        revenueProductYears.setQuantityUnit(quaUnit);
                        revenueProductYears.setMarketPriceValue(marketPriceVal);
                        revenueProductYears.setMarketPriceCurrency(priceCurrency);
                        revenueProductYears.setSubtotal(totalVal);
                        if(currentSocialCapitalServey.equals("Pastureland")) {
                            revenueProductYears.setHarvestArea(harvestArea);
                        }
                        //realm.commitTransaction();

//                        Log.e("REV ",revenueProductYears.toString());
                    }
                }





            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if(currentCostProductIndex < totalCostProductCount){
                    loadRevenueProduct(revenueProducts.get(currentCostProductIndex));
                    //currentCostProductIndex++;
                    buttonNext.setClickable(true);
                }else{
                    Toast.makeText(context,getResources().getText(R.string.completed_text),Toast.LENGTH_SHORT).show();
                    allCashFlow();
                    calculateNPV();

                    Intent intent=new Intent(getApplicationContext(),NaturalCapitalCostActivityA.class);
                    startActivity(intent);
                }

                // Log.e("REALM", "All done updating.");
                // Log.d("BG", t.getName());
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // transaction is automatically rolled-back, do any cleanup here
                // Log.e("REALM", "All done updating."+error.toString());
            }
        });










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

    @Override
    protected void onResume() {
        super.onResume();
        buttonNext.setClickable(true);
    }

    public void allCashFlow(){
        Survey results = realm.where(Survey.class)
                .equalTo("surveyId",serveyId)
                .findFirst();
        RealmList<CashFlow> cashFlows = new RealmList<>();
        for(LandKind landKind:results.getLandKinds()){

            if(landKind.getName().equals("Forestland") && currentSocialCapitalServey.equals("Forestland")){
                // Log.e("DIS RATE ", landKind.getSocialCapitals().getDiscountRate()+"");
                int k = 0;
                for(RevenueProduct revenueProduct:landKind.getForestLand().getRevenueProducts()){

                    for (RevenueProductYears revenueProductYears00 : revenueProduct.getRevenueProductYearses()) {
                        Log.e("RPPPPPPP ", revenueProductYears00+"");
                    }
                    if(k <= 0) {
                        for (RevenueProductYears revenueProductYears : revenueProduct.getRevenueProductYearses()) {
                            if(!landKind.getSocialCapitals().isDiscountFlag()){
                                cashFlows.add(calculateCashFlow("Forestland",revenueProductYears.getYear(),landKind.getSocialCapitals().getDiscountRate()));
                            }else{
                                cashFlows.add(calculateCashFlow("Forestland",revenueProductYears.getYear(),landKind.getSocialCapitals().getDiscountRateOverride()));
                            }
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
                            if(!landKind.getSocialCapitals().isDiscountFlag()){
                                cashFlows.add(calculateCashFlow("Cropland",revenueProductYears.getYear(),landKind.getSocialCapitals().getDiscountRate()));
                            }else {
                                cashFlows.add(calculateCashFlow("Cropland",revenueProductYears.getYear(),landKind.getSocialCapitals().getDiscountRateOverride()));
                            }
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
                            if(!landKind.getSocialCapitals().isDiscountFlag()){
                                cashFlows.add(calculateCashFlow("Pastureland",revenueProductYears.getYear(),landKind.getSocialCapitals().getDiscountRate()));
                            }else {
                                cashFlows.add(calculateCashFlow("Pastureland",revenueProductYears.getYear(),landKind.getSocialCapitals().getDiscountRateOverride()));
                            }
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
                            if(!landKind.getSocialCapitals().isDiscountFlag()){
                                cashFlows.add(calculateCashFlow("Mining Land",revenueProductYears.getYear(),landKind.getSocialCapitals().getDiscountRate()));
                            }else{
                                cashFlows.add(calculateCashFlow("Mining Land",revenueProductYears.getYear(),landKind.getSocialCapitals().getDiscountRateOverride()));
                            }
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

        OutlayYears outlayYears = realm.where(OutlayYears.class)
                .equalTo("surveyId",serveyId)
                .equalTo("landKind",landKind)
                .equalTo("year",year)
                .findFirst();

        RealmResults<RevenueProductYears> revenueProductYearses = realm.where(RevenueProductYears.class)
                .equalTo("surveyId",serveyId)
                .equalTo("landKind",landKind)
                .equalTo("year",year)
                .findAll();

        RealmResults<CostElementYears> costElementYearses = realm.where(CostElementYears.class)
                .equalTo("surveyId",serveyId)
                .equalTo("landKind",landKind)
                .equalTo("year",year)
                .findAll();

        RealmResults<OutlayYears> outlayYearses = realm.where(OutlayYears.class)
                .equalTo("surveyId",serveyId)
                .equalTo("landKind",landKind)
                .equalTo("year",year)
                .findAll();


        double revenueTotal = 0;
        double costTotal = 0;
        double outlayTotal = 0;
        double disRate = disRatePersent/100;

        double disFactor = 0;

        BigDecimal bigDecimalOne = new BigDecimal("1");

        for(RevenueProductYears revenueProductYears1:revenueProductYearses){
            revenueTotal = revenueTotal + revenueProductYears1.getSubtotal();

            double powerFactor = Math.pow(1+disRate,revenueProductYears.getProjectedIndex());
            BigDecimal bigDecimalPowerFactor = new BigDecimal(powerFactor);
            BigDecimal bigDecimalDisFactor = bigDecimalOne.divide(bigDecimalPowerFactor, MathContext.DECIMAL64);

            disFactor = bigDecimalDisFactor.doubleValue();

            // disFactor = 1 / Math.pow(1+disRate,revenueProductYears.getProjectedIndex());
//            Log.e("PRO IN  ",disRate+" "+revenueProductYears.getProjectedIndex()+"");
        }

        for(CostElementYears costElementYears1:costElementYearses){
            costTotal = costTotal + costElementYears1.getSubtotal();

            double powerFactor = Math.pow(1+disRate,costElementYears.getProjectedIndex());
            BigDecimal bigDecimalPowerFactor = new BigDecimal(powerFactor);
            BigDecimal bigDecimalDisFactor = bigDecimalOne.divide(bigDecimalPowerFactor, MathContext.DECIMAL64);

            disFactor = bigDecimalDisFactor.doubleValue();
            // disFactor = 1 / Math.pow(1+disRate,costElementYears.getProjectedIndex());
//            Log.e("PRO IN  ",disRate+" "+costElementYears.getProjectedIndex()+"");
        }

        for(OutlayYears outlayYears1:outlayYearses){
            outlayTotal = outlayTotal + outlayYears1.getPrice();
        }
//        Log.e("DIS FACT ",disFactor+"");

//        if(revenueProductYears != null){
//            revenueTotal = revenueProductYears.getSubtotal();
//            disFactor = 1 / Math.pow(1+disRate,revenueProductYears.getProjectedIndex());
//        }
//        if(costElementYears != null){
//            costTotal = costElementYears.getSubtotal();
//            disFactor = 1 / Math.pow(1+disRate,costElementYears.getProjectedIndex());
//        }
//        if(outlayYears != null){
//            outlayTotal = outlayYears.getPrice();
//        }
        double cashFlowVal = revenueTotal - costTotal - outlayTotal;

        //cashFlowVal = roundToTwoDecimal(cashFlowVal);
        Log.e("CASH FLOW CAL ", year+" "+cashFlowVal+" "+revenueTotal +" "+ costTotal +" "+ outlayTotal);

        double discountedCashFlow = cashFlowVal * disFactor;
        ///discountedCashFlow = roundToTwoDecimal(discountedCashFlow);

        // Log.e("DIS FACT ",disFactor+"");

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
                    Log.e("Cash flow ", cashFlow.toString());
                    if(cashFlow.getYear() >= year){
                        npv = npv + cashFlow.getDiscountedCashFlow();
                    }
                }

                //npv = roundToTwoDecimal(npv);

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

                //npv = roundToTwoDecimal(npv);

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

                //npv = roundToTwoDecimal(npv);

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

                //npv = roundToTwoDecimal(npv);

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

    public double roundToTwoDecimal(double val){
        val = val*100;
        val = Math.round(val);
        val = val /100;
        return val;
    }
}
