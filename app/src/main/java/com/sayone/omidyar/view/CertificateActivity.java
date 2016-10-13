package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.text.Format;
import java.text.SimpleDateFormat;

import io.realm.Realm;

public class CertificateActivity extends BaseActivity implements View.OnClickListener {

    TextView parcelId,community,site,surveyorName,valuationDate,inflationRate,socialCapitalForest,socialCapitalCrop,socialCapitalPasture,socialCapitalMining;
    SharedPreferences sharedPref;
    private Realm realm;
    TextView headingForest,headingCrop,headingPasture,headingMining;
    private String surveyId;
    LinearLayout forestlandLayout,croplandLayout,pasturelandLayout,mininglandLayout,fullscreen;
    Context context;
    ImageView mapImageForest,mapImageCrop,mapImagePasture,mapImageMining,mapFullScreen;
    private String currentSocialCapitalServey;
    private File fforest;
    private File fcrop;
    private File fpasture;
    private File fmining;
    private Animation scaleAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);

        parcelId=(TextView)findViewById(R.id.parcel_id);
        community=(TextView)findViewById(R.id.community_name);
        site=(TextView)findViewById(R.id.site_name);
        surveyorName=(TextView)findViewById(R.id.surveyor_name);
        valuationDate=(TextView)findViewById(R.id.valuation_date);
        inflationRate=(TextView)findViewById(R.id.inflation_rate);
        socialCapitalForest=(TextView)findViewById(R.id.forest_social_capital_score);
        socialCapitalCrop=(TextView)findViewById(R.id.crop_social_capital_score);
        socialCapitalPasture=(TextView)findViewById(R.id.pasture_social_capital_score);
        socialCapitalMining=(TextView)findViewById(R.id.minimg_social_capital_score);
        mapImageForest=(ImageView)findViewById(R.id.map_image_forest);
        mapImageCrop=(ImageView)findViewById(R.id.map_image_crop);
        mapImagePasture=(ImageView)findViewById(R.id.map_image_pasture);
        mapImageMining=(ImageView)findViewById(R.id.map_image_mining);
        forestlandLayout=(LinearLayout)findViewById(R.id.forestland_layout);
        croplandLayout=(LinearLayout)findViewById(R.id.cropland_layout);
        pasturelandLayout=(LinearLayout)findViewById(R.id.pastureland_layout);
        mininglandLayout=(LinearLayout)findViewById(R.id.miningland_layout);
        headingCrop=(TextView)findViewById(R.id.heading_cropland);
        headingForest=(TextView)findViewById(R.id.heading_forest);
        headingPasture=(TextView)findViewById(R.id.heading_pastureland);
        headingMining=(TextView)findViewById(R.id.heading_miningland);
        mapFullScreen=(ImageView)findViewById(R.id.map_fullscreen);
        fullscreen=(LinearLayout)findViewById(R.id.fullscreen);

        mapImageForest.setOnClickListener(this);
        mapImageCrop.setOnClickListener(this);
        mapImagePasture.setOnClickListener(this);
        mapImageMining.setOnClickListener(this);





        context=this;

        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        surveyId = sharedPref.getString("surveyId","");
        currentSocialCapitalServey = sharedPref.getString("currentSocialCapitalServey","");
        Realm realm = Realm.getDefaultInstance();
        Survey surveyCheck = realm.where(Survey.class)
                .equalTo("surveyId",surveyId)
                .findFirst();

        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        String s = formatter.format(surveyCheck.getDate());

        LandKind landKindLoad = realm.where(LandKind.class)
                .equalTo("surveyId", surveyId)
                .equalTo("status", "active")
                .findFirst();
        SocialCapital socialCapital = landKindLoad.getSocialCapitals();

        String pathForestMap = Environment.getExternalStorageDirectory().toString() +"/MapImagesNew/"+"Forestland"+surveyId+"screen.jpg/";
        mapImageForest.setVisibility(View.VISIBLE);
        fforest = new File(pathForestMap);
        if(!fforest.exists()) {
            forestlandLayout.setVisibility(View.GONE);
            headingForest.setVisibility(View.GONE);
        }
        Picasso.with(context).load(fforest).memoryPolicy(MemoryPolicy.NO_CACHE).into(mapImageForest);

        String pathCropMap = Environment.getExternalStorageDirectory().toString() +"/MapImagesNew/"+"Cropland"+surveyId+"screen.jpg/";
        mapImageCrop.setVisibility(View.VISIBLE);
        fcrop = new File(pathCropMap);
        if(!fcrop.exists()){
            croplandLayout.setVisibility(View.GONE);
            headingCrop.setVisibility(View.GONE);
        }
        Picasso.with(context).load(fcrop).memoryPolicy(MemoryPolicy.NO_CACHE).into(mapImageCrop);
        String pathPastureMap = Environment.getExternalStorageDirectory().toString() +"/MapImagesNew/"+"Pastureland"+surveyId+"screen.jpg/";
        mapImagePasture.setVisibility(View.VISIBLE);
        fpasture = new File(pathPastureMap);
        if(!fpasture.exists()){
            pasturelandLayout.setVisibility(View.GONE);
            headingPasture.setVisibility(View.GONE);
        }
        Picasso.with(context).load(fpasture).memoryPolicy(MemoryPolicy.NO_CACHE).into(mapImagePasture);
        String pathminingMap = Environment.getExternalStorageDirectory().toString() +"/MapImagesNew/"+"Mining Land"+surveyId+"screen.jpg/";
        mapImageMining.setVisibility(View.VISIBLE);
        fmining = new File(pathminingMap);
        if(!fmining.exists()){
            mininglandLayout.setVisibility(View.GONE);
            headingMining.setVisibility(View.GONE);
        }
        Picasso.with(context).load(fmining).memoryPolicy(MemoryPolicy.NO_CACHE).into(mapImageMining);

        community.setText(surveyCheck.getCommunity().toString());
        parcelId.setText(surveyCheck.getSurveyId().toString());
        surveyorName.setText(surveyCheck.getSurveyor().toString());
        valuationDate.setText(s);
        socialCapitalForest.setText(""+socialCapital.getScore());
        socialCapitalCrop.setText("0");
        socialCapitalPasture.setText("0");
        socialCapitalMining.setText("0");
      //  inflationRate.setText(surveyCheck.getInflationRate().toString());

    }

    @Override
    public void onBackPressed() {
        if (mapFullScreen.getVisibility() == View.VISIBLE) {
            mapFullScreen.setVisibility(View.GONE);
            fullscreen.setVisibility(View.VISIBLE);
        }
        else {
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
                mapFullScreen.startAnimation( scaleAnim );
                break;

            case R.id.map_image_crop:
                fullscreen.setVisibility(View.GONE);
                Picasso.with(context).load(fcrop).into(mapFullScreen);
                mapFullScreen.setVisibility(View.VISIBLE);
                scaleAnim = AnimationUtils.loadAnimation(this, R.anim.fadein);
                mapFullScreen.startAnimation( scaleAnim );
                break;
            case R.id.map_image_pasture:
                fullscreen.setVisibility(View.GONE);
                Picasso.with(context).load(fpasture).into(mapFullScreen);
                mapFullScreen.setVisibility(View.VISIBLE);
                scaleAnim = AnimationUtils.loadAnimation(this, R.anim.fadein);
                mapFullScreen.startAnimation( scaleAnim );
                break;

            case R.id.map_image_mining:
                fullscreen.setVisibility(View.GONE);
                Picasso.with(context).load(fmining).into(mapFullScreen);
                mapFullScreen.setVisibility(View.VISIBLE);
                scaleAnim = AnimationUtils.loadAnimation(this, R.anim.fadein);
                mapFullScreen.startAnimation( scaleAnim );
                break;

        }
    }



}
