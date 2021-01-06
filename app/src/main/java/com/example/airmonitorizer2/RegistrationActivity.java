package com.example.airmonitorizer2;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;


public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
        //button to login page
        TextView login = (TextView)findViewById(R.id.lnkLogin);
        login.setPaintFlags(login.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        login.setMovementMethod(LinkMovementMethod.getInstance());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        EditText editText1 = (EditText) findViewById(R.id.txtName);

        EditText editText2 = (EditText) findViewById(R.id.txtPhone);

        EditText editText3 = (EditText) findViewById(R.id.txtEmail);

        EditText editText4 = (EditText) findViewById(R.id.txtPwd1);

        EditText editText5 = (EditText) findViewById(R.id.txtPwd2);

        mAuth = FirebaseAuth.getInstance();
        //button for register
        Button register = (Button)findViewById(R.id.btnRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }

            private void registerUser() {
                String name = editText1.getText().toString().trim();
                String phone = editText2.getText().toString().trim();
                String email = editText3.getText().toString().trim();
                String password1 = editText4.getText().toString().trim();
                String password2 = editText5.getText().toString().trim();

                if(name.isEmpty()){
                    editText1.setError("Full name is required!");
                    editText1.requestFocus();
                    return;
                }
                if(phone.isEmpty()){
                    editText2.setError("Phone number is required!");
                    editText2.requestFocus();
                    return;
                }
                if(email.isEmpty()){
                    editText3.setError("Email is required!");
                    editText3.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editText3.setError("Please provide valid email!");
                    editText3.requestFocus();
                    return;
                }
                if(password1.isEmpty()){
                    editText4.setError("Password is required!");
                    editText4.requestFocus();
                    return;
                }
                if(password1.length() < 6){
                    editText4.setError("Minimum password length should be 6 characters!");
                    editText4.requestFocus();
                    return;
                }
                if(password2.isEmpty()){
                    editText5.setError("Confirm password is required!");
                    editText5.requestFocus();
                    return;
                }
                if(!password1.equals(password2)){
                    editText5.setError("Invalid password!");
                    editText5.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password1)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progress.setVisibility(View.VISIBLE);
                                if(task.isSuccessful()){
                                   User user = new User(name, email, phone);

                                   FirebaseDatabase.getInstance().getReference("Users")
                                           .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                           .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if(task.isSuccessful()){
//                                               redirect to user profile
                                               progress.setVisibility(View.GONE);
                                               Toast.makeText(RegistrationActivity.this, "Congratulations!", Toast.LENGTH_LONG).show();
                                               startActivity(new Intent(RegistrationActivity.this, WelcomeUserActivity.class));
                                           }
                                           else{
                                               Toast.makeText(RegistrationActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                           }
                                       }
                                   });
                                }
                                else{
                                    progress.setVisibility(View.GONE);
                                    try
                                    {
                                        throw task.getException();
                                    }
                                    catch(FirebaseAuthUserCollisionException existEmail)
                                    {
                                        Toast.makeText(RegistrationActivity.this, "This email is already used!Please use another email!", Toast.LENGTH_LONG).show();
                                    }
                                    catch(Exception e)
                                    {
                                        Toast.makeText(RegistrationActivity.this, "Failed to register!Try again later!", Toast.LENGTH_LONG).show();
                                    }

                                }
                            }
                        });
            }
        });

    }
}


