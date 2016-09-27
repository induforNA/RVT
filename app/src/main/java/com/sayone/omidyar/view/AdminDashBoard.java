package com.sayone.omidyar.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;

public class AdminDashBoard extends BaseActivity implements View.OnClickListener  {
    TextView rates,projectionTimeFrame,units,summaryOfSurveys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board);

        rates=(TextView)findViewById(R.id.textview_rates);
        projectionTimeFrame=(TextView)findViewById(R.id.textview_projection);
        units=(TextView)findViewById(R.id.textview_units);
        summaryOfSurveys=(TextView)findViewById(R.id.textview_summary);

        rates.setOnClickListener(this);
        projectionTimeFrame.setOnClickListener(this);
        units.setOnClickListener(this);
        summaryOfSurveys.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {

            case R.id.textview_rates:
                intent=new Intent(getApplicationContext(),AdminRatesActivity.class);
                startActivity(intent);
                break;

            case R.id.textview_projection:
                intent=new Intent(getApplicationContext(),ProjectionTimeFrameActivity.class);
                startActivity(intent);
                break;

            case R.id.textview_units:
                intent=new Intent(getApplicationContext(),UnitsActivity.class);
                startActivity(intent);
                break;

            case R.id.textview_summary:
                intent=new Intent(getApplicationContext(),SurveySummaryActivity.class);
                startActivity(intent);
                break;
        }
    }

}
