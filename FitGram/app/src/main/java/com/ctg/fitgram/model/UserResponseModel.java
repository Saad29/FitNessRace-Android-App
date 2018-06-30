package com.ctg.fitgram.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by syeds on 3/5/2018.
 */

public class UserResponseModel {

    @SerializedName("data")
    private User user;
    private String email;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}