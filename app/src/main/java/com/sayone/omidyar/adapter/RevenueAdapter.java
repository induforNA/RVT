package com.sayone.omidyar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sayone.omidyar.R;
import com.sayone.omidyar.model.Participant;
import com.sayone.omidyar.model.RevenueProduct;

import java.util.List;

/**
 * Created by sayone on 5/10/16.
 */

public class RevenueAdapter extends RecyclerView.Adapter<RevenueAdapter.RevenueProductViewHolder>{

    private List<RevenueProduct> revenueProducts;

    public class RevenueProductViewHolder extends RecyclerView.ViewHolder {
        public TextView revenueProductName;
        public RevenueProductViewHolder(View itemView) {
            super(itemView);
            revenueProductName = (TextView) itemView.findViewById(R.id.revenue_product_name);
        }
    }

    public RevenueAdapter(List<RevenueProduct> revenueProducts) {
        this.revenueProducts = revenueProducts;
    }

    @Override
    public RevenueProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.revenue_product_list_item, parent, false);

        return new RevenueProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RevenueProductViewHolder holder, int position) {
        RevenueProduct revenueProduct = revenueProducts.get(position);
        holder.revenueProductName.setText(revenueProduct.getName());
    }

    @Override
    public int getItemCount() {
        return revenueProducts.size();
    }



}
