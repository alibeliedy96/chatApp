package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.chatapp.Adapters.ChatRoomsAdapter;
import com.example.chatapp.Base.BaseActivity;
import com.example.chatapp.FirebaseUtils.Model.Room;
import com.example.chatapp.FirebaseUtils.RoomDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity  {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ChatRoomsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(activity);
        adapter= new ChatRoomsAdapter(null);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ChatRoomsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, Room room) {
                ChatRoom.currentRoom=room;
                startActivity(new Intent(activity,ChatRoom.class));
            }
        });

        FloatingActionButton fab =  findViewById(R.id.add_room);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity,AddRoom.class));
            }
        });

        RoomDao.getRoomsRef()
                .addValueEventListener(roomsValueEventListener);
    }


    ValueEventListener roomsValueEventListener= new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<Room> rooms = new ArrayList<>();
            for(DataSnapshot roomdata :dataSnapshot.getChildren()){
                Room room = roomdata.getValue(Room.class);
                rooms.add(room);
            }
            adapter.changeData(rooms);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            showMessage(getString(R.string.error),databaseError.getMessage(),getString(R.string.ok));
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RoomDao.getRoomsRef().
                removeEventListener(roomsValueEventListener);

    }
}
