package com.ctg.fitgram.model;

/**
 * Created by syeds on 3/6/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Biking {

    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("diistance")
    @Expose
    private Integer diistance;

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getDiistance() {
        return diistance;
    }

    public void setDiistance(Integer diistance) {
        this.diistance = diistance;
    }

}