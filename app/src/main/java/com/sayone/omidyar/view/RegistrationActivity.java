package com.sayone.omidyar.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.Survey;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by sayone on 16/9/16.
 */
public class RegistrationActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = RegistrationActivity.class.getName();
    private Realm realm;

    public Spinner countrySpinner, currencySpinner, languageSpinner;
    public EditText respondentGroup, state, district, community, surveyor;
    public Button signUp, login;
    public String country, language, currency, selectedDate;
    private DatePicker datePicker;
    private Calendar calendar;
    private EditText dateView;
    int surveyId;
    private int year, month, day;
    private Date date;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        context = this;
        realm = Realm.getDefaultInstance();
        preferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        signUp = (Button) findViewById(R.id.button_sign_up);
        login = (Button) findViewById(R.id.button_login);

        respondentGroup = (EditText) findViewById(R.id.edittext_respondent_group);
        state = (EditText) findViewById(R.id.edittext_state);
        district = (EditText) findViewById(R.id.edittext_district);
        community = (EditText) findViewById(R.id.edittext_community);
        surveyor = (EditText) findViewById(R.id.edittext_surveyor);
        dateView = (EditText) findViewById(R.id.edittext_date);

        countrySpinner = (Spinner) findViewById(R.id.spinner_country);
        currencySpinner = (Spinner) findViewById(R.id.spinner_currency);
        languageSpinner = (Spinner) findViewById(R.id.spinner_language);


        dateView.setInputType(InputType.TYPE_NULL);
        login.setOnClickListener(this);
        signUp.setOnClickListener(this);
        dateView.setOnClickListener(this);


        ArrayAdapter<CharSequence> country_adapter = ArrayAdapter.createFromResource(this,
                R.array.country_array, android.R.layout.simple_spinner_item);
        country_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> language_adapter = ArrayAdapter.createFromResource(this,
                R.array.language_array, android.R.layout.simple_spinner_item);
        language_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> currency_adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);
        currency_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        countrySpinner.setAdapter(country_adapter);
        languageSpinner.setAdapter(language_adapter);
        currencySpinner.setAdapter(currency_adapter);
        country = countrySpinner.getSelectedItem().toString();
        language = languageSpinner.getSelectedItem().toString();
        currency = currencySpinner.getSelectedItem().toString();

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

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                language = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                currency = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void toastfunction(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public int getNextKeySurvey() {
        if(realm.where(Survey.class).max("id") == null){
            return 1;
        }
        return realm.where(Survey.class).max("id").intValue() + 1;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.button_login:
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                break;

            case R.id.edittext_date:
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(RegistrationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        selectedDate = String.format("%s/%s/%s", selectedday, selectedmonth + 1, selectedyear);
                        dateView.setText(selectedDate);
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            date = dateFormat.parse(selectedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();
                break;

            case R.id.button_sign_up:
                if (dateView.getText().toString().equals("")) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_date_filed));
                } else if (respondentGroup.getText().toString().equals("")) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_respondent_group));
                } else if (surveyor.getText().toString().equals("")) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_surveyor_filed));
                } else if (language.equals(getResources().getString(R.string.empty_language_field))) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_language_field));
                } else if (currency.equals(getResources().getString(R.string.empty_currency_field))) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_currency_field));
                } else if (community.getText().toString().equals("")) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_community_field));
                } else if (district.getText().toString().equals("")) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_district_filed));
                } else if (state.getText().toString().equals("")) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_state_filed));
                } else if (country.equals(getResources().getString(R.string.empty_country_filed))) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_country_filed));
                } else {
                    insertData();
                }
                break;
        }
    }

    public void insertData() {
        surveyId = getNextKeySurvey();
        String formattediId = String.format("%04d", surveyId);
        RealmList<LandKind> landKinds = new RealmList<>();

        landKinds.add(insertAllLandKinds(formattediId,"Forestland"));
        landKinds.add(insertAllLandKinds(formattediId,"Cropland"));
        landKinds.add(insertAllLandKinds(formattediId,"Pastureland"));
        landKinds.add(insertAllLandKinds(formattediId,"Mining Land"));

        realm.beginTransaction();
        Survey survey = realm.createObject(Survey.class);
        survey.setId(surveyId);
        survey.setSurveyId(formattediId);
        editor = preferences.edit();
        editor.putString("surveyId", formattediId);
        editor.apply();
        survey.setSurveyor(surveyor.getText().toString());
        survey.setRespondentGroup(respondentGroup.getText().toString());
        survey.setLandKinds(landKinds);
        survey.setState(state.getText().toString());
        survey.setDistrict(district.getText().toString());
        survey.setCommunity(community.getText().toString());
        survey.setCountry(country);
        survey.setLanguage(language);
        survey.setCurrency(currency);
        survey.setDate(date);
        realm.commitTransaction();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public LandKind insertAllLandKinds(String serveyIdForLand, String landTypeName){
        realm.beginTransaction();
        LandKind landKind = realm.createObject(LandKind.class);
        landKind.setId(getNextKeyLandKind());
        landKind.setSurveyId(serveyIdForLand);
        landKind.setName(landTypeName);
        landKind.setStatus("deleted");
        realm.commitTransaction();
        return landKind;
    }

    public int getNextKeyLandKind() {
        return realm.where(LandKind.class).max("id").intValue() + 1;
    }
}
