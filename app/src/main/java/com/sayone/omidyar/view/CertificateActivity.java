package com.sayone.omidyar.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.SocialCapital;
import com.sayone.omidyar.model.SocialCapitalQuestions;
import com.sayone.omidyar.model.Survey;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;

import io.realm.Realm;

public class CertificateActivity extends BaseActivity {

    TextView parcelId,community,site,surveyorName,valuationDate,inflationRate,socialCapitalForest,socialCapitalCrop,socialCapitalPasture,socialCapitalMining;
    SharedPreferences sharedPref;
    private Realm realm;
    private String surveyId;
    Context context;
    ImageView mapImage;

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
        socialCapitalForest=(TextView)findViewById(R.id.forest_social_capital_score);
        socialCapitalCrop=(TextView)findViewById(R.id.crop_social_capital_score);
        socialCapitalPasture=(TextView)findViewById(R.id.pasture_social_capital_score);
        socialCapitalMining=(TextView)findViewById(R.id.minimg_social_capital_score);
        mapImage=(ImageView)findViewById(R.id.map_image);

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

        LandKind landKindLoad = realm.where(LandKind.class)
                .equalTo("surveyId", surveyId)
                .equalTo("status", "active")
                .findFirst();
        SocialCapital socialCapital = landKindLoad.getSocialCapitals();

        String path = Environment.getExternalStorageDirectory().toString() +"/MapImagesNew/"+"screen.jpg/";
        mapImage.setVisibility(View.VISIBLE);
        File f = new File(path);
        Picasso.with(context).load(f).into(mapImage);


        community.setText(surveyCheck.getCommunity().toString());
        parcelId.setText(surveyCheck.getSurveyId().toString());
        surveyorName.setText(surveyCheck.getSurveyor().toString());
        valuationDate.setText(s);
        socialCapitalForest.setText(""+socialCapital.getScore());
        socialCapitalCrop.setText("0");
        socialCapitalPasture.setText("0");
        socialCapitalMining.setText("0");
      //  inflationRate.setText(surveyCheck.getInflationRate().toString());

    }
}
