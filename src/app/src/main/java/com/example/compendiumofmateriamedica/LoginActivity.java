package com.example.compendiumofmateriamedica;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.Datastructure.DataType;
import model.Datastructure.Plant;
import model.Datastructure.PlantTreeManager;
import model.Datastructure.Post;
import model.Datastructure.PostTreeManager;
import model.Datastructure.RBTree;
import model.Datastructure.RBTreeNode;
import model.Datastructure.User;
import model.Datastructure.UserTreeManager;
import model.FirebaseAuthManager;
import model.GeneratorFactory;

/**
 * @author: Tianhao Shan
 * @datetime: 2024/4/24
 * @description: Login Page
 */

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;



    private RBTree<User> userTree;
    private RBTree<Plant> plantTree;
    private RBTree<Post> postTree;
    // 开发用的，这行可删
    private UserTreeManager userTreeManager;
    private PostTreeManager postTreeManager;
    private PlantTreeManager plantTreeManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.submit(() -> {
//            Log.i("哈哈哈哈", "h哈哈哈哈哈啊");
//            // 执行耗时操作
//            DataInitial();
//            performTimeConsumingOperation();
//            //Log.i("user", userTree.toString());
//
//            // 这里可以使用 runOnUiThread 来更新UI，如果需要
//            runOnUiThread(() -> {
//                // 更新UI操作
//            });
//        });
//        executor.shutdown();



        // 运行加载数据的函数
        DataInitial();

        //Log.i("user", userTree.toString());

        // 创建user，post和plant的管理类的全局单例
        userTreeManager = UserTreeManager.getInstance(userTree);
        postTreeManager = PostTreeManager.getInstance(postTree);
        plantTreeManager = PlantTreeManager.getInstance(plantTree);

        // Initialize UI elements
        editTextEmail = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Set a click listener for the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve entered email and password
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                // 添加一个全空白时直接跳转
                if (email == null || password == null ||
                    email.isEmpty() || password.isEmpty()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    // 这段可删，XingChen:这里开发的时候默认是一个指定用户登录的吧，传入后面的界面，后面搞好了可以改
                    ArrayList<RBTreeNode<User>> users = userTreeManager.search(UserTreeManager.UserInfoType.ID, 5);
                    if(!users.isEmpty()){
                        User user = users.get(0).getValue();
                        intent.putExtra("User", user);
                    }

                    startActivity(intent);
                    // Failed login
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                } else {

                    // Implement authentication logic here
                    // email:user1@test.com password:111111
                    FirebaseAuth firebaseAuth = FirebaseAuthManager.getInstance().getmAuth();
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            // TODO: 创建一个用户虚拟类class User, 将这个类的putExtra 到 Main 下面，后续会用到
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference usersRef = database.getReference("users");

                            String emailToSearch = email; // 你要查询的电子邮箱地址

                            usersRef.orderByChild("email").equalTo(emailToSearch)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    Log.d("FirebaseTest", "DataSnapshot: " + snapshot.getValue());
                                                    User user = snapshot.getValue(User.class);
                                                    // 处理用户数据，例如打印信息
                                                    Log.d("UserData", "User ID: " + user.getUsername() + ", Username: " + user.getUser_id());
                                                    intent.putExtra("User", user);
                                                    startActivity(intent);
                                                }
                                            } else {
                                                Log.d("UserData", "No user found with email " + emailToSearch);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.w("UserData", "loadUser:onCancelled", databaseError.toException());
                                        }
                                    });
                            /*
                            ArrayList<RBTreeNode<User>> users = userTreeManager.search(UserTreeManager.UserInfoType.EMAIL, email);
                            User user = users.get(0).getValue();*/

                        } else {
                            // Failed login
                            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /*
     * @author: Haochen Gong
     * 加载数据
     */
    private void DataInitial() {
//        userTree = (RBTree<User>) GeneratorFactory.tree(this, DataType.USER, R.raw.users);
//        plantTree = (RBTree<Plant>) GeneratorFactory.tree(this, DataType.PLANT, R.raw.plants);
//        postTree = (RBTree<Post>) GeneratorFactory.tree(this, DataType.POST, R.raw.posts);

       /* DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference plantsRef = FirebaseDatabase.getInstance().getReference("plants");
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot snapShot : snapshot.getChildren()) {
                    User user = snapShot.getValue(User.class);
                    users.add(user);
                    //Log.i("user", user.toString());
                }

                userTree = (RBTree<User>) GeneratorFactory.tree(users, DataType.USER);
                Log.i("post", userTree.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Error loading users: " + error.getMessage());
            }
        });


        plantsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Plant> plants = new ArrayList<>();
                for (DataSnapshot snapShot : snapshot.getChildren()) {
                    Plant plant = snapShot.getValue(Plant.class);
                    plants.add(plant);
                    //Log.i("plant", plant.toString());
                }
                plantTree = (RBTree<Plant>) GeneratorFactory.tree(plants, DataType.PLANT);
                Log.i("plant", plantTree.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Error loading plants: " + error.getMessage());
            }
        });


        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Post> posts = new ArrayList<>();
                for (DataSnapshot snapShot : snapshot.getChildren()) {
                    //Log.i("______", snapShot.getKey());
                    Post post = snapShot.getValue(Post.class);
                    posts.add(post);
                    //Log.i("post", post.toString());
                }
                postTree = (RBTree<Post>) GeneratorFactory.tree(posts, DataType.POST);
                Log.i("post", postTree.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Error loading posts: " + error.getMessage());
            }
        });
    }

    private void performTimeConsumingOperation() {
        // 模拟耗时操作
        try {
            Thread.sleep(2000); // 模拟操作耗时
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Post> posts = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    posts.add(document.toObject(Post.class));
                }
                // 在这里，posts列表已经包含了从Firestore读取的所有Post对象
                // 可以在这里处理这些数据，如更新UI、保存到本地等
                postTree = (RBTree<Post>) GeneratorFactory.tree(posts, DataType.POST);
                Log.i("post", postTree.toString());

            } else {
                Log.w("Firestore", "Error getting documents.", task.getException());
            }
        });

        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<User> users = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    users.add(document.toObject(User.class));
                }
                // 在这里，posts列表已经包含了从Firestore读取的所有Post对象
                // 可以在这里处理这些数据，如更新UI、保存到本地等
                userTree = (RBTree<User>) GeneratorFactory.tree(users, DataType.USER);
                Log.i("post", userTree.toString());

            } else {
                Log.w("Firestore", "Error getting documents.", task.getException());
            }
        });

        db.collection("plants").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Plant> plants = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    plants.add(document.toObject(Plant.class));
                }
                // 在这里，posts列表已经包含了从Firestore读取的所有Post对象
                // 可以在这里处理这些数据，如更新UI、保存到本地等
                plantTree = (RBTree<Plant>) GeneratorFactory.tree(plants, DataType.PLANT);
                Log.i("post", plantTree.toString());

            } else {
                Log.w("Firestore", "Error getting documents.", task.getException());
            }
        });
    }


    public void readDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Post> posts = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    posts.add(document.toObject(Post.class));
                }
                // 在这里，posts列表已经包含了从Firestore读取的所有Post对象
                // 可以在这里处理这些数据，如更新UI、保存到本地等
            } else {
                Log.w("Firestore", "Error getting documents.", task.getException());
            }
        });
    }
}
