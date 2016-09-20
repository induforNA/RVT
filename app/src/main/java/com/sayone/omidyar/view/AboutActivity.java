package com.sayone.omidyar.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;

/**
 * Created by sayone on 17/9/16.
 */
public class AboutActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
