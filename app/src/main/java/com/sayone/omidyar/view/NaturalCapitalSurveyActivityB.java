package com.sayone.omidyar.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    Button buttonBack,buttonNext;
    ImageView buttonAddWood;
    RealmList<RevenueProduct> revenueProducts;
    RealmList<RevenueProduct> revenueProductsToSave;

    RecyclerView timberList;
    RevenueAdapter revenueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_capital_survey_b);

        context = this;
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = sharedPref.getString("surveyId","");

        revenueProducts = new RealmList<>();
        revenueProductsToSave = new RealmList<>();
        Survey survey = realm.where(Survey.class).equalTo("surveyId", serveyId).findFirst();
        for(LandKind landKind:survey.getLandKinds()){
            if(landKind.getName().equals("Forestland")){
                //revenueProducts = landKind.getForestLand().getRevenueProducts();
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

        timberList = (RecyclerView) findViewById(R.id.timber_list);

        revenueAdapter = new RevenueAdapter(revenueProducts);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        timberList.setLayoutManager(mLayoutManager);
        timberList.setItemAnimator(new DefaultItemAnimator());
        timberList.setAdapter(revenueAdapter);


        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        buttonAddWood.setOnClickListener(this);
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

            case R.id.button_add_wood:
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_forest_revenue_item);
                dialog.setTitle("Add a participant");
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
                            Toast.makeText(context,"Fill name",Toast.LENGTH_SHORT).show();
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
}
