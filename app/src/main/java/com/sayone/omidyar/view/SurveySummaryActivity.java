package com.sayone.omidyar.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.adapter.ParticipantsAdapter;
import com.sayone.omidyar.adapter.SurveyAdapter;
import com.sayone.omidyar.model.Participant;
import com.sayone.omidyar.model.Survey;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class SurveySummaryActivity extends BaseActivity implements View.OnClickListener{

    TextView completedSurveys;
    CheckBox checkBoxSurvey;
    RecyclerView recyclerView;
    RealmList<Survey> surveys;
    private SurveyAdapter surveyAdapter;
    int surveyCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_summary);


       // checkBoxSurvey1=(CheckBox)findViewById(R.id.checkBox_survey1);
       // checkBoxSurvey2=(CheckBox)findViewById(R.id.checkBox_survey2);
        completedSurveys=(TextView)findViewById(R.id.completed_surveys);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_survey_list);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Survey> surveyList = realm.where(Survey.class).findAll();
        surveyCount=surveyList.size();

        surveyAdapter = new SurveyAdapter(surveyList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(surveyAdapter);

        completedSurveys.setText(""+surveyCount);


    }
}
