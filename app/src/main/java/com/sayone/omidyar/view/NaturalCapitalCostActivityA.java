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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.adapter.CostAdapter;
import com.sayone.omidyar.model.CostElement;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.RevenueProduct;
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
    private TextView textViewAbout;
    private TextView logout;
    private TextView startSurvey;
    private DrawerLayout menuDrawerLayout;

    String serveyId;
    Button buttonBack,buttonNext;
    ImageView buttonAddWood;
    RealmList<CostElement> costElements;
    RealmList<CostElement> costProductsToSave;

    RecyclerView timberList;
    CostAdapter costAdapter;

    String landKindName;
    String currentSocialCapitalServey;
    private String language;
    private TextView surveyIdDrawer;

    CheckBox optionA, optionB, optionC, optionD, optionE, optionF, optionG, optionH;
    String costNameA, costNameB, costNameC, costNameD, costNameE, costNameF, costNameG, costNameH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_cost_survey_a);

        context = this;
        language = Locale.getDefault().getDisplayLanguage();
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = sharedPref.getString("surveyId","");
        currentSocialCapitalServey = sharedPref.getString("currentSocialCapitalServey","");

        costNameA = "Equipment cost";
        costNameB = "Transportation cost";
        costNameC = "Seed/seedling cost";
        costNameD = "Fertilizer cost";
        costNameE = "Water cost";
        costNameF = "Labor cost";
        costNameG = "Infrastructure cost";
        costNameH = "Tax, fees, and other costs";

        optionA = (CheckBox) findViewById(R.id.option_a);
        optionB = (CheckBox) findViewById(R.id.option_b);
        optionC = (CheckBox) findViewById(R.id.option_c);
        optionD = (CheckBox) findViewById(R.id.option_d);
        optionE = (CheckBox) findViewById(R.id.option_e);
        optionF = (CheckBox) findViewById(R.id.option_f);
        optionG = (CheckBox) findViewById(R.id.option_g);
        optionH = (CheckBox) findViewById(R.id.option_h);

        optionA.setText(costNameA);
        optionB.setText(costNameB);
        optionC.setText(costNameC);
        optionD.setText(costNameD);
        optionE.setText(costNameE);
        optionF.setText(costNameF);
        optionG.setText(costNameG);
        optionH.setText(costNameH);

        costElements = new RealmList<>();
        costProductsToSave = new RealmList<>();
        Survey survey = realm.where(Survey.class).equalTo("surveyId", serveyId).findFirst();
        for(LandKind landKind:survey.getLandKinds()){
            if(landKind.getName().equals("Forestland") && currentSocialCapitalServey.equals("Forestland")){
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for(CostElement costElement:landKind.getForestLand().getCostElements()){
                    costProductsToSave.add(costElement);
                    if(costElement.getType().equals("Timber")){
                        costElements.add(costElement);
                        loadCostCheckBox(costElement.getName());
                    }
                }
            }else if(landKind.getName().equals("Cropland") && currentSocialCapitalServey.equals("Cropland")){
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for(CostElement costElement:landKind.getCropLand().getCostElements()){
                    costProductsToSave.add(costElement);
                    if(costElement.getType().equals("Timber")){
                        costElements.add(costElement);
                        loadCostCheckBox(costElement.getName());
                    }
                }
            }else if(landKind.getName().equals("Pastureland") && currentSocialCapitalServey.equals("Pastureland")){
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for(CostElement costElement:landKind.getPastureLand().getCostElements()){
                    costProductsToSave.add(costElement);
                    if(costElement.getType().equals("Timber")){
                        costElements.add(costElement);
                        loadCostCheckBox(costElement.getName());
                    }
                }
            }else if(landKind.getName().equals("Mining Land") && currentSocialCapitalServey.equals("Mining Land")){
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for(CostElement costElement:landKind.getMiningLand().getCostElements()){
                    costProductsToSave.add(costElement);
                    if(costElement.getType().equals("Timber")){
                        costElements.add(costElement);
                        loadCostCheckBox(costElement.getName());
                    }
                }
            }
        }


        buttonBack=(Button)findViewById(R.id.button_back);
        buttonNext=(Button)findViewById(R.id.button_next);
        // buttonAddWood = (ImageView) findViewById(R.id.button_add_wood);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        textViewAbout = (TextView) findViewById(R.id.text_view_about);
        logout = (TextView) findViewById(R.id.logout);
        startSurvey=(TextView)findViewById(R.id.text_start_survey);
        surveyIdDrawer=(TextView)findViewById(R.id.text_view_id);
        landType=(TextView)findViewById(R.id.land_type);
        landType.setText(currentSocialCapitalServey);

        // timberList = (RecyclerView) findViewById(R.id.timber_list);

        costAdapter = new CostAdapter(costElements,NaturalCapitalCostActivityA.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        timberList.setLayoutManager(mLayoutManager);
//        timberList.setItemAnimator(new DefaultItemAnimator());
//        timberList.setAdapter(costAdapter);
        surveyIdDrawer.setText(serveyId);


        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        // buttonAddWood.setOnClickListener(this);
        imageViewMenuIcon.setOnClickListener(this);
        drawerCloseBtn.setOnClickListener(this);
        textViewAbout.setOnClickListener(this);
        logout.setOnClickListener(this);
        startSurvey.setOnClickListener(this);

    }

    public void loadCostCheckBox(String name){
        if(name.equals(costNameA)){
            optionA.setChecked(true);
        }
        if(name.equals(costNameB)){
            optionB.setChecked(true);
        }
        if(name.equals(costNameC)){
            optionC.setChecked(true);
        }
        if(name.equals(costNameD)){
            optionD.setChecked(true);
        }
        if(name.equals(costNameE)){
            optionE.setChecked(true);
        }
        if(name.equals(costNameF)){
            optionF.setChecked(true);
        }
        if(name.equals(costNameG)){
            optionG.setChecked(true);
        }
        if(name.equals(costNameH)){
            optionH.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.button_next:
                RealmResults<CostElement> costElements1 = realm.where(CostElement.class)
                        .equalTo("surveyId", serveyId)
                        .equalTo("landKind", currentSocialCapitalServey)
                        .findAll();
                if(costElements1.size() <= 0){
                    intent=new Intent(getApplicationContext(),NaturalCapitalCostOutlay.class);
                    startActivity(intent);
                }else{
                    intent=new Intent(getApplicationContext(),NaturalCapitalCostActivityB.class);
                    startActivity(intent);
                }
                break;

            case R.id.button_back:
                finish();
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
                Intent intentt = new Intent(getApplicationContext(),MainActivity.class);
                intentt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentt);
                break;
            case R.id.logout:
                Intent intents = new Intent(getApplicationContext(),RegistrationActivity.class);
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
                editTextWood.setHint("Add cost element");

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

                    if(!name.equals("")) {
                        realm.beginTransaction();
                        CostElement costElement = realm.createObject(CostElement.class);
                        costElement.setId(getNextKeyCostElement());
                        costElement.setName(name);
                        costElement.setType("Timber");
                        costElement.setLandKind(landKindName);
                        costElement.setSurveyId(serveyId);
                        realm.commitTransaction();

                        costElements.add(costElement);
                        costProductsToSave.add(costElement);
                        Survey surveyRevenueProduct = realm.where(Survey.class).equalTo("surveyId", serveyId).findFirst();


                        for(LandKind landKind:surveyRevenueProduct.getLandKinds()){
                            Log.e("BBB ",landKind.getName()+" "+landKind.getForestLand());
                            if(landKind.getName().equals("Forestland") && currentSocialCapitalServey.equals("Forestland")){
                                Log.e("BBB ", costElements.size()+"");
                                Log.e("AAA ",landKind.getForestLand().getCostElements().toString());
                                realm.beginTransaction();
                                landKind.getForestLand().setCostElements(costProductsToSave);
                                realm.commitTransaction();
                            }else if(landKind.getName().equals("Cropland") && currentSocialCapitalServey.equals("Cropland")){
                                Log.e("BBB ", costElements.size()+"");
                                Log.e("AAA ",landKind.getCropLand().getCostElements().toString());
                                realm.beginTransaction();
                                landKind.getCropLand().setCostElements(costProductsToSave);
                                realm.commitTransaction();
                            }else if(landKind.getName().equals("Pastureland") && currentSocialCapitalServey.equals("Pastureland")){
                                Log.e("BBB ", costElements.size()+"");
                                Log.e("AAA ",landKind.getPastureLand().getCostElements().toString());
                                realm.beginTransaction();
                                landKind.getPastureLand().setCostElements(costProductsToSave);
                                realm.commitTransaction();
                            }else if(landKind.getName().equals("Mining Land") && currentSocialCapitalServey.equals("Mining Land")){
                                Log.e("BBB ", costElements.size()+"");
                                Log.e("AAA ",landKind.getMiningLand().getCostElements().toString());
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
                    }else {
                            Toast.makeText(context,getResources().getString(R.string.string_fill_name), Toast.LENGTH_SHORT).show();

                    }
                    }
                });
                dialog.show();

