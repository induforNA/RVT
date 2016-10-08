package com.sayone.omidyar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sayone.omidyar.R;
import com.sayone.omidyar.model.RevenueProduct;
import com.sayone.omidyar.model.RevenueProductYears;

import java.util.ArrayList;

/**
 * Created by sayone on 6/10/16.
 */

public class YearsAdapter extends RecyclerView.Adapter<YearsAdapter.YearViewHolder> {

    ArrayList<RevenueProductYears> revenueProductYearses;

    public YearsAdapter(ArrayList<RevenueProductYears> revenueProductYearses) {
        this.revenueProductYearses = revenueProductYearses;
    }

    public class YearViewHolder extends RecyclerView.ViewHolder {
        public TextView revenueProductYear;
        public YearViewHolder(View itemView) {
            super(itemView);
            revenueProductYear = (TextView) itemView.findViewById(R.id.revenue_product_year);
        }
    }

    @Override
    public YearsAdapter.YearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.revenue_year_list_item, parent, false);

        return new YearsAdapter.YearViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(YearsAdapter.YearViewHolder holder, int position) {
        RevenueProductYears revenueProductYears = revenueProductYearses.get(position);
        holder.revenueProductYear.setText(revenueProductYears.getYear());
    }

    @Override
    public int getItemCount() {
        return revenueProductYearses.size();
    }


}
