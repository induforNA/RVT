package com.sayone.omidyar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sayone.omidyar.R;
import com.sayone.omidyar.model.Participant;
import com.sayone.omidyar.model.Survey;

import java.util.List;

/**
 * Created by sayone on 3/10/16.
 */
public class SurveyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<Survey>surveyList;
    private final int viewTypeHeader=0,viewTypeList=1;
    private Boolean flag;


    public class SurveyViewHolder extends RecyclerView.ViewHolder {
        private TextView surveyName;
        private CheckBox checkBoxSurvey;

        public SurveyViewHolder(View itemView) {
            super(itemView);
            surveyName = (TextView) itemView.findViewById(R.id.survey_name);
            checkBoxSurvey=(CheckBox)itemView.findViewById(R.id.checkbox_survey);

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder){
            if(flag){
                ((HeaderViewHolder)holder).header.setText("Unselect all");
            } else {
                ((HeaderViewHolder)holder).header.setText("Select all");
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
            Survey survey = surveyList.get(pos);

            ((SurveyViewHolder)holder).surveyName.setText(survey.getSurveyId());
          if(flag){
              ((SurveyViewHolder)holder).checkBoxSurvey.setChecked(true);
           } else {
              ((SurveyViewHolder)holder).checkBoxSurvey.setChecked(false);
          }
        }
    }

    private boolean toggle() {
        return !flag;
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
