package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.model.LandKind;
import com.sayone.omidyar.model.MultipleAnswer;
import com.sayone.omidyar.model.SocialCapitalAnswer;
import com.sayone.omidyar.model.SocialCapitalAnswerOptions;
import com.sayone.omidyar.model.SocialCapitalQuestions;
import com.sayone.omidyar.model.Survey;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by sayone on 21/9/16.
 */

public class SocialCapitalActivity1 extends BaseActivity {

    public static final String TAG = SocialCapitalActivity1.class.getName();
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

    private DrawerLayout menuDrawerLayout;
    private ImageView imageViewMenuIcon;
    private ImageView drawerCloseBtn;
    private TextView textViewAbout;
    private TextView surveyIdDrawer;
    private TextView logout;
    private TextView startSurvey;

    private int currentQuestionId = 0;

    RealmResults<SocialCapitalQuestions> socialCapitalQuestionses;
    SocialCapitalQuestions socialCapitalQuestionsSelectedItem;
    RealmList<SocialCapitalAnswerOptions> socialCapitalAnswerOptionsesList;

    LandKind landKind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_capital);

        Log.e("Current Activity ", "4454");

        context = this;
        realm = Realm.getDefaultInstance();
        preferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        serveyId = preferences.getString("surveyId", "");
        currentSocialCapitalServey = preferences.getString("currentSocialCapitalServey", "");

        socialCapitalAnswerOptionsesList = new RealmList<>();

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

        menuDrawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        imageViewMenuIcon = (ImageView) findViewById(R.id.image_view_menu_icon);
        drawerCloseBtn = (ImageView) findViewById(R.id.drawer_close_btn);
        textViewAbout = (TextView) findViewById(R.id.text_view_about);
        surveyIdDrawer=(TextView)findViewById(R.id.text_view_id);
        logout = (TextView) findViewById(R.id.logout);
        startSurvey=(TextView)findViewById(R.id.text_start_survey);

//        socialCapitalQuestionses = realm.where(SocialCapitalQuestions.class).findAll();
//        for (SocialCapitalQuestions socialCapitalQuestions : socialCapitalQuestionses) {
//            Log.e("Social capital", socialCapitalQuestions.getId() + " " + socialCapitalQuestions.getQuestion());
//        }

//        final RealmResults<Survey> surveys = realm.where(Survey.class).findAll();
//        for (SocialCapitalQuestions socialCapitalQuestions : socialCapitalQuestionses) {
//            Log.e("Servey", socialCapitalQuestions.getQuestion());
//
//        }

        landKind = realm.where(LandKind.class)
                .equalTo("surveyId", serveyId)
                .equalTo("status", "active")
                .equalTo("name", currentSocialCapitalServey)
                .findFirst();

        Log.e("LandKind", landKind.getSocialCapitals().getSocialCapitalAnswers().get(0).getSocialCapitalQuestion() + "");
//        for(SocialCapitalAnswer socialCapitalAnswer:landKind.getSocialCapitals().getSocialCapitalAnswers()){
//            SocialCapitalQuestions socialCapitalQuestionsLandKind = socialCapitalQuestionses.get(currentQuestionId);
//            setQuestionView(socialCapitalQuestionsLandKind.getOptionType(), socialCapitalQuestionsLandKind.getQuestion(), socialCapitalQuestionsLandKind.getSocialCapitalAnswerOptionses());
//        }

        currentQuestionId = 0;
        SocialCapitalQuestions socialCapitalQuestionsLandKind = landKind.getSocialCapitals().getSocialCapitalAnswers().get(currentQuestionId).getSocialCapitalQuestion();
        setQuestionView(socialCapitalQuestionsLandKind.getOptionType(),
                socialCapitalQuestionsLandKind.getQuestion(),
                socialCapitalQuestionsLandKind.getSocialCapitalAnswerOptionses(),
                landKind.getSocialCapitals().getSocialCapitalAnswers().get(currentQuestionId).getMultipleAnswers());


        Log.e("MULTIPLE ",landKind.getSocialCapitals().getSocialCapitalAnswers().get(currentQuestionId).getMultipleAnswers().toString());



