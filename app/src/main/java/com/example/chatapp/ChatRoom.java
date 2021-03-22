package com.example.chatapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Adapters.ChatThreadAdapter;
import com.example.chatapp.Base.BaseActivity;
import com.example.chatapp.FirebaseUtils.MessagesDao;
import com.example.chatapp.FirebaseUtils.Model.Message;
import com.example.chatapp.FirebaseUtils.Model.Room;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatRoom extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ChatRoom";
    protected RecyclerView recyclerView;
    protected EditText message;
    protected ImageButton send;
    ChatThreadAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    public static Room currentRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_chat_room);
        initView();
        layoutManager = new LinearLayoutManager(activity);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        try {
            adapter = new ChatThreadAdapter(null,DataHolder.currentUser.getId());
        }catch (NullPointerException e){
            Log.d(TAG, " aly onCreate: ");
        }



        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        query=MessagesDao.getMessagesByRoomId(currentRoom.getId());
        query.addChildEventListener(messagesEventListener);



    }
    //using query to get the id of the room
     Query query;
    //using ChildEventListener instead of valueEventListener because we want listen to one new message inserted
    //and valueEventListener listen to whole list
    ChildEventListener messagesEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Message message=snapshot.getValue(Message.class);
            adapter.inserNewItem(message);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    // using onDestroy because when we get out from the activity messagesEventListener do not stay listen
    @Override
    protected void onDestroy() {
        super.onDestroy();
        query.removeEventListener(messagesEventListener);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send) {
            String messsageContent=message.getText().toString();
            Message message=new Message();
            message.setContent(messsageContent);
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            String currentDate=simpleDateFormat.format(new Date());
            message.setSentAt(currentDate);
            message.setSenderId(DataHolder.currentUser.getId());
            message.setSenderName(DataHolder.currentUser.getUsername());
            message.setRoomId(currentRoom.getId());
            MessagesDao.sendMessage(message,onMessageAdded,onMessageSendFail);

        }
    }

    OnSuccessListener onMessageAdded=new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            message.setText("");
        }
    };
    OnFailureListener onMessageSendFail=new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            showMessage(getString(R.string.error),e.getLocalizedMessage(),getString(R.string.ok));
        }
    };

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_chat);
        message = (EditText) findViewById(R.id.message);
        send = (ImageButton) findViewById(R.id.send);
        send.setOnClickListener(ChatRoom.this);
    }
}
