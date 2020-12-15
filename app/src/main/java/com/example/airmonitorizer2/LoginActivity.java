package com.example.airmonitorizer2;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private FirebaseUser user;
    private String userID, admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        TextView register = (TextView)findViewById(R.id.lnkRegister);
        register.setPaintFlags(register.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        register.setMovementMethod(LinkMovementMethod.getInstance());
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        TextView resetPassword = (TextView)findViewById(R.id.lnkResetPassword);
        resetPassword.setPaintFlags(resetPassword.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        resetPassword.setMovementMethod(LinkMovementMethod.getInstance());
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        EditText editText1 = (EditText) findViewById(R.id.txtEmail);

        EditText editText2 = (EditText) findViewById(R.id.txtPwd);

        Button login = (Button) findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }

            private void userLogin() {
                String email = editText1.getText().toString().trim();
                String password = editText2.getText().toString().trim();

                if(email.isEmpty()){
                    editText1.setError("Email is required!");
                    editText1.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editText1.setError("Please provide valid email!");
                    editText1.requestFocus();
                    return;
                }
                if(password.isEmpty()){
                    editText2.setError("Password is required!");
                    editText2.requestFocus();
                    return;
                }
                if(password.length() < 6){
                    editText1.setError("Minimum password length should be 6 characters!");
                    editText1.requestFocus();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //redirect to user profile
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            rootRef  = FirebaseDatabase.getInstance().getReference("Users");
                            userID = user.getUid();
                            rootRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User userProfile = snapshot.getValue(User.class);
                                    if(userProfile!=null){
                                        admin = userProfile.admin;
                                    }

                                    if(admin.equals("yes")){
                                        //admin profile
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    }
                                    else{
                                        //user profile
                                        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                        else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                Toast.makeText(LoginActivity.this, "You entered a wrong email!", Toast.LENGTH_LONG).show();
                            } catch (FirebaseAuthInvalidCredentialsException invalidEmail) {
                                Toast.makeText(LoginActivity.this, "You entered a wrong password!", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(LoginActivity.this, "Failed to login! Please check your credentials!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });
    }
}
