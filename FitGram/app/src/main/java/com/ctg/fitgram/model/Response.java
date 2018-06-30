package com.ctg.fitgram.model;

/**
 * Created by syeds on 11/25/2017.
 */


public class Response
{

    private String message;
    private String token;
    private String path;
    private User[] data;



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

    public User[] getData() { return  data;}




    public void setMessage(String message)
    {
        this.message = message;
    }
}