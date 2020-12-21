package com.example.airmonitorizer2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        EditText email = (EditText) findViewById(R.id.txtEmail);
        Button reset = (Button) findViewById(R.id.btnResetPassword);
        mAuth = FirebaseAuth.getInstance();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }

            private void resetPassword() {
                String emailText = email.getText().toString().trim();
                if(emailText.isEmpty()){
                    email.setError("Email is required!");
                    email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
                    email.setError("Please provide valid email!");
                    email.requestFocus();
                    return;
                }
                mAuth.sendPasswordResetEmail(emailText).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ResetPasswordActivity.this, "Check your email!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                        }
                        else{
                            Toast.makeText(ResetPasswordActivity.this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        }
}