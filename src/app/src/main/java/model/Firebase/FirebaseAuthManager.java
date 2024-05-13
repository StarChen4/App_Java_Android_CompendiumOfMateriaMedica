package model.Firebase;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthManager {
    private static FirebaseAuthManager instance = null;
    private FirebaseAuth mAuth;
    private FirebaseAuthManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseAuthManager getInstance(){
        if (instance == null){
            instance = new FirebaseAuthManager();
        }
        return instance;
    }

    public FirebaseAuth getmAuth(){
        return mAuth;
    }
}
