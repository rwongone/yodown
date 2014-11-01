package com.example.david.yodown;

/**
 * Created by xueqi on 11/1/14.
 */
public class User {
    private double longitude;
    private double latitude;
    private String password;
    private String userName;
    private String uniqueId;

    public User( String uniqueId, String userName, String password){
        this.userName = userName;
        this.uniqueId = uniqueId;
        this.password = password;
    }

    public User( String uniqueId, String userName, String password, double longitude, double latitude) {
        this(uniqueId, userName, password);
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public String getUniqueId(){
        return this.uniqueId;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public String getPassword(){
        return this.password;
    }

    public String getUserName(){
        return this.userName;
    }

    @Override
    public String toString(){
        return getUserName();
    }

}
