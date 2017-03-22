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
import com.sayone.omidyar.model.Outlay;
import com.sayone.omidyar.model.OutlayYears;
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

import static android.R.layout.simple_spinner_dropdown_item;

/**
 * Created by sayone on 18/10/16.
 */

public class NaturalCapitalCostOutlayB extends BaseActivity {

    private static final int PROJECTION_COUNT = 15;
    Context context;
    Realm realm;
    SharedPreferences sharedPref;

    String surveyId;
    Button buttonBack, buttonNext, buttonSaveNext;
    ImageView buttonAddWood;
    Spinner spinnerOccurence;
    RealmList<Outlay> costOutlays;
    RealmList<Outlay> costOutlaysToSave;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView surveyIdDrawer;
    private EditText frequencyNumber;
    private DrawerLayout menuDrawerLayout;

    double inflationRate = 0.05;

    Button buttonSave;

    Spinner spinnerYear, spinnerItem;

    TextView landType;
    TextView questionRevenue;

    String landKindName;
    String currentSocialCapitalServey;
    private String language;

    ArrayAdapter<String> year_adapter;
    ArrayList<String> yearList;

    ArrayList<String> currencyList;

    ArrayAdapter<String> occurrenceAdapter;

    ArrayAdapter<String> item_adapter;
    ArrayList<String> itemList;
    ArrayList<String> timePeriodList;


    EditText costValue;

    int currentYearIndex, totalYears, currentItemIndex, totalIems;

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
        setContentView(R.layout.activity_natural_capital_cost_outlay_b);

        context = this;
        language = Locale.getDefault().getDisplayLanguage();
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        surveyId = sharedPref.getString("surveyId", "");
        currentSocialCapitalServey = sharedPref.getString("currentSocialCapitalSurvey", "");

        costOutlays = new RealmList<>();
        costOutlaysToSave = new RealmList<>();


        spinnerYear = (Spinner) findViewById(R.id.spinner_year);
        spinnerItem = (Spinner) findViewById(R.id.spinner_item);
        spinnerOccurence = (Spinner) findViewById(R.id.spinner_occurance);

        yearList = new ArrayList<>();
        currencyList = new ArrayList<>();
        itemList = new ArrayList<>();
        timePeriodList = new ArrayList<>();

        year_adapter = new ArrayAdapter<>(this, simple_spinner_dropdown_item, yearList);
        spinnerYear.setAdapter(year_adapter);


        item_adapter = new ArrayAdapter<>(this, simple_spinner_dropdown_item, itemList);
        spinnerItem.setAdapter(item_adapter);

        occurrenceAdapter = new ArrayAdapter<>(this, simple_spinner_dropdown_item, timePeriodList);

//        ArrayAdapter<CharSequence> occurance_adapter = ArrayAdapter.createFromResource(this,
//                R.array.time_period_array, android.R.layout.simple_spinner_item);
        //  occurance_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOccurence.setAdapter(occurrenceAdapter);

        int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
        currentYearIndex = 0;
        totalYears = 0;
        currentItemIndex = 0;
        totalIems = 0;


        currencyList.add("INR");

        Survey survey = realm.where(Survey.class).equalTo("surveyId", surveyId).findFirst();
        for (LandKind landKind : survey.getLandKinds()) {
            if (landKind.getName().equals(getString(R.string.string_forestland)) && currentSocialCapitalServey.equals(getString(R.string.string_forestland))) {
                for (int i = 0; i <= PROJECTION_COUNT; i++) {
                    yearList.add(String.valueOf(year));
                    year++;
                }
                year_adapter.notifyDataSetChanged();
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for (Outlay outlay : landKind.getForestLand().getOutlays()) {
                    costOutlaysToSave.add(outlay);
                    if (outlay.getType().equals("Timber")) {
                        costOutlays.add(outlay);
                    }
                }
            } else if (landKind.getName().equals(getString(R.string.string_cropland)) && currentSocialCapitalServey.equals(getString(R.string.string_cropland))) {
                for (int i = 0; i <= PROJECTION_COUNT; i++) {
                    yearList.add(String.valueOf(year));
                    year++;
                }
                year_adapter.notifyDataSetChanged();
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for (Outlay outlay : landKind.getCropLand().getOutlays()) {
                    costOutlaysToSave.add(outlay);
                    if (outlay.getType().equals("Timber")) {
                        costOutlays.add(outlay);
                    }
                }
            } else if (landKind.getName().equals(getString(R.string.string_pastureland)) && currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                for (int i = 0; i <= PROJECTION_COUNT; i++) {
                    yearList.add(String.valueOf(year));
                    year++;
                }
                year_adapter.notifyDataSetChanged();
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for (Outlay outlay : landKind.getPastureLand().getOutlays()) {
                    costOutlaysToSave.add(outlay);
                    if (outlay.getType().equals("Timber")) {
                        costOutlays.add(outlay);
                    }
                }
            } else if (landKind.getName().equals(getString(R.string.string_miningland)) && currentSocialCapitalServey.equals(getString(R.string.string_miningland))) {
                for (int i = 0; i <= PROJECTION_COUNT; i++) {
                    yearList.add(String.valueOf(year));
                    year++;
                }
                year_adapter.notifyDataSetChanged();
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for (Outlay outlay : landKind.getMiningLand().getOutlays()) {
                    costOutlaysToSave.add(outlay);
                    if (outlay.getType().equals("Timber")) {
                        costOutlays.add(outlay);
                    }
                }
            }
        }

