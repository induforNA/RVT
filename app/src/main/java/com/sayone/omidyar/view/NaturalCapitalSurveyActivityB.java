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
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.RevenueProduct;
import com.sayone.omidyar.model.Survey;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class NaturalCapitalSurveyActivityB extends BaseActivity implements View.OnClickListener {

    Context context;
    Realm realm;
    SharedPreferences sharedPref;

    String surveyId;
    Button buttonBack, buttonNext;
    ImageView buttonAddWood;
    RealmList<RevenueProduct> revenueProducts;
    RealmList<RevenueProduct> revenueProductsToSave;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView surveyIdDrawer;
    private DrawerLayout menuDrawerLayout;

    RecyclerView timberList;
    RevenueAdapterB revenueAdapter;
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
        setContentView(R.layout.activity_natural_capital_survey_b);

        context = this;
        language = Locale.getDefault().getDisplayLanguage();
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        surveyId = sharedPref.getString("surveyId", "");
        currentSocialCapitalServey = sharedPref.getString("currentSocialCapitalSurvey", "");

        revenueProducts = new RealmList<>();
        revenueProductsToSave = new RealmList<>();
        Survey survey = realm.where(Survey.class).equalTo("surveyId", surveyId).findFirst();
        for (LandKind landKind : survey.getLandKinds()) {
            if (landKind.getName().equals(getString(R.string.string_forestland) ) && currentSocialCapitalServey.equals(getString(R.string.string_forestland) )) {
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for (RevenueProduct revenueProduct : landKind.getForestLand().getRevenueProducts()) {
                    revenueProductsToSave.add(revenueProduct);
                    if (revenueProduct.getType().equals("Non Timber")) {
                        revenueProducts.add(revenueProduct);
                    }
                }
            } else if (landKind.getName().equals(getString(R.string.string_cropland) ) && currentSocialCapitalServey.equals(getString(R.string.string_cropland) )) {
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for (RevenueProduct revenueProduct : landKind.getCropLand().getRevenueProducts()) {
                    revenueProductsToSave.add(revenueProduct);
                    if (revenueProduct.getType().equals("Non Timber")) {
                        revenueProducts.add(revenueProduct);
                    }                
                }
            } else if (landKind.getName().equals(getString(R.string.string_pastureland) ) && currentSocialCapitalServey.equals(getString(R.string.string_pastureland) )) {
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for (RevenueProduct revenueProduct : landKind.getPastureLand().getRevenueProducts()) {
                    revenueProductsToSave.add(revenueProduct);
                    if (revenueProduct.getType().equals("Livestock")) {
                        revenueProducts.add(revenueProduct);
                    }
                }
            } else if (landKind.getName().equals(getString(R.string.string_miningland)) && currentSocialCapitalServey.equals(getString(R.string.string_miningland))) {
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for (RevenueProduct revenueProduct : landKind.getMiningLand().getRevenueProducts()) {
                    revenueProductsToSave.add(revenueProduct);
                    if (revenueProduct.getType().equals("Timber")) {
                        revenueProducts.add(revenueProduct);
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

        timberList = (RecyclerView) findViewById(R.id.timber_list);

        revenueAdapter = new RevenueAdapterB(revenueProducts, NaturalCapitalSurveyActivityB.this);
        if (currentSocialCapitalServey.equals(getString(R.string.string_forestland) )) {
            questionRevenue.setText(getResources().getString(R.string.qn_natural_b));

        } else if (currentSocialCapitalServey.equals(getString(R.string.string_cropland) )) {
            questionRevenue.setText(getResources().getString(R.string.qn_natural_b));

        } else if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland) )) {
            questionRevenue.setText(getResources().getString(R.string.qn_natural_b));

        } else if (currentSocialCapitalServey.equals(getString(R.string.string_miningland))) {
            questionRevenue.setText(getResources().getString(R.string.qn_natural_b));

        }

        // landType.setText(currentSocialCapitalSurvey);
        if (currentSocialCapitalServey.equals(getString(R.string.string_forestland) ))
            landType.setText(getResources().getText(R.string.string_forestland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland) ))
            landType.setText(getResources().getText(R.string.string_pastureland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_miningland)))
            landType.setText(getResources().getText(R.string.string_miningland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_cropland) ))
            landType.setText(getResources().getText(R.string.title_cropland));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        timberList.setLayoutManager(mLayoutManager);
        timberList.setItemAnimator(new DefaultItemAnimator());
        timberList.setAdapter(revenueAdapter);

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
//                if(currentSocialCapitalSurvey.equals("Forestland")){
//                    intent=new Intent(getApplicationContext(),NaturalCapitalSurveyActivityB.class);
//                    startActivity(intent);
//                }else{
                // Survey surveyRevenueProduct = realm.where(Survey.class).equalTo("surveyId", surveyId).findFirst();
//                    RevenueProduct revenueProduct = realm.where(RevenueProduct.class)
//                            .equalTo("surveyId", surveyId)
//                            .equalTo("landKind", currentSocialCapitalSurvey)
//                            .findFirst();

                RealmResults<RevenueProduct> revenueProducts1 = realm.where(RevenueProduct.class)
                        .equalTo("surveyId", surveyId)
                        .equalTo("landKind", currentSocialCapitalServey)
                        .findAll();
                if (revenueProducts1.size() <= 0) {
                    intent = new Intent(getApplicationContext(), NaturalCapitalCostActivityA.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getApplicationContext(), NaturalCapitalSurveyActivityC.class);
                    startActivity(intent);
                }
