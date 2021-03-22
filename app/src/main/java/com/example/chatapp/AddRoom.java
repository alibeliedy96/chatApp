package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.chatapp.Base.BaseActivity;
import com.example.chatapp.FirebaseUtils.Model.Room;
import com.example.chatapp.FirebaseUtils.RoomDao;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddRoom extends BaseActivity implements OnClickListener {
    protected TextInputLayout roomName;
    protected TextInputLayout roomDesc;
    protected Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        initView();


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add) {
            String name = roomName.getEditText().getText().toString();
            String desc = roomDesc.getEditText().getText().toString();
            if (!name.isEmpty()&&name!=null&& !desc.isEmpty()&&desc!=null){
                Room room = new Room();
                room.setName(name);
                room.setDescription(desc);
                room.setCurrentActiveUsers(0);
                SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                String currentTime = s.format(new Date());
                room.setCreatedAt(currentTime);
                showProgressBar(R.string.loading);
                RoomDao.addNewRoom(room, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        hideProgressBar();
                        showConfirmationMessage(R.string.success, R.string.registered_successfully, R.string.ok
                                , new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        finish();
                                    }
                                }).setCancelable(false);
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressBar();
                        showMessage(getString(R.string.success),
                                e.getMessage(), getString(R.string.ok));
                    }
                });
            }else {
//                showConfirmationMessage(R.string.error, R.string.field_empty, R.string.ok
//                        , new MaterialDialog.SingleButtonCallback() {
//                            @Override
//                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                dialog.dismiss();
//                                finish();
//                            }
//                        }).setCancelable(true);
                showMessage(R.string.error,R.string.field_empty,R.string.ok);
            }

        }
    }
    private void initView(){
        roomName = (TextInputLayout) findViewById(R.id.room_name);
        roomDesc = (TextInputLayout) findViewById(R.id.room_desc);
        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(AddRoom.this);
    }
}