//        if (landKind.getSocialCapitals().getSocialCapitalAnswers().size() == 0) {
//            currentQuestionId = 0;
//            SocialCapitalQuestions socialCapitalQuestionsLandKind = socialCapitalQuestionses.get(currentQuestionId);
//            setQuestionView(socialCapitalQuestionsLandKind.getOptionType(), socialCapitalQuestionsLandKind.getQuestion(), socialCapitalQuestionsLandKind.getSocialCapitalAnswerOptionses());
//        }


        backButton = (Button) findViewById(R.id.back_button);
        nextButton = (Button) findViewById(R.id.next_button);

        surveyIdDrawer.setText(serveyId);

        imageViewMenuIcon.setOnClickListener(this);
        surveyIdDrawer.setText(serveyId);
        drawerCloseBtn.setOnClickListener(this);
        textViewAbout.setOnClickListener(this);
        logout.setOnClickListener(this);
        backButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        startSurvey.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                if (currentQuestionId > 0) {
                    int nextQuestionId = currentQuestionId - 1;
                    Log.e("Current id ", nextQuestionId + "");
                    SocialCapitalQuestions socialCapitalQuestionsLandKind = landKind.getSocialCapitals().getSocialCapitalAnswers().get(nextQuestionId).getSocialCapitalQuestion();
                    setQuestionView(socialCapitalQuestionsLandKind.getOptionType(),
                            socialCapitalQuestionsLandKind.getQuestion(),
                            socialCapitalQuestionsLandKind.getSocialCapitalAnswerOptionses(),
                            landKind.getSocialCapitals().getSocialCapitalAnswers().get(currentQuestionId).getMultipleAnswers());
                    currentQuestionId = nextQuestionId;
                } else {
                    finish();
                }
                break;
            case R.id.next_button:
                if (currentQuestionId < 13) {
                    Log.e("Current id ", currentQuestionId + "");
                    RealmList<MultipleAnswer> multipleAnswers;
                    multipleAnswers = new RealmList<>();
                    for (SocialCapitalAnswerOptions socialCapitalAnswerOptions : socialCapitalAnswerOptionsesList) {
                        multipleAnswers.add(addAnswer(socialCapitalAnswerOptions.getId()));
                    }
                    realm.beginTransaction();
                    landKind.getSocialCapitals().getSocialCapitalAnswers().get(currentQuestionId).setMultipleAnswers(multipleAnswers);
                    realm.commitTransaction();
//                    if(landKind.getSocialCapitals().getSocialCapitalAnswers().size() == 0){
//                        RealmList<MultipleAnswer> multipleAnswers;
//                        multipleAnswers = new RealmList<>();
//                        for(SocialCapitalAnswerOptions socialCapitalAnswerOptions : socialCapitalAnswerOptionsesList){
//                            multipleAnswers.add(addAnswer(socialCapitalAnswerOptions.getId()));
//                        }
//
//                        realm.beginTransaction();
//                        SocialCapitalAnswer socialCapitalAnswer = realm.createObject(SocialCapitalAnswer.class);
//                        socialCapitalAnswer.setId(getNextKeySocialCapitalAnswer());
//                        socialCapitalAnswer.setSurveyId(serveyId);
//                        socialCapitalAnswer.setSocialCapitalQuestion(socialCapitalQuestionses.get(currentQuestionId));
//                        socialCapitalAnswer.setMultipleAnswers(multipleAnswers);
//                        realm.commitTransaction();
//
//                        RealmList<SocialCapitalAnswer> socialCapitalAnswers = new RealmList<SocialCapitalAnswer>();
//                        socialCapitalAnswers.add(socialCapitalAnswer);
//                    }

                    socialCapitalAnswerOptionsesList.clear();
                    clearOptions();


                    Log.e("MULTIPLE ",landKind.getSocialCapitals().getSocialCapitalAnswers().get(currentQuestionId).getMultipleAnswers().toString());


                    int preQuestionId = currentQuestionId + 1;
                    SocialCapitalQuestions socialCapitalQuestionsLandKind = landKind.getSocialCapitals().getSocialCapitalAnswers().get(preQuestionId).getSocialCapitalQuestion();
                    setQuestionView(socialCapitalQuestionsLandKind.getOptionType(),
                            socialCapitalQuestionsLandKind.getQuestion(),
                            socialCapitalQuestionsLandKind.getSocialCapitalAnswerOptionses(),
                            landKind.getSocialCapitals().getSocialCapitalAnswers().get(currentQuestionId).getMultipleAnswers());
                    currentQuestionId = preQuestionId;


                }
