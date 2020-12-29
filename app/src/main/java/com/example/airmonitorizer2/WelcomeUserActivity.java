package com.example.airmonitorizer2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.content.ContentValues.TAG;


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

    private TextView welcome;
    private View title;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef, rootRef2;
    private FirebaseUser user, user2;
    private String userID, name, userID2;
    private View createProfile, viewProfile, setParam;
    private View profile;
    private Button saveChecks;
    private CheckBox asthma;
    private CheckBox allergy;
    private String diseases = "";
    private String checkExistingDiseases = "";
    private Button viewDetails;
    private TextView showDetails;
    private View parameters;
    private Button saveParameters;
    private String checkedParameters = "";
    private CheckBox temperatureCheck;
    private CheckBox humidityCheck;
    private CheckBox gasCheck;
    private CheckBox dustCheck;
    private CheckBox smokeCheck;
    private String checkExistingParameters = "";
    private View viewInfo;
    private TextView infoName;
    private TextView infoEmail;
    private TextView infoPhone;
    private TextView infoDiseases;
    private TextView infoParameters;



     @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);
        title = (View) findViewById(R.id.titleBar);
         welcome = (TextView)title.findViewById(R.id.welcomeText);
         user = FirebaseAuth.getInstance().getCurrentUser();
         user2 = FirebaseAuth.getInstance().getCurrentUser();
         rootRef  = FirebaseDatabase.getInstance().getReference("Users");
         rootRef2  = FirebaseDatabase.getInstance().getReference("Users");
         userID = user.getUid();
         userID2 = user.getUid();
         profile = (View) findViewById(R.id.displayPaneCreateProfile);
         saveChecks = (Button)profile.findViewById(R.id.save);
         asthma = (CheckBox) profile.findViewById(R.id.asthmaCheck);
         allergy = (CheckBox) profile.findViewById(R.id.dustAllergyCheck);
         viewDetails = (Button)profile.findViewById(R.id.viewDetails);
         showDetails = (TextView) profile.findViewById(R.id.showDetails);
         parameters = (View) findViewById(R.id.displayPaneSetParameters);
         saveParameters = (Button) parameters.findViewById(R.id.buttonSave);
         temperatureCheck = (CheckBox) parameters.findViewById(R.id.temperatureCheck);
         humidityCheck = (CheckBox) parameters.findViewById(R.id.humidityCheck);
         gasCheck = (CheckBox) parameters.findViewById(R.id.gasCheck);
         dustCheck = (CheckBox) parameters.findViewById(R.id.dustCheck);
         smokeCheck = (CheckBox) parameters.findViewById(R.id.smokeCheck);
         viewInfo = (View) findViewById(R.id.displayPaneViewProfile);
         infoName = (TextView) viewInfo.findViewById(R.id.infoName);
         infoEmail = (TextView) viewInfo.findViewById(R.id.infoEmail);
         infoPhone = (TextView) viewInfo.findViewById(R.id.infoPhone);
         infoDiseases = (TextView) viewInfo.findViewById(R.id.infoDiseases);
         infoParameters = (TextView) viewInfo.findViewById(R.id.infoParameters);

         //preluam datele de la utilizatorul care este logat
         rootRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                  @Override
                                                                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                      User userProfile = snapshot.getValue(User.class);
                                                                      if (userProfile != null) {
                                                                          name = userProfile.fullname;
                                                                          welcome.setText("Welcome, " + name + "!");
                                                                          checkExistingDiseases = userProfile.diseases;
                                                                          checkExistingParameters = userProfile.parameters;
                                                                          if (!checkExistingDiseases.equals("")){
                                                                              String[] d = checkExistingDiseases.split(",",3);

                                                                              for(String each:d){
                                                                                  if(each.equals("Asthma")){
                                                                                      asthma.setChecked(true);
                                                                                  }
                                                                                  else if(each.equals("Dust allergy")){
                                                                                      allergy.setChecked(true);
                                                                                  }
                                                                              }
                                                                          }
                                                                          else{
                                                                              infoDiseases.setText("You didn't select anything!");
                                                                          }
                                                                          if (!checkExistingParameters.equals("")){
                                                                              String[] p = checkExistingParameters.split(",",6);
                                                                              for(String each:p){
                                                                                  if(each.equals("Temperature")){
                                                                                      temperatureCheck.setChecked(true);
                                                                                  }
                                                                                  else if(each.equals("Humidity")){
                                                                                      humidityCheck.setChecked(true);
                                                                                  }
                                                                                  else if(each.equals("Gas")){
                                                                                      gasCheck.setChecked(true);
                                                                                  }
                                                                                  else if(each.equals("Dust")){
                                                                                      dustCheck.setChecked(true);
                                                                                  }
                                                                                  else if(each.equals("Smoke")){
                                                                                      smokeCheck.setChecked(true);
                                                                                  }
                                                                              }

                                                                          }
                                                                          else{
                                                                              infoParameters.setText("You didn't select anything!");
                                                                          }
                                                                      }
                                                                  }

                                                                  @Override
                                                                  public void onCancelled(@NonNull DatabaseError error) {
                                                                      Toast.makeText(WelcomeUserActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                                                                  }
                                                              });


         gaugeView = (GaugeView) findViewById(R.id.gaugeView);
         gaugeView.setRotateDegree(degree);
         startRunning();

         createProfile = (View) findViewById(R.id.displayPaneCreateProfile);
         viewProfile = (View) findViewById(R.id.displayPaneViewProfile);
         setParam = (View) findViewById(R.id.displayPaneSetParameters);

        final Button buttonCreateProfile = findViewById(R.id.buttonCreateProfile);
        buttonCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProfile.setVisibility(View.VISIBLE);
                viewProfile.setVisibility(View.INVISIBLE);
                setParam.setVisibility(View.INVISIBLE);
            }
        });

         final Button buttonViewProfile = findViewById(R.id.buttonViewProfile);
         buttonViewProfile.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
