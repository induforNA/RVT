package com.sayone.omidyar.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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
import com.sayone.omidyar.model.SharedCostElement;
import com.sayone.omidyar.model.SharedCostElementYears;
import com.sayone.omidyar.model.Survey;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class NaturalCapitalSharedCostActivityC extends BaseActivity implements View.OnClickListener {

    private Realm realm;
    private SharedPreferences sharedPref;
    private String surveyId;
    private Context context;
    private String currentSocialCapitalSurvey;

    private Spinner spinnerYear, spinnerUnit, spinnerTimePeriod;
    private String year, unit, timePeriod;
    private Button buttonBack, buttonNext;
    private TextView loadQuestions;
    private Button saveBtn;
    private RealmList<SharedCostElement> revenueProducts;
    private int totalCostProductCount = 0;
    private int currentCostProductIndex = 0;
    private int totalYearsCount = 0;
    private int currentYearIndex = 0;
    private int currentCostProductIndexSave = 0;
    private int currentYearIndexSave = 0;
    private int previousYearIndex = 0;
    private int previousCostProductIndex = 0;
    private ArrayAdapter<String> timePeriod_adapter;
    private ArrayList<String> timePeriodList;
    private ArrayAdapter<String> year_adapter;
    private ArrayList<String> yearList;
    private ArrayAdapter<String> unit_adapter;
    private ArrayList<String> unitList;
    private TextView quantityQuestion;
    private TextView productQuestion;
    private EditText noOfTimesEdit;
    private EditText quanityEdit;
    private EditText priceEdit;
    private double inflationRate = 0.05;
    private long productReveneIdCon;
    private long productReveneIdCheck = 0;
    private String currentProductName;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private DrawerLayout menuDrawerLayout;
    private TextView surveyIdDrawer;
    private TextView areaQuestion;
    private TextView landType;
    private EditText areaEdit;
    private String language;
    private TextView householdText;
    private EditText householdEdit;
    private LinearLayout householdContainer;
    private double mSharedDiscountRate;
    private Survey results;
    private int lastYearIndex = 0;
    private boolean nextProduct = false;

    double mHarvestFre = 0;
    double mHarvestTimes = 0;
    double mHarvestPrice = 0;
    double mHarvestArea = 0;
    double mHousehold = 0;
    public double mMarketPriceValue;

    double mFreqUnit = 0;
    String mQuaUnit = "";
    String mPriceCurrency = "";

    //Side Nav
    private TextView textViewAbout;
    private TextView startSurvey;
    private TextView harvestingForestProducts;
    private TextView agriculture;
    private TextView grazing;
    private TextView mining;
    private TextView sharedCostsOutlays;
    private TextView certificate;
    private TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_cost_survey_c);

        context = this;
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        surveyId = sharedPref.getString("surveyId", "");
        currentSocialCapitalSurvey = sharedPref.getString("currentSocialCapitalSurvey", "");

        spinnerYear = (Spinner) findViewById(R.id.spinner_year);
        spinnerUnit = (Spinner) findViewById(R.id.spinner_unit);
        spinnerTimePeriod = (Spinner) findViewById(R.id.spinner_time_period);
        buttonBack = (Button) findViewById(R.id.button_back);
        buttonNext = (Button) findViewById(R.id.button_next);
        loadQuestions = (TextView) findViewById(R.id.load_questions);
        householdText = (TextView) findViewById(R.id.household_text);
        householdEdit = (EditText) findViewById(R.id.household_edit);
        householdContainer = (LinearLayout) findViewById(R.id.household_layout);
        quantityQuestion = (TextView) findViewById(R.id.quantity_question);
        productQuestion = (TextView) findViewById(R.id.product_question);
        noOfTimesEdit = (EditText) findViewById(R.id.no_of_times_edit);
        quanityEdit = (EditText) findViewById(R.id.quanity_edit);
        priceEdit = (EditText) findViewById(R.id.price_edit);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        surveyIdDrawer = (TextView) findViewById(R.id.text_view_id);
        landType = (TextView) findViewById(R.id.land_type);

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

        inflationRate = 0.05;

        yearList = new ArrayList<>();
        timePeriodList = new ArrayList<>();
        unitList = new ArrayList<>();

        year_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, yearList);
        timePeriod_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timePeriodList);
        unit_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, unitList);

        language = Locale.getDefault().getDisplayLanguage();
        results = realm.where(Survey.class)
                .equalTo("surveyId", surveyId)
                .findFirst();
        if (!results.getInflationRate().equals("")) {
            inflationRate = Double.parseDouble(results.getInflationRate());
        }

        RealmResults<Frequency> frequencyResult = realm.where(Frequency.class).findAll();
        for (Frequency frequency : frequencyResult) {
            if (language.equals("हिन्दी")) {
                timePeriodList.add(frequency.getHarvestFrequencyHindi());
            } else {
                timePeriodList.add(frequency.getHarvestFrequency());
            }
        }

        RealmResults<Quantity> quantityResult = realm.where(Quantity.class).findAll();
        for (Quantity quantity : quantityResult) {
            if (language.equals("हिन्दी")) {
                unitList.add(quantity.getQuantityNameHindi());
            } else {
                unitList.add(quantity.getQuantityName());
            }
        }

        spinnerYear.setAdapter(year_adapter);
        spinnerUnit.setAdapter(unit_adapter);
        spinnerTimePeriod.setAdapter(timePeriod_adapter);

        if (currentSocialCapitalSurvey.equals(getString(R.string.string_forestland)))
            landType.setText(getResources().getText(R.string.string_forestland));
        if (currentSocialCapitalSurvey.equals(getString(R.string.string_pastureland)))
            landType.setText(getResources().getText(R.string.string_pastureland));
        if (currentSocialCapitalSurvey.equals(getString(R.string.string_miningland)))
            landType.setText(getResources().getText(R.string.string_miningland));
        if (currentSocialCapitalSurvey.equals(getString(R.string.string_cropland)))
            landType.setText(getResources().getText(R.string.title_cropland));

        if (results.getSharedCostElements().size() > 0) {
            revenueProducts = results.getSharedCostElements();
            totalCostProductCount = revenueProducts.size();
            currentCostProductIndex = 0;
            currentYearIndex = 0;
            if (currentCostProductIndex < totalCostProductCount) {
                productReveneIdCon = results.getSharedCostElements().get(currentCostProductIndex).getId();
                loadRevenueProduct(results.getSharedCostElements().get(currentCostProductIndex));
            }
        }

        unit = spinnerUnit.getSelectedItem().toString();


        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        imageViewMenuIcon.setOnClickListener(this);
        drawerCloseBtn.setOnClickListener(this);
        surveyIdDrawer.setText(surveyId);


        calculateDiscountRate(results.getLandKinds());
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                year = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                unit = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        spinnerTimePeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                timePeriod = parent.getItemAtPosition(pos).toString();
                if (timePeriod.equals("one-time")) {
                    noOfTimesEdit.setText("1");
                    noOfTimesEdit.setEnabled(false);
                } else {
                    noOfTimesEdit.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        //Side Nav
        textViewAbout = (TextView) findViewById(R.id.text_view_about);
        startSurvey = (TextView) findViewById(R.id.text_start_survey);
        harvestingForestProducts = (TextView) findViewById(R.id.text_harvesting_forest_products);
        agriculture = (TextView) findViewById(R.id.text_agriculture);
        grazing = (TextView) findViewById(R.id.text_grazing);
        mining = (TextView) findViewById(R.id.text_mining);
        sharedCostsOutlays = (TextView) findViewById(R.id.text_shared_costs_outlays);
        certificate = (TextView) findViewById(R.id.text_certificate);
        logout = (TextView) findViewById(R.id.logout);
        textViewAbout.setOnClickListener(this);
        startSurvey.setOnClickListener(this);
        harvestingForestProducts.setOnClickListener(this);
        agriculture.setOnClickListener(this);
        grazing.setOnClickListener(this);
        mining.setOnClickListener(this);
        sharedCostsOutlays.setOnClickListener(this);
        certificate.setOnClickListener(this);
        logout.setOnClickListener(this);
        setNav();
    }

    private void calculateDiscountRate(RealmList<LandKind> landKinds) {
        double landDisc;
        double totalVal = 0;
        double totalValNum = 0;

        for (LandKind landKind : landKinds) {
            if (landKind.getStatus().equals("active")) {
                if (landKind.getSocialCapitals().isDiscountFlag()) {
                    landDisc = landKind.getSocialCapitals().getDiscountRateOverride();
                } else {
                    landDisc = landKind.getSocialCapitals().getDiscountRate();
                }

                if (landKind.getName().equals(getString(R.string.string_forestland))) {

                    totalValNum = totalValNum + (results.getComponents().getForestValue() * landDisc);
                    totalVal = totalVal + results.getComponents().getForestValue();
                }

                if (landKind.getName().equals(getString(R.string.string_cropland))) {
                    totalValNum = totalValNum + (results.getComponents().getCroplandValue() * landDisc);
                    totalVal = totalVal + results.getComponents().getCroplandValue();
                }

                if (landKind.getName().equals(getString(R.string.string_pastureland))) {
                    totalValNum = totalValNum + (results.getComponents().getPastureValue() * landDisc);
                    totalVal = totalVal + results.getComponents().getPastureValue();
                }

                if (landKind.getName().equals(getString(R.string.string_miningland))) {
                    totalValNum = totalValNum + (results.getComponents().getMiningLandValue() * landDisc);
                    totalVal = totalVal + results.getComponents().getMiningLandValue();
                }
            }
        }
        mSharedDiscountRate = totalValNum / totalVal;
        Log.e("SHARED DISCOUNT RATE", mSharedDiscountRate + "");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        backButtonAction();
    }

    public void backButtonAction() {
        int currentYearBackIndex = getIndexYears(spinnerYear.getSelectedItem().toString());
        int currentPrductNameBackIndex = getIndexRevenueProducts(currentProductName);
        if (currentYearBackIndex != -1 && currentPrductNameBackIndex != -1) {
            if (currentPrductNameBackIndex == 0) {
                if (currentYearBackIndex == 0) {
                    finish();
                } else {
                    currentYearIndex = currentYearBackIndex - 1;
                    currentCostProductIndex = currentPrductNameBackIndex;
                }
            } else {
                if (currentYearBackIndex == 0) {
                    currentCostProductIndex = currentPrductNameBackIndex - 1;
                    currentYearIndex = yearList.size() - 1;
                } else {
                    currentYearIndex = currentYearBackIndex - 1;
                    currentCostProductIndex = currentPrductNameBackIndex;
                }
            }
        }

        if (revenueProducts.size() == 1) {
            int yearCounting = 0;
            for (SharedCostElementYears costElementYearsA : revenueProducts.get(0).getCostElementYearses()) {
                // Log.e("YEAR ", costElementYearsA.getYear()+"");
                if (costElementYearsA.getYear() != 0 && costElementYearsA.getYear() < sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR))) {
                    yearCounting++;
                }
            }
            if (yearCounting == 1) {
                finish();
            } else {
                loadRevenueProduct(revenueProducts.get(currentCostProductIndexSave));
            }
        } else {
            loadRevenueProduct(revenueProducts.get(currentCostProductIndexSave));
        }


