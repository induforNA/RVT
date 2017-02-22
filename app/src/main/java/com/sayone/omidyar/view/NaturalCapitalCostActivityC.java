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
import com.sayone.omidyar.model.CostElement;
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

public class NaturalCapitalCostActivityC extends BaseActivity implements View.OnClickListener {

    Realm realm;
    SharedPreferences sharedPref;
    String serveyId;
    Context context;
    String currentSocialCapitalServey;

    Spinner spinnerYear, spinnerUnit, spinnerTimePeriod;
    String year, unit, currency, numberTimes, timePeriod;
    Button buttonBack, buttonNext;
    TextView loadQuestions;
    Button saveBtn;

    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView textViewAbout;
    private TextView logout;
    private TextView startSurvey;
    private DrawerLayout menuDrawerLayout;
    private TextView surveyIdDrawer;
    private TextView areaQuestion;
    private TextView landType;
    private EditText areaEdit;

    RealmList<CostElement> revenueProducts;
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
    private TextView householdText;
    private EditText householdEdit;
    private LinearLayout householdContainer;
    int lastYearIndex = 0;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_cost_survey_c);

        context = this;
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = sharedPref.getString("surveyId", "");
        currentSocialCapitalServey = sharedPref.getString("currentSocialCapitalServey", "");

        spinnerYear = (Spinner) findViewById(R.id.spinner_year);
        spinnerUnit = (Spinner) findViewById(R.id.spinner_unit);
        spinnerTimePeriod = (Spinner) findViewById(R.id.spinner_time_period);
        buttonBack = (Button) findViewById(R.id.button_back);
        buttonNext = (Button) findViewById(R.id.button_next);
        loadQuestions = (TextView) findViewById(R.id.load_questions);
        //saveBtn = (Button) findViewById(R.id.save_btn);
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
        textViewAbout = (TextView) findViewById(R.id.text_view_about);
        logout = (TextView) findViewById(R.id.logout);
        startSurvey = (TextView) findViewById(R.id.text_start_survey);
        surveyIdDrawer = (TextView) findViewById(R.id.text_view_id);
        //areaQuestion = (TextView) findViewById(R.id.area_question);
        //areaEdit = (EditText) findViewById(R.id.area_edit);
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
        Survey results = realm.where(Survey.class)
                .equalTo("surveyId", serveyId)
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


//        year_adapter = ArrayAdapter.createFromResource(this,
//                R.array.year_array, android.R.layout.simple_spinner_item);

        //year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        ArrayAdapter<CharSequence> unit_adapter = ArrayAdapter.createFromResource(this,
