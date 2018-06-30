package com.ctg.fitgram.model;

/**
 * Created by syeds on 3/6/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Walking {

    @SerializedName("steps")
    @Expose
    private Integer steps;
    @SerializedName("distance")
    @Expose
    private Integer distance;
    private Integer time;

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

}