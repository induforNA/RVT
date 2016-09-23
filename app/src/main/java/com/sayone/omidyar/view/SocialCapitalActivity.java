package com.sayone.omidyar.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.SocialCapitalAnswerOptions;
import com.sayone.omidyar.model.SocialCapitalQuestions;
import com.sayone.omidyar.model.Survey;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by sayone on 21/9/16.
 */

public class SocialCapitalActivity extends BaseActivity implements View.OnClickListener, RealmChangeListener{

    public static final String TAG = SocialCapitalActivity.class.getName();
    private Realm realm;
    private SharedPreferences preferences;
    Context context;

    String currentSocialCapitalServey;
    String serveyId;

    private Button backButton;
    private Button nextButton;

    private LinearLayout checkboxgroup;
    private RadioGroup radiobuttongroup;

    private TextView question;

    private CheckBox optionA;
    private CheckBox optionB;
    private CheckBox optionC;
    private CheckBox optionD;
    private CheckBox optionE;

    private RadioButton optionARadio;
    private RadioButton optionBRadio;
    private RadioButton optionCRadio;
    private RadioButton optionDRadio;
    private RadioButton optionERadio;

    private int currentQuestionId = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_capital);

        Log.e("Current Activity ", "4454");

        context = this;
        realm = Realm.getDefaultInstance();
        preferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = preferences.getString("surveyId","");
        currentSocialCapitalServey = preferences.getString("currentSocialCapitalServey","");

        checkboxgroup = (LinearLayout) findViewById(R.id.checkboxgroup);
        radiobuttongroup = (RadioGroup) findViewById(R.id.radiobuttongroup);

        question = (TextView) findViewById(R.id.question);

        optionA = (CheckBox) findViewById(R.id.option_a);
        optionB = (CheckBox) findViewById(R.id.option_b);
        optionC = (CheckBox) findViewById(R.id.option_c);
        optionD = (CheckBox) findViewById(R.id.option_d);
        optionE = (CheckBox) findViewById(R.id.option_e);

        optionARadio = (RadioButton) findViewById(R.id.option_a_radio);
        optionBRadio = (RadioButton) findViewById(R.id.option_b_radio);
        optionCRadio = (RadioButton) findViewById(R.id.option_c_radio);
        optionDRadio = (RadioButton) findViewById(R.id.option_d_radio);
        optionERadio = (RadioButton) findViewById(R.id.option_e_radio);

        final RealmResults<SocialCapitalQuestions> socialCapitalQuestionses = realm.where(SocialCapitalQuestions.class).findAll();
        for(SocialCapitalQuestions socialCapitalQuestions:socialCapitalQuestionses){
            Log.e("Social capital",socialCapitalQuestions.getId()+" "+socialCapitalQuestions.getQuestion());
        }

        final RealmResults<Survey> surveys = realm.where(Survey.class).findAll();
        for(SocialCapitalQuestions socialCapitalQuestions:socialCapitalQuestionses){
            Log.e("Servey",socialCapitalQuestions.getQuestion());

        }

        final RealmResults<LandKind> landKinds = realm.where(LandKind.class)
                .equalTo("surveyId",serveyId)
                .equalTo("status","active")
                .findAll();
        for(LandKind landKind:landKinds){
            if(landKind.getName().equals(currentSocialCapitalServey)){
                Log.e("LandKind", landKind.getSocialCapitals().getSocialCapitalAnswers().size()+"");

                if(landKind.getSocialCapitals().getSocialCapitalAnswers().size() == 0){
                    currentQuestionId = 6;
                    SocialCapitalQuestions SocialCapitalQuestions = socialCapitalQuestionses.get(currentQuestionId);
                    setQuestionView(SocialCapitalQuestions.getOptionType(),SocialCapitalQuestions.getQuestion(),SocialCapitalQuestions.getSocialCapitalAnswerOptionses());
                }
            }
        }





