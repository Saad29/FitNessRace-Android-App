package com.ctg.fitgram.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by syeds on 11/25/2017.
 */

public class User
{

    private String name;
    private String email;
    private String city;
    private int age;

    private String password;
    private String created_at;
    private String newPassword;
    private String token;
    private Integer achievements;
    private Integer rewards;
    private Integer distance;
    private Integer time;
    private List<Activity> activity = null;
    @SerializedName("biking")
    @Expose
    private Biking biking;
    @SerializedName("running")
    @Expose
    private Running running;
    @SerializedName("walking")
    @Expose
    private Walking walking;
    @SerializedName("location")
    @Expose
    private JSONObject location;
    @SerializedName("dat")
    @Expose
    private String dat;
    @SerializedName("date")
    @Expose
    private Integer date;




    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCity()
    {
        return city;
    }

    public Integer getAge() {
        return age;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public Integer getAchievements() {
        return achievements;
    }

    public void setAchievements(Integer achievements) {
        this.achievements = achievements;
    }

    public Integer getRewards() {
        return rewards;
    }

    public void setRewards(Integer rewards) {
        this.rewards = rewards;
    }

    public List<Activity> getActivity() {
        return activity;
    }

    public void setActivity(List<Activity> activity) {
        this.activity = activity;
    }

    public Biking getBiking() {
        return biking;
    }

    public void setBiking(Biking biking) {
        this.biking = biking;
    }

    public Running getRunning() {
        return running;
    }

    public void setRunning(Running running) {
        this.running = running;
    }

    public Walking getWalking() {
        return walking;
    }

    public void setWalking(Walking walking) {
        this.walking = walking;
    }

    public JSONObject getLocation() {
        return location;
    }

    public void setLocation(JSONObject location) {
        this.location = location;
    }

    public String getDat() {
        return dat;
    }

    public void setDat(String dat) {
        this.dat = dat;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }
}
