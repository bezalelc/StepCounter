package com.yehonatand_bezalelc.stepcounter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DBPipeLine {
    final private FirebaseFirestore db;
    String username, password;
    private FirebaseAuth mAuth;


    public DBPipeLine(FirebaseFirestore db, String username, String password) {
        this.db = FirebaseFirestore.getInstance();
        this.username = username;
        this.password = password;

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }


}
