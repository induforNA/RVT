package com.sayone.omidyar.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sayone.omidyar.R;
import com.sayone.omidyar.model.Outlay;
import com.sayone.omidyar.model.Participant;
import com.sayone.omidyar.view.MainActivity;
import com.sayone.omidyar.view.NaturalCapitalCostOutlay;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Riyas PK on 9/18/2016.
 */
public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ParticipantsViewHolder> {

    private final MainActivity mContext;
    private List<Participant> participantList;

    public class ParticipantsViewHolder extends RecyclerView.ViewHolder {

        public TextView participantNo;
        public TextView participantName;
        public TextView participantOccupation;
        public TextView participantGender;
        public TextView participantAge;
        public TextView participantEducation;
        public ImageView deleteButton;

        public ParticipantsViewHolder(View itemView) {
            super(itemView);
            participantNo = (TextView) itemView.findViewById(R.id.participant_no);
            participantName = (TextView) itemView.findViewById(R.id.participant_name);
            participantOccupation = (TextView) itemView.findViewById(R.id.participant_occupation);
            participantGender = (TextView) itemView.findViewById(R.id.participant_gender);
            participantAge = (TextView) itemView.findViewById(R.id.participant_age);
            participantEducation = (TextView) itemView.findViewById(R.id.participant_education);
            deleteButton=(ImageView)itemView.findViewById(R.id.button_delete);
        }
    }

    public ParticipantsAdapter(List<Participant> participantList, MainActivity context) {
        this.participantList = participantList;
        mContext=context;
    }

    @Override
    public ParticipantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.participants_list_item, parent, false);

        return new ParticipantsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ParticipantsViewHolder holder, int position) {
        Participant participant = participantList.get(position);
        holder.participantNo.setText(String.valueOf(position+1));
        holder.participantName.setText(participant.getName());
        holder.participantOccupation.setText(participant.getOccupation());
        holder.participantGender.setText(participant.getGender());
        holder.participantAge.setText(String.valueOf(participant.getAge()));
        holder.participantEducation.setText(String.valueOf(participant.getYearsOfEdu()));
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                Participant participant = realm.where(Participant.class)
                        .equalTo("name",holder.participantName.getText().toString())
                        .findFirst();
                participant.deleteFromRealm();
                realm.commitTransaction();
                Toast toast = Toast.makeText(mContext,"Deleted", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent=new Intent(mContext,MainActivity.class);
                mContext.startActivity(intent);
                mContext.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }
}
