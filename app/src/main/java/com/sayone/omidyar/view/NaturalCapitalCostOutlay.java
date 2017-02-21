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

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by sayone on 18/10/16.
 */

public class NaturalCapitalCostOutlay extends BaseActivity {

    Context context;
    Realm realm;
    SharedPreferences sharedPref;

    String serveyId;
    Button buttonBack,buttonNext;
    ImageView buttonAddWood;
    RealmList<Outlay> costOutlays;
    RealmList<Outlay> costOutlaysToSave;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView textViewAbout;
    private TextView logout;
    private TextView startSurvey;
    private TextView surveyIdDrawer;
    private DrawerLayout menuDrawerLayout;

    RecyclerView outlayItemList;
    CostOutlayAdapter costOutlayAdapter;
    TextView landType;
    TextView questionRevenue;

    String landKindName;
    String currentSocialCapitalServey;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_capital_cost_outlay);

        context = this;
        language = Locale.getDefault().getDisplayLanguage();
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = sharedPref.getString("surveyId","");
        currentSocialCapitalServey = sharedPref.getString("currentSocialCapitalServey","");

        costOutlays = new RealmList<>();
        costOutlaysToSave = new RealmList<>();
        Survey survey = realm.where(Survey.class).equalTo("surveyId", serveyId).findFirst();
        for(LandKind landKind:survey.getLandKinds()){
            if(landKind.getName().equals("Forestland") && currentSocialCapitalServey.equals("Forestland")){
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for(Outlay outlay:landKind.getForestLand().getOutlays()){
                    costOutlaysToSave.add(outlay);
                    if(outlay.getType().equals("Timber")){
                        costOutlays.add(outlay);
                    }
                }
            }else if(landKind.getName().equals("Cropland") && currentSocialCapitalServey.equals("Cropland")){
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for(Outlay outlay:landKind.getCropLand().getOutlays()){
                    costOutlaysToSave.add(outlay);
                    if(outlay.getType().equals("Timber")){
                        costOutlays.add(outlay);
                    }
                }
            }else if(landKind.getName().equals("Pastureland") && currentSocialCapitalServey.equals("Pastureland")){
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for(Outlay outlay:landKind.getPastureLand().getOutlays()){
                    costOutlaysToSave.add(outlay);
                    if(outlay.getType().equals("Timber")){
                        costOutlays.add(outlay);
                    }
                }
            }else if(landKind.getName().equals("Mining Land") && currentSocialCapitalServey.equals("Mining Land")){
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for(Outlay outlay:landKind.getMiningLand().getOutlays()){
                    costOutlaysToSave.add(outlay);
                    if(outlay.getType().equals("Timber")){
                        costOutlays.add(outlay);
                    }
                }
            }
        }


        buttonBack=(Button)findViewById(R.id.button_back);
        buttonNext=(Button)findViewById(R.id.button_next);
        buttonAddWood = (ImageView) findViewById(R.id.button_add_wood);
        landType = (TextView) findViewById(R.id.land_type);
        questionRevenue = (TextView) findViewById(R.id.question_revenue);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        textViewAbout = (TextView) findViewById(R.id.text_view_about);
        logout = (TextView) findViewById(R.id.logout);
        startSurvey=(TextView)findViewById(R.id.text_start_survey);
        surveyIdDrawer=(TextView)findViewById(R.id.text_view_id);

        outlayItemList = (RecyclerView) findViewById(R.id.timber_list);

