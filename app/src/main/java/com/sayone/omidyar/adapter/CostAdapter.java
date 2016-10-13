package com.sayone.omidyar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sayone.omidyar.R;
import com.sayone.omidyar.model.CostElement;
import com.sayone.omidyar.model.RevenueProduct;

import java.util.List;

/**
 * Created by sayone on 5/10/16.
 */

public class CostAdapter extends RecyclerView.Adapter<CostAdapter.CostProductViewHolder>{

    private List<CostElement> costElements;

    public class CostProductViewHolder extends RecyclerView.ViewHolder {
        public TextView costProductName;
        public CostProductViewHolder(View itemView) {
            super(itemView);
            costProductName = (TextView) itemView.findViewById(R.id.revenue_product_name);
        }
    }

    public CostAdapter(List<CostElement> costElements) {
        this.costElements = costElements;
    }

    @Override
    public CostProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.revenue_product_list_item, parent, false);

        return new CostProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CostProductViewHolder holder, int position) {
        CostElement costElement = costElements.get(position);
        holder.costProductName.setText(costElement.getName());
    }

    @Override
    public int getItemCount() {
        return costElements.size();
    }



}
