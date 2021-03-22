package com.example.chatapp;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.chatapp.Base.BaseActivity;
import com.example.chatapp.FirebaseUtils.Model.User;
import com.example.chatapp.FirebaseUtils.UserDao;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class Registration extends BaseActivity implements View.OnClickListener {
    protected TextInputLayout userName;
    protected TextInputLayout email;
    protected TextInputLayout password;
    protected Button register;
    protected TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initView();


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register) {


            final String sUsername = userName.getEditText().getText().toString();
            final String sEmail = email.getEditText().getText().toString();
            final String sPassword = password.getEditText().getText().toString();
            //validation
            // check if no user name written
            if (sUsername.trim().length() == 0) {
                userName.setError(getString(R.string.required));
                return;
            }
            userName.setError(null);
            if (!isValidEmail(sEmail)) {
                email.setError(getString(R.string.invalidEmail));
                return;
            }
            email.setError(null);
            if (sPassword.length() < 6) {
                password.setError(getString(R.string.passwordError));
                return;
            }
            password.setError(null);

            showProgressBar(R.string.loading);
            UserDao.getUsersByEmail(sEmail)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            hideProgressBar();

                            if (dataSnapshot.hasChildren()) {
                                showMessage(R.string.error, R.string.email_registerd_before, R.string.ok);
                            } else {
                                final User user = new User(sUsername, sEmail, sPassword);
                                registerUser(user);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            hideProgressBar();
                            showMessage(getString(R.string.error), databaseError.getMessage(), getString(R.string.ok));

                        }
                    });


        } else if (view.getId() == R.id.login) {
            startActivity(new Intent(activity,Login.class));
            finish();

        }


    }

    private void registerUser(final User user) {

        showProgressBar(R.string.loading);
        UserDao.AddNewUser(user, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                //startActivity
                hideProgressBar();
                DataHolder.currentUser = user;
                //using gson to save object from sharePreferencees
                Gson gson=new Gson();
                String userJson=gson.toJson(user);
                saveString("user",userJson);
                showConfirmationMessage(R.string.success, R.string.registered_successfully, R.string.ok
                        , new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                startActivity(new Intent(activity, HomeActivity.class));
                                finish();
                            }
                        })
                        .setCancelable(false);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressBar();
                showMessage(getString(R.string.success), e.getMessage(), getString(R.string.ok));

            }
        });
    }

    //to check if email valid
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void initView() {
        userName = (TextInputLayout) findViewById(R.id.user_name);
        email = (TextInputLayout) findViewById(R.id.email);
        password = (TextInputLayout) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(Registration.this);
        login = (TextView) findViewById(R.id.login);
        login.setOnClickListener(Registration.this);
    }
}
