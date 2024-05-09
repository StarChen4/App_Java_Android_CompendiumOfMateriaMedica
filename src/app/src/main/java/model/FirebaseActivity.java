package model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.compendiumofmateriamedica.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import model.Datastructure.Plant;
import model.Datastructure.Post;
import model.Datastructure.User;

public class FirebaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

/*        List<Post> posts = loadPostsFromJson(this);
        new FirebaseDataImporter().importJsonToFirestore(posts);*/
        List<User> users = loadPostsFromJson(this);
        new FirebaseDataImporter().importJsonToFirestore(users);

/*        UserCreateFromJson();*/

/*        String json = loadJsonFromAssets(this, "users.json");
        new FirebaseDataImporter().importJsonToFirebase(json, "users");*/

/*        String json = loadJsonFromAssets(this, "plants.json");
        new FirebaseDataImporter().importJsonToFirebase(json, "users");*/

/*        String json = loadJsonFromAssets(this, "posts.json");
        new FirebaseDataImporter().importJsonToFirebase(json, "posts");*/
    }
    public static String loadJsonFromAssets(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public static List<User> loadPostsFromJson(Context context) {
        String json = loadJsonFromAssets(context, "users.json");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<User>>(){}.getType();
        return gson.fromJson(json, listType);
    }
    private void UserCreateFromJson(){
        FirebaseUserImporter importer = new FirebaseUserImporter(this);
        importer.createUsersFromJson(R.raw.users); // 假设文件名为users.json
    }
}