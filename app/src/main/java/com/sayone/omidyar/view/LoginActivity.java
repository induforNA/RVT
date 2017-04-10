package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.Survey;

import java.util.Locale;

import io.realm.Realm;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    Button signUp, adminLogin, login;
    EditText surveyorName, communityName, surveyName;
    String surveyor, community, survey;
    boolean flag = false;
    SharedPreferences.Editor editor;

    private SharedPreferences preferences;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        preferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        login = (Button) findViewById(R.id.button_login);
        signUp = (Button) findViewById(R.id.button_sign_up);
        adminLogin = (Button) findViewById(R.id.button_admin_login);
        communityName = (EditText) findViewById(R.id.edittext_community);
        surveyorName = (EditText) findViewById(R.id.edittext_surveyor);
        surveyName = (EditText) findViewById(R.id.edittext_survey);

        login.setOnClickListener(this);
        signUp.setOnClickListener(this);
        adminLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {

            case R.id.button_login:
                surveyor = surveyorName.getText().toString();
                community = communityName.getText().toString();
                survey = surveyName.getText().toString();
                if (surveyor.equals("")) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_surveyor_filed));
                } else if (community.equals("")) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_community_field));
                } else if (survey.equals("")) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_survey_field));
                } else {
                    Realm realm = Realm.getDefaultInstance();
                    Survey surveyCheck = realm.where(Survey.class)
                            .equalTo("surveyId", survey)
                            .findFirst();
                    if (surveyCheck != null) {
                        if (surveyor.equals(surveyCheck.getSurveyor())) {
                            if (community.equals(surveyCheck.getCommunity())) {
                                toastfunction(getApplicationContext(), getResources().getString(R.string.login_successful));
                                flag = true;
                                editor = preferences.edit();
                                editor.putString("surveyId", survey);
                                editor.apply();
                                Locale myLocale = new Locale(surveyCheck.getLanguage().substring(0,2).toLowerCase());
                                Resources res = getResources();
                                DisplayMetrics dm = res.getDisplayMetrics();
                                Configuration conf = res.getConfiguration();
                                conf.locale = myLocale;
                                res.updateConfiguration(conf, dm);
                                intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            } else {
                                toastfunction(getApplicationContext(), getResources().getString(R.string.login_failed));
                            }
                        }

                        if (!flag) {
                            toastfunction(getApplicationContext(), getResources().getString(R.string.login_failed));
                        }
                    } else {
                        toastfunction(getApplicationContext(), getResources().getString(R.string.survey_absent));
                    }
                }
                break;

            case R.id.button_sign_up:
                intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
                break;

            case R.id.button_admin_login:
                intent = new Intent(getApplicationContext(), AdminLoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void toastfunction(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
