package com.sayone.omidyar.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.adapter.ParticipantsAdapter;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.Participant;
import com.sayone.omidyar.model.Survey;

import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = MainActivity.class.getName();
    Context context;
    SharedPreferences sharedPref;
    Survey survey;
    RealmList<Participant> participants;
    private Realm realm;
    private String androidId;
    private String surveyId;
    private DrawerLayout menuDrawerLayout;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private ImageView buttonAddParticipant;
    private RecyclerView recyclerView;
    private TextView surveyIdTextView;
    private Button nextButton;
    private TextView surveyIdDrawer;
    private LinearLayout noParticipantLayout;
    private LinearLayout participantLayout;
    private ParticipantsAdapter participantsAdapter;
    private String language;

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
        setContentView(R.layout.activity_main);

        participants = new RealmList<>();
        context = this;
        language = Locale.getDefault().getDisplayLanguage();
        realm = Realm.getDefaultInstance();
        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        surveyId = sharedPref.getString("surveyId", "");

        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        surveyIdDrawer = (TextView) findViewById(R.id.text_view_id);
        buttonAddParticipant = (ImageView) findViewById(R.id.button_add_participant);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        surveyIdTextView = (TextView) findViewById(R.id.servey_id);
        nextButton = (Button) findViewById(R.id.next_button);
        noParticipantLayout = (LinearLayout) findViewById(R.id.no_participant_layout);
        participantLayout = (LinearLayout) findViewById(R.id.participant_layout);
        surveyIdTextView.setText(surveyId);

        participantsAdapter = new ParticipantsAdapter(participants, MainActivity.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(participantsAdapter);

        //serveyIdPrefix = encodeDeviceId(androidId);

        imageViewMenuIcon.setOnClickListener(this);
        surveyIdDrawer.setText(surveyId);
        drawerCloseBtn.setOnClickListener(this);
        buttonAddParticipant.setOnClickListener(this);
        nextButton.setOnClickListener(this);

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

        //realm.beginTransaction();
        survey = realm.where(Survey.class).equalTo("surveyId", surveyId).findFirst();

        Calendar cal = Calendar.getInstance();
        cal.setTime(survey.getDate());
        int surveyyear = cal.get(Calendar.YEAR);
        Log.e("SURVEY DATE ", surveyyear + "");

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("surveyyear", surveyyear);
        editor.apply();

//        sharedPref.
//        inflationRateStr = preferences.getString("inflationRate","5");


//        Survey survey = realm.createObject(Survey.class);
//        survey.setId(getNextKeySurvey());
//        survey.setSurveyId(surveyId);
//        survey.setSurveyor("Riyas PK");
//        survey.setRespondentGroup("Respondent group 1");
//        survey.setState("Kerala");
//        survey.setDistrict("Ernamkulam");
//        survey.setCommunity("Community");
//        survey.setCountry("India");
//        survey.setLanguage("English");
//        survey.setCurrency("INR");
        //realm.commitTransaction();

        //Log.e(TAG ,encodeDeviceId(androidId)+" "+androidId);

        if (survey.getParticipants().size() == 0) {
            participantLayout.setVisibility(View.GONE);
            noParticipantLayout.setVisibility(View.VISIBLE);
        } else {
            //participants = survey.getParticipants();
            for (Participant participantIter : survey.getParticipants()) {
                participants.add(participantIter);
            }
            Log.e("TEST ", participants.toString());
            participantsAdapter.notifyDataSetChanged();
            noParticipantLayout.setVisibility(View.GONE);
            participantLayout.setVisibility(View.VISIBLE);
        }

        RealmResults<Survey> results = realm.where(Survey.class).findAll();
        for (Survey survey1 : results) {
            Log.e(TAG, survey1.toString());
            Log.e(TAG, String.valueOf(survey1.getParticipants().size()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onResume() {
        setNav();
        super.onResume();
    }

    public int getNextKeySurvey() {
        return realm.where(Survey.class).max("id").intValue() + 1;
    }

    public int getNextKeyParticipant() {
        return realm.where(Participant.class).max("id").intValue() + 1;
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
            case R.id.text_view_about:
                Intent i = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(i);
                break;
            case R.id.text_start_survey:
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
            case R.id.button_add_participant:
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_participant_form);
                dialog.setTitle(getResources().getString(R.string.string_add_participant));
                dialog.setCancelable(false);

                Button popupCancel = (Button) dialog.findViewById(R.id.popup_cancel);
                Button saveParticipant = (Button) dialog.findViewById(R.id.save_participant);
                final EditText editTextParticipant = (EditText) dialog.findViewById(R.id.edit_text_participant);
                final EditText editTextOccupation = (EditText) dialog.findViewById(R.id.edit_text_occupation);
                final EditText editTextGender = (EditText) dialog.findViewById(R.id.edit_text_gender);
                final EditText editTextAge = (EditText) dialog.findViewById(R.id.edit_text_age);
                final EditText editTextEducation = (EditText) dialog.findViewById(R.id.edit_text_education);


                popupCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                saveParticipant.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = editTextParticipant.getText().toString();
                        String occupation = editTextOccupation.getText().toString();
                        String gender = editTextGender.getText().toString();
                        String age = editTextAge.getText().toString();
                        String education = editTextEducation.getText().toString();
                        if (!name.equals("")) {
                            realm.beginTransaction();
                            Participant participant = realm.createObject(Participant.class);
                            participant.setId(getNextKeyParticipant());
                            participant.setSurveyId(surveyId);
                            participant.setName(name);
                            participant.setOccupation(occupation);
                            participant.setGender(gender);
                            if (!age.equals("")) {
                                participant.setAge(Integer.parseInt(age));
                            }
                            if (!education.equals("")) {
                                participant.setYearsOfEdu(Integer.parseInt(education));
                            }
                            realm.commitTransaction();

                            participants.add(participant);


                            Survey surveyParticipantUpdate = realm.where(Survey.class).equalTo("surveyId", surveyId).findFirst();
                            realm.beginTransaction();

                            surveyParticipantUpdate.setParticipants(participants);
                            realm.commitTransaction();
                            RealmResults<Survey> results = realm.where(Survey.class).findAll();
                            for (Survey survey1 : results) {
                                Log.e(TAG + "abcd", survey1.toString());

                                //Log.e(TAG, String.valueOf(survey1.getParticipants().size()));
                                for (Participant participant1 : survey1.getParticipants()) {
                                    Log.e(TAG + "abcd", participant1.toString());
                                }
                            }
                            noParticipantLayout.setVisibility(View.GONE);
                            participantLayout.setVisibility(View.VISIBLE);
                            participantsAdapter.notifyDataSetChanged();
                            dialog.cancel();
                        } else {
                            Toast.makeText(context, getResources().getString(R.string.string_fill_name), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
                break;
            case R.id.next_button:
                final Dialog dialog1 = new Dialog(MainActivity.this, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.blackDisable)));
                dialog1.setContentView(R.layout.layout_show_save_warning);
                Button dialogNext = (Button) dialog1.findViewById(R.id.button_next);

                dialogNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentToLandTypeSelectionActivity = new Intent(MainActivity.this, LandTypeSelectionActivity.class);
                        startActivity(intentToLandTypeSelectionActivity);
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
                break;
            case R.id.logout:
                Intent intents = new Intent(MainActivity.this, RegistrationActivity.class);
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intents);
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
        Intent intent = new Intent(MainActivity.this, StartLandTypeActivity.class);
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

    public void toggleMenuDrawer() {
        if (menuDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            menuDrawerLayout.openDrawer(GravityCompat.START);
        }
    }
}