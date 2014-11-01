package com.example.david.yodown;

/**
 * Created by xueqi on 11/1/14.
 */
public class User {
    private String userName;
    private int uniqueId;
    private double longitude;
    private double latitude;

    public User( int uniqueId, String userName){
        this.userName = userName;
        this.uniqueId = uniqueId;
    }

    public User( int uniqueId, String userName, double longitude, double latitude) {
        this(uniqueId, userName);
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public int getUniqueId(){
        return this.uniqueId;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public String getUserName(){
        return this.userName;
    }

    @Override
    public String toString(){
        return getUserName();
    }

}
