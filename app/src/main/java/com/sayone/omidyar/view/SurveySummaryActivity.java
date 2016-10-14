package com.sayone.omidyar.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.adapter.ParticipantsAdapter;
import com.sayone.omidyar.adapter.SurveyAdapter;
import com.sayone.omidyar.model.Component;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.Participant;
import com.sayone.omidyar.model.SocialCapitalQuestions;
import com.sayone.omidyar.model.Survey;
import com.sayone.omidyar.serializer.SurveySerializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class SurveySummaryActivity extends BaseActivity implements View.OnClickListener {

    TextView completedSurveys;
    CheckBox checkBoxSurvey;
    RecyclerView recyclerView;
    RealmList<Survey> surveys;
    private SurveyAdapter surveyAdapter;
    int surveyCount;

    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_summary);


        // checkBoxSurvey1=(CheckBox)findViewById(R.id.checkBox_survey1);
        // checkBoxSurvey2=(CheckBox)findViewById(R.id.checkBox_survey2);
        completedSurveys = (TextView) findViewById(R.id.completed_surveys);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_survey_list);


        Realm realm = Realm.getDefaultInstance();
        RealmResults<Survey> surveyList = realm.where(Survey.class).findAll();
        surveyCount = surveyList.size();

        surveyAdapter = new SurveyAdapter(surveyList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(surveyAdapter);

        completedSurveys.setText("" + surveyCount);


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
        new LongOperation().execute("");

    }


    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext()).deleteRealmIfMigrationNeeded().build();
            Realm.setDefaultConfiguration(config);
            Realm realm = Realm.getDefaultInstance();

            Survey survey = realm.where(Survey.class).findFirst();


            jsonObject = new JSONObject();
            try {
                jsonObject.put("id", survey.getId());
                jsonObject.put("surveyor", survey.getSurveyor());
                jsonObject.put("surveyId", survey.getSurveyId());
                jsonObject.put("community", survey.getCommunity());
                jsonObject.put("district", survey.getDistrict());
                jsonObject.put("state", survey.getState());
                jsonObject.put("country", survey.getCountry());
                jsonObject.put("language", survey.getLanguage());

//                private String community;
//
//                private String district;
//
//                private String state;
//
//                private String country;
//
//                private String language;
//
//                private String currency;
//
//                private String respondentGroup;
//
//                private Date date;
//
//                private RealmList<Participant> participants;
//
//                private RealmList<LandKind> landKinds;
//
//                private String inflationRate;
//
//                private Component components;
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //realm.where(Survey.class).findFirst();

//            Gson gson = new Gson();
//            String json = gson.toJson(realm.where(SocialCapitalQuestions.class).findFirst());
//            Log.e("JSON ",json);
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            makeJsonObjectRequest();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    private void makeJsonObjectRequest() {

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

        RequestQueue queue = Volley.newRequestQueue(this);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "http://52.66.160.79/api/v1/create-quicksurvey/", jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("RES ", response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("TAG ", "Error: " + error.getMessage());
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
            Log.e("Re ",jsonObjReq.getBody()+" "+jsonObjReq.getHeaders().get("Authorization").toString() );
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }

        queue.add(jsonObjReq);

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
