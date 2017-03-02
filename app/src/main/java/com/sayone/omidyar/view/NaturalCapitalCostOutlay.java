package com.sayone.omidyar.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.adapter.CostOutlayAdapter;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.Outlay;
import com.sayone.omidyar.model.OutlayYears;
import com.sayone.omidyar.model.Survey;

import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by sayone on 18/10/16.
 */

public class NaturalCapitalCostOutlay extends BaseActivity {

    private static final int PROJECTION_COUNT = 15;
    Context context;
    Realm realm;
    SharedPreferences sharedPref;

    String surveyId;
    Button buttonBack, buttonNext;
    ImageView buttonAddWood;
    RealmList<Outlay> costOutlays;
    RealmList<Outlay> costOutlaysToSave;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView surveyIdDrawer;
    private DrawerLayout menuDrawerLayout;

    RecyclerView outlayItemList;
    CostOutlayAdapter costOutlayAdapter;
    TextView landType;
    TextView questionRevenue;

    String landKindName;
    String currentSocialCapitalServey;
    private String language;

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
        setContentView(R.layout.activity_natural_capital_cost_outlay);

        context = this;
        language = Locale.getDefault().getDisplayLanguage();
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        surveyId = sharedPref.getString("surveyId", "");
        currentSocialCapitalServey = sharedPref.getString("currentSocialCapitalSurvey", "");

        costOutlays = new RealmList<>();
        costOutlaysToSave = new RealmList<>();
        Survey survey = realm.where(Survey.class).equalTo("surveyId", surveyId).findFirst();
        for (LandKind landKind : survey.getLandKinds()) {
            if (landKind.getName().equals(getString(R.string.string_forestland)) && currentSocialCapitalServey.equals(getString(R.string.string_forestland))) {
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for (Outlay outlay : landKind.getForestLand().getOutlays()) {
                    costOutlaysToSave.add(outlay);
                    if (outlay.getType().equals("Timber")) {
                        costOutlays.add(outlay);
                    }
                }
            } else if (landKind.getName().equals(getString(R.string.string_cropland)) && currentSocialCapitalServey.equals(getString(R.string.string_cropland))) {
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for (Outlay outlay : landKind.getCropLand().getOutlays()) {
                    costOutlaysToSave.add(outlay);
                    if (outlay.getType().equals("Timber")) {
                        costOutlays.add(outlay);
                    }
                }
            } else if (landKind.getName().equals(getString(R.string.string_pastureland)) && currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for (Outlay outlay : landKind.getPastureLand().getOutlays()) {
                    costOutlaysToSave.add(outlay);
                    if (outlay.getType().equals("Timber")) {
                        costOutlays.add(outlay);
                    }
                }
            } else if (landKind.getName().equals(getString(R.string.string_miningland)) && currentSocialCapitalServey.equals(getString(R.string.string_miningland))) {
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for (Outlay outlay : landKind.getMiningLand().getOutlays()) {
                    costOutlaysToSave.add(outlay);
                    if (outlay.getType().equals("Timber")) {
                        costOutlays.add(outlay);
                    }
                }
            }
        }


        buttonBack = (Button) findViewById(R.id.button_back);
        buttonNext = (Button) findViewById(R.id.button_next);
        buttonAddWood = (ImageView) findViewById(R.id.button_add_wood);
        landType = (TextView) findViewById(R.id.land_type);
        questionRevenue = (TextView) findViewById(R.id.question_revenue);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        surveyIdDrawer = (TextView) findViewById(R.id.text_view_id);

        outlayItemList = (RecyclerView) findViewById(R.id.timber_list);

