package com.yegorov.app;

import android.graphics.Bitmap;

public class UserInfo {
    private String name;
    private String profileUrl;
    private String photoUrl;
    private Bitmap bitmap;

    public UserInfo() {
        this.name = "";
        this.profileUrl = "";
        this.photoUrl = "";
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
