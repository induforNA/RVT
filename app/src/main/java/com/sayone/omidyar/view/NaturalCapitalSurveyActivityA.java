package com.sayone.omidyar.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;

public class NaturalCapitalSurveyActivityA extends BaseActivity implements View.OnClickListener {

    Button buttonBack,buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_capital_survey_a);

        buttonBack=(Button)findViewById(R.id.button_back);
        buttonNext=(Button)findViewById(R.id.button_next);

        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.button_next:
                intent=new Intent(getApplicationContext(),NaturalCapitalSurveyActivity2.class);
                startActivity(intent);
                break;

            case R.id.button_back:
                intent=new Intent(getApplicationContext(),NaturalCapitalSurveyActivity.class);
                startActivity(intent);
                break;

        }
    }
}
