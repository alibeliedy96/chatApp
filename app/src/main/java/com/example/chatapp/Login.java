package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.chatapp.Base.BaseActivity;
import com.example.chatapp.FirebaseUtils.Model.User;
import com.example.chatapp.FirebaseUtils.UserDao;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class Login extends BaseActivity implements View.OnClickListener {

    protected TextInputLayout email;
    protected TextInputLayout password;
    protected Button Login;
    protected TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_login);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Login) {
            String sEmail=email.getEditText().getText().toString();
            final String sPassword=password.getEditText().getText().toString();

            showProgressBar(R.string.loading);
            UserDao.getUsersByEmail(sEmail)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            hideProgressBar();
                            if(!dataSnapshot.hasChildren()){
                                showMessage(R.string.error,R.string.invalide_email_or_password,R.string.ok);
                            }else {
                                for(DataSnapshot object:dataSnapshot.getChildren()){
                                    User user=object.getValue(User.class);
                                    if(user.getPassword().equals(sPassword)){
                                        //redirect to home
                                        DataHolder.currentUser=user;
                                        startActivity(new Intent(activity,HomeActivity.class));
                                        finish();
                                    }
                                }
                                showMessage(R.string.error,R.string.invalide_email_or_password,R.string.ok);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            hideProgressBar();
                            showMessage(getString(R.string.error),databaseError.getMessage(),getString(R.string.ok));

                        }
                    });

        } else if (view.getId() == R.id.register) {
            startActivity(new Intent(activity,Registration.class));
            finish();

        }
    }

    private void initView() {
        email = (TextInputLayout) findViewById(R.id.email);
        password = (TextInputLayout) findViewById(R.id.password);
        Login = (Button) findViewById(R.id.Login);
        Login.setOnClickListener(this);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);
    }
}
