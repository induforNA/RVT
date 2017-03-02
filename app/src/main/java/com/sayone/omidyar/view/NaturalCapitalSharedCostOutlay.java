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

public class NaturalCapitalSharedCostOutlay extends BaseActivity {

    private static final int PROJECTION_COUNT = 15;
    private Context context;
    private Realm realm;
    private SharedPreferences sharedPref;

    private String surveyId;
    private Button buttonBack, buttonNext;
    private ImageView buttonAddWood;
    private RealmList<Outlay> costOutlays;
    private RealmList<Outlay> costOutlaysToSave;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView surveyIdDrawer;
    private DrawerLayout menuDrawerLayout;

    private RecyclerView outlayItemList;
    private CostOutlayAdapter costOutlayAdapter;
    private TextView landType;
    private TextView questionRevenue;

    private String currentSocialCapitalServey;
    private String language;
    private Survey survey;

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
        survey = realm.where(Survey.class).equalTo("surveyId", surveyId).findFirst();

        for (Outlay outlay : survey.getSharedOutlays()) {
            costOutlaysToSave.add(outlay);
            costOutlays.add(outlay);
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

        landType.setText(getResources().getText(R.string.shared_costs_outlays));
        questionRevenue.setText(getResources().getText(R.string.shared_outlay_question));

        costOutlayAdapter = new CostOutlayAdapter(costOutlays, this);

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

                RealmResults<Outlay> outlays1 = realm.where(Outlay.class)
                        .equalTo("surveyId", surveyId)
                        .equalTo("landKind", "")
                        .findAll();
                if (outlays1.size() > 0) {
                    intent = new Intent(getApplicationContext(), NaturalCapitalSharedCostOutlayB.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getApplicationContext(), NewCertificateActivity.class);
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
                                    .equalTo("landKind", "")
                                    .equalTo("itemName", name)
                                    .findFirst();
                            if (outlayCheck == null) {
                                realm.beginTransaction();
                                outlayCheck = realm.createObject(Outlay.class);
                                outlayCheck.setId(getNextKeyOutlay());
                                outlayCheck.setItemName(name);
                                outlayCheck.setType("");
                                outlayCheck.setLandKind("");
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

                            realm.beginTransaction();
                            surveyRevenueProduct.setSharedOutlays(costOutlaysToSave);
                            realm.commitTransaction();

                            costOutlayAdapter.notifyDataSetChanged();
                            dialog.cancel();
                        } else {
                            Toast.makeText(context, getResources().getString(R.string.string_fill_name), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
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
        Intent intent = new Intent(NaturalCapitalSharedCostOutlay.this, StartLandTypeActivity.class);
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
        int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));

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
                outlayYearsCheck.setLandKind("");
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
            if (landKind.getName().equals("")) {
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