//                intent=new Intent(getApplicationContext(),NaturalCapitalSurveyActivityD.class);
//                startActivity(intent);
                break;

        }
    }

    public void slectedItems(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        String costName = "";

        switch (view.getId()) {
            case R.id.option_a:
                costName = costNameA;
                if(checked){
                    saveDataCheckBox(costName);
                }else {
                    deleteDataCheckBox(costName);
                }
                break;
            case R.id.option_b:
                costName = costNameB;
                if(checked){
                    saveDataCheckBox(costName);
                }else {
                    deleteDataCheckBox(costName);
                }
                break;
            case R.id.option_c:
                costName = costNameC;
                if(checked){
                    saveDataCheckBox(costName);
                }else {
                    deleteDataCheckBox(costName);
                }
                break;
            case R.id.option_d:
                costName = costNameD;
                if(checked){
                    saveDataCheckBox(costName);
                }else {
                    deleteDataCheckBox(costName);
                }
                break;
            case R.id.option_e:
                costName = costNameE;
                if(checked){
                    saveDataCheckBox(costName);
                }else {
                    deleteDataCheckBox(costName);
                }
                break;
            case R.id.option_f:
                costName = costNameF;
                if(checked){
                    saveDataCheckBox(costName);
                }else {
                    deleteDataCheckBox(costName);
                }
                break;
            case R.id.option_g:
                costName = costNameG;
                if(checked){
                    saveDataCheckBox(costName);
                }else {
                    deleteDataCheckBox(costName);
                }
                break;
            case R.id.option_h:
                costName = costNameH;
                if(checked){
                    saveDataCheckBox(costName);
                }else {
                    deleteDataCheckBox(costName);
                }
                break;
        }
    }

    public void deleteDataCheckBox(String name){
        CostElement costElement = realm.where(CostElement.class)
                .equalTo("landKind", currentSocialCapitalServey)
                .equalTo("surveyId", serveyId)
                .equalTo("name",name)
                .findFirst();
        if(costElement != null){
            realm.beginTransaction();
            costElement.deleteFromRealm();
            realm.commitTransaction();
//            costElements.remove(costElement);
//            costProductsToSave.remove(costElement);
        }
    }

    public void saveDataCheckBox(String name){
        if(!name.equals("")) {
            realm.beginTransaction();
            CostElement costElement = realm.createObject(CostElement.class);
            costElement.setId(getNextKeyCostElement());
            costElement.setName(name);
            costElement.setType("Timber");
            costElement.setLandKind(landKindName);
            costElement.setSurveyId(serveyId);
            realm.commitTransaction();

            costElements.add(costElement);
            costProductsToSave.add(costElement);
            Survey surveyRevenueProduct = realm.where(Survey.class).equalTo("surveyId", serveyId).findFirst();


            for(LandKind landKind:surveyRevenueProduct.getLandKinds()){
                Log.e("BBB ",landKind.getName()+" "+landKind.getForestLand());
                if(landKind.getName().equals("Forestland") && currentSocialCapitalServey.equals("Forestland")){
                    Log.e("BBB ", costElements.size()+"");
                    Log.e("AAA ",landKind.getForestLand().getCostElements().toString());
                    realm.beginTransaction();
                    landKind.getForestLand().setCostElements(costProductsToSave);
                    realm.commitTransaction();
                }else if(landKind.getName().equals("Cropland") && currentSocialCapitalServey.equals("Cropland")){
                    Log.e("BBB ", costElements.size()+"");
                    Log.e("AAA ",landKind.getCropLand().getCostElements().toString());
                    realm.beginTransaction();
                    landKind.getCropLand().setCostElements(costProductsToSave);
                    realm.commitTransaction();
                }else if(landKind.getName().equals("Pastureland") && currentSocialCapitalServey.equals("Pastureland")){
                    Log.e("BBB ", costElements.size()+"");
                    Log.e("AAA ",landKind.getPastureLand().getCostElements().toString());
                    realm.beginTransaction();
                    landKind.getPastureLand().setCostElements(costProductsToSave);
                    realm.commitTransaction();
                }else if(landKind.getName().equals("Mining Land") && currentSocialCapitalServey.equals("Mining Land")){
                    Log.e("BBB ", costElements.size()+"");
                    Log.e("AAA ",landKind.getMiningLand().getCostElements().toString());
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

            // costAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(context,getResources().getString(R.string.string_fill_name), Toast.LENGTH_SHORT).show();

        }
    }

    public int getNextKeyCostElement() {
        return realm.where(CostElement.class).max("id").intValue() + 1;
    }
    public void toggleMenuDrawer(){
        if(menuDrawerLayout.isDrawerOpen(GravityCompat.START)){
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            menuDrawerLayout.openDrawer(GravityCompat.START);
        }
    }
}
