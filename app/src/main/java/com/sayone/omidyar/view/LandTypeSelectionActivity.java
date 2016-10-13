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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.Participant;
import com.sayone.omidyar.model.SocialCapital;
import com.sayone.omidyar.model.Survey;

import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by sayone on 19/9/16.
 */
public class LandTypeSelectionActivity extends BaseActivity implements View.OnClickListener{

    public static final String TAG = LandTypeSelectionActivity.class.getName();
    Context context;
    SharedPreferences sharedPref;
    private Realm realm;
    private String serveyId;
    Survey survey;
    RealmList<LandKind> landKinds;
    String currentSocialCapitalServey;


    private Button nextButton;
    private Button backButton;
    private CheckBox forestland;
    private CheckBox cropland;
    private CheckBox pastureland;
    private CheckBox miningland;

    private DrawerLayout menuDrawerLayout;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView textViewAbout;
    private TextView surveyIdDrawer;
    private TextView logout;
    private TextView startSurvey;

    Set<String> landTypeNames;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_type_selection);

        context = this;
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = sharedPref.getString("surveyId","");
        currentSocialCapitalServey = sharedPref.getString("currentSocialCapitalServey", "");


        landKinds = new RealmList<>();
        landTypeNames = new HashSet<String>();

        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        nextButton = (Button) findViewById(R.id.next_button);
        backButton = (Button) findViewById(R.id.back_button);

        forestland = (CheckBox) findViewById(R.id.forestland);
        cropland = (CheckBox) findViewById(R.id.cropland);
        pastureland = (CheckBox) findViewById(R.id.pastureland);
        miningland = (CheckBox) findViewById(R.id.miningland);

        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        textViewAbout = (TextView) findViewById(R.id.text_view_about);
        surveyIdDrawer=(TextView)findViewById(R.id.text_view_id);
        logout = (TextView) findViewById(R.id.logout);
        startSurvey=(TextView)findViewById(R.id.text_start_survey);


        survey = realm.where(Survey.class)
                .equalTo("surveyId",serveyId)
                .findFirst();
        for(LandKind landKindItrate :survey.getLandKinds()) {
            Log.e(TAG+" 11",landKindItrate.toString());
            if(landKindItrate.getName().equals("Forestland") && landKindItrate.getStatus().equals("active")){
                forestland.setChecked(true);
                landTypeNames.add("Forestland");
            }else if(landKindItrate.getName().equals("Cropland") && landKindItrate.getStatus().equals("active")){
                cropland.setChecked(true);
                landTypeNames.add("Cropland");
            }else if(landKindItrate.getName().equals("Pastureland") && landKindItrate.getStatus().equals("active")){
                pastureland.setChecked(true);
                landTypeNames.add("Pastureland");
            }else if(landKindItrate.getName().equals("Mining Land") && landKindItrate.getStatus().equals("active")){
                miningland.setChecked(true);
                landTypeNames.add("Mining Land");
            }
        }

        surveyIdDrawer.setText(serveyId);

        imageViewMenuIcon.setOnClickListener(this);
        surveyIdDrawer.setText(serveyId);
        drawerCloseBtn.setOnClickListener(this);
        textViewAbout.setOnClickListener(this);
        logout.setOnClickListener(this);
        imageViewMenuIcon.setOnClickListener(this);
        drawerCloseBtn.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        startSurvey.setOnClickListener(this);
    }

    public void slectedLandKind(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.forestland:
                addLandTyeInSet(checked,"Forestland");
//                if (checked) {
//                    landTypeNames.add("Forestland");
//                }else{
//                    landTypeNames.remove("Forestland");
//                }
                break;
            case R.id.cropland:
                addLandTyeInSet(checked,"Cropland");
//                if (checked) {
//                    landTypeNames.add("Cropland");
//                }else{
//                    landTypeNames.remove("Cropland");
//                }
                break;
            case R.id.pastureland:
                addLandTyeInSet(checked,"Pastureland");
//                if (checked) {
//                    landTypeNames.add("Pastureland");
//                }else{
//                    landTypeNames.remove("Pastureland");
//                }
                break;
            case R.id.miningland:
                addLandTyeInSet(checked,"Mining Land");
//                if (checked) {
//                    landTypeNames.add("Mining Land");
//                }else{
//                    landTypeNames.add("Pastureland");
//                }
                break;
        }
    }

    public void addLandTyeInSet(boolean checked, String name){
        Log.e("CHECKED STATUS ", checked+" "+name);
        if (checked) {
            LandKind landKind = realm.where(LandKind.class)
                    .equalTo("name",name)
                    .equalTo("surveyId",serveyId)
                    .findFirst();
            SocialCapital socialCapital = null;
            if(landKind.getSocialCapitals() == null){
                realm.beginTransaction();
                socialCapital = realm.createObject(SocialCapital.class);
                socialCapital.setId(getNextKeySocialCapital());
                socialCapital.setSurveyId(serveyId);
                realm.commitTransaction();
            }

            realm.beginTransaction();
            landKind.setStatus("active");
            if(landKind.getSocialCapitals() == null) {
                landKind.setSocialCapitals(socialCapital);
            }
            realm.commitTransaction();
            landTypeNames.add(name);
        }else{
            LandKind landKind = realm.where(LandKind.class)
                    .equalTo("surveyId",serveyId)
                    .equalTo("name",name)
                    .findFirst();
            SocialCapital socialCapital = null;
            if(landKind.getSocialCapitals() == null){
                realm.beginTransaction();
                socialCapital = realm.createObject(SocialCapital.class);
                socialCapital.setId(getNextKeySocialCapital());
                socialCapital.setSurveyId(serveyId);
                realm.commitTransaction();
            }

            realm.beginTransaction();
            landKind.setStatus("deleted");
            landKind.setSocialCapitals(socialCapital);
            realm.commitTransaction();
            landTypeNames.remove(name);
        }
        Log.e("CHECKED STATUS ", landTypeNames.toString());
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
            case R.id.next_button:
                Log.e("CLICK ","CHECK");
                saveLandKind();
                break;
            case R.id.back_button:
                finish();
                break;
            case  R.id.text_view_about:
                Intent i = new Intent(LandTypeSelectionActivity.this,AboutActivity.class);
                startActivity(i);
                break;
            case R.id.logout:
                Intent intent = new Intent(LandTypeSelectionActivity.this,RegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.text_start_survey:
                Intent intents = new Intent(getApplicationContext(),MainActivity.class);
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

    public void saveLandKind(){
        RealmResults<LandKind> landKindRealmResults = realm.where(LandKind.class)
                .equalTo("surveyId",serveyId)
                .equalTo("status","active")
                .findAll();
        if(landKindRealmResults.size() != 0) {

            RealmResults<LandKind> results = realm.where(LandKind.class)
                    .equalTo("surveyId", serveyId)
                    .equalTo("status", "active")
                    .findAll();
            for (LandKind survey1 : results) {
                Log.e(TAG, survey1.toString());
                //Log.e(TAG, String.valueOf(survey1.getParticipants().size()));
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("currentSocialCapitalServey", landKindRealmResults.get(0).getName());
            editor.apply();

            Intent intent = new Intent(LandTypeSelectionActivity.this, OmidyarMap.class);
            startActivity(intent);
        }else{
            Toast.makeText(context,"Select atleast one",Toast.LENGTH_SHORT).show();
        }
    }

    public int getNextKeyLandKind() {
        return realm.where(LandKind.class).max("id").intValue() + 1;
    }
    public int getNextKeySocialCapital() {
        return realm.where(SocialCapital.class).max("id").intValue() + 1;
    }
}