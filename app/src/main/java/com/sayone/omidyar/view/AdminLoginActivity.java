package com.sayone.omidyar.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;

public class AdminLoginActivity extends BaseActivity implements View.OnClickListener {
    private String passcode;
    private EditText adminPassword;
    private Button login, surveyorLogin, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminPassword = (EditText) findViewById(R.id.edittext_passcode);
        login = (Button) findViewById(R.id.button_login);
        surveyorLogin = (Button) findViewById(R.id.button_surveyor_login);
        signUp = (Button) findViewById(R.id.button_sign_up);

        login.setOnClickListener(this);
        surveyorLogin.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {

            case R.id.button_login:
                passcode = adminPassword.getText().toString();
                if (passcode.equals("omidyaradmin")) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.login_successful));
                    intent = new Intent(getApplicationContext(), AdminDashBoard.class);
                    startActivity(intent);
                } else if (passcode.equals("")) {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.empty_passcode));
                } else {
                    toastfunction(getApplicationContext(), getResources().getString(R.string.login_failed));
                }
                break;

            case R.id.button_surveyor_login:
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.button_sign_up:
                intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
        }
    }

    public void toastfunction(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
