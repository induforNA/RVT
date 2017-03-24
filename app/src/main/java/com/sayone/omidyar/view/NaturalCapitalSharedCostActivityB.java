package com.sayone.omidyar.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.SharedCostElement;
import com.sayone.omidyar.model.SharedCostElementYears;
import com.sayone.omidyar.model.Survey;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmList;

public class NaturalCapitalSharedCostActivityB extends BaseActivity implements View.OnClickListener {

    private static final int PROJECTION_COUNT = 15;
    private Realm realm;
    private SharedPreferences sharedPref;
    private String surveyId;
    private String currentSocialCapitalSurvey;

    private Spinner spinnerYear;
    private String year;
    private Button buttonBack, buttonNext;
    private LinearLayout allEditText;
    private Context context;
    private Button addYearsButton;
    private ArrayList<Spinner> editTexts;
    private RealmList<SharedCostElementYears> costElementYearsArrayList;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private DrawerLayout menuDrawerLayout;
    private TextView surveyIdDrawer;
    private TextView landType;
    private TextView enterYearHeading;

    private int i = 0;

    //Side Nav
    private TextView textViewAbout;
    private TextView startSurvey;
    private TextView harvestingForestProducts;
    private TextView agriculture;
    private TextView grazing;
    private TextView mining;
    private TextView sharedCostsOutlays;
    private TextView certificate;
    private TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_shared_cost_survey_b);

        context = this;
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        surveyId = sharedPref.getString("surveyId", "");
        currentSocialCapitalSurvey = sharedPref.getString("currentSocialCapitalSurvey", "");

        editTexts = new ArrayList<>();
        costElementYearsArrayList = new RealmList<>();

        spinnerYear = (Spinner) findViewById(R.id.spinner_year);
        buttonBack = (Button) findViewById(R.id.button_back);
        buttonNext = (Button) findViewById(R.id.button_next);
        allEditText = (LinearLayout) findViewById(R.id.all_edit_text);
        addYearsButton = (Button) findViewById(R.id.add_years_button);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        surveyIdDrawer = (TextView) findViewById(R.id.text_view_id);
        enterYearHeading = (TextView) findViewById(R.id.enter_year_heading);
        landType = (TextView) findViewById(R.id.land_type);

        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        addYearsButton.setOnClickListener(this);
        imageViewMenuIcon.setOnClickListener(this);
        drawerCloseBtn.setOnClickListener(this);
        surveyIdDrawer.setText(surveyId);
        landType.setText(getResources().getText(R.string.shared_costs_outlays));

        Survey results = realm.where(Survey.class)
                .equalTo("surveyId", surveyId)
                .findFirst();
        loadYears(results.getSharedCostElements().get(0).getCostElementYearses());

        if (i == 0) {
            i = 1;
        }

        ArrayAdapter<CharSequence> year_adapter = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_dropdown_item);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerYear.setAdapter(year_adapter);

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                year = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        //Side Nav
        textViewAbout = (TextView) findViewById(R.id.text_view_about);
        startSurvey = (TextView) findViewById(R.id.text_start_survey);
        harvestingForestProducts = (TextView) findViewById(R.id.text_harvesting_forest_products);
        agriculture = (TextView) findViewById(R.id.text_agriculture);
        grazing = (TextView) findViewById(R.id.text_grazing);
        mining = (TextView) findViewById(R.id.text_mining);
        sharedCostsOutlays = (TextView) findViewById(R.id.text_shared_costs_outlays);
        certificate = (TextView) findViewById(R.id.text_certificate);
        logout = (TextView) findViewById(R.id.logout);
        textViewAbout.setOnClickListener(this);
        startSurvey.setOnClickListener(this);
        harvestingForestProducts.setOnClickListener(this);
        agriculture.setOnClickListener(this);
        grazing.setOnClickListener(this);
        mining.setOnClickListener(this);
        sharedCostsOutlays.setOnClickListener(this);
        certificate.setOnClickListener(this);
        logout.setOnClickListener(this);
        setNav();
    }

    @Override
    protected void onResume() {
        setNav();
        super.onResume();
    }

    public void loadYears(RealmList<SharedCostElementYears> costElementYearsRealmList) {
        i = 1;
        LinearLayout.LayoutParams mRparams = new LinearLayout.LayoutParams(400, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (final SharedCostElementYears costElementYears : costElementYearsRealmList) {
            int currentYear = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));

            if (costElementYears.getYear() < currentYear && costElementYears.getYear() != 0) {
                ArrayList yearArray = new ArrayList();
                int year = currentYear - 1;
                yearArray.add(getResources().getString(R.string.qn_natural_d_4));
                while (year >= 1990) {
                    yearArray.add(String.valueOf(year));
                    year--;
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, yearArray);

                final Spinner spinner = new Spinner(this);
                spinner.setAdapter(arrayAdapter);
                spinner.setSelection(arrayAdapter.getPosition(costElementYears.getYear() + ""));
                spinner.setLayoutParams(mRparams);

                spinner.setId(i);
                spinner.setTag(R.id.tagName, "loaded");

                final LinearLayout parent = new LinearLayout(context);
                parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                parent.setOrientation(LinearLayout.HORIZONTAL);

                ImageView iv = new ImageView(context);
                iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                iv.setImageResource(R.drawable.ic_delete_black_24dp);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String spinnerYear = spinner.getSelectedItem().toString();

                        SharedCostElementYears costElementYearsDel = realm.where(SharedCostElementYears.class)
                                .equalTo("id", costElementYears.getId())
                                .findFirst();

                        realm.beginTransaction();
                        costElementYearsDel.deleteFromRealm();
                        realm.commitTransaction();

                        allEditText.removeView(parent);
                        editTexts.remove(spinner);
                    }
                });

                parent.addView(spinner);
                parent.addView(iv);

                allEditText.addView(parent);
                editTexts.add(spinner);

                arrayAdapter.notifyDataSetChanged();
                i++;
            }
        }
    }

    public void generateYearFields(int j) {
        LinearLayout.LayoutParams mRparams = new LinearLayout.LayoutParams(400, LinearLayout.LayoutParams.WRAP_CONTENT);
        int k;
        int currentYear = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
        for (k = 1; k <= j; k++) {
            ArrayList yearArray = new ArrayList();
            int year = currentYear - 1;
            yearArray.add(getResources().getString(R.string.qn_natural_d_4));
            while (year >= 1990) {
                yearArray.add(year--);
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, yearArray);

            final Spinner spinner = new Spinner(this);
            spinner.setLayoutParams(mRparams);
            spinner.setAdapter(arrayAdapter);
            spinner.setId(k);
            spinner.setTag(R.id.tagName, "generated");


            final LinearLayout parent = new LinearLayout(context);
            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            parent.setOrientation(LinearLayout.HORIZONTAL);


            ImageView iv = new ImageView(context);
            iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            iv.setImageResource(R.drawable.ic_delete_black_24dp);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allEditText.removeView(parent);
                    editTexts.remove(spinner);
                }
            });

            parent.addView(spinner);
            parent.addView(iv);

            allEditText.addView(parent);
            editTexts.add(spinner);
            i++;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button_next:
                if (editTexts.size() > 0) {
                    boolean selectYearNotFoud = true;
                    Set<String> set = new HashSet<String>();
                    for (Spinner editText1 : editTexts) {
                        if (editText1.getSelectedItem().toString().length() > 5) {
                            selectYearNotFoud = false;
                        } else {
                            set.add(editText1.getSelectedItem().toString());
                        }
                    }
                    if (selectYearNotFoud) {
                        if (set.size() < editTexts.size()) {
                            Toast.makeText(context, getResources().getText(R.string.select_different_year), Toast.LENGTH_SHORT).show();
                        } else {
                            saveYears();
                        }
                    } else {
                        Toast.makeText(context, getResources().getText(R.string.select_all_year), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getResources().getText(R.string.select_atleast_oneyear), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.button_back:
                finish();
                break;

            case R.id.add_years_button:
                if (!year.equals("year") && !year.equals("साल")) {
                    enterYearHeading.setVisibility(View.VISIBLE);
                    generateYearFields(Integer.parseInt(year));
                } else {
                    Toast.makeText(context, getResources().getString(R.string.select_no_of_years), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.image_view_menu_icon:
                toggleMenuDrawer();
                break;
            case R.id.drawer_close_btn:
                toggleMenuDrawer();
                break;
            case R.id.text_view_about:
                Intent i = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(i);
                break;
            case R.id.text_start_survey:
                Intent intentt = new Intent(getApplicationContext(), MainActivity.class);
                intentt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentt);
                break;
            case R.id.logout:
                Locale myLocale = new Locale(Locale.getDefault().getDisplayLanguage());
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);

                Intent intents = new Intent(getApplicationContext(), RegistrationActivity.class);
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intents);
                break;
            case R.id.text_harvesting_forest_products:
                setCurrentSocialCapitalSurvey(getString(R.string.string_forestland));
                startLandTypeActivity();
                break;
            case R.id.text_agriculture:
                setCurrentSocialCapitalSurvey(getString(R.string.string_cropland));
                startLandTypeActivity();
                break;
            case R.id.text_grazing:
                setCurrentSocialCapitalSurvey(getString(R.string.string_pastureland));
                startLandTypeActivity();
                break;
            case R.id.text_mining:
                setCurrentSocialCapitalSurvey(getString(R.string.string_miningland));
                startLandTypeActivity();
                break;
            case R.id.text_shared_costs_outlays:
                Intent intent_outlay = new Intent(getApplicationContext(), SharedCostSurveyStartActivity.class);
                startActivity(intent_outlay);
                break;
            case R.id.text_certificate:
                Intent intent_certificate = new Intent(getApplicationContext(), NewCertificateActivity.class);
                startActivity(intent_certificate);
                break;
        }
    }

    private void setNav() {
        harvestingForestProducts.setVisibility(View.GONE);
        agriculture.setVisibility(View.GONE);
        grazing.setVisibility(View.GONE);
        mining.setVisibility(View.GONE);

        if (checkLandKind(getString(R.string.string_forestland))) {
            harvestingForestProducts.setVisibility(View.VISIBLE);
        }

        if (checkLandKind(getString(R.string.string_cropland))) {
            agriculture.setVisibility(View.VISIBLE);
        }

        if (checkLandKind(getString(R.string.string_pastureland))) {
            grazing.setVisibility(View.VISIBLE);
        }

        if (checkLandKind(getString(R.string.string_miningland))) {
            mining.setVisibility(View.VISIBLE);
        }
    }

    private void startLandTypeActivity() {
        Intent intent = new Intent(NaturalCapitalSharedCostActivityB.this, StartLandTypeActivity.class);
        startActivity(intent);
    }

    private void setCurrentSocialCapitalSurvey(String name) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("currentSocialCapitalSurvey", name);
        editor.apply();
    }

    private boolean checkLandKind(String name) {
        LandKind landKind = realm.where(LandKind.class)
                .equalTo("name", name)
                .equalTo("surveyId", surveyId)
                .findFirst();
        if (landKind.getStatus().equals("active")) {
            return true;
        } else {
            return false;
        }
    }

    public void saveYears() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle(getResources().getText(R.string.loading));
        progress.setMessage(getResources().getText(R.string.wait_while_loading));
        progress.show();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Survey results = realm.where(Survey.class)
                        .equalTo("surveyId", surveyId)
                        .findFirst();
                for (SharedCostElement costElement1 : results.getSharedCostElements()) {

                    for (Spinner editText : editTexts) {
                        if (!editText.getSelectedItem().toString().equals("")) {
                            costElementYearsArrayList.add(saveProductYears(Integer.parseInt(editText.getSelectedItem().toString()), costElement1.getId(), "", realm));

                        }
                    }
                    int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
                    costElementYearsArrayList.add(saveTrend(costElement1.getId(), "", realm));
                    for (int k = 0; k <= PROJECTION_COUNT; k++) {
                        costElementYearsArrayList.add(saveProjectionYears(year, costElement1.getId(), "", k, realm));
                        year++;
                    }
                    costElement1.setCostElementYearses(costElementYearsArrayList);
                    costElementYearsArrayList.clear();
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                progress.dismiss();

                Intent intent = new Intent(getApplicationContext(), NaturalCapitalSharedCostActivityC.class);
                startActivity(intent);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // transaction is automatically rolled-back, do any cleanup here
                //Log.e("REALM", "All done updating."+error.toString());
            }
        });

    }

    public SharedCostElementYears saveProductYears(int yearVal, long costElementId, String landKindName, Realm realm) {
        SharedCostElementYears costElementYearsCheck = realm.where(SharedCostElementYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("costElementId", costElementId)
                .equalTo("year", yearVal)
                .findFirst();
        for (SharedCostElementYears costElementYears1 : realm.where(SharedCostElementYears.class).findAll()) {
            // Log.e("BBB ", costElementYears1.toString());
        }
        if (costElementYearsCheck == null) {
            SharedCostElementYears costElementYears = realm.createObject(SharedCostElementYears.class);
            costElementYears.setId(getNextKeyRevenueProductYears(realm));
            costElementYears.setYear(yearVal);
            costElementYears.setCostElementId(costElementId);
            costElementYears.setLandKind(landKindName);
            costElementYears.setSurveyId(surveyId);
            return costElementYears;
        } else {
            return costElementYearsCheck;
        }
    }

    public SharedCostElementYears saveTrend(long costElementId, String landKindName, Realm realm) {
        SharedCostElementYears costElementYearsCheck = realm.where(SharedCostElementYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("costElementId", costElementId)
                .equalTo("year", 0)
                .findFirst();
        for (SharedCostElementYears costElementYears1 : realm.where(SharedCostElementYears.class).findAll()) {
        }

        if (costElementYearsCheck == null) {
            SharedCostElementYears costElementYears = realm.createObject(SharedCostElementYears.class);
            costElementYears.setId(getNextKeyRevenueProductYears(realm));
            costElementYears.setYear(0);
            costElementYears.setCostElementId(costElementId);
            costElementYears.setLandKind(landKindName);
            costElementYears.setSurveyId(surveyId);
            return costElementYears;
        } else {
            return costElementYearsCheck;
        }
    }

    public SharedCostElementYears saveProjectionYears(int yearVal, long costElementId, String landKindName, int projectionIndex, Realm realm) {
        SharedCostElementYears costElementYearsCheck = realm.where(SharedCostElementYears.class)
                .equalTo("surveyId", surveyId)
                .equalTo("costElementId", costElementId)
                .equalTo("year", yearVal)
                .findFirst();
        for (SharedCostElementYears costElementYears1 : realm.where(SharedCostElementYears.class).findAll()) {
            //Log.e("BBB ", costElementYears1.toString());
        }

        if (costElementYearsCheck == null) {
            SharedCostElementYears costElementYears = realm.createObject(SharedCostElementYears.class);
            costElementYears.setId(getNextKeyRevenueProductYears(realm));
            costElementYears.setYear(yearVal);
            costElementYears.setCostElementId(costElementId);
            costElementYears.setLandKind(landKindName);
            costElementYears.setSurveyId(surveyId);
            costElementYears.setProjectedIndex(calculateProjectionIndex(projectionIndex));
            return costElementYears;
        } else {
            return costElementYearsCheck;
        }
    }

    public int getNextKeyRevenueProductYears(Realm realm) {
        return realm.where(SharedCostElementYears.class).max("id").intValue() + 1;
    }

    public void toggleMenuDrawer() {
        if (menuDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            menuDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public double calculateProjectionIndex(double val) {
        double resVal = 0;

        int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
        for (i = 0; i <= val; i++) {
            if (i == 0) {
                resVal = 0;
            } else if (year % 4 == 0) {
                BigDecimal bigDecimal1 = new BigDecimal("366.0");
                BigDecimal bigDecimal2 = new BigDecimal("365.0");

                BigDecimal bigDecimal3 = bigDecimal1.divide(bigDecimal2, MathContext.DECIMAL64);
                double aa = bigDecimal3.doubleValue();
                resVal = resVal + aa;
            } else {
                resVal = resVal + 1;
            }
            year++;
        }
        Log.e("PRO IND AA ", resVal + "");
        return resVal;
    }
}
