package com.example.compendiumofmateriamedica.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.compendiumofmateriamedica.MainActivity;
import com.example.compendiumofmateriamedica.R;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.ArrayList;
import java.util.List;

import model.Adapters.PostAdapter;
import model.Datastructure.Post;
import model.Datastructure.User;

/**
 * @author: Hongjun Xu, Xing Chen
 * @datetime: 2024/5/2
 * @description: A fragment to show the social page in app
 * using HomeViewModel to control the datastream.
 */
public class MyPost extends AppCompatActivity {

    private TextView page_name;
    private ImageView back;
    private PostAdapter postAdapter;
    private RecyclerView postsRecyclerView;
    private User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        page_name=findViewById(R.id.page_name);
        page_name.setText("My Posts");

        // back button
        back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            // when start an activity from a fragment, the fragment never got killed
            // simply come back to original fragment by kill the current activity
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // my posts
        currentUser = (User) this.getIntent().getSerializableExtra("CurrentUser");
        postsRecyclerView = findViewById(R.id.messages_recyclerView);
        List<Post> myPosts = MainActivity.getPostsByUserId(currentUser.getUser_id());
        postAdapter = new PostAdapter(this, myPosts, getSupportFragmentManager());
        postsRecyclerView.setAdapter(postAdapter);

        // set layout of recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        postsRecyclerView.setLayoutManager(layoutManager);

    }
}