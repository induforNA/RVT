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
import com.sayone.omidyar.model.CostElement;
import com.sayone.omidyar.model.Quantity;
import com.sayone.omidyar.model.RevenueProduct;
import com.sayone.omidyar.view.NaturalCapitalCostActivityA;

import java.util.List;

import io.realm.Realm;

/**
 * Created by sayone on 5/10/16.
 */

public class CostAdapter extends RecyclerView.Adapter<CostAdapter.CostProductViewHolder>{

    private List<CostElement> costElements;
    private NaturalCapitalCostActivityA mContext;

    public class CostProductViewHolder extends RecyclerView.ViewHolder {
        public TextView costProductName;
        public ImageView deleteButton;
        public CostProductViewHolder(final View itemView) {
            super(itemView);
            costProductName = (TextView) itemView.findViewById(R.id.revenue_product_name);
            deleteButton=(ImageView)itemView.findViewById(R.id.button_delete);

        }
    }

    public CostAdapter(List<CostElement> costElements, NaturalCapitalCostActivityA context) {
        this.costElements = costElements;
        mContext=context;
    }

    @Override
    public CostProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.revenue_product_list_item, parent, false);

        return new CostProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CostProductViewHolder holder, int position) {
        CostElement costElement = costElements.get(position);
        holder.costProductName.setText(costElement.getName());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                CostElement costElement = realm.where(CostElement.class)
                        .equalTo("name",holder.costProductName.getText().toString())
                        .findFirst();
                costElement.deleteFromRealm();
                realm.commitTransaction();
                Toast toast = Toast.makeText(mContext,mContext.getResources().getText(R.string.text_deleted), Toast.LENGTH_SHORT);
                toast.show();

                Intent intent=new Intent(mContext,NaturalCapitalCostActivityA.class);
                mContext.startActivity(intent);
                mContext.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return costElements.size();
    }



}
