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
import com.sayone.omidyar.adapter.CostAdapter;
import com.sayone.omidyar.model.CostElement;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.Survey;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class NaturalCapitalCostActivityA extends BaseActivity implements View.OnClickListener {

    Context context;
    Realm realm;
    SharedPreferences sharedPref;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView landType;
    private DrawerLayout menuDrawerLayout;

    String surveyId;
    Button buttonBack, buttonNext;
    ImageView buttonAddWood;
    RealmList<CostElement> costElements;
    RealmList<CostElement> costProductsToSave;

    RecyclerView timberList;
    CostAdapter costAdapter;

    String landKindName;
    String currentSocialCapitalSurvey;
    private String language;
    private TextView surveyIdDrawer;

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
        setContentView(R.layout.activity_natural_cost_survey_a);

        context = this;
        language = Locale.getDefault().getDisplayLanguage();
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        surveyId = sharedPref.getString("surveyId", "");
        currentSocialCapitalSurvey = sharedPref.getString("currentSocialCapitalSurvey", "");

        costElements = new RealmList<>();
        costProductsToSave = new RealmList<>();
        Survey survey = realm.where(Survey.class).equalTo("surveyId", surveyId).findFirst();
        for (LandKind landKind : survey.getLandKinds()) {
            if (landKind.getName().equals(getString(R.string.string_forestland)) && currentSocialCapitalSurvey.equals(getString(R.string.string_forestland))) {
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for (CostElement costElement : landKind.getForestLand().getCostElements()) {
                    costProductsToSave.add(costElement);
                    if (costElement.getType().equals("Timber")) {
                        costElements.add(costElement);
                    }
                }
            } else if (landKind.getName().equals(getString(R.string.string_cropland)) && currentSocialCapitalSurvey.equals(getString(R.string.string_cropland))) {
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for (CostElement costElement : landKind.getCropLand().getCostElements()) {
                    costProductsToSave.add(costElement);
                    if (costElement.getType().equals("Timber")) {
                        costElements.add(costElement);
                    }
                }
            } else if (landKind.getName().equals(getString(R.string.string_pastureland)) && currentSocialCapitalSurvey.equals(getString(R.string.string_pastureland))) {
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for (CostElement costElement : landKind.getPastureLand().getCostElements()) {
                    costProductsToSave.add(costElement);
                    if (costElement.getType().equals("Timber")) {
                        costElements.add(costElement);
                    }
                }
            } else if (landKind.getName().equals(getString(R.string.string_miningland)) && currentSocialCapitalSurvey.equals(getString(R.string.string_miningland))) {
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for (CostElement costElement : landKind.getMiningLand().getCostElements()) {
                    costProductsToSave.add(costElement);
                    if (costElement.getType().equals("Timber")) {
                        costElements.add(costElement);
                    }
                }
            }
        }

        buttonBack = (Button) findViewById(R.id.button_back);
        buttonNext = (Button) findViewById(R.id.button_next);
        buttonAddWood = (ImageView) findViewById(R.id.button_add_wood);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        textViewAbout = (TextView) findViewById(R.id.text_view_about);
        logout = (TextView) findViewById(R.id.logout);
        startSurvey = (TextView) findViewById(R.id.text_start_survey);
        surveyIdDrawer = (TextView) findViewById(R.id.text_view_id);
        landType = (TextView) findViewById(R.id.land_type);


        if (currentSocialCapitalSurvey.equals(getString(R.string.string_forestland)))
            landType.setText(getResources().getText(R.string.string_forestland));
        if (currentSocialCapitalSurvey.equals(getString(R.string.string_pastureland)))
            landType.setText(getResources().getText(R.string.string_pastureland));
        if (currentSocialCapitalSurvey.equals(getString(R.string.string_miningland)))
            landType.setText(getResources().getText(R.string.string_miningland));
        if (currentSocialCapitalSurvey.equals(getString(R.string.string_cropland)))
            landType.setText(getResources().getText(R.string.string_cropland));
        //  landType.setText(currentSocialCapitalSurvey);

        timberList = (RecyclerView) findViewById(R.id.timber_list);

        costAdapter = new CostAdapter(costElements, NaturalCapitalCostActivityA.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        timberList.setLayoutManager(mLayoutManager);
        timberList.setItemAnimator(new DefaultItemAnimator());
        timberList.setAdapter(costAdapter);
        surveyIdDrawer.setText(surveyId);


        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        buttonAddWood.setOnClickListener(this);
        imageViewMenuIcon.setOnClickListener(this);
        drawerCloseBtn.setOnClickListener(this);
        textViewAbout.setOnClickListener(this);
        logout.setOnClickListener(this);
        startSurvey.setOnClickListener(this);

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
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.button_next:
                RealmResults<CostElement> costElements1 = realm.where(CostElement.class)
                        .equalTo("surveyId", surveyId)
                        .equalTo("landKind", currentSocialCapitalSurvey)
                        .findAll();
                if (costElements1.size() <= 0) {
                    intent = new Intent(getApplicationContext(), NaturalCapitalCostOutlay.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getApplicationContext(), NaturalCapitalCostActivityB.class);
                    startActivity(intent);
                }
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
                dialog.setTitle(getResources().getString(R.string.string_add_cost_element));
                dialog.setCancelable(false);
                Button popupCancel = (Button) dialog.findViewById(R.id.popup_cancel);
                Button saveParticipant = (Button) dialog.findViewById(R.id.save_participant);
                final EditText editTextWood = (EditText) dialog.findViewById(R.id.edit_text_wood);
                editTextWood.setHint(getResources().getString(R.string.string_add_cost_element));

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
                            realm.beginTransaction();
                            CostElement costElement = realm.createObject(CostElement.class);
                            costElement.setId(getNextKeyCostElement());
                            costElement.setName(name);
                            costElement.setType("Timber");
                            costElement.setLandKind(landKindName);
                            costElement.setSurveyId(surveyId);
                            realm.commitTransaction();

                            costElements.add(costElement);
                            costProductsToSave.add(costElement);
                            Survey surveyRevenueProduct = realm.where(Survey.class).equalTo("surveyId", surveyId).findFirst();


                            for (LandKind landKind : surveyRevenueProduct.getLandKinds()) {
                                Log.e("BBB ", landKind.getName() + " " + landKind.getForestLand());
                                if (landKind.getName().equals(getString(R.string.string_forestland)) && currentSocialCapitalSurvey.equals(getString(R.string.string_forestland))) {
                                    Log.e("BBB ", costElements.size() + "");
                                    Log.e("AAA ", landKind.getForestLand().getCostElements().toString());
                                    realm.beginTransaction();
                                    landKind.getForestLand().setCostElements(costProductsToSave);
                                    realm.commitTransaction();
                                } else if (landKind.getName().equals(getString(R.string.string_cropland)) && currentSocialCapitalSurvey.equals(getString(R.string.string_cropland))) {
                                    Log.e("BBB ", costElements.size() + "");
                                    Log.e("AAA ", landKind.getCropLand().getCostElements().toString());
                                    realm.beginTransaction();
                                    landKind.getCropLand().setCostElements(costProductsToSave);
                                    realm.commitTransaction();
                                } else if (landKind.getName().equals(getString(R.string.string_pastureland)) && currentSocialCapitalSurvey.equals(getString(R.string.string_pastureland))) {
                                    Log.e("BBB ", costElements.size() + "");
                                    Log.e("AAA ", landKind.getPastureLand().getCostElements().toString());
                                    realm.beginTransaction();
                                    landKind.getPastureLand().setCostElements(costProductsToSave);
                                    realm.commitTransaction();
                                } else if (landKind.getName().equals(getString(R.string.string_miningland)) && currentSocialCapitalSurvey.equals(getString(R.string.string_miningland))) {
                                    Log.e("BBB ", costElements.size() + "");
                                    Log.e("AAA ", landKind.getMiningLand().getCostElements().toString());
                                    realm.beginTransaction();
                                    landKind.getMiningLand().setCostElements(costProductsToSave);
                                    realm.commitTransaction();
                                }
                            }

//                        Survey results = realm.where(Survey.class).findFirst();
//                        for(LandKind landKind:results.getLandKinds()){
//                            if(landKind.getName().equals("Forestland")){
//                                for (CostElement costElement1: landKind.getForestLand().getCostElements()){
//                                    Log.e("LAND ", costElement1.getName());
//                                }
//                            }
//                        }

                            costAdapter.notifyDataSetChanged();

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
                Intent intent_outlay = new Intent(getApplicationContext(), NaturalCapitalSharedCostActivityA.class);
                startActivity(intent_outlay);
                break;
            case R.id.text_certificate:
                Intent intent_certificate = new Intent(getApplicationContext(),NewCertificateActivity.class);
                startActivity(intent_certificate);
                break;
        }
    }

    @Override
    protected void onResume() {
        setNav();
        super.onResume();
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
        Intent intent = new Intent(NaturalCapitalCostActivityA.this, StartLandTypeActivity.class);
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

    public int getNextKeyCostElement() {
        return realm.where(CostElement.class).max("id").intValue() + 1;
    }

    public void toggleMenuDrawer() {
        if (menuDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            menuDrawerLayout.openDrawer(GravityCompat.START);
        }
    }
}