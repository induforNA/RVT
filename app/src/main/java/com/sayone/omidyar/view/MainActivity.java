package com.sayone.omidyar.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
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
    private Realm realm;
    private String androidId;
    private String serveyId;
    Survey survey;

    private DrawerLayout menuDrawerLayout;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView textViewAbout;
    private TextView startSurvey;
    private ImageView buttonAddParticipant;
    private RecyclerView recyclerView;
    private TextView serveyIdTextView;
    private Button nextButton;
    private TextView surveyIdDrawer;
    private LinearLayout noParticipantLayout;
    private LinearLayout participantLayout;
    private TextView logout;

    RealmList<Participant> participants;
    private ParticipantsAdapter participantsAdapter;
    private String language;

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

        serveyId = sharedPref.getString("surveyId", "");


        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        textViewAbout = (TextView) findViewById(R.id.text_view_about);
        surveyIdDrawer = (TextView) findViewById(R.id.text_view_id);
        buttonAddParticipant = (ImageView) findViewById(R.id.button_add_participant);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        serveyIdTextView = (TextView) findViewById(R.id.servey_id);
        nextButton = (Button) findViewById(R.id.next_button);
        noParticipantLayout = (LinearLayout) findViewById(R.id.no_participant_layout);
        participantLayout = (LinearLayout) findViewById(R.id.participant_layout);
        logout = (TextView) findViewById(R.id.logout);
        startSurvey = (TextView) findViewById(R.id.text_start_survey);

        serveyIdTextView.setText(serveyId);

        participantsAdapter = new ParticipantsAdapter(participants, MainActivity.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(participantsAdapter);

        //serveyIdPrefix = encodeDeviceId(androidId);

        imageViewMenuIcon.setOnClickListener(this);
        surveyIdDrawer.setText(serveyId);
        drawerCloseBtn.setOnClickListener(this);
        textViewAbout.setOnClickListener(this);
        buttonAddParticipant.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        logout.setOnClickListener(this);
        startSurvey.setOnClickListener(this);


        //realm.beginTransaction();
        survey = realm.where(Survey.class).equalTo("surveyId", serveyId).findFirst();


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
//        survey.setSurveyId(serveyId);
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
                            participant.setSurveyId(serveyId);
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


                            Survey surveyParticipantUpdate = realm.where(Survey.class).equalTo("surveyId", serveyId).findFirst();
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
                Intent intentToLandTypeSelectionActivity = new Intent(MainActivity.this, LandTypeSelectionActivity.class);
                startActivity(intentToLandTypeSelectionActivity);
                break;
            case R.id.logout:
                Intent intents = new Intent(MainActivity.this, RegistrationActivity.class);
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intents);
                break;
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