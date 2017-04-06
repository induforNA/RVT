package com.sayone.omidyar.view;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.ApiClient;
import com.sayone.omidyar.model.ApiInterface;
import com.sayone.omidyar.model.CropLand;
import com.sayone.omidyar.model.DevIdSend;
import com.sayone.omidyar.model.ForestLand;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.MiningLand;
import com.sayone.omidyar.model.ParcelLocation;
import com.sayone.omidyar.model.PastureLand;
import com.sayone.omidyar.model.Survey;
import com.sayone.omidyar.model.UniqueId;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sayone on 16/9/16.
 */
public class RegistrationActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = RegistrationActivity.class.getName();
    private static final int MY_PERMISSIONS_REQUEST = 0;
    private double inflationRate = 0.05;
    private double riskRate;
    private Realm realm;
    private AutoCompleteTextView dateView;
    private int surveyId;
    private Date date;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private ProgressDialog progressDialog;
    private boolean isFirstLaunch;
    private Button signUp;
    private Button login;
    private Button about;
    private AutoCompleteTextView respondentGroup;
    private AutoCompleteTextView state;
    private AutoCompleteTextView district;
    private AutoCompleteTextView community;
    private AutoCompleteTextView surveyor;
    private EditText editRiskRate;
    private EditText editInflationRate;
//    private EditText country;
//    private EditText currency;
    private Spinner languageSpinner;
    private Spinner countrySpinner;
    private Spinner currencySpinner;
    private String country;
    private String language;
    private String currency;
    private String selectedDate;
    private TextView sourceRiskrate;
    private TextView sourceInflationRate;

    static String encode(long num, String symbols) {
        final int B = symbols.length();
        StringBuilder sb = new StringBuilder();
        while (num != 0) {
            sb.append(symbols.charAt((int) (num % B)));
            num /= B;
        }
        return sb.reverse().toString();
    }

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
        about = (Button) findViewById(R.id.button_about);

        respondentGroup = (AutoCompleteTextView) findViewById(R.id.edittext_respondent_group);
        state = (AutoCompleteTextView) findViewById(R.id.edittext_state);
        district = (AutoCompleteTextView) findViewById(R.id.edittext_district);
        community = (AutoCompleteTextView) findViewById(R.id.edittext_community);
        surveyor = (AutoCompleteTextView) findViewById(R.id.edittext_surveyor);
        dateView = (AutoCompleteTextView) findViewById(R.id.edittext_date);
        editRiskRate = (EditText) findViewById(R.id.reg_risk_rate);
        editInflationRate = (EditText) findViewById(R.id.reg_inflation_rate);