//        if(revenueProducts.size() == 1 && revenueProducts.get(currentCostProductIndex).getCostElementYearses().size() == 1){
//            finish();
//        }else {
//            loadRevenueProduct(revenueProducts.get(currentCostProductIndex));
//        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.button_next:
//                allCashFlow();
//                calculateNPV();
//
//                intent=new Intent(getApplicationContext(),NaturalCapitalCostActivityA.class);
//                startActivity(intent);
                if (buttonNext.isClickable()) {
                    buttonNext.setClickable(false);
                    saveYearlyDatas(revenueProducts.get(currentCostProductIndexSave));
                }
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


//                finish();
//                //Log.e("YEAR ","PRE "+previousYearIndex+" Cur "+currentYearIndex);
//                //Log.e("COST ","PRE "+previousCostProductIndex+" Cur "+currentCostProductIndex   );
//
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
//                        if(productRevenueIdCheck == 0 || productRevenueIdCheck == productRevenueIdCon){
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

                // loadRevenueProduct(revenueProducts.get(currentCostProductIndex));

                break;

            case R.id.image_view_menu_icon:
                toggleMenuDrawer();
                break;
            case R.id.drawer_close_btn:
                toggleMenuDrawer();
                break;
            case R.id.text_view_about:
                Intent i = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(i);
                break;
            case R.id.text_start_survey:
                Intent intentt = new Intent(getApplicationContext(), MainActivity.class);
                intentt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentt);
                break;
            case R.id.logout:
                Intent intents = new Intent(getApplicationContext(), RegistrationActivity.class);
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intents);
                break;

            case R.id.text_harvesting_forest_products:
                setCurrentSocialCapitalSurvey(getString(R.string.string_forestland));
                startLandTypeActivity();
                break;
            case R.id.text_agriculture:
                setCurrentSocialCapitalSurvey(getString(R.string.string_cropland));
                startLandTypeActivity();
                break;
            case R.id.text_grazing:
                setCurrentSocialCapitalSurvey(getString(R.string.string_pastureland));
                startLandTypeActivity();
                break;
            case R.id.text_mining:
                setCurrentSocialCapitalSurvey(getString(R.string.string_miningland));
                startLandTypeActivity();
                break;
            case R.id.text_shared_costs_outlays:
                Intent intent_outlay = new Intent(getApplicationContext(), NaturalCapitalSharedCostActivityA.class);
                startActivity(intent_outlay);
                break;
            case R.id.text_certificate:
                Intent intent_certificate = new Intent(getApplicationContext(), NewCertificateActivity.class);
                startActivity(intent_certificate);
                break;


