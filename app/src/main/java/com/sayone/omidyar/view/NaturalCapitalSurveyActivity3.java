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

public class NaturalCapitalSurveyActivity3 extends BaseActivity {

    Spinner spinnerInfrastructure1,spinnerInfrastructure2,spinnerInfrastructure3,spinnerInfrastructure4,spinnerInfrastructure5,spinnerInfrastructure6,spinnerInfrastructure7,spinnerInfrastructure8;
    String infrastructure1,infrastructure2,infrastructure3,infrastructure4,infrastructure5,infrastructure6,infrastructure7,infrastructure8;
    Button buttonNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_capital_survey3);

        spinnerInfrastructure1=(Spinner)findViewById(R.id.spinner_infrastructure1);
        spinnerInfrastructure2=(Spinner)findViewById(R.id.spinner_infrastructure2);
        spinnerInfrastructure3=(Spinner)findViewById(R.id.spinner_infrastructure3);
        spinnerInfrastructure4=(Spinner)findViewById(R.id.spinner_infrastructure4);
        spinnerInfrastructure5=(Spinner)findViewById(R.id.spinner_infrastructure5);
        spinnerInfrastructure6=(Spinner)findViewById(R.id.spinner_infrastructure6);
        spinnerInfrastructure7=(Spinner)findViewById(R.id.spinner_infrastructure7);
        spinnerInfrastructure8=(Spinner)findViewById(R.id.spinner_infrastructure8);
        buttonNext=(Button)findViewById(R.id.button_next);

        ArrayAdapter<CharSequence> infrastructureAdapter1 = ArrayAdapter.createFromResource(this,
                R.array.number_of_harvests, android.R.layout.simple_spinner_item);
        infrastructureAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> infrastructureAdapter2 = ArrayAdapter.createFromResource(this,
                R.array.number_of_harvests, android.R.layout.simple_spinner_item);
        infrastructureAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> infrastructureAdapter3 = ArrayAdapter.createFromResource(this,
                R.array.number_of_harvests, android.R.layout.simple_spinner_item);
        infrastructureAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> infrastructureAdapter4 = ArrayAdapter.createFromResource(this,
                R.array.number_of_harvests, android.R.layout.simple_spinner_item);
        infrastructureAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> infrastructureAdapter5 = ArrayAdapter.createFromResource(this,
                R.array.number_of_harvests, android.R.layout.simple_spinner_item);
        infrastructureAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> infrastructureAdapter6 = ArrayAdapter.createFromResource(this,
                R.array.number_of_harvests, android.R.layout.simple_spinner_item);
        infrastructureAdapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> infrastructureAdapter7 = ArrayAdapter.createFromResource(this,
                R.array.number_of_harvests, android.R.layout.simple_spinner_item);
        infrastructureAdapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> infrastructureAdapter8 = ArrayAdapter.createFromResource(this,
                R.array.number_of_harvests, android.R.layout.simple_spinner_item);
        infrastructureAdapter8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerInfrastructure1.setAdapter(infrastructureAdapter1);
        spinnerInfrastructure2.setAdapter(infrastructureAdapter2);
        spinnerInfrastructure3.setAdapter(infrastructureAdapter3);
        spinnerInfrastructure4.setAdapter(infrastructureAdapter4);
        spinnerInfrastructure5.setAdapter(infrastructureAdapter5);
        spinnerInfrastructure6.setAdapter(infrastructureAdapter6);
        spinnerInfrastructure7.setAdapter(infrastructureAdapter7);
        spinnerInfrastructure8.setAdapter(infrastructureAdapter8);

        spinnerInfrastructure1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                infrastructure1= parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        spinnerInfrastructure2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                infrastructure2= parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        spinnerInfrastructure3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                infrastructure3= parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        spinnerInfrastructure4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                infrastructure4= parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        spinnerInfrastructure5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                infrastructure5= parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        spinnerInfrastructure6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                infrastructure6= parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        spinnerInfrastructure7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                infrastructure7= parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        spinnerInfrastructure8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                infrastructure8= parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),CroplandSurveyA.class);
                startActivity(intent);
            }
        });

    }
}
