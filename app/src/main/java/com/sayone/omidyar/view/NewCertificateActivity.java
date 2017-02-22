package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
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
    Context context;
    double totalVal = 0;
    double parcelVal=0;
    float parcelArea=0;
    TextView forestValue, cropValue, pastureValue, miningValue, totalText,parcelValue;
    private Realm realm;
    private String surveyId;
    private Boolean flag = true;
    private String currentSocialCapitalServey;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView textViewAbout;
    private TextView logout;
    private TextView startSurvey;
    private TextView respondentGroup;
    private TextView surveyIdDrawer;
    private DrawerLayout menuDrawerLayout;
    private String serveyId;
    private SocialCapital socialCapital;
    private TextView forestDiscountRateValue;
    private TextView cropDiscountRateValue;
    private TextView pastureDiscountRateValue;
    private TextView miningDiscountRateValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_certificate);

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
        headingCrop = (TextView) findViewById(R.id.heading_cropland);
        headingForest = (TextView) findViewById(R.id.heading_forest);
        headingPasture = (TextView) findViewById(R.id.heading_pastureland);
        headingMining = (TextView) findViewById(R.id.heading_miningland);
        fullscreen = (LinearLayout) findViewById(R.id.fullscreen);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        textViewAbout = (TextView) findViewById(R.id.text_view_about);
        logout = (TextView) findViewById(R.id.logout);
        startSurvey = (TextView) findViewById(R.id.text_start_survey);
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
        parcelValue=(TextView)findViewById(R.id.parcel_value);
        gpsCoordinate_1 = (TextView) findViewById(R.id.gps_coordinate_1);
        gpsCoordinate_2 = (TextView) findViewById(R.id.gps_coordinate_2);
        gpsCoordinate_3 = (TextView) findViewById(R.id.gps_coordinate_3);
        gpsCoordinate_4 = (TextView) findViewById(R.id.gps_coordinate_4);
        gpsCoordinate_5 = (TextView) findViewById(R.id.gps_coordinate_5);
        gpsCoordinate_6 = (TextView) findViewById(R.id.gps_coordinate_6);
        parcelSize = (TextView) findViewById(R.id.parcel_size);
        imageViewMenuIcon.setOnClickListener(this);
        drawerCloseBtn.setOnClickListener(this);
        textViewAbout.setOnClickListener(this);
        logout.setOnClickListener(this);
        startSurvey.setOnClickListener(this);

        context = this;

        totalVal = 0;

        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        surveyId = sharedPref.getString("surveyId", "");
        currentSocialCapitalServey = sharedPref.getString("currentSocialCapitalServey", "");
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

        Log.e("Language : ", Locale.getDefault().getDisplayLanguage());

        forestlandLayout.setVisibility(View.GONE);
        headingForest.setVisibility(View.GONE);

        croplandLayout.setVisibility(View.GONE);
        headingCrop.setVisibility(View.GONE);

        pasturelandLayout.setVisibility(View.GONE);
        headingPasture.setVisibility(View.GONE);

        mininglandLayout.setVisibility(View.GONE);
        headingMining.setVisibility(View.GONE);

        //GPS Coordinates
        RealmResults<ParcelLocation> parcelLocations = realm.where(ParcelLocation.class).
                equalTo("surveyId", surveyId)
                .findAll();

        for (ParcelLocation parcelLocation : parcelLocations) {
            String coordinate_1 = LocationConverter.getLatitudeDMS(parcelLocation.getLat_1()) + ", " + LocationConverter.getLongitudeDMS(parcelLocation.getLng_1());
            String coordinate_2 = LocationConverter.getLatitudeDMS(parcelLocation.getLat_2()) + ", " + LocationConverter.getLongitudeDMS(parcelLocation.getLng_2());
            String coordinate_3 = LocationConverter.getLatitudeDMS(parcelLocation.getLat_3()) + ", " + LocationConverter.getLongitudeDMS(parcelLocation.getLng_3());
            String coordinate_4 = LocationConverter.getLatitudeDMS(parcelLocation.getLat_4()) + ", " + LocationConverter.getLongitudeDMS(parcelLocation.getLng_4());
            String coordinate_5 = LocationConverter.getLatitudeDMS(parcelLocation.getLat_5()) + ", " + LocationConverter.getLongitudeDMS(parcelLocation.getLng_5());
            String coordinate_6 = LocationConverter.getLatitudeDMS(parcelLocation.getLat_6()) + ", " + LocationConverter.getLongitudeDMS(parcelLocation.getLng_6());
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

            if (landKind.getName().equals("Forestland")) {
                forestlandLayout.setVisibility(View.VISIBLE);
                headingForest.setVisibility(View.VISIBLE);
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
                            forestValue.setText(" ₹" + yourFormattedString);
                        } else {
                            double value = (surveyCheck.getComponents().getForestValue());
                            String yourFormattedString = valueFormatter.format(roundTwo(value) * -1);
                            forestValue.setText("- ₹" + yourFormattedString);
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

            if (landKind.getName().equals("Cropland")) {
                croplandLayout.setVisibility(View.VISIBLE);
                headingCrop.setVisibility(View.VISIBLE);
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
                            cropValue.setText(" ₹" + yourFormattedString);
                        } else {
                            double value = surveyCheck.getComponents().getCroplandValue();
                            String yourFormattedString = valueFormatter.format(roundTwo(value) * -1);
                            cropValue.setText("- ₹" + yourFormattedString);
                        }

                        totalVal = totalVal + surveyCheck.getComponents().getCroplandValue();
                    } else {
                        cropValue.setText("0");
                    }
                } else {
                    cropValue.setText("0");
                }
            }

            if (landKind.getName().equals("Pastureland")) {
                pasturelandLayout.setVisibility(View.VISIBLE);
                headingPasture.setVisibility(View.VISIBLE);
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
                            pastureValue.setText(" ₹" + yourFormattedString);
                        } else {
                            double value = surveyCheck.getComponents().getPastureValue();
                            String yourFormattedString = valueFormatter.format(roundTwo(value) * -1);
                            pastureValue.setText("- ₹" + yourFormattedString);
                        }


                        totalVal = totalVal + surveyCheck.getComponents().getPastureValue();
                    } else {
                        pastureValue.setText("0");
                    }
                } else {
                    pastureValue.setText("0");
                }
            }

            if (landKind.getName().equals("Mining Land")) {
                mininglandLayout.setVisibility(View.VISIBLE);
                headingMining.setVisibility(View.VISIBLE);
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
                            miningValue.setText(" ₹" + yourFormattedString);
                        } else {
                            double value = surveyCheck.getComponents().getMiningLandValue();
                            String yourFormattedString = valueFormatter.format(roundTwo(value) * -1);
                            miningValue.setText("- ₹" + yourFormattedString);
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

        community.setText(surveyCheck.getCommunity().toString());
        parcelId.setText(surveyCheck.getSurveyId().toString());
        surveyorName.setText(surveyCheck.getSurveyor().toString());
        respondentGroup.setText(surveyCheck.getRespondentGroup().toString());
        country.setText(surveyCheck.getCountry().toString());
        surveyDate.setText(s);
        surveyIdDrawer.setText(surveyId);
        DecimalFormat valueFormatter = new DecimalFormat("#,###,###");

//        double lowerLimit;
//        double upperLimit;
//        if (totalVal > 0) {
//            lowerLimit = totalVal * 0.9999;
//            lowerLimit = Math.round(lowerLimit);
//            upperLimit = totalVal * 1.0001;
//            upperLimit = Math.round(upperLimit);
//        } else if (totalVal < 0) {
//            lowerLimit = totalVal * 1.0001;
//            lowerLimit = Math.round(lowerLimit);
//            upperLimit = totalVal * 0.9999;
//            upperLimit = Math.round(upperLimit);
//        } else {
//            lowerLimit = 0;
//            upperLimit = 0;
//        }

//        String lowerLimitStr = String.valueOf((long) lowerLimit);
//        String upperLimitStr = String.valueOf((long) upperLimit);

//        numberProcess(lowerLimitStr);
//        Log.e("LOWER LIMIT Test ", numberProcess(lowerLimitStr));


//        Log.e("LOWER LIMIT ", String.valueOf(lowerLimit));
//        Log.e("LOWER LIMIT ", String.valueOf(Math.round(lowerLimit / 10000) * 10000));
//        Log.e("UPPER LIMIT ", String.valueOf((long) upperLimit));

        String totalValStr = formattedString(Long.valueOf(numberProcess(String.valueOf((long) totalVal))));
        String totalValStrToSend = formattedStringNoSymbol(Long.valueOf(numberProcess(String.valueOf((long) totalVal))));

        Log.e("Final Value ", totalValStr);

        if (surveyCheck.getComponents() != null) {
            realm.beginTransaction();
            surveyCheck.getComponents().setTotalValueStr(totalValStrToSend);
            realm.commitTransaction();
        }

        totalText.setText(totalValStr);

//        double infl = Double.parseDouble(surveyCheck.getInflationRate()) * 100.00;
//        double roundOff = Math.round(infl * 100.0) / 100.0;
        inflationRate.setText(surveyCheck.getInflationRate() + "%");

        //Sovereign/Country Risk Rate
//        double riskRate = surveyCheck.getRiskRate() * 100.00;
//        double roundOffRisk = Math.round(riskRate * 100.0) / 100.0;
        countryRiskRate.setText(surveyCheck.getRiskRate() + "%");

        parcelVal=totalVal/parcelArea;
        String parcelValStr = formattedString(Long.valueOf(numberProcess(String.valueOf((long) parcelVal))));
        parcelValue.setText(parcelValStr);
    }

    public String numberProcess(String numberStr) {
        String zerosStr = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        int zerosCount = 0;
        long q = Long.valueOf(numberStr);
        if (numberStr.length() >= 4) {
            zerosCount = numberStr.substring(4).length();
            double afterDec = Double.valueOf("0." + numberStr.substring(4));
            q = Long.valueOf(numberStr.substring(0, 4)) + Math.round(afterDec);
        }
        return String.valueOf(q) + zerosStr.substring(0, zerosCount);
    }

    public String formattedString(long l) {
        DecimalFormat valueFormatter1 = new DecimalFormat("#,###,###");

        String fString = "0";
        if (l < 0) {
            fString = "- ₹" + valueFormatter1.format(l * -1);
        } else {
            fString = "₹" + valueFormatter1.format(l);
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