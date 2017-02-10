package com.sayone.omidyar.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import com.sayone.omidyar.model.RevenueProduct;
import com.sayone.omidyar.model.RevenueProductYears;
import com.sayone.omidyar.model.Survey;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmList;

public class NaturalCapitalSurveyActivityC extends BaseActivity implements View.OnClickListener {

    private static final int PROJECTION_COUNT = 15;

    Realm realm;
    SharedPreferences sharedPref;
    String serveyId;
    String currentSocialCapitalServey;

    Spinner spinnerYear;
    TextView question;
    String year;
    Button buttonBack, buttonNext;
    LinearLayout allEditText;
    Context context;
    Button addYearsButton;
    ArrayList<Spinner> editTexts;
    RealmList<RevenueProductYears> revenueProductYearsArrayList;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView textViewAbout;
    private TextView logout;
    private TextView startSurvey;
    private DrawerLayout menuDrawerLayout;
    private TextView surveyIdDrawer;
    private TextView landType;
    TextView enterYearHeading;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_capital_survey_c);

        context = this;
        realm = Realm.getDefaultInstance();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = sharedPref.getString("surveyId", "");
        currentSocialCapitalServey = sharedPref.getString("currentSocialCapitalServey", "");
        Log.e("SER ID ", serveyId);

        editTexts = new ArrayList<>();
        revenueProductYearsArrayList = new RealmList<>();


        spinnerYear = (Spinner) findViewById(R.id.spinner_year);
        buttonBack = (Button) findViewById(R.id.button_back);
        buttonNext = (Button) findViewById(R.id.button_next);
        allEditText = (LinearLayout) findViewById(R.id.all_edit_text);
        addYearsButton = (Button) findViewById(R.id.add_years_button);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        textViewAbout = (TextView) findViewById(R.id.text_view_about);
        logout = (TextView) findViewById(R.id.logout);
        startSurvey = (TextView) findViewById(R.id.text_start_survey);
        surveyIdDrawer = (TextView) findViewById(R.id.text_view_id);
        enterYearHeading = (TextView) findViewById(R.id.enter_year_heading);
        landType = (TextView) findViewById(R.id.land_type);
        question = (TextView) findViewById(R.id.textView17);


        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        addYearsButton.setOnClickListener(this);
        imageViewMenuIcon.setOnClickListener(this);
        drawerCloseBtn.setOnClickListener(this);
        textViewAbout.setOnClickListener(this);
        logout.setOnClickListener(this);
        startSurvey.setOnClickListener(this);
        surveyIdDrawer.setText(serveyId);
        if (currentSocialCapitalServey.equals(getString(R.string.string_forestland)))
            landType.setText(getResources().getText(R.string.string_forestland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_pastureland)))
            landType.setText(getResources().getText(R.string.string_pastureland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_miningland)))
            landType.setText(getResources().getText(R.string.string_miningland));
        if (currentSocialCapitalServey.equals(getString(R.string.string_cropland)))
            landType.setText(getResources().getText(R.string.string_cropland));
        // landType.setText(currentSocialCapitalServey);

        Survey results = realm.where(Survey.class)
                .equalTo("surveyId", serveyId)
                .findFirst();
        for (LandKind landKind : results.getLandKinds()) {
            if (landKind.getName().equals(getString(R.string.string_forestland)) && currentSocialCapitalServey.equals(getString(R.string.string_forestland))) {
                question.setText(getResources().getText(R.string.qn_natural_c));
                if (landKind.getForestLand().getRevenueProducts().size() > 0) {
                    loadYears(landKind.getForestLand().getRevenueProducts().get(0).getRevenueProductYearses());
                }
            } else if (landKind.getName().equals(getString(R.string.string_cropland)) && currentSocialCapitalServey.equals(getString(R.string.string_cropland))) {
                question.setText(getResources().getText(R.string.qn_natural_c));
                if (landKind.getCropLand().getRevenueProducts().size() > 0) {
                    loadYears(landKind.getCropLand().getRevenueProducts().get(0).getRevenueProductYearses());
                }
            } else if (landKind.getName().equals(getString(R.string.string_pastureland)) && currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                question.setText(getResources().getText(R.string.year_data_livestock));
                if (landKind.getPastureLand().getRevenueProducts().size() > 0) {
                    loadYears(landKind.getPastureLand().getRevenueProducts().get(0).getRevenueProductYearses());
                }
            } else if (landKind.getName().equals(getString(R.string.string_miningland)) && currentSocialCapitalServey.equals(getString(R.string.string_miningland))) {
                question.setText(getResources().getText(R.string.year_data_extraction));
                if (landKind.getMiningLand().getRevenueProducts().size() > 0) {
                    loadYears(landKind.getMiningLand().getRevenueProducts().get(0).getRevenueProductYearses());
                }
            }
        }
        if (i == 0) {
            i = 1;
        }


        ArrayAdapter<CharSequence> year_adapter = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_item);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerYear.setAdapter(year_adapter);

        //year= spinnerYear.getSelectedItem().toString();

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                year = parent.getItemAtPosition(pos).toString();
                //Log.e("Year ",year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void loadYears(RealmList<RevenueProductYears> revenueProductYearsRealmList) {
        i = 1;
        LinearLayout.LayoutParams mRparams = new LinearLayout.LayoutParams(400, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (final RevenueProductYears revenueProductYears : revenueProductYearsRealmList) {
            //int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int currentYear = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));

            Log.e("YEAR PEEEEEEEEE", revenueProductYears.getYear() + " " + currentYear);
            if (revenueProductYears.getYear() < currentYear && revenueProductYears.getYear() != 0) {
                ArrayList yearArray = new ArrayList();
                yearArray.add(getResources().getString(R.string.qn_natural_d_4));
                int year = currentYear - 1;
                while (year >= 1990) {
                    yearArray.add(String.valueOf(year));
                    year--;
                }


                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearArray);
                //ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,yearArray);

                final Spinner spinner = new Spinner(this);
                spinner.setAdapter(arrayAdapter);
                spinner.setSelection(arrayAdapter.getPosition(revenueProductYears.getYear() + ""));
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

                        RevenueProductYears revenueProductYearsDel = realm.where(RevenueProductYears.class)
                                .equalTo("id", revenueProductYears.getId())
                                .findFirst();

                        realm.beginTransaction();
                        revenueProductYearsDel.deleteFromRealm();
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
                //setContentView();
                //layout.addView(spinner);
                i++;


//                EditText myEditText = new EditText(context);
//                myEditText.setLayoutParams(mRparams);
//                myEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
//                myEditText.setId(i);
//                myEditText.setHint(getResources().getString(R.string.enter_year_hint)+" "+i);
//                myEditText.setText(revenueProductYears.getYear()+"");
//                allEditText.addView(myEditText);
//                editTexts.add(myEditText);
//                i++;
            }
        }
    }

    public void generateYearFields(int j) {
        LinearLayout.LayoutParams mRparams = new LinearLayout.LayoutParams(400, LinearLayout.LayoutParams.WRAP_CONTENT);
        int k;
        // int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentYear = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
        for (k = 1; k <= j; k++) {
            ArrayList yearArray = new ArrayList();
            int year = currentYear - 1;
            yearArray.add(getResources().getString(R.string.qn_natural_d_4));
            while (year >= 1990) {
                yearArray.add(year--);
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearArray);

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
            //layout.addView(spinner);
            i++;


//            EditText myEditText = new EditText(context);
//            myEditText.setLayoutParams(mRparams);
//            myEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
//            myEditText.setId(k);
//            myEditText.setHint(getResources().getString(R.string.string_atleast_one)+" "+k);
//            allEditText.addView(myEditText);
//            editTexts.add(myEditText);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.button_next:
                if (editTexts.size() > 0) {
                    boolean selectYearNotFoud = true;
                    Set<String> set = new HashSet<String>();
                    for (Spinner editText1 : editTexts) {
                        //editText.setText("233");
                        //Log.e("SSS ",editText.getText().toString());
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
                    // saveYears();
                } else {
                    Toast.makeText(context, getResources().getText(R.string.select_atleast_oneyear), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.button_back:
                finish();
                break;

            case R.id.add_years_button:
                //Log.e("Year ",year);
                if (!year.equals("year")) {
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
                Intent intents = new Intent(getApplicationContext(), RegistrationActivity.class);
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intents);
                break;
        }

    }

    public void saveYears() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle(getResources().getString(R.string.loading));
        progress.setMessage(getResources().getString(R.string.wait_while_loading));
        progress.show();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Survey results = realm.where(Survey.class)
                        .equalTo("surveyId", serveyId)
                        .findFirst();
                for (LandKind landKind : results.getLandKinds()) {
                    if (landKind.getName().equals(getString(R.string.string_forestland)) && currentSocialCapitalServey.equals(getString(R.string.string_forestland))) {
                        for (RevenueProduct revenueProduct1 : landKind.getForestLand().getRevenueProducts()) {
                            //Log.e("LAND ", revenueProduct1.getName());
                            //Log.e("LAND AA ", revenueProduct1.getRevenueProductYearses().size()+"");


                            for (Spinner editText : editTexts) {
                                //editText.setText("233");
                                //Log.e("SSS ",editText.getText().toString());
                                if (!editText.getSelectedItem().toString().equals("")) {
                                    revenueProductYearsArrayList.add(saveProductYears(Integer.parseInt(editText.getSelectedItem().toString()), revenueProduct1.getId(), getString(R.string.string_forestland), realm));

                                }
                            }
                            // int year = Calendar.getInstance().get(Calendar.YEAR);
                            int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
                            revenueProductYearsArrayList.add(saveTrend(revenueProduct1.getId(), getString(R.string.string_forestland), realm));
                            for (int k = 0; k <= PROJECTION_COUNT; k++) {
                                revenueProductYearsArrayList.add(saveProjectionYears(year, revenueProduct1.getId(), getString(R.string.string_forestland), k, realm));
                                year++;
                            }


                            //realm.beginTransaction();
                            revenueProduct1.setRevenueProductYearses(revenueProductYearsArrayList);
                            //realm.commitTransaction();
                            revenueProductYearsArrayList.clear();
                        }
                    } else if (landKind.getName().equals(getString(R.string.string_cropland)) && currentSocialCapitalServey.equals(getString(R.string.string_cropland))) {
                        for (RevenueProduct revenueProduct1 : landKind.getCropLand().getRevenueProducts()) {
                            //Log.e("LAND ", revenueProduct1.getName());
                            //Log.e("LAND AA ", revenueProduct1.getRevenueProductYearses().size()+"");


                            for (Spinner editText : editTexts) {
                                //editText.setText("233");
                                //Log.e("SSS ",editText.getText().toString());
                                revenueProductYearsArrayList.add(saveProductYears(Integer.parseInt(editText.getSelectedItem().toString()), revenueProduct1.getId(), getString(R.string.string_cropland), realm));
                            }

                            // int year = Calendar.getInstance().get(Calendar.YEAR);
                            int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
                            revenueProductYearsArrayList.add(saveTrend(revenueProduct1.getId(), getString(R.string.string_cropland), realm));
                            for (int k = 0; k <= PROJECTION_COUNT; k++) {
                                revenueProductYearsArrayList.add(saveProjectionYears(year, revenueProduct1.getId(), getString(R.string.string_cropland), k, realm));
                                year++;
                            }


                            //realm.beginTransaction();
                            revenueProduct1.setRevenueProductYearses(revenueProductYearsArrayList);
                            //realm.commitTransaction();
                            revenueProductYearsArrayList.clear();
                        }
                    } else if (landKind.getName().equals(getString(R.string.string_pastureland)) && currentSocialCapitalServey.equals(getString(R.string.string_pastureland))) {
                        for (RevenueProduct revenueProduct1 : landKind.getPastureLand().getRevenueProducts()) {
                            //Log.e("LAND ", revenueProduct1.getName());
                            //Log.e("LAND AA ", revenueProduct1.getRevenueProductYearses().size()+"");


                            for (Spinner editText : editTexts) {
                                //editText.setText("233");
                                //Log.e("SSS ",editText.getText().toString());
                                revenueProductYearsArrayList.add(saveProductYears(Integer.parseInt(editText.getSelectedItem().toString()), revenueProduct1.getId(), getString(R.string.string_pastureland), realm));
                            }

                            // int year = Calendar.getInstance().get(Calendar.YEAR);
                            int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
                            revenueProductYearsArrayList.add(saveTrend(revenueProduct1.getId(), getString(R.string.string_pastureland), realm));
                            for (int k = 0; k <= PROJECTION_COUNT; k++) {
                                revenueProductYearsArrayList.add(saveProjectionYears(year, revenueProduct1.getId(), getString(R.string.string_pastureland), k, realm));
                                year++;
                            }


                            //realm.beginTransaction();
                            revenueProduct1.setRevenueProductYearses(revenueProductYearsArrayList);
                            //realm.commitTransaction();
                            revenueProductYearsArrayList.clear();
                        }
                    } else if (landKind.getName().equals(getString(R.string.string_miningland)) && currentSocialCapitalServey.equals(getString(R.string.string_miningland))) {
                        for (RevenueProduct revenueProduct1 : landKind.getMiningLand().getRevenueProducts()) {
                            //Log.e("LAND ", revenueProduct1.getName());
                            //Log.e("LAND AA ", revenueProduct1.getRevenueProductYearses().size()+"");


                            for (Spinner editText : editTexts) {
                                //editText.setText("233");
                                //Log.e("SSS ",editText.getText().toString());
                                revenueProductYearsArrayList.add(saveProductYears(Integer.parseInt(editText.getSelectedItem().toString()), revenueProduct1.getId(), getString(R.string.string_miningland), realm));
                            }

                            // int year = Calendar.getInstance().get(Calendar.YEAR);
                            int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
                            revenueProductYearsArrayList.add(saveTrend(revenueProduct1.getId(), getString(R.string.string_miningland), realm));
                            for (int k = 0; k <= PROJECTION_COUNT; k++) {
                                revenueProductYearsArrayList.add(saveProjectionYears(year, revenueProduct1.getId(), getString(R.string.string_miningland), k, realm));
                                year++;
                            }


                            //realm.beginTransaction();
                            revenueProduct1.setRevenueProductYearses(revenueProductYearsArrayList);
                            //realm.commitTransaction();
                            revenueProductYearsArrayList.clear();
                        }
                    }
                }


            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                progress.dismiss();
                Intent intent = new Intent(getApplicationContext(), NaturalCapitalSurveyActivityD.class);
                startActivity(intent);
                Log.e("REALM", "All done updating.");
                // Log.d("BG", t.getName());
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // transaction is automatically rolled-back, do any cleanup here
                Log.e("REALM", "All done updating." + error.toString());
            }
        });


