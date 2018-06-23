package com.simhuang.trivial.model;

/**
 * Quick reference pojo to user
 */
public class UserTag {

    private String username;
    private String uid;
    private String imageURL;

    public UserTag() {}

    public UserTag(String username, String uid, String imageURL) {
        this.username = username;
        this.uid = uid;
        this.imageURL = imageURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageUR(String imageURL) {
        this.imageURL = imageURL;
    }
}