        RealmResults<Frequency> frequencyResult = realm.where(Frequency.class).findAll();
        for (Frequency frequency : frequencyResult) {
            Log.e("HARVEST ", frequency.getHarvestFrequency() + " " + frequency.getFrequencyValue());
            if (language.equals("हिन्दी") || language.equalsIgnoreCase("Hindi")) {
                timePeriodList.add(frequency.getHarvestFrequencyHindi());
            } else {
                timePeriodList.add(frequency.getHarvestFrequency());
            }
        }
        occurrenceAdapter.notifyDataSetChanged();

        for (Outlay outlay : costOutlays) {
            itemList.add(outlay.getItemName());
        }
        item_adapter.notifyDataSetChanged();

        totalYears = yearList.size();
        totalIems = itemList.size();


        buttonBack = (Button) findViewById(R.id.button_back);
        buttonNext = (Button) findViewById(R.id.button_next);
        // buttonSaveNext = (Button) findViewById(R.id.button_save_next);
//        buttonAddWood = (ImageView) findViewById(R.id.button_add_wood);
        landType = (TextView) findViewById(R.id.land_type);
        questionRevenue = (TextView) findViewById(R.id.question_revenue);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        surveyIdDrawer = (TextView) findViewById(R.id.text_view_id);
        buttonSave = (Button) findViewById(R.id.button_save);
        costValue = (EditText) findViewById(R.id.cost_value);
        frequencyNumber = (EditText) findViewById(R.id.frequency_number);

//        if(currentSocialCapitalSurvey.equals("Forestland")){
//            questionRevenue.setText("Add investment/cost outlay items");
//
//        }else if(currentSocialCapitalSurvey.equals(getString(R.string.string_cropland))){
//            questionRevenue.setText("Add investment/cost outlay items");
//
//        }else if(currentSocialCapitalSurvey.equals(getString(R.string.string_pastureland))){
//            questionRevenue.setText("Add investment/cost outlay items");
//
//        }else if(currentSocialCapitalSurvey.equals(getString(R.string.string_miningland))){
//            questionRevenue.setText("Add investment/cost outlay items");
//
//        }

//        landType.setText(currentSocialCapitalSurvey);
        if (currentSocialCapitalServey.equals(getString(R.string.string_forestland)))
            landType.setText(getResources().getText(R.string.string_forestland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland)))
            landType.setText(getResources().getText(R.string.string_pastureland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_miningland)))
            landType.setText(getResources().getText(R.string.string_miningland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_cropland)))
            landType.setText(getResources().getText(R.string.title_cropland));

//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        outlayItemList.setLayoutManager(mLayoutManager);
//        outlayItemList.setItemAnimator(new DefaultItemAnimator());
//        outlayItemList.setAdapter(costOutlayAdapter);


        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
