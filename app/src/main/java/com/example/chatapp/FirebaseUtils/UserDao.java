package com.example.chatapp.FirebaseUtils;

import com.example.chatapp.FirebaseUtils.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class UserDao {
    public static final String usersBranch="users";

    public static DatabaseReference getUsersBranch(){
        return FirebaseDatabase.getInstance()
                .getReference(usersBranch);
    }

    public static void AddNewUser(User user, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener){
        DatabaseReference databaseReference=
                getUsersBranch()
                        .push();
        user.setId(databaseReference.getKey());

        databaseReference.setValue(user)
                .addOnFailureListener(onFailureListener)
                .addOnSuccessListener(onSuccessListener);
    }

    public static Query getUsersByEmail(String Email){
        Query query=getUsersBranch()
                .orderByChild("email")
                .equalTo(Email);
        return query;
    }
}