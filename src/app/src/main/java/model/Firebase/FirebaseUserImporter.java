package model.Firebase;

import android.content.Context;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class FirebaseUserImporter {
    private FirebaseAuth mAuth;
    private Context mContext; // Context to access application assets

    public FirebaseUserImporter(Context context) {
        this.mAuth = FirebaseAuth.getInstance();
        this.mContext = context;
    }
    public void createUsersFromJson(int resourceId) {
        new Thread(() -> {
            try {
                // 从资源文件中读取JSON
                InputStream is = mContext.getResources().openRawResource(resourceId);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                is.close();

                // 解析JSON数据
                JSONArray usersArray = new JSONArray(builder.toString());
                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject userObject = usersArray.getJSONObject(i);
                    String email = userObject.getString("email");
                    String password = userObject.getString("password");
                    createUser(email, password);
                }
            } catch (IOException | JSONException e) {
                Log.e("UserImporter", "Error parsing JSON or reading file", e);
            }
        }).start();
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // 注册成功
                        FirebaseUser user = mAuth.getCurrentUser();
                        System.out.println("User created successfully: " + user.getEmail());
                    } else {
                        // 如果注册失败，显示一个消息给用户
                        System.out.println("Failed to create user: " + task.getException().getMessage());
                    }
                });
    }

}