//        Survey results1 = realm.where(Survey.class).findFirst();
//        for(LandKind landKind:results1.getLandKinds()){
//            if(landKind.getName().equals("Forestland")){
//                for (RevenueProduct revenueProduct1: landKind.getForestLand().getRevenueProducts()){
//                    Log.e("YEAR 1 ",revenueProduct1.getName()+"");
//
//                    for(RevenueProductYears revenueProductYears:revenueProduct1.getRevenueProductYearses()){
//
//                        Log.e("YEAR ",revenueProductYears.getYear()+" "+revenueProductYears.getId());
//                    }
//                }
//            }
//        }
    }


    public RevenueProductYears saveProductYears(int yearVal, long revenueProductId, String landKindName, Realm realm) {
        Log.e("CCC ", serveyId + " " + yearVal + " " + revenueProductId + " " + landKindName);
        RevenueProductYears revenueProductYearsCheck = realm.where(RevenueProductYears.class)
                .equalTo("surveyId", serveyId)
                .equalTo("landKind", landKindName)
                .equalTo("revenueProductId", revenueProductId)
                .equalTo("year", yearVal)
                .findFirst();
        for (RevenueProductYears revenueProductYears1 : realm.where(RevenueProductYears.class).findAll()) {
            // Log.e("BBB ", revenueProductYears1.toString());
        }
        Log.e("AA ", String.valueOf(revenueProductYearsCheck == null));
        if (revenueProductYearsCheck == null) {
            Log.e("AA ", "212121212121212");
            //realm.beginTransaction();
            RevenueProductYears revenueProductYears = realm.createObject(RevenueProductYears.class);
            revenueProductYears.setId(getNextKeyRevenueProductYears(realm));
            revenueProductYears.setYear(yearVal);
            revenueProductYears.setRevenueProductId(revenueProductId);
            revenueProductYears.setLandKind(landKindName);
            revenueProductYears.setSurveyId(serveyId);
            //realm.commitTransaction();
            return revenueProductYears;
        } else {
            return revenueProductYearsCheck;
        }
    }

    public RevenueProductYears saveTrend(long revenueProductId, String landKindName, Realm realm) {
        Log.e("CCC ", serveyId + " " + 0 + " " + revenueProductId + " " + landKindName);
        RevenueProductYears revenueProductYearsCheck = realm.where(RevenueProductYears.class)
                .equalTo("surveyId", serveyId)
                .equalTo("landKind", landKindName)
                .equalTo("revenueProductId", revenueProductId)
                .equalTo("year", 0)
                .findFirst();
        for (RevenueProductYears revenueProductYears1 : realm.where(RevenueProductYears.class).findAll()) {
            //Log.e("BBB ", revenueProductYears1.toString());
        }
        Log.e("AA ", String.valueOf(revenueProductYearsCheck == null));
        if (revenueProductYearsCheck == null) {
            //realm.beginTransaction();
            RevenueProductYears revenueProductYears = realm.createObject(RevenueProductYears.class);
            revenueProductYears.setId(getNextKeyRevenueProductYears(realm));
            revenueProductYears.setYear(0);
            revenueProductYears.setRevenueProductId(revenueProductId);
            revenueProductYears.setLandKind(landKindName);
            revenueProductYears.setSurveyId(serveyId);
            //realm.commitTransaction();
            return revenueProductYears;
        } else {
            return revenueProductYearsCheck;
        }
    }

    public RevenueProductYears saveProjectionYears(int yearVal, long revenueProductId, String landKindName, int projectionIndex, Realm realm) {
        Log.e("CCC ", serveyId + " " + yearVal + " " + revenueProductId + " " + landKindName);
        RevenueProductYears revenueProductYearsCheck = realm.where(RevenueProductYears.class)
                .equalTo("surveyId", serveyId)
                .equalTo("landKind", landKindName)
                .equalTo("revenueProductId", revenueProductId)
                .equalTo("year", yearVal)
                .findFirst();
        //calculateProjectionIndex(projectionIndex);
        for (RevenueProductYears revenueProductYears1 : realm.where(RevenueProductYears.class).findAll()) {
            //Log.e("BBB ", revenueProductYears1.toString());
        }
        Log.e("AA ", String.valueOf(revenueProductYearsCheck == null));
        Log.e("INDEX ZERO ", calculateProjectionIndex(projectionIndex) + "");
        if (revenueProductYearsCheck == null) {
            //realm.beginTransaction();
            RevenueProductYears revenueProductYears = realm.createObject(RevenueProductYears.class);
            revenueProductYears.setId(getNextKeyRevenueProductYears(realm));
            revenueProductYears.setYear(yearVal);
            revenueProductYears.setRevenueProductId(revenueProductId);
            revenueProductYears.setLandKind(landKindName);
            revenueProductYears.setSurveyId(serveyId);
            revenueProductYears.setProjectedIndex(calculateProjectionIndex(projectionIndex));
            //realm.commitTransaction();
            return revenueProductYears;
        } else {
            return revenueProductYearsCheck;
        }
    }

    public int getNextKeyRevenueProductYears(Realm realm) {
        return realm.where(RevenueProductYears.class).max("id").intValue() + 1;
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

        // int year = Calendar.getInstance().get(Calendar.YEAR);
        int year = sharedPref.getInt("surveyyear", Calendar.getInstance().get(Calendar.YEAR));
        for (i = 0; i <= val; i++) {
            if (i == 0) {
                resVal = 0;
            } else if (year % 4 == 0) {
                BigDecimal bigDecimal1 = new BigDecimal("366.0");
                BigDecimal bigDecimal2 = new BigDecimal("365.0");

                //double aa = 366.0 / 365.0;
                BigDecimal bigDecimal3 = bigDecimal1.divide(bigDecimal2, MathContext.DECIMAL64);
                double aa = bigDecimal3.doubleValue();

                Log.e("PRO IND BB ", aa + "");
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
