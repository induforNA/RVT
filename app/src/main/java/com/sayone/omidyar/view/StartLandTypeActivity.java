package com.sayone.omidyar.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;

/**
 * Created by sayone on 14/10/16.
 */

public class StartLandTypeActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_land_type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_button:
                break;
            case R.id.back_button:
                finish();
                break;
        }
    }
}
