package com.example.airmonitorizer2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminPageActivity extends AppCompatActivity {
    private View users;
    private DatabaseReference rootRef, rootRef2;
    private FirebaseUser user;
    private String userID;
    private View title;
    private TextView welcome;
    private TextView usersInfo;
    private String allUsersInfo = "";
    private View admins;
    private TextView adminsInfo;
    private String allAdminsInfo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        users = (View) findViewById(R.id.allUsers);
        user = FirebaseAuth.getInstance().getCurrentUser();
        rootRef  = FirebaseDatabase.getInstance().getReference("Users");
        rootRef2  = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        title = (View) findViewById(R.id.titleBar);
        welcome = (TextView)title.findViewById(R.id.welcomeText);
        usersInfo = (TextView) users.findViewById(R.id.usersInfo);
        admins = (View) findViewById(R.id.allAdmins);
        adminsInfo = (TextView) admins.findViewById(R.id.adminsInfo); 


        rootRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    welcome.setText("          Welcome, " + userProfile.fullname + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(adminPageActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        final Button buttonSignOut = findViewById(R.id.buttonSignOutAdmin);
        buttonSignOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(adminPageActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        final Button allUsers = findViewById(R.id.viewProfiles);
        allUsers.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                users.setVisibility(View.VISIBLE);
                admins.setVisibility(View.INVISIBLE);
                allUsersInfo = "";
                rootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            User databaseUser = postSnapshot.getValue(User.class);
                            if(!postSnapshot.getKey().equals(userID) && !databaseUser.admin.equals("yes")){
                                allUsersInfo += "Name: "+ databaseUser.fullname+"\n";
                                allUsersInfo += "Phone: "+ databaseUser.phone+"\n";
                                allUsersInfo += "Email: "+ databaseUser.email+"\n";
                                allUsersInfo += "Diseases: "+ databaseUser.diseases+"\n";
                                allUsersInfo += "Parameters: "+ databaseUser.parameters+"\n\n";
                            }
                        }
                        usersInfo.setText(allUsersInfo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(adminPageActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        final Button allAdmins = findViewById(R.id.viewAdmins);
        allAdmins.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                users.setVisibility(View.INVISIBLE);
                admins.setVisibility(View.VISIBLE);
                allAdminsInfo = "";
                rootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            User databaseUser = postSnapshot.getValue(User.class);
                            if(!postSnapshot.getKey().equals(userID) && databaseUser.admin.equals("yes")){
                                allAdminsInfo += "Name: "+ databaseUser.fullname+"\n";
                                allAdminsInfo += "Phone: "+ databaseUser.phone+"\n";
                                allAdminsInfo += "Email: "+ databaseUser.email+"\n\n";
                            }
                        }
                        adminsInfo.setText(allAdminsInfo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(adminPageActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void onBackPressed() {
            Intent a = new Intent(adminPageActivity.this, adminPageActivity.class);
            startActivity(a);
    }
}