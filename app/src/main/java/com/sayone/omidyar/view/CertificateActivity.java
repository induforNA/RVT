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
import java.text.Format;
import java.text.SimpleDateFormat;

import io.realm.Realm;
import io.realm.RealmResults;

public class CertificateActivity extends BaseActivity implements View.OnClickListener {

    TextView parcelId,community,site,surveyorName,valuationDate,inflationRate,socialCapitalForest,socialCapitalCrop,socialCapitalPasture,socialCapitalMining;
    SharedPreferences sharedPref;
    private Realm realm;
    TextView headingForest,headingCrop,headingPasture,headingMining;
    private String surveyId;
    LinearLayout fullscreen;
    CardView forestlandLayout,croplandLayout,pasturelandLayout,mininglandLayout;
    Context context;
    ImageView mapImageForest,mapImageCrop,mapImagePasture,mapImageMining,mapFullScreen;
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
    private TextView surveyIdDrawer;
    private DrawerLayout menuDrawerLayout;
    private String serveyId;

    double totalVal = 0;

    TextView forestValue, cropValue, pastureValue, miningValue, totalText;

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
        forestlandLayout=(CardView) findViewById(R.id.forestland_layout);
        croplandLayout=(CardView) findViewById(R.id.cropland_layout);
        pasturelandLayout=(CardView) findViewById(R.id.pastureland_layout);
        mininglandLayout=(CardView) findViewById(R.id.miningland_layout);
        headingCrop=(TextView)findViewById(R.id.heading_cropland);
        headingForest=(TextView)findViewById(R.id.heading_forest);
        headingPasture=(TextView)findViewById(R.id.heading_pastureland);
        headingMining=(TextView)findViewById(R.id.heading_miningland);
        mapFullScreen=(ImageView)findViewById(R.id.map_fullscreen);
        fullscreen=(LinearLayout)findViewById(R.id.fullscreen);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        textViewAbout = (TextView) findViewById(R.id.text_view_about);
        logout = (TextView) findViewById(R.id.logout);
        startSurvey=(TextView)findViewById(R.id.text_start_survey);
        surveyIdDrawer=(TextView)findViewById(R.id.text_view_id);

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
        surveyIdDrawer.setText(serveyId);

        context=this;

        totalVal = 0;

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


        RealmResults<LandKind> landKinds = realm.where(LandKind.class)
                .equalTo("surveyId", surveyId)
                .equalTo("status", "active")
                .findAll();

        forestlandLayout.setVisibility(View.GONE);
        headingForest.setVisibility(View.GONE);

        croplandLayout.setVisibility(View.GONE);
        headingCrop.setVisibility(View.GONE);

        pasturelandLayout.setVisibility(View.GONE);
        headingPasture.setVisibility(View.GONE);

        mininglandLayout.setVisibility(View.GONE);
        headingMining.setVisibility(View.GONE);

