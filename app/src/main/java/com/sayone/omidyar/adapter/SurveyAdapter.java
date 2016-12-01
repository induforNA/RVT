package com.sayone.omidyar.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sayone.omidyar.R;
import com.sayone.omidyar.model.Participant;
import com.sayone.omidyar.model.Survey;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;

/**
 * Created by sayone on 3/10/16.
 */
public class SurveyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<Survey>surveyList;
    private final int viewTypeHeader=0,viewTypeList=1;
    private Boolean flag,flag1=true;
    private Realm realm;
    private SharedPreferences sharedPref;

    private Context context;
    private String surveyId;
    Set<String> set;
    private SharedPreferences.Editor editor;


    public class SurveyViewHolder extends RecyclerView.ViewHolder {
        private TextView surveyName;
        private CheckBox checkBoxSurvey;
        private Button exportButton;

        public SurveyViewHolder(View itemView) {
            super(itemView);
            set = new HashSet<String>();
            realm = Realm.getDefaultInstance();
            surveyName = (TextView) itemView.findViewById(R.id.survey_name);
            checkBoxSurvey=(CheckBox)itemView.findViewById(R.id.checkbox_survey);
            exportButton=(Button)itemView.findViewById(R.id.button_export);

        }
    }
    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView header;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.header);

        }
    }

    public SurveyAdapter(List<Survey> surveyList) {
        this.flag=false;
        this.surveyList = surveyList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        sharedPref = parent.getContext().getSharedPreferences(
                "com.sayone.omidyar.PREFERENCE_FILE_KEY_SET", Context.MODE_PRIVATE);
        context=parent.getContext();
        editor=sharedPref.edit();
        editor.clear();
        editor.putStringSet("surveySet",set);
        switch(viewType)
        {

            case viewTypeHeader:
                itemView= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_header, parent, false);
                return new HeaderViewHolder(itemView);


            case viewTypeList:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.survey_list_item, parent, false);
                return new SurveyViewHolder(itemView);

        }


        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder){
            if(flag){
                ((HeaderViewHolder)holder).header.setText(context.getResources().getText(R.string.unselect_all));
            } else {
                ((HeaderViewHolder)holder).header.setText(context.getResources().getText(R.string.select_all));
            }
            ((HeaderViewHolder)holder).header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  flag=toggle();
                    notifyDataSetChanged();
                }
            });

        }
        if (holder instanceof SurveyViewHolder){
            int pos=position-1;
            final Survey survey = surveyList.get(pos);
            if(survey.isSendStatus()) {
                ((SurveyViewHolder) holder).exportButton.setVisibility(View.VISIBLE);
            }
            else{
                ((SurveyViewHolder) holder).exportButton.setVisibility(View.INVISIBLE);
            }

            ((SurveyViewHolder)holder).checkBoxSurvey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(flag1) {
                        ((SurveyViewHolder) holder).checkBoxSurvey.setChecked(true);
                        realm.beginTransaction();
                        survey.setSendStatus(true);
                        realm.commitTransaction();
                        set.add(survey.getSurveyId());
                        editor = sharedPref.edit();
                        editor.clear();
                        editor.putStringSet("surveySet", set);
                        editor.apply();
                    }
                    else{
                       ((SurveyViewHolder) holder).checkBoxSurvey.setChecked(false);
//                        realm.beginTransaction();
//                        survey.setSendStatus(false);
//                        realm.commitTransaction();
                        set.remove(survey.getSurveyId());
                        editor = sharedPref.edit();
                        editor.clear();
                        editor.putStringSet("surveySet", set);
                        editor.apply();
                    }
// notifyDataSetChanged();
                    flag1=toggle1();
                }
            });
            ((SurveyViewHolder)holder).surveyName.setText(survey.getSurveyId());
          if(flag){
              ((SurveyViewHolder)holder).checkBoxSurvey.setChecked(true);

           } else {
              ((SurveyViewHolder)holder).checkBoxSurvey.setChecked(false);
              set.clear();
          }
            if(((SurveyViewHolder)holder).checkBoxSurvey.isChecked()){
                realm.beginTransaction();
                survey.setSendStatus(true);
                realm.commitTransaction();
                set.add(survey.getSurveyId());
            }


        }
        editor=sharedPref.edit();
        editor.clear();
        editor.putStringSet("surveySet",set);
        editor.apply();


    }

    private boolean toggle() {
        return !flag;
    }

    private boolean toggle1() {
        return !flag1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? viewTypeHeader : viewTypeList;
    }

   /* @Override
    public void onBindViewHolder(SurveyViewHolder holder, int position) {
        Survey survey = surveyList.get(position);
        holder.surveyName.setText(survey.getSurveyId());
      //  holder.checkBoxSurvey.setChecked();

    }*/

    @Override
    public int getItemCount() {
        return surveyList.size()+1;
    }



}