//                R.array.unit_array, android.R.layout.simple_spinner_item);
//        unit_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        final ArrayAdapter<CharSequence> timePeriod_adapter = ArrayAdapter.createFromResource(this,
//                R.array.time_period_array, android.R.layout.simple_spinner_item);
//        timePeriod_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerYear.setAdapter(year_adapter);
        spinnerUnit.setAdapter(unit_adapter);
        spinnerTimePeriod.setAdapter(timePeriod_adapter);

        if (currentSocialCapitalServey.equals(getString(R.string.string_forestland)))
            landType.setText(getResources().getText(R.string.string_forestland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland)))
            landType.setText(getResources().getText(R.string.string_pastureland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_miningland)))
            landType.setText(getResources().getText(R.string.string_miningland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_cropland)))
            landType.setText(getResources().getText(R.string.title_cropland));
        //  landType.setText(currentSocialCapitalServey);



        for (LandKind landKind : results.getLandKinds()) {
            if (landKind.getName().equals(getString(R.string.string_forestland)) && currentSocialCapitalServey.equals(getString(R.string.string_forestland))) {
//                householdText.setText(getString(R.string.string_household,getString(R.string.text_incur_cost)+landKind.getForestLand().getRevenueProducts().get(currentCostProductIndex).getName()));
                if (landKind.getForestLand().getCostElements().size() > 0) {
                    //loadYears(landKind.getForestLand().getRevenueProducts().get(0).getRevenueProductYearses());
                    revenueProducts = landKind.getForestLand().getCostElements();
                    totalCostProductCount = revenueProducts.size();
                    currentCostProductIndex = 0;
                    currentYearIndex = 0;
                    if (currentCostProductIndex < totalCostProductCount) {
                        productReveneIdCon = landKind.getForestLand().getCostElements().get(currentCostProductIndex).getId();
                        loadRevenueProduct(landKind.getForestLand().getCostElements().get(currentCostProductIndex));
                    }
                }
            } else if (landKind.getName().equals(getString(R.string.string_cropland)) && currentSocialCapitalServey.equals(getString(R.string.string_cropland))) {
//                householdText.setText(getString(R.string.string_household, getString(R.string.text_incur_cost)+landKind.getCropLand().getRevenueProducts().get(currentCostProductIndex).getName()));
                if (landKind.getCropLand().getCostElements().size() > 0) {
                    //loadYears(landKind.getForestLand().getRevenueProducts().get(0).getRevenueProductYearses());
                    revenueProducts = landKind.getCropLand().getCostElements();
                    totalCostProductCount = revenueProducts.size();
                    currentCostProductIndex = 0;
                    currentYearIndex = 0;
                    if (currentCostProductIndex < totalCostProductCount) {
                        productReveneIdCon = landKind.getCropLand().getCostElements().get(currentCostProductIndex).getId();
                        loadRevenueProduct(landKind.getCropLand().getCostElements().get(currentCostProductIndex));
                    }
                }
            } else if (landKind.getName().equals(getString(R.string.string_pastureland)) && currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
//                householdText.setText(getString(R.string.string_household,getString(R.string.text_incur_cost)+landKind.getPastureLand().getRevenueProducts().get(currentCostProductIndex).getName()));

                if (landKind.getPastureLand().getCostElements().size() > 0) {
                    //loadYears(landKind.getForestLand().getRevenueProducts().get(0).getRevenueProductYearses());
                    revenueProducts = landKind.getPastureLand().getCostElements();
                    totalCostProductCount = revenueProducts.size();
                    currentCostProductIndex = 0;
                    currentYearIndex = 0;
                    if (currentCostProductIndex < totalCostProductCount) {
                        productReveneIdCon = landKind.getPastureLand().getCostElements().get(currentCostProductIndex).getId();
                        loadRevenueProduct(landKind.getPastureLand().getCostElements().get(currentCostProductIndex));
                    }
                }
            } else if (landKind.getName().equals(getString(R.string.string_miningland)) && currentSocialCapitalServey.equals(getString(R.string.string_miningland))) {
//                householdText.setText(getString(R.string.string_household,getString(R.string.text_incur_cost)+landKind.getMiningLand().getRevenueProducts().get(currentCostProductIndex).getName()));

                if (landKind.getMiningLand().getCostElements().size() > 0) {
                    //loadYears(landKind.getForestLand().getRevenueProducts().get(0).getRevenueProductYearses());
                    revenueProducts = landKind.getMiningLand().getCostElements();
                    totalCostProductCount = revenueProducts.size();
                    currentCostProductIndex = 0;
                    currentYearIndex = 0;
                    if (currentCostProductIndex < totalCostProductCount) {
                        productReveneIdCon = landKind.getMiningLand().getCostElements().get(currentCostProductIndex).getId();
                        loadRevenueProduct(landKind.getMiningLand().getCostElements().get(currentCostProductIndex));
                    }
                }
            }
        }


        ///year= spinnerYear.getSelectedItem().toString();
        unit = spinnerUnit.getSelectedItem().toString();
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
                year = parent.getItemAtPosition(pos).toString();
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
                //Log.e("Time period ",timePeriod);
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

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            // Log.e("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        // Log.e("CDA", "onBackPressed Called");
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
            for (CostElementYears costElementYearsA : revenueProducts.get(0).getCostElementYearses()) {
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


//            case R.id.save_btn:
//                // saveYearlyDatas(costOutlays.get(currentCostProductIndexSave));
//
//
//                break;

        }
    }

    public int getIndexRevenueProducts(String itemName) {
        for (int i = 0; i < revenueProducts.size(); i++) {
            CostElement costElementGet = revenueProducts.get(i);
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

    public void loadRevenueProduct(CostElement costElementLoad) {
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
        for (CostElementYears costElementYears : costElementLoad.getCostElementYearses()) {
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

    public void loadRevenueYears(CostElementYears costElementYearsLoad, CostElement costElementLoad1) {
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

    public void saveYearlyDatas(final CostElement costElement2) {
        final long costElementId = costElement2.getId();
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle(getResources().getString(R.string.loading));
        progress.setMessage(getResources().getString(R.string.wait_while_loading));
        progress.show();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CostElement revenueProduct2 = realm.where(CostElement.class)
                        .equalTo("id", costElementId)
                        .findFirst();


                // Log.e("REVE PRO ",revenueProduct2.toString()+" YEAR "+spinnerYear.getSelectedItem());
                for (CostElementYears costElementYears1 : revenueProduct2.getCostElementYearses()) {
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


                        if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
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
//                        costElementYears1.setCostPerPeriodValue(Double.parseDouble(quanityEdit.getText().toString()));
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


                for (CostElementYears costElementYears11 : revenueProduct2.getCostElementYearses()) {
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
                CostElement costElement4 = realm.where(CostElement.class)
                        .equalTo("id", revenueProductIdLong)
                        .findFirst();
                double harvestFre = 0;
                double harvestTimes = 0;
                double harvestPrice = 0;
                double household = 0;

                double freqUnit = 0;
                String quaUnit = "";
                String priceCurrency = "";
                if(lastYearIndex++ < totalYearsCount) {
                    if (costElement4.getCostElementYearses().size() > 0) {
                        if (costElement4.getCostElementYearses().get(0).getCostFrequencyUnit() == 0) {
                            for (CostElementYears costElementYears : costElement4.getCostElementYearses()) {
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
                            }
                        } else {
                            int eleCount = 0;
                            for (CostElementYears costElementYears : costElement4.getCostElementYearses()) {
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

                    for (CostElementYears costElementYears : costElement4.getCostElementYearses()) {
                        if (costElementYears.getYear() == 0) {

                            //realm.beginTransaction();
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

                            //realm.commitTransaction();
                        }
                        if (costElementYears.getProjectedIndex() > 0) {
                            BigDecimal bigDecimalPowerFactor = new BigDecimal(Math.pow((1 + inflationRate), costElementYears.getProjectedIndex()));
                            BigDecimal bigDecimalHarvestPrice = new BigDecimal(harvestPrice);
                            BigDecimal bigDecimalHousehold = new BigDecimal(household);

                            double marketPriceVal = bigDecimalHarvestPrice.multiply(bigDecimalPowerFactor, MathContext.DECIMAL64).doubleValue();

                            // double marketPriceVal = harvestPrice * Math.pow((1 + inflationRate), costElementYears.getProjectedIndex());
                            //marketPriceVal = roundToTwoDecimal(marketPriceVal);

                            BigDecimal bigDecimalfreqUnit = new BigDecimal(freqUnit);
                            // if(!currentSocialCapitalServey.equals("Pastureland")){
                            if (freqUnit == 2) {
                                bigDecimalfreqUnit = new BigDecimal(1);
                            }
                            // }


                            BigDecimal bigDecimalharvestFre = new BigDecimal(harvestFre);

                            BigDecimal bigDecimalharvestTimes = new BigDecimal(harvestTimes);
                            BigDecimal bigDecimalmarketPriceVal = new BigDecimal(marketPriceVal);

                            double totalVal = bigDecimalfreqUnit.multiply(bigDecimalharvestFre, MathContext.DECIMAL64)
                                    .multiply(bigDecimalharvestTimes, MathContext.DECIMAL64)
                                    .multiply(bigDecimalHousehold, MathContext.DECIMAL64)
                                    .multiply(bigDecimalmarketPriceVal, MathContext.DECIMAL64).doubleValue();

//                        double totalVal = freqUnit
//                                * harvestFre
//                                * harvestTimes
//                                * marketPriceVal;
                            //totalVal = roundToTwoDecimal(totalVal);

                            //realm.beginTransaction();
                            costElementYears.setCostFrequencyValue((int) harvestFre);
                            costElementYears.setCostFrequencyUnit(freqUnit);
                            costElementYears.setCostPerPeriodValue(harvestTimes);
                            costElementYears.setCostPerPeriodUnit(quaUnit);
                            costElementYears.setCostPerUnitValue(marketPriceVal);
                            costElementYears.setCostPerUnitUnit(priceCurrency);
                            costElementYears.setHouseholds(household);
                            costElementYears.setSubtotal(totalVal);
                            //realm.commitTransaction();
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

                    Intent i = new Intent(NaturalCapitalCostActivityC.this, NaturalCapitalCostOutlay.class);
                    startActivity(i);

                    // nextLandKind();

//                    Intent intent=new Intent(getApplicationContext(),NaturalCapitalCostActivityA.class);
//                    startActivity(intent);
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

    private void showTrendDialog(final CostElement costElement) {

        final Dialog dialog = new Dialog(NaturalCapitalCostActivityC.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
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
        dialogQuestionHouseholds.setText(getString(R.string.text_question_harvest, costElement.getName()));
        dialogQuestionPerHousehold.setText(getString(R.string.text_question_harvest, costElement.getName()));
        dialogQuestionPerUnit.setText(getString(R.string.text_question_harvest, costElement.getName()));
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

                                    if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
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
                                                .multiply(bigDecimalHarvestArea, MathContext.DECIMAL64)
                                                .doubleValue();
                                    }

//                        double totalVal = freqUnit
//                                * harvestFre
//                                * harvestTimes
//                                * marketPriceVal;
                                    //totalVal = roundToTwoDecimal(totalVal);

                                    //realm.beginTransaction();
                                    revenueProductYears.setHarvestFrequencyValue(harvestFre);
                                    revenueProductYears.setHarvestFrequencyUnit(mFreqUnit);
                                    revenueProductYears.setQuantityValue(harvestTimes);
                                    revenueProductYears.setQuantityUnit(mQuaUnit);
                                    revenueProductYears.setMarketPriceValue(marketPriceVal);
                                    revenueProductYears.setMarketPriceCurrency(mPriceCurrency);
                                    revenueProductYears.setHouseholds(household);
                                    revenueProductYears.setSubtotal(totalVal);

                                    if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
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
        super.onResume();
        buttonNext.setClickable(true);
    }

    public void nextLandKind() {
        RealmResults<LandKind> landKindRealmResults = realm.where(LandKind.class)
                .equalTo("surveyId", serveyId)
                .equalTo("status", "active")
                .findAll();
        int j = 0;
        int i = 0;
        for (LandKind landKind : landKindRealmResults) {
            //Log.e("TAG ", landKind.toString());
            //Log.e(TAG, String.valueOf(survey1.getParticipants().size()));
            if (landKind.getName().equals(currentSocialCapitalServey)) {
                j = i + 1;
            }
            i++;
        }

        if (j < i) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("currentSocialCapitalServey", landKindRealmResults.get(j).getName());
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), StartLandTypeActivity.class);
            startActivity(intent);

        }else{
            Intent intent = new Intent(getApplicationContext(),NewCertificateActivity.class);
            startActivity(intent);
        }
    }

    public void allCashFlow() {
        Survey results = realm.where(Survey.class)
                .equalTo("surveyId", serveyId)
                .findFirst();
        RealmList<CashFlow> cashFlows = new RealmList<>();
        for (LandKind landKind : results.getLandKinds()) {

            double  discRate;
            int year = 0;
            CashFlow cashFlowTemp = null;

            if (landKind.getName().equals(getString(R.string.string_forestland)) && currentSocialCapitalServey.equals(getString(R.string.string_forestland))) {
                // Log.e("DIS RATE ", landKind.getSocialCapitals().getDiscountRate()+"");
                int k = 0;
                discRate = landKind.getSocialCapitals().isDiscountFlag() ?  landKind.getSocialCapitals().getDiscountRateOverride() :  landKind.getSocialCapitals().getDiscountRate();
                for (CostElement costElement : landKind.getForestLand().getCostElements()) {
                    for (CostElementYears costElementYears00 : costElement.getCostElementYearses()) {
                        Log.e("RPPPPPPP ", costElementYears00 + "");
                    }
                    if (k <= 0) {
                        for (CostElementYears costElementYears : costElement.getCostElementYearses()) {
                            cashFlowTemp = calculateCashFlow(getString(R.string.string_forestland), costElementYears.getYear(), discRate);
                            cashFlows.add(cashFlowTemp);
                            year =  costElementYears.getYear();

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
                discRate = landKind.getSocialCapitals().isDiscountFlag() ?  landKind.getSocialCapitals().getDiscountRateOverride() :  landKind.getSocialCapitals().getDiscountRate();
                for (CostElement costElement : landKind.getCropLand().getCostElements()) {
                    for (CostElementYears costElementYears00 : costElement.getCostElementYearses()) {
                        Log.e("RPPPPPPP ", costElementYears00 + "");
                    }
                    if (k <= 0) {
                        for (CostElementYears costElementYears : costElement.getCostElementYearses()) {
                            cashFlowTemp = calculateCashFlow(getString(R.string.string_cropland), costElementYears.getYear(), discRate);
                            cashFlows.add(cashFlowTemp);
                            year =  costElementYears.getYear();

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
                discRate = landKind.getSocialCapitals().isDiscountFlag() ?  landKind.getSocialCapitals().getDiscountRateOverride() :  landKind.getSocialCapitals().getDiscountRate();
                for (CostElement costElement : landKind.getPastureLand().getCostElements()) {
                    for (CostElementYears costElementYears00 : costElement.getCostElementYearses()) {
                        Log.e("RPPPPPPP ", costElementYears00 + "");
                    }
                    if (k <= 0) {
                        for (CostElementYears costElementYears : costElement.getCostElementYearses()) {
                            cashFlowTemp = calculateCashFlow(getString(R.string.string_pastureland), costElementYears.getYear(), discRate);
                            cashFlows.add(cashFlowTemp);
                            year =  costElementYears.getYear();
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
                discRate = landKind.getSocialCapitals().isDiscountFlag() ?  landKind.getSocialCapitals().getDiscountRateOverride() :  landKind.getSocialCapitals().getDiscountRate();
                for (CostElement costElement : landKind.getMiningLand().getCostElements()) {
                    for (CostElementYears costElementYears00 : costElement.getCostElementYearses()) {
                        Log.e("RPPPPPPP ", costElementYears00 + "");
                    }
                    if (k <= 0) {
                        for (CostElementYears costElementYears : costElement.getCostElementYearses()) {
                            cashFlowTemp = calculateCashFlow(getString(R.string.string_miningland), costElementYears.getYear(), discRate);
                            cashFlows.add(cashFlowTemp);
                            year =  costElementYears.getYear();
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

    public CashFlow calculateCashFlow(String landKind, int year, double disRatePersent) {
        RevenueProductYears revenueProductYears = realm.where(RevenueProductYears.class)
                .equalTo("surveyId", serveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findFirst();

        CostElementYears costElementYears = realm.where(CostElementYears.class)
                .equalTo("surveyId", serveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findFirst();

        OutlayYears outlayYears = realm.where(OutlayYears.class)
                .equalTo("surveyId", serveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findFirst();

        RealmResults<RevenueProductYears> revenueProductYearses = realm.where(RevenueProductYears.class)
                .equalTo("surveyId", serveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findAll();

        RealmResults<CostElementYears> costElementYearses = realm.where(CostElementYears.class)
                .equalTo("surveyId", serveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findAll();

        RealmResults<OutlayYears> outlayYearses = realm.where(OutlayYears.class)
                .equalTo("surveyId", serveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findAll();

        for (RevenueProductYears revenueProductYears1 : revenueProductYearses) {
            //Log.e("Revenue ",revenueProductYears1.toString());
        }

        for (CostElementYears costElementYears1 : costElementYearses) {
            //Log.e("Cost ",costElementYears1.toString());
        }


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

            // disFactor = 1 / Math.pow(1+disRate,revenueProductYears.getProjectedIndex());
        }

        for (CostElementYears costElementYears1 : costElementYearses) {
            costTotal = costTotal + costElementYears1.getSubtotal();

            double powerFactor = Math.pow(1 + disRate, costElementYears.getProjectedIndex());
            BigDecimal bigDecimalPowerFactor = new BigDecimal(powerFactor);
            BigDecimal bigDecimalDisFactor = bigDecimalOne.divide(bigDecimalPowerFactor, MathContext.DECIMAL64);

            disFactor = bigDecimalDisFactor.doubleValue();

            // disFactor = 1 / Math.pow(1+disRate,costElementYears.getProjectedIndex());
        }

        for (OutlayYears outlayYears1 : outlayYearses) {
            outlayTotal = outlayTotal + outlayYears1.getPrice();
        }

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
        //Log.e("CASH FLOW CAL ", year+" "+cashFlowVal+" "+revenueTotal +" "+ costTotal +" "+ outlayTotal);

        double discountedCashFlow = cashFlowVal * disFactor;
        //discountedCashFlow = roundToTwoDecimal(discountedCashFlow);

        // Log.e("DIS FACT ",disFactor+"");

        realm.beginTransaction();
        CashFlow cashFlow = realm.createObject(CashFlow.class);
        cashFlow.setId(getNextKeyCashFlow());
        cashFlow.setSurveyId(serveyId);
        cashFlow.setYear(year);
        cashFlow.setValue(cashFlowVal);
        cashFlow.setDiscountingFactor(disFactor);
        cashFlow.setDiscountedCashFlow(discountedCashFlow);

        cashFlow.setTotalRevenue(revenueTotal);
        cashFlow.setTotalCost(costTotal);
        cashFlow.setTotalOutlay(outlayTotal);
        realm.commitTransaction();

        // Log.e("CASH ",cashFlow.toString()+"");
        return cashFlow;
    }

    private CashFlow calculateTerminalValue(String landKind, int year, CashFlow cashFlowTemp, double discountRateOverride) {

        RevenueProductYears revenueProductYears = realm.where(RevenueProductYears.class)
                .equalTo("surveyId", serveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findFirst();

        CostElementYears costElementYears = realm.where(CostElementYears.class)
                .equalTo("surveyId", serveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findFirst();

        RealmResults<RevenueProductYears> revenueProductYearses = realm.where(RevenueProductYears.class)
                .equalTo("surveyId", serveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findAll();

        RealmResults<CostElementYears> costElementYearses = realm.where(CostElementYears.class)
                .equalTo("surveyId", serveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findAll();

        RealmResults<OutlayYears> outlayYearses = realm.where(OutlayYears.class)
                .equalTo("surveyId", serveyId)
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

        terminalValue =  disRate>inflationRate ? (cashFlowTemp.getValue()/(disRate-inflationRate)) : 0;

        double discountedCashFlow = terminalValue * disFactor;

        realm.beginTransaction();
        CashFlow cashFlow = realm.createObject(CashFlow.class);
        cashFlow.setId(getNextKeyCashFlow());
        cashFlow.setSurveyId(serveyId);
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
                .equalTo("surveyId", serveyId)
                .findFirst();
        // int year = Calendar.getInstance().get(Calendar.YEAR);
        int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
        for (LandKind landKind : results.getLandKinds()) {
            if (landKind.getName().equals(getString(R.string.string_forestland)) && currentSocialCapitalServey.equals(getString(R.string.string_forestland))) {

                double npv = 0;
                for (CashFlow cashFlow : landKind.getForestLand().getCashFlows()) {
                    // Log.e("Cash flow ", cashFlow.toString());
                    if (cashFlow.getYear() >= year) {
                        npv = npv + cashFlow.getDiscountedCashFlow();
                    }
                }

                //npv = roundToTwoDecimal(npv);


                if (results.getComponents() == null) {
                    realm.beginTransaction();
                    Component component = realm.createObject(Component.class);
                    component.setId(getNextKeyComponent());
                    component.setSurveyId(serveyId);
                    component.setForestValue(npv);
                    realm.commitTransaction();

                    realm.beginTransaction();
                    results.setComponents(component);
                    realm.commitTransaction();
                } else {
                    realm.beginTransaction();
                    results.getComponents().setForestValue(npv);
                    realm.commitTransaction();
                }
            } else if (landKind.getName().equals(getString(R.string.string_cropland)) && currentSocialCapitalServey.equals(getString(R.string.string_cropland))) {
                double npv = 0;
                for (CashFlow cashFlow : landKind.getCropLand().getCashFlows()) {
                    if (cashFlow.getYear() >= year) {
                        npv = npv + cashFlow.getDiscountedCashFlow();
                    }
                }

                //npv = roundToTwoDecimal(npv);

                if (results.getComponents() == null) {
                    realm.beginTransaction();
                    Component component = realm.createObject(Component.class);
                    component.setId(getNextKeyComponent());
                    component.setSurveyId(serveyId);
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

                //npv = roundToTwoDecimal(npv);

                if (results.getComponents() == null) {
                    realm.beginTransaction();
                    Component component = realm.createObject(Component.class);
                    component.setId(getNextKeyComponent());
                    component.setSurveyId(serveyId);
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

                //npv = roundToTwoDecimal(npv);

                if (results.getComponents() == null) {
                    realm.beginTransaction();
                    Component component = realm.createObject(Component.class);
                    component.setId(getNextKeyComponent());
                    component.setSurveyId(serveyId);
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