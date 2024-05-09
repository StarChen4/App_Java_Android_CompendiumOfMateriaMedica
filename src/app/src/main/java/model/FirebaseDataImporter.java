package model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import model.Datastructure.Plant;
import model.Datastructure.Post;
import model.Datastructure.User;

public class FirebaseDataImporter {

    public void importJsonToFirebase(String json, String path) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postsRef = database.getReference(path);

        // 使用 Gson 解析 JSON 数据
        Gson gson = new Gson();
        Type type = new TypeToken<List<Map<String, Object>>>(){}.getType();
        List<Map<String, Object>> postData = gson.fromJson(json, type);

        // 批量写入数据到 Firebase Database
        for (Map<String, Object> post : postData) {
            // 创建一个新的 post ID
            String postId = postsRef.push().getKey();
            if (postId != null) {
                postsRef.child(postId).setValue(post);
            }
        }
    }
    public void importJsonToFirestore(List<User> users) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int[] successCount = new int[1];
        for (User user : users) {
            db.collection("users").add(user)
                    .addOnSuccessListener(documentReference -> {
                        successCount[0]++;  // 成功上传一个文档，计数器
                        Log.d("Firestore", "DocumentSnapshot added with ID: " + documentReference.getId());
                        // 检查是否所有文档都已处理
                        if (successCount[0] == users.size()) {
                            Log.d("Firestore", "All documents uploaded successfully. Total: " + successCount[0]);
                        }
                    })
                    .addOnFailureListener(e -> Log.w("Firestore", "Error adding document", e));
        }
    }
}