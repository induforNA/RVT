package com.sayone.omidyar.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.Survey;

import java.text.Format;
import java.text.SimpleDateFormat;

import io.realm.Realm;

public class CertificateActivity extends BaseActivity {

    TextView parcelId,community,site,surveyorName,valuationDate,inflationRate;
    SharedPreferences sharedPref;
    private Realm realm;
    private String surveyId;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);

        parcelId=(TextView)findViewById(R.id.parcel_id);
        community=(TextView)findViewById(R.id.community_name);
        site=(TextView)findViewById(R.id.site_name);
        surveyorName=(TextView)findViewById(R.id.surveyor_name);
        valuationDate=(TextView)findViewById(R.id.valuation_date);
        inflationRate=(TextView)findViewById(R.id.inflation_rate);

        context=this;

        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        surveyId = sharedPref.getString("surveyId","");
        Realm realm = Realm.getDefaultInstance();
        Survey surveyCheck = realm.where(Survey.class)
                .equalTo("surveyId",surveyId)
                .findFirst();

        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        String s = formatter.format(surveyCheck.getDate());

        community.setText(surveyCheck.getCommunity().toString());
        parcelId.setText(surveyCheck.getSurveyId().toString());
        surveyorName.setText(surveyCheck.getSurveyor().toString());
        valuationDate.setText(s);
      //  inflationRate.setText(surveyCheck.getInflationRate().toString());

    }
}
