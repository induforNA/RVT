package com.sayone.omidyar.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.sayone.omidyar.R;

public class AdminDashBoard extends AppCompatActivity {
    TextView rates,projectionTimeFrame,units,summaryOfSurveys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board);

        rates=(TextView)findViewById(R.id.textview_rates);
        projectionTimeFrame=(TextView)findViewById(R.id.textview_projection);
        units=(TextView)findViewById(R.id.textview_units);
        summaryOfSurveys=(TextView)findViewById(R.id.textview_summary);


    }
}
