package com.sayone.omidyar.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);

        /*final RotateAnimation anim = new RotateAnimation(0f, 350f, 0f,350f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(700);
        final ImageView splash = (ImageView) findViewById(R.id.icon);
        splash.setAnimation(null);*/
        final ImageView imageView= (ImageView)findViewById(R.id.icon);
        final Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);




        Thread timerThread = new Thread() {
            public void run() {
                try {
                    imageView.startAnimation(fadeInAnimation );
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                   Intent intent = new Intent(getApplicationContext(), LoadingActivity.class);
                    startActivity(intent);

                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
