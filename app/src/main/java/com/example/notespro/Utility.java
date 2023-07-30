package com.example.notespro;


import android.annotation.SuppressLint;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Objects;

public class Utility {

    static CollectionReference getCollectionReferenceForNotes(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notes")
                                               .document(Objects.requireNonNull(currentUser).getUid()).collection("my_notes");

    }



    @SuppressLint("SimpleDateFormat")
    static String timestamptostring(Timestamp timestamp){
       return  new SimpleDateFormat("dd-MM-yyyy (HH:mm:ss)").format(timestamp.toDate());

    }

}
