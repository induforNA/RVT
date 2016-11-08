package com.sayone.omidyar.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.sayone.omidyar.R;

public class Main2Activity extends AppCompatActivity {
    ImageView button;
    private View toolbar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        button=(ImageView) findViewById(R.id.button_add_participant);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View myView = findViewById(R.id.image2);

// get the center for the clipping circle
                int cx = myView.getWidth() / 2;
                int cy = myView.getHeight() / 2;

// get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);

// create the animator for this view (the start radius is zero)
                Animator anim =
                        null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
                }

// make the view visible and start the animation
                myView.setVisibility(View.VISIBLE);
                anim.start();
            }
        });


    }
}
