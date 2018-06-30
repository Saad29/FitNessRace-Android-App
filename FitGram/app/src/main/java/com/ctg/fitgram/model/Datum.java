package com.ctg.fitgram.model;

/**
 * Created by syeds on 3/6/2018.
 */
//used for Activity Data mainly
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class Datum {

    @SerializedName("walking")
    @Expose
    private Walking walking;
    @SerializedName("running")
    @Expose
    private Running running;
    @SerializedName("biking")
    @Expose
    private Biking biking;
    @SerializedName("dat")
    @Expose
    private String dat;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("date")
    @Expose
    private String date;

    private JSONObject loc;

    public Walking getWalking() {
        return walking;
    }

    public void setWalking(Walking walking) {
        this.walking = walking;
    }

    public Running getRunning() {
        return running;
    }

    public void setRunning(Running running) {
        this.running = running;
    }

    public Biking getBiking() {
        return biking;
    }

    public void setBiking(Biking biking) {
        this.biking = biking;
    }

    public String getDat() {
        return dat;
    }

    public void setDat(String dat) {
        this.dat = dat;
    }



    public JSONObject getloc() {
        return loc;
    }

    public void setloc(JSONObject loc) {
        this.loc = loc;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}