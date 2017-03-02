package com.sayone.omidyar.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.adapter.SurveyAdapter;
import com.sayone.omidyar.model.ApiClient;
import com.sayone.omidyar.model.ApiInterface;
import com.sayone.omidyar.model.CashFlow;
import com.sayone.omidyar.model.Component;
import com.sayone.omidyar.model.CostElement;
import com.sayone.omidyar.model.CostElementYears;
import com.sayone.omidyar.model.CropLand;
import com.sayone.omidyar.model.DataWithId;
import com.sayone.omidyar.model.DiscountedCashFlow;
import com.sayone.omidyar.model.DiscountingFactor;
import com.sayone.omidyar.model.ExportData;
import com.sayone.omidyar.model.ForestLand;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.MiningLand;
import com.sayone.omidyar.model.MultipleAnswer;
import com.sayone.omidyar.model.Outlay;
import com.sayone.omidyar.model.OutlayYears;
import com.sayone.omidyar.model.ParcelLocation;
import com.sayone.omidyar.model.Participant;
import com.sayone.omidyar.model.PastureLand;
import com.sayone.omidyar.model.RevenueProduct;
import com.sayone.omidyar.model.RevenueProductYears;
import com.sayone.omidyar.model.SharedCostElement;
import com.sayone.omidyar.model.SharedCostElementYears;
import com.sayone.omidyar.model.SocialCapital;
import com.sayone.omidyar.model.SocialCapitalAnswer;
import com.sayone.omidyar.model.SocialCapitalAnswerOptions;
import com.sayone.omidyar.model.SocialCapitalQuestions;
import com.sayone.omidyar.model.Survey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;

public class SurveySummaryActivity extends BaseActivity implements View.OnClickListener {

    TextView completedSurveys;
    CheckBox checkBoxSurvey;
    RecyclerView recyclerView;
    RealmList<Survey> surveys;
    private SurveyAdapter surveyAdapter;
    RealmResults<Survey> surveyList;
    int surveyCount;

    JSONObject jsonObject;
    private Context context;
    private Button sendDataToServer, resetData, exportDataEmail;
    private SharedPreferences sharedPref;
    private Set<String> set = null;
    private Realm realm;

    String emailIdsStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_summary);
        context = this;


        // checkBoxSurvey1=(CheckBox)findViewById(R.id.checkBox_survey1);
        // checkBoxSurvey2=(CheckBox)findViewById(R.id.checkBox_survey2);
        completedSurveys = (TextView) findViewById(R.id.completed_surveys);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_survey_list);
        sendDataToServer = (Button) findViewById(R.id.button_send_data_to_server);
        resetData = (Button) findViewById(R.id.button_reset_data);
        exportDataEmail = (Button) findViewById(R.id.button_export_data_email);
        exportDataEmail.setEnabled(false);
        exportDataEmail.setBackgroundResource(android.R.drawable.btn_default);
        exportDataEmail.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorDisable), PorterDuff.Mode.MULTIPLY);

        sharedPref = context.getSharedPreferences(
                "com.sayone.omidyar.PREFERENCE_FILE_KEY_SET", Context.MODE_PRIVATE);

        emailIdsStr = sharedPref.getString("emailIdsStr", "");


        realm = Realm.getDefaultInstance();
        surveyList = realm.where(Survey.class).findAll();
        surveyCount = surveyList.size();


        surveyAdapter = new SurveyAdapter(surveyList, SurveySummaryActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(surveyAdapter);
        sendDataToServer.setOnClickListener(this);
        resetData.setOnClickListener(this);
        exportDataEmail.setOnClickListener(this);


        completedSurveys.setText("" + surveyCount);


        resetData.setBackgroundResource(android.R.drawable.btn_default);
        resetData.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);


        sendDataToServer.setBackgroundResource(android.R.drawable.btn_default);
        sendDataToServer.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);


//        Gson gson = new GsonBuilder()
//                .setExclusionStrategies(new ExclusionStrategy() {
//                    @Override
//                    public boolean shouldSkipField(FieldAttributes f) {
//                        return f.getDeclaringClass().equals(RealmObject.class);
//                    }
//
//                    @Override
//                    public boolean shouldSkipClass(Class<?> clazz) {
//                        return false;
//                    }
//                })
//                .registerTypeAdapter(Survey.class, new SurveySerializer())
//                .create();
//
//
//// Serialize a Realm object to a JSON string
//        String json = gson.toJson(realm.where(Survey.class).findFirst());
//        Log.e("SUR ",json.toString());

//        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//        try {
//            String json = ow.writeValueAsString(realm.where(Survey.class).findFirst());
//            Log.e("JSON ",json);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }


