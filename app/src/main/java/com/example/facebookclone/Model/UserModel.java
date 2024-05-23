package com.example.facebookclone.Model;

public class UserModel {
    private String username, email, password;
    private String coverBanner;
    private String profileImage;
    private String UserId;
    private int followerCount;
    private String lastMessenger;

    public UserModel() {
    }
    // user sign in
    public UserModel(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    //

    public UserModel(String username, String email, String password, String coverBanner, String profileImage, String userId, int followerCount, String lastMessenger) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.coverBanner = coverBanner;
        this.profileImage = profileImage;
        this.UserId = userId;
        this.followerCount = followerCount;
        this.lastMessenger = lastMessenger;
    }

    public String getLastMessenger() {
        return lastMessenger;
    }

    public void setLastMessenger(String lastMessenger) {
        this.lastMessenger = lastMessenger;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getCoverBanner() {
        return coverBanner;
    }

    public void setCoverBanner(String coverBanner) {
        this.coverBanner = coverBanner;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }
}
