package com.example.airmonitorizer2;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class WelcomeUserActivity extends AppCompatActivity {

    GaugeView gaugeView;
    private float degree = -225;
    private float sweepAngleControl = 0;
    private float sweepAngleFirstChart = 1;
    private float sweepAngleSecondChart = 1;
    private float sweepAngleThirdChart = 1;
    private boolean isInProgress = false;
    private boolean resetMode = false;
    private boolean canReset = false;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);
         gaugeView = (GaugeView) findViewById(R.id.gaugeView);
         gaugeView.setRotateDegree(degree);
         startRunning();

        final Button buttonCreateProfile = findViewById(R.id.buttonCreateProfile);
        buttonCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //Intent intent = new Intent(WelcomeUserActivity.this,);

                //startActivity(intent);
            }
        });

         final Button buttonViewProfile = findViewById(R.id.buttonViewProfile);
         buttonViewProfile.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(WelcomeUserActivity.this, RegistrationActivity.class);
                 startActivity(intent);
             }
         });

         final Button buttonSetParameters = findViewById(R.id.buttonSetParameters);
         buttonSetParameters.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(WelcomeUserActivity.this, RegistrationActivity.class);
                 startActivity(intent);
             }
         });

         final Button buttonViewMeasurements = findViewById(R.id.buttonViewMeasurements);
         buttonViewMeasurements.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(WelcomeUserActivity.this, RegistrationActivity.class);
                 startActivity(intent);
             }
         });

         final Button buttonViewHistory = findViewById(R.id.buttonViewHistory);
         buttonViewHistory.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(WelcomeUserActivity.this, RegistrationActivity.class);
                 startActivity(intent);
             }
         });

         final Button buttonSignOut = findViewById(R.id.buttonSignOut);

         hideNavigationBar();
    }


private void hideNavigationBar(){
         this.getWindow().getDecorView()
                 .setSystemUiVisibility(
                         View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                 );
}












    private void resetGauges() {
        new Thread() {
            public void run() {
                for (int i = 0; i < 300; i++) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sweepAngleControl--;
                                sweepAngleFirstChart = 1;
                                sweepAngleSecondChart = 1;
                                sweepAngleThirdChart = 1;

                                degree--;
                                gaugeView.setSweepAngleFirstChart(0);
                                gaugeView.setSweepAngleSecondChart(0);
                                gaugeView.setSweepAngleThirdChart(0);
                                gaugeView.setRotateDegree(degree);
                            }
                        });
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (i == 299) {
                        resetMode = false;
                        canReset = false;
                    }

                }
            }
        }.start();
    }

    private void startRunning() {
        new Thread() {
            public void run() {
                for (int i = 0; i < 300; i++) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                degree++;
                                sweepAngleControl++;
                                if (degree < 45) {
                                    gaugeView.setRotateDegree(degree);
                                }

                                if (sweepAngleControl <= 90) {
                                    sweepAngleFirstChart++;
                                    gaugeView.setSweepAngleFirstChart(sweepAngleFirstChart);
                                } else if (sweepAngleControl <= 180) {
                                    sweepAngleSecondChart++;
                                    gaugeView.setSweepAngleSecondChart(sweepAngleSecondChart);
                                } else if (sweepAngleControl <= 270) {
                                    sweepAngleThirdChart++;
                                    gaugeView.setSweepAngleThirdChart(sweepAngleThirdChart);
                                }

                            }
                        });
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (i == 299) {
                        isInProgress = false;
                        canReset = true;
                    }

                }
            }
        }.start();
    }
}
