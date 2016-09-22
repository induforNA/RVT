package com.sayone.omidyar.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;

public class SurveySummaryActivity extends BaseActivity implements View.OnClickListener{

    TextView selectAll;
    CheckBox checkBoxSurvey1,checkBoxSurvey2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_summary);

        selectAll=(TextView)findViewById(R.id.select_all);
        checkBoxSurvey1=(CheckBox)findViewById(R.id.checkBox_survey1);
        checkBoxSurvey2=(CheckBox)findViewById(R.id.checkBox_survey2);

        selectAll.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {

            case R.id.select_all:

                checkBoxSurvey1.setChecked(true);
                checkBoxSurvey2.setChecked(true);

                break;

        }
    }

}