//                }
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
                dialog.setTitle(getResources().getString(R.string.string_add_revenue));
                dialog.setCancelable(false);

                Button popupCancel = (Button) dialog.findViewById(R.id.popup_cancel);
                Button saveParticipant = (Button) dialog.findViewById(R.id.save_participant);
                final EditText editTextWood = (EditText) dialog.findViewById(R.id.edit_text_wood);
                if (currentSocialCapitalServey.equals(getString(R.string.string_forestland) )) {
                    editTextWood.setHint(getResources().getString(R.string.hint_add_nftp));
                } else if (currentSocialCapitalServey.equals(getString(R.string.string_cropland) )) {
                    editTextWood.setHint(getResources().getString(R.string.hint_add_croptype));
                } else if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland) )) {
                    editTextWood.setHint(getResources().getString(R.string.hint_add_livestock));
                } else if (currentSocialCapitalServey.equals(getString(R.string.string_miningland))) {
                    editTextWood.setHint(getResources().getString(R.string.hint_add_mineral));
                }

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
                            RevenueProduct revenueProduct = realm.createObject(RevenueProduct.class);
                            revenueProduct.setId(getNextKeyRevenueProduct());
                            revenueProduct.setName(name);
                            if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland) )) {
                                revenueProduct.setType("Livestock");
                            } else {
                                revenueProduct.setType("Non Timber");
                            }
                            revenueProduct.setLandKind(landKindName);
                            revenueProduct.setSurveyId(surveyId);
                            realm.commitTransaction();

                            revenueProducts.add(revenueProduct);
                            revenueProductsToSave.add(revenueProduct);
                            Survey surveyRevenueProduct = realm.where(Survey.class).equalTo("surveyId", surveyId).findFirst();


                            for (LandKind landKind : surveyRevenueProduct.getLandKinds()) {
                                Log.e("BBB ", landKind.getName() + " " + landKind.getForestLand());
                                if (landKind.getName().equals(getString(R.string.string_forestland) ) && currentSocialCapitalServey.equals(getString(R.string.string_forestland) )) {
                                    Log.e("BBB ", revenueProducts.size() + "");
                                    Log.e("AAA ", landKind.getForestLand().getRevenueProducts().toString());
                                    realm.beginTransaction();
                                    landKind.getForestLand().setRevenueProducts(revenueProductsToSave);
                                    realm.commitTransaction();
                                } else if (landKind.getName().equals(getString(R.string.string_cropland) ) && currentSocialCapitalServey.equals(getString(R.string.string_cropland) )) {
                                    Log.e("BBB ", revenueProducts.size() + "");
                                    Log.e("AAA ", landKind.getCropLand().getRevenueProducts().toString());
                                    realm.beginTransaction();
                                    landKind.getCropLand().setRevenueProducts(revenueProductsToSave);
                                    realm.commitTransaction();
                                } else if (landKind.getName().equals(getString(R.string.string_pastureland) ) && currentSocialCapitalServey.equals(getString(R.string.string_pastureland) )) {
                                    Log.e("BBB ", revenueProducts.size() + "");
                                    Log.e("AAA ", landKind.getPastureLand().getRevenueProducts().toString());
                                    realm.beginTransaction();
                                    landKind.getPastureLand().setRevenueProducts(revenueProductsToSave);
                                    realm.commitTransaction();
                                } else if (landKind.getName().equals(getString(R.string.string_miningland)) && currentSocialCapitalServey.equals(getString(R.string.string_miningland))) {
                                    Log.e("BBB ", revenueProducts.size() + "");
                                    Log.e("AAA ", landKind.getMiningLand().getRevenueProducts().toString());
                                    realm.beginTransaction();
                                    landKind.getMiningLand().setRevenueProducts(revenueProductsToSave);
                                    realm.commitTransaction();
                                }
                            }

//                        Survey results = realm.where(Survey.class).findFirst();
//                        for(LandKind landKind:results.getLandKinds()){
//                            if(landKind.getName().equals("Forestland") && currentSocialCapitalSurvey.equals("Forestland")){
//                                for (RevenueProduct revenueProduct1: landKind.getForestLand().getRevenueProducts()){
//                                    Log.e("LAND ", revenueProduct1.getName());
//                                }
//                            }else if(landKind.getName().equals(getString(R.string.string_cropland) ) && currentSocialCapitalSurvey.equals(getString(R.string.string_cropland) )){
//                                for (RevenueProduct revenueProduct1: landKind.getCropLand().getRevenueProducts()){
//                                    Log.e("LAND ", revenueProduct1.getName());
//                                }
//                            }
//                        }

                            revenueAdapter.notifyDataSetChanged();

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
        Intent intent = new Intent(NaturalCapitalSurveyActivityB.this, StartLandTypeActivity.class);
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

    public int getNextKeyRevenueProduct() {
        return realm.where(RevenueProduct.class).max("id").intValue() + 1;
    }

    public void toggleMenuDrawer() {
        if (menuDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            menuDrawerLayout.openDrawer(GravityCompat.START);
        }
    }
}
