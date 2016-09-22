package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.Participant;
import com.sayone.omidyar.model.Survey;

import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by sayone on 19/9/16.
 */
public class LandTypeSelectionActivity extends BaseActivity implements View.OnClickListener{

    public static final String TAG = LandTypeSelectionActivity.class.getName();
    Context context;
    SharedPreferences sharedPref;
    private Realm realm;
    private String serveyId;
    Survey survey;
    RealmList<LandKind> landKinds;

    private DrawerLayout menuDrawerLayout;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private Button nextButton;
    private Button backButton;
    private CheckBox forestland;
    private CheckBox cropland;
    private CheckBox pastureland;
    private CheckBox miningland;

    Set<String> landTypeNames;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_type_selection);

        context = this;
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = sharedPref.getString("surveyId","");
        landKinds = new RealmList<>();
        landTypeNames = new HashSet<String>();

        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        nextButton = (Button) findViewById(R.id.next_button);
        backButton = (Button) findViewById(R.id.back_button);

        forestland = (CheckBox) findViewById(R.id.forestland);
        cropland = (CheckBox) findViewById(R.id.cropland);
        pastureland = (CheckBox) findViewById(R.id.pastureland);
        miningland = (CheckBox) findViewById(R.id.miningland);

        survey = realm.where(Survey.class)
                .equalTo("surveyId",serveyId)
                .findFirst();
        for(LandKind landKindItrate :survey.getLandKinds()) {
            Log.e(TAG+" 11",landKindItrate.toString());
            if(landKindItrate.getName().equals("Forestland") && landKindItrate.getStatus().equals("active")){
                forestland.setChecked(true);
                landTypeNames.add("Forestland");
            }else if(landKindItrate.getName().equals("Cropland") && landKindItrate.getStatus().equals("active")){
                cropland.setChecked(true);
                landTypeNames.add("Cropland");
            }else if(landKindItrate.getName().equals("Pastureland") && landKindItrate.getStatus().equals("active")){
                pastureland.setChecked(true);
                landTypeNames.add("Pastureland");
            }else if(landKindItrate.getName().equals("Mining Land") && landKindItrate.getStatus().equals("active")){
                miningland.setChecked(true);
                landTypeNames.add("Mining Land");
            }
        }

        imageViewMenuIcon.setOnClickListener(this);
        drawerCloseBtn.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    public void slectedLandKind(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.forestland:
                addLandTyeInSet(checked,"Forestland");
//                if (checked) {
//                    landTypeNames.add("Forestland");
//                }else{
//                    landTypeNames.remove("Forestland");
//                }
                break;
            case R.id.cropland:
                addLandTyeInSet(checked,"Cropland");
//                if (checked) {
//                    landTypeNames.add("Cropland");
//                }else{
//                    landTypeNames.remove("Cropland");
//                }
                break;
            case R.id.pastureland:
                addLandTyeInSet(checked,"Pastureland");
//                if (checked) {
//                    landTypeNames.add("Pastureland");
//                }else{
//                    landTypeNames.remove("Pastureland");
//                }
                break;
            case R.id.miningland:
                addLandTyeInSet(checked,"Mining Land");
//                if (checked) {
//                    landTypeNames.add("Mining Land");
//                }else{
//                    landTypeNames.add("Pastureland");
//                }
                break;
        }
    }

    public void addLandTyeInSet(boolean checked, String name){
        Log.e("CHECKED STATUS ", checked+" "+name);
        if (checked) {
            LandKind landKind = realm.where(LandKind.class).equalTo("name",name).findFirst();
            realm.beginTransaction();
            landKind.setStatus("active");
            realm.commitTransaction();
            landTypeNames.add(name);
        }else{
            LandKind landKind = realm.where(LandKind.class).equalTo("name",name).findFirst();
            realm.beginTransaction();
            landKind.setStatus("deleted");
            realm.commitTransaction();
            landTypeNames.remove(name);
        }
        Log.e("CHECKED STATUS ", landTypeNames.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view_menu_icon:
                toggleMenuDrawer();
                break;
            case R.id.drawer_close_btn:
                toggleMenuDrawer();
                break;
            case R.id.next_button:
                Log.e("CLICK ","CHECK");
                saveLandKind();
                break;
            case R.id.back_button:
                finish();
                break;
        }
    }

    public void toggleMenuDrawer(){
        if(menuDrawerLayout.isDrawerOpen(GravityCompat.START)){
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            menuDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public void saveLandKind(){
        Log.e("CLICK ","CHECK");
//        for (String landTypeName : landTypeNames) {
//            LandKind landKind = realm.where(LandKind.class).equalTo("name",landTypeName).findFirst();
//            realm.beginTransaction();
//            landKind.setStatus("active");
//            realm.commitTransaction();
//
//            landKinds.add(landKind);
//        }

        // Log.e(TAG,survey1.toString());

        RealmResults<LandKind> results = realm.where(LandKind.class).findAll();
        for (LandKind survey1 : results) {
            Log.e(TAG,survey1.toString());
            //Log.e(TAG, String.valueOf(survey1.getParticipants().size()));
        }
//        realm.beginTransaction();
//        survey.setLandKinds(landKinds);
//        realm.commitTransaction();

        Intent intent = new Intent(LandTypeSelectionActivity.this, SocialCapitalStartActivity.class);
        startActivity(intent);
    }

    public int getNextKeyLandKind() {
        return realm.where(LandKind.class).max("id").intValue() + 1;
    }
}
