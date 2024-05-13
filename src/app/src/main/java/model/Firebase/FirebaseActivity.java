package model.Firebase;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.compendiumofmateriamedica.R;
import com.google.gson.reflect.TypeToken;
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

        // 创建用户账户
//        FirebaseUserImporter userCreator = new FirebaseUserImporter(this);
//        userCreator.createUsersFromJson(R.raw.users);
//
//        Type userListType = new TypeToken<List<User>>(){}.getType();
//        List<User> users = JsonTools.loadListFromJson(this, R.raw.users, userListType);
//        FirebaseDataImporter<User> userImporter = new FirebaseDataImporter<>();
//        userImporter.importJsonToFirestore(users, "users");
//        userImporter.importJsonToFirebase(users, "users");

        // 加载Post列表
        Type postListType = new TypeToken<List<Post>>(){}.getType();
        List<Post> posts = JsonTools.loadListFromJson(this, R.raw.posts, postListType);
        FirebaseDataImporter<Post> postImporter = new FirebaseDataImporter<>();
        postImporter.importJsonToFirestore(posts, "posts");
        postImporter.importJsonToFirebase(posts,"posts2");

        // 加载Plant列表
//        Type plantListType = new TypeToken<List<Plant>>(){}.getType();
//        List<Plant> plants = JsonTools.loadListFromJson(this, R.raw.plants, plantListType);
//        FirebaseDataImporter<Plant> plantImporter = new FirebaseDataImporter<>();
//        plantImporter.importJsonToFirestore(plants, "plants");
//        plantImporter.importJsonToFirebase(plants, "plants");
    }
}