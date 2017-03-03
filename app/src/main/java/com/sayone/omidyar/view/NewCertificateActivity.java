package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.ParcelLocation;
import com.sayone.omidyar.model.SocialCapital;
import com.sayone.omidyar.model.Survey;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

import io.realm.Realm;
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

        Format formatter = new SimpleDateFormat("MMM d, yyyy");
        String s = formatter.format(surveyCheck.getDate());

        RealmResults<LandKind> landKinds = realm.where(LandKind.class)
                .equalTo("surveyId", surveyId)
                .equalTo("status", "active")
                .findAll();

        Log.e("CHECK ", surveyCheck.toString());

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

        //GPS Coordinates
        Survey survey = realm.where(Survey.class).
                equalTo("surveyId", surveyId)
                .findFirst();

        ParcelLocation parcelLocation = survey.getParcelLocations();

        if (parcelLocation != null) {
            String coordinate_1 = parcelLocation.getCoordinateOne();
            String coordinate_2 = parcelLocation.getCoordinateTwo();
            String coordinate_3 = parcelLocation.getCoordinateThree();
            String coordinate_4 = parcelLocation.getCoordinateFour();
            String coordinate_5 = parcelLocation.getCoordinateFive();
            String coordinate_6 = parcelLocation.getCoordinateSix();
            parcelArea = parcelLocation.getArea();

            gpsCoordinate_1.setText(coordinate_1);
            gpsCoordinate_2.setText(coordinate_2);
            gpsCoordinate_3.setText(coordinate_3);
            gpsCoordinate_4.setText(coordinate_4);
            gpsCoordinate_5.setText(coordinate_5);
            gpsCoordinate_6.setText(coordinate_6);
            parcelSize.setText(Float.toString(parcelArea) + "ha");
        }


        for (LandKind landKind : landKinds) {
            if (landKind.getName().equals(getString(R.string.string_forestland))) {
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
                            forestValue.setText(" " + currency + " " + yourFormattedString);
                        } else {
                            double value = (surveyCheck.getComponents().getForestValue());
                            String yourFormattedString = valueFormatter.format(roundTwo(value) * -1);
                            forestValue.setText("- " + currency + " " + yourFormattedString);
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

            if (landKind.getName().equals(getString(R.string.string_cropland))) {
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
                            cropValue.setText(" " + currency + " " + yourFormattedString);
                        } else {
                            double value = surveyCheck.getComponents().getCroplandValue();
                            String yourFormattedString = valueFormatter.format(roundTwo(value) * -1);
                            cropValue.setText("- " + currency + " " + yourFormattedString);
                        }

                        totalVal = totalVal + surveyCheck.getComponents().getCroplandValue();
                    } else {
                        cropValue.setText("0");
                    }
                } else {
                    cropValue.setText("0");
                }
            }

            if (landKind.getName().equals(getString(R.string.string_pastureland))) {
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
                            pastureValue.setText(" " + currency + " " + yourFormattedString);
                        } else {
                            double value = surveyCheck.getComponents().getPastureValue();
                            String yourFormattedString = valueFormatter.format(roundTwo(value) * -1);
                            pastureValue.setText("- " + currency + " " + yourFormattedString);
                        }


                        totalVal = totalVal + surveyCheck.getComponents().getPastureValue();
                    } else {
                        pastureValue.setText("0");
                    }
                } else {
                    pastureValue.setText("0");
                }
            }

            if (landKind.getName().equals(getString(R.string.string_miningland))) {
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
                            miningValue.setText(" " + currency + " " + yourFormattedString);
                        } else {
                            double value = surveyCheck.getComponents().getMiningLandValue();
                            String yourFormattedString = valueFormatter.format(roundTwo(value) * -1);
                            miningValue.setText("- " + currency + " " + yourFormattedString);
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

        parcelVal = totalVal / parcelArea;
        if (totalVal > 500) {
            if (totalVal >= 1000) {
                totalVal = (Math.round(totalVal / 1000)) * 1000;
            } else totalVal = 1000;
        }
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

        String parcelValStr = formattedString(Long.valueOf(String.valueOf((long) parcelVal)));
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
            fString = "- " + currency + " " + valueFormatter1.format(l * -1);
        } else {
            fString = " " + currency + " " + valueFormatter1.format(l);
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
}
