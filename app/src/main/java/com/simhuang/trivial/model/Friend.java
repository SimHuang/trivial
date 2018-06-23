package com.simhuang.trivial.model;

/**
 * This represents a friend object that is saved in
 * a user friends list
 */
public class Friend {

    private String userId;
    private String username;

    public Friend() {}

    public Friend(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
