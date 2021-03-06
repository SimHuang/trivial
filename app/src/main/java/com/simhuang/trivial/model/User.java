package com.simhuang.trivial.model;

public class User {

    private String uid;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String bio;
    private String imageURI;
    private int token;
    private int gameStreak;
    private int gamesWon;
    private int gamesLost;

    public User() {}

    public User(String uid, String username, String email, String firstName, String lastName, String bio, String imageURI, int token,
                int gameStreak, int gamesWon, int gamesLost) {

        this.uid = uid;
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.imageURI = imageURI;
        this.token = token;
        this.gamesLost = gamesLost;
        this.gamesWon = gamesWon;
        this.gameStreak = gameStreak;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public int getGameStreak() {
        return gameStreak;
    }

    public void setGameStreak(int gameStreak) {
        this.gameStreak = gameStreak;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }
}
