package com.sayone.omidyar.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;

public class AdminRatesActivity extends BaseActivity {

    public Spinner countrySpinner,landUseSpinner;
    String country,landUseType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_rates);

        countrySpinner = (Spinner) findViewById(R.id.spinner_country);
        landUseSpinner = (Spinner) findViewById(R.id.spinner_land_use);

        ArrayAdapter<CharSequence> country_adapter = ArrayAdapter.createFromResource(this,
                R.array.country_array, android.R.layout.simple_spinner_item);
        country_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> landUse_adapter = ArrayAdapter.createFromResource(this,
                R.array.land_use_array, android.R.layout.simple_spinner_item);
        landUse_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        countrySpinner.setAdapter(country_adapter);
        landUseSpinner.setAdapter(landUse_adapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                country = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        landUseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                landUseType = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }
}
