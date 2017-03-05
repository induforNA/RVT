package com.sayone.omidyar.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
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

    public double mMarketPriceValue;
    Realm realm;
    SharedPreferences sharedPref;
    String surveyId;
    Context context;
    String currentSocialCapitalServey;
    Spinner spinnerYear, spinnerUnit, spinnerTimePeriod;
    String year, unit, timePeriod;
    Button buttonBack, buttonNext;
    TextView loadQuestions;
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
    int lastYearIndex = 0;
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
    double mHarvestFre = 0;
    double mHarvestTimes = 0;
    double mHarvestPrice = 0;
    double mHarvestArea = 0;
    double mHousehold = 0;
    double mFreqUnit = 0;
    String mQuaUnit = "";
    String mPriceCurrency = "";
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView landType;
    private DrawerLayout menuDrawerLayout;
    private TextView surveyIdDrawer;
    private TextView areaQuestion;
    private TextView livestockQuestion;
    private TextView householdQuestion;
    private EditText areaEdit;
    private EditText livestockEdit;
    private EditText householdEdit;
    private String language;
    private LinearLayout householdContainer;
    private LinearLayout livestockContainer;
    private LinearLayout areaContainer;
    private boolean nextProduct = false;

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
    private boolean isTrend = false;
    private double harvestFreDisp;
    private ArrayList timePeriodListSecOneTime;
    private ArrayList<String> timePeriodListSec;
    private ArrayList<String> unitListSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_capital_survey_d);

        context = this;
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        surveyId = sharedPref.getString("surveyId", "");
        currentSocialCapitalServey = sharedPref.getString("currentSocialCapitalSurvey", "");

        spinnerYear = (Spinner) findViewById(R.id.spinner_year);
        spinnerUnit = (Spinner) findViewById(R.id.spinner_unit);
        spinnerTimePeriod = (Spinner) findViewById(R.id.spinner_time_period);
        buttonBack = (Button) findViewById(R.id.button_back);
        buttonNext = (Button) findViewById(R.id.button_next);
        loadQuestions = (TextView) findViewById(R.id.load_questions);
        householdQuestion = (TextView) findViewById(R.id.household_text);
        householdContainer = (LinearLayout) findViewById(R.id.household_layout);
        livestockContainer = (LinearLayout) findViewById(R.id.container_livestock);
        areaContainer = (LinearLayout) findViewById(R.id.area_harvest_container);
        // saveBtn = (Button) findViewById(R.id.save_btn);
        quantityQuestion = (TextView) findViewById(R.id.quantity_question);
        productQuestion = (TextView) findViewById(R.id.product_question);
        noOfTimesEdit = (EditText) findViewById(R.id.no_of_times_edit);
        quanityEdit = (EditText) findViewById(R.id.quanity_edit);
        priceEdit = (EditText) findViewById(R.id.price_edit);
        householdEdit = (EditText) findViewById(R.id.household_edit);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        surveyIdDrawer = (TextView) findViewById(R.id.text_view_id);
        areaQuestion = (TextView) findViewById(R.id.area_question);
        areaEdit = (EditText) findViewById(R.id.area_edit);
        landType = (TextView) findViewById(R.id.land_type);
        timePerHead = (TextView) findViewById(R.id.time_per_head);
        numTimesHead = (TextView) findViewById(R.id.num_times_head);
        livestockQuestion = (TextView) findViewById(R.id.livestock_question);
        livestockEdit = (EditText) findViewById(R.id.livestock_edit);

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
        householdContainer.setVisibility(View.VISIBLE);
        if (currentSocialCapitalServey.equals(getString(R.string.string_forestland)))
            landType.setText(getResources().getText(R.string.string_forestland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
            householdContainer.setVisibility(View.GONE);
            landType.setText(getResources().getText(R.string.string_pastureland));
        }
        if (currentSocialCapitalServey.equals(getString(R.string.string_miningland)))
            landType.setText(getResources().getText(R.string.string_miningland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_cropland)))
            landType.setText(getResources().getText(R.string.title_cropland));

        inflationRate = 0.05;
        language = Locale.getDefault().getDisplayLanguage();

        yearList = new ArrayList<>();
        timePeriodList = new ArrayList<>();
        unitList = new ArrayList<>();
        timePeriodListSec = new ArrayList<>();
        timePeriodListSecOneTime = new ArrayList<>();

        year_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, yearList);
        timePeriod_adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, timePeriodList);
        unit_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, unitList);

        Survey results = realm.where(Survey.class)
                .equalTo("surveyId", surveyId)
                .findFirst();

        if (results.getOverRideInflationRate() != 0) {
            inflationRate = results.getOverRideInflationRate() / 100;
        } else if (results.getInflationRate() != 0) {
            inflationRate = results.getInflationRate() / 100;
        }
        RealmResults<Frequency> frequencyResult = realm.where(Frequency.class).findAll();
        for (Frequency frequency : frequencyResult) {
            Log.e("HARVEST ", frequency.getHarvestFrequency() + " " + frequency.getFrequencyValue());
            if (language.equals("			")) {
                timePeriodList.add(frequency.getHarvestFrequencyHindi());
            } else {
                timePeriodList.add(frequency.getHarvestFrequency());
                if (!frequency.getHarvestFrequency().equals("one-time")) {
                    timePeriodListSec.add(frequency.getHarvestFrequency());
                } else {
                    timePeriodListSecOneTime.add(frequency.getHarvestFrequency());
                }
            }
        }

        RealmResults<Quantity> quantityResult = realm.where(Quantity.class).findAll();
        for (Quantity quantity : quantityResult) {
            if (language.equals("			")) {
                unitList.add(quantity.getQuantityNameHindi());
            } else {
                unitList.add(quantity.getQuantityName());
            }
        }

        spinnerYear.setAdapter(year_adapter);
        spinnerUnit.setAdapter(unit_adapter);
        spinnerTimePeriod.setAdapter(timePeriod_adapter);

        for (LandKind landKind : results.getLandKinds()) {
            if (landKind.getName().equals(getString(R.string.string_forestland)) && currentSocialCapitalServey.equals(getString(R.string.string_forestland))) {
                if (landKind.getForestLand().getRevenueProducts().size() > 0) {
                    revenueProducts = landKind.getForestLand().getRevenueProducts();
                    totalCostProductCount = revenueProducts.size();
                    currentCostProductIndex = 0;
                    currentYearIndex = 0;
                    if (currentCostProductIndex < totalCostProductCount) {
                        productReveneIdCon = landKind.getForestLand().getRevenueProducts().get(currentCostProductIndex).getId();
                        loadRevenueProduct(landKind.getForestLand().getRevenueProducts().get(currentCostProductIndex));
                    }
                }
            } else if (landKind.getName().equals(getString(R.string.string_cropland)) && currentSocialCapitalServey.equals(getString(R.string.string_cropland))) {
                if (landKind.getCropLand().getRevenueProducts().size() > 0) {
                    revenueProducts = landKind.getCropLand().getRevenueProducts();
                    totalCostProductCount = revenueProducts.size();
                    currentCostProductIndex = 0;
                    currentYearIndex = 0;
                    if (currentCostProductIndex < totalCostProductCount) {
                        productReveneIdCon = landKind.getCropLand().getRevenueProducts().get(currentCostProductIndex).getId();
                        loadRevenueProduct(landKind.getCropLand().getRevenueProducts().get(currentCostProductIndex));
                    }
                }
            } else if (landKind.getName().equals(getString(R.string.string_pastureland)) && currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                if (landKind.getPastureLand().getRevenueProducts().size() > 0) {
                    revenueProducts = landKind.getPastureLand().getRevenueProducts();
                    totalCostProductCount = revenueProducts.size();
                    currentCostProductIndex = 0;
                    currentYearIndex = 0;
                    if (currentCostProductIndex < totalCostProductCount) {
                        productReveneIdCon = landKind.getPastureLand().getRevenueProducts().get(currentCostProductIndex).getId();
                        loadRevenueProduct(landKind.getPastureLand().getRevenueProducts().get(currentCostProductIndex));
                    }
                }
            } else if (landKind.getName().equals(getString(R.string.string_miningland)) && currentSocialCapitalServey.equals(getString(R.string.string_miningland))) {
                if (landKind.getMiningLand().getRevenueProducts().size() > 0) {
                    revenueProducts = landKind.getMiningLand().getRevenueProducts();
                    totalCostProductCount = revenueProducts.size();
                    currentCostProductIndex = 0;
                    currentYearIndex = 0;
                    if (currentCostProductIndex < totalCostProductCount) {
                        productReveneIdCon = landKind.getMiningLand().getRevenueProducts().get(currentCostProductIndex).getId();
                        loadRevenueProduct(landKind.getMiningLand().getRevenueProducts().get(currentCostProductIndex));
                    }
                }
            }
        }

        unit = spinnerUnit.getSelectedItem().toString();

        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        imageViewMenuIcon.setOnClickListener(this);
        drawerCloseBtn.setOnClickListener(this);
        surveyIdDrawer.setText(surveyId);

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
                unitListSec = new ArrayList<>();
                unitListSec.add(unit);
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
                // Log.e("Time period ",timePeriod);
                if (timePeriod.equals("one-time") && !currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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

    public void backButtonAction() {
        int currentYearBackIndex = getIndexYears(spinnerYear.getSelectedItem().toString());
        int currentPrductNameBackIndex = getIndexRevenueProducts(currentProductName);
        if (isTrend)
            isTrend = false;
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
//        Log.e("AA BB ", revenueProducts.get(0).getRevenueProductYearses().size()+"");

        if (revenueProducts.size() == 1) {
            int yearCounting = 0;
            for (RevenueProductYears revenueProductYearsA : revenueProducts.get(0).getRevenueProductYearses()) {
                Log.e("YEAR ", revenueProductYearsA.getYear() + "");
                if (revenueProductYearsA.getYear() != 0 && revenueProductYearsA.getYear() < sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR))) {
                    yearCounting++;
                }
            }
            if (yearCounting == 1) {
                finish();
            } else {
             /*   if(currentCostProductIndex < totalCostProductCount)
                    loadRevenueProduct(revenueProducts.get(currentCostProductIndex));
                else*/
                loadRevenueProduct(revenueProducts.get(currentCostProductIndexSave));

            }
        } else {
            if (currentCostProductIndex < totalCostProductCount) {
                loadRevenueProduct(revenueProducts.get(currentCostProductIndex));
            } else
                loadRevenueProduct(revenueProducts.get(currentCostProductIndexSave));
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.button_next:

                if (buttonNext.isClickable()) {
                    buttonNext.setClickable(false);
                    saveYearlyDatas(revenueProducts.get(currentCostProductIndexSave));
                }
//                Log.e("YEAR ","PRE "+previousYearIndex+" Cur "+currentYearIndex);
//                Log.e("COST ","PRE "+previousCostProductIndex+" Cur "+currentCostProductIndex   );
                break;

            case R.id.button_back:
                backButtonAction();
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
                Intent intent_outlay = new Intent(getApplicationContext(), SharedCostSurveyStartActivity.class);
                startActivity(intent_outlay);
                break;

            case R.id.text_certificate:
                Intent intent_certificate = new Intent(getApplicationContext(), NewCertificateActivity.class);
                startActivity(intent_certificate);
                break;
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
        Intent intent = new Intent(NaturalCapitalSurveyActivityD.this, StartLandTypeActivity.class);
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
            RevenueProduct revenueProductGet = revenueProducts.get(i);
            if (itemName.equals(revenueProductGet.getName())) {
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

    public void loadRevenueProduct(RevenueProduct revenueProductLoad) {
        // Log.e("LOAD ","REVENUE 11111");
        livestockContainer.setVisibility(View.GONE);
        areaContainer.setVisibility(View.VISIBLE);
        final long id = revenueProductLoad.getId();
        currentProductName = revenueProductLoad.getName();

        if (revenueProductLoad.getType().equals("Livestock")) {
            areaContainer.setVisibility(View.GONE);
            livestockContainer.setVisibility(View.VISIBLE);
            householdQuestion.setText(getString(R.string.string_household, "harvest " + revenueProductLoad.getName()));
            loadQuestions.setText(getString(R.string.text_question_livestock) + revenueProductLoad.getName() + " on this piece of land?");
            quantityQuestion.setText(getString(R.string.text_quantity_question_livestock));
            productQuestion.setText(getString(R.string.text_product_question_livestock));
            livestockQuestion.setText(getString(R.string.text_question_number_livestock));
            timePerHead.setVisibility(View.INVISIBLE);
            spinnerTimePeriod.setVisibility(View.INVISIBLE);
            numTimesHead.setVisibility(View.INVISIBLE);
            noOfTimesEdit.setEnabled(true);
        } else {
            householdQuestion.setText(getString(R.string.string_household, "harvest " + revenueProductLoad.getName()));
            loadQuestions.setText(getResources().getString(R.string.qn_natural_complex_1_1) + " harvest " + revenueProductLoad.getName() + getResources().getString(R.string.qn_natural_complex_1_2) + "?");
            quantityQuestion.setText(getResources().getString(R.string.qn_natural_capital_1, revenueProductLoad.getName() + getString(R.string.text_harvest)));
            productQuestion.setText(getResources().getString(R.string.qn_natural_complex_3_1) + " " + revenueProductLoad.getName() + " " + getResources().getString(R.string.qn_natural_complex_3_2));
            areaQuestion.setText(getResources().getString(R.string.percentage_area_harvested));
            if (revenueProductLoad.getLandKind().equals(getString(R.string.string_miningland))) {
                quantityQuestion.setText(getResources().getString(R.string.qn_natural_capital_1, revenueProductLoad.getName() + getString(R.string.text_extract)));
                areaQuestion.setText(getResources().getString(R.string.percentage_area_extracted));

            }
        }
        if (currentSocialCapitalServey.equals(getString(R.string.string_miningland))) {
            householdQuestion.setText(getString(R.string.string_household, "extract " + revenueProductLoad.getName()));
            loadQuestions.setText(getResources().getString(R.string.qn_natural_complex_1_1) + " extract " + revenueProductLoad.getName() + getResources().getString(R.string.qn_natural_complex_1_2) + "?");
        }
        productReveneIdCheck = revenueProductLoad.getId();

        int currentYear = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
        yearList.clear();
        totalYearsCount = 0;
        for (RevenueProductYears revenueProductYears : revenueProductLoad.getRevenueProductYearses()) {
            if (revenueProductYears.getYear() < currentYear && revenueProductYears.getYear() != 0) {
                yearList.add(revenueProductYears.getYear() + "");
                year_adapter.notifyDataSetChanged();

                // Log.e("REV PROD ID ",revenueProductYears.getRevenueProductId()+"");
                totalYearsCount++;
            }
        }

        currentCostProductIndexSave = currentCostProductIndex;
        currentYearIndexSave = currentYearIndex;

        if (currentYearIndex < totalYearsCount) {
            loadRevenueYears(revenueProductLoad.getRevenueProductYearses().get(currentYearIndexSave), revenueProductLoad);
            if (currentYearIndex == totalYearsCount) {
                previousCostProductIndex = currentCostProductIndex;
                currentCostProductIndex++;
                currentYearIndex = 0;
            }
        }
    }

    private void showTrendDialog(final RevenueProduct revenueProduct) {
        final long productId = revenueProduct.getId();
        isTrend = true;

        final Dialog dialog = new Dialog(NaturalCapitalSurveyActivityD.this, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.blackDisable)));
        dialog.setContentView(R.layout.activity_natural_capital_trend);
        Button dialogBack = (Button) dialog.findViewById(R.id.button_back);
        Button dialogNext = (Button) dialog.findViewById(R.id.button_next);
        final RadioGroup dialogRadioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);
        TextView dialogFrequency = (TextView) dialog.findViewById(R.id.text_trend_frequency);
        TextView dialogTimePeriod = (TextView) dialog.findViewById(R.id.text_trend_time_perioid);
        final TextView dialogHouseholds = (TextView) dialog.findViewById(R.id.text_trend_num_households);
        TextView dialogQuantity = (TextView) dialog.findViewById(R.id.text_trend_quantity);
        TextView dialogUnit = (TextView) dialog.findViewById(R.id.text_trend_unit);
        TextView dialogPrice = (TextView) dialog.findViewById(R.id.text_trend_price);
        final TextView dialogArea = (TextView) dialog.findViewById(R.id.text_trend_area);
        TextView dialogQuestionArea = (TextView) dialog.findViewById(R.id.text_area_question);
        TextView dialogQuestionHarvest = (TextView) dialog.findViewById(R.id.text_question_harvest);
        TextView dialogQuestionHouseholds = (TextView) dialog.findViewById(R.id.text_question_households);
        TextView dialogQuestionPerHousehold = (TextView) dialog.findViewById(R.id.text_question_per_household);
        TextView dialogQuestionPerUnit = (TextView) dialog.findViewById(R.id.text_question_per_unit);
        LinearLayout containerTimePeriod = (LinearLayout) dialog.findViewById(R.id.container_timePeriod);
        LinearLayout containerArea = (LinearLayout) dialog.findViewById(R.id.container_area);

        final EditText dialogEditFrequency = (EditText) dialog.findViewById(R.id.edit_trend_frequency);
        final EditText dialogEditHousehold = (EditText) dialog.findViewById(R.id.edit_trend_num_households);
        final EditText dialogEditQuantity = (EditText) dialog.findViewById(R.id.edit_trend_quantity);
        final EditText dialogEditPrice = (EditText) dialog.findViewById(R.id.edit_trend_price);
        final EditText dialogEditArea = (EditText) dialog.findViewById(R.id.edit_trend_area);
        final EditText dialogLivestock = (EditText) dialog.findViewById(R.id.livestock_edit);
        final Spinner dialogSpinnerTimePeriod = (Spinner) dialog.findViewById(R.id.dialog_spinner_time_period);
        final Spinner dialogSpinnerQuantityUnit = (Spinner) dialog.findViewById(R.id.dialog_spinner_quantity_unit);

        dialogRadioGroup.check(R.id.radio_button_positive);
        dialogEditFrequency.setEnabled(false);
        dialogEditHousehold.setEnabled(false);
        dialogEditQuantity.setEnabled(false);
        dialogEditPrice.setEnabled(false);
        dialogEditArea.setEnabled(false);
        dialogSpinnerTimePeriod.setEnabled(false);
        dialogSpinnerQuantityUnit.setEnabled(false);
        dialogRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (dialogRadioGroup.getCheckedRadioButtonId() == R.id.radio_button_negative) {
                    if(mFreqUnit == 1)
                        dialogEditFrequency.setEnabled(false);
                    else
                        dialogEditFrequency.setEnabled(true);
                    dialogEditHousehold.setEnabled(true);
                    dialogEditQuantity.setEnabled(true);
                    dialogEditPrice.setEnabled(true);
                    dialogEditArea.setEnabled(true);
                    dialogSpinnerTimePeriod.setEnabled(true);
                    dialogSpinnerQuantityUnit.setEnabled(true);
                } else if (dialogRadioGroup.getCheckedRadioButtonId() == R.id.radio_button_positive) {
                    dialogEditFrequency.setEnabled(false);
                    dialogEditHousehold.setEnabled(false);
                    dialogEditQuantity.setEnabled(false);
                    dialogEditPrice.setEnabled(false);
                    dialogEditArea.setEnabled(false);
                    dialogSpinnerTimePeriod.setEnabled(false);
                    dialogSpinnerQuantityUnit.setEnabled(false);
                }
            }
        });

        RevenueProductYears revenueProductTrend = revenueProduct.getRevenueProductTrend();
        if(revenueProductTrend != null){
            if(revenueProductTrend.getHarvestFrequencyValue() != 0)
                dialogEditFrequency.setText(String.valueOf(revenueProductTrend.getHarvestFrequencyValue()));
            if(revenueProductTrend.getHouseholds() != 0)
                dialogEditHousehold.setText(String.valueOf(revenueProductTrend.getHouseholds()));
            if(revenueProductTrend.getQuantityValue() != 0)
                dialogEditQuantity.setText(String.valueOf(revenueProductTrend.getQuantityValue()));
            if(revenueProductTrend.getMarketPriceValue() != 0)
                dialogEditPrice.setText(String.valueOf(revenueProductTrend.getMarketPriceValue()));
            if(revenueProductTrend.getHarvestArea() != 0)
                dialogArea.setText(String.valueOf(revenueProductTrend.getHarvestArea()));
        }

        dialogSpinnerTimePeriod.setAdapter(timePeriod_adapter);
        dialogSpinnerQuantityUnit.setAdapter(unit_adapter);
        Quantity quantity = realm.where(Quantity.class)
                .equalTo("quantityName", mQuaUnit)
                .findFirst();
        Frequency frequency = realm.where(Frequency.class)
                .equalTo("harvestFrequency", timePeriod)
                .findFirst();

        if (timePeriodList.size() != 0 && frequency != null) {
            // Log.e("TEST FRE ", timePeriod_adapter.getPosition(frequency.getHarvestFrequency())+"");
            if (language.equals("			")) {
                dialogSpinnerTimePeriod.setSelection(timePeriod_adapter.getPosition(frequency.getHarvestFrequencyHindi()));
            } else {
                if(revenueProductTrend != null && revenueProductTrend.getHarvestFrequencyUnit() != 0)
                    dialogSpinnerTimePeriod.setSelection(timePeriod_adapter.getPosition(String.valueOf(revenueProductTrend.getHarvestFrequencyUnit())));
                else
                    dialogSpinnerTimePeriod.setSelection(timePeriod_adapter.getPosition(frequency.getHarvestFrequency()));
            }
        }

        if (unitList.size() != 0 && quantity != null) {
            // Log.e("QUANTITY ", unit_adapter.getPosition(quantity.getQuantityName())+"");
            if(revenueProductTrend != null && revenueProductTrend.getQuantityUnit() != null)
                dialogSpinnerQuantityUnit.setSelection(unit_adapter.getPosition(revenueProductTrend.getQuantityUnit()));
            else
                dialogSpinnerQuantityUnit.setSelection(unit_adapter.getPosition(quantity.getQuantityName()));
        }
        dialogSpinnerTimePeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                timePeriod = parent.getItemAtPosition(pos).toString();

                // Log.e("Time period ",timePeriod);
                if (timePeriod.equals("one-time") && !currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                    dialogEditFrequency.setText("1");
                    dialogEditFrequency.setEnabled(false);
                } else {
                    dialogEditFrequency.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        dialogSpinnerQuantityUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                unit = parent.getItemAtPosition(pos).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        if (timePeriod.equals("one-time") && !currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
            dialogEditFrequency.setText("1");
            dialogEditFrequency.setEnabled(false);
            mHarvestFre = 0;
        } else {
            dialogEditFrequency.setEnabled(true);
        }

        dialogQuestionHarvest.setText(getString(R.string.text_question_harvest, getString(R.string.text_harvest) + " " + revenueProduct.getName()));
        dialogQuestionHouseholds.setText(getString(R.string.text_number_of_households, getString(R.string.text_harvest) + " " + revenueProduct.getName()));
        dialogQuestionPerHousehold.setText(getString(R.string.text_question_quantity, revenueProduct.getName() + " was harvested "));
        dialogQuestionPerUnit.setText(getString(R.string.text_question_price, revenueProduct.getName()));
        dialogQuestionArea.setText(R.string.percentage_area_harvested);
        if(currentSocialCapitalServey.equals(getString(R.string.string_miningland))){
            dialogQuestionHarvest.setText(getString(R.string.text_question_harvest, getString(R.string.text_extract)+" "+ revenueProduct.getName()));
            dialogQuestionHouseholds.setText(getString(R.string.text_number_of_households, getString(R.string.text_extract)+" "+revenueProduct.getName()));
            dialogQuestionPerHousehold.setText(getString(R.string.text_question_quantity, revenueProduct.getName()+" was extracted "));
            dialogQuestionArea.setText(R.string.percentage_area_extracted);
        }

        dialogFrequency.setText(String.valueOf(roundToTwoDecimal(harvestFreDisp)));
        dialogTimePeriod.setText(timePeriod);
        dialogHouseholds.setText(String.valueOf(roundToTwoDecimal(mHousehold)));
        dialogQuantity.setText(String.valueOf(roundToTwoDecimal(mHarvestTimes)));
        dialogUnit.setText(String.valueOf(mQuaUnit));
        dialogPrice.setText(String.valueOf(roundToTwoDecimal(mMarketPriceValue)));
        dialogArea.setText(String.valueOf(roundToTwoDecimal(mHarvestArea)));

        if(currentSocialCapitalServey.equals(getString(R.string.string_pastureland))){
            dialogQuestionHarvest.setText(getString(R.string.text_question_livestock) +revenueProduct.getName());
            dialogQuestionHouseholds.setText(getString(R.string.text_question_number_livestock));
            containerTimePeriod.setVisibility(View.GONE);
            containerArea.setVisibility(View.GONE);
        } else {
            containerTimePeriod.setVisibility(View.VISIBLE);
            containerArea.setVisibility(View.VISIBLE);
        }

        /*if(mFreqUnit == 1.0 && !currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
            dialogEditFrequency.setFocusable(false);
            dialogEditFrequency.setBackgroundColor(ContextCompat.getColor(this,R.color.colorDisable));
        } else
            dialogEditFrequency.setFocusable(true);*/

        dialogBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialogNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialogRadioGroup.getCheckedRadioButtonId() == R.id.radio_button_negative) {

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            RevenueProduct revenueProduct4 = realm.where(RevenueProduct.class)
                                    .equalTo("id", productId)
                                    .findFirst();
                            RevenueProductYears revenueProductTrend = realm.where(RevenueProductYears.class)
                                    .equalTo("id", productId)
                                    .findFirst();
                            Quantity quantity = realm.where(Quantity.class)
                                    .equalTo("quantityName", unit)
                                    .findFirst();
                            Frequency frequency = realm.where(Frequency.class)
                                    .equalTo("harvestFrequency", timePeriod)
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

                            if (dialogLivestock != null && !dialogLivestock.getText().toString().equals(""))
                                household = Double.parseDouble(dialogLivestock.getText().toString());

                            if(harvestFre == 1) {
                                harvestFre = 0;
                            }

                            for (RevenueProductYears revenueProductYears : revenueProduct4.getRevenueProductYearses()) {
                                if (revenueProductYears.getYear() == 0) {
                                    revenueProductYears.setHarvestFrequencyValue(harvestFre);
                                    revenueProductTrend.setHarvestFrequencyValue(harvestFre);
                                    revenueProductYears.setQuantityValue(harvestTimes);
                                    revenueProductTrend.setQuantityValue(harvestTimes);
                                    revenueProductYears.setMarketPriceValue(harvestPrice);
                                    revenueProductTrend.setMarketPriceValue(harvestPrice);
                                    revenueProductYears.setProjectedIndex(0);
                                    revenueProductTrend.setProjectedIndex(0);
                                    revenueProductYears.setSubtotal(0);
                                    revenueProductTrend.setSubtotal(0);
                                    revenueProductYears.setHouseholds(household);
                                    revenueProductTrend.setHouseholds(household);
                                    revenueProductYears.setHarvestFrequencyUnit(frequency.getFrequencyValue());
                                    revenueProductTrend.setHarvestFrequencyUnit(frequency.getFrequencyValue());
                                    revenueProductYears.setQuantityUnit(quantity.getQuantityName());
                                    revenueProductTrend.setQuantityUnit(quantity.getQuantityName());
                                    revenueProductYears.setMarketPriceCurrency(mPriceCurrency);
                                    revenueProductTrend.setMarketPriceCurrency(mPriceCurrency);
                                    revenueProduct4.setRevenueProductTrend(revenueProductTrend);

                                    if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                                        revenueProductYears.setHarvestArea(harvestArea);
                                    }
                                }
                                if (revenueProductYears.getProjectedIndex() > 0) {
                                    BigDecimal bigDecimalPowerFactor = new BigDecimal(Math.pow((1 + inflationRate), revenueProductYears.getProjectedIndex()), MathContext.DECIMAL64);
                                    BigDecimal bigDecimalHarvestPrice = new BigDecimal(harvestPrice);

                                    double marketPriceVal = bigDecimalPowerFactor.multiply(bigDecimalHarvestPrice, MathContext.DECIMAL64).doubleValue();

                                    BigDecimal bigDecimalfreqUnit = new BigDecimal(mFreqUnit);
                                    if (!currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
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

                                    if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                                        BigDecimal bigDecimal12 = new BigDecimal("12");
                                        BigDecimal bigDecimalNewHarvestArea = bigDecimalharvestFre.divide(bigDecimal12, MathContext.DECIMAL64);
                                        totalVal = bigDecimalNewHarvestArea.multiply(bigDecimalharvestTimes, MathContext.DECIMAL64)
                                                .multiply(bigDecimalmarketPriceVal, MathContext.DECIMAL64)
                                                .multiply(bigDecimalHousehold, MathContext.DECIMAL64)
                                                .doubleValue();
                                    }

                                    //realm.beginTransaction();
                                    revenueProductYears.setHarvestFrequencyValue(harvestFre);
                                    revenueProductYears.setHarvestFrequencyUnit(frequency.getFrequencyValue());
                                    revenueProductYears.setQuantityValue(harvestTimes);
                                    revenueProductYears.setQuantityUnit(quantity.getQuantityName());
                                    revenueProductYears.setMarketPriceValue(marketPriceVal);
                                    revenueProductYears.setMarketPriceCurrency(mPriceCurrency);
                                    revenueProductYears.setHouseholds(household);
                                    revenueProductYears.setSubtotal(totalVal);
                                    revenueProductYears.setHarvestArea(harvestArea);

/*
                                    if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                                        revenueProductYears.setHarvestArea(harvestArea);
                                    }*/
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
                       /* else
                            saveYearlyDatas(revenueProducts.get(currentCostProductIndexSave));*/
                    }
                }
            }
        });

        dialog.show();
    }

    public void loadRevenueYears(RevenueProductYears revenueProductYearsLoad, RevenueProduct revenueProductLoad1) {
        // Log.e("LOAD ","11111");
        if (revenueProductYearsLoad.getHarvestFrequencyValue() != 0) {
            noOfTimesEdit.setText(revenueProductYearsLoad.getHarvestFrequencyValue() + "");
        } else {
            noOfTimesEdit.setText("1");
        }

        if (revenueProductYearsLoad.getQuantityValue() != 0) {
            // Log.e("KKKKKKKKKKKKK ",revenueProductYearsLoad.getQuantityValue()+"");
            quanityEdit.setText(revenueProductYearsLoad.getQuantityValue() + "");
        } else {
            quanityEdit.setText("");
        }

        if (revenueProductYearsLoad.getMarketPriceValue() != 0) {
            priceEdit.setText(revenueProductYearsLoad.getMarketPriceValue() + "");
        } else {
            priceEdit.setText("");
        }

        if (revenueProductYearsLoad.getHarvestArea() != 0) {
            areaEdit.setText(revenueProductYearsLoad.getHarvestArea() + "");
        } else {
            areaEdit.setText("");
        }

        if (revenueProductYearsLoad.getHouseholds() != 0) {
            livestockEdit.setText(revenueProductYearsLoad.getHouseholds() + "");
            householdEdit.setText(revenueProductYearsLoad.getHouseholds() + "");
        } else {
            householdEdit.setText("");
            livestockEdit.setText("");
        }

        spinnerYear.setSelection(currentYearIndex);

        Frequency frequency = realm.where(Frequency.class)
                .equalTo("frequencyValue", (int) revenueProductYearsLoad.getHarvestFrequencyUnit())
                .findFirst();

        if (timePeriodList.size() != 0) {
            if (currentYearIndexSave == 0) {
                timePeriod_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timePeriodList);
            } else {
                if (frequency != null) {
                    if (frequency.getFrequencyValue() == 1)
                        timePeriod_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timePeriodListSecOneTime);
                    else
                        timePeriod_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timePeriodListSec);
                } else {
                    if (spinnerTimePeriod.getSelectedItem().equals("one-time")) {
                        timePeriod_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timePeriodListSecOneTime);
                        spinnerTimePeriod.setSelection(0);
                    } else {
                        timePeriod_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timePeriodListSec);
                    }
                }
            }
            // Log.e("TEST FRE ", timePeriod_adapter.getPosition(frequency.getHarvestFrequency())+"");
        }
        spinnerTimePeriod.setAdapter(timePeriod_adapter);

        if (frequency != null) {
            if (language.equals("			")) {
                spinnerTimePeriod.setSelection(timePeriod_adapter.getPosition(frequency.getHarvestFrequencyHindi()));
            } else {
                spinnerTimePeriod.setSelection(timePeriod_adapter.getPosition(frequency.getHarvestFrequency()));
            }
        }

        Quantity quantity = realm.where(Quantity.class)
                .equalTo("quantityName", revenueProductYearsLoad.getQuantityUnit())
                .findFirst();

        if (unitList.size() != 0) {
            if (currentYearIndexSave == 0) {
                unit_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, unitList);
            } else {
                if (quantity != null) {
                    unitListSec = new ArrayList<>();
                    unitListSec.add(quantity.getQuantityName());
                }
                unit_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, unitListSec);
            }
        }
        spinnerUnit.setAdapter(unit_adapter);

        if (unitList.size() != 0 && quantity != null) {
            // Log.e("QUANTITY ", unit_adapter.getPosition(quantity.getQuantityName())+"");
            spinnerUnit.setSelection(unit_adapter.getPosition(quantity.getQuantityName()));
        }

        previousYearIndex = currentYearIndex;
        currentYearIndex++;
    }

    public void saveYearlyDatas(final RevenueProduct revenueProduct2) {
        Log.e("currentProductIndex", currentCostProductIndex + currentCostProductIndexSave + "");
        final long revenueProductId = revenueProduct2.getId();
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle(getResources().getString(R.string.loading));
        progress.setMessage(getResources().getString(R.string.wait_while_loading));
        progress.show();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RevenueProduct revenueProduct2 = realm.where(RevenueProduct.class)
                        .equalTo("id", revenueProductId)
                        .findFirst();

                // Log.e("REVE PRO ",revenueProduct2.toString()+" YEAR "+spinnerYear.getSelectedItem());
                for (RevenueProductYears revenueProductYears1 : revenueProduct2.getRevenueProductYearses()) {
                    if (String.valueOf(revenueProductYears1.getYear()).equals(spinnerYear.getSelectedItem())) {
                        // Log.e("REVE YEAR ",revenueProductYears1.toString());
                        String spinnerTimePeriodStr = spinnerTimePeriod.getSelectedItem().toString();
                        Log.e("SPINNER STR ", spinnerTimePeriodStr);
                        Frequency frequency;
                        if (language.equals("			")) {
                            frequency = realm.where(Frequency.class)
                                    .equalTo("harvestFrequencyHindi", spinnerTimePeriodStr)
                                    .findFirst();
                        } else {
                            frequency = realm.where(Frequency.class)
                                    .equalTo("harvestFrequency", spinnerTimePeriodStr)
                                    .findFirst();
                        }

                        Log.e("SPINNER STR ", frequency.getHarvestFrequency() + " " + frequency.getFrequencyValue());


                        // int yearCurent = Calendar.getInstance().get(Calendar.YEAR);
                        int yearCurent = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
                        int yearIndex = revenueProductYears1.getYear() - yearCurent;

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

                        double total = 0;

                        String areaEditStr = areaEdit.getText().toString();
                        String livestockEditStr = livestockEdit.getText().toString();
                        double harverArea = 0;
                        if (areaEditStr.equals("")) {
                            harverArea = 0;
                        } else {
                            harverArea = Double.parseDouble(areaEditStr);
                        }

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
                        if (livestockEditStr.equals("")) {
                            livestockEditStr = "0";
                        }

                        BigDecimal bigDecimalNoOfTimes = new BigDecimal(noOfTimesEditStr);
                        BigDecimal bigDecimalPrice = new BigDecimal(priceEditStr);
                        BigDecimal bigDecimalQuanityEditStr = new BigDecimal(quanityEditStr);
                        BigDecimal bigDecimalHarverArea = new BigDecimal(harverArea);
                        BigDecimal bigDecimalHousehold = new BigDecimal(householdEditStr);
                        BigDecimal bigDecimalLivestock = new BigDecimal(livestockEditStr);

//                        Log.e("LAND ", currentSocialCapitalSurvey);

                        if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {

                            bigDecimalHousehold = bigDecimalLivestock;
                            householdEditStr = livestockEditStr;
                            BigDecimal bigDecimalTotal = bigDecimalNoOfTimes.multiply(bigDecimalPrice, MathContext.DECIMAL64)
                                    .multiply(bigDecimalQuanityEditStr, MathContext.DECIMAL64)
                                    .multiply(bigDecimalLivestock, MathContext.DECIMAL64);


                            total = bigDecimalTotal.divide(new BigDecimal("12"), MathContext.DECIMAL64).doubleValue();

                        } else {
                            BigDecimal bigDecimalFrequency;
                            if (frequency.getFrequencyValue() == 2) {
                                bigDecimalFrequency = new BigDecimal(1);
                            } else if (frequency.getFrequencyValue() == 1) {
                                bigDecimalFrequency = new BigDecimal(0);
                            } else {
                                bigDecimalFrequency = new BigDecimal(frequency.getFrequencyValue());
                            }

                            Log.e("FREQUENCY ", bigDecimalFrequency.doubleValue() + "");

                            BigDecimal bigDecimalTotal = bigDecimalFrequency.multiply(bigDecimalNoOfTimes, MathContext.DECIMAL64)
                                    .multiply(bigDecimalPrice, MathContext.DECIMAL64)
                                    .multiply(bigDecimalHousehold, MathContext.DECIMAL64)
                                    .multiply(bigDecimalQuanityEditStr, MathContext.DECIMAL64);

                            total = bigDecimalTotal.doubleValue();
                            Log.e("TOTAL  ", bigDecimalTotal.doubleValue() + "");

                        }
//                        Log.e("TOTAL ",total+"");

                        //realm.beginTransaction();
                        revenueProductYears1.setHarvestFrequencyValue(Double.parseDouble(noOfTimesEditStr));
                        revenueProductYears1.setHarvestFrequencyUnit(frequency.getFrequencyValue());
                        revenueProductYears1.setQuantityValue(Double.parseDouble(quanityEditStr));
                        revenueProductYears1.setQuantityUnit(spinnerUnit.getSelectedItem().toString());
                        revenueProductYears1.setMarketPriceValue(Double.parseDouble(priceEditStr));
                        revenueProductYears1.setProjectedIndex(yearIndex);
                        revenueProductYears1.setSubtotal(total);
                        revenueProductYears1.setHarvestArea(harverArea);
                        revenueProductYears1.setHouseholds(Double.parseDouble(householdEditStr));
                        if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                            revenueProductYears1.setHouseholds(Double.parseDouble(livestockEditStr));
                        }/* else {
                            revenueProductYears1.setHarvestArea(harverArea);
                        }*/

                        // Log.e("RE CHECK ",revenueProductYears1.toString());
                    }
                }

                for (RevenueProductYears revenueProductYears11 : revenueProduct2.getRevenueProductYearses()) {
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
                Log.e("REALM", " ERROR ." + error.toString());
            }
        });
    }

    public void calculateTrend(final long revenueProductIdLong) {

        realm.executeTransactionAsync(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                RevenueProduct revenueProduct4 = realm.where(RevenueProduct.class)
                        .equalTo("id", revenueProductIdLong)
                        .findFirst();
                double harvestFre = 0;
                double harvestTimes = 0;
                double harvestPrice = 0;
                double harvestArea = 0;
                double household = 0;

                double freqUnit = 0;
                String quaUnit = "";
                String priceCurrency = "";

                double previousFrequencyUnit = 0;

                if (!isTrend) {
                    if (revenueProduct4.getRevenueProductYearses().size() > 0) {
                        if (revenueProduct4.getRevenueProductYearses().get(0).getHarvestFrequencyUnit() == 0) {
                            int eleCount = 0;
                            harvestFreDisp = 0;
                            for (RevenueProductYears revenueProductYears : revenueProduct4.getRevenueProductYearses()) {
                                if (revenueProductYears.getProjectedIndex() < 0) {
                                    harvestFreDisp += harvestFre;

                                    harvestFre = 0;
                                   /* if (harvestTimes < revenueProductYears.getQuantityValue()) {
                                        harvestTimes = revenueProductYears.getQuantityValue();
                                    }
                                    if (harvestPrice < revenueProductYears.getMarketPriceValue()) {
                                        harvestPrice = revenueProductYears.getMarketPriceValue();
                                    }*/
//                                    harvestFre = revenueProductYears.getHarvestFrequencyValue();
                                    harvestTimes = harvestTimes + revenueProductYears.getQuantityValue();
                                    household = household + revenueProductYears.getHouseholds();
                                    harvestPrice = harvestPrice + revenueProductYears.getMarketPriceValue();

                                    freqUnit = revenueProductYears.getHarvestFrequencyUnit();
                                    quaUnit = revenueProductYears.getQuantityUnit();
                                    priceCurrency = revenueProductYears.getMarketPriceCurrency();
                                    harvestArea = harvestArea + revenueProductYears.getHarvestArea();

                                    eleCount++;
                                }
                            }
                            harvestFreDisp = harvestFreDisp / eleCount;
                            harvestTimes = harvestTimes / eleCount;
                            harvestPrice = harvestPrice / eleCount;
                            harvestArea = harvestArea / eleCount;
                            household = household / eleCount;

                        } else {
                            int eleCount = 0;
                            for (RevenueProductYears revenueProductYear : revenueProduct4.getRevenueProductYearses()) {
                                if (revenueProductYear.getProjectedIndex() < 0) {
                                    if (previousFrequencyUnit == 0) {
                                        previousFrequencyUnit = revenueProductYear.getHarvestFrequencyUnit();
                                    }
                                    previousFrequencyUnit = (previousFrequencyUnit < revenueProductYear.getHarvestFrequencyUnit())
                                            ? previousFrequencyUnit : revenueProductYear.getHarvestFrequencyUnit();
                                }
                            }
                            for (RevenueProductYears revenueProductYears : revenueProduct4.getRevenueProductYearses()) {

                                if (revenueProductYears.getProjectedIndex() < 0) {

                                    double harvestFreTemp ;
                                    double previousFreqTemp = previousFrequencyUnit == 2 ? 1 : previousFrequencyUnit;

                                    harvestTimes = harvestTimes + revenueProductYears.getQuantityValue();
                                    harvestPrice = harvestPrice + revenueProductYears.getMarketPriceValue();
                                    harvestArea = harvestArea + revenueProductYears.getHarvestArea();
                                    household = household + revenueProductYears.getHouseholds();

                                    freqUnit = revenueProductYears.getHarvestFrequencyUnit();
                                    quaUnit = revenueProductYears.getQuantityUnit();
                                    priceCurrency = revenueProductYears.getMarketPriceCurrency();

                                    if (revenueProductYears.getHarvestFrequencyUnit() != previousFrequencyUnit) {
                                        harvestFreTemp = (freqUnit / previousFreqTemp) * revenueProductYears.getHarvestFrequencyValue();
                                        freqUnit = previousFrequencyUnit;
                                    } else {
                                        harvestFreTemp = revenueProductYears.getHarvestFrequencyValue();
                                    }
                                    harvestFre += harvestFreTemp;

                                    eleCount++;
                                }
                            }

                            harvestFre = harvestFre / eleCount;
                            harvestTimes = harvestTimes / eleCount;
                            harvestPrice = harvestPrice / eleCount;
                            harvestArea = harvestArea / eleCount;
                            household = household / eleCount;
                            harvestFreDisp = harvestFre;
                        }
                    }

                    if (freqUnit == 1 && !currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                        harvestFre = 0;
                    }

                    for (RevenueProductYears revenueProductYears : revenueProduct4.getRevenueProductYearses()) {
                        if (revenueProductYears.getYear() == 0) {
                            //realm.beginTransaction();
                            revenueProductYears.setHarvestFrequencyValue(harvestFre);
                            revenueProductYears.setHarvestFrequencyUnit(freqUnit);
                            revenueProductYears.setQuantityValue(harvestTimes);
                            revenueProductYears.setQuantityUnit(quaUnit);
                            revenueProductYears.setMarketPriceValue(harvestPrice);
                            revenueProductYears.setMarketPriceCurrency(priceCurrency);
                            revenueProductYears.setProjectedIndex(0);
                            revenueProductYears.setSubtotal(0);
                            revenueProductYears.setHouseholds(household);
                            revenueProductYears.setHarvestArea(harvestArea);
                            mHarvestArea = harvestArea;
                            mHarvestFre = harvestFre;
                            mFreqUnit = freqUnit;
                            mHarvestTimes = harvestTimes;
                            mQuaUnit = quaUnit;
                            mMarketPriceValue = harvestPrice;
                            mPriceCurrency = priceCurrency;
                            mHousehold = household;
                           /* if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                                revenueProductYears.setHarvestArea(harvestArea);
                                mHarvestArea = harvestArea;
                            }*/
                            //realm.commitTransaction();
//                        Log.e("REV ",revenueProductYears.toString());
                        }
                        if (revenueProductYears.getProjectedIndex() > 0) {
                            BigDecimal bigDecimalPowerFactor = new BigDecimal(Math.pow((1 + inflationRate), revenueProductYears.getProjectedIndex()), MathContext.DECIMAL64);
                            BigDecimal bigDecimalHarvestPrice = new BigDecimal(harvestPrice);

                            double marketPriceVal = bigDecimalPowerFactor.multiply(bigDecimalHarvestPrice, MathContext.DECIMAL64).doubleValue();

                            // double marketPriceVal = harvestPrice * Math.pow((1 + inflationRate), revenueProductYears.getProjectedIndex());
                            //marketPriceVal = roundToTwoDecimal(marketPriceVal);


                            BigDecimal bigDecimalfreqUnit = new BigDecimal(freqUnit);
                            if (!currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                                if (freqUnit == 2) {
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

                            if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                                BigDecimal bigDecimal12 = new BigDecimal("12");
                                BigDecimal bigDecimalNewHarvestArea = bigDecimalharvestFre.divide(bigDecimal12, MathContext.DECIMAL64);
                                totalVal = bigDecimalNewHarvestArea.multiply(bigDecimalharvestTimes, MathContext.DECIMAL64)
                                        .multiply(bigDecimalmarketPriceVal, MathContext.DECIMAL64)
                                        .multiply(bigDecimalHousehold, MathContext.DECIMAL64)
                                        .doubleValue();
                            }

                            //realm.beginTransaction();
                            revenueProductYears.setHarvestFrequencyValue(harvestFre);
                            revenueProductYears.setHarvestFrequencyUnit(freqUnit);
                            revenueProductYears.setQuantityValue(harvestTimes);
                            revenueProductYears.setQuantityUnit(quaUnit);
                            revenueProductYears.setMarketPriceValue(marketPriceVal);
                            revenueProductYears.setMarketPriceCurrency(priceCurrency);
                            revenueProductYears.setHouseholds(household);
                            revenueProductYears.setSubtotal(totalVal);
                            revenueProductYears.setHarvestArea(harvestArea);

                           /* if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                                revenueProductYears.setHarvestArea(harvestArea);
                            }*/
                            //realm.commitTransaction();
                            Log.e("REV ", revenueProductYears.toString());
                        }
                    }

                } else
                    isTrend = false;
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (currentYearIndex == 0 && currentCostProductIndex <= totalCostProductCount
                        && currentCostProductIndex != 0 && !nextProduct) {
                    Log.e("REQQQQQQ", "REEEEEEE");
                    buttonNext.setClickable(true);
                   /* if(currentCostProductIndex < totalCostProductCount)
                        showTrendDialog(revenueProducts.get(currentCostProductIndex));
                    else*/
                    showTrendDialog(revenueProducts.get(currentCostProductIndexSave));
                } else if (currentCostProductIndex < totalCostProductCount) {
                    loadRevenueProduct(revenueProducts.get(currentCostProductIndex));
                    //currentCostProductIndex++;
                    nextProduct = false;
                    buttonNext.setClickable(true);

                } else {
                    Log.e("revenue", revenueProducts.toString());
                    nextProduct = false;
                    Toast.makeText(context, getResources().getText(R.string.completed_text), Toast.LENGTH_SHORT).show();
                    allCashFlow();
                    calculateNPV();
                    Intent intent = new Intent(getApplicationContext(), NaturalCapitalCostActivityA.class);
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
    }

    @Override
    protected void onResume() {
        setNav();
        super.onResume();
        buttonNext.setClickable(true);
    }

    public void allCashFlow() {
        Survey results = realm.where(Survey.class)
                .equalTo("surveyId", surveyId)
                .findFirst();
        RealmList<CashFlow> cashFlows = new RealmList<>();
        for (LandKind landKind : results.getLandKinds()) {

            double discRate;
            int year = 0;
            CashFlow cashFlowTemp = null;

            if (landKind.getName().equals(getString(R.string.string_forestland)) && currentSocialCapitalServey.equals(getString(R.string.string_forestland))) {

                discRate = landKind.getSocialCapitals().isDiscountFlag() ? landKind.getSocialCapitals().getDiscountRateOverride() : landKind.getSocialCapitals().getDiscountRate();

                // Log.e("DIS RATE ", landKind.getSocialCapitals().getDiscountRate()+"");
                int k = 0;
                for (RevenueProduct revenueProduct : landKind.getForestLand().getRevenueProducts()) {

                    for (RevenueProductYears revenueProductYears00 : revenueProduct.getRevenueProductYearses()) {
                        Log.e("RPPPPPPP ", revenueProductYears00 + "");
                    }
                    if (k <= 0) {
                        for (RevenueProductYears revenueProductYears : revenueProduct.getRevenueProductYearses()) {
                            cashFlowTemp = calculateCashFlow(getString(R.string.string_forestland), revenueProductYears.getYear(), discRate);
                            cashFlows.add(cashFlowTemp);
                            year = revenueProductYears.getYear();

                        }
                        cashFlows.add(calculateTerminalValue(getString(R.string.string_forestland), year, cashFlowTemp, discRate));
                    }
                    k++;
                }
                realm.beginTransaction();
                landKind.getForestLand().setCashFlows(cashFlows);
                realm.commitTransaction();
            } else if (landKind.getName().equals(getString(R.string.string_cropland)) && currentSocialCapitalServey.equals(getString(R.string.string_cropland))) {

                int k = 0;
                discRate = landKind.getSocialCapitals().isDiscountFlag() ? landKind.getSocialCapitals().getDiscountRateOverride() : landKind.getSocialCapitals().getDiscountRate();

                for (RevenueProduct revenueProduct : landKind.getCropLand().getRevenueProducts()) {
                    for (RevenueProductYears revenueProductYears00 : revenueProduct.getRevenueProductYearses()) {
                        Log.e("RPPPPPPP ", revenueProductYears00 + "");
                    }
                    if (k <= 0) {
                        for (RevenueProductYears revenueProductYears : revenueProduct.getRevenueProductYearses()) {
                            cashFlowTemp = calculateCashFlow(getString(R.string.string_cropland), revenueProductYears.getYear(), discRate);
                            cashFlows.add(cashFlowTemp);
                            year = revenueProductYears.getYear();

                        }
                        cashFlows.add(calculateTerminalValue(getString(R.string.string_cropland), year, cashFlowTemp, discRate));
                    }
                    k++;
                }
                realm.beginTransaction();
                landKind.getCropLand().setCashFlows(cashFlows);
                realm.commitTransaction();
            } else if (landKind.getName().equals(getString(R.string.string_pastureland)) && currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                int k = 0;
                discRate = landKind.getSocialCapitals().isDiscountFlag() ? landKind.getSocialCapitals().getDiscountRateOverride() : landKind.getSocialCapitals().getDiscountRate();

                for (RevenueProduct revenueProduct : landKind.getPastureLand().getRevenueProducts()) {
                    for (RevenueProductYears revenueProductYears00 : revenueProduct.getRevenueProductYearses()) {
                        Log.e("RPPPPPPP ", revenueProductYears00 + "");
                    }
                    if (k <= 0) {
                        for (RevenueProductYears revenueProductYears : revenueProduct.getRevenueProductYearses()) {
                            cashFlowTemp = calculateCashFlow(getString(R.string.string_pastureland), revenueProductYears.getYear(), discRate);
                            cashFlows.add(cashFlowTemp);
                            year = revenueProductYears.getYear();

                        }
                        cashFlows.add(calculateTerminalValue(getString(R.string.string_pastureland), year, cashFlowTemp, discRate));
                    }
                    k++;
                }
                realm.beginTransaction();
                landKind.getPastureLand().setCashFlows(cashFlows);
                realm.commitTransaction();
            } else if (landKind.getName().equals(getString(R.string.string_miningland)) && currentSocialCapitalServey.equals(getString(R.string.string_miningland))) {
                int k = 0;
                discRate = landKind.getSocialCapitals().isDiscountFlag() ? landKind.getSocialCapitals().getDiscountRateOverride() : landKind.getSocialCapitals().getDiscountRate();

                for (RevenueProduct revenueProduct : landKind.getMiningLand().getRevenueProducts()) {
                    for (RevenueProductYears revenueProductYears00 : revenueProduct.getRevenueProductYearses()) {
                        Log.e("RPPPPPPP ", revenueProductYears00 + "");
                    }
                    if (k <= 0) {
                        for (RevenueProductYears revenueProductYears : revenueProduct.getRevenueProductYearses()) {
                            cashFlowTemp = calculateCashFlow(getString(R.string.string_miningland), revenueProductYears.getYear(), discRate);
                            cashFlows.add(cashFlowTemp);
                            year = revenueProductYears.getYear();
                        }
                        cashFlows.add(calculateTerminalValue(getString(R.string.string_miningland), year, cashFlowTemp, discRate));
                    }
                    k++;
                }
                realm.beginTransaction();
                landKind.getMiningLand().setCashFlows(cashFlows);
                realm.commitTransaction();
            }
        }
    }

    private CashFlow calculateTerminalValue(String landKind, int year, CashFlow cashFlowTemp, double discountRateOverride) {

        RevenueProductYears revenueProductYears = realm.where(RevenueProductYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findFirst();

        CostElementYears costElementYears = realm.where(CostElementYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findFirst();

        RealmResults<RevenueProductYears> revenueProductYearses = realm.where(RevenueProductYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findAll();

        RealmResults<CostElementYears> costElementYearses = realm.where(CostElementYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findAll();

        RealmResults<OutlayYears> outlayYearses = realm.where(OutlayYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findAll();

        double disRate = discountRateOverride / 100;
        double revenueTotal = 0;
        double costTotal = 0;
        double outlayTotal = 0;

        double disFactor = 0;
        double terminalValue = 0;

        BigDecimal bigDecimalOne = new BigDecimal("1");

        for (RevenueProductYears revenueProductYears1 : revenueProductYearses) {
            revenueTotal = revenueTotal + revenueProductYears1.getSubtotal();

            double powerFactor = Math.pow(1 + disRate, revenueProductYears.getProjectedIndex());
            BigDecimal bigDecimalPowerFactor = new BigDecimal(powerFactor);
            BigDecimal bigDecimalDisFactor = bigDecimalOne.divide(bigDecimalPowerFactor, MathContext.DECIMAL64);

            disFactor = bigDecimalDisFactor.doubleValue();
        }

        for (CostElementYears costElementYears1 : costElementYearses) {
            costTotal = costTotal + costElementYears1.getSubtotal();

            double powerFactor = Math.pow(1 + disRate, costElementYears.getProjectedIndex());
            BigDecimal bigDecimalPowerFactor = new BigDecimal(powerFactor);
            BigDecimal bigDecimalDisFactor = bigDecimalOne.divide(bigDecimalPowerFactor, MathContext.DECIMAL64);

            disFactor = bigDecimalDisFactor.doubleValue();
        }

        for (OutlayYears outlayYears1 : outlayYearses) {
            outlayTotal = outlayTotal + outlayYears1.getPrice();
        }

        terminalValue = disRate != 0 ? (cashFlowTemp.getValue() / (disRate)) : 0;

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

    public CashFlow calculateCashFlow(String landKind, int year, double disRatePersent) {
        RevenueProductYears revenueProductYears = realm.where(RevenueProductYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findFirst();

        CostElementYears costElementYears = realm.where(CostElementYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findFirst();

        OutlayYears outlayYears = realm.where(OutlayYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findFirst();

        RealmResults<RevenueProductYears> revenueProductYearses = realm.where(RevenueProductYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findAll();

        RealmResults<CostElementYears> costElementYearses = realm.where(CostElementYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findAll();

        RealmResults<OutlayYears> outlayYearses = realm.where(OutlayYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findAll();

        double revenueTotal = 0;
        double costTotal = 0;
        double outlayTotal = 0;
        double disRate = disRatePersent / 100;

        double disFactor = 0;

        BigDecimal bigDecimalOne = new BigDecimal("1");

        for (RevenueProductYears revenueProductYears1 : revenueProductYearses) {
            revenueTotal = revenueTotal + revenueProductYears1.getSubtotal();

            double powerFactor = Math.pow(1 + disRate, revenueProductYears.getProjectedIndex());
            BigDecimal bigDecimalPowerFactor = new BigDecimal(powerFactor);
            BigDecimal bigDecimalDisFactor = bigDecimalOne.divide(bigDecimalPowerFactor, MathContext.DECIMAL64);

            disFactor = bigDecimalDisFactor.doubleValue();

//            Log.e("PRO IN  ",disRate+" "+revenueProductYears.getProjectedIndex()+"");
        }

        for (CostElementYears costElementYears1 : costElementYearses) {
            costTotal = costTotal + costElementYears1.getSubtotal();

            double powerFactor = Math.pow(1 + disRate, costElementYears.getProjectedIndex());
            BigDecimal bigDecimalPowerFactor = new BigDecimal(powerFactor);
            BigDecimal bigDecimalDisFactor = bigDecimalOne.divide(bigDecimalPowerFactor, MathContext.DECIMAL64);

            disFactor = bigDecimalDisFactor.doubleValue();
//            Log.e("PRO IN  ",disRate+" "+costElementYears.getProjectedIndex()+"");
        }

        for (OutlayYears outlayYears1 : outlayYearses) {
            outlayTotal = outlayTotal + outlayYears1.getPrice();
        }

        double cashFlowVal = revenueTotal - costTotal - outlayTotal;

        Log.e("CASH FLOW CAL ", year + " " + cashFlowVal + " " + revenueTotal + " " + costTotal + " " + outlayTotal);

        double discountedCashFlow = cashFlowVal * disFactor;
        // Log.e("DIS FACT ",disFactor+"");

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

    public void calculateNPV() {
        Survey results = realm.where(Survey.class)
                .equalTo("surveyId", surveyId)
                .findFirst();
        // int year = Calendar.getInstance().get(Calendar.YEAR);
        int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
        for (LandKind landKind : results.getLandKinds()) {
            if (landKind.getName().equals(getString(R.string.string_forestland)) && currentSocialCapitalServey.equals(getString(R.string.string_forestland))) {

                double npv = 0;
                for (CashFlow cashFlow : landKind.getForestLand().getCashFlows()) {
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
                    component.setForestValue(npv);
                    realm.commitTransaction();

                    Log.e("COM ", component.toString());

                    realm.beginTransaction();
                    results.setComponents(component);
                    realm.commitTransaction();
                } else {
                    realm.beginTransaction();
                    results.getComponents().setForestValue(npv);
                    realm.commitTransaction();

                    Log.e("COM ", results.getComponents().toString());
                }
            } else if (landKind.getName().equals(getString(R.string.string_cropland)) && currentSocialCapitalServey.equals(getString(R.string.string_cropland))) {
                double npv = 0;
                for (CashFlow cashFlow : landKind.getCropLand().getCashFlows()) {
                    if (cashFlow.getYear() >= year) {
                        npv = npv + cashFlow.getDiscountedCashFlow();
                    }
                }

                if (results.getComponents() == null) {
                    realm.beginTransaction();
                    Component component = realm.createObject(Component.class);
                    component.setId(getNextKeyComponent());
                    component.setSurveyId(surveyId);
                    component.setCroplandValue(npv);
                    realm.commitTransaction();

                    realm.beginTransaction();
                    results.setComponents(component);
                    realm.commitTransaction();
                } else {
                    realm.beginTransaction();
                    results.getComponents().setCroplandValue(npv);
                    realm.commitTransaction();
                }

            } else if (landKind.getName().equals(getString(R.string.string_pastureland)) && currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                double npv = 0;
                for (CashFlow cashFlow : landKind.getPastureLand().getCashFlows()) {
                    if (cashFlow.getYear() >= year) {
                        npv = npv + cashFlow.getDiscountedCashFlow();
                    }
                }

                if (results.getComponents() == null) {
                    realm.beginTransaction();
                    Component component = realm.createObject(Component.class);
                    component.setId(getNextKeyComponent());
                    component.setSurveyId(surveyId);
                    component.setPastureValue(npv);
                    realm.commitTransaction();

                    realm.beginTransaction();
                    results.setComponents(component);
                    realm.commitTransaction();
                } else {
                    realm.beginTransaction();
                    results.getComponents().setPastureValue(npv);
                    realm.commitTransaction();
                }
            } else if (landKind.getName().equals(getString(R.string.string_miningland)) && currentSocialCapitalServey.equals(getString(R.string.string_miningland))) {
                double npv = 0;
                for (CashFlow cashFlow : landKind.getMiningLand().getCashFlows()) {
                    if (cashFlow.getYear() >= year) {
                        npv = npv + cashFlow.getDiscountedCashFlow();
                    }
                }

                if (results.getComponents() == null) {
                    realm.beginTransaction();
                    Component component = realm.createObject(Component.class);
                    component.setId(getNextKeyComponent());
                    component.setSurveyId(surveyId);
                    component.setMiningLandValue(npv);
                    realm.commitTransaction();

                    realm.beginTransaction();
                    results.setComponents(component);
                    realm.commitTransaction();
                } else {
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