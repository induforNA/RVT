package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.SocialCapital;
import com.sayone.omidyar.model.SocialCapitalQuestions;
import com.sayone.omidyar.model.Survey;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;

import io.realm.Realm;
import io.realm.RealmResults;

public class CertificateActivity extends BaseActivity implements View.OnClickListener {

    TextView parcelId, community, site, surveyorName, valuationDate, inflationRate, socialCapitalForest, socialCapitalCrop, socialCapitalPasture, socialCapitalMining;
    SharedPreferences sharedPref;
    private Realm realm;
    TextView headingForest, headingCrop, headingPasture, headingMining;
    private String surveyId;
    private Boolean flag=true;
    LinearLayout fullscreen;
    CardView forestlandLayout, croplandLayout, pasturelandLayout, mininglandLayout;
    Context context;
    ImageView mapImageForest, mapImageCrop, mapImagePasture, mapImageMining, mapFullScreen;
    private String currentSocialCapitalServey;
    private File fforest;
    private File fcrop;
    private File fpasture;
    private File fmining;
    private Animation scaleAnim;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView textViewAbout;
    private TextView logout;
    private TextView startSurvey;
    private TextView respondentGroup;
    private TextView surveyIdDrawer;
    private TextView forestValueSymbol;
    private TextView cropValueSymbol;
    private TextView pastureValueSymbol;
    private TextView miningValueSymbol;
    private TextView totalSymbol;
    private DrawerLayout menuDrawerLayout;
    private String serveyId;
    private SocialCapital socialCapital;


    double totalVal = 0;