//        buttonAddWood.setOnClickListener(this);
        imageViewMenuIcon.setOnClickListener(this);
        drawerCloseBtn.setOnClickListener(this);
        surveyIdDrawer.setText(surveyId);
        buttonSave.setOnClickListener(this);
        // buttonSaveNext.setOnClickListener(this);


        spinnerItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadDatas();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadDatas();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
    protected void onResume() {
        setNav();
        super.onResume();
    }

    public void findNextData() {
        saveDatas();

//        currentYearIndex++;
//        if(currentYearIndex >= totalYears){
        //currentYearIndex = 0;
        currentItemIndex++;
        if (currentItemIndex >= totalIems) {
            currentItemIndex = 0;
            allCashFlow();
            // Toast.makeText(context,"Completed",Toast.LENGTH_SHORT).show();
        }
//        }
        Log.e("INDEX ", currentYearIndex + " " + currentItemIndex + " " + year_adapter.getCount() + " " + item_adapter.getCount());
//        yearList.size();
//        totalIems = itemList.size();
        //spinnerYear.setSelection(year_adapter.getPosition(yearList.get(currentYearIndex)));
        spinnerItem.setSelection(item_adapter.getPosition(itemList.get(currentItemIndex)));


//        spinnerYear.setSelection(yearList.indexOf(yearList.get(currentYearIndex)));
//        spinnerCurrency.setSelection(yearList.indexOf(yearList.get(currentYearIndex))            currentItemIndex);
    }

    public void findBackData() {
//        currentYearIndex--;
//        if(currentYearIndex < 0){
//            currentYearIndex = totalYears - 1;
        currentItemIndex--;
        if (currentItemIndex < 0) {
            currentItemIndex = totalIems - 1;
            finish();
            // allCashFlow();
            // Toast.makeText(context,"Completed",Toast.LENGTH_SHORT).show();
        }
//        }
        Log.e("INDEX ", currentYearIndex + " " + currentItemIndex + " " + year_adapter.getCount() + " " + item_adapter.getCount());
//        yearList.size();
//        totalIems = itemList.size();
//         setSelection(year_adapter.getPosition(yearList.get(currentYearIndex)));
        spinnerItem.setSelection(item_adapter.getPosition(itemList.get(currentItemIndex)));
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
//            case R.id.button_save_next:
//                findNextData();
//                break;
            case R.id.button_next:
                findNextData();


                break;

            case R.id.button_back:
                findBackData();
                // finish();
                break;

            case R.id.button_save:
                saveDatas();
                Toast.makeText(this, getResources().getString(R.string.text_data_saved), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(NaturalCapitalCostOutlayB.this, StartLandTypeActivity.class);
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
                // Log.e("DIS RATE ", landKind.getSocialCapitals().getDiscountRate()+"");
                int k = 0;
                discRate = landKind.getSocialCapitals().isDiscountFlag() ? landKind.getSocialCapitals().getDiscountRateOverride() : landKind.getSocialCapitals().getDiscountRate();

                for (RevenueProduct revenueProduct : landKind.getForestLand().getRevenueProducts()) {
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

                // Log.e("MM ",landKind.getForestLand().getCashFlows().get(0).toString());
            } else if (landKind.getName().equals(getString(R.string.string_cropland)) && currentSocialCapitalServey.equals(getString(R.string.string_cropland))) {
                int k = 0;
                discRate = landKind.getSocialCapitals().isDiscountFlag() ? landKind.getSocialCapitals().getDiscountRateOverride() : landKind.getSocialCapitals().getDiscountRate();

                for (RevenueProduct revenueProduct : landKind.getCropLand().getRevenueProducts()) {
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
        calculateNPV();
        nextLandKind();
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

        for (RevenueProductYears revenueProductYears1 : revenueProductYearses) {
            revenueTotal = revenueTotal + revenueProductYears1.getSubtotal();
            disFactor = 1 / Math.pow(1 + disRate, revenueProductYears.getProjectedIndex());
        }

        for (CostElementYears costElementYears1 : costElementYearses) {
            costTotal = costTotal + costElementYears1.getSubtotal();
            disFactor = 1 / Math.pow(1 + disRate, costElementYears.getProjectedIndex());
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
//            Log.e("AA  ",""+outlayYears.getPrice());
//        }

//        Log.e("TEST  ",outlayTotal+"");
//        Log.e("DIS FACT ",outlayYears+"");
        double cashFlowVal = revenueTotal - costTotal - outlayTotal;
        //Log.e("cashFlowVal ",cashFlowVal+"");


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
        Survey results = realm.where(Survey.class)
                .equalTo("surveyId", surveyId)
                .findFirst();

        if(results.getOverRideInflationRate() != 0){
            inflationRate = results.getOverRideInflationRate()/100;
        } else if (results.getInflationRate() != 0) {
            inflationRate = results.getInflationRate()/100;
        }

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

    public void calculateNPV() {
        Survey results = realm.where(Survey.class)
                .equalTo("surveyId", surveyId)
                .findFirst();
        int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
        for (LandKind landKind : results.getLandKinds()) {
            if (landKind.getName().equals(getString(R.string.string_forestland)) && currentSocialCapitalServey.equals(getString(R.string.string_forestland))) {

                double npv = 0;
                for (CashFlow cashFlow : landKind.getForestLand().getCashFlows()) {
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

    public int getNextKeyComponent() {
        return realm.where(Component.class).max("id").intValue() + 1;
    }

    public int getNextKeyCashFlow() {
        return realm.where(CashFlow.class).max("id").intValue() + 1;
    }

    private void loadDatas() {
        String itemName = spinnerItem.getSelectedItem().toString();
        int yearVal = Integer.parseInt(spinnerYear.getSelectedItem().toString());

        Outlay outlayResult = realm.where(Outlay.class)
                .equalTo("surveyId", surveyId)
                .equalTo("landKind", currentSocialCapitalServey)
                .equalTo("itemName", itemName)
                .findFirst();
        //Log.e("Save ", outlayResult.toString());
        for (OutlayYears outlayYears : outlayResult.getOutlayYearses()) {
            //Log.e("CHECK ", outlayYears.getYear()+" "+spinnerYear.getSelectedItem().toString());
            if (outlayYears.getYear() == Integer.parseInt(spinnerYear.getSelectedItem().toString())) {
                if (outlayYears.getPrice() != 0) {
                    costValue.setText(outlayYears.getPrice() + "");
                    if (outlayYears.getFrequency() != 0) {
                        frequencyNumber.setText(outlayYears.getFrequency() + "");
                        if (timePeriodList.size() != 0) {
                            // Log.e("TEST FRE ", timePeriod_adapter.getPosition(frequency.getHarvestFrequency())+"");
//                            if(language.equals("हिन्दी")) {
                            spinnerOccurence.setSelection(occurrenceAdapter.getPosition(outlayYears.getTimePeriod()));
//                            }
//                            else{
//                                spinnerTimePeriod.setSelection(occurrenceAdapter.getPosition(outlayYears.getHarvestFrequency()));
//                            }
                        }
                    }
                } else {
                    costValue.setText("");
                    frequencyNumber.setText("");
                }
                spinnerYear.setSelection(year_adapter.getPosition(String.valueOf(yearVal)));
//                realm.beginTransaction();
//                outlayYears.setPrice(Double.parseDouble(costValue.getText().toString()));
//                realm.commitTransaction();
            }
        }

        totalYears = yearList.size();
        totalIems = itemList.size();
    }

    private void saveDatas() {
        //Log.e("Save","DATA");
        Outlay outlayResult = realm.where(Outlay.class)
                .equalTo("surveyId", surveyId)
                .equalTo("landKind", currentSocialCapitalServey)
                .equalTo("itemName", spinnerItem.getSelectedItem().toString())
                .findFirst();
        //Log.e("Save ", outlayResult.toString());
        for (OutlayYears outlayYears : outlayResult.getOutlayYearses()) {
            Log.e("CHECK ", outlayYears.getYear() + " " + spinnerYear.getSelectedItem().toString());
            if (outlayYears.getYear() == Integer.parseInt(spinnerYear.getSelectedItem().toString())) {
                String val = costValue.getText().toString();
                String freq = frequencyNumber.getText().toString();
                String timePeriod = spinnerOccurence.getSelectedItem().toString();
                if (val.equals("")) {
                    val = "0";
                }
                if (freq.equals("")) {
                    freq = "0";
                }
                realm.beginTransaction();
                outlayYears.setPrice(Double.parseDouble(val));
                outlayYears.setFrequency(Double.parseDouble(freq));
                outlayYears.setTimePeriod(timePeriod);
                realm.commitTransaction();

            }
        }

        // Toast.makeText(context,getResources().getText(R.string.text_data_saved),Toast.LENGTH_SHORT).show();
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
            if (landKind.getName().equals(currentSocialCapitalServey)) {
                j = i + 1;
            }
            i++;
        }

        currentYearIndex = totalYears - 1;
        currentItemIndex = totalIems - 1;

        if (j < i) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("currentSocialCapitalSurvey", landKindRealmResults.get(j).getName());
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), StartLandTypeActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), SharedCostSurveyStartActivity.class);
            startActivity(intent);
        }
    }

//    public int getNextKeyRevenueProduct() {
//        return realm.where(RevenueProduct.class).max("id").intValue() + 1;
//    }

    public int getNextKeyOutlay() {
        return realm.where(Outlay.class).max("id").intValue() + 1;
    }

    public void toggleMenuDrawer() {
        if (menuDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            menuDrawerLayout.openDrawer(GravityCompat.START);
        }
    }
}
