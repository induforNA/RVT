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
import com.sayone.omidyar.model.Frequency;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.Outlay;
import com.sayone.omidyar.model.OutlayYears;
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

import static android.R.layout.simple_spinner_dropdown_item;

/**
 * Created by sayone on 18/10/16.
 */

public class NaturalCapitalSharedCostOutlayB extends BaseActivity {

    private static final int PROJECTION_COUNT = 15;
    private Context context;
    private Realm realm;
    private SharedPreferences sharedPref;

    private String surveyId;
    private Button buttonBack, buttonNext;
    private ImageView buttonAddWood;
    private Spinner spinnerOccurance;
    private RealmList<Outlay> costOutlays;
    private RealmList<Outlay> costOutlaysToSave;
    private double inflationRate = 0.05;
    private Button buttonSave;
    private Spinner spinnerYear, spinnerItem;
    private TextView landType;
    private TextView questionRevenue;
    private String landKindName;
    private String currentSocialCapitalServey;
    private ArrayAdapter<String> year_adapter;
    private ArrayList<String> yearList;
    private ArrayList<String> currenyList;
    private ArrayAdapter<String> occuranceAdapter;
    private ArrayAdapter<String> item_adapter;
    private ArrayList<String> itemList;
    private ArrayList<String> timePeriodList;
    private EditText costValue;
    private int currentYearIndex, totalYears, currentItemIndex, totalIems;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView surveyIdDrawer;
    private EditText frequencyNumber;
    private DrawerLayout menuDrawerLayout;
    private String language;
    private Survey survey;
    private double mSharedDiscountRate;

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
        spinnerOccurance = (Spinner) findViewById(R.id.spinner_occurance);

        yearList = new ArrayList<>();
        currenyList = new ArrayList<>();
        itemList = new ArrayList<>();
        timePeriodList = new ArrayList<>();

        year_adapter = new ArrayAdapter<>(this, simple_spinner_dropdown_item, yearList);
        spinnerYear.setAdapter(year_adapter);

        item_adapter = new ArrayAdapter<>(this, simple_spinner_dropdown_item, itemList);
        spinnerItem.setAdapter(item_adapter);

        occuranceAdapter = new ArrayAdapter<>(this, simple_spinner_dropdown_item, timePeriodList);
        spinnerOccurance.setAdapter(occuranceAdapter);

        int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
        currentYearIndex = 0;
        totalYears = 0;
        currentItemIndex = 0;
        totalIems = 0;

        currenyList.add("INR");

        survey = realm.where(Survey.class).equalTo("surveyId", surveyId).findFirst();

        language = survey.getLanguage();

