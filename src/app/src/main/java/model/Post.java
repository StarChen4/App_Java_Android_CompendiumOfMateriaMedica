package model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: Haochen Gong
 * @description: Post类
 **/
public class Post implements Comparable<Post>{
    // Start with 1
    private int post_id;
    private int user_id;
    private int plant_id;
    private String photo_url;
    private String content;
    private String timestamp;
    // user id who liked this post
    private List<Integer> likes;
    private Map<Integer, String> comments;

    public Post(){}

    public Post(int postId, int uid, int plantId, String photo, String content, String timestamp){
        this.post_id = postId;
        this.user_id = uid;
        this.plant_id = plantId;
        this.photo_url = photo;
        this.content = content;
        this.timestamp = timestamp;
        this.likes = new ArrayList<>();
        this.comments = new LinkedHashMap<>();
    }
    public Post(int postId, int uid, int plantId, String photo, String content, String timestamp, List<Integer> likes, Map<Integer, String> comments){
        this.post_id = postId;
        this.user_id = uid;
        this.plant_id = plantId;
        this.photo_url = photo;
        this.content = content;
        this.timestamp = timestamp;
        this.likes = likes;
        this.comments = comments;
    }

    public int getPost_id() {
        return post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getPlant_id() {
        return plant_id;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<Integer> getLikes() { return likes; }

    public Map<Integer, String> getComments() { return comments; }

    @Override
    public int compareTo(Post post) {
        return this.timestamp.compareTo(post.timestamp);
    }

    @NonNull
    @Override
    public String toString() {
        return "{PostID: " + post_id + ", "
                + "UsrID: " + user_id + ", "
                + "PlantID: " + plant_id + ", "
                + "PhotoUrl: " + photo_url + ", "
                + "Content: " + content + ", "
                + "Time: " + timestamp + ", "
                + "Likes: " + likes.toString() + ", "
                + "Comments: " + comments.toString() + "}";
    }
}