//        question.setText(socialCapitalQuestionses.get(0).getQuestion());
//        optionA.setText(socialCapitalQuestionses.get(0).getSocialCapitalAnswerOptionses().get(0).getOptions());
//        optionB.setText(socialCapitalQuestionses.get(0).getSocialCapitalAnswerOptionses().get(1).getOptions());
//        optionC.setText(socialCapitalQuestionses.get(0).getSocialCapitalAnswerOptionses().get(2).getOptions());
//        optionD.setText(socialCapitalQuestionses.get(0).getSocialCapitalAnswerOptionses().get(3).getOptions());
//        optionE.setText(socialCapitalQuestionses.get(0).getSocialCapitalAnswerOptionses().get(4).getOptions());

        backButton = (Button) findViewById(R.id.back_button);
        nextButton = (Button) findViewById(R.id.next_button);

        backButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.next_button:
//                Intent intent = new Intent(SocialCapitalActivity.this, SocialCapitalActivity.class);
//                startActivity(intent);
                break;
        }
    }

    // type multiple/single
    public void setQuestionView(String type, String questionval, RealmList<SocialCapitalAnswerOptions> options){
        question.setText(questionval);
        if(type.equals("multiple")) {
            checkboxgroup.setVisibility(View.VISIBLE);
            radiobuttongroup.setVisibility(View.GONE);
            if (options.get(0) == null) {
                optionA.setVisibility(View.GONE);
            } else {
                optionA.setVisibility(View.VISIBLE);
                optionA.setText(options.get(0).getOptions());
            }
            if (options.get(1) == null) {
                optionB.setVisibility(View.GONE);
            } else {
                optionB.setVisibility(View.VISIBLE);
                optionB.setText(options.get(1).getOptions());
            }
            if (options.get(2) == null) {
                optionC.setVisibility(View.GONE);
            } else {
                optionC.setVisibility(View.VISIBLE);
                optionC.setText(options.get(2).getOptions());
            }
            if (options.get(3) == null) {
                optionD.setVisibility(View.GONE);
            } else {
                optionD.setVisibility(View.VISIBLE);
                optionD.setText(options.get(3).getOptions());
            }
            if (options.get(4) == null) {
                optionE.setVisibility(View.GONE);
            } else {
                optionE.setVisibility(View.VISIBLE);
                optionE.setText(options.get(4).getOptions());
            }
        }else{
            radiobuttongroup.setVisibility(View.VISIBLE);
            checkboxgroup.setVisibility(View.GONE);
            if (options.get(0) == null) {
                optionARadio.setVisibility(View.GONE);
            } else {
                optionARadio.setVisibility(View.VISIBLE);
                optionARadio.setText(options.get(0).getOptions());
            }
            if (options.get(1) == null) {
                optionBRadio.setVisibility(View.GONE);
            } else {
                optionBRadio.setVisibility(View.VISIBLE);
                optionBRadio.setText(options.get(1).getOptions());
            }
            if (options.get(2) == null) {
                optionCRadio.setVisibility(View.GONE);
            } else {
                optionCRadio.setVisibility(View.VISIBLE);
                optionCRadio.setText(options.get(2).getOptions());
            }
            if (options.get(3) == null) {
                optionDRadio.setVisibility(View.GONE);
            } else {
                optionDRadio.setVisibility(View.VISIBLE);
                optionDRadio.setText(options.get(3).getOptions());
            }
            if (options.get(4) == null) {
                optionERadio.setVisibility(View.GONE);
            } else {
                optionERadio.setVisibility(View.VISIBLE);
                optionERadio.setText(options.get(4).getOptions());
            }
        }
//        optionA.setText(socialCapitalQuestionses.get(0).getSocialCapitalAnswerOptionses().get(0).getOptions());
//        optionB.setText(socialCapitalQuestionses.get(0).getSocialCapitalAnswerOptionses().get(1).getOptions());
//        optionC.setText(socialCapitalQuestionses.get(0).getSocialCapitalAnswerOptionses().get(2).getOptions());
//        optionD.setText(socialCapitalQuestionses.get(0).getSocialCapitalAnswerOptionses().get(3).getOptions());
//        optionE.setText(socialCapitalQuestionses.get(0).getSocialCapitalAnswerOptionses().get(4).getOptions());
    }

    @Override
    public void onChange(Object element) {

    }

    public void slectedLandKind(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.forestland:

                break;
            case R.id.cropland:

                break;
            case R.id.pastureland:

                break;
            case R.id.miningland:
//                if (checked) {
//                    landTypeNames.add("Mining Land");
//                }else{
//                    landTypeNames.add("Pastureland");
//                }
                break;
        }
    }
}