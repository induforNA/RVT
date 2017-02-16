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
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.SharedCostElement;
import com.sayone.omidyar.model.Survey;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class NaturalCapitalSharedCostActivityA extends BaseActivity implements View.OnClickListener {

    private Context context;
    private Realm realm;
    private SharedPreferences sharedPref;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView landType;
    private TextView textViewAbout;
    private TextView logout;
    private TextView startSurvey;
    private DrawerLayout menuDrawerLayout;

    private String serveyId;
    private Button buttonBack, buttonNext;
    private ImageView buttonAddWood;
    private RealmList<SharedCostElement> costElements;
    private RealmList<SharedCostElement> costProductsToSave;

    private RecyclerView timberList;
    private CostAdapter costAdapter;

    private String currentSocialCapitalServey;
    private String language;
    private TextView surveyIdDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_shared_cost_survey_a);

        context = this;
        language = Locale.getDefault().getDisplayLanguage();
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = sharedPref.getString("surveyId", "");
        currentSocialCapitalServey = sharedPref.getString("currentSocialCapitalServey", "");

        costElements = new RealmList<>();
        costProductsToSave = new RealmList<>();
        Survey survey = realm.where(Survey.class).equalTo("surveyId", serveyId).findFirst();
        for (SharedCostElement costElement : survey.getSharedCostElements()) {
            costProductsToSave.add(costElement);
            if (costElement.getType().equals("Timber")) {
                costElements.add(costElement);
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


        if (currentSocialCapitalServey.equals(getString(R.string.string_forestland)))
            landType.setText(getResources().getText(R.string.string_forestland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland)))
            landType.setText(getResources().getText(R.string.string_pastureland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_miningland)))
            landType.setText(getResources().getText(R.string.string_miningland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_cropland)))
            landType.setText(getResources().getText(R.string.string_cropland));

        timberList = (RecyclerView) findViewById(R.id.timber_list);

        costAdapter = new CostAdapter(costElements, NaturalCapitalSharedCostActivityA.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        timberList.setLayoutManager(mLayoutManager);
        timberList.setItemAnimator(new DefaultItemAnimator());
        timberList.setAdapter(costAdapter);
        surveyIdDrawer.setText(serveyId);

        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        buttonAddWood.setOnClickListener(this);
        imageViewMenuIcon.setOnClickListener(this);
        drawerCloseBtn.setOnClickListener(this);
        textViewAbout.setOnClickListener(this);
        logout.setOnClickListener(this);
        startSurvey.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.button_next:
                RealmResults<SharedCostElement> costElements1 = realm.where(SharedCostElement.class)
                        .equalTo("surveyId", serveyId)
                        .findAll();
                if (costElements1.size() <= 0) {
                    intent = new Intent(getApplicationContext(), NaturalCapitalCostOutlay.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getApplicationContext(), NaturalCapitalSharedCostActivityB.class);
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
                            SharedCostElement costElement = realm.createObject(SharedCostElement.class);
                            costElement.setId(getNextKeyCostElement());
                            costElement.setName(name);
                            costElement.setType("");
                            costElement.setLandKind("");
                            costElement.setSurveyId(serveyId);
                            realm.commitTransaction();

                            costElements.add(costElement);
                            costProductsToSave.add(costElement);
                            Survey surveyRevenueProduct = realm.where(Survey.class).equalTo("surveyId", serveyId).findFirst();


                            realm.beginTransaction();
                            surveyRevenueProduct.setSharedCostElements(costProductsToSave);
                            realm.commitTransaction();

                            costAdapter.notifyDataSetChanged();
                            dialog.cancel();
                        } else {
                            Toast.makeText(context, getResources().getString(R.string.string_fill_name), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                dialog.show();
                break;
        }
    }

    public int getNextKeyCostElement() {
        return realm.where(SharedCostElement.class).max("id").intValue() + 1;
    }

    public void toggleMenuDrawer() {
        if (menuDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            menuDrawerLayout.openDrawer(GravityCompat.START);
        }
    }
}