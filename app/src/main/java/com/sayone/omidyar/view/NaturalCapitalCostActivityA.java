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
import com.sayone.omidyar.model.RevenueProduct;
import com.sayone.omidyar.model.Survey;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;

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
                    }
                }
            }else if(landKind.getName().equals("Cropland") && currentSocialCapitalServey.equals("Cropland")){
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for(CostElement costElement:landKind.getCropLand().getCostElements()){
                    costProductsToSave.add(costElement);
                    if(costElement.getType().equals("Timber")){
                        costElements.add(costElement);
                    }
                }
            }else if(landKind.getName().equals("Pastureland") && currentSocialCapitalServey.equals("Pastureland")){
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for(CostElement costElement:landKind.getPastureLand().getCostElements()){
                    costProductsToSave.add(costElement);
                    if(costElement.getType().equals("Timber")){
                        costElements.add(costElement);
                    }
                }
            }else if(landKind.getName().equals("Mining Land") && currentSocialCapitalServey.equals("Mining Land")){
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for(CostElement costElement:landKind.getMiningLand().getCostElements()){
                    costProductsToSave.add(costElement);
                    if(costElement.getType().equals("Timber")){
                        costElements.add(costElement);
                    }
                }
            }
        }


        buttonBack=(Button)findViewById(R.id.button_back);
        buttonNext=(Button)findViewById(R.id.button_next);
        buttonAddWood = (ImageView) findViewById(R.id.button_add_wood);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        textViewAbout = (TextView) findViewById(R.id.text_view_about);
        logout = (TextView) findViewById(R.id.logout);
        startSurvey=(TextView)findViewById(R.id.text_start_survey);
        surveyIdDrawer=(TextView)findViewById(R.id.text_view_id);
        landType=(TextView)findViewById(R.id.land_type);
        landType.setText(currentSocialCapitalServey);

        timberList = (RecyclerView) findViewById(R.id.timber_list);

        costAdapter = new CostAdapter(costElements);

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
                intent=new Intent(getApplicationContext(),NaturalCapitalCostActivityB.class);
                startActivity(intent);
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