        //       landType.setText(currentSocialCapitalSurvey);
        if (currentSocialCapitalServey.equals(getString(R.string.string_forestland)))
            landType.setText(getResources().getText(R.string.string_forestland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland)))
            landType.setText(getResources().getText(R.string.string_pastureland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_miningland)))
            landType.setText(getResources().getText(R.string.string_miningland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_cropland)))
            landType.setText(getResources().getText(R.string.string_cropland));

        costOutlayAdapter = new CostOutlayAdapter(costOutlays, NaturalCapitalCostOutlay.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        outlayItemList.setLayoutManager(mLayoutManager);
        outlayItemList.setItemAnimator(new DefaultItemAnimator());
        outlayItemList.setAdapter(costOutlayAdapter);


        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        buttonAddWood.setOnClickListener(this);
        imageViewMenuIcon.setOnClickListener(this);
        drawerCloseBtn.setOnClickListener(this);
        surveyIdDrawer.setText(surveyId);


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
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.button_next:
//                Outlay outlayCheck = realm.where(Outlay.class)
//                        .equalTo("surveyId",surveyId)
//                        .equalTo("landKind",currentSocialCapitalSurvey)
//                        .findFirst();

                RealmResults<Outlay> outlays1 = realm.where(Outlay.class)
                        .equalTo("surveyId", surveyId)
                        .equalTo("landKind", currentSocialCapitalServey)
                        .findAll();
                if (outlays1.size() > 0) {
                    intent = new Intent(getApplicationContext(), NaturalCapitalCostOutlayB.class);
                    startActivity(intent);
                } else {
                    nextLandKind();
                }


//                intent=new Intent(getApplicationContext(),NaturalCapitalCostOutlayB.class);
//                startActivity(intent);
                break;
            case R.id.button_back:
                finish();
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

            case R.id.button_add_wood:
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_forest_revenue_item);
                dialog.setTitle(getResources().getString(R.string.natural_outlay_a_qn2));
                dialog.setCancelable(false);

                Button popupCancel = (Button) dialog.findViewById(R.id.popup_cancel);
                Button saveParticipant = (Button) dialog.findViewById(R.id.save_participant);
                final EditText editTextWood = (EditText) dialog.findViewById(R.id.edit_text_wood);
                editTextWood.setHint(getResources().getString(R.string.natural_outlay_a_qn2));

                popupCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                saveParticipant.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = editTextWood.getText().toString();

                        if (!name.equals("")) {
                            Outlay outlayCheck;
                            outlayCheck = realm.where(Outlay.class)
                                    .equalTo("surveyId", surveyId)
                                    .equalTo("landKind", currentSocialCapitalServey)
                                    .equalTo("itemName", name)
                                    .findFirst();
                            if (outlayCheck == null) {
                                realm.beginTransaction();
                                outlayCheck = realm.createObject(Outlay.class);
                                outlayCheck.setId(getNextKeyOutlay());
                                outlayCheck.setItemName(name);
                                outlayCheck.setType("Timber");
                                outlayCheck.setLandKind(landKindName);
                                outlayCheck.setSurveyId(surveyId);
                                realm.commitTransaction();
                            }


                            RealmList<OutlayYears> outlayYearses = addOutlayYears(name, outlayCheck.getId());
                            realm.beginTransaction();
                            outlayCheck.setOutlayYearses(outlayYearses);
                            realm.commitTransaction();


                            costOutlays.add(outlayCheck);
                            costOutlaysToSave.add(outlayCheck);
                            Survey surveyRevenueProduct = realm.where(Survey.class).equalTo("surveyId", surveyId).findFirst();


                            for (LandKind landKind : surveyRevenueProduct.getLandKinds()) {
                                Log.e("BBB ", landKind.getName() + " " + landKind.getForestLand());
                                if (landKind.getName().equals(getString(R.string.string_forestland)) && currentSocialCapitalServey.equals(getString(R.string.string_forestland))) {
                                    Log.e("BBB ", costOutlays.size() + "");
                                    Log.e("AAA ", landKind.getForestLand().getOutlays().toString());
                                    realm.beginTransaction();
                                    landKind.getForestLand().setOutlays(costOutlaysToSave);
                                    realm.commitTransaction();
                                } else if (landKind.getName().equals(getString(R.string.string_cropland)) && currentSocialCapitalServey.equals(getString(R.string.string_cropland))) {
                                    Log.e("BBB ", costOutlays.size() + "");
                                    Log.e("AAA ", landKind.getCropLand().getOutlays().toString());
                                    realm.beginTransaction();
                                    landKind.getCropLand().setOutlays(costOutlaysToSave);
                                    realm.commitTransaction();
                                } else if (landKind.getName().equals(getString(R.string.string_pastureland)) && currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                                    Log.e("BBB ", costOutlays.size() + "");
                                    Log.e("AAA ", landKind.getPastureLand().getOutlays().toString());
                                    realm.beginTransaction();
                                    landKind.getPastureLand().setOutlays(costOutlaysToSave);
                                    realm.commitTransaction();
                                } else if (landKind.getName().equals(getString(R.string.string_miningland)) && currentSocialCapitalServey.equals(getString(R.string.string_miningland))) {
                                    Log.e("BBB ", costOutlays.size() + "");
                                    Log.e("AAA ", landKind.getMiningLand().getOutlays().toString());
                                    realm.beginTransaction();
                                    landKind.getMiningLand().setOutlays(costOutlaysToSave);
                                    realm.commitTransaction();
                                }
                            }

//                        Survey results = realm.where(Survey.class).findFirst();
//                        for(LandKind landKind:results.getLandKinds()){
//                            if(landKind.getName().equals("Forestland") && currentSocialCapitalSurvey.equals("Forestland")){
//                                for (RevenueProduct revenueProduct1: landKind.getForestLand().getRevenueProducts()){
//                                    Log.e("LAND ", revenueProduct1.getName());
//                                }
//                            }else if(landKind.getName().equals(getString(R.string.string_cropland)) && currentSocialCapitalSurvey.equals(getString(R.string.string_cropland))){
//                                for (RevenueProduct revenueProduct1: landKind.getCropLand().getRevenueProducts()){
//                                    Log.e("LAND ", revenueProduct1.getName());
//                                }
//                            }
//                        }

                            costOutlayAdapter.notifyDataSetChanged();

//                        noParticipantLayout.setVisibility(View.GONE);
//                        participantLayout.setVisibility(View.VISIBLE);
//                        participantsAdapter.notifyDataSetChanged();
                            dialog.cancel();
                        } else {
                            Toast.makeText(context, getResources().getString(R.string.string_fill_name), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();

//                intent=new Intent(getApplicationContext(),NaturalCapitalSurveyActivityD.class);
//                startActivity(intent);
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
        Intent intent = new Intent(NaturalCapitalCostOutlay.this, StartLandTypeActivity.class);
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

    public RealmList<OutlayYears> addOutlayYears(String name, long outlayYearsId) {
        RealmList<OutlayYears> outlayYearses = new RealmList<>();
        //int yearsCount = 0;
        int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));

       /* if (currentSocialCapitalSurvey.equals("Forestland")) {
            yearsCount = 15;New
        } else if (currentSocialCapitalSurvey.equals("Cropland")) {
            yearsCount = 5;
        } else if (currentSocialCapitalSurvey.equals("Pastureland")) {
            yearsCount = 8;
        } else if (currentSocialCapitalSurvey.equals("Mining Land")) {
            yearsCount = 5;
        }*/

        for (int i = 0; i <= PROJECTION_COUNT; i++) {
            OutlayYears outlayYearsCheck;
            outlayYearsCheck = realm.where(OutlayYears.class)
                    .equalTo("outlayId", outlayYearsId)
                    .equalTo("year", year)
                    .findFirst();

            if (outlayYearsCheck == null) {
                realm.beginTransaction();
                outlayYearsCheck = realm.createObject(OutlayYears.class);
                outlayYearsCheck.setId(getNextKeyOutlayYears());
                outlayYearsCheck.setLandKind(currentSocialCapitalServey);
                outlayYearsCheck.setSurveyId(surveyId);
                outlayYearsCheck.setOutlayId(outlayYearsId);
                outlayYearsCheck.setYear(year);
                realm.commitTransaction();
            }
            outlayYearses.add(outlayYearsCheck);
            year++;
        }
        return outlayYearses;
    }

    public void nextLandKind() {
        RealmResults<LandKind> landKindRealmResults = realm.where(LandKind.class)
                .equalTo("surveyId", surveyId)
                .equalTo("status", "active")
                .findAll();
        int j = 0;
        int i = 0;
        for (LandKind landKind : landKindRealmResults) {
            Log.e("TAG ", landKind.toString());
            //Log.e(TAG, String.valueOf(survey1.getParticipants().size()));
            if (landKind.getName().equals(currentSocialCapitalServey)) {
                j = i + 1;
            }
            i++;
        }

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

//    public int getNextKeyRevenueProduct() {
//        return realm.where(RevenueProduct.class).max("id").intValue() + 1;
//    }

    public int getNextKeyOutlay() {
        return realm.where(Outlay.class).max("id").intValue() + 1;
    }

    public int getNextKeyOutlayYears() {
        return realm.where(OutlayYears.class).max("id").intValue() + 1;
    }

    public void toggleMenuDrawer() {
        if (menuDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            menuDrawerLayout.openDrawer(GravityCompat.START);
        }
    }
}
