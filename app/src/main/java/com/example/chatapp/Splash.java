package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.chatapp.Base.BaseActivity;
import com.example.chatapp.FirebaseUtils.Model.User;
import com.google.gson.Gson;


public class Splash extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_splash) ;
        String userJson=getString("user");
        if (userJson!=null){
            Gson gson=new Gson();
            DataHolder.currentUser=gson.fromJson(userJson, User.class);
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(activity,HomeActivity.class));
                            finish();
                        }
                    },2000);
        }else {
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(activity,Registration.class));
                                finish();
                            }
                        },2000);
            }

        }

}
