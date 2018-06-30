package com.ctg.fitgram.model;

/**
 * Created by syeds on 3/6/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class Activity {

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
    private String  location;
    @SerializedName("dat")
    @Expose
    private String dat;
    @SerializedName("date")
    @Expose
    private Integer date;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String  location) {
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