//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                realm.where(Survey.class).findFirst()
//
//                Gson gson = new Gson();
//                String json = gson.toJson(realm.where(Survey.class).findFirst());
//                Log.e("JSON ",json);
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                Log.d("REALM", "All done updating.");
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // transaction is automatically rolled-back, do any cleanup here
//                Log.e("REALM", "errrrrrrrrrrrrrrrrrrrrrroooooooooooooooooooooooooooooooooooorrrrrrrrrr "+error.getMessage());
//            }
//        });

        // new LongOperation().execute("");
        // open();

    }

    public void open() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        final EditText inputA = new EditText(SurveySummaryActivity.this);
        if (emailIdsStr.equals("")) {
            emailIdsStr = sharedPref.getString("emailIdsStr", "");
        }
        if (emailIdsStr.equals("")) {
            emailIdsStr="";
//            emailIdsStr = "yijia.chen@indufor-na.com, daphne.yin@indufor-na.com";
            // emailIdsStr = "riyas.sayone@gmail.com,issac.sayone@gmail.com";
        }
        inputA.setText(emailIdsStr);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        inputA.setLayoutParams(lp);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // alertDialogBuilder.setView(inflater.inflate(R.layout.send_emails,null));
        alertDialogBuilder.setView(inputA);


        alertDialogBuilder.setMessage("Enter email ids separated by commas");
        alertDialogBuilder.setPositiveButton("Send Data",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String value = inputA.getText().toString();

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("emailIdsStr", value);
                        editor.commit();

                        // alertDialogBuilder.setView(null);
                        exportDatas(value);
                        // Toast.makeText(SurveySummaryActivity.this,"You clicked yes  button",Toast.LENGTH_LONG).show();
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Toast.makeText(SurveySummaryActivity.this,"You clicked yes  button",Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void setButtonEnabled() {
        exportDataEmail.setEnabled(true);
        exportDataEmail.setBackgroundResource(android.R.drawable.btn_default);
        exportDataEmail.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        //  exportDataEmail.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
    }

    public void setButtonDisabled() {
        exportDataEmail.setEnabled(false);
        //  exportDataEmail.getBackground().setColorFilter(ContextCompat.getColor(this,R.color.colorDisable), PorterDuff.Mode.MULTIPLY);
        //exportDataEmail.setBackgroundColor(ContextCompat.getColor(this,R.color.colorDisable));
    }


    private class LongOperation extends AsyncTask<String, Void, String> {

        private ProgressDialog progress;

        @Override
        protected String doInBackground(String... params) {
            RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext()).deleteRealmIfMigrationNeeded().build();
            Realm.setDefaultConfiguration(config);
            Realm realm = Realm.getDefaultInstance();

            RealmResults<Survey> surveys = realm.where(Survey.class).findAll();
            for (String setsend : set) {
                for (Survey survey : surveys) {
                    Log.e("setsend:", setsend);
                    Log.e("surveyId:", survey.getSurveyId());
                    if (survey.getSurveyId().equals(setsend)) {
                        Log.e("AA ", "ok");
                        jsonObject = new JSONObject();
                        try {
                            if (survey.getId() == 0) {
                                jsonObject.put("id", "");
                            } else
                                jsonObject.put("id", survey.getId());
                            if (survey.getSurveyor() == null) {
                                jsonObject.put("surveyor", survey.getSurveyor());
                            } else
                                jsonObject.put("surveyor", survey.getSurveyor());
                            if (survey.getSurveyId() == null) {
                                jsonObject.put("surveyId", "");
                            } else
                                jsonObject.put("surveyId", survey.getSurveyId());
                            if (survey.getCommunity() == null) {
                                jsonObject.put("community", "");
                            } else
                                jsonObject.put("community", survey.getCommunity());
                            if (survey.getDistrict() == null) {
                                jsonObject.put("district", "");
                            } else
                                jsonObject.put("district", survey.getDistrict());
                            if (survey.getState() == null) {
                                jsonObject.put("state", "");
                            } else
                                jsonObject.put("state", survey.getState());
                            if (survey.getCountry() == null) {
                                jsonObject.put("country", "");
                            } else
                                jsonObject.put("country", survey.getCountry());
                            if (survey.getLanguage() == null) {
                                jsonObject.put("language", "");
                            } else
                                jsonObject.put("language", survey.getLanguage());
                            if (survey.getCurrency() == null) {
                                jsonObject.put("currency", "");
                            } else
                                jsonObject.put("currency", survey.getCurrency());
                            if (survey.getRespondentGroup() == null) {
                                jsonObject.put("respondentGroup", "");
                            } else
                                jsonObject.put("respondentGroup", survey.getRespondentGroup());
                            if (survey.getDateString() == null) {
                                jsonObject.put("date", "");
                            } else
                                jsonObject.put("date", survey.getDateString());
                            if (survey.getParticipants() == null) {
                                jsonObject.put("participants", "");
                            } else
                                jsonObject.put("participants", getParticipentArray(survey.getParticipants()));
                            if (survey.getLandKinds() == null) {
                                jsonObject.put("landKinds", "");
                            } else
                                jsonObject.put("landKinds", getLandKindArray(survey.getLandKinds()));
                            if (survey.getInflationRate() == 0) {
                                jsonObject.put("inflationRate", "");
                            } else
                                jsonObject.put("inflationRate", survey.getInflationRate());
                            if (survey.getComponents() == null) {
                                jsonObject.put("components", "");
                            } else
                                jsonObject.put("components", getComponent(survey.getComponents()));
                            if (survey.getParcelLocations() == null) {
                                jsonObject.put("gpsCoordinates", "");
                            } else
                                jsonObject.put("gpsCoordinates", getParcelLocations(survey.getParcelLocations()));
                            if (survey.getRiskRate() == 0) {
                                jsonObject.put("sovRate", "");
                            } else
                                jsonObject.put("sovRate", survey.getRiskRate());
                            if (survey.getSharedCashFlows() == null) {
                                jsonObject.put("sharedCashFlow", "");
                            } else
                                jsonObject.put("sharedCashFlow", getCashFlows(survey.getSharedCashFlows()));
                            if (survey.getSharedOutlays() == null) {
                                jsonObject.put("sharedOutlays", "");
                            } else
                                jsonObject.put("sharedOutlays", getOutLays(survey.getSharedOutlays()));
                            if (survey.getSharedCostElements() == null) {
                                jsonObject.put("sharedCostElements", "");
                            } else
                                jsonObject.put("sharedCostElements", getSharedCostElements(survey.getSharedCostElements()));
                            if (survey.getOverRideInflationRate() == 0) {
                                jsonObject.put("overrideSovRate", "");
                            } else
                                jsonObject.put("overrideSovRate", survey.getOverRideRiskRate());
                            if (survey.getOverRideInflationRate() == 0) {
                                jsonObject.put("overrideInflationRate", "");
                            } else
                                jsonObject.put("overrideInflationRate", survey.getOverRideInflationRate());
                            makeJsonObjectRequest(survey.getSurveyId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            return "Executed";

        }


        @Override
        protected void onPostExecute(String result) {
            //   makeJsonObjectRequest();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    progress.dismiss();
                    Toast toast = Toast.makeText(context, getResources().getText(R.string.completed_text), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }, 3000);

            surveyList = realm.where(Survey.class).findAll();
            surveyAdapter = new SurveyAdapter(surveyList, SurveySummaryActivity.this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(surveyAdapter);
            // surveyAdapter.notifyDataSetChanged();

        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(context);
            progress.setTitle(getResources().getString(R.string.sending_data));
            progress.setMessage(getResources().getString(R.string.wait_while_sending));
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {


        }
    }

    private JSONArray getSharedCostElements(RealmList<SharedCostElement> sharedCostElements) {
        JSONArray jsonCostElementArray = new JSONArray();
        for (SharedCostElement costelement : sharedCostElements) {
            JSONObject jsonObjectCostElement = new JSONObject();
            try {
                if (costelement.getId() == 0) {
                    jsonObjectCostElement.put("id", "");
                } else
                    jsonObjectCostElement.put("id", costelement.getId());
                if (costelement.getSurveyId() == null) {
                    jsonObjectCostElement.put("surveyId", "");
                } else
                    jsonObjectCostElement.put("surveyId", costelement.getSurveyId());
                if (costelement.getCostElementYearses() == null) {
                    jsonObjectCostElement.put("costElementYearses", "");
                } else
                    jsonObjectCostElement.put("costElementYearses", getSharedCostElementYears(costelement.getCostElementYearses()));
                if (costelement.getLandKind() == null) {
                    jsonObjectCostElement.put("landKind", "");
                } else
                    jsonObjectCostElement.put("landKind", costelement.getLandKind());
                if (costelement.getName() == null) {
                    jsonObjectCostElement.put("name", "");
                } else
                    jsonObjectCostElement.put("name", costelement.getName());
                if (costelement.getType() == null) {
                    jsonObjectCostElement.put("type", "");
                } else
                    jsonObjectCostElement.put("type", costelement.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonCostElementArray.put(jsonObjectCostElement);
        }
        return jsonCostElementArray;
    }

    private JSONArray getSharedCostElementYears(RealmList<SharedCostElementYears> costElementYearses) {
        JSONArray jsonCostElementYearsArray = new JSONArray();
        for (SharedCostElementYears costElementYears : costElementYearses) {
            JSONObject jsonObjectCostElementYears = new JSONObject();
            try {
                if (costElementYears.getSurveyId() == null) {
                    jsonObjectCostElementYears.put("surveyI1d", "");
                } else
                    jsonObjectCostElementYears.put("surveyI1d", costElementYears.getSurveyId());
                if (costElementYears.getLandKind() == null) {
                    jsonObjectCostElementYears.put("landKind", "");
                } else
                    jsonObjectCostElementYears.put("landKind", costElementYears.getLandKind());
                if (costElementYears.getId() == 0) {
                    jsonObjectCostElementYears.put("id", "");
                } else
                    jsonObjectCostElementYears.put("id", costElementYears.getId());
                if (costElementYears.getHouseholds() == 0) {
                    jsonObjectCostElementYears.put("households", "");
                } else
                    jsonObjectCostElementYears.put("households", costElementYears.getHouseholds());
                if (costElementYears.getCostFrequencyValue() == 0) {
                    jsonObjectCostElementYears.put("costFrequencyValue", "");
                } else
                    jsonObjectCostElementYears.put("costFrequencyValue", costElementYears.getCostFrequencyValue());
                if (costElementYears.getCostPerPeriodValue() == 0) {
                    jsonObjectCostElementYears.put("costPerPeriodValue", "");
                } else
                    jsonObjectCostElementYears.put("costPerPeriodValue", costElementYears.getCostPerPeriodValue());
                if (costElementYears.getCostPerUnitValue() == 0) {
                    jsonObjectCostElementYears.put("costPerUnitValue", "");
                } else
                    jsonObjectCostElementYears.put("costPerUnitValue", costElementYears.getCostPerUnitValue());
                if (costElementYears.getCostElementId() == 0) {
                    jsonObjectCostElementYears.put("costElementId", "");
                } else
                    jsonObjectCostElementYears.put("costElementId", costElementYears.getCostElementId());
                if (costElementYears.getCostFrequencyUnit() == 0) {
                    jsonObjectCostElementYears.put("costFrequencyUnit", 0);
                } else
                    jsonObjectCostElementYears.put("costFrequencyUnit", costElementYears.getCostFrequencyUnit());
                if (costElementYears.getCostPerPeriodUnit() == null) {
                    jsonObjectCostElementYears.put("costPerPeriodUni", "");
                } else
                    jsonObjectCostElementYears.put("costPerPeriodUni", costElementYears.getCostPerPeriodUnit());


                if (costElementYears.getProjectedIndex() == 0) {
                    if (costElementYears.getYear() == 0) {
                        jsonObjectCostElementYears.put("projectedIndex", "");
                    } else {
                        jsonObjectCostElementYears.put("projectedIndex", 0);
                    }
                } else {
                    jsonObjectCostElementYears.put("projectedIndex", costElementYears.getProjectedIndex());
                }


                if (costElementYears.getSubtotal() == 0) {
                    jsonObjectCostElementYears.put("subtotal", "");
                } else
                    jsonObjectCostElementYears.put("subtotal", costElementYears.getSubtotal());
                if (costElementYears.getYear() == 0) {
                    jsonObjectCostElementYears.put("year", "");
                } else
                    jsonObjectCostElementYears.put("year", costElementYears.getYear());
                if (costElementYears.getCostPerUnitUnit() == null) {
                    jsonObjectCostElementYears.put("costPerUnitUnit", "");
                } else
                    jsonObjectCostElementYears.put("costPerUnitUnit", costElementYears.getCostPerUnitUnit());


            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonCostElementYearsArray.put(jsonObjectCostElementYears);
        }
        return jsonCostElementYearsArray;
    }

    private JSONObject getParcelLocations(ParcelLocation parcelLocations) {

        JSONObject jsonObjectParcel = new JSONObject();
        try {
            if (parcelLocations.getId() == 0) {
                jsonObjectParcel.put("id", "");
            } else
                jsonObjectParcel.put("id", parcelLocations.getId());

            if (parcelLocations.getArea() == 0) {
                jsonObjectParcel.put("parcelArea", "");
            } else
                jsonObjectParcel.put("parcelArea", parcelLocations.getArea());

            if (parcelLocations.getCoordinateOne().equals("")) {
                jsonObjectParcel.put("coordinateOne", "");
            } else
                jsonObjectParcel.put("coordinateOne", parcelLocations.getCoordinateOne());

            if (parcelLocations.getCoordinateTwo().equals("")) {
                jsonObjectParcel.put("coordinateTwo", "");
            } else
                jsonObjectParcel.put("coordinateTwo", parcelLocations.getCoordinateTwo());

            if (parcelLocations.getCoordinateThree().equals("")) {
                jsonObjectParcel.put("coordinateThree", "");
            } else
                jsonObjectParcel.put("coordinateThree", parcelLocations.getCoordinateThree());

            if (parcelLocations.getCoordinateFour().equals("")) {
                jsonObjectParcel.put("coordinateFour", "");
            } else
                jsonObjectParcel.put("coordinateFour", parcelLocations.getCoordinateFour());

            if (parcelLocations.getCoordinateFive().equals("")) {
                jsonObjectParcel.put("coordinateFive", "");
            } else
                jsonObjectParcel.put("coordinateFive", parcelLocations.getCoordinateFive());

            if (parcelLocations.getCoordinateSix().equals("")) {
                jsonObjectParcel.put("coordinateSix", "");
            } else
                jsonObjectParcel.put("coordinateSix", parcelLocations.getCoordinateSix());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectParcel;
    }

    private void makeJsonObjectRequest(String surId) {

        Log.e("JSON ", jsonObject.toString());
//        JSONObject jsonObject1 = null;
//        JSONArray jsonArray = new JSONArray();
//
//        try {
//            jsonObject1 = new JSONObject(jsonObject.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        jsonArray.put(jsonObject1);


        JSONObject object = new JSONObject();
        try {
            object.put("survey_no", surId);
            object.put("content", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("JSON ", object.toString());

        RequestQueue queue = Volley.newRequestQueue(this);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "http://52.66.160.79/api/v1/create-quicksurvey/", object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("RES ", response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("TAG ", "Error: " + error.getMessage());

                Log.e("TAG ", error.networkResponse + "");

//                Toast.makeText(getApplicati   onContext(),
//                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                // hide the progress dialog
//                hidepDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token 2fb88b01c22ac470cbb969f604e9b3c87d6c8c7d");
                params.put("Content-Type", "application/json");

                return params;
            }
        };

        try {
            Log.e("Re ", jsonObjReq.getBody() + " " + jsonObjReq.getHeaders().get("Authorization").toString());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }


        int socketTimeout = 0;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);

//        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjReq);

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public JSONArray getParticipentArray(RealmList<Participant> participants) {
        JSONArray jsonArray = new JSONArray();
        for (Participant participant : participants) {
            JSONObject jsonObjectParticipant = new JSONObject();
            try {
                if (participant.getId() == 0) {
                    jsonObjectParticipant.put("id", "");
                } else
                    jsonObjectParticipant.put("id", participant.getId());
                if (participant.getSurveyId() == null) {
                    jsonObjectParticipant.put("surveyId", "");
                } else
                    jsonObjectParticipant.put("surveyId", participant.getSurveyId());
                if (participant.getName() == null) {
                    jsonObjectParticipant.put("name", "");
                } else
                    jsonObjectParticipant.put("name", participant.getName());
                if (participant.getOccupation() == null) {
                    jsonObjectParticipant.put("occupation", "");
                } else
                    jsonObjectParticipant.put("occupation", participant.getOccupation());
                if (participant.getGender() == null) {
                    jsonObjectParticipant.put("gender", "");
                } else
                    jsonObjectParticipant.put("gender", participant.getGender());
                if (participant.getYearsOfEdu() == 0) {
                    jsonObjectParticipant.put("yearsOfEdu", "");
                } else
                    jsonObjectParticipant.put("yearsOfEdu", participant.getYearsOfEdu());
                if (participant.getAge() == 0) {
                    jsonObjectParticipant.put("age", "");
                } else
                    jsonObjectParticipant.put("age", participant.getAge());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObjectParticipant);
        }
        return jsonArray;
    }

    public JSONArray getLandKindArray(RealmList<LandKind> landKinds) {
        JSONArray jsonArray = new JSONArray();
        for (LandKind landKind : landKinds) {
            JSONObject jsonObjectLandKind = new JSONObject();
            try {
                if (landKind.getId() == 0) {
                    jsonObjectLandKind.put("id", "");
                } else
                    jsonObjectLandKind.put("id", landKind.getId());
                if (landKind.getSurveyId() == null) {
                    jsonObjectLandKind.put("surveyId", "");
                } else
                    jsonObjectLandKind.put("surveyId", landKind.getSurveyId());
                if (landKind.getName() == null) {
                    jsonObjectLandKind.put("name", "");
                } else
                    jsonObjectLandKind.put("name", landKind.getName());
                if (landKind.getSocialCapitals() == null) {
                    jsonObjectLandKind.put("socialCapitals", "");
                } else
                    jsonObjectLandKind.put("socialCapitals", getSocialCapital(landKind.getSocialCapitals()));
                if (landKind.getForestLand() == null) {
                    jsonObjectLandKind.put("forestLand", "");
                } else
                    jsonObjectLandKind.put("forestLand", getForestLand(landKind.getForestLand()));
                if (landKind.getCropLand() == null) {
                    jsonObjectLandKind.put("cropLand", "");
                } else
                    jsonObjectLandKind.put("cropLand", getCropLand(landKind.getCropLand()));
                if (landKind.getPastureLand() == null) {
                    jsonObjectLandKind.put("pastureLand", "");
                } else
                    jsonObjectLandKind.put("pastureLand", getPastureLand(landKind.getPastureLand()));
                if (landKind.getMiningLand() == null) {
                    jsonObjectLandKind.put("miningLand", "");
                } else
                    jsonObjectLandKind.put("miningLand", getMiningLand(landKind.getMiningLand()));
                if (landKind.getStatus() == null) {
                    jsonObjectLandKind.put("status", "");
                } else
                    jsonObjectLandKind.put("status", landKind.getStatus());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObjectLandKind);
        }
        return jsonArray;
    }

    private JSONObject getMiningLand(MiningLand miningLand) {
        JSONObject jsonObjectMiningLand = new JSONObject();
        try {
            if (miningLand.getId() == 0) {
                jsonObjectMiningLand.put("id", "");
            } else
                jsonObjectMiningLand.put("id", miningLand.getId());
            if (miningLand.getSurveyId() == null) {
                jsonObjectMiningLand.put("surveyId", "");
            } else
                jsonObjectMiningLand.put("surveyId", miningLand.getSurveyId());
            if (miningLand.getDiscountingFactors() == null) {
                jsonObjectMiningLand.put("discountingFactors", "");
            } else
                jsonObjectMiningLand.put("discountingFactors", getDiscountingFactors(miningLand.getDiscountingFactors()));
            if (miningLand.getOutlays() == null) {
                jsonObjectMiningLand.put("outlays", "");
            } else
                jsonObjectMiningLand.put("outlays", getOutLays(miningLand.getOutlays()));
            if (miningLand.getDiscountPercentage() == 0) {
                jsonObjectMiningLand.put("discountPercentage", "");
            } else
                jsonObjectMiningLand.put("discountPercentage", miningLand.getDiscountPercentage());
            if (miningLand.getNetPresentValue() == 0) {
                jsonObjectMiningLand.put("netPresentValue", "");
            } else
                jsonObjectMiningLand.put("netPresentValue", miningLand.getNetPresentValue());
            if (miningLand.getCashFlows() == null) {
                jsonObjectMiningLand.put("cashFlows", "");
            } else
                jsonObjectMiningLand.put("cashFlows", getCashFlows(miningLand.getCashFlows()));
            if (miningLand.getCostElements() == null) {
                jsonObjectMiningLand.put("costElements", "");
            } else
                jsonObjectMiningLand.put("costElements", getCostElement(miningLand.getCostElements()));
            if (miningLand.getRevenueProducts() == null) {
                jsonObjectMiningLand.put("revenueProducts", "");
            } else
                jsonObjectMiningLand.put("revenueProducts", getRevenueProducts(miningLand.getRevenueProducts()));
            if (miningLand.getDiscountedCashFlows() == null) {
                jsonObjectMiningLand.put("discountedCashFlows", "");
            } else
                jsonObjectMiningLand.put("discountedCashFlows", getDiscountedCashFlows(miningLand.getDiscountedCashFlows()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectMiningLand;
    }

    private JSONObject getPastureLand(PastureLand pastureLand) {

        JSONObject jsonObjectPastureLand = new JSONObject();
        try {
            if (pastureLand.getId() == 0) {
                jsonObjectPastureLand.put("id", "");
            } else
                jsonObjectPastureLand.put("id", pastureLand.getId());
            if (pastureLand.getSurveyId() == null) {
                jsonObjectPastureLand.put("surveyId", "");
            } else
                jsonObjectPastureLand.put("surveyId", pastureLand.getSurveyId());
            if (pastureLand.getDiscountingFactors() == null) {
                jsonObjectPastureLand.put("discountingFactors", "");
            } else
                jsonObjectPastureLand.put("discountingFactors", getDiscountingFactors(pastureLand.getDiscountingFactors()));
            if (pastureLand.getOutlays() == null) {
                jsonObjectPastureLand.put("outlays", "");
            } else
                jsonObjectPastureLand.put("outlays", getOutLays(pastureLand.getOutlays()));
            if (pastureLand.getDiscountPercentage() == 0) {
                jsonObjectPastureLand.put("discountPercentage", "");
            } else
                jsonObjectPastureLand.put("discountPercentage", pastureLand.getDiscountPercentage());
            if (pastureLand.getNetPresentValue() == 0) {
                jsonObjectPastureLand.put("netPresentValue", "");
            } else
                jsonObjectPastureLand.put("netPresentValue", pastureLand.getNetPresentValue());
            if (pastureLand.getCashFlows() == null) {
                jsonObjectPastureLand.put("cashFlows", "");
            } else
                jsonObjectPastureLand.put("cashFlows", getCashFlows(pastureLand.getCashFlows()));
            if (pastureLand.getCostElements() == null) {
                jsonObjectPastureLand.put("costElements", "");
            } else
                jsonObjectPastureLand.put("costElements", getCostElement(pastureLand.getCostElements()));
            if (pastureLand.getRevenueProducts() == null) {
                jsonObjectPastureLand.put("revenueProducts", "");
            } else
                jsonObjectPastureLand.put("revenueProducts", getRevenueProducts(pastureLand.getRevenueProducts()));
            if (pastureLand.getDiscountedCashFlows() == null) {
                jsonObjectPastureLand.put("discountedCashFlows", "");
            } else
                jsonObjectPastureLand.put("discountedCashFlows", getDiscountedCashFlows(pastureLand.getDiscountedCashFlows()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectPastureLand;
    }

    private JSONObject getCropLand(CropLand cropLand) {
        JSONObject jsonObjectCropLand = new JSONObject();
        try {
            if (cropLand.getId() == 0) {
                jsonObjectCropLand.put("id", "");
            } else
                jsonObjectCropLand.put("id", cropLand.getId());
            if (cropLand.getSurveyId() == null) {
                jsonObjectCropLand.put("surveyId", "");
            } else
                jsonObjectCropLand.put("surveyId", cropLand.getSurveyId());
            if (cropLand.getNetPresentValue() == 0) {
                jsonObjectCropLand.put("netPresentValue", "");
            } else
                jsonObjectCropLand.put("netPresentValue", cropLand.getNetPresentValue());
            if (cropLand.getCashFlows() == null) {
                jsonObjectCropLand.put("cashFlows", "");
            } else
                jsonObjectCropLand.put("cashFlows", getCashFlows(cropLand.getCashFlows()));
            if (cropLand.getCostElements() == null) {
                jsonObjectCropLand.put("costElements", "");
            } else
                jsonObjectCropLand.put("costElements", getCostElement(cropLand.getCostElements()));
            if (cropLand.getDiscountedCashFlows() == null) {
                jsonObjectCropLand.put("discountedCashFlows", "");
            } else
                jsonObjectCropLand.put("discountedCashFlows", getDiscountedCashFlows(cropLand.getDiscountedCashFlows()));
            if (cropLand.getDiscountPercentage() == 0) {
                jsonObjectCropLand.put("discountPercentage", "");
            } else
                jsonObjectCropLand.put("discountPercentage", cropLand.getDiscountPercentage());
            if (cropLand.getOutlays() == null) {
                jsonObjectCropLand.put("outlays", "");
            } else
                jsonObjectCropLand.put("outlays", getOutLays(cropLand.getOutlays()));
            if (cropLand.getRevenueProducts() == null) {
                jsonObjectCropLand.put("revenueProducts", "");
            } else
                jsonObjectCropLand.put("revenueProducts", getRevenueProducts(cropLand.getRevenueProducts()));
            if (cropLand.getDiscountingFactors() == null) {
                jsonObjectCropLand.put("discountingFactors", "");
            } else
                jsonObjectCropLand.put("discountingFactors", getDiscountingFactors(cropLand.getDiscountingFactors()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectCropLand;
    }

    private JSONObject getForestLand(ForestLand forestLand) {
        JSONObject jsonObjectForestLand = new JSONObject();
        try {
            if (forestLand.getId() == 0) {
                jsonObjectForestLand.put("id", "");
            } else
                jsonObjectForestLand.put("id", forestLand.getId());
            if (forestLand.getNetPresentValue() == 0) {
                jsonObjectForestLand.put("netPresentValue", "");
            } else
                jsonObjectForestLand.put("netPresentValue", forestLand.getNetPresentValue());
            if (forestLand.getCashFlows() == null) {
                jsonObjectForestLand.put("cashFlows", "");
            } else
                jsonObjectForestLand.put("cashFlows", getCashFlows(forestLand.getCashFlows()));
            if (forestLand.getCostElements() == null) {
                jsonObjectForestLand.put("costElements", "");
            } else
                jsonObjectForestLand.put("costElements", getCostElement(forestLand.getCostElements()));
            if (forestLand.getDiscountedCashFlows() == null) {
                jsonObjectForestLand.put("discountedCashFlows", "");
            } else
                jsonObjectForestLand.put("discountedCashFlows", getDiscountedCashFlows(forestLand.getDiscountedCashFlows()));
            if (forestLand.getDiscountingFactors() == null) {
                jsonObjectForestLand.put("discountingFactors", "");
            } else
                jsonObjectForestLand.put("discountingFactors", getDiscountingFactors(forestLand.getDiscountingFactors()));
            if (forestLand.getDiscountPercentage() == 0) {
                jsonObjectForestLand.put("discountPercentage", "");
            } else
                jsonObjectForestLand.put("discountPercentage", forestLand.getDiscountPercentage());
            if (forestLand.getOutlays() == null) {
                jsonObjectForestLand.put("outlays", "");
            } else
                jsonObjectForestLand.put("outlays", getOutLays(forestLand.getOutlays()));
            if (forestLand.getRevenueProducts() == null) {
                jsonObjectForestLand.put("revenueProducts", "");
            } else
                jsonObjectForestLand.put("revenueProducts", getRevenueProducts(forestLand.getRevenueProducts()));
            if (forestLand.getSurveyId() == null) {
                jsonObjectForestLand.put("surveyId", "");
            } else
                jsonObjectForestLand.put("surveyId", forestLand.getSurveyId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectForestLand;
    }

    private JSONArray getRevenueProducts(RealmList<RevenueProduct> revenueProducts) {
        JSONArray jsonRevenueProductsArray = new JSONArray();
        for (RevenueProduct revenueProduct : revenueProducts) {
            JSONObject jsonObjectRevenueProducts = new JSONObject();
            try {
                if (revenueProduct.getId() == 0) {
                    jsonObjectRevenueProducts.put("id", "");
                } else
                    jsonObjectRevenueProducts.put("id", revenueProduct.getId());
                if (revenueProduct.getSurveyId() == null) {
                    jsonObjectRevenueProducts.put("surveyId", "");
                } else
                    jsonObjectRevenueProducts.put("surveyId", revenueProduct.getSurveyId());
                if (revenueProduct.getType() == null) {
                    jsonObjectRevenueProducts.put("type", "");
                } else
                    jsonObjectRevenueProducts.put("type", revenueProduct.getType());
                if (revenueProduct.getLandKind() == null) {
                    jsonObjectRevenueProducts.put("landKind", "");
                } else
                    jsonObjectRevenueProducts.put("landKind", revenueProduct.getLandKind());
                if (revenueProduct.getName() == null) {
                    jsonObjectRevenueProducts.put("name", "");
                } else
                    jsonObjectRevenueProducts.put("name", revenueProduct.getName());
                if (revenueProduct.getRevenueProductYearses() == null) {
                    jsonObjectRevenueProducts.put("revenueProductYearses", "");
                } else
                    jsonObjectRevenueProducts.put("revenueProductYearses", getRevenueProductYear(revenueProduct.getRevenueProductYearses()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonRevenueProductsArray.put(jsonObjectRevenueProducts);
        }
        return jsonRevenueProductsArray;
    }

    private JSONArray getRevenueProductYear(RealmList<RevenueProductYears> revenueProductYearses) {

        JSONArray jsonRevenueProductYearArray = new JSONArray();
        for (RevenueProductYears revenueProductYear : revenueProductYearses) {
            JSONObject jsonObjectRevenueProductYear = new JSONObject();
            try {
                if (revenueProductYear.getId() == 0) {
                    jsonObjectRevenueProductYear.put("id", "");
                } else
                    jsonObjectRevenueProductYear.put("id", revenueProductYear.getId());
                if (revenueProductYear.getSurveyId() == null) {
                    jsonObjectRevenueProductYear.put("surveyId", "");
                } else
                    jsonObjectRevenueProductYear.put("surveyId", revenueProductYear.getSurveyId());
                if (revenueProductYear.getLandKind() == null) {
                    jsonObjectRevenueProductYear.put("landKind", "");
                } else
                    jsonObjectRevenueProductYear.put("landKind", revenueProductYear.getLandKind());
                if (revenueProductYear.getQuantityValue() == 0) {
                    jsonObjectRevenueProductYear.put("quantityValue", "");
                } else
                    jsonObjectRevenueProductYear.put("quantityValue", revenueProductYear.getQuantityValue());
                if (revenueProductYear.getHarvestFrequencyValue() == 0) {
                    jsonObjectRevenueProductYear.put("harvestFrequencyValue", "");
                } else
                    jsonObjectRevenueProductYear.put("harvestFrequencyValue", revenueProductYear.getHarvestFrequencyValue());
                if (revenueProductYear.getMarketPriceValue() == 0) {
                    jsonObjectRevenueProductYear.put("marketPriceValue", "");
                } else
                    jsonObjectRevenueProductYear.put("marketPriceValue", revenueProductYear.getMarketPriceValue());

                if (revenueProductYear.getHarvestFrequencyUnit() == 0) {
                    jsonObjectRevenueProductYear.put("harvestFrequencyUnit", 0);
                } else {
                    jsonObjectRevenueProductYear.put("harvestFrequencyUnit", revenueProductYear.getHarvestFrequencyUnit());
                }
                if (revenueProductYear.getHouseholds() == 0) {
                    jsonObjectRevenueProductYear.put("households", "");
                } else
                    jsonObjectRevenueProductYear.put("households", revenueProductYear.getHouseholds());
                if (revenueProductYear.getMarketPriceCurrency() == null) {
                    jsonObjectRevenueProductYear.put("marketPriceCurrency", "");
                } else
                    jsonObjectRevenueProductYear.put("marketPriceCurrency", revenueProductYear.getMarketPriceCurrency());

                if (revenueProductYear.getProjectedIndex() == 0) {
                    if (revenueProductYear.getYear() == 0) {
                        jsonObjectRevenueProductYear.put("projectedIndex", "");
                    } else {
                        jsonObjectRevenueProductYear.put("projectedIndex", 0);
                    }
                } else {
                    jsonObjectRevenueProductYear.put("projectedIndex", revenueProductYear.getProjectedIndex());
                }

                if (revenueProductYear.getQuantityUnit() == null) {
                    jsonObjectRevenueProductYear.put("quantityUnit", "");
                } else
                    jsonObjectRevenueProductYear.put("quantityUnit", revenueProductYear.getQuantityUnit());
                if (revenueProductYear.getRevenueProductId() == 0) {
                    jsonObjectRevenueProductYear.put("revenueProductId", "");
                } else
                    jsonObjectRevenueProductYear.put("revenueProductId", revenueProductYear.getRevenueProductId());
                if (revenueProductYear.getSubtotal() == 0) {
                    jsonObjectRevenueProductYear.put("subtotal", "");
                } else
                    jsonObjectRevenueProductYear.put("subtotal", revenueProductYear.getSubtotal());
                if (revenueProductYear.getYear() == 0) {
                    jsonObjectRevenueProductYear.put("year", "");
                } else {
                    jsonObjectRevenueProductYear.put("year", revenueProductYear.getYear());
                }
                if (revenueProductYear.getHarvestArea() == 0) {
                    jsonObjectRevenueProductYear.put("harvestArea", "");
                } else {
                    jsonObjectRevenueProductYear.put("harvestArea", revenueProductYear.getHarvestArea());
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonRevenueProductYearArray.put(jsonObjectRevenueProductYear);
        }
        return jsonRevenueProductYearArray;
    }

    private JSONArray getOutLays(RealmList<Outlay> outlays) {

        JSONArray jsonOutLaysArray = new JSONArray();
        for (Outlay outlay : outlays) {
            JSONObject jsonObjectOutLays = new JSONObject();
            try {
                if (outlay.getId() == 0) {
                    jsonObjectOutLays.put("id", "");
                } else {
                    jsonObjectOutLays.put("id", outlay.getId());
                }
                if (outlay.getSurveyId() == null) {
                    jsonObjectOutLays.put("surveyId", "");
                } else {
                    jsonObjectOutLays.put("surveyId", outlay.getSurveyId());
                }
                if (outlay.getSurveyId() == null) {
                    jsonObjectOutLays.put("itemName", "");
                } else {
                    jsonObjectOutLays.put("itemName", outlay.getItemName());
                }
                if (outlay.getOutlayYearses() == null) {
                    jsonObjectOutLays.put("outlayYears", "");
                } else {
                    jsonObjectOutLays.put("outlayYears", getOutlayYears(outlay.getOutlayYearses()));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonOutLaysArray.put(jsonObjectOutLays);
        }
        return jsonOutLaysArray;
    }

    private JSONArray getOutlayYears(RealmList<OutlayYears> outlayYearses) {

        JSONArray jsonOutlayYearsArray = new JSONArray();
        for (OutlayYears outlayYears : outlayYearses) {
            JSONObject jsonObjectOutlayYears = new JSONObject();
            try {
                if (outlayYears.getId() == 0) {
                    jsonObjectOutlayYears.put("id", "");
                } else {
                    jsonObjectOutlayYears.put("id", outlayYears.getId());
                }
                if (outlayYears.getYear() == 0) {
                    jsonObjectOutlayYears.put("year", "");
                } else {
                    jsonObjectOutlayYears.put("year", outlayYears.getYear());
                }
                if (outlayYears.getPrice() == 0) {
                    jsonObjectOutlayYears.put("price", 0);
                } else {
                    jsonObjectOutlayYears.put("price", outlayYears.getPrice());
                }
                Log.e("GGG ", outlayYears.toString());
                if (outlayYears.getUnit() == null) {
                    jsonObjectOutlayYears.put("unit", "");
                } else {
                    jsonObjectOutlayYears.put("unit", outlayYears.getUnit());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonOutlayYearsArray.put(jsonObjectOutlayYears);
        }
        return jsonOutlayYearsArray;
    }

    private JSONArray getDiscountingFactors(RealmList<DiscountingFactor> discountingFactors) {

        JSONArray jsonDiscountingFactorsArray = new JSONArray();
        for (DiscountingFactor discountingFactor : discountingFactors) {
            JSONObject jsonObjectDiscountingFactor = new JSONObject();
            try {
                if (discountingFactor.getId() == 0) {
                    jsonObjectDiscountingFactor.put("id", "");
                } else
                    jsonObjectDiscountingFactor.put("id", discountingFactor.getId());
                if (discountingFactor.getSurveyId() == null) {
                    jsonObjectDiscountingFactor.put("surveyId", "");
                } else
                    jsonObjectDiscountingFactor.put("surveyId", discountingFactor.getSurveyId());
                if (discountingFactor.getValue() == 0) {
                    jsonObjectDiscountingFactor.put("value", "");
                } else
                    jsonObjectDiscountingFactor.put("value", discountingFactor.getValue());
                if (discountingFactor.getYear() == 0) {
                    jsonObjectDiscountingFactor.put("year", "");
                } else
                    jsonObjectDiscountingFactor.put("year", discountingFactor.getYear());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonDiscountingFactorsArray.put(jsonObjectDiscountingFactor);
        }
        return jsonDiscountingFactorsArray;
    }

    private JSONArray getDiscountedCashFlows(RealmList<DiscountedCashFlow> discountedCashFlows) {
        JSONArray jsonDiscountedCashFlowArray = new JSONArray();
        for (DiscountedCashFlow discountedCashFlow : discountedCashFlows) {
            JSONObject jsonObjectDiscountedCashFlow = new JSONObject();
            try {
                if (discountedCashFlow.getId() == 0) {
                    jsonObjectDiscountedCashFlow.put("id", "");
                } else
                    jsonObjectDiscountedCashFlow.put("id", discountedCashFlow.getId());
                if (discountedCashFlow.getSurveyId() == null) {
                    jsonObjectDiscountedCashFlow.put("surveyId", "");
                } else
                    jsonObjectDiscountedCashFlow.put("surveyId", discountedCashFlow.getSurveyId());
                if (discountedCashFlow.getYear() == 0) {
                    jsonObjectDiscountedCashFlow.put("year", "");
                } else
                    jsonObjectDiscountedCashFlow.put("year", discountedCashFlow.getYear());
                if (discountedCashFlow.getValue() == 0) {
                    jsonObjectDiscountedCashFlow.put("value", "");
                } else
                    jsonObjectDiscountedCashFlow.put("value", discountedCashFlow.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonDiscountedCashFlowArray.put(jsonObjectDiscountedCashFlow);
        }
        return jsonDiscountedCashFlowArray;
    }

    private JSONArray getCostElement(RealmList<CostElement> costElements) {

        JSONArray jsonCostElementArray = new JSONArray();
        for (CostElement costelement : costElements) {
            JSONObject jsonObjectCostElement = new JSONObject();
            try {
                if (costelement.getId() == 0) {
                    jsonObjectCostElement.put("id", "");
                } else
                    jsonObjectCostElement.put("id", costelement.getId());
                if (costelement.getSurveyId() == null) {
                    jsonObjectCostElement.put("surveyId", "");
                } else
                    jsonObjectCostElement.put("surveyId", costelement.getSurveyId());
                if (costelement.getCostElementYearses() == null) {
                    jsonObjectCostElement.put("costElementYearses", "");
                } else
                    jsonObjectCostElement.put("costElementYearses", getCostElementYears(costelement.getCostElementYearses()));
                if (costelement.getLandKind() == null) {
                    jsonObjectCostElement.put("landKind", "");
                } else
                    jsonObjectCostElement.put("landKind", costelement.getLandKind());
                if (costelement.getName() == null) {
                    jsonObjectCostElement.put("name", "");
                } else
                    jsonObjectCostElement.put("name", costelement.getName());
                if (costelement.getType() == null) {
                    jsonObjectCostElement.put("type", "");
                } else
                    jsonObjectCostElement.put("type", costelement.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonCostElementArray.put(jsonObjectCostElement);
        }
        return jsonCostElementArray;
    }

    private JSONArray getCostElementYears(RealmList<CostElementYears> costElementYearses) {

        JSONArray jsonCostElementYearsArray = new JSONArray();
        for (CostElementYears costElementYears : costElementYearses) {
            JSONObject jsonObjectCostElementYears = new JSONObject();
            try {
                if (costElementYears.getSurveyId() == null) {
                    jsonObjectCostElementYears.put("surveyI1d", "");
                } else
                    jsonObjectCostElementYears.put("surveyI1d", costElementYears.getSurveyId());
                if (costElementYears.getLandKind() == null) {
                    jsonObjectCostElementYears.put("landKind", "");
                } else
                    jsonObjectCostElementYears.put("landKind", costElementYears.getLandKind());
                if (costElementYears.getId() == 0) {
                    jsonObjectCostElementYears.put("id", "");
                } else
                    jsonObjectCostElementYears.put("id", costElementYears.getId());
                if (costElementYears.getCostFrequencyValue() == 0) {
                    jsonObjectCostElementYears.put("costFrequencyValue", "");
                } else
                    jsonObjectCostElementYears.put("costFrequencyValue", costElementYears.getCostFrequencyValue());
                if (costElementYears.getCostPerPeriodValue() == 0) {
                    jsonObjectCostElementYears.put("costPerPeriodValue", "");
                } else
                    jsonObjectCostElementYears.put("costPerPeriodValue", costElementYears.getCostPerPeriodValue());
                if (costElementYears.getCostPerUnitValue() == 0) {
                    jsonObjectCostElementYears.put("costPerUnitValue", "");
                } else
                    jsonObjectCostElementYears.put("costPerUnitValue", costElementYears.getCostPerUnitValue());
                if (costElementYears.getCostElementId() == 0) {
                    jsonObjectCostElementYears.put("costElementId", "");
                } else
                    jsonObjectCostElementYears.put("costElementId", costElementYears.getCostElementId());
                if (costElementYears.getCostFrequencyUnit() == 0) {
                    jsonObjectCostElementYears.put("costFrequencyUnit", 0);
                } else
                    jsonObjectCostElementYears.put("costFrequencyUnit", costElementYears.getCostFrequencyUnit());
                if (costElementYears.getCostPerPeriodUnit() == null) {
                    jsonObjectCostElementYears.put("costPerPeriodUni", "");
                } else
                    jsonObjectCostElementYears.put("costPerPeriodUni", costElementYears.getCostPerPeriodUnit());
                if (costElementYears.getHouseholds() == 0) {
                    jsonObjectCostElementYears.put("households", "");
                } else
                    jsonObjectCostElementYears.put("households", costElementYears.getHouseholds());

                if (costElementYears.getProjectedIndex() == 0) {
                    if (costElementYears.getYear() == 0) {
                        jsonObjectCostElementYears.put("projectedIndex", "");
                    } else {
                        jsonObjectCostElementYears.put("projectedIndex", 0);
                    }
                } else {
                    jsonObjectCostElementYears.put("projectedIndex", costElementYears.getProjectedIndex());
                }


                if (costElementYears.getSubtotal() == 0) {
                    jsonObjectCostElementYears.put("subtotal", "");
                } else
                    jsonObjectCostElementYears.put("subtotal", costElementYears.getSubtotal());
                if (costElementYears.getYear() == 0) {
                    jsonObjectCostElementYears.put("year", "");
                } else
                    jsonObjectCostElementYears.put("year", costElementYears.getYear());
                if (costElementYears.getCostPerUnitUnit() == null) {
                    jsonObjectCostElementYears.put("costPerUnitUnit", "");
                } else
                    jsonObjectCostElementYears.put("costPerUnitUnit", costElementYears.getCostPerUnitUnit());


            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonCostElementYearsArray.put(jsonObjectCostElementYears);
        }
        return jsonCostElementYearsArray;
    }

    private JSONArray getCashFlows(RealmList<CashFlow> cashFlows) {
        JSONArray jsonCashFlowArray = new JSONArray();
        for (CashFlow cashFlow : cashFlows) {
            JSONObject jsonObjectCashFlow = new JSONObject();
            try {
                if (cashFlow.getSurveyId() == null) {
                    jsonObjectCashFlow.put("surveyId", "");
                } else
                    jsonObjectCashFlow.put("surveyId", cashFlow.getSurveyId());
                if (cashFlow.getValue() == 0) {
                    jsonObjectCashFlow.put("value", "");
                } else
                    jsonObjectCashFlow.put("value", cashFlow.getValue());
                if (cashFlow.getDiscountedCashFlow() == 0) {
                    jsonObjectCashFlow.put("discountedCashFlow", "");
                } else
                    jsonObjectCashFlow.put("discountedCashFlow", cashFlow.getDiscountedCashFlow());
                if (cashFlow.getDiscountingFactor() == 0) {
                    jsonObjectCashFlow.put("discountingFactor", "");
                } else
                    jsonObjectCashFlow.put("discountingFactor", cashFlow.getDiscountingFactor());
                if (cashFlow.getYear() == 0) {
                    jsonObjectCashFlow.put("year", "");
                } else
                    jsonObjectCashFlow.put("year", cashFlow.getYear());
                if (cashFlow.getId() == 0) {
                    jsonObjectCashFlow.put("id", "");
                } else {
                    jsonObjectCashFlow.put("id", cashFlow.getId());
                }

                if (cashFlow.getTotalRevenue() == 0) {
                    jsonObjectCashFlow.put("totalRevenue", 0);
                } else {
                    jsonObjectCashFlow.put("totalRevenue", cashFlow.getTotalRevenue());
                }
                if (cashFlow.getTotalCost() == 0) {
                    jsonObjectCashFlow.put("totalCost", 0);
                } else {
                    jsonObjectCashFlow.put("totalCost", cashFlow.getTotalCost());
                }
                if (cashFlow.getTotalOutlay() == 0) {
                    jsonObjectCashFlow.put("totalOutlay", 0);
                } else {
                    jsonObjectCashFlow.put("totalOutlay", cashFlow.getTotalOutlay());
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonCashFlowArray.put(jsonObjectCashFlow);

        }

        return jsonCashFlowArray;
    }

    public JSONObject getSocialCapital(SocialCapital socialCapitals) {
        JSONObject jsonObjectSocialCapital = new JSONObject();
        try {
            if (socialCapitals.getId() == 0) {
                jsonObjectSocialCapital.put("id", "");
            } else
                jsonObjectSocialCapital.put("id", socialCapitals.getId());
            if (socialCapitals.getSurveyId() == null) {
                jsonObjectSocialCapital.put("surveyId", "");
            } else
                jsonObjectSocialCapital.put("surveyId", socialCapitals.getSurveyId());
            if (socialCapitals.getScore() == 0) {
                jsonObjectSocialCapital.put("score", 0);
            } else
                jsonObjectSocialCapital.put("score", socialCapitals.getScore());
            if (socialCapitals.getRating() == null) {
                jsonObjectSocialCapital.put("rating", "");
            } else
                jsonObjectSocialCapital.put("rating", socialCapitals.getRating());
            if (socialCapitals.getSovereign() == 0) {
                jsonObjectSocialCapital.put("sovereign", "");
            } else
                jsonObjectSocialCapital.put("sovereign", socialCapitals.getSovereign());
            if (socialCapitals.getDiscountRate() == 0) {
                jsonObjectSocialCapital.put("discountRate", 0);
            } else
                jsonObjectSocialCapital.put("discountRate", socialCapitals.getDiscountRate());
//            if (socialCapitals.getDiscountRateOverride() == 0) {
//                jsonObjectSocialCapital.put("discountRateOverride", 0);
//            } else
            jsonObjectSocialCapital.put("discountRateOverride", socialCapitals.getDiscountRateOverride());
            if (socialCapitals.getSpread() == 0) {
                jsonObjectSocialCapital.put("spread", 0);
            } else
                jsonObjectSocialCapital.put("spread", socialCapitals.getSpread());
            if (socialCapitals.getSocialCapitalAnswers() == null) {
                jsonObjectSocialCapital.put("socialCapitalAnswers", "");
            } else
                jsonObjectSocialCapital.put("socialCapitalAnswers", getSocialCapitalAnswerArray(socialCapitals.getSocialCapitalAnswers()));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObjectSocialCapital;

    }

    public JSONArray getSocialCapitalAnswerArray(RealmList<SocialCapitalAnswer> socialCapitalAnswers) {
        JSONArray jsonSocialCapitalAnswerArray = new JSONArray();
        for (SocialCapitalAnswer socialCapitalAnswer : socialCapitalAnswers) {
            JSONObject jsonObjectSocialCapitalAnswer = new JSONObject();
            try {
                if (socialCapitalAnswer.getId() == 0) {
                    jsonObjectSocialCapitalAnswer.put("id", "");
                } else
                    jsonObjectSocialCapitalAnswer.put("id", socialCapitalAnswer.getId());
                if (socialCapitalAnswer.getSurveyId() == null) {
                    jsonObjectSocialCapitalAnswer.put("surveyId", "");
                } else
                    jsonObjectSocialCapitalAnswer.put("surveyId", socialCapitalAnswer.getSurveyId());
                if (socialCapitalAnswer.getFactorScore() == 0) {
                    jsonObjectSocialCapitalAnswer.put("factorScore", 0);
                } else
                    jsonObjectSocialCapitalAnswer.put("factorScore", socialCapitalAnswer.getFactorScore());


                if (socialCapitalAnswer.getSocialCapitalQuestion() == null) {
                    jsonObjectSocialCapitalAnswer.put("socialCapitalQuestion", "");
                } else {
                    jsonObjectSocialCapitalAnswer.put("socialCapitalQuestion", getSocialCapitalQuestions(socialCapitalAnswer.getSocialCapitalQuestion()));
                }


                if (socialCapitalAnswer.getMultipleAnswers() == null) {
                    jsonObjectSocialCapitalAnswer.put("multipleAnswers", "");
                } else
                    jsonObjectSocialCapitalAnswer.put("multipleAnswers", getMultipleAnswers(socialCapitalAnswer.getMultipleAnswers()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonSocialCapitalAnswerArray.put(jsonObjectSocialCapitalAnswer);
        }
        return jsonSocialCapitalAnswerArray;

    }

    private JSONArray getMultipleAnswers(RealmList<MultipleAnswer> multipleAnswers) {

        JSONArray jsonMultipleAnswerArray = new JSONArray();
        for (MultipleAnswer multipleAnswer : multipleAnswers) {
            JSONObject jsonObjectMultipleAnwser = new JSONObject();
            try {
                if (multipleAnswer.getId() == 0) {
                    jsonObjectMultipleAnwser.put("id", "");
                } else {
                    jsonObjectMultipleAnwser.put("id", multipleAnswer.getId());
                }
                if (multipleAnswer.getAnswer() == 0) {
                    jsonObjectMultipleAnwser.put("answer", "");
                } else {
                    jsonObjectMultipleAnwser.put("answer", multipleAnswer.getAnswer());
                }
                if (multipleAnswer.getAnswerValue() == 0) {
                    jsonObjectMultipleAnwser.put("answerValue", 0);
                } else {
                    jsonObjectMultipleAnwser.put("answerValue", multipleAnswer.getAnswerValue());
                }
                if (multipleAnswer.getQuestionNo() == 0) {
                    jsonObjectMultipleAnwser.put("questionNo", "");
                } else {
                    jsonObjectMultipleAnwser.put("questionNo", multipleAnswer.getQuestionNo());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonMultipleAnswerArray.put(jsonObjectMultipleAnwser);
        }
        return jsonMultipleAnswerArray;
    }

    private JSONObject getSocialCapitalQuestions(SocialCapitalQuestions socialCapitalQuestion) {
        JSONObject jsonObjectSocialCapitalQuestion = new JSONObject();
        try {
            if (socialCapitalQuestion.getId() == 0) {
                jsonObjectSocialCapitalQuestion.put("id", "");
            } else
                jsonObjectSocialCapitalQuestion.put("id", socialCapitalQuestion.getId());
            if (socialCapitalQuestion.getOptionType() == null) {
                jsonObjectSocialCapitalQuestion.put("optionType", "");
            } else
                jsonObjectSocialCapitalQuestion.put("optionType", socialCapitalQuestion.getOptionType());
            if (socialCapitalQuestion.getQuestion() == null) {
                jsonObjectSocialCapitalQuestion.put("question", "");
            } else
                jsonObjectSocialCapitalQuestion.put("question", socialCapitalQuestion.getQuestion());
            if (socialCapitalQuestion.getQuestionHindi() == null) {
                jsonObjectSocialCapitalQuestion.put("questionHindi", "");
            } else
                jsonObjectSocialCapitalQuestion.put("questionHindi", socialCapitalQuestion.getQuestionHindi());
            if (socialCapitalQuestion.getSocialCapitalAnswerOptionses() == null) {
                jsonObjectSocialCapitalQuestion.put("socialCapitalAnswerOptionses", "");
            } else
                jsonObjectSocialCapitalQuestion.put("socialCapitalAnswerOptionses", getSocialCapitalAnswerOptions(socialCapitalQuestion.getSocialCapitalAnswerOptionses()));
            if (socialCapitalQuestion.getWeight() == 0) {
                jsonObjectSocialCapitalQuestion.put("weight", "");
            } else
                jsonObjectSocialCapitalQuestion.put("weight", socialCapitalQuestion.getWeight());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectSocialCapitalQuestion;

    }

    private JSONArray getSocialCapitalAnswerOptions(RealmList<SocialCapitalAnswerOptions> socialCapitalAnswerOptionses) {

        JSONArray jsonSocialCapitalAnswerOptionsArray = new JSONArray();
        for (SocialCapitalAnswerOptions socialCapitalAnswerOptions : socialCapitalAnswerOptionses) {
            JSONObject jsonObjectSocialCapitalAnswerOptions = new JSONObject();
            try {
                if (socialCapitalAnswerOptions.getId() == 0) {
                    jsonObjectSocialCapitalAnswerOptions.put("id", "");
                } else
                    jsonObjectSocialCapitalAnswerOptions.put("id", socialCapitalAnswerOptions.getId());
                if (socialCapitalAnswerOptions.getOptions() == null) {
                    jsonObjectSocialCapitalAnswerOptions.put("options", "");
                } else
                    jsonObjectSocialCapitalAnswerOptions.put("options", socialCapitalAnswerOptions.getOptions());
                if (socialCapitalAnswerOptions.getOptionsHindi() == null) {
                    jsonObjectSocialCapitalAnswerOptions.put("optionsHindi", "");
                } else
                    jsonObjectSocialCapitalAnswerOptions.put("optionsHindi", socialCapitalAnswerOptions.getOptionsHindi());
                if (socialCapitalAnswerOptions.getVal() == 0) {
                    jsonObjectSocialCapitalAnswerOptions.put("val", "");
                } else
                    jsonObjectSocialCapitalAnswerOptions.put("val", socialCapitalAnswerOptions.getVal());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonSocialCapitalAnswerOptionsArray.put(jsonObjectSocialCapitalAnswerOptions);
        }
        return jsonSocialCapitalAnswerOptionsArray;
    }

    public JSONObject getComponent(Component component) {
        JSONObject jsonObjectComponent = new JSONObject();
        try {
            if (component.getId() == 0) {
                jsonObjectComponent.put("id", "");
            } else
                jsonObjectComponent.put("id", component.getId());
            if (component.getCroplandValue() == 0) {
                jsonObjectComponent.put("croplandValue", "");
            } else
                jsonObjectComponent.put("croplandValue", component.getCroplandValue());
            if (component.getCroplandCompleteness() == 0) {
                jsonObjectComponent.put("croplandCompleteness", "");
            } else
                jsonObjectComponent.put("croplandCompleteness", component.getCroplandCompleteness());
            if (component.getCroplandSocialCapitalScore() == 0) {
                jsonObjectComponent.put("croplandSocialCapitalScore", "");
            } else
                jsonObjectComponent.put("croplandSocialCapitalScore", component.getCroplandSocialCapitalScore());
            if (component.getForestValue() == 0) {
                jsonObjectComponent.put("forestValue", "");
            } else
                jsonObjectComponent.put("forestValue", component.getForestValue());
            if (component.getForestCompleteness() == 0) {
                jsonObjectComponent.put("forestCompleteness", "");
            } else
                jsonObjectComponent.put("forestCompleteness", component.getForestCompleteness());
            if (component.getForestSocialCapitalScore() == 0) {
                jsonObjectComponent.put("forestSocialCapitalScore", "");
            } else
                jsonObjectComponent.put("forestSocialCapitalScore", component.getForestSocialCapitalScore());
            if (component.getPastureValue() == 0) {
                jsonObjectComponent.put("pastureValue", "");
            } else
                jsonObjectComponent.put("pastureValue", component.getPastureValue());
            if (component.getPastureCompleteness() == 0) {
                jsonObjectComponent.put("pastureCompleteness", "");
            } else
                jsonObjectComponent.put("pastureCompleteness", component.getPastureCompleteness());
            if (component.getPastureSocialCapitalScore() == 0) {
                jsonObjectComponent.put("pastureSocialCapitalScore", "");
            } else
                jsonObjectComponent.put("pastureSocialCapitalScore", component.getPastureSocialCapitalScore());
            if (component.getMiningLandValue() == 0) {
                jsonObjectComponent.put("miningLandValue", "");
            } else
                jsonObjectComponent.put("miningLandValue", component.getMiningLandValue());
            if (component.getMiningLandCompleteness() == 0) {
                jsonObjectComponent.put("miningLandCompleteness", "");
            } else
                jsonObjectComponent.put("miningLandCompleteness", component.getMiningLandCompleteness());
            if (component.getMiningSocialCapitalScore() == 0) {
                jsonObjectComponent.put("miningSocialCapitalScore", "");
            } else
                jsonObjectComponent.put("miningSocialCapitalScore", component.getMiningSocialCapitalScore());
            if (component.getTotalValue() == 0) {
                jsonObjectComponent.put("totalValue", "");
            } else
                jsonObjectComponent.put("totalValue", component.getTotalValue());
            if (component.getTotalSocialCapitalScore() == 0) {
                jsonObjectComponent.put("totalSocialCapitalScore", "");
            } else {
                jsonObjectComponent.put("totalSocialCapitalScore", component.getTotalSocialCapitalScore());
            }
            if (component.getTotalValueStr().equals("")) {
                jsonObjectComponent.put("totalValueStr", "");
            } else {
                jsonObjectComponent.put("totalValueStr", component.getTotalValueStr());
            }
            if (component.getTotalValuePerHa().equals("")) {
                jsonObjectComponent.put("totalValuePerHaStr", "");
            } else {
                jsonObjectComponent.put("totalValuePerHaStr", component.getTotalValuePerHa());
            }
            if (component.getSharedCostValue() == 0) {
                jsonObjectComponent.put("sharedCostValue", "");
            } else {
                jsonObjectComponent.put("sharedCostValue", component.getSharedCostValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectComponent;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_send_data_to_server:
                set = sharedPref.getStringSet("surveySet", null);
                if (set != null) {
                    if (!set.isEmpty()) {
                        for (String temp : set) {
                            Log.e("Sirvey : ", temp);
                            realm.beginTransaction();
                            Survey survey = realm.where(Survey.class).equalTo("surveyId", temp).findFirst();
                            survey.setSendStatus(true);
                            exportDataEmail.setVisibility(View.VISIBLE);
                            realm.commitTransaction();
                            new LongOperation().execute("");
                        }
                    }
                } else {
                    Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
                    for (String temp : set) {
                        Log.e("Sirvey : ", temp);
                        realm.beginTransaction();
                        Survey survey = realm.where(Survey.class).equalTo("surveyId", temp).findFirst();
                        survey.setSendStatus(true);
                        realm.commitTransaction();
                    }
                }

                break;

            case R.id.button_export_data_email:
                open();

//                exportDataEmail.setEnabled(false);
//                RealmResults<Survey> surveyExports = realm.where(Survey.class).equalTo("sendStatus", true).findAll();
//                ArrayList<String> strings = new ArrayList<>();
//                JSONArray jsonArray = new JSONArray();
//                JSONArray jsonArrayEmails = new JSONArray();
//                jsonArrayEmails.put("riyas.sayone@gmail.com");
//                jsonArrayEmails.put("issac.sayone@gmail.com");
//                for(Survey surveyExport : surveyExports){
//                    jsonArray.put(surveyExport.getSurveyId());
//                    strings.add(surveyExport.getSurveyId());
//                }
//
//                JSONObject object = new JSONObject();
//                try {
//                    object.put("id", jsonArray);
//                    object.put("email",jsonArrayEmails);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//                ApiInterface apiService =
//                        ApiClient.getClient().create(ApiInterface.class);
//
//                DataWithId dataWithId = new DataWithId(strings);
//
//                Log.e("Res ",dataWithId.toString());
//
//                Call<ExportData> call = apiService.getExported(dataWithId);
//                Log.e("URL ", call.request().url()+"");
//                Log.e("URL ", call.request().body()+"");
//                call.enqueue(new Callback<ExportData>() {
//
//                    @Override
//                    public void onResponse(Call<ExportData> call, retrofit2.Response<ExportData> response) {
//                        Log.e("Response ",""+response.toString());
//                        exportDataEmail.setEnabled(true);
//                        Toast toast = Toast.makeText(context,"Data exported", Toast.LENGTH_SHORT);
//                        toast.show();
//                    }
//
//                    @Override
//                    public void onFailure(Call<ExportData> call, Throwable t) {
//                        Log.e("Response ",""+t.toString());
//                        Toast toast = Toast.makeText(context,getResources().getString(R.string.save_failed), Toast.LENGTH_SHORT);
//                        toast.show();
//                        exportDataEmail.setEnabled(true);
//                    }
//                });


//                Log.e("JSON ", object.toString());
//
//                RequestQueue queue = Volley.newRequestQueue(this);
//
//
//                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                        "http://52.66.160.79/api/v1/generate-mini-excel/", object, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.e("RES ", response.toString());
//                        exportDataEmail.setEnabled(true);
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        VolleyLog.e("TAG ", "Error: " + error.getMessage());
//                        Toast toast = Toast.makeText(context,getResources().getString(R.string.save_failed), Toast.LENGTH_SHORT);
//                        toast.show();
//                        exportDataEmail.setEnabled(true);
//                    }
//                }) {
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("Authorization", "Token 2fb88b01c22ac470cbb969f604e9b3c87d6c8c7d");
//                        params.put("Content-Type", "application/json");
//
//                        return params;
//                    }
//                };
//
////                try {
////                    Log.e("Re ", jsonObjReq.getBody() + " " + jsonObjReq.getHeaders().get("Authorization").toString());
////                } catch (AuthFailureError authFailureError) {
////                    authFailureError.printStackTrace();
////                }
//
//                queue.add(jsonObjReq);
//                Toast toast = Toast.makeText(context,getResources().getString(R.string.completed_text), Toast.LENGTH_SHORT);
//                toast.show();


                break;
            case R.id.button_reset_data:
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                RealmResults<CashFlow> cashFlow = realm.where(CashFlow.class).findAll();
                cashFlow.deleteAllFromRealm();
                RealmResults<Component> components = realm.where(Component.class).findAll();
                components.deleteAllFromRealm();
                RealmResults<CostElement> costElements = realm.where(CostElement.class).findAll();
                costElements.deleteAllFromRealm();
                RealmResults<CostElementYears> costElementYearses = realm.where(CostElementYears.class).findAll();
                costElementYearses.deleteAllFromRealm();
                RealmResults<CropLand> cropLands = realm.where(CropLand.class).findAll();
                cropLands.deleteAllFromRealm();
                RealmResults<DiscountedCashFlow> discountedCashFlows = realm.where(DiscountedCashFlow.class).findAll();
                discountedCashFlows.deleteAllFromRealm();
                RealmResults<DiscountingFactor> discountingFactors = realm.where(DiscountingFactor.class).findAll();
                discountingFactors.deleteAllFromRealm();
                RealmResults<ForestLand> forestLands = realm.where(ForestLand.class).findAll();
                forestLands.deleteAllFromRealm();
                RealmResults<LandKind> landKinds = realm.where(LandKind.class).findAll();
                landKinds.deleteAllFromRealm();
                RealmResults<MiningLand> miningLands = realm.where(MiningLand.class).findAll();
                miningLands.deleteAllFromRealm();
                RealmResults<MultipleAnswer> multipleAnswers = realm.where(MultipleAnswer.class).findAll();
                multipleAnswers.deleteAllFromRealm();
                RealmResults<Outlay> outlays = realm.where(Outlay.class).findAll();
                outlays.deleteAllFromRealm();
                RealmResults<Participant> participants = realm.where(Participant.class).findAll();
                participants.deleteAllFromRealm();
                RealmResults<PastureLand> pastureLands = realm.where(PastureLand.class).findAll();
                pastureLands.deleteAllFromRealm();
                RealmResults<RevenueProduct> revenueProducts = realm.where(RevenueProduct.class).findAll();
                revenueProducts.deleteAllFromRealm();
                RealmResults<RevenueProductYears> revenueProductYearses = realm.where(RevenueProductYears.class).findAll();
                revenueProductYearses.deleteAllFromRealm();
                RealmResults<SocialCapital> socialCapitals = realm.where(SocialCapital.class).findAll();
                socialCapitals.deleteAllFromRealm();
                RealmResults<SocialCapitalAnswer> socialCapitalAnswers = realm.where(SocialCapitalAnswer.class).findAll();
                socialCapitalAnswers.deleteAllFromRealm();
                RealmResults<Survey> surveys = realm.where(Survey.class).findAll();
                surveys.deleteAllFromRealm();
                realm.commitTransaction();
                surveyAdapter.notifyDataSetChanged();
                surveyCount = 0;
                completedSurveys.setText("" + surveyCount);
                exportDataEmail.setEnabled(false);
                exportDataEmail.setBackgroundResource(android.R.drawable.btn_default);
                exportDataEmail.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorDisable), PorterDuff.Mode.MULTIPLY);
                break;
        }
    }

    public void exportDatas(String aa) {
        // ArrayList<String> elephantList = (ArrayList<String>) Arrays.asList(aa.split(","));
        ArrayList<String> elephantList = new ArrayList<String>(Arrays.asList(aa.split(",")));

        exportDataEmail.setEnabled(false);
        RealmResults<Survey> surveyExports = realm.where(Survey.class).equalTo("sendStatus", true).findAll();
        ArrayList<String> strings = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayEmails = new JSONArray();
        for (String a : elephantList) {
            jsonArrayEmails.put(a);
        }

        Log.e("EMl ", jsonArrayEmails.toString());

        // jsonArrayEmails.put("issac.sayone@gmail.com");
        for (Survey surveyExport : surveyExports) {
            jsonArray.put(surveyExport.getSurveyId());
            strings.add(surveyExport.getSurveyId());
        }

        JSONObject object = new JSONObject();
        try {
            object.put("id", jsonArray);
            object.put("email", jsonArrayEmails);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        DataWithId dataWithId = new DataWithId(strings, elephantList);

        Log.e("Res ", dataWithId.toString());

        Call<ExportData> call = apiService.getExported(dataWithId);
        Log.e("URL ", call.request().url() + "");
        Log.e("URL ", call.request().body() + "");
        call.enqueue(new Callback<ExportData>() {

            @Override
            public void onResponse(Call<ExportData> call, retrofit2.Response<ExportData> response) {
                Log.e("Response ", "" + response.toString());
                exportDataEmail.setEnabled(true);
                Toast toast = Toast.makeText(context, "Data exported", Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onFailure(Call<ExportData> call, Throwable t) {
                Log.e("Response ", "" + t.toString());
                Toast toast = Toast.makeText(context, getResources().getString(R.string.save_failed), Toast.LENGTH_SHORT);
                toast.show();
                exportDataEmail.setEnabled(true);
            }
        });
    }
}