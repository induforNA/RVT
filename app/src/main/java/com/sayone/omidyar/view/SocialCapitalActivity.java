package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;

import io.realm.Realm;

/**
 * Created by sayone on 21/9/16.
 */

public class SocialCapitalActivity extends BaseActivity implements View.OnClickListener{

    public static final String TAG = SocialCapitalStartActivity.class.getName();
    private Realm realm;
    private SharedPreferences preferences;
    Context context;

    String currentSocialCapitalServey;
    String serveyId;

    private Button backButton;
    private Button nextButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_capital);

        Log.e("Current Activity ", "4454");

        context = this;
        realm = Realm.getDefaultInstance();
        preferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = preferences.getString("surveyId","");
        currentSocialCapitalServey = preferences.getString("currentSocialCapitalServey","");

        Log.e("CURRENT SOCIAL CAPITAL ",currentSocialCapitalServey+" ok");

        backButton = (Button) findViewById(R.id.back_button);
        nextButton = (Button) findViewById(R.id.next_button);

        backButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.next_button:
//                Intent intent = new Intent(SocialCapitalActivity.this, SocialCapitalActivity.class);
//                startActivity(intent);
                break;
        }
    }
}
