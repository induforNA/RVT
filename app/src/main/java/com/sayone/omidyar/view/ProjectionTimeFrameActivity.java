package com.sayone.omidyar.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;

public class ProjectionTimeFrameActivity extends BaseActivity {

    public Spinner yearSpinner1, yearSpinner2, yearSpinner3;
    String year1, year2, year3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projection_time_frame);

        yearSpinner1 = (Spinner) findViewById(R.id.spinner_year1);
        yearSpinner2 = (Spinner) findViewById(R.id.spinner_year2);
        yearSpinner3 = (Spinner) findViewById(R.id.spinner_year3);

        ArrayAdapter<CharSequence> year1_adapter = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_dropdown_item);
        year1_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> year2_adapter = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_dropdown_item);
        year2_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> year3_adapter = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_dropdown_item);
        year3_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        yearSpinner1.setAdapter(year1_adapter);
        yearSpinner2.setAdapter(year2_adapter);
        yearSpinner3.setAdapter(year3_adapter);

        yearSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                year1 = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        yearSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                year2 = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        yearSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                year3 = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }
}
