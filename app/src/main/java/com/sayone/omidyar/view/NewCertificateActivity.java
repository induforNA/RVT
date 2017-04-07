package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.CashFlow;
import com.sayone.omidyar.model.Component;
import com.sayone.omidyar.model.CostElement;
import com.sayone.omidyar.model.CostElementYears;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.Outlay;
import com.sayone.omidyar.model.OutlayYears;
import com.sayone.omidyar.model.ParcelLocation;
import com.sayone.omidyar.model.RevenueProduct;
import com.sayone.omidyar.model.RevenueProductYears;
import com.sayone.omidyar.model.SharedCostElement;
import com.sayone.omidyar.model.SharedCostElementYears;
import com.sayone.omidyar.model.SocialCapital;
import com.sayone.omidyar.model.Survey;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class NewCertificateActivity extends BaseActivity implements View.OnClickListener {

    TextView parcelId, community, surveyorName, surveyDate, country, inflationRate, socialCapitalForest, socialCapitalCrop, socialCapitalPasture, socialCapitalMining;
    SharedPreferences sharedPref;
    TextView countryRiskRate;
    TextView headingForest, headingCrop, headingPasture, headingMining;
    TextView gpsCoordinate_1, gpsCoordinate_2, gpsCoordinate_3, gpsCoordinate_4, gpsCoordinate_5, gpsCoordinate_6, parcelSize;
    LinearLayout fullscreen, forestlandLayout, croplandLayout, pasturelandLayout, mininglandLayout;
    GridLayout parcelGridLayout;
    Context context;
    double totalVal = 0;
    double parcelVal = 0;
    float parcelArea = 0;
    TextView forestValue, cropValue, pastureValue, miningValue, totalText, parcelValue;
    private Realm realm;
    private String surveyId;
    private Boolean flag = true;
    private String currentSocialCapitalServey;
    private TextView respondentGroup;

    private String serveyId;
    private SocialCapital socialCapital;
    private TextView forestDiscountRateValue;
    private TextView cropDiscountRateValue;
    private TextView pastureDiscountRateValue;
    private TextView miningDiscountRateValue;
    private String currency;

    //Side Nav
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView surveyIdDrawer;
    private DrawerLayout menuDrawerLayout;
    private TextView textViewAbout;
    private TextView startSurvey;
    private TextView harvestingForestProducts;
    private TextView agriculture;
    private TextView grazing;
    private TextView mining;
    private TextView sharedCostsOutlays;
    private TextView certificate;
    private TextView logout;
    private double mSharedDiscountRate;

    private Button certificateSaveButton;
    private Button certificateExitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_certificate);
        realm = Realm.getDefaultInstance();
        parcelId = (TextView) findViewById(R.id.parcel_id);
        community = (TextView) findViewById(R.id.community_name);
        surveyorName = (TextView) findViewById(R.id.surveyor_name);
        surveyDate = (TextView) findViewById(R.id.survey_date);
        country = (TextView) findViewById(R.id.country);
        inflationRate = (TextView) findViewById(R.id.inflation_rate);
        countryRiskRate = (TextView) findViewById(R.id.country_risk_rate);
        socialCapitalForest = (TextView) findViewById(R.id.forest_social_capital_score);
        socialCapitalCrop = (TextView) findViewById(R.id.crop_social_capital_score);
        socialCapitalPasture = (TextView) findViewById(R.id.pasture_social_capital_score);
        socialCapitalMining = (TextView) findViewById(R.id.mining_social_capital_score);
        forestlandLayout = (LinearLayout) findViewById(R.id.forestland_layout);
        croplandLayout = (LinearLayout) findViewById(R.id.cropland_layout);
        pasturelandLayout = (LinearLayout) findViewById(R.id.pastureland_layout);
        mininglandLayout = (LinearLayout) findViewById(R.id.miningland_layout);
        parcelGridLayout = (GridLayout) findViewById(R.id.parcel_grid_layout);
        headingCrop = (TextView) findViewById(R.id.heading_cropland);
        headingForest = (TextView) findViewById(R.id.heading_forest);
        headingPasture = (TextView) findViewById(R.id.heading_pastureland);
        headingMining = (TextView) findViewById(R.id.heading_miningland);
        fullscreen = (LinearLayout) findViewById(R.id.fullscreen);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        surveyIdDrawer = (TextView) findViewById(R.id.text_view_id);
        forestDiscountRateValue = (TextView) findViewById(R.id.value_discount_rate_forest);
        cropDiscountRateValue = (TextView) findViewById(R.id.value_discount_rate_crop);
        pastureDiscountRateValue = (TextView) findViewById(R.id.value_discount_rate_pasture);
        miningDiscountRateValue = (TextView) findViewById(R.id.value_discount_rate_mining);
        respondentGroup = (TextView) findViewById(R.id.respondent_name);
        forestValue = (TextView) findViewById(R.id.forest_value);
        cropValue = (TextView) findViewById(R.id.crop_value);
        pastureValue = (TextView) findViewById(R.id.pasture_value);
        miningValue = (TextView) findViewById(R.id.mining_value);
        totalText = (TextView) findViewById(R.id.total_text);
        parcelValue = (TextView) findViewById(R.id.parcel_value);
        gpsCoordinate_1 = (TextView) findViewById(R.id.gps_coordinate_1);
        gpsCoordinate_2 = (TextView) findViewById(R.id.gps_coordinate_2);
        gpsCoordinate_3 = (TextView) findViewById(R.id.gps_coordinate_3);
        gpsCoordinate_4 = (TextView) findViewById(R.id.gps_coordinate_4);
        gpsCoordinate_5 = (TextView) findViewById(R.id.gps_coordinate_5);
        gpsCoordinate_6 = (TextView) findViewById(R.id.gps_coordinate_6);
        parcelSize = (TextView) findViewById(R.id.parcel_size);
        certificateExitButton = (Button) findViewById(R.id.certificate_exit_button);
        certificateSaveButton = (Button) findViewById(R.id.certificate_save_button);
        certificateExitButton.setOnClickListener(this);
        certificateSaveButton.setOnClickListener(this);
        imageViewMenuIcon.setOnClickListener(this);
        drawerCloseBtn.setOnClickListener(this);
        context = this;
        totalVal = 0;
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        surveyId = sharedPref.getString("surveyId", "");
        currentSocialCapitalServey = sharedPref.getString("currentSocialCapitalSurvey", "");
        Realm realm = Realm.getDefaultInstance();
        Survey surveyCheck = realm.where(Survey.class)
                .equalTo("surveyId", surveyId)
                .findFirst();

        Log.e("Symbol:", surveyCheck.getCurrency());
        currency = surveyCheck.getCurrency();

        Log.e("Language : ", Locale.getDefault().getDisplayLanguage());

        forestlandLayout.setVisibility(View.GONE);
        croplandLayout.setVisibility(View.GONE);
        pasturelandLayout.setVisibility(View.GONE);
        mininglandLayout.setVisibility(View.GONE);

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
        allCashFlow();
        sharedCashFlow();

        setDatas();
        //GPS Coordinates

    }

    private void setDatas() {
        Survey surveyCheck = realm.where(Survey.class).
                equalTo("surveyId", surveyId)
                .findFirst();

        RealmResults<LandKind> landKinds = realm.where(LandKind.class)
                .equalTo("surveyId", surveyId)
                .equalTo("status", "active")
                .findAll();

        Log.e("CHECK ", surveyCheck.toString());

        Format formatter = new SimpleDateFormat("MMM d, yyyy");
        String s = formatter.format(surveyCheck.getDate());

        ParcelLocation parcelLocation = surveyCheck.getParcelLocations();

        if (parcelLocation != null) {
            String coordinate_1 = parcelLocation.getCoordinateOne().equals("Not Set") ? getString(R.string.text_not_set) :  parcelLocation.getCoordinateOne();
            String coordinate_2 = parcelLocation.getCoordinateTwo().equals("Not Set") ? getString(R.string.text_not_set) :  parcelLocation.getCoordinateTwo();
            String coordinate_3 = parcelLocation.getCoordinateThree().equals("Not Set") ? getString(R.string.text_not_set) :  parcelLocation.getCoordinateThree();
            String coordinate_4 = parcelLocation.getCoordinateFour().equals("Not Set") ? getString(R.string.text_not_set) :  parcelLocation.getCoordinateFour();
            String coordinate_5 = parcelLocation.getCoordinateFive().equals("Not Set") ? getString(R.string.text_not_set) :  parcelLocation.getCoordinateFive();
            String coordinate_6 = parcelLocation.getCoordinateSix().equals("Not Set") ? getString(R.string.text_not_set) :  parcelLocation.getCoordinateSix();
            parcelArea = parcelLocation.getArea();

            gpsCoordinate_1.setText(coordinate_1);
            gpsCoordinate_2.setText(coordinate_2);
            gpsCoordinate_3.setText(coordinate_3);
            gpsCoordinate_4.setText(coordinate_4);
            gpsCoordinate_5.setText(coordinate_5);
            gpsCoordinate_6.setText(coordinate_6);
            if(parcelArea != 0)
                parcelSize.setText(Float.toString(parcelArea) + " ha");
            else
                parcelSize.setText(getString(R.string.text_not_available));
        }


        for (LandKind landKind : landKinds) {
            if (landKind.getName().equals(getString(R.string.string_forestland)) && landKind.getStatus().equals("active")) {
                forestlandLayout.setVisibility(View.VISIBLE);
                socialCapital = landKind.getSocialCapitals();
                if (socialCapital.isDiscountFlag()) {
                    forestDiscountRateValue.setText(String.valueOf(roundTwo(socialCapital.getDiscountRateOverride())) + "%");
                } else {
                    forestDiscountRateValue.setText(String.valueOf(roundTwo(socialCapital.getDiscountRate())) + "%");
                }
                socialCapitalForest.setText("" + landKind.getSocialCapitals().getScore() + "/20");

                if (surveyCheck.getComponents() != null) {
                    realm.beginTransaction();
                    surveyCheck.getComponents().setForestSocialCapitalScore(landKind.getSocialCapitals().getScore());
                    realm.commitTransaction();
                }

                if (surveyCheck.getComponents() != null) {
                    if (surveyCheck.getComponents().getForestValue() != 0) {
                        DecimalFormat valueFormatter = new DecimalFormat("#,###,###");
                        if (!(surveyCheck.getComponents().getForestValue() < 0)) {
                            String yourFormattedString = valueFormatter.format(roundTwo(surveyCheck.getComponents().getForestValue()));
                            forestValue.setText(yourFormattedString + " " + currency);
                        } else {
                            double value = (surveyCheck.getComponents().getForestValue());
                            String yourFormattedString = valueFormatter.format(roundTwo(value) * -1);
                            forestValue.setText("- " + yourFormattedString + " " + currency);
                        }

                        //forestValue.setText(roundTwo(surveyCheck.getComponents().getForestValue())+"");
                        totalVal = totalVal + surveyCheck.getComponents().getForestValue();
                    } else {
                        forestValue.setText("0");
                    }
                } else {
                    forestValue.setText("0");
                }
            }

            if (landKind.getName().equals(getString(R.string.string_cropland))  && landKind.getStatus().equals("active")) {
                croplandLayout.setVisibility(View.VISIBLE);
                socialCapital = landKind.getSocialCapitals();
                if (socialCapital.isDiscountFlag()) {
                    cropDiscountRateValue.setText(String.valueOf(roundTwo(socialCapital.getDiscountRateOverride())) + "%");
                } else {
                    cropDiscountRateValue.setText(String.valueOf(roundTwo(socialCapital.getDiscountRate())) + "%");
                }
                socialCapitalCrop.setText("" + landKind.getSocialCapitals().getScore() + "/20");

                if (surveyCheck.getComponents() != null) {
                    realm.beginTransaction();
                    surveyCheck.getComponents().setCroplandSocialCapitalScore(landKind.getSocialCapitals().getScore());
                    realm.commitTransaction();
                }

                if (surveyCheck.getComponents() != null) {
                    if (surveyCheck.getComponents().getCroplandValue() != 0) {
                        DecimalFormat valueFormatter = new DecimalFormat("#,###,###");
                        if (!(surveyCheck.getComponents().getCroplandValue() < 0)) {
                            String yourFormattedString = valueFormatter.format(roundTwo(surveyCheck.getComponents().getCroplandValue()));
                            cropValue.setText(yourFormattedString + " " + currency);
                        } else {
                            double value = surveyCheck.getComponents().getCroplandValue();
                            String yourFormattedString = valueFormatter.format(roundTwo(value) * -1);
                            cropValue.setText("- " + yourFormattedString + " " + currency);
                        }

                        totalVal = totalVal + surveyCheck.getComponents().getCroplandValue();
                    } else {
                        cropValue.setText("0");
                    }
                } else {
                    cropValue.setText("0");
                }
            }

            if (landKind.getName().equals(getString(R.string.string_pastureland))  && landKind.getStatus().equals("active")) {
                pasturelandLayout.setVisibility(View.VISIBLE);
                socialCapital = landKind.getSocialCapitals();
                if (socialCapital.isDiscountFlag()) {
                    pastureDiscountRateValue.setText(String.valueOf(roundTwo(socialCapital.getDiscountRateOverride())) + "%");
                } else {
                    pastureDiscountRateValue.setText(String.valueOf(roundTwo(socialCapital.getDiscountRate())) + "%");
                }
                socialCapitalPasture.setText("" + landKind.getSocialCapitals().getScore() + "/20");

                if (surveyCheck.getComponents() != null) {
                    realm.beginTransaction();
                    surveyCheck.getComponents().setPastureSocialCapitalScore(landKind.getSocialCapitals().getScore());
                    realm.commitTransaction();
                }

                if (surveyCheck.getComponents() != null) {
                    if (surveyCheck.getComponents().getPastureValue() != 0) {
                        DecimalFormat valueFormatter = new DecimalFormat("#,###,###");
                        if (!(surveyCheck.getComponents().getPastureValue() < 0)) {
                            String yourFormattedString = valueFormatter.format(roundTwo(surveyCheck.getComponents().getPastureValue()));
                            pastureValue.setText(yourFormattedString + " " + currency);
                        } else {
                            double value = surveyCheck.getComponents().getPastureValue();
                            String yourFormattedString = valueFormatter.format(roundTwo(value) * -1);
                            pastureValue.setText("- " + yourFormattedString + " " + currency);
                        }


                        totalVal = totalVal + surveyCheck.getComponents().getPastureValue();
                    } else {
                        pastureValue.setText("0");
                    }
                } else {
                    pastureValue.setText("0");
                }
            }

            if (landKind.getName().equals(getString(R.string.string_miningland)) && landKind.getStatus().equals("active")) {
                mininglandLayout.setVisibility(View.VISIBLE);
                socialCapital = landKind.getSocialCapitals();
                if (socialCapital.isDiscountFlag()) {
                    miningDiscountRateValue.setText(String.valueOf(roundTwo(socialCapital.getDiscountRateOverride())) + "%");
                } else {
                    miningDiscountRateValue.setText(String.valueOf(roundTwo(socialCapital.getDiscountRate())) + "%");
                }

                socialCapitalMining.setText("" + landKind.getSocialCapitals().getScore() + "/20");

                if (surveyCheck.getComponents() != null) {
                    realm.beginTransaction();
                    surveyCheck.getComponents().setMiningSocialCapitalScore(landKind.getSocialCapitals().getScore());
                    realm.commitTransaction();
                }

                if (surveyCheck.getComponents() != null) {
                    if (surveyCheck.getComponents().getMiningLandValue() != 0) {
                        DecimalFormat valueFormatter = new DecimalFormat("#,###,###");
                        if (!(surveyCheck.getComponents().getMiningLandValue() < 0)) {
                            String yourFormattedString = valueFormatter.format(roundTwo(surveyCheck.getComponents().getMiningLandValue()));
                            miningValue.setText(yourFormattedString + " " + currency);
                        } else {
                            double value = surveyCheck.getComponents().getMiningLandValue();
                            String yourFormattedString = valueFormatter.format(roundTwo(value) * -1);
                            miningValue.setText("- " + yourFormattedString + " " + currency);
                        }

                        totalVal = totalVal + surveyCheck.getComponents().getMiningLandValue();
                    } else {
                        miningValue.setText("0");
                    }
                } else {
                    miningValue.setText("0");
                }
            }
        }

        if (forestlandLayout.getVisibility() == View.GONE) {
            parcelGridLayout.removeView(forestlandLayout);
        }
        if (croplandLayout.getVisibility() == View.GONE) {
            parcelGridLayout.removeView(croplandLayout);
        }
        if (pasturelandLayout.getVisibility() == View.GONE) {
            parcelGridLayout.removeView(pasturelandLayout);
        }
        if (mininglandLayout.getVisibility() == View.GONE) {
            parcelGridLayout.removeView(mininglandLayout);
        }

        forestlandLayout.setMinimumWidth(500);
        croplandLayout.setMinimumWidth(500);
        pasturelandLayout.setMinimumWidth(500);
        mininglandLayout.setMinimumWidth(500);

        community.setText(surveyCheck.getCommunity().toString());
        parcelId.setText(surveyCheck.getSurveyId().toString());
        surveyorName.setText(surveyCheck.getSurveyor().toString());
        respondentGroup.setText(surveyCheck.getRespondentGroup().toString());
        country.setText(surveyCheck.getCountry().toString());
        surveyDate.setText(s);
        surveyIdDrawer.setText(surveyId);
        DecimalFormat valueFormatter = new DecimalFormat("#,###,###");
        if (surveyCheck.getComponents() != null)
            totalVal = totalVal + surveyCheck.getComponents().getSharedCostValue();


        if (totalVal > 500) {
            if (totalVal >= 1000) {
                totalVal = (Math.round(totalVal / 1000)) * 1000;
            } else totalVal = 1000;
        } else if(totalVal < 0) {
            totalVal =  (Math.round(totalVal / 1000)) * 1000;
        }
        int parcel = parcelArea > 0 ? (int) parcelArea : 1;
        parcelVal = totalVal / parcel;
        parcelVal = Math.round(parcelVal);

        String totalValStr = formattedString(Long.valueOf(String.valueOf((long) totalVal)));
        String totalValStrToSend = formattedStringNoSymbol(Long.valueOf(String.valueOf((long) totalVal)));
        String totalValPerHaStrToSend = formattedStringNoSymbol(Long.valueOf(String.valueOf((long) parcelVal)));

        Log.e("Final Value ", totalValStr);

        if (surveyCheck.getComponents() != null) {
            realm.beginTransaction();
            surveyCheck.getComponents().setTotalValueStr(totalValStrToSend);
            surveyCheck.getComponents().setTotalValuePerHa(totalValPerHaStrToSend);
            realm.commitTransaction();
        }

        totalText.setText(totalValStr);

        double inflation = surveyCheck.getOverRideInflationRate() == 0 ? surveyCheck.getInflationRate() : surveyCheck.getOverRideInflationRate();
        double sovereign = surveyCheck.getOverRideRiskRate() == 0 ? surveyCheck.getRiskRate() : surveyCheck.getOverRideRiskRate();

        inflationRate.setText(inflation + "%");
        countryRiskRate.setText(sovereign + "%");

        String parcelValStr =  parcelArea != 0 ? formattedString(Long.valueOf(String.valueOf((long) parcelVal))) : getString(R.string.text_not_available);
        parcelValue.setText(parcelValStr);
    }

    @Override
    protected void onResume() {
        setNav();
        super.onResume();
    }

    public String formattedString(long l) {
        DecimalFormat valueFormatter1 = new DecimalFormat("#,###,###");

        String fString = "0";
        if (l < 0) {
            fString = "- " + valueFormatter1.format(l * -1) + " " + currency;
        } else {
            fString = " " + valueFormatter1.format(l) + " " + currency;
        }
        return fString;
    }

    public String formattedStringNoSymbol(long l) {
        DecimalFormat valueFormatter1 = new DecimalFormat("#,###,###");

        String fString = "0";
        if (l < 0) {
            fString = "- " + valueFormatter1.format(l * -1);
        } else {
            fString = "" + valueFormatter1.format(l);
        }
        return fString;
    }

    public double roundTwo(double val) {
        val = val * 100;
        val = Math.round(val);
        val = val / 100;
        return val;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            case R.id.logout:
                Locale myLocale1 = new Locale(Locale.getDefault().getDisplayLanguage());
                Resources res1 = getResources();
                DisplayMetrics dm1 = res1.getDisplayMetrics();
                Configuration conf1 = res1.getConfiguration();
                conf1.locale = myLocale1;
                res1.updateConfiguration(conf1, dm1);

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
            case R.id.certificate_exit_button:
                Locale myLocale = new Locale(Locale.getDefault().getDisplayLanguage());
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);

                Intent logoutIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
                break;
            case R.id.certificate_save_button:
                Intent startSurveyIntent = new Intent(getApplicationContext(), MainActivity.class);
                startSurveyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(startSurveyIntent);
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
        Intent intent = new Intent(NewCertificateActivity.this, StartLandTypeActivity.class);
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

    public void toggleMenuDrawer() {
        if (menuDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            menuDrawerLayout.openDrawer(GravityCompat.START);
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

            if (landKind.getStatus().equals("active")) {
                if (landKind.getName().equals(getString(R.string.string_forestland)) && currentSocialCapitalServey.equals(getString(R.string.string_forestland))) {
                    // Log.e("DIS RATE ", landKind.getSocialCapitals().getDiscountRate()+"");
                    int k = 0;
                    discRate = landKind.getSocialCapitals().isDiscountFlag() ? landKind.getSocialCapitals().getDiscountRateOverride() : landKind.getSocialCapitals().getDiscountRate();

                    if (!landKind.getForestLand().getRevenueProducts().isEmpty()) {
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
                    } else if (!landKind.getForestLand().getCostElements().isEmpty()) {
                        for (CostElement costElement : landKind.getForestLand().getCostElements()) {
                            for (CostElementYears costElementYears00 : costElement.getCostElementYearses()) {
                                Log.e("RPPPPPPP ", costElementYears00 + "");
                            }
                            if (k <= 0) {
                                for (CostElementYears costElementYears : costElement.getCostElementYearses()) {
                                    cashFlowTemp = calculateCashFlow(getString(R.string.string_forestland), costElementYears.getYear(), discRate);
                                    cashFlows.add(cashFlowTemp);
                                    year = costElementYears.getYear();

                                }
                                cashFlows.add(calculateTerminalValue(getString(R.string.string_forestland), year, cashFlowTemp, discRate));
                            }
                            k++;
                        }
                    } else if (!landKind.getForestLand().getOutlays().isEmpty()) {
                        for (Outlay outlay : landKind.getForestLand().getOutlays()) {
                            for (OutlayYears outlay1 : outlay.getOutlayYearses()) {
                                Log.e("RPPPPPPP ", outlay1 + "");
                            }
                            if (k <= 0) {
                                for (OutlayYears outlayYears : outlay.getOutlayYearses()) {
                                    cashFlowTemp = calculateCashFlow(getString(R.string.string_forestland), outlayYears.getYear(), discRate);
                                    cashFlows.add(cashFlowTemp);
                                    year = outlayYears.getYear();

                                }
                                cashFlows.add(calculateTerminalValue(getString(R.string.string_forestland), year, cashFlowTemp, discRate));
                            }
                            k++;
                        }
                    }
                    realm.beginTransaction();
                    landKind.getForestLand().setCashFlows(cashFlows);
                    realm.commitTransaction();

                    // Log.e("MM ",landKind.getForestLand().getCashFlows().get(0).toString());
                } else if (landKind.getName().equals(getString(R.string.string_cropland)) && currentSocialCapitalServey.equals(getString(R.string.string_cropland))) {
                    int k = 0;
                    discRate = landKind.getSocialCapitals().isDiscountFlag() ? landKind.getSocialCapitals().getDiscountRateOverride() : landKind.getSocialCapitals().getDiscountRate();

                    if (!landKind.getCropLand().getRevenueProducts().isEmpty()) {
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
                    } else if (!landKind.getCropLand().getCostElements().isEmpty()) {
                        for (CostElement costElement : landKind.getCropLand().getCostElements()) {
                            for (CostElementYears costElementYears00 : costElement.getCostElementYearses()) {
                                Log.e("RPPPPPPP ", costElementYears00 + "");
                            }
                            if (k <= 0) {
                                for (CostElementYears costElementYears : costElement.getCostElementYearses()) {
                                    cashFlowTemp = calculateCashFlow(getString(R.string.string_cropland), costElementYears.getYear(), discRate);
                                    cashFlows.add(cashFlowTemp);
                                    year = costElementYears.getYear();

                                }
                                cashFlows.add(calculateTerminalValue(getString(R.string.string_cropland), year, cashFlowTemp, discRate));
                            }
                            k++;
                        }
                    } else if (!landKind.getCropLand().getOutlays().isEmpty()) {
                        for (Outlay outlay : landKind.getCropLand().getOutlays()) {
                            for (OutlayYears outlay1 : outlay.getOutlayYearses()) {
                                Log.e("RPPPPPPP ", outlay1 + "");
                            }
                            if (k <= 0) {
                                for (OutlayYears outlayYears : outlay.getOutlayYearses()) {
                                    cashFlowTemp = calculateCashFlow(getString(R.string.string_forestland), outlayYears.getYear(), discRate);
                                    cashFlows.add(cashFlowTemp);
                                    year = outlayYears.getYear();

                                }
                                cashFlows.add(calculateTerminalValue(getString(R.string.string_forestland), year, cashFlowTemp, discRate));
                            }
                            k++;
                        }
                    }

                    realm.beginTransaction();
                    landKind.getCropLand().setCashFlows(cashFlows);
                    realm.commitTransaction();
                } else if (landKind.getName().equals(getString(R.string.string_pastureland)) && currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                    int k = 0;
                    discRate = landKind.getSocialCapitals().isDiscountFlag() ? landKind.getSocialCapitals().getDiscountRateOverride() : landKind.getSocialCapitals().getDiscountRate();

                    if (!landKind.getPastureLand().getRevenueProducts().isEmpty()) {
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
                    } else if (!landKind.getPastureLand().getCostElements().isEmpty()) {
                        for (CostElement costElement : landKind.getPastureLand().getCostElements()) {
                            for (CostElementYears costElementYears00 : costElement.getCostElementYearses()) {
                                Log.e("RPPPPPPP ", costElementYears00 + "");
                            }
                            if (k <= 0) {
                                for (CostElementYears costElementYears : costElement.getCostElementYearses()) {
                                    cashFlowTemp = calculateCashFlow(getString(R.string.string_pastureland), costElementYears.getYear(), discRate);
                                    cashFlows.add(cashFlowTemp);
                                    year = costElementYears.getYear();

                                }
                                cashFlows.add(calculateTerminalValue(getString(R.string.string_pastureland), year, cashFlowTemp, discRate));
                            }
                            k++;
                        }
                    } else if (!landKind.getPastureLand().getOutlays().isEmpty()) {
                        for (Outlay outlay : landKind.getPastureLand().getOutlays()) {
                            for (OutlayYears outlay1 : outlay.getOutlayYearses()) {
                                Log.e("RPPPPPPP ", outlay1 + "");
                            }
                            if (k <= 0) {
                                for (OutlayYears outlayYears : outlay.getOutlayYearses()) {
                                    cashFlowTemp = calculateCashFlow(getString(R.string.string_pastureland), outlayYears.getYear(), discRate);
                                    cashFlows.add(cashFlowTemp);
                                    year = outlayYears.getYear();

                                }
                                cashFlows.add(calculateTerminalValue(getString(R.string.string_pastureland), year, cashFlowTemp, discRate));
                            }
                            k++;
                        }
                    }

                    realm.beginTransaction();
                    landKind.getPastureLand().setCashFlows(cashFlows);
                    realm.commitTransaction();
                } else if (landKind.getName().equals(getString(R.string.string_miningland)) && currentSocialCapitalServey.equals(getString(R.string.string_miningland))) {
                    int k = 0;
                    discRate = landKind.getSocialCapitals().isDiscountFlag() ? landKind.getSocialCapitals().getDiscountRateOverride() : landKind.getSocialCapitals().getDiscountRate();

                    if (!landKind.getMiningLand().getRevenueProducts().isEmpty()) {
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
                    } else if (!landKind.getMiningLand().getCostElements().isEmpty()) {
                        for (CostElement costElement : landKind.getMiningLand().getCostElements()) {
                            for (CostElementYears costElementYears00 : costElement.getCostElementYearses()) {
                                Log.e("RPPPPPPP ", costElementYears00 + "");
                            }
                            if (k <= 0) {
                                for (CostElementYears costElementYears : costElement.getCostElementYearses()) {
                                    cashFlowTemp = calculateCashFlow(getString(R.string.string_miningland), costElementYears.getYear(), discRate);
                                    cashFlows.add(cashFlowTemp);
                                    year = costElementYears.getYear();

                                }
                                cashFlows.add(calculateTerminalValue(getString(R.string.string_miningland), year, cashFlowTemp, discRate));
                            }
                            k++;
                        }
                    } else if (!landKind.getMiningLand().getOutlays().isEmpty()) {
                        for (Outlay outlay : landKind.getMiningLand().getOutlays()) {
                            for (OutlayYears outlay1 : outlay.getOutlayYearses()) {
                                Log.e("RPPPPPPP ", outlay1 + "");
                            }
                            if (k <= 0) {
                                for (OutlayYears outlayYears : outlay.getOutlayYearses()) {
                                    cashFlowTemp = calculateCashFlow(getString(R.string.string_miningland), outlayYears.getYear(), discRate);
                                    cashFlows.add(cashFlowTemp);
                                    year = outlayYears.getYear();

                                }
                                cashFlows.add(calculateTerminalValue(getString(R.string.string_miningland), year, cashFlowTemp, discRate));
                            }
                            k++;
                        }
                    }

                    realm.beginTransaction();
                    landKind.getMiningLand().setCashFlows(cashFlows);
                    realm.commitTransaction();
                }
            }
        }
        calculateNPV();
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
        int year1 = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));


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
            if (disFactor == 0)
                disFactor = 1 / Math.pow(1 + disRate, calculateProjectionIndex(year - year1));
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
        int year1 = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));

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
            if (disFactor == 0)
                disFactor = 1 / Math.pow(1 + disRate, calculateProjectionIndex(year - year1));
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

    public void sharedCashFlow() {
        Survey results = realm.where(Survey.class)
                .equalTo("surveyId", surveyId)
                .findFirst();
        RealmList<CashFlow> cashFlows = new RealmList<>();


        int k = 0;
        int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
        CashFlow cashFlowTemp = null;


        if (!results.getSharedCostElements().isEmpty()) {
            for (SharedCostElement costElement : results.getSharedCostElements()) {
                for (SharedCostElementYears costElementYears00 : costElement.getCostElementYearses()) {
                    Log.e("RPPPPPPP ", costElementYears00 + "");
                }
                if (k <= 0) {
                    for (SharedCostElementYears costElementYears : costElement.getCostElementYearses()) {
                        cashFlowTemp = calculateSharedCashFlow("", costElementYears.getYear(), results.getSharedDiscountRate());
                        cashFlows.add(cashFlowTemp);
                        year = costElementYears.getYear();
                        Log.e("SHARED CASHFLOW", cashFlowTemp.toString());

                    }
                    cashFlows.add(calculateSharedTerminalValue("", year, cashFlowTemp, results.getSharedDiscountRate()));
                    Log.e("SHARED TER CASHFLOW", calculateSharedTerminalValue("", year, cashFlowTemp, mSharedDiscountRate).toString());

                }
                k++;
            }
        } else if (!results.getSharedOutlays().isEmpty()) {
            for (Outlay outlay : results.getSharedOutlays()) {
                for (OutlayYears outlay1 : outlay.getOutlayYearses()) {
                    Log.e("RPPPPPPP ", outlay1 + "");
                }
                if (k <= 0) {
                    for (OutlayYears outlayYears : outlay.getOutlayYearses()) {
                        cashFlowTemp = calculateSharedCashFlow("", outlayYears.getYear(), mSharedDiscountRate);
                        cashFlows.add(cashFlowTemp);
                        year = outlayYears.getYear();

                    }
                    cashFlows.add(calculateSharedTerminalValue("", year, cashFlowTemp, mSharedDiscountRate));
                }
                k++;
            }
        }
        realm.beginTransaction();
        results.setSharedCashFlows(cashFlows);
        realm.commitTransaction();
        calculateDiscountRate(results.getLandKinds());
        calculateSharedNPV();
    }

    public CashFlow calculateSharedCashFlow(String landKind, int year, double disRatePersent) {

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
        int year1 = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));


        for (SharedCostElementYears costElementYears1 : costElementYearses) {
            costTotal = costTotal + costElementYears1.getSubtotal();
            disFactor = 1 / Math.pow(1 + disRate, costElementYears.getProjectedIndex());
        }

        for (OutlayYears outlayYears1 : outlayYearses) {
            if (outlayYears1.getYear() == year)
                outlayTotal = outlayTotal + outlayYears1.getPrice();
            if (disFactor == 0)
                disFactor = 1 / Math.pow(1 + disRate, calculateProjectionIndex(year - year1));
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

    private CashFlow calculateSharedTerminalValue(String landKind, int year, CashFlow cashFlowTemp, double discountRateOverride) {
        Survey results = realm.where(Survey.class)
                .equalTo("surveyId", surveyId)
                .findFirst();

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
        int year1 = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));

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
            if (disFactor == 0)
                disFactor = 1 / Math.pow(1 + disRate, calculateProjectionIndex(year - year1));
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

    public void calculateSharedNPV() {
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
        mSharedDiscountRate = (totalVal == 0) ? 0 : (totalValNum / totalVal);
        realm.beginTransaction();
        results.setSharedDiscountRate(mSharedDiscountRate);
        realm.commitTransaction();

    }

    public double calculateProjectionIndex(double val) {
        double resVal = 0;

        // int year = Calendar.getInstance().get(Calendar.YEAR);
        int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
        int i;
        for (i = 0; i <= val; i++) {
            if (i == 0) {
                resVal = 0;
            } else if (year % 4 == 0) {
                BigDecimal bigDecimal1 = new BigDecimal("366.0");
                BigDecimal bigDecimal2 = new BigDecimal("365.0");

                //double aa = 366.0 / 365.0;
                BigDecimal bigDecimal3 = bigDecimal1.divide(bigDecimal2, MathContext.DECIMAL64);
                double aa = bigDecimal3.doubleValue();

                Log.e("PRO IND BB ", aa + "");
                resVal = resVal + aa;
            } else {
                resVal = resVal + 1;
            }
            year++;
        }
        Log.e("PRO IND AA ", resVal + "");
        return resVal;
    }

}
