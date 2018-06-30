package com.ctg.fitgram.model;

/**
 * Created by syeds on 3/6/2018.
 */

public class ResponseTimeline {

    private String message;
    private String token;
    private String path;
    private Activity[] data;



    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getPath()
    {
        return path;
    }

    public Activity[] getData() { return  data;}




    public void setMessage(String message)
    {
        this.message = message;
    }
}