    TextView forestValue, cropValue, pastureValue, miningValue, totalText;
    private TextView forestDiscountRateValue;
    private TextView cropDiscountRateValue;
    private TextView pastureDiscountRateValue;
    private TextView miningDiscountRateValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);


        parcelId = (TextView) findViewById(R.id.parcel_id);
        community = (TextView) findViewById(R.id.community_name);
        surveyorName = (TextView) findViewById(R.id.surveyor_name);
        valuationDate = (TextView) findViewById(R.id.valuation_date);
        inflationRate = (TextView) findViewById(R.id.inflation_rate);
        socialCapitalForest = (TextView) findViewById(R.id.forest_social_capital_score);
        socialCapitalCrop = (TextView) findViewById(R.id.crop_social_capital_score);
        socialCapitalPasture = (TextView) findViewById(R.id.pasture_social_capital_score);
        socialCapitalMining = (TextView) findViewById(R.id.minimg_social_capital_score);
        mapImageForest = (ImageView) findViewById(R.id.map_image_forest);
        mapImageCrop = (ImageView) findViewById(R.id.map_image_crop);
        mapImagePasture = (ImageView) findViewById(R.id.map_image_pasture);
        mapImageMining = (ImageView) findViewById(R.id.map_image_mining);
        forestlandLayout = (CardView) findViewById(R.id.forestland_layout);
        croplandLayout = (CardView) findViewById(R.id.cropland_layout);
        pasturelandLayout = (CardView) findViewById(R.id.pastureland_layout);
        mininglandLayout = (CardView) findViewById(R.id.miningland_layout);
        headingCrop = (TextView) findViewById(R.id.heading_cropland);
        headingForest = (TextView) findViewById(R.id.heading_forest);
        headingPasture = (TextView) findViewById(R.id.heading_pastureland);
        headingMining = (TextView) findViewById(R.id.heading_miningland);
        mapFullScreen = (ImageView) findViewById(R.id.map_fullscreen);
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
        forestValueSymbol = (TextView) findViewById(R.id.forest_value_symbol);
        cropValueSymbol = (TextView) findViewById(R.id.crop_value_symbol);
        pastureValueSymbol = (TextView) findViewById(R.id.pasture_value_symbol);
        miningValueSymbol = (TextView) findViewById(R.id.mining_value_symbol);
        totalSymbol = (TextView) findViewById(R.id.total_symbol);


        forestValue = (TextView) findViewById(R.id.forest_value);
        cropValue = (TextView) findViewById(R.id.crop_value);
        pastureValue = (TextView) findViewById(R.id.pasture_value);
        miningValue = (TextView) findViewById(R.id.mining_value);

        totalText = (TextView) findViewById(R.id.total_text);

        mapImageForest.setOnClickListener(this);
        mapImageCrop.setOnClickListener(this);
        mapImagePasture.setOnClickListener(this);
        mapImageMining.setOnClickListener(this);
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

        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        String s = formatter.format(surveyCheck.getDate());


        RealmResults<LandKind> landKinds = realm.where(LandKind.class)
                .equalTo("surveyId", surveyId)
                .equalTo("status", "active")
                .findAll();
        Log.e("Symbol:", surveyCheck.getCurrency());

        if (surveyCheck.getCurrency().equals("INR")) {
            forestValueSymbol.setText("₹");
            pastureValueSymbol.setText("₹");
            cropValueSymbol.setText("₹");
            miningValueSymbol.setText("₹");
            totalSymbol.setText(" ₹");
        }

        forestlandLayout.setVisibility(View.GONE);
        headingForest.setVisibility(View.GONE);

        croplandLayout.setVisibility(View.GONE);
        headingCrop.setVisibility(View.GONE);

        pasturelandLayout.setVisibility(View.GONE);
        headingPasture.setVisibility(View.GONE);

        mininglandLayout.setVisibility(View.GONE);
        headingMining.setVisibility(View.GONE);


        for (LandKind landKind : landKinds) {
            if (landKind.getName().equals("Forestland")) {
                forestlandLayout.setVisibility(View.VISIBLE);
                headingForest.setVisibility(View.VISIBLE);
                socialCapital = landKind.getSocialCapitals();
                if (socialCapital.isDiscountFlag()) {
                    forestDiscountRateValue.setText(String.valueOf(socialCapital.getDiscountRateOverride()) + "%");
                } else {
                    forestDiscountRateValue.setText(String.valueOf(socialCapital.getDiscountRate()) + "%");
                }

                String pathForestMap = Environment.getExternalStorageDirectory().toString() + "/MapImagesNew/" + "Forestland" + surveyId + "screen.jpg/";
                mapImageForest.setVisibility(View.VISIBLE);
                fforest = new File(pathForestMap);
//                if(!fforest.exists()) {
//                    forestlandLayout.setVisibility(View.GONE);
//                    headingForest.setVisibility(View.GONE);
//                }
                Picasso.with(context).load(fforest).memoryPolicy(MemoryPolicy.NO_CACHE).into(mapImageForest);
                socialCapitalForest.setText("" + landKind.getSocialCapitals().getScore() + "/20");
                if (surveyCheck.getComponents() != null) {
                    if (surveyCheck.getComponents().getForestValue() != 0) {
                        DecimalFormat valueFormatter = new DecimalFormat("#,###,###");
                        if(!(surveyCheck.getComponents().getForestValue() <0)){
                            String yourFormattedString = valueFormatter.format(roundTwo(surveyCheck.getComponents().getForestValue()));
                            forestValue.setText(yourFormattedString);
                        }
                        else{
                            double value = (surveyCheck.getComponents().getForestValue())*-1;
                            String yourFormattedString = valueFormatter.format(roundTwo(value));
                            forestValue.setText(yourFormattedString);
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
                    cropDiscountRateValue.setText(String.valueOf(socialCapital.getDiscountRateOverride()) + "%");
                } else {
                    cropDiscountRateValue.setText(String.valueOf(socialCapital.getDiscountRate()) + "%");
                }

                String pathCropMap = Environment.getExternalStorageDirectory().toString() + "/MapImagesNew/" + "Cropland" + surveyId + "screen.jpg/";
                mapImageCrop.setVisibility(View.VISIBLE);
                fcrop = new File(pathCropMap);
//                if(!fcrop.exists()){
//                    croplandLayout.setVisibility(View.GONE);
//                    headingCrop.setVisibility(View.GONE);
//                }
                Picasso.with(context).load(fcrop).memoryPolicy(MemoryPolicy.NO_CACHE).into(mapImageCrop);
                socialCapitalCrop.setText("" + landKind.getSocialCapitals().getScore() + "/20");


                if (surveyCheck.getComponents() != null) {
                    if (surveyCheck.getComponents().getCroplandValue() != 0) {
                        DecimalFormat valueFormatter = new DecimalFormat("#,###,###");
                        if(!(surveyCheck.getComponents().getCroplandValue() <0)){
                            String yourFormattedString = valueFormatter.format(roundTwo(surveyCheck.getComponents().getCroplandValue()));
                            cropValue.setText(yourFormattedString);
                        }
                        else{
                            double value = surveyCheck.getComponents().getCroplandValue();
                            String yourFormattedString = valueFormatter.format(roundTwo(value));
                            cropValue.setText(yourFormattedString);
                        }


                        //cropValue.setText(roundTwo(surveyCheck.getComponents().getCroplandValue())+"");
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
                    pastureDiscountRateValue.setText(String.valueOf(socialCapital.getDiscountRateOverride()) + "%");
                } else {
                    pastureDiscountRateValue.setText(String.valueOf(socialCapital.getDiscountRate()) + "%");
                }
                String pathPastureMap = Environment.getExternalStorageDirectory().toString() + "/MapImagesNew/" + "Pastureland" + surveyId + "screen.jpg/";
                mapImagePasture.setVisibility(View.VISIBLE);
                fpasture = new File(pathPastureMap);
//                if(!fpasture.exists()){
//                    pasturelandLayout.setVisibility(View.GONE);
//                    headingPasture.setVisibility(View.GONE);
//                }
                Picasso.with(context).load(fpasture).memoryPolicy(MemoryPolicy.NO_CACHE).into(mapImagePasture);
                socialCapitalPasture.setText("" + landKind.getSocialCapitals().getScore() + "/20");


                if (surveyCheck.getComponents() != null) {
                    if (surveyCheck.getComponents().getPastureValue() != 0) {
                        DecimalFormat valueFormatter = new DecimalFormat("#,###,###");
                        if(!(surveyCheck.getComponents().getPastureValue() <0)){
                            String yourFormattedString = valueFormatter.format(roundTwo(surveyCheck.getComponents().getPastureValue()));
                            pastureValue.setText(yourFormattedString);
                        }
                        else{
                            double value = surveyCheck.getComponents().getPastureValue();
                            String yourFormattedString = valueFormatter.format(roundTwo(value));
                            pastureValue.setText(yourFormattedString);
                        }


                        //pastureValue.setText(roundTwo(surveyCheck.getComponents().getPastureValue())+""+"");
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
                    miningDiscountRateValue.setText(String.valueOf(socialCapital.getDiscountRateOverride()) + "%");
                } else {
                    miningDiscountRateValue.setText(String.valueOf(socialCapital.getDiscountRate()) + "%");
                }
                String pathminingMap = Environment.getExternalStorageDirectory().toString() + "/MapImagesNew/" + "Mining Land" + surveyId + "screen.jpg/";
                mapImageMining.setVisibility(View.VISIBLE);
                fmining = new File(pathminingMap);
//                if(!fmining.exists()){
//                    mininglandLayout.setVisibility(View.GONE);
//                    headingMining.setVisibility(View.GONE);
//                }
                Picasso.with(context).load(fmining).memoryPolicy(MemoryPolicy.NO_CACHE).into(mapImageMining);
                socialCapitalMining.setText("" + landKind.getSocialCapitals().getScore() + "/20");


                if (surveyCheck.getComponents() != null) {
                    if (surveyCheck.getComponents().getMiningLandValue() != 0) {
                        DecimalFormat valueFormatter = new DecimalFormat("#,###,###");
                        if(!(surveyCheck.getComponents().getMiningLandValue() <0)){
                            String yourFormattedString = valueFormatter.format(roundTwo(surveyCheck.getComponents().getMiningLandValue()));
                            miningValue.setText(yourFormattedString);
                        }
                        else{
                            double value = surveyCheck.getComponents().getMiningLandValue();
                            String yourFormattedString = valueFormatter.format(roundTwo(value));
                            miningValue.setText(yourFormattedString);
                        }


                        //miningValue.setText(roundTwo(surveyCheck.getComponents().getMiningLandValue())+"");
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
        valuationDate.setText(s);
        surveyIdDrawer.setText(surveyId);
        DecimalFormat valueFormatter = new DecimalFormat("#,###,###");
        if(!(totalVal <0)){

            String yourFormattedString = valueFormatter.format(roundTwo(totalVal));
            totalText.setText(yourFormattedString);
            flag=true;
        }
        else{
            totalVal=totalVal*-1;
            String yourFormattedString = valueFormatter.format(roundTwo(totalVal));
            totalText.setText(yourFormattedString);
            flag=false;
        }

        if(surveyCheck.getCurrency().equals("INR")){
            if(flag==true) {
                forestValueSymbol.setText("₹");
                pastureValueSymbol.setText("₹");
                cropValueSymbol.setText("₹");
                miningValueSymbol.setText("₹");
                totalSymbol.setText(" ₹");
            }
            else{
                forestValueSymbol.setText("- ₹");
                pastureValueSymbol.setText("- ₹");
                cropValueSymbol.setText("- ₹");
                miningValueSymbol.setText("- ₹");
                totalSymbol.setText(" - ₹");
            }
        }


//        socialCapitalCrop.setText("0");
//        socialCapitalPasture.setText("0");
//        socialCapitalMining.setText("0");
        //  inflationRate.setText(surveyCheck.getInflationRate().toString());

    }

    public double roundTwo(double val) {
        val = val * 100;
        val = Math.round(val);
        val = val / 100;
        return val;

    }

    @Override
    public void onBackPressed() {
        if (mapFullScreen.getVisibility() == View.VISIBLE) {
            mapFullScreen.setVisibility(View.GONE);
            fullscreen.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_image_forest:
                fullscreen.setVisibility(View.GONE);
                Picasso.with(context).load(fforest).into(mapFullScreen);
                mapFullScreen.setVisibility(View.VISIBLE);
                scaleAnim = AnimationUtils.loadAnimation(this, R.anim.fadein);
                mapFullScreen.startAnimation(scaleAnim);
                break;

            case R.id.map_image_crop:
                fullscreen.setVisibility(View.GONE);
                Picasso.with(context).load(fcrop).into(mapFullScreen);
                mapFullScreen.setVisibility(View.VISIBLE);
                scaleAnim = AnimationUtils.loadAnimation(this, R.anim.fadein);
                mapFullScreen.startAnimation(scaleAnim);
                break;

            case R.id.map_image_pasture:
                fullscreen.setVisibility(View.GONE);
                Picasso.with(context).load(fpasture).into(mapFullScreen);
                mapFullScreen.setVisibility(View.VISIBLE);
                scaleAnim = AnimationUtils.loadAnimation(this, R.anim.fadein);
                mapFullScreen.startAnimation(scaleAnim);
                break;

            case R.id.map_image_mining:
                fullscreen.setVisibility(View.GONE);
                Picasso.with(context).load(fmining).into(mapFullScreen);
                mapFullScreen.setVisibility(View.VISIBLE);
                scaleAnim = AnimationUtils.loadAnimation(this, R.anim.fadein);
                mapFullScreen.startAnimation(scaleAnim);
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
