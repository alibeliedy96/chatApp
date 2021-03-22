package com.example.chatapp.FirebaseUtils;

import com.example.chatapp.DataHolder;
import com.example.chatapp.FirebaseUtils.Model.Message;
import com.example.chatapp.FirebaseUtils.Model.Room;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MessagesDao {

    public static final String messagesBranch="messages";
    public static DatabaseReference getMessagesRef(){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child(messagesBranch);
        ref.child(DataHolder.currentUser.getId());
        return ref;
    }

//    DatabaseReference ref = FirebaseDatabse.getInstance().getReference().child("messages");
//
//    ref.child(user.getUid()).setValue(Post.class);


    public static void sendMessage(Message message, OnSuccessListener onSuccessListener,
                                   OnFailureListener onFailureListener){
        DatabaseReference messageNode=getMessagesRef()
                .push();
        message.setId(messageNode.getKey());
        messageNode.setValue(message)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);

    }

    public static Query getMessagesByRoomId(String roomId){

        Query query= getMessagesRef()
                .orderByChild("roomId")
                .equalTo(roomId);
        return query;
    }
}