//                 Intent intent = new Intent(WelcomeUserActivity.this, RegistrationActivity.class);
//                 startActivity(intent);
                 createProfile.setVisibility(View.INVISIBLE);
                 viewProfile.setVisibility(View.VISIBLE);
                 setParam.setVisibility(View.INVISIBLE);

                 rootRef2.child(userID2).addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                         User userProfile2 = snapshot.getValue(User.class);
                         if (userProfile2 != null) {
                             infoName.setText(userProfile2.fullname);
                             infoEmail.setText(userProfile2.email);
                             infoPhone.setText(userProfile2.phone);
                             infoDiseases.setText(userProfile2.diseases);
                             infoParameters.setText(userProfile2.parameters);
                             checkExistingDiseases = userProfile2.diseases;
                             checkExistingParameters = userProfile2.parameters;
                             if (checkExistingDiseases.equals("")){
                                 infoDiseases.setText("You didn't select anything!");
                         }
                         if (checkExistingParameters.equals("")){
                             infoParameters.setText("You didn't select anything!");
                     }
                 }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {
                         Toast.makeText(WelcomeUserActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                     }
                 });
             }
         });

         final Button buttonSetParameters = findViewById(R.id.buttonSetParameters);
         buttonSetParameters.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 createProfile.setVisibility(View.INVISIBLE);
                 viewProfile.setVisibility(View.INVISIBLE);
                 setParam.setVisibility(View.VISIBLE);
             }
         });

         final Button buttonViewMeasurements = findViewById(R.id.buttonViewMeasurements);
         buttonViewMeasurements.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(WelcomeUserActivity.this, MainActivity.class);
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
         buttonSignOut.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View v){
                 FirebaseAuth.getInstance().signOut();
                 Intent intent = new Intent(WelcomeUserActivity.this, HomeActivity.class);
                 startActivity(intent);
             }
         });

         saveChecks.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View v){
                 diseases = "";
                 if(asthma.isChecked()) {
                     diseases =diseases + "Asthma,";
                 }
                 if(allergy.isChecked()) {
                     diseases =diseases + "Dust allergy,";
                 }
                     FirebaseDatabase.getInstance().getReference("Users")
                             .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("diseases")
                             .setValue(diseases).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if (task.isSuccessful()) {
                                 Toast.makeText(WelcomeUserActivity.this, "Diseases saved!", Toast.LENGTH_LONG).show();
                             } else {
                                 Toast.makeText(WelcomeUserActivity.this, "Failed to save disease! Try again!", Toast.LENGTH_LONG).show();
                             }
                         }
                     });
             }
         });

         viewDetails.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View v){
                 showDetails.setMovementMethod(new ScrollingMovementMethod());
                 if(showDetails.getVisibility()==View.INVISIBLE){
                 showDetails.setVisibility(View.VISIBLE);}
                 else if (showDetails.getVisibility()==View.VISIBLE){
                     showDetails.setVisibility(View.INVISIBLE);
                 }
             }
         });

         saveParameters.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View v){
                 checkedParameters = "";
                 if(temperatureCheck.isChecked()) {
                     checkedParameters =checkedParameters + "Temperature,";
                 }
                 if(humidityCheck.isChecked()) {
                     checkedParameters =checkedParameters + "Humidity,";
                 }
                 if(gasCheck.isChecked()) {
                     checkedParameters =checkedParameters + "Gas,";
                 }
                 if(dustCheck.isChecked()) {
                     checkedParameters =checkedParameters + "Dust,";
                 }
                 if(smokeCheck.isChecked()) {
                     checkedParameters =checkedParameters + "Smoke,";
                 }
                 FirebaseDatabase.getInstance().getReference("Users")
                         .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parameters")
                         .setValue(checkedParameters).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         if (task.isSuccessful()) {
                             Toast.makeText(WelcomeUserActivity.this, "Parameters saved!", Toast.LENGTH_LONG).show();
                         } else {
                             Toast.makeText(WelcomeUserActivity.this, "Failed to save parameters! Try again!", Toast.LENGTH_LONG).show();
                         }
                     }
                 });
             }
         });
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
    public void onBackPressed() {

        Intent a = new Intent(WelcomeUserActivity.this, WelcomeUserActivity.class);
        startActivity(a);
    }
}


