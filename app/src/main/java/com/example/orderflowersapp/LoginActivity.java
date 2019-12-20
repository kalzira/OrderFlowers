package com.example.orderflowersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.orderflowersapp.Common.Common;
import com.example.orderflowersapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);

        btnLogin =findViewById(R.id.login_btn);

        //Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(LoginActivity.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Check if user exists in database
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            //Get user info
                            mDialog.dismiss();
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);

                            user.setPhone(edtPhone.getText().toString()); //Set Phone
                            if((user.getPhone().equals("0000"))&&(user.getPassword().equals("admin"))){
                                Intent homeIntent= new Intent(LoginActivity.this, HomeAdmin.class);
//                                homeIntent.putExtra("AdminId","AdminId");
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                            }
                            else if (!user.getPhone().equals("0000") && !user.getPassword().equals("admin")&& (user.getPassword().equals(edtPassword.getText().toString()))) {
                                Toast.makeText(LoginActivity.this, "You are welcome", Toast.LENGTH_SHORT).show();
                                Intent homeIntent= new Intent(LoginActivity.this, HomeActivity.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                            }

                            else {
                                Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            mDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Pnone number is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
