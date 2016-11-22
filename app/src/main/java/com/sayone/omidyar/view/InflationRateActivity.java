package com.sayone.omidyar.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sayone.omidyar.R;

public class InflationRateActivity extends AppCompatActivity {

    EditText inflationRate;
    String inflationrateString;
    Button saveButton;
    Context context;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inflation_rate);
        inflationRate = (EditText) findViewById(R.id.inflation_rate);
        saveButton = (Button) findViewById(R.id.button_save);

        context = this;
        preferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                inflationrateString = inflationRate.getText().toString();
                editor.putString("inflationRate", inflationrateString);
                editor.apply();
                Toast toast = Toast.makeText(context,getResources().getText(R.string.text_data_saved), Toast.LENGTH_SHORT);
                toast.show();
                String rate = preferences.getString("inflationRate", "");
                Log.e("Rate :", rate);
            }
        });


    }
}
