package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.LandKind;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by sayone on 14/10/16.
 */

public class StartLandTypeActivity extends BaseActivity implements View.OnClickListener{

    Context context;
    Realm realm;
    SharedPreferences sharedPref;

    Button backButton, nextButton;
    String currentSocialCapitalServey, serveyId;
    TextView landType;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView textViewAbout;
    private TextView surveyIdDrawer;
    private TextView logout;
    private TextView startSurvey;
    private DrawerLayout menuDrawerLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_land_type);

        context = this;
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = sharedPref.getString("surveyId","");
        currentSocialCapitalServey = sharedPref.getString("currentSocialCapitalServey","");

        backButton = (Button) findViewById(R.id.back_button);
        nextButton = (Button) findViewById(R.id.next_button);
        landType = (TextView) findViewById(R.id.land_type);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        textViewAbout = (TextView) findViewById(R.id.text_view_about);
        surveyIdDrawer=(TextView)findViewById(R.id.text_view_id);
        logout = (TextView) findViewById(R.id.logout);
        startSurvey=(TextView)findViewById(R.id.text_start_survey);

        surveyIdDrawer.setText(serveyId);

      //  landType.setText(currentSocialCapitalServey);

        if(currentSocialCapitalServey.equals("Forestland"))
            landType.setText(getResources().getText(R.string.string_forestland));
        if(currentSocialCapitalServey.equals("Pastureland"))
            landType.setText(getResources().getText(R.string.string_pastureland));
        if(currentSocialCapitalServey.equals("Mining Land"))
            landType.setText(getResources().getText(R.string.string_miningland));
        if(currentSocialCapitalServey.equals("Cropland"))
            landType.setText(getResources().getText(R.string.title_cropland));
        //  landType.setText(currentSocialCapitalServey);

        backButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        imageViewMenuIcon.setOnClickListener(this);
        surveyIdDrawer.setText(serveyId);
        drawerCloseBtn.setOnClickListener(this);
        textViewAbout.setOnClickListener(this);
        logout.setOnClickListener(this);
        backButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        startSurvey.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.next_button:
                Intent intent = new Intent(StartLandTypeActivity.this, OmidyarMap.class);
                startActivity(intent);
                break;
            case R.id.back_button:
                previousLandKind();
                finish();
                break;
            case  R.id.drawer_close_btn:
                toggleMenuDrawer();
                break;
            case  R.id.text_view_about:
                i = new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(i);
                break;
            case R.id.logout:
                i = new Intent(getApplicationContext(),RegistrationActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
            case R.id.text_start_survey:
                i = new Intent(getApplicationContext(),MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
            case R.id.image_view_menu_icon:
                toggleMenuDrawer();
                break;
        }
    }

    public void previousLandKind(){
        RealmResults<LandKind> landKindRealmResults = realm.where(LandKind.class)
                .equalTo("surveyId",serveyId)
                .equalTo("status","active")
                .findAll();
        int j = 0;
        int i = 0;
        for (LandKind landKind : landKindRealmResults) {
            Log.e("TAG ", landKind.toString());
            //Log.e(TAG, String.valueOf(survey1.getParticipants().size()));
            if(landKind.getName().equals(currentSocialCapitalServey)){
                j = i - 1;
            }
            i++;
        }

        if(j >= 0){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("currentSocialCapitalServey", landKindRealmResults.get(j).getName());
            editor.apply();

            Intent intent = new Intent(getApplicationContext(),StartLandTypeActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getApplicationContext(),CertificateActivity.class);
            startActivity(intent);
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
