package com.sayone.omidyar.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.Participant;
import com.sayone.omidyar.model.RevenueProduct;
import com.sayone.omidyar.model.Survey;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class CroplandSurveyA extends BaseActivity implements View.OnClickListener {

    ImageView addCropType;
    Realm realm;
    Context context;
    TextView cropName;
    String name;

    SharedPreferences sharedPref;
    private String surveyId;
    RealmList<RevenueProduct> revenueProducts;
    private String language;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView textViewAbout;
    private TextView logout;
    private TextView startSurvey;
    private DrawerLayout menuDrawerLayout;
    private TextView surveyIdDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropland_survey);
        context=this;
        language = Locale.getDefault().getDisplayLanguage();
        realm = Realm.getDefaultInstance();
        revenueProducts=new RealmList<>();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        surveyId = sharedPref.getString("surveyId","");

//        int cropArray;
//        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.crop_list_item, cropArray);
//
//        ListView listView = (ListView) findViewById(R.id.crop_list);
//        listView.setAdapter(adapter);

        addCropType=(ImageView)findViewById(R.id.button_add_crop_type);
        cropName=(TextView)findViewById(R.id.crop);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        textViewAbout = (TextView) findViewById(R.id.text_view_about);
        logout = (TextView) findViewById(R.id.logout);
        startSurvey=(TextView)findViewById(R.id.text_start_survey);
        surveyIdDrawer=(TextView)findViewById(R.id.text_view_id);

        addCropType.setOnClickListener(this);
        imageViewMenuIcon.setOnClickListener(this);
        drawerCloseBtn.setOnClickListener(this);
        textViewAbout.setOnClickListener(this);
        logout.setOnClickListener(this);
        startSurvey.setOnClickListener(this);
        surveyIdDrawer.setText(surveyId);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.button_add_crop_type:

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_cropland);

                    dialog.setTitle(getResources().getString(R.string.string_add_crop));

                dialog.setCancelable(false);

                Button popupCancel = (Button) dialog.findViewById(R.id.popup_cancel);
                Button saveParticipant = (Button) dialog.findViewById(R.id.save_cropland);
                final EditText cropland_type = (EditText) dialog.findViewById(R.id.cropland_type);


                popupCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                saveParticipant.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = cropland_type.getText().toString();
                        if(!name.equals("")) {

                           /* realm.beginTransaction();
                            RevenueProduct revenueProduct=realm.createObject(RevenueProduct.class);
                            revenueProduct.setId(getNextKey());
                            revenueProduct.setName(cropland_type.getText().toString());
                            revenueProduct.setSurveyId(surveyId);
                            revenueProduct.setType("crop");
                            realm.commitTransaction();
                            costElements.add(revenueProduct);

                            //Survey survey = realm.createObject(Survey.class);
                            Survey survey = realm.where(Survey.class).equalTo("surveyId", surveyId).findFirst();
                            RealmList<LandKind> landKind=survey.getLandKinds();
                            for(LandKind l:landKind){
                                if(l.getName().equals("Cropland")){
                                    realm.beginTransaction();
                                    l.getCropLand().setRevenueProducts(costElements);*/
                                    cropName.setText(name);
                                   /* realm.commitTransaction();
                                }
                            }

                           // noParticipantLayout.setVisibility(View.GONE);
                           //   participantLayout.setVisibility(View.VISIBLE);
                           // participantsAdapter.notifyDataSetChanged();*/
                            dialog.cancel();
                        }else {
                                Toast.makeText(context,getResources().getString(R.string.string_fill_name), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                dialog.show();
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
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.logout:
                Intent intents = new Intent(getApplicationContext(),RegistrationActivity.class);
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intents);
                break;


        }
    }
    public int getNextKey() {
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