        for (int i = 0; i <= PROJECTION_COUNT; i++) {
            yearList.add(String.valueOf(year));
            year++;
        }
        year_adapter.notifyDataSetChanged();
        for (Outlay outlay : survey.getSharedOutlays()) {
            costOutlaysToSave.add(outlay);
            if (outlay.getType().equals("")) {
                costOutlays.add(outlay);
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
        occuranceAdapter.notifyDataSetChanged();

        for (Outlay outlay : costOutlays) {
            itemList.add(outlay.getItemName());
        }
        item_adapter.notifyDataSetChanged();

        totalYears = yearList.size();
        totalIems = itemList.size();


        buttonBack = (Button) findViewById(R.id.button_back);
        buttonNext = (Button) findViewById(R.id.button_next);
        landType = (TextView) findViewById(R.id.land_type);
        questionRevenue = (TextView) findViewById(R.id.question_revenue);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        surveyIdDrawer = (TextView) findViewById(R.id.text_view_id);
        buttonSave = (Button) findViewById(R.id.button_save);
        costValue = (EditText) findViewById(R.id.cost_value);
        frequencyNumber = (EditText) findViewById(R.id.frequency_number);

        calculateDiscountRate(survey.getLandKinds());

        /*if (currentSocialCapitalServey.equals(getString(R.string.string_forestland)))
            landType.setText(getResources().getText(R.string.string_forestland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland)))
            landType.setText(getResources().getText(R.string.string_pastureland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_miningland)))
            landType.setText(getResources().getText(R.string.string_miningland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_cropland)))
            landType.setText(getResources().getText(R.string.title_cropland));*/

        landType.setText(getResources().getText(R.string.shared_costs_outlays));

        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        imageViewMenuIcon.setOnClickListener(this);
        drawerCloseBtn.setOnClickListener(this);
        surveyIdDrawer.setText(surveyId);
        buttonSave.setOnClickListener(this);


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
        currentItemIndex++;
        if (currentItemIndex >= totalIems) {
            currentItemIndex = 0;
            allCashFlow();
        }
        Log.e("INDEX ", currentYearIndex + " " + currentItemIndex + " " + year_adapter.getCount() + " " + item_adapter.getCount());
        spinnerItem.setSelection(item_adapter.getPosition(itemList.get(currentItemIndex)));
    }

    public void findBackData() {
        currentItemIndex--;
        if (currentItemIndex < 0) {
            currentItemIndex = totalIems - 1;
            finish();
        }
        Log.e("INDEX ", currentYearIndex + " " + currentItemIndex + " " + year_adapter.getCount() + " " + item_adapter.getCount());
        spinnerItem.setSelection(item_adapter.getPosition(itemList.get(currentItemIndex)));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_next:
                findNextData();
                break;

            case R.id.button_back:
                findBackData();
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
        Intent intent = new Intent(NaturalCapitalSharedCostOutlayB.this, StartLandTypeActivity.class);
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

        int k = 0;
        int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
        CashFlow cashFlowTemp = null;


        for (SharedCostElement costElement : results.getSharedCostElements()) {
            for (SharedCostElementYears costElementYears00 : costElement.getCostElementYearses()) {
                Log.e("RPPPPPPP ", costElementYears00 + "");
            }
            if (k <= 0) {
                for (SharedCostElementYears costElementYears : costElement.getCostElementYearses()) {
                    cashFlowTemp = calculateCashFlow("", costElementYears.getYear(), mSharedDiscountRate);
                    cashFlows.add(cashFlowTemp);
                    year = costElementYears.getYear();
                    Log.e("SHARED CASHFLOW", cashFlowTemp.toString());

                }
                cashFlows.add(calculateTerminalValue("", year, cashFlowTemp, mSharedDiscountRate));
                Log.e("SHARED TER CASHFLOW", calculateTerminalValue("", year, cashFlowTemp, mSharedDiscountRate).toString());

            }
            k++;
        }
        realm.beginTransaction();
        results.setSharedCashFlows(cashFlows);
        realm.commitTransaction();
        calculateNPV();
        nextLandKind();
    }

    public CashFlow calculateCashFlow(String landKind, int year, double disRatePersent) {

        SharedCostElementYears costElementYears = realm.where(SharedCostElementYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("landKind", landKind)
                .equalTo("year", year)
                .findFirst();


        RealmResults<SharedCostElementYears> costElementYearses = realm.where(SharedCostElementYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("year", year)
                .findAll();

        RealmResults<OutlayYears> outlayYearses = realm.where(OutlayYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("year", year)
                .equalTo("landKind", "")
                .findAll();

        double revenueTotal = 0;
        double costTotal = 0;
        double outlayTotal = 0;
        double disRate = disRatePersent / 100;

        double disFactor = 0;


        for (SharedCostElementYears costElementYears1 : costElementYearses) {
            costTotal = costTotal + costElementYears1.getSubtotal();
            disFactor = 1 / Math.pow(1 + disRate, costElementYears.getProjectedIndex());
        }

        for (OutlayYears outlayYears1 : outlayYearses) {
            if (outlayYears1.getYear() == year)
                outlayTotal = outlayTotal + outlayYears1.getPrice();
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
        Survey results = realm.where(Survey.class)
                .equalTo("surveyId", surveyId)
                .findFirst();

        if(results.getOverRideInflationRate() != 0){
            inflationRate = results.getOverRideInflationRate()/100;
        } else if (results.getInflationRate() != 0) {
            inflationRate = results.getInflationRate()/100;
        }

        SharedCostElementYears costElementYears = realm.where(SharedCostElementYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("year", year)
                .findFirst();

        RealmResults<SharedCostElementYears> costElementYearses = realm.where(SharedCostElementYears.class)
                .equalTo("surveyId", surveyId)
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

        for (SharedCostElementYears costElementYears1 : costElementYearses) {
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

        double npv = 0;
        for (CashFlow cashFlow : results.getSharedCashFlows()) {
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
        Log.e("SHARED NPV", npv + "");
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
                .equalTo("landKind", "")
                .equalTo("itemName", itemName)
                .findFirst();
        for (OutlayYears outlayYears : outlayResult.getOutlayYearses()) {
            if (outlayYears.getYear() == Integer.parseInt(spinnerYear.getSelectedItem().toString())) {
                if (outlayYears.getPrice() != 0) {
                    costValue.setText(outlayYears.getPrice() + "");
                    if (outlayYears.getFrequency() != 0) {
                        frequencyNumber.setText(outlayYears.getFrequency() + "");
                        if (timePeriodList.size() != 0) {
                            spinnerOccurance.setSelection(occuranceAdapter.getPosition(outlayYears.getTimePeriod()));
                        }
                    }
                } else {
                    costValue.setText("");
                    frequencyNumber.setText("");
                }
                spinnerYear.setSelection(year_adapter.getPosition(String.valueOf(yearVal)));
            }
        }

        totalYears = yearList.size();
        totalIems = itemList.size();
    }

    private void saveDatas() {
        Outlay outlayResult = realm.where(Outlay.class)
                .equalTo("surveyId", surveyId)
                .equalTo("landKind", "")
                .equalTo("itemName", spinnerItem.getSelectedItem().toString())
                .findFirst();
        for (OutlayYears outlayYears : outlayResult.getOutlayYearses()) {
            Log.e("CHECK ", outlayYears.getYear() + " " + spinnerYear.getSelectedItem().toString());
            if (outlayYears.getYear() == Integer.parseInt(spinnerYear.getSelectedItem().toString())) {
                String val = costValue.getText().toString();
                String freq = frequencyNumber.getText().toString();
                String timePeriod = spinnerOccurance.getSelectedItem().toString();
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
    }

    public void nextLandKind() {
        RealmResults<LandKind> landKindRealmResults = realm.where(LandKind.class)
                .equalTo("surveyId", surveyId)
                .equalTo("status", "active")
                .findAll();
        int j = 0;
        int i = 0;
        for (LandKind landKind : landKindRealmResults) {
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
            Intent intent = new Intent(getApplicationContext(), NewCertificateActivity.class);
            startActivity(intent);
        }
    }

    public void toggleMenuDrawer() {
        if (menuDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            menuDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void calculateDiscountRate(RealmList<LandKind> landKinds) {
        Survey results = realm.where(Survey.class)
                .equalTo("surveyId", surveyId)
                .findFirst();
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
                    totalValNum = totalValNum + (survey.getComponents().getForestValue() * landDisc);
                    totalVal = totalVal + survey.getComponents().getForestValue();
                }

                if (landKind.getName().equals(getString(R.string.string_cropland))) {
                    totalValNum = totalValNum + (survey.getComponents().getCroplandValue() * landDisc);
                    totalVal = totalVal + survey.getComponents().getCroplandValue();
                }

                if (landKind.getName().equals(getString(R.string.string_pastureland))) {
                    totalValNum = totalValNum + (survey.getComponents().getPastureValue() * landDisc);
                    totalVal = totalVal + survey.getComponents().getPastureValue();
                }

                if (landKind.getName().equals(getString(R.string.string_miningland))) {
                    totalValNum = totalValNum + (survey.getComponents().getMiningLandValue() * landDisc);
                    totalVal = totalVal + survey.getComponents().getMiningLandValue();
                }
            }
        }
        mSharedDiscountRate = (totalVal == 0) ? 0 : (totalValNum / totalVal);
        realm.beginTransaction();
        results.setSharedDiscountRate(mSharedDiscountRate);
        realm.commitTransaction();

    }
}
