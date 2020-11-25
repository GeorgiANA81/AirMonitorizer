package com.example.airmonitorizer2;

import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class WelcomeUserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);

        FrameLayout welcomeFrame = new FrameLayout(this);
        /*welcomeFrame.setLayoutParams(new AbsListView.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                     FrameLayout.LayoutParams.MATCH_PARENT));*/
        final TextView welcomeUser = findViewById(R.id.welcomeUser);
        welcomeFrame.addView(welcomeUser);
        setContentView(welcomeFrame);

        FrameLayout optionsFrame = new FrameLayout(this);
        /*optionsFrame.setLayoutParams(new AbsListView.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                     FrameLayout.LayoutParams.MATCH_PARENT));*/
        final Button createProfile = findViewById(R.id.buttonCreateProfile);
        final Button viewProfile = findViewById(R.id.buttonViewProfile);
        final Button setParameters = findViewById(R.id.buttonSetParameters);
        final Button viewMeasurements = findViewById(R.id.buttonViewMeasurements);
        final Button viewHistory = findViewById(R.id.buttonViewHistory);
        welcomeFrame.addView(createProfile);
        welcomeFrame.addView(viewProfile);
        welcomeFrame.addView(setParameters);
        welcomeFrame.addView(viewMeasurements);
        welcomeFrame.addView(viewHistory);
        setContentView(optionsFrame);

        FrameLayout statusFrame = new FrameLayout(this);
        optionsFrame.setLayoutParams(new AbsListView.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                     FrameLayout.LayoutParams.MATCH_PARENT));
        final TextView airQualityNow = findViewById(R.id.airQualityNow);
        final Button signOut = findViewById(R.id.buttonSignOut);
        welcomeFrame.addView(airQualityNow);
        welcomeFrame.addView(signOut);
        setContentView(statusFrame);
    }
}
