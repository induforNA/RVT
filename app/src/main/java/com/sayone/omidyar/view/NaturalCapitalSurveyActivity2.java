package com.sayone.omidyar.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;

public class NaturalCapitalSurveyActivity2 extends BaseActivity implements View.OnClickListener {

    Spinner spinnerYear;
    String year;
    Button buttonBack,buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_capital_survey2);

        spinnerYear=(Spinner)findViewById(R.id.spinner_year);
        buttonBack=(Button)findViewById(R.id.button_back);
        buttonNext=(Button)findViewById(R.id.button_next);

        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);

        ArrayAdapter<CharSequence> year_adapter = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_item);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerYear.setAdapter(year_adapter);

        year= spinnerYear.getSelectedItem().toString();

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                year= parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){

            case R.id.button_next:
                intent=new Intent(getApplicationContext(),NaturalCapitalSurveyActivity3.class);
                startActivity(intent);
                break;

            case R.id.button_back:
                intent=new Intent(getApplicationContext(),NaturalCapitalSurveyActivity1.class);
                startActivity(intent);
                break;
        }

    }
}
