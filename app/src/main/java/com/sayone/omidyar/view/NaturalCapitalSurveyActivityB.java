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
import com.sayone.omidyar.adapter.RevenueAdapter;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.RevenueProduct;
import com.sayone.omidyar.model.Survey;

import io.realm.Realm;
import io.realm.RealmList;

public class NaturalCapitalSurveyActivityB extends BaseActivity implements View.OnClickListener {

    Context context;
    Realm realm;
    SharedPreferences sharedPref;

    String serveyId;
    String currentSocialCapitalServey;
    Button buttonBack,buttonNext;
    ImageView buttonAddWood;
    RealmList<RevenueProduct> revenueProducts;
    RealmList<RevenueProduct> revenueProductsToSave;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView textViewAbout;
    private TextView logout;
    private TextView startSurvey;
    private TextView surveyIdDrawer;
    private DrawerLayout menuDrawerLayout;

    RecyclerView timberList;
    RevenueAdapter revenueAdapter;

    TextView landTypeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_capital_survey_b);

        context = this;
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = sharedPref.getString("surveyId","");
        currentSocialCapitalServey = sharedPref.getString("currentSocialCapitalServey","");

        revenueProducts = new RealmList<>();
        revenueProductsToSave = new RealmList<>();
        Survey survey = realm.where(Survey.class).equalTo("surveyId", serveyId).findFirst();
        for(LandKind landKind:survey.getLandKinds()){
            if(landKind.getName().equals("Forestland")){
                //costElements = landKind.getForestLand().getRevenueProducts();
                for(RevenueProduct revenueProduct:landKind.getForestLand().getRevenueProducts()){
                    revenueProductsToSave.add(revenueProduct);
                    if(revenueProduct.getType().equals("Non Timber")){
                        revenueProducts.add(revenueProduct);
                    }
                }
            }
        }


        buttonBack=(Button)findViewById(R.id.button_back);
        buttonNext=(Button)findViewById(R.id.button_next);
        buttonAddWood = (ImageView) findViewById(R.id.button_add_wood);
        landTypeName = (TextView) findViewById(R.id.land_type_name);
        landTypeName.setText(currentSocialCapitalServey);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        textViewAbout = (TextView) findViewById(R.id.text_view_about);
        logout = (TextView) findViewById(R.id.logout);
        startSurvey=(TextView)findViewById(R.id.text_start_survey);
        surveyIdDrawer=(TextView)findViewById(R.id.text_view_id);

        timberList = (RecyclerView) findViewById(R.id.timber_list);

        revenueAdapter = new RevenueAdapter(revenueProducts);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        timberList.setLayoutManager(mLayoutManager);
        timberList.setItemAnimator(new DefaultItemAnimator());
        timberList.setAdapter(revenueAdapter);


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
                intent=new Intent(getApplicationContext(),NaturalCapitalSurveyActivityC.class);
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
                dialog.setTitle(getResources().getString(R.string.string_add_participant));
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
                            RevenueProduct revenueProduct = realm.createObject(RevenueProduct.class);
                            revenueProduct.setId(getNextKeyRevenueProduct());
                            revenueProduct.setName(name);
                            revenueProduct.setType("Non Timber");
                            revenueProduct.setSurveyId(serveyId);
                            realm.commitTransaction();

                            revenueProductsToSave.add(revenueProduct);
                            revenueProducts.add(revenueProduct);
                            Survey surveyRevenueProduct = realm.where(Survey.class).equalTo("surveyId", serveyId).findFirst();


                            for(LandKind landKind:surveyRevenueProduct.getLandKinds()){
                                Log.e("BBB ",landKind.getName()+" "+landKind.getForestLand());
                                if(landKind.getName().equals("Forestland")){
                                    Log.e("BBB ",revenueProducts.size()+"");
                                    Log.e("AAA ",landKind.getForestLand().getRevenueProducts().toString());
                                    realm.beginTransaction();
                                    landKind.getForestLand().setRevenueProducts(revenueProductsToSave);
                                    realm.commitTransaction();
                                }
                            }

                            Survey results = realm.where(Survey.class).findFirst();
                            for(LandKind landKind:results.getLandKinds()){
                                if(landKind.getName().equals("Forestland")){
                                    for (RevenueProduct revenueProduct1: landKind.getForestLand().getRevenueProducts()){
                                        Log.e("LAND ", revenueProduct1.getName());
                                    }
                                }
                            }

                            revenueAdapter.notifyDataSetChanged();

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

    public int getNextKeyRevenueProduct() {
        return realm.where(RevenueProduct.class).max("id").intValue() + 1;
    }
    public void toggleMenuDrawer(){
        if(menuDrawerLayout.isDrawerOpen(GravityCompat.START)){
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            menuDrawerLayout.openDrawer(GravityCompat.START);
        }
    }
}
