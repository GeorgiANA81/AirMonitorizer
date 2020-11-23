package com.example.airmonitorizer2;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    public void openLoginPage(View view) {
        startActivity(new Intent(this,LoginActivity.class));
    }

    public void openRegisterPage(View view) {
        startActivity(new Intent(this,RegistrationActivity.class));
    }
}