        for(LandKind landKind: landKinds){
            if(landKind.getName().equals("Forestland")){
                forestlandLayout.setVisibility(View.VISIBLE);
                headingForest.setVisibility(View.VISIBLE);


                String pathForestMap = Environment.getExternalStorageDirectory().toString() +"/MapImagesNew/"+"Forestland"+surveyId+"screen.jpg/";
                mapImageForest.setVisibility(View.VISIBLE);
                fforest = new File(pathForestMap);
//                if(!fforest.exists()) {
//                    forestlandLayout.setVisibility(View.GONE);
//                    headingForest.setVisibility(View.GONE);
//                }
                Picasso.with(context).load(fforest).memoryPolicy(MemoryPolicy.NO_CACHE).into(mapImageForest);
                socialCapitalForest.setText(""+landKind.getSocialCapitals().getScore()+"/20");
                forestValue.setText(roundTwo(surveyCheck.getComponents().getForestValue())+"");
                totalVal = totalVal + surveyCheck.getComponents().getForestValue();
            }

            if(landKind.getName().equals("Cropland")){
                croplandLayout.setVisibility(View.VISIBLE);
                headingCrop.setVisibility(View.VISIBLE);

                String pathCropMap = Environment.getExternalStorageDirectory().toString() +"/MapImagesNew/"+"Cropland"+surveyId+"screen.jpg/";
                mapImageCrop.setVisibility(View.VISIBLE);
                fcrop = new File(pathCropMap);
//                if(!fcrop.exists()){
//                    croplandLayout.setVisibility(View.GONE);
//                    headingCrop.setVisibility(View.GONE);
//                }
                Picasso.with(context).load(fcrop).memoryPolicy(MemoryPolicy.NO_CACHE).into(mapImageCrop);
                socialCapitalCrop.setText(""+landKind.getSocialCapitals().getScore()+"/20");
                cropValue.setText(roundTwo(surveyCheck.getComponents().getCroplandValue())+"");

                totalVal = totalVal + surveyCheck.getComponents().getCroplandValue();
            }

            if(landKind.getName().equals("Pastureland")){
                pasturelandLayout.setVisibility(View.VISIBLE);
                headingPasture.setVisibility(View.VISIBLE);

                String pathPastureMap = Environment.getExternalStorageDirectory().toString() +"/MapImagesNew/"+"Pastureland"+surveyId+"screen.jpg/";
                mapImagePasture.setVisibility(View.VISIBLE);
                fpasture = new File(pathPastureMap);
//                if(!fpasture.exists()){
//                    pasturelandLayout.setVisibility(View.GONE);
//                    headingPasture.setVisibility(View.GONE);
//                }
                Picasso.with(context).load(fpasture).memoryPolicy(MemoryPolicy.NO_CACHE).into(mapImagePasture);
                socialCapitalPasture.setText(""+landKind.getSocialCapitals().getScore()+"/20");
                pastureValue.setText(roundTwo(surveyCheck.getComponents().getPastureValue())+""+"");

                totalVal = totalVal + surveyCheck.getComponents().getPastureValue();
            }

            if(landKind.getName().equals("Mining Land")){
                mininglandLayout.setVisibility(View.VISIBLE);
                headingMining.setVisibility(View.VISIBLE);
                String pathminingMap = Environment.getExternalStorageDirectory().toString() +"/MapImagesNew/"+"Mining Land"+surveyId+"screen.jpg/";
                mapImageMining.setVisibility(View.VISIBLE);
                fmining = new File(pathminingMap);
//                if(!fmining.exists()){
//                    mininglandLayout.setVisibility(View.GONE);
//                    headingMining.setVisibility(View.GONE);
//                }
                Picasso.with(context).load(fmining).memoryPolicy(MemoryPolicy.NO_CACHE).into(mapImageMining);
                socialCapitalMining.setText(""+landKind.getSocialCapitals().getScore()+"/20");
                miningValue.setText(roundTwo(surveyCheck.getComponents().getMiningLandValue())+"");

                totalVal = totalVal + surveyCheck.getComponents().getMiningLandValue();
            }
        }



        community.setText(surveyCheck.getCommunity().toString());
        parcelId.setText(surveyCheck.getSurveyId().toString());
        surveyorName.setText(surveyCheck.getSurveyor().toString());
        valuationDate.setText(s);

        totalText.setText(roundTwo(totalVal)+"");

//        socialCapitalCrop.setText("0");
//        socialCapitalPasture.setText("0");
//        socialCapitalMining.setText("0");
      //  inflationRate.setText(surveyCheck.getInflationRate().toString());

    }

    public double roundTwo(double val){
        val = val*100;
        val = Math.round(val);
        val = val /100;
        return val;
    }

    @Override
    public void onBackPressed() {
        if (mapFullScreen.getVisibility() == View.VISIBLE) {
            mapFullScreen.setVisibility(View.GONE);
            fullscreen.setVisibility(View.VISIBLE);
        }else {
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

            case R.id.image_view_menu_icon:
                toggleMenuDrawer();
                break;

            case  R.id.drawer_close_btn:
                toggleMenuDrawer();
                break;

            case  R.id.text_view_about:
                Intent i = new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(i);
                break;

            case R.id.text_start_survey:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            case R.id.logout:
                Intent intents = new Intent(getApplicationContext(),RegistrationActivity.class);
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intents);
                break;

        }
    }


    public void toggleMenuDrawer(){
        if(menuDrawerLayout.isDrawerOpen(GravityCompat.START)){
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            menuDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

}