//                Intent intent = new Intent(SocialCapitalActivity.this, SocialCapitalActivity.class);
//                startActivity(intent);
                break;
            case R.id.image_view_menu_icon:
                toggleMenuDrawer();
                break;
            case  R.id.drawer_close_btn:
                toggleMenuDrawer();
                break;
            case  R.id.text_view_about:
                Intent i = new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(i);
                break;
            case R.id.logout:
                Intent intent = new Intent(getApplicationContext(),RegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.text_start_survey:
                Intent intents = new Intent(getApplicationContext(),MainActivity.class);
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intents);
                break;
        }
    }

    public void clearOptions(){
        optionA.setChecked(false);
        optionB.setChecked(false);
        optionC.setChecked(false);
        optionD.setChecked(false);
        optionE.setChecked(false);

        optionARadio.setChecked(false);
        optionBRadio.setChecked(false);
        optionCRadio.setChecked(false);
        optionDRadio.setChecked(false);
        optionERadio.setChecked(false);
    }

    public MultipleAnswer addAnswer(long answerId) {
        realm.beginTransaction();
        MultipleAnswer multipleAnswer = realm.createObject(MultipleAnswer.class);
        multipleAnswer.setId(getNextKeyMultipleAnswer());
        multipleAnswer.setAnswer((int) answerId);
        realm.commitTransaction();
        return multipleAnswer;
    }


    public void setQuestionView(String type, String questionval,
                                RealmList<SocialCapitalAnswerOptions> options,
                                RealmList<MultipleAnswer> multipleAnswers1) {
        LandKind landKind1 = realm.where(LandKind.class)
                .equalTo("surveyId", serveyId)
                .equalTo("status", "active")
                .equalTo("name", currentSocialCapitalServey)
                .findFirst();

        RealmList<MultipleAnswer> multipleAnswers = landKind1.getSocialCapitals().getSocialCapitalAnswers().get(currentQuestionId).getMultipleAnswers();
        //RealmList<SocialCapitalAnswerOptions> options = landKind1.getSocialCapitals().getSocialCapitalAnswers().get(currentQuestionId).getSocialCapitalQuestion();

        question.setText(questionval);

        optionA.setVisibility(View.GONE);
        optionB.setVisibility(View.GONE);
        optionC.setVisibility(View.GONE);
        optionD.setVisibility(View.GONE);
        optionE.setVisibility(View.GONE);

        optionARadio.setVisibility(View.GONE);
        optionBRadio.setVisibility(View.GONE);
        optionCRadio.setVisibility(View.GONE);
        optionDRadio.setVisibility(View.GONE);
        optionERadio.setVisibility(View.GONE);

        int i = 0;
        for (SocialCapitalAnswerOptions socialCapitalAnswerOptions : options) {
            if (i == 0) {
                if (type.equals("multiple")) {
                    changeDisplay(type, options, i, optionA, null, multipleAnswers);
                } else {
                    changeDisplay(type, options, i, null, optionARadio, multipleAnswers);
                }
            } else if (i == 1) {
                if (type.equals("multiple")) {
                    changeDisplay(type, options, i, optionB, null, multipleAnswers);
                } else {
                    changeDisplay(type, options, i, null, optionBRadio, multipleAnswers);
                }
            } else if (i == 2) {
                if (type.equals("multiple")) {
                    changeDisplay(type, options, i, optionC, null, multipleAnswers);
                } else {
                    changeDisplay(type, options, i, null, optionCRadio, multipleAnswers);
                }
            } else if (i == 3) {
                if (type.equals("multiple")) {
                    changeDisplay(type, options, i, optionD, null, multipleAnswers);
                } else {
                    changeDisplay(type, options, i, null, optionDRadio, multipleAnswers);
                }
            } else if (i == 4) {
                if (type.equals("multiple")) {
                    changeDisplay(type, options, i, optionE, null, multipleAnswers);
                } else {
                    changeDisplay(type, options, i, null, optionERadio, multipleAnswers);
                }
            }
            i++;
        }
    }

    public void changeDisplay(String type, RealmList<SocialCapitalAnswerOptions> options,
                              int index, CheckBox checkBox, RadioButton radioButton,
                              RealmList<MultipleAnswer> multipleAnswers) {

        LandKind landKind1 = realm.where(LandKind.class)
                .equalTo("surveyId", serveyId)
                .equalTo("status", "active")
                .equalTo("name", currentSocialCapitalServey)
                .findFirst();

        if (type.equals("multiple")) {
            checkboxgroup.setVisibility(View.VISIBLE);
            radiobuttongroup.setVisibility(View.GONE);
            if (options.get(index) == null) {
                checkBox.setVisibility(View.GONE);
            } else {
                for(MultipleAnswer multipleAnswerItr : landKind1.getSocialCapitals().getSocialCapitalAnswers().get(currentQuestionId).getMultipleAnswers()){
                    Log.e("AFTER ",options.get(index).getId()+" "+multipleAnswerItr.getAnswer());
                    if(options.get(index).getId() == multipleAnswerItr.getAnswer()){
                        Log.e("AFTER ","CLEAR CHANGE DISPLAY TEST");
                        checkBox.setChecked(true);
                        socialCapitalAnswerOptionsesList.add(options.get(index));
                    }
                }
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setText(options.get(index).getOptions());
                checkBox.setTag(R.string.checkbox_id, index);
            }
        } else {
            radiobuttongroup.setVisibility(View.VISIBLE);
            checkboxgroup.setVisibility(View.GONE);
            if (options.get(index) == null) {
                radioButton.setVisibility(View.GONE);
            } else {
                for(MultipleAnswer multipleAnswerItr : landKind1.getSocialCapitals().getSocialCapitalAnswers().get(currentQuestionId).getMultipleAnswers()){
                    if(options.get(index).getId() == multipleAnswerItr.getAnswer()){
                        radioButton.setChecked(true);
                        socialCapitalAnswerOptionsesList.add(options.get(index));
                    }
                }

                radioButton.setVisibility(View.VISIBLE);
                radioButton.setText(options.get(index).getOptions());
                radioButton.setTag(R.string.checkbox_id, index);
            }
        }
    }

    public void slectedItems(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        socialCapitalQuestionsSelectedItem = landKind.getSocialCapitals().getSocialCapitalAnswers().get(currentQuestionId).getSocialCapitalQuestion();
        switch (view.getId()) {
            case R.id.option_a:
                Log.e("PP ", optionA.getTag(R.string.checkbox_id).toString());
                for (SocialCapitalAnswerOptions socialCapitalAnswerOptions : socialCapitalQuestionsSelectedItem.getSocialCapitalAnswerOptionses()) {
                    Log.e("ZZ ", socialCapitalAnswerOptions.toString());
                }
                if (checked) {
                    socialCapitalAnswerOptionsesList.add(socialCapitalQuestionsSelectedItem.getSocialCapitalAnswerOptionses().get(Integer.parseInt(optionA.getTag(R.string.checkbox_id).toString())));
                } else {
                    socialCapitalAnswerOptionsesList.remove(socialCapitalQuestionsSelectedItem.getSocialCapitalAnswerOptionses().get(Integer.parseInt(optionA.getTag(R.string.checkbox_id).toString())));
                }
                break;
            case R.id.option_b:
                Log.e("PP ", optionB.getTag(R.string.checkbox_id).toString());
                if (checked) {
                    socialCapitalAnswerOptionsesList.add(
                            socialCapitalQuestionsSelectedItem.getSocialCapitalAnswerOptionses().get(
                                    Integer.parseInt(
                                            optionB.getTag(R.string.checkbox_id).toString())));
                } else {
                    socialCapitalAnswerOptionsesList.remove(socialCapitalQuestionsSelectedItem.getSocialCapitalAnswerOptionses().get(Integer.parseInt(optionB.getTag(R.string.checkbox_id).toString())));
                }
                break;
            case R.id.option_c:
                Log.e("PP ", optionC.getTag(R.string.checkbox_id).toString());
                if (checked) {
                    socialCapitalAnswerOptionsesList.add(socialCapitalQuestionsSelectedItem.getSocialCapitalAnswerOptionses().get(Integer.parseInt(optionC.getTag(R.string.checkbox_id).toString())));
                } else {
                    socialCapitalAnswerOptionsesList.remove(socialCapitalQuestionsSelectedItem.getSocialCapitalAnswerOptionses().get(Integer.parseInt(optionC.getTag(R.string.checkbox_id).toString())));
                }
                break;
            case R.id.option_d:
                Log.e("PP ", optionD.getTag(R.string.checkbox_id).toString());
                if (checked) {
                    socialCapitalAnswerOptionsesList.add(socialCapitalQuestionsSelectedItem.getSocialCapitalAnswerOptionses().get(Integer.parseInt(optionD.getTag(R.string.checkbox_id).toString())));
                } else {
                    socialCapitalAnswerOptionsesList.remove(socialCapitalQuestionsSelectedItem.getSocialCapitalAnswerOptionses().get(Integer.parseInt(optionD.getTag(R.string.checkbox_id).toString())));
                }
                break;
            case R.id.option_e:
                Log.e("PP ", optionE.getTag(R.string.checkbox_id).toString());
                if (checked) {
                    socialCapitalAnswerOptionsesList.add(socialCapitalQuestionsSelectedItem.getSocialCapitalAnswerOptionses().get(Integer.parseInt(optionE.getTag(R.string.checkbox_id).toString())));
                } else {
                    socialCapitalAnswerOptionsesList.remove(socialCapitalQuestionsSelectedItem.getSocialCapitalAnswerOptionses().get(Integer.parseInt(optionE.getTag(R.string.checkbox_id).toString())));
                }
                break;
        }
    }

    public int getNextKeySocialCapitalAnswer() {
        return realm.where(SocialCapitalAnswer.class).max("id").intValue() + 1;
    }

    public int getNextKeyMultipleAnswer() {
        return realm.where(MultipleAnswer.class).max("id").intValue() + 1;
    }


    public void getAllSurvey() {
        Survey survey = realm.where(Survey.class)
                .equalTo("surveyId", serveyId)
                .findFirst();

    }
    public void toggleMenuDrawer(){
        if(menuDrawerLayout.isDrawerOpen(GravityCompat.START)){
            menuDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            menuDrawerLayout.openDrawer(GravityCompat.START);
        }
    }
}