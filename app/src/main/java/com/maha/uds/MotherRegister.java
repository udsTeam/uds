package com.maha.uds;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.maha.uds.Model.AccountModel;

public class MotherRegister extends AppCompatActivity {




    private EditText motherName_text;
    private EditText phoneNum_text;
    private EditText email_text;
    private EditText password_text;
    private EditText confirmPassword_text;
    private TextView signIn;
    private Button register;
    private FirebaseAuth mFirebaseAuth;
    private ProgressDialog mProgressDialog;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mother_register);
        // firebase authentication !!
        mFirebaseAuth = FirebaseAuth.getInstance();
        // progress dialog for the wait time
        mProgressDialog = new ProgressDialog(this);
        // connect all the views from the XML layout
        setUIview();
        // set the actoion for the two buttons
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MotherRegister.this, SignIn.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidate()) {
                    // get the data form the fields
                    final String userName = motherName_text.getText().toString().trim();
                    final String userEmail = email_text.getText().toString().trim();
                    final String userPassword = password_text.getText().toString().trim();
                    final String phoneNum = phoneNum_text.getText().toString().trim();
                    final String userBio = null;
                    final String age = null;
                    final int ratting =0;
                    final String status = null;
                    final String accountType = "mother";

                    mProgressDialog.setMessage("Singing Up");
                    mProgressDialog.show();
                    mFirebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                //adding the user information to the realtiem database
                                UID = mFirebaseAuth.getCurrentUser().getUid();
                                AccountModel account = new AccountModel(userEmail,accountType,userName,userBio,phoneNum,ratting,age,status);
                                FirebaseDatabase.getInstance().getReference("accounts").child(UID).setValue(account);
                                sendEmailVer();
                            } else {
                                Toast.makeText(MotherRegister.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }


    private void setUIview() {

        motherName_text = findViewById(R.id.username_text);
        phoneNum_text = findViewById(R.id.phone_text);
        email_text = findViewById(R.id.email_text);
        password_text = findViewById(R.id.password_text);
        confirmPassword_text = findViewById(R.id.confirm_password_text);
        signIn = findViewById(R.id.signIn_btn);
        register = findViewById(R.id.register_btn);
    }

    private boolean isValidate() {
        boolean cheack = false;
        // reading data from text field
        final String motherName = motherName_text.getText().toString();
        final String phoneNum = phoneNum_text.getText().toString();
        final String email = email_text.getText().toString();
        final String password = password_text.getText().toString();
        final String passCong = confirmPassword_text.getText().toString();

        if (TextUtils.isEmpty(motherName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passCong)
                ||TextUtils.isEmpty(phoneNum)) {
            Toast.makeText(MotherRegister.this, "all fields are required ", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 8) {
            Toast.makeText(MotherRegister.this, "the password should be at least 8 digits", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(passCong)) {
            Toast.makeText(MotherRegister.this, "the two password dose not match", Toast.LENGTH_SHORT).show();
        } else if(!email.contains("@aou.edu.sa")){
            Toast.makeText(MotherRegister.this, "enter your university email", Toast.LENGTH_SHORT).show();

        }  else{
            cheack = true;
        }
        return cheack;
    }

    private void sendEmailVer() {
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mProgressDialog.dismiss();
                        Toast.makeText(MotherRegister.this, "Successfully registered, please confirm your email", Toast.LENGTH_LONG).show();
                        mFirebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(MotherRegister.this, SignIn.class));
                    } else {
                        Toast.makeText(MotherRegister.this, "Registration failed ", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }



}