//
//        country = (EditText) findViewById(R.id.edittext_country);
//        currency = (EditText) findViewById(R.id.edittext_currency);
        languageSpinner = (Spinner) findViewById(R.id.edittext_language);
        countrySpinner = (Spinner) findViewById(R.id.spinner_country);
        currencySpinner = (Spinner) findViewById(R.id.spinner_currency);

        sourceInflationRate = (TextView) findViewById(R.id.inflation_rate_source);
        sourceRiskrate = (TextView) findViewById(R.id.risk_rate_source);
        sourceInflationRate.setOnClickListener(this);
        sourceRiskrate.setOnClickListener(this);

        dateView.setInputType(InputType.TYPE_NULL);
        login.setOnClickListener(this);
        signUp.setOnClickListener(this);
        about.setOnClickListener(this);
        dateView.setOnClickListener(this);

        language = Locale.getDefault().getDisplayLanguage();

        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        ArrayAdapter<CharSequence> country_adapter = ArrayAdapter.createFromResource(this,
                R.array.country_array, android.R.layout.simple_spinner_dropdown_item);
        country_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> language_adapter = ArrayAdapter.createFromResource(this,
                R.array.language_array, android.R.layout.simple_spinner_dropdown_item);
        language_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> currency_adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_dropdown_item);
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
                Locale myLocale = new Locale(language);
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);

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

        if (ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegistrationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST);
            }
        }
        isFirstLaunch = preferences.getBoolean("firstLaunch", true);
        if (isFirstLaunch) {
            progressDialog = new ProgressDialog(RegistrationActivity.this);
            progressDialog.setMessage("Registering device id");
            progressDialog.setCancelable(false);
            progressDialog.show();
            getUniqueId();
        }
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
        SharedPreferences.Editor editor = preferences.edit();
        if(preferences.getInt("serialNumber",0) == 0) {
            editor.putInt("serialNumber",1);
        } else
            editor.putInt("serialNumber",preferences.getInt("serialNumber", 0)+1);
        editor.apply();

       /* if (realm.where(Survey.class).max("id") == null) {
            return Integer.parseInt(preferences.getString("serialNumber", "0000"))+1;
//            return 00001;
        }*/

        return preferences.getInt("serialNumber",0);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.risk_rate_source:
                Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.tradingeconomics.com/bonds"));
                startActivity(browserIntent1);
                break;
            case R.id.inflation_rate_source:
                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.tradingeconomics.com/forecast/inflation-rate"));
                startActivity(browserIntent2);
                break;
            case R.id.button_login:
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                break;

            case R.id.button_about:
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
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
                } /*else if (currency.getText().toString().equals("")) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_currency_field));*/
                /*} else if (currency.getText().toString().length()>3) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.invalid_currency_field));
                } */else if (community.getText().toString().equals("")) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_community_field));
                } else if (district.getText().toString().equals("")) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_district_filed));
                } else if (state.getText().toString().equals("")) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_state_filed));
                }/* else if (country.getText().toString().equals("")) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_country_filed));
                }*/ else if (editRiskRate.getText().toString().equals("")) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_risk_rate));
                } else if (editInflationRate.getText().toString().equals("")) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_inflation_rate));
                } else {
//                    double tempInflationRate = Double.parseDouble(editInflationRate.getText().toString());
//                    double tempRiskRate = Double.parseDouble(editRiskRate.getText().toString());
//                    if (tempRiskRate < tempInflationRate) {
//                        toastfunction(getApplicationContext(), getResources().getString(R.string.sovereign_larger_than_inflation));
//                    } else {
                        toastfunction(getApplicationContext(), getResources().getString(R.string.registration_successful));
                        insertData();
//                    }
                }
                break;
        }
    }

    private void getUniqueId() {
        String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        DevIdSend devIdSend = new DevIdSend(androidId);

        Call<UniqueId> call = apiService.getUniqueId(devIdSend);
        call.enqueue(new Callback<UniqueId>() {
            @Override
            public void onResponse(Call<UniqueId> call, Response<UniqueId> response) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("uniqueId", response.body().getUniqueId());
                editor.putBoolean("firstLaunch", false);
                editor.apply();
            }

            @Override
            public void onFailure(Call<UniqueId> call, Throwable t) {

            }
        });
    }

    public void insertData() {
        surveyId = getNextKeySurvey();

        String enId = preferences.getString("uniqueId", "0000");

        String formattediId = String.format("%04d", surveyId);

        enId = enId.toUpperCase();

        formattediId = enId + formattediId;
        RealmList<LandKind> landKinds = new RealmList<>();

        landKinds.add(insertAllLandKinds(formattediId, getString(R.string.string_forestland)));
        landKinds.add(insertAllLandKinds(formattediId, getString(R.string.string_cropland)));
        landKinds.add(insertAllLandKinds(formattediId, getString(R.string.string_pastureland)));
        landKinds.add(insertAllLandKinds(formattediId, getString(R.string.string_miningland)));

        inflationRate = Double.parseDouble(editInflationRate.getText().toString());
        riskRate = Double.parseDouble(editRiskRate.getText().toString());

        realm.beginTransaction();
        Survey survey = realm.createObject(Survey.class);
        survey.setId(getNextKeyComponent());
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
        survey.setSendStatus(false);
        survey.setInflationRate(inflationRate);
        survey.setRiskRate(riskRate);
        realm.commitTransaction();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public LandKind insertAllLandKinds(String serveyIdForLand, String landTypeName) {
        ForestLand forestLand = null;
        CropLand cropLand = null;
        PastureLand pastureLand = null;
        MiningLand miningLand = null;
        if (landTypeName.equals(getString(R.string.string_forestland))) {
            realm.beginTransaction();
            forestLand = realm.createObject(ForestLand.class);
            forestLand.setId(getNextKeyForestLand());
            realm.commitTransaction();
        } else if (landTypeName.equals(getString(R.string.string_cropland))) {
            realm.beginTransaction();
            cropLand = realm.createObject(CropLand.class);
            cropLand.setId(getNextKeyCropLand());
            realm.commitTransaction();
        } else if (landTypeName.equals(getString(R.string.string_pastureland))) {
            realm.beginTransaction();
            pastureLand = realm.createObject(PastureLand.class);
            pastureLand.setId(getNextKeyPastureLand());
            realm.commitTransaction();
        } else if (landTypeName.equals(getString(R.string.string_miningland))) {
            realm.beginTransaction();
            miningLand = realm.createObject(MiningLand.class);
            miningLand.setId(getNextKeyMiningLand());
            realm.commitTransaction();
        }


        realm.beginTransaction();
        LandKind landKind = realm.createObject(LandKind.class);
        landKind.setId(getNextKeyLandKind());
        landKind.setSurveyId(serveyIdForLand);
        landKind.setName(landTypeName);
        landKind.setStatus("deleted");
        landKind.setForestLand(forestLand);
        landKind.setCropLand(cropLand);
        landKind.setPastureLand(pastureLand);
        landKind.setMiningLand(miningLand);
        realm.commitTransaction();


        return landKind;
    }

    public int getNextKeyLandKind() {
        return realm.where(LandKind.class).max("id").intValue() + 1;
    }

    public int getNextKeyForestLand() {
        return realm.where(ForestLand.class).max("id").intValue() + 1;
    }

    public int getNextKeyCropLand() {
        return realm.where(CropLand.class).max("id").intValue() + 1;
    }

    public int getNextKeyPastureLand() {
        return realm.where(PastureLand.class).max("id").intValue() + 1;
    }

    public int getNextKeyMiningLand() {
        return realm.where(MiningLand.class).max("id").intValue() + 1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    if (ContextCompat.checkSelfPermission(RegistrationActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(RegistrationActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION)) {

                        } else {
                            ActivityCompat.requestPermissions(RegistrationActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST);
                        }
                    }
                }
        }
    }

    public int getNextKeyComponent() {
        return realm.where(ParcelLocation.class).max("id").intValue() + 1;
    }
}
