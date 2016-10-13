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
import com.sayone.omidyar.adapter.CostAdapter;
import com.sayone.omidyar.model.CostElement;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.RevenueProduct;
import com.sayone.omidyar.model.Survey;

import io.realm.Realm;
import io.realm.RealmList;

public class NaturalCapitalCostActivityA extends BaseActivity implements View.OnClickListener {

    Context context;
    Realm realm;
    SharedPreferences sharedPref;

    String serveyId;
    Button buttonBack,buttonNext;
    ImageView buttonAddWood;
    RealmList<CostElement> costElements;
    RealmList<CostElement> costProductsToSave;

    RecyclerView timberList;
    CostAdapter costAdapter;

    String landKindName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_cost_survey_a);

        context = this;
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = sharedPref.getString("surveyId","");

        costElements = new RealmList<>();
        costProductsToSave = new RealmList<>();
        Survey survey = realm.where(Survey.class).equalTo("surveyId", serveyId).findFirst();
        for(LandKind landKind:survey.getLandKinds()){
            if(landKind.getName().equals("Forestland")){
                landKindName = landKind.getName();
                //costElements = landKind.getForestLand().getRevenueProducts();
                for(CostElement costElement:landKind.getForestLand().getCostElements()){
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

        timberList = (RecyclerView) findViewById(R.id.timber_list);

        costAdapter = new CostAdapter(costElements);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        timberList.setLayoutManager(mLayoutManager);
        timberList.setItemAnimator(new DefaultItemAnimator());
        timberList.setAdapter(costAdapter);


        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        buttonAddWood.setOnClickListener(this);
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

            case R.id.button_add_wood:
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_forest_revenue_item);
                dialog.setTitle("Add a cost element");
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
                            if(landKind.getName().equals("Forestland")){
                                Log.e("BBB ", costElements.size()+"");
                                Log.e("AAA ",landKind.getForestLand().getCostElements().toString());
                                realm.beginTransaction();
                                landKind.getForestLand().setCostElements(costProductsToSave);
                                realm.commitTransaction();
                            }
                        }

                        Survey results = realm.where(Survey.class).findFirst();
                        for(LandKind landKind:results.getLandKinds()){
                            if(landKind.getName().equals("Forestland")){
                                for (CostElement costElement1: landKind.getForestLand().getCostElements()){
                                    Log.e("LAND ", costElement1.getName());
                                }
                            }
                        }

                        costAdapter.notifyDataSetChanged();

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

    public int getNextKeyCostElement() {
        return realm.where(CostElement.class).max("id").intValue() + 1;
    }
}
