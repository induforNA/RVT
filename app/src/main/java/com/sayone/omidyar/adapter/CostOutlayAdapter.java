package com.sayone.omidyar.adapter;

import android.content.Context;
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
import com.sayone.omidyar.model.RevenueProduct;
import com.sayone.omidyar.view.NaturalCapitalCostOutlay;
import com.sayone.omidyar.view.NaturalCapitalSurveyActivityB;

import java.util.List;

import io.realm.Realm;

/**
 * Created by sayone on 5/10/16.
 */

public class CostOutlayAdapter extends RecyclerView.Adapter<CostOutlayAdapter.CostOutlayViewHolder>{

    private List<Outlay> costOutlays;
    private NaturalCapitalCostOutlay mContext;

    public class CostOutlayViewHolder extends RecyclerView.ViewHolder {
        public TextView revenueProductName;
        public ImageView deleteButton;
        public CostOutlayViewHolder(View itemView) {
            super(itemView);
            revenueProductName = (TextView) itemView.findViewById(R.id.revenue_product_name);
            deleteButton=(ImageView)itemView.findViewById(R.id.button_delete);
        }
    }

    public CostOutlayAdapter(List<Outlay> revenueProducts, NaturalCapitalCostOutlay context) {
        this.costOutlays = revenueProducts;
        mContext=context;
    }

    @Override
    public CostOutlayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.revenue_product_list_item, parent, false);

        return new CostOutlayViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CostOutlayViewHolder holder, int position) {
        Outlay costOutlay = costOutlays.get(position);
        holder.revenueProductName.setText(costOutlay.getItemName());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                Outlay outlay = realm.where(Outlay.class)
                        .equalTo("itemName",holder.revenueProductName.getText().toString())
                        .findFirst();
                outlay.deleteFromRealm();
                realm.commitTransaction();
                Toast toast = Toast.makeText(mContext,mContext.getResources().getText(R.string.text_deleted), Toast.LENGTH_SHORT);
                toast.show();
                Intent intent=new Intent(mContext,NaturalCapitalCostOutlay.class);
                mContext.startActivity(intent);
                mContext.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return costOutlays.size();
    }



}