//            case R.id.save_btn:
//                // saveYearlyDatas(costOutlays.get(currentCostProductIndexSave));
//
//
//                break;

        }
    }

    private void setNav() {
        harvestingForestProducts.setVisibility(View.GONE);
        agriculture.setVisibility(View.GONE);
        grazing.setVisibility(View.GONE);
        mining.setVisibility(View.GONE);

        if (checkLandKind(getString(R.string.string_forestland))) {
            harvestingForestProducts.setVisibility(View.VISIBLE);
        }

        if (checkLandKind(getString(R.string.string_cropland))) {
            agriculture.setVisibility(View.VISIBLE);
        }

        if (checkLandKind(getString(R.string.string_pastureland))) {
            grazing.setVisibility(View.VISIBLE);
        }

        if (checkLandKind(getString(R.string.string_miningland))) {
            mining.setVisibility(View.VISIBLE);
        }
    }

    private void startLandTypeActivity() {
        Intent intent = new Intent(NaturalCapitalSharedCostActivityC.this, StartLandTypeActivity.class);
        startActivity(intent);
    }

    private void setCurrentSocialCapitalSurvey(String name) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("currentSocialCapitalSurvey", name);
        editor.apply();
    }

    private boolean checkLandKind(String name) {
        LandKind landKind = realm.where(LandKind.class)
                .equalTo("name", name)
                .equalTo("surveyId", surveyId)
                .findFirst();
        if (landKind.getStatus().equals("active")) {
            return true;
        } else {
            return false;
        }
    }

    public int getIndexRevenueProducts(String itemName) {
        for (int i = 0; i < revenueProducts.size(); i++) {
            SharedCostElement costElementGet = revenueProducts.get(i);
            if (itemName.equals(costElementGet.getName())) {
                return i;
            }
        }
        return -1;
    }

    public int getIndexYears(String itemName) {
        for (int i = 0; i < yearList.size(); i++) {
            String yearGet = yearList.get(i);
            if (itemName.equals(yearGet)) {
                return i;
            }
        }
        return -1;
    }

    public void loadRevenueProduct(SharedCostElement costElementLoad) {
        // Log.e("LOAD ","REVENUE 11111");

        currentProductName = costElementLoad.getName();

        if (costElementLoad.getType().equals("Timber")) {
            loadQuestions.setText(getResources().getString(R.string.qn_natural_cost_1_1) + " " + costElementLoad.getName() + "" + getResources().getString(R.string.qn_natural_cost_1_2) + "?");
            quantityQuestion.setText(getResources().getString(R.string.qn_natural_cost_2_1) + " " + costElementLoad.getName() + " " + getResources().getString(R.string.qn_natural_cost_2_2) + "?");
            productQuestion.setText(getResources().getString(R.string.qn_natural_cost_3_1) + " " + costElementLoad.getName() + " " + getResources().getString(R.string.qn_natural_cost_3_2));
        } else if (costElementLoad.getType().equals("Non Timber")) {
            loadQuestions.setText(getResources().getString(R.string.qn_natural_cost_1_1) + " " + costElementLoad.getName() + "" + getResources().getString(R.string.qn_natural_cost_1_2) + "?");
            quantityQuestion.setText(getResources().getString(R.string.qn_natural_cost_2_1) + " " + costElementLoad.getName() + " " + getResources().getString(R.string.qn_natural_cost_2_2) + "?");
            productQuestion.setText(getResources().getString(R.string.qn_natural_cost_3_1) + " " + costElementLoad.getName() + " " + getResources().getString(R.string.qn_natural_cost_3_2));
        } else {
            loadQuestions.setText(getResources().getString(R.string.qn_natural_cost_1_1) + " " + costElementLoad.getName() + "" + getResources().getString(R.string.qn_natural_cost_1_2) + "?");
            quantityQuestion.setText(getResources().getString(R.string.qn_natural_cost_2_1) + " " + costElementLoad.getName() + " " + getResources().getString(R.string.qn_natural_cost_2_2) + "?");
            productQuestion.setText(getResources().getString(R.string.qn_natural_cost_3_1) + " " + costElementLoad.getName() + " " + getResources().getString(R.string.qn_natural_cost_3_2));
        }
        householdText.setText(getString(R.string.string_household,getString(R.string.text_incur_cost)+costElementLoad.getName()));
        productReveneIdCheck = costElementLoad.getId();


        //yearList = revenueProductLoad.getRevenueProductYearses();
        // int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentYear = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
        yearList.clear();
        totalYearsCount = 0;
        for (SharedCostElementYears costElementYears : costElementLoad.getCostElementYearses()) {
            if (costElementYears.getYear() < currentYear && costElementYears.getYear() != 0) {
                yearList.add(costElementYears.getYear() + "");
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

        if (currentYearIndex < totalYearsCount) {
            loadRevenueYears(costElementLoad.getCostElementYearses().get(currentYearIndex), costElementLoad);
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

    public void loadRevenueYears(SharedCostElementYears costElementYearsLoad, SharedCostElement costElementLoad1) {
        // Log.e("LOAD ","11111");
        if (costElementYearsLoad.getCostFrequencyValue() != 0) {
            noOfTimesEdit.setText(costElementYearsLoad.getCostFrequencyValue() + "");
        }
        if (costElementYearsLoad.getCostPerPeriodValue() != 0) {
            // Log.e("KKKKKKKKKKKKK ",revenueProductYearsLoad.getQuantityValue()+"");
            quanityEdit.setText(costElementYearsLoad.getCostPerPeriodValue() + "");
        } else {
            quanityEdit.setText("");
        }
        if (costElementYearsLoad.getCostPerUnitValue() != 0) {
            priceEdit.setText(costElementYearsLoad.getCostPerUnitValue() + "");
        } else {
            priceEdit.setText("");
        }

        if (costElementYearsLoad.getHouseholds() != 0) {
            householdEdit.setText(costElementYearsLoad.getHouseholds() + "");
        } else {
            householdEdit.setText("");
        }

//        if(costElementYearsLoad.getHarvestArea() != 0){
//            areaEdit.setText(costElementYearsLoad.getHarvestArea()+"");
//        }else{
//            areaEdit.setText("0");
//        }


        spinnerYear.setSelection(currentYearIndex);

        Frequency frequency = realm.where(Frequency.class)
                .equalTo("frequencyValue", (int) costElementYearsLoad.getCostFrequencyUnit())
                .findFirst();

        if (timePeriodList.size() != 0 && frequency != null) {
            // Log.e("TEST FRE ", timePeriod_adapter.getPosition(frequency.getHarvestFrequency())+"");

            spinnerTimePeriod.setSelection(timePeriod_adapter.getPosition(frequency.getHarvestFrequency()));
            //spinnerTimePeriod.setSelection(timePeriodList.indexOf(frequency.getHarvestFrequency()));
        }


        Quantity quantity = realm.where(Quantity.class)
                .equalTo("quantityName", costElementYearsLoad.getCostPerPeriodUnit())
                .findFirst();

        if (unitList.size() != 0 && quantity != null) {
            // Log.e("QUANTITY ", unit_adapter.getPosition(quantity.getQuantityName())+"");
            spinnerUnit.setSelection(unit_adapter.getPosition(quantity.getQuantityName()));
        }

        previousYearIndex = currentYearIndex;
        currentYearIndex++;
    }

    public void saveYearlyDatas(final SharedCostElement costElement2) {
        final long costElementId = costElement2.getId();
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle(getResources().getString(R.string.loading));
        progress.setMessage(getResources().getString(R.string.wait_while_loading));
        progress.show();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SharedCostElement revenueProduct2 = realm.where(SharedCostElement.class)
                        .equalTo("id", costElementId)
                        .findFirst();


                // Log.e("REVE PRO ",revenueProduct2.toString()+" YEAR "+spinnerYear.getSelectedItem());
                for (SharedCostElementYears costElementYears1 : revenueProduct2.getCostElementYearses()) {
                    if (String.valueOf(costElementYears1.getYear()).equals(spinnerYear.getSelectedItem())) {
                        // Log.e("REVE YEAR ",revenueProductYears1.toString());
                        String spinnerTimePeriodStr = spinnerTimePeriod.getSelectedItem().toString();
                        Frequency frequency;

                        if (language.equals("हिन्दी")) {
                            frequency = realm.where(Frequency.class)
                                    .equalTo("harvestFrequencyHindi", spinnerTimePeriodStr)
                                    .findFirst();
                        } else {
                            frequency = realm.where(Frequency.class)
                                    .equalTo("harvestFrequency", spinnerTimePeriodStr)
                                    .findFirst();
                        }


                        // int yearCurent = Calendar.getInstance().get(Calendar.YEAR);
                        int yearCurent = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
                        int yearIndex = costElementYears1.getYear() - yearCurent;

                        runOnUiThread(new Runnable() {
                            public void run() {
//                                if(noOfTimesEdit.getText().toString().equals("")){
//                                    noOfTimesEdit.setText("0");
//                                }
//                                if(quantityEdit.getText().toString().equals("")){
//                                    quantityEdit.setText("0");
//                                }
//                                if(priceEdit.getText().toString().equals("")){
//                                    priceEdit.setText("0");
//                                }
//                                if(areaEdit.getText().toString().equals("")){
//                                    areaEdit.setText("0");
//                                }
                            }
                        });

                        String noOfTimesEditStr = noOfTimesEdit.getText().toString();
                        String priceEditStr = priceEdit.getText().toString();
                        String quanityEditStr = quanityEdit.getText().toString();
                        String householdEditStr = householdEdit.getText().toString();

                        if (noOfTimesEditStr.equals("")) {
                            noOfTimesEditStr = "0";
                        }
                        if (priceEditStr.equals("")) {
                            priceEditStr = "0";
                        }
                        if (quanityEditStr.equals("")) {
                            quanityEditStr = "0";
                        }
                        if (householdEditStr.equals("")) {
                            householdEditStr = "0";
                        }

                        BigDecimal bigDecimalFrequency;
                        if (frequency.getFrequencyValue() == 2) {
                            bigDecimalFrequency = new BigDecimal(1);
                        } else {
                            bigDecimalFrequency = new BigDecimal(frequency.getFrequencyValue());
                        }

                        BigDecimal bigDecimalNoOfTimes = new BigDecimal(noOfTimesEditStr);
                        BigDecimal bigDecimalPrice = new BigDecimal(priceEditStr);
                        BigDecimal bigDecimalQuanityEditStr = new BigDecimal(quanityEditStr);
                        BigDecimal bigDecimalHouseholdEditStr = new BigDecimal(householdEditStr);

                        BigDecimal bigDecimalTotal = bigDecimalFrequency.multiply(bigDecimalNoOfTimes, MathContext.DECIMAL64)
                                .multiply(bigDecimalPrice, MathContext.DECIMAL64)
                                .multiply(bigDecimalHouseholdEditStr, MathContext.DECIMAL64)
                                .multiply(bigDecimalQuanityEditStr, MathContext.DECIMAL64);


                        if (currentSocialCapitalSurvey.equals(getString(R.string.string_pastureland))) {
                            bigDecimalTotal = bigDecimalFrequency.multiply(bigDecimalNoOfTimes, MathContext.DECIMAL64)
                                    .multiply(bigDecimalPrice, MathContext.DECIMAL64)
                                    .multiply(bigDecimalQuanityEditStr, MathContext.DECIMAL64);
                        }

                        double total = bigDecimalTotal.doubleValue();


//                        double total = frequency.getFrequencyValue()
//                                * Integer.parseInt(noOfTimesEditStr)
//                                * Double.parseDouble(priceEditStr)
//                                * Double.parseDouble(quanityEditStr);

                        //String areaEditStr = areaEdit.getText().toString();
                        double harverArea = 0;
//                        if(areaEditStr.equals("")){
//                            harverArea = 0;
//                        }else{
//                            harverArea = Double.parseDouble(areaEditStr);
//                        }
                        //Log.e("TOTAL ",total+"");

                        //total = roundToTwoDecimal(total);


                        //realm.beginTransaction();
                        costElementYears1.setCostFrequencyValue(Double.parseDouble(noOfTimesEditStr));
                        //if(frequency.getFrequencyValue() == 2) {
                        //costElementYears1.setCostFrequencyUnit(1);
                        //}else{
                        costElementYears1.setCostFrequencyUnit(frequency.getFrequencyValue());
                        // }
                        costElementYears1.setCostPerPeriodValue(Double.parseDouble(quanityEditStr));
                        costElementYears1.setCostPerPeriodUnit(spinnerUnit.getSelectedItem().toString());
                        costElementYears1.setCostPerUnitValue(Double.parseDouble(priceEditStr));
                        costElementYears1.setHouseholds(Double.parseDouble(householdEditStr));
                        costElementYears1.setProjectedIndex(yearIndex);
                        costElementYears1.setSubtotal(total);
                        //costElementYears1.setHarvestArea(harverArea);


//                        costElementYears1.setCostFrequencyValue(Integer.parseInt(noOfTimesEdit.getText().toString()));
//                        costElementYears1.setCostFrequencyUnit(frequency.getFrequencyValue());
//                        costElementYears1.setCostPerPeriodValue(Double.parseDouble(quantityEdit.getText().toString()));
//                        costElementYears1.setCostPerPeriodUnit(spinnerUnit.getSelectedItem().toString());
//                        costElementYears1.setCostPerUnitValue(Double.parseDouble(priceEdit.getText().toString()));
//                        costElementYears1.setCostPerUnitUnit(spinnerCurrency.getSelectedItem().toString());
//                        costElementYears1.setProjectedIndex(yearIndex);
//                        costElementYears1.setSubtotal(total);


                        //revenueProductYears1.setHarvestArea();
                        //realm.commitTransaction();

                        // Log.e("RE CHECK ",revenueProductYears1.toString());
                    }
                }


                for (SharedCostElementYears costElementYears11 : revenueProduct2.getCostElementYearses()) {
                    // Log.e("ALL YEARS DATA",costElementYears11.toString());
                }


            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                progress.dismiss();
                calculateTrend(costElement2.getId());


                // Log.e("REALM", "All done updating.");
                // Log.d("BG", t.getName());
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // transaction is automatically rolled-back, do any cleanup here
                // Log.e("REALM", " ERROR ."+error.toString());
            }
        });

    }

    public void calculateTrend(final long revenueProductIdLong) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SharedCostElement costElement4 = realm.where(SharedCostElement.class)
                        .equalTo("id", revenueProductIdLong)
                        .findFirst();
                double harvestFre = 0;
                double harvestTimes = 0;
                double harvestPrice = 0;
                double household = 0;

                double freqUnit = 0;
                String quaUnit = "";
                String priceCurrency = "";

                double previousFrequencyUnit = 0;

                if(lastYearIndex++ < totalYearsCount) {
                    if (costElement4.getCostElementYearses().size() > 0) {
                        if (costElement4.getCostElementYearses().get(0).getCostFrequencyUnit() == 0) {
                            for (SharedCostElementYears costElementYears : costElement4.getCostElementYearses()){
                                if (costElementYears.getProjectedIndex() < 0) {
                                    if(previousFrequencyUnit == 0) {
                                        previousFrequencyUnit = costElementYears.getCostFrequencyUnit();
                                    }
                                    previousFrequencyUnit = (previousFrequencyUnit < costElementYears.getCostFrequencyUnit())
                                            ? previousFrequencyUnit : costElementYears.getCostFrequencyUnit();
                                }
                            }
                            for (SharedCostElementYears costElementYears : costElement4.getCostElementYearses()) {
                                if (costElementYears.getProjectedIndex() < 0) {
                                    harvestFre = 0;
                                    if (harvestTimes < costElementYears.getCostPerPeriodValue()) {
                                        harvestTimes = costElementYears.getCostPerPeriodValue();
                                    }
                                    if (harvestPrice < costElementYears.getCostPerUnitValue()) {
                                        harvestPrice = costElementYears.getCostPerUnitValue();
                                    }
                                    freqUnit = costElementYears.getCostFrequencyUnit();
                                    quaUnit = costElementYears.getCostPerPeriodUnit();
                                    priceCurrency = costElementYears.getCostPerUnitUnit();
                                    household = costElementYears.getHouseholds();
                                }
                                if( costElementYears.getCostFrequencyUnit() != previousFrequencyUnit){
                                    harvestFre = harvestFre * (freqUnit/previousFrequencyUnit);
                                    freqUnit = previousFrequencyUnit;
                                }
                            }
                        } else {
                            int eleCount = 0;
                            for (SharedCostElementYears costElementYears : costElement4.getCostElementYearses()) {
                                if (costElementYears.getProjectedIndex() < 0) {
                                    harvestFre = costElementYears.getCostFrequencyValue();
                                    harvestTimes = harvestTimes + costElementYears.getCostPerPeriodValue();
                                    harvestPrice = harvestPrice + costElementYears.getCostPerUnitValue();

                                    freqUnit = costElementYears.getCostFrequencyUnit();
                                    quaUnit = costElementYears.getCostPerPeriodUnit();
                                    priceCurrency = costElementYears.getCostPerUnitUnit();
                                    household += costElementYears.getHouseholds();

                                    eleCount++;
                                }
                            }

                            harvestTimes = harvestTimes / eleCount;
                            harvestPrice = harvestPrice / eleCount;
                            household = household / eleCount;

                        }
                    }

                    if (freqUnit == 1) {
                        harvestFre = 0;
                    }

                    for (SharedCostElementYears costElementYears : costElement4.getCostElementYearses()) {
                        if (costElementYears.getYear() == 0) {

                            costElementYears.setCostFrequencyValue((int) harvestFre);
                            costElementYears.setCostFrequencyUnit(freqUnit);
                            costElementYears.setCostPerPeriodValue(harvestTimes);
                            costElementYears.setCostPerPeriodUnit(quaUnit);
                            costElementYears.setCostPerUnitValue(harvestPrice);
                            costElementYears.setCostPerUnitUnit(priceCurrency);
                            costElementYears.setHouseholds(household);
                            costElementYears.setProjectedIndex(0);
                            costElementYears.setSubtotal(0);
                            mHarvestFre = harvestFre;
                            mFreqUnit = freqUnit;
                            mHarvestTimes = harvestTimes;
                            mQuaUnit = quaUnit;
                            mMarketPriceValue = harvestPrice;
                            mPriceCurrency = priceCurrency;
                            mHousehold = household;
                        }
                        if (costElementYears.getProjectedIndex() > 0) {
                            BigDecimal bigDecimalPowerFactor = new BigDecimal(Math.pow((1 + inflationRate), costElementYears.getProjectedIndex()));
                            BigDecimal bigDecimalHarvestPrice = new BigDecimal(harvestPrice);
                            BigDecimal bigDecimalHousehold = new BigDecimal(household);

                            double marketPriceVal = bigDecimalHarvestPrice.multiply(bigDecimalPowerFactor, MathContext.DECIMAL64).doubleValue();

                            BigDecimal bigDecimalfreqUnit = new BigDecimal(freqUnit);
                            if (freqUnit == 2) {
                                bigDecimalfreqUnit = new BigDecimal(1);
                            }

                            BigDecimal bigDecimalharvestFre = new BigDecimal(harvestFre);

                            BigDecimal bigDecimalharvestTimes = new BigDecimal(harvestTimes);
                            BigDecimal bigDecimalmarketPriceVal = new BigDecimal(marketPriceVal);

                            double totalVal = bigDecimalfreqUnit.multiply(bigDecimalharvestFre, MathContext.DECIMAL64)
                                    .multiply(bigDecimalharvestTimes, MathContext.DECIMAL64)
                                    .multiply(bigDecimalHousehold, MathContext.DECIMAL64)
                                    .multiply(bigDecimalmarketPriceVal, MathContext.DECIMAL64).doubleValue();

                            costElementYears.setCostFrequencyValue((int) harvestFre);
                            costElementYears.setCostFrequencyUnit(freqUnit);
                            costElementYears.setCostPerPeriodValue(harvestTimes);
                            costElementYears.setCostPerPeriodUnit(quaUnit);
                            costElementYears.setCostPerUnitValue(marketPriceVal);
                            costElementYears.setCostPerUnitUnit(priceCurrency);
                            costElementYears.setHouseholds(household);
                            costElementYears.setSubtotal(totalVal);
                        }
                    }
                }
                else
                    lastYearIndex = 0;

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if(currentYearIndex == 0 && currentCostProductIndex <= totalCostProductCount
                        && currentCostProductIndex !=0 && !nextProduct){
                    Log.e("REQQQQQQ","REEEEEEE");
                    buttonNext.setClickable(true);
                    showTrendDialog(revenueProducts.get(currentCostProductIndexSave));
                } else if (currentCostProductIndex < totalCostProductCount) {
                    loadRevenueProduct(revenueProducts.get(currentCostProductIndex));
                    //currentCostProductIndex++;
                    nextProduct = false;
                    buttonNext.setClickable(true);
                } else {
                    nextProduct = false;
                    Toast.makeText(context, getResources().getText(R.string.completed_text), Toast.LENGTH_SHORT).show();
                    allCashFlow();
                    calculateNPV();

                    Intent i = new Intent(NaturalCapitalSharedCostActivityC.this, NaturalCapitalSharedCostOutlay.class);
                    startActivity(i);
                }
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // transaction is automatically rolled-back, do any cleanup here
                // Log.e("REALM", "All done updating."+error.toString());
            }
        });
    }

    private void showTrendDialog(final SharedCostElement costElement) {

        final Dialog dialog = new Dialog(NaturalCapitalSharedCostActivityC.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.activity_natural_capital_trend);
        Button dialogBack = (Button) dialog.findViewById(R.id.button_back);
        Button dialogNext = (Button) dialog.findViewById(R.id.button_next);
        final RadioGroup dialogRadioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);
        TextView dialogFrequency = (TextView) dialog.findViewById(R.id.text_trend_frequency);
        TextView dialogTimePeriod = (TextView) dialog.findViewById(R.id.text_trend_time_perioid);
        TextView dialogHouseholds = (TextView) dialog.findViewById(R.id.text_trend_num_households);
        TextView dialogQuantity = (TextView) dialog.findViewById(R.id.text_trend_quantity);
        TextView dialogUnit = (TextView) dialog.findViewById(R.id.text_trend_unit);
        TextView dialogPrice = (TextView) dialog.findViewById(R.id.text_trend_price);
        TextView dialogArea = (TextView) dialog.findViewById(R.id.text_trend_area);
        TextView dialogQuestionHarvest = (TextView) dialog.findViewById(R.id.text_question_harvest);
        TextView dialogQuestionHouseholds = (TextView) dialog.findViewById(R.id.text_question_households);
        TextView dialogQuestionPerHousehold = (TextView) dialog.findViewById(R.id.text_question_per_household);
        TextView dialogQuestionPerUnit = (TextView) dialog.findViewById(R.id.text_question_per_unit);


        final EditText dialogEditFrequency = (EditText) dialog.findViewById(R.id.edit_trend_frequency);
        final EditText dialogEditHousehold = (EditText) dialog.findViewById(R.id.edit_trend_num_households);
        final EditText dialogEditQuantity = (EditText) dialog.findViewById(R.id.edit_trend_quantity);
        final EditText dialogEditPrice = (EditText) dialog.findViewById(R.id.edit_trend_price);
        final EditText dialogEditArea = (EditText) dialog.findViewById(R.id.edit_trend_area);

        dialogQuestionHarvest.setText(getString(R.string.text_question_harvest, costElement.getName()));
        dialogQuestionHouseholds.setText(getString(R.string.text_number_of_households, costElement.getName()));
        dialogQuestionPerHousehold.setText(getString(R.string.text_question_quantity, costElement.getName()));
        dialogQuestionPerUnit.setText(getString(R.string.text_question_price, costElement.getName()));
        dialogFrequency.setText(String.valueOf(mHarvestFre));
        dialogTimePeriod.setText(String.valueOf(mFreqUnit));
        dialogHouseholds.setText(String.valueOf(mHousehold));
        dialogQuantity.setText(String.valueOf(mHarvestTimes));
        dialogUnit.setText(String.valueOf(mQuaUnit));
        dialogPrice.setText(String.valueOf(roundToTwoDecimal(mMarketPriceValue)));
        dialogArea.setText(String.valueOf(mHarvestArea));

        if(mFreqUnit == 1.0) {
            dialogEditFrequency.setFocusable(false);
            dialogEditFrequency.setBackgroundColor(ContextCompat.getColor(this,R.color.colorDisable));
        } else
            dialogEditFrequency.setFocusable(true);

        dialogBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialogNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialogRadioGroup.getCheckedRadioButtonId() == R.id.radio_button_positive) {

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            RevenueProduct revenueProduct4 = realm.where(RevenueProduct.class)
                                    .equalTo("id", costElement.getId())
                                    .findFirst();
                            double harvestFre = mHarvestFre;
                            double harvestTimes = mHarvestTimes;
                            double harvestPrice = mHarvestPrice;
                            double harvestArea = mHarvestArea;
                            double household = mHousehold;


                            if (!dialogEditFrequency.getText().toString().equals(""))
                                harvestFre = Double.parseDouble(dialogEditFrequency.getText().toString());

                            if (!dialogEditHousehold.getText().toString().equals(""))
                                household = Double.parseDouble(dialogEditHousehold.getText().toString());

                            if (!dialogEditQuantity.getText().toString().equals(""))
                                harvestTimes = Double.parseDouble(dialogEditQuantity.getText().toString());

                            if (!dialogEditPrice.getText().toString().equals(""))
                                harvestPrice = Double.parseDouble(dialogEditPrice.getText().toString());

                            if (!dialogEditArea.getText().toString().equals(""))
                                harvestArea = Double.parseDouble(dialogEditArea.getText().toString());

                            for (RevenueProductYears revenueProductYears : revenueProduct4.getRevenueProductYearses()) {
                                if (revenueProductYears.getYear() == 0) {
                                    revenueProductYears.setHarvestFrequencyValue(harvestFre);
                                    revenueProductYears.setQuantityValue(harvestTimes);
                                    revenueProductYears.setMarketPriceValue(harvestPrice);
                                    revenueProductYears.setProjectedIndex(0);
                                    revenueProductYears.setSubtotal(0);
                                    revenueProductYears.setHouseholds(household);
                                    revenueProductYears.setHarvestFrequencyUnit(mFreqUnit);
                                    revenueProductYears.setQuantityUnit(mQuaUnit);
                                    revenueProductYears.setMarketPriceCurrency(mPriceCurrency);

                                    if (currentSocialCapitalSurvey.equals(getString(R.string.string_pastureland))) {
                                        revenueProductYears.setHarvestArea(harvestArea);
                                    }
                                }
                                if (revenueProductYears.getProjectedIndex() > 0) {
                                    BigDecimal bigDecimalPowerFactor = new BigDecimal(Math.pow((1 + inflationRate), revenueProductYears.getProjectedIndex()), MathContext.DECIMAL64);
                                    BigDecimal bigDecimalHarvestPrice = new BigDecimal(harvestPrice);

                                    double marketPriceVal = bigDecimalPowerFactor.multiply(bigDecimalHarvestPrice, MathContext.DECIMAL64).doubleValue();

                                    // double marketPriceVal = harvestPrice * Math.pow((1 + inflationRate), revenueProductYears.getProjectedIndex());
                                    //marketPriceVal = roundToTwoDecimal(marketPriceVal);


                                    BigDecimal bigDecimalfreqUnit = new BigDecimal(mFreqUnit);
                                    if (!currentSocialCapitalSurvey.equals(getString(R.string.string_pastureland))) {
                                        if (mFreqUnit == 2) {
                                            bigDecimalfreqUnit = new BigDecimal(1);
                                        }
                                    }
                                    BigDecimal bigDecimalharvestFre = new BigDecimal(harvestFre);
                                    BigDecimal bigDecimalharvestTimes = new BigDecimal(harvestTimes);
                                    BigDecimal bigDecimalmarketPriceVal = new BigDecimal(marketPriceVal);
                                    BigDecimal bigDecimalHarvestArea = new BigDecimal(harvestArea);
                                    BigDecimal bigDecimalHousehold = new BigDecimal(household);


                                    double totalVal = bigDecimalfreqUnit.multiply(bigDecimalharvestFre, MathContext.DECIMAL64)
                                            .multiply(bigDecimalharvestTimes, MathContext.DECIMAL64)
                                            .multiply(bigDecimalHousehold, MathContext.DECIMAL64)
                                            .multiply(bigDecimalmarketPriceVal, MathContext.DECIMAL64).doubleValue();

                                    if (currentSocialCapitalSurvey.equals(getString(R.string.string_pastureland))) {
                                        BigDecimal bigDecimal12 = new BigDecimal("12");
                                        BigDecimal bigDecimalNewHarvestArea = bigDecimalharvestFre.divide(bigDecimal12, MathContext.DECIMAL64);
                                        totalVal = bigDecimalNewHarvestArea.multiply(bigDecimalharvestTimes, MathContext.DECIMAL64)
                                                .multiply(bigDecimalmarketPriceVal, MathContext.DECIMAL64)
                                                .multiply(bigDecimalHarvestArea, MathContext.DECIMAL64)
                                                .doubleValue();
                                    }

                                    revenueProductYears.setHarvestFrequencyValue(harvestFre);
                                    revenueProductYears.setHarvestFrequencyUnit(mFreqUnit);
                                    revenueProductYears.setQuantityValue(harvestTimes);
                                    revenueProductYears.setQuantityUnit(mQuaUnit);
                                    revenueProductYears.setMarketPriceValue(marketPriceVal);
                                    revenueProductYears.setMarketPriceCurrency(mPriceCurrency);
                                    revenueProductYears.setHouseholds(household);
                                    revenueProductYears.setSubtotal(totalVal);

                                    if (currentSocialCapitalSurvey.equals(getString(R.string.string_pastureland))) {
                                        revenueProductYears.setHarvestArea(harvestArea);
                                    }
                                }
                            }
                        }
                    }, new Realm.Transaction.OnSuccess() {

                        @Override
                        public void onSuccess() {
                            dialog.dismiss();
                            if (buttonNext.isClickable()) {
                                buttonNext.setClickable(false);
                                nextProduct = true;
                                saveYearlyDatas(revenueProducts.get(currentCostProductIndexSave));
                            }
                        }
                    });
                } else {
                    dialog.dismiss();
                    if (buttonNext.isClickable()) {
                        buttonNext.setClickable(false);
                        nextProduct = true;
                        saveYearlyDatas(revenueProducts.get(currentCostProductIndexSave));
                    }
                }
            }
        });

        dialog.show();
    }

    @Override
    protected void onResume() {
        setNav();
        super.onResume();
        buttonNext.setClickable(true);
    }

    public void nextLandKind() {
        RealmResults<LandKind> landKindRealmResults = realm.where(LandKind.class)
                .equalTo("surveyId", surveyId)
                .equalTo("status", "active")
                .findAll();
        int j = 0;
        int i = 0;
        for (LandKind landKind : landKindRealmResults) {
            //Log.e("TAG ", landKind.toString());
            //Log.e(TAG, String.valueOf(survey1.getParticipants().size()));
            if (landKind.getName().equals(currentSocialCapitalSurvey)) {
                j = i + 1;
            }
            i++;
        }

        if (j < i) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("currentSocialCapitalSurvey", landKindRealmResults.get(j).getName());
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), StartLandTypeActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), NewCertificateActivity.class);
            startActivity(intent);
        }
    }

    public void allCashFlow() {
        Survey results = realm.where(Survey.class)
                .equalTo("surveyId", surveyId)
                .findFirst();
        RealmList<CashFlow> cashFlows = new RealmList<>();


        CashFlow cashFlowTemp = null;
        int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));

        int k = 0;

        for (SharedCostElement costElement : results.getSharedCostElements()) {
            for (SharedCostElementYears costElementYears00 : costElement.getCostElementYearses()) {
                Log.e("SHAREDCOSTYEARS", costElementYears00 + "");
            }
            if (k <= 0) {
                for (SharedCostElementYears costElementYears : costElement.getCostElementYearses()) {
                    cashFlowTemp = calculateCashFlow("", costElementYears.getYear(), mSharedDiscountRate);
                    cashFlows.add(cashFlowTemp);
                    year = costElementYears.getYear();
                    Log.e("SHARED CASHFLOW", cashFlowTemp.toString());

                }
                cashFlows.add(calculateTerminalValue("", year, cashFlowTemp, mSharedDiscountRate));
                Log.e("SHARED CASHFLOW TER", calculateTerminalValue("", year, cashFlowTemp, mSharedDiscountRate).toString());

            }
            k++;
        }
        realm.beginTransaction();
        results.setSharedCashFlows(cashFlows);
        realm.commitTransaction();
    }

    public CashFlow calculateCashFlow(String landKind, int year, double disRatePersent) {


        results = realm.where(Survey.class)
                .equalTo("surveyId", surveyId)
                .findFirst();
        RealmResults<SharedCostElementYears> costElementYearses = realm.where(SharedCostElementYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("year", year)
                .findAll();

        SharedCostElementYears costElementYears = realm.where(SharedCostElementYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("year", year)
                .findFirst();


        double revenueTotal = 0;
        double costTotal = 0;
        double outlayTotal = 0;
        double disRate = disRatePersent / 100;

        double disFactor = 0;
        int year1 = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));

        BigDecimal bigDecimalOne = new BigDecimal("1");

        for (SharedCostElementYears costElementYears1 : costElementYearses) {
            costTotal = costTotal + costElementYears1.getSubtotal();

            double powerFactor = Math.pow(1 + disRate, costElementYears.getProjectedIndex());
            BigDecimal bigDecimalPowerFactor = new BigDecimal(powerFactor);
            BigDecimal bigDecimalDisFactor = bigDecimalOne.divide(bigDecimalPowerFactor, MathContext.DECIMAL64);

            disFactor = bigDecimalDisFactor.doubleValue();
        }

        double cashFlowVal = revenueTotal - costTotal - outlayTotal;

        double discountedCashFlow = cashFlowVal * disFactor;

        realm.beginTransaction();
        CashFlow cashFlow = realm.createObject(CashFlow.class);
        cashFlow.setId(getNextKeyCashFlow());
        cashFlow.setSurveyId(surveyId);
        cashFlow.setYear(year);
        cashFlow.setValue(cashFlowVal);
        cashFlow.setDiscountingFactor(disFactor);
        cashFlow.setDiscountedCashFlow(discountedCashFlow);

        cashFlow.setTotalRevenue(revenueTotal);
        cashFlow.setTotalCost(costTotal);
        cashFlow.setTotalOutlay(outlayTotal);
        realm.commitTransaction();

        return cashFlow;
    }

    private CashFlow calculateTerminalValue(String landKind, int year, CashFlow cashFlowTemp, double discountRateOverride) {

        SharedCostElementYears costElementYears = realm.where(SharedCostElementYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findFirst();


        RealmResults<SharedCostElementYears> costElementYearses = realm.where(SharedCostElementYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("year", year)
                .findAll();

        double disRate = discountRateOverride / 100;
        double revenueTotal = 0;
        double costTotal = 0;
        double outlayTotal = 0;

        double disFactor = 0;
        double terminalValue = 0;

        BigDecimal bigDecimalOne = new BigDecimal("1");

        for (SharedCostElementYears costElementYears1 : costElementYearses) {
            costTotal = costTotal + costElementYears1.getSubtotal();

            double powerFactor = Math.pow(1 + disRate, costElementYears.getProjectedIndex());
            BigDecimal bigDecimalPowerFactor = new BigDecimal(powerFactor);
            BigDecimal bigDecimalDisFactor = bigDecimalOne.divide(bigDecimalPowerFactor, MathContext.DECIMAL64);

            disFactor = bigDecimalDisFactor.doubleValue();
        }

        terminalValue = disRate > inflationRate ? (cashFlowTemp.getValue() / (disRate - inflationRate)) : 0;

        double discountedCashFlow = terminalValue * disFactor;

        realm.beginTransaction();
        CashFlow cashFlow = realm.createObject(CashFlow.class);
        cashFlow.setId(getNextKeyCashFlow());
        cashFlow.setSurveyId(surveyId);
        cashFlow.setYear(year);
        cashFlow.setValue(terminalValue);
        cashFlow.setDiscountingFactor(disFactor);
        cashFlow.setDiscountedCashFlow(discountedCashFlow);
        cashFlow.setTotalRevenue(revenueTotal);
        cashFlow.setTotalCost(costTotal);
        cashFlow.setTotalOutlay(outlayTotal);
        realm.commitTransaction();

        return cashFlow;

    }


    public void calculateNPV() {
        Survey results = realm.where(Survey.class)
                .equalTo("surveyId", surveyId)
                .findFirst();
        int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
        double npv = 0;

        for (CashFlow cashFlow : results.getSharedCashFlows()) {
            Log.e("Cash flow ", cashFlow.toString());
            if (cashFlow.getYear() >= year) {
                npv = npv + cashFlow.getDiscountedCashFlow();
            }
        }

        if (results.getComponents() == null) {
            realm.beginTransaction();
            Component component = realm.createObject(Component.class);
            component.setId(getNextKeyComponent());
            component.setSurveyId(surveyId);
            component.setSharedCostValue(npv);
            realm.commitTransaction();

            realm.beginTransaction();
            results.setComponents(component);
            realm.commitTransaction();
        } else {
            realm.beginTransaction();
            results.getComponents().setSharedCostValue(npv);
            realm.commitTransaction();
        }
    }

    public int getNextKeyCashFlow() {
        return realm.where(CashFlow.class).max("id").intValue() + 1;
    }

    public int getNextKeyComponent() {
        return realm.where(Component.class).max("id").intValue() + 1;
    }

    public void toggleMenuDrawer() {
        if (menuDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            menuDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public double roundToTwoDecimal(double val) {
        val = val * 100;
        val = Math.round(val);
        val = val / 100;
        return val;
    }
}