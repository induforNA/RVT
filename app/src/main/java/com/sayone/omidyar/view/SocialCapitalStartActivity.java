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
import com.sayone.omidyar.model.SocialCapitalAnswer;
import com.sayone.omidyar.model.SocialCapitalQuestions;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by sayone on 20/9/16.
 */
public class SocialCapitalStartActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = SocialCapitalStartActivity.class.getName();
    private Realm realm;
    private SharedPreferences preferences;
    Context context;
    private DrawerLayout menuDrawerLayout;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView surveyIdDrawer;

    String currentSocialCapitalServey;
    String surveyId;
    private TextView landType;
    private Button backButton;
    private Button nextButton;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_capital_start);

        context = this;
        realm = Realm.getDefaultInstance();
        preferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        surveyId = preferences.getString("surveyId", "");
        currentSocialCapitalServey = preferences.getString("currentSocialCapitalSurvey", "");

        landType = (TextView) findViewById(R.id.land_type);
        backButton = (Button) findViewById(R.id.back_button);
        nextButton = (Button) findViewById(R.id.next_button);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        surveyIdDrawer = (TextView) findViewById(R.id.text_view_id);

        surveyIdDrawer.setText(surveyId);

        imageViewMenuIcon.setOnClickListener(this);
        surveyIdDrawer.setText(surveyId);
        drawerCloseBtn.setOnClickListener(this);
        backButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        if (currentSocialCapitalServey.equals("")) {
            RealmResults<LandKind> landKindRealmResults = realm.where(LandKind.class)
                    .equalTo("surveyId", surveyId)
                    .equalTo("status", "active")
                    .findAll();
            SharedPreferences.Editor editor = preferences.edit();
            currentSocialCapitalServey = landKindRealmResults.get(0).getName();
            editor.putString("currentSocialCapitalSurvey", currentSocialCapitalServey);
            editor.apply();
//            for (LandKind landKind : landKindRealmResults) {
//                Log.e("NAME ",landKind.getName());
//            }
        }
//        else if(currentSocialCapitalSurvey.equals("Forestland")){
//
//        }else if(currentSocialCapitalSurvey.equals("Cropland")){
//
//        }else if(currentSocialCapitalSurvey.equals("Pastureland")){
//
//        }else if(currentSocialCapitalSurvey.equals("Mining Land")){
//
//        }

        LandKind landKind = realm.where(LandKind.class)
                .equalTo("surveyId", surveyId)
                .equalTo("name", currentSocialCapitalServey)
                .equalTo("status", "active")
                .findFirst();
        if (landKind.getSocialCapitals().getSocialCapitalAnswers().size() == 0) {
            Log.e("Here ", "NULL");


            RealmResults<SocialCapitalQuestions> socialCapitalQuestionses = realm.where(SocialCapitalQuestions.class).findAll();
            RealmList<SocialCapitalAnswer> socialCapitalAnswers = new RealmList<>();
            for (SocialCapitalQuestions socialCapitalQuestions : socialCapitalQuestionses) {
                Log.e("Social capital", socialCapitalQuestions.getId() + " " + socialCapitalQuestions.getQuestion());
                realm.beginTransaction();
                SocialCapitalAnswer socialCapitalAnswer = realm.createObject(SocialCapitalAnswer.class);
                socialCapitalAnswer.setId(getNextKeySocialCapitalAnswer());
                socialCapitalAnswer.setSurveyId(surveyId);
                socialCapitalAnswer.setSocialCapitalQuestion(socialCapitalQuestions);
                realm.commitTransaction();
                socialCapitalAnswers.add(socialCapitalAnswer);
            }
            realm.beginTransaction();
            landKind.getSocialCapitals().setSocialCapitalAnswers(socialCapitalAnswers);
            realm.commitTransaction();
        }


        LandKind landKind1 = realm.where(LandKind.class)
                .equalTo("surveyId", surveyId)
                .equalTo("name", currentSocialCapitalServey)
                .equalTo("status", "active")
                .findFirst();

        Log.e("LLL ", landKind1.getSocialCapitals().getSocialCapitalAnswers().toString());
        Log.e("blaa : ", currentSocialCapitalServey);


        if (currentSocialCapitalServey.equals(getString(R.string.string_forestland)))
            landType.setText(getResources().getText(R.string.string_forestland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland)))
            landType.setText(getResources().getText(R.string.string_pastureland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_miningland)))
            landType.setText(getResources().getText(R.string.string_miningland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_cropland)))
            landType.setText(getResources().getText(R.string.title_cropland));
        //  landType.setText(currentSocialCapitalSurvey);
        // landType.setText(currentSocialCapitalSurvey);

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

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.next_button:
                Intent intent = new Intent(SocialCapitalStartActivity.this, SocialCapitalActivity.class);
                startActivity(intent);
                break;
            case R.id.image_view_menu_icon:
                toggleMenuDrawer();
                break;
            case R.id.drawer_close_btn:
                toggleMenuDrawer();
                break;
            case R.id.text_view_about:
                i = new Intent(SocialCapitalStartActivity.this, AboutActivity.class);
                startActivity(i);
                break;
            case R.id.logout:
                i = new Intent(SocialCapitalStartActivity.this, RegistrationActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
            case R.id.text_start_survey:
                i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
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
        Intent intent = new Intent(SocialCapitalStartActivity.this, StartLandTypeActivity.class);
        startActivity(intent);
    }

    private void setCurrentSocialCapitalSurvey(String name) {
        SharedPreferences.Editor editor = preferences.edit();
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

    public int getNextKeySocialCapitalAnswer() {
        return realm.where(SocialCapitalAnswer.class).max("id").intValue() + 1;
    }

    public void toggleMenuDrawer() {
        if (menuDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            menuDrawerLayout.openDrawer(GravityCompat.START);
        }
    }
}