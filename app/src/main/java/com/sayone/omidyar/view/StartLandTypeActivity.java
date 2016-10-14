package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;

import java.util.Locale;

import io.realm.Realm;

/**
 * Created by sayone on 14/10/16.
 */

public class StartLandTypeActivity extends BaseActivity implements View.OnClickListener{

    Context context;
    Realm realm;
    SharedPreferences sharedPref;

    Button backButton, nextButton;
    String currentSocialCapitalServey, serveyId;
    TextView landType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_land_type);

        context = this;
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = sharedPref.getString("surveyId","");
        currentSocialCapitalServey = sharedPref.getString("currentSocialCapitalServey","");

        backButton = (Button) findViewById(R.id.back_button);
        nextButton = (Button) findViewById(R.id.next_button);
        landType = (TextView) findViewById(R.id.land_type);

        landType.setText(currentSocialCapitalServey);

        backButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_button:
                Intent intent = new Intent(StartLandTypeActivity.this, OmidyarMap.class);
                startActivity(intent);
                break;
            case R.id.back_button:
                finish();
                break;
        }
    }
}
