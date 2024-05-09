package model.Datastructure;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
/*    private List<Integer> likes;
    private List<Integer> likesRecord;
    private Map<Integer, String> comments;*/

    public Post(){}

    public Post(int postId, int uid, int plantId, String photo, String content, String timestamp){
        this.post_id = postId;
        this.user_id = uid;
        this.plant_id = plantId;
        this.photo_url = photo;
        this.content = content;
        this.timestamp = timestamp;
/*        this.likes = new ArrayList<>();
        this.likesRecord = new ArrayList<>();
        this.comments = new LinkedHashMap<>();*/
    }
    public Post(int postId, int uid, int plantId, String photo, String content, String timestamp, List<Integer> likes, List<Integer> likesRecord, Map<Integer, String> comments){
        this.post_id = postId;
        this.user_id = uid;
        this.plant_id = plantId;
        this.photo_url = photo;
        this.content = content;
        this.timestamp = timestamp;
/*        this.likes = likes;
        this.likesRecord = likesRecord;
        this.comments = comments;*/
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

/*    public List<Integer> getLikes() { return likes; }

    public List<Integer> getLikesRecord() { return likesRecord; }

    public Map<Integer, String> getComments() { return comments; }
    // 判断Post现在是否被某用户点赞了
    public boolean isLikedByUser(int uid){
        return this.likes.contains(uid);
    }
    // 判断Post以前是否被某用户点赞过
    public boolean wasLikedByUser(int uid){
        return this.likesRecord.contains(uid);
    }
    // 在Post被用户点赞的时候调用
    public void likedByUser(int uid){
        if (isLikedByUser(uid)) {
            // 如果现在已经被该用户点赞了
            // 取消点赞
            this.likes.remove(Integer.valueOf(uid)); //这里使用Integer.valueOf(uid)防止被认为是索引
        } else {
            // 现在没有被该用户点赞
            // 点赞
            this.likes.add(uid);
            // 如果以前没有被他点赞过，则加入点赞用户记录
            if(!wasLikedByUser(uid)){
                likesRecord.add(uid);
            }
            // 如果以前被他点赞过，说明他是取消了再点的,不用做处理
        }
    }*/

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
                + "Time: " + timestamp + ", ";
   /*             + "Likes: " + likes.toString() + ", "
                + "Comments: " + comments.toString() + "}";*/
    }
}