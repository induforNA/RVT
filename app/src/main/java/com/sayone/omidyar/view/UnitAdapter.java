package com.sayone.omidyar.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.sayone.omidyar.R;

import java.util.HashSet;
import java.util.List;

/**
 * Created by sayone on 24/10/16.
 */
public class UnitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<String> unitList;

    public UnitAdapter(List<String> unitList) {
        this.unitList = unitList;
    }

    public class UnitViewHolder extends RecyclerView.ViewHolder {
        private TextView unitName;
        private ImageView imageEditButton;

        public UnitViewHolder(View itemView) {
            super(itemView);
            unitName = (TextView) itemView.findViewById(R.id.unit_name);
            imageEditButton=(ImageView) itemView.findViewById(R.id.edit_unit);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.unit_list_item, parent, false);

        return new UnitViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((UnitViewHolder)holder).unitName.setText(unitList.get(position+1));
    }

    @Override
    public int getItemCount() {
        return unitList.size()-1;
    }
}
