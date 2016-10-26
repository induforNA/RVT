package com.sayone.omidyar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sayone.omidyar.R;
import com.sayone.omidyar.model.Outlay;
import com.sayone.omidyar.model.RevenueProduct;

import java.util.List;

/**
 * Created by sayone on 5/10/16.
 */

public class CostOutlayAdapter extends RecyclerView.Adapter<CostOutlayAdapter.CostOutlayViewHolder>{

    private List<Outlay> costOutlays;

    public class CostOutlayViewHolder extends RecyclerView.ViewHolder {
        public TextView revenueProductName;
        public CostOutlayViewHolder(View itemView) {
            super(itemView);
            revenueProductName = (TextView) itemView.findViewById(R.id.revenue_product_name);
        }
    }

    public CostOutlayAdapter(List<Outlay> revenueProducts) {
        this.costOutlays = revenueProducts;
    }

    @Override
    public CostOutlayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.revenue_product_list_item, parent, false);

        return new CostOutlayViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CostOutlayViewHolder holder, int position) {
        Outlay costOutlay = costOutlays.get(position);
        holder.revenueProductName.setText(costOutlay.getItemName());
    }

    @Override
    public int getItemCount() {
        return costOutlays.size();
    }



}
