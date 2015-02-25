package com.yegorov.app;

import android.os.Parcel;
import android.os.Parcelable;


public class User implements Parcelable {
    public final static String USER_TAG = "USER_TAG";

    private String userId;
    private String accessToken;
    private String expiresIn;

    public String getUserId() {
        return userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public User(String userId, String accessToken, String expiresIn) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public User(String userId, String accessToken) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.expiresIn = "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(accessToken);
        dest.writeString(expiresIn);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel source) {
        userId = source.readString();
        accessToken = source.readString();
        expiresIn = source.readString();
    }
}
