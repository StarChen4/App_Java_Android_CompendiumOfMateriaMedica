package model.Datastructure;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * @author Haochen Gong
 * User class
 **/
public class User implements Comparable<User>, Serializable {
    private int user_id;
    private String username;
    private String email;
    private String password;
    private String avatar_url;

    public User(){}

    public User(int id, String name, String email, String password, String avatar){
        this.user_id = id;
        this.username = name;
        this.email = email;
        this.password = password;
        this.avatar_url = avatar;
    }

    public int getUser_id(){
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

//    public void setAvatar_url(String avatar_url){ this.avatar_url=avatar_url;}

//    public void setUsername (String username){ this.username=username;}

    @Override
    public int compareTo(User user) {
        return user_id - user.user_id;
    }


    @NonNull
    @Override
    public String toString() {
        return "{UsrID: " + user_id + ", "
                + "Name: " + username + ", "
                + "Email: " + email + ", "
                + "Password: " + password + ", "
                + "AvatarUrl: " + avatar_url + "}";
    }

    public void setValue(User user) {
        this.user_id=user.user_id;
        this.email=user.email;
        this.avatar_url=user.avatar_url;
        this.password=user.password;
        this.username=user.username;
    }

    public void setAvatar_url(String url) {
        this.avatar_url=url;
    }

    public void setUsername(String uname) {
        this.username=uname;
    }
}