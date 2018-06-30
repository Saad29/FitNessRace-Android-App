package com.ctg.fitgram.model;

/**
 * Created by syeds on 3/6/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Running {

    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("distance")
    @Expose
    private Integer distance;

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

}