package com.sayone.omidyar.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;

public class NaturalCapitalSurveyActivity extends BaseActivity {

    Spinner spinnerYear,spinnerUnit,spinnerCurrency,spinnerNumberTimes,spinnerTimePeriod;
    String year,unit,currency,numberTimes,timePeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_capital_survey);

        spinnerYear=(Spinner)findViewById(R.id.spinner_year);
        spinnerUnit=(Spinner)findViewById(R.id.spinner_unit);
        spinnerCurrency=(Spinner)findViewById(R.id.spinner_currency);
        spinnerNumberTimes=(Spinner)findViewById(R.id.spinner_number_times);
        spinnerTimePeriod=(Spinner)findViewById(R.id.spinner_time_period);

        ArrayAdapter<CharSequence> year_adapter = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_item);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> unit_adapter = ArrayAdapter.createFromResource(this,
                R.array.unit_array, android.R.layout.simple_spinner_item);
        unit_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> currency_adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);
        currency_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> numberTimes_adapter = ArrayAdapter.createFromResource(this,
                R.array.number_of_harvests, android.R.layout.simple_spinner_item);
        numberTimes_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<CharSequence> timePeriod_adapter = ArrayAdapter.createFromResource(this,
                R.array.time_period_array, android.R.layout.simple_spinner_item);
        timePeriod_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerYear.setAdapter(year_adapter);
        spinnerUnit.setAdapter(unit_adapter);
        spinnerCurrency.setAdapter(currency_adapter);
        spinnerNumberTimes.setAdapter(numberTimes_adapter);
        spinnerTimePeriod.setAdapter(timePeriod_adapter);

        year= spinnerYear.getSelectedItem().toString();
        unit= spinnerUnit.getSelectedItem().toString();
        currency= spinnerCurrency.getSelectedItem().toString();
        numberTimes= spinnerNumberTimes.getSelectedItem().toString();
        timePeriod= spinnerTimePeriod.getSelectedItem().toString();

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
        spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                unit= parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                currency= parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        spinnerNumberTimes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                numberTimes= parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        spinnerTimePeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                timePeriod= parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }
}
