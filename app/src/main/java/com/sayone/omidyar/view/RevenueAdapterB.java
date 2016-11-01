package com.sayone.omidyar.view;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sayone.omidyar.R;
import com.sayone.omidyar.adapter.RevenueAdapter;
import com.sayone.omidyar.model.RevenueProduct;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by sayone on 31/10/16.
 */
public class RevenueAdapterB extends RecyclerView.Adapter<RevenueAdapterB.RevenueProductViewHolder> {
    private final NaturalCapitalSurveyActivityB mContext;
    private List<RevenueProduct> revenueProducts;

    public class RevenueProductViewHolder extends RecyclerView.ViewHolder {
        private final ImageView deleteButton;
        public TextView revenueProductName;
        public RevenueProductViewHolder(View itemView) {
            super(itemView);
            revenueProductName = (TextView) itemView.findViewById(R.id.revenue_product_name);
            deleteButton=(ImageView)itemView.findViewById(R.id.button_delete);
        }
    }

    public RevenueAdapterB(List<RevenueProduct> revenueProducts, NaturalCapitalSurveyActivityB context) {

        this.revenueProducts = revenueProducts;
        mContext=context;
    }

    @Override
    public RevenueProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.revenue_product_list_item, parent, false);

        return new RevenueProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RevenueProductViewHolder holder, int position) {
        RevenueProduct revenueProduct = revenueProducts.get(position);
        holder.revenueProductName.setText(revenueProduct.getName());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                RevenueProduct revenueProduct = realm.where(RevenueProduct.class)
                        .equalTo("name",holder.revenueProductName.getText().toString())
                        .findFirst();
                revenueProduct.deleteFromRealm();
                realm.commitTransaction();
                Toast toast = Toast.makeText(mContext,"Deleted", Toast.LENGTH_SHORT);
                toast.show();

                Intent intent=new Intent(mContext,NaturalCapitalSurveyActivityB.class);
                mContext.startActivity(intent);
                mContext.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return revenueProducts.size();
    }
}