//        if(currentSocialCapitalServey.equals("Forestland")){
//            questionRevenue.setText("Add investment/cost outlay items");
//
//        }else if(currentSocialCapitalServey.equals("Cropland")){
//            questionRevenue.setText("Add investment/cost outlay items");
//
//        }else if(currentSocialCapitalServey.equals("Pastureland")){
//            questionRevenue.setText("Add investment/cost outlay items");
//
//        }else if(currentSocialCapitalServey.equals("Mining Land")){
//            questionRevenue.setText("Add investment/cost outlay items");
//
//        }

 //       landType.setText(currentSocialCapitalServey);
        if(currentSocialCapitalServey.equals("Forestland"))
            landType.setText(getResources().getText(R.string.string_forestland));
        if(currentSocialCapitalServey.equals("Pastureland"))
            landType.setText(getResources().getText(R.string.string_pastureland));
        if(currentSocialCapitalServey.equals("Mining land"))
            landType.setText(getResources().getText(R.string.string_miningland));
        if(currentSocialCapitalServey.equals("Cropland"))
            landType.setText(getResources().getText(R.string.title_cropland));

        costOutlayAdapter = new CostOutlayAdapter(costOutlays,NaturalCapitalCostOutlay.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        outlayItemList.setLayoutManager(mLayoutManager);
        outlayItemList.setItemAnimator(new DefaultItemAnimator());
        outlayItemList.setAdapter(costOutlayAdapter);


        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        buttonAddWood.setOnClickListener(this);
        imageViewMenuIcon.setOnClickListener(this);
        drawerCloseBtn.setOnClickListener(this);
        textViewAbout.setOnClickListener(this);
        logout.setOnClickListener(this);
        startSurvey.setOnClickListener(this);
        surveyIdDrawer.setText(serveyId);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.button_next:
//                Outlay outlayCheck = realm.where(Outlay.class)
//                        .equalTo("surveyId",serveyId)
//                        .equalTo("landKind",currentSocialCapitalServey)
//                        .findFirst();

                RealmResults<Outlay> outlays1 = realm.where(Outlay.class)
                    .equalTo("surveyId",serveyId)
                    .equalTo("landKind",currentSocialCapitalServey)
                    .findAll();
                if(outlays1.size() > 0){
                    intent=new Intent(getApplicationContext(),NaturalCapitalCostOutlayB.class);
                    startActivity(intent);
                }else{
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

                        if(!name.equals("")) {
                            Outlay outlayCheck;
                            outlayCheck = realm.where(Outlay.class)
                                    .equalTo("surveyId",serveyId)
                                    .equalTo("landKind",currentSocialCapitalServey)
                                    .equalTo("itemName",name)
                                    .findFirst();
                            if(outlayCheck == null){
                                realm.beginTransaction();
                                outlayCheck = realm.createObject(Outlay.class);
                                outlayCheck.setId(getNextKeyOutlay());
                                outlayCheck.setItemName(name);
                                outlayCheck.setType("Timber");
                                outlayCheck.setLandKind(landKindName);
                                outlayCheck.setSurveyId(serveyId);
                                realm.commitTransaction();
                            }


                            RealmList<OutlayYears> outlayYearses = addOutlayYears(name, outlayCheck.getId());
                            realm.beginTransaction();
                            outlayCheck.setOutlayYearses(outlayYearses);
                            realm.commitTransaction();




                            costOutlays.add(outlayCheck);
                            costOutlaysToSave.add(outlayCheck);
                            Survey surveyRevenueProduct = realm.where(Survey.class).equalTo("surveyId", serveyId).findFirst();


                            for(LandKind landKind:surveyRevenueProduct.getLandKinds()){
                                Log.e("BBB ",landKind.getName()+" "+landKind.getForestLand());
                                if(landKind.getName().equals("Forestland") && currentSocialCapitalServey.equals("Forestland")){
                                    Log.e("BBB ", costOutlays.size()+"");
                                    Log.e("AAA ",landKind.getForestLand().getOutlays().toString());
                                    realm.beginTransaction();
                                    landKind.getForestLand().setOutlays(costOutlaysToSave);
                                    realm.commitTransaction();
                                }else if(landKind.getName().equals("Cropland") && currentSocialCapitalServey.equals("Cropland")){
                                    Log.e("BBB ", costOutlays.size()+"");
                                    Log.e("AAA ",landKind.getCropLand().getOutlays().toString());
                                    realm.beginTransaction();
                                    landKind.getCropLand().setOutlays(costOutlaysToSave);
                                    realm.commitTransaction();
                                }else if(landKind.getName().equals("Pastureland") && currentSocialCapitalServey.equals("Pastureland")){
                                    Log.e("BBB ", costOutlays.size()+"");
                                    Log.e("AAA ",landKind.getPastureLand().getOutlays().toString());
                                    realm.beginTransaction();
                                    landKind.getPastureLand().setOutlays(costOutlaysToSave);
                                    realm.commitTransaction();
                                }else if(landKind.getName().equals("Mining Land") && currentSocialCapitalServey.equals("Mining Land")){
                                    Log.e("BBB ", costOutlays.size()+"");
                                    Log.e("AAA ",landKind.getMiningLand().getOutlays().toString());
                                    realm.beginTransaction();
                                    landKind.getMiningLand().setOutlays(costOutlaysToSave);
                                    realm.commitTransaction();
                                }
                            }

//                        Survey results = realm.where(Survey.class).findFirst();
//                        for(LandKind landKind:results.getLandKinds()){
//                            if(landKind.getName().equals("Forestland") && currentSocialCapitalServey.equals("Forestland")){
//                                for (RevenueProduct revenueProduct1: landKind.getForestLand().getRevenueProducts()){
//                                    Log.e("LAND ", revenueProduct1.getName());
//                                }
//                            }else if(landKind.getName().equals("Cropland") && currentSocialCapitalServey.equals("Cropland")){
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
                        }else {
                            Toast.makeText(context,getResources().getString(R.string.string_fill_name),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();

//                intent=new Intent(getApplicationContext(),NaturalCapitalSurveyActivityD.class);
//                startActivity(intent);
                break;

        }
    }

    public RealmList<OutlayYears> addOutlayYears(String name, long outlayYearsId){
        RealmList<OutlayYears> outlayYearses = new RealmList<>();
        int yearsCount = 0;
        // int year = Calendar.getInstance().get(Calendar.YEAR);
        int year = sharedPref.getInt("surveyyear",2016);

        if(currentSocialCapitalServey.equals("Forestland")){
            yearsCount = 15;
        }else if(currentSocialCapitalServey.equals("Cropland")){
            yearsCount = 5;
        }else if(currentSocialCapitalServey.equals("Pastureland")){
            yearsCount = 8;
        }else if(currentSocialCapitalServey.equals("Mining Land")){
            yearsCount = 5;
        }

        for(int i=0;i<=yearsCount;i++){
            OutlayYears outlayYearsCheck;
            outlayYearsCheck = realm.where(OutlayYears.class)
                    .equalTo("outlayId",outlayYearsId)
                    .equalTo("year",year)
                    .findFirst();

            if(outlayYearsCheck == null){
                realm.beginTransaction();
                outlayYearsCheck = realm.createObject(OutlayYears.class);
                outlayYearsCheck.setId(getNextKeyOutlayYears());
                outlayYearsCheck.setLandKind(currentSocialCapitalServey);
                outlayYearsCheck.setSurveyId(serveyId);
                outlayYearsCheck.setOutlayId(outlayYearsId);
                outlayYearsCheck.setYear(year);
                realm.commitTransaction();
            }
            outlayYearses.add(outlayYearsCheck);
            year++;
        }
        return outlayYearses;
    }

    public void nextLandKind(){
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
                j = i + 1;
            }
            i++;
        }

        if(j < i){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("currentSocialCapitalServey", landKindRealmResults.get(j).getName());
            editor.apply();

            Intent intent = new Intent(getApplicationContext(),StartLandTypeActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getApplicationContext(),NewCertificateActivity.class);
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

    public void toggleMenuDrawer(){
        if(menuDrawerLayout.isDrawerOpen(GravityCompat.START)){
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            menuDrawerLayout.openDrawer(GravityCompat.START);
        }
    }